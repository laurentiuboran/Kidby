package com.bach.kidby;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class GameActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    EditText name;
    private ListView listView;
    private ImageView imageView;

    private String imageUri;

    private static final int pic_id = 123;

    private GameAdapter mAdapter;

    private TextToSpeech t;

    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle readInstanceState) {
        super.onCreate(readInstanceState);

        setContentView(R.layout.game_activity);

        getSupportActionBar().setTitle("Ghicește obiectul!");

        AlertDialog alertDialog2 = new AlertDialog.Builder(GameActivity.this).create();
        alertDialog2.setTitle("Bun venit!");
        alertDialog2.setMessage("Pentru a începe jocul, apasă pe 'Adaugă element', salvează obiectul și începe distracția!");
        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "Super!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog2.show();

        final DatabaseHelper helper = new DatabaseHelper(this);
        final ArrayList<Game> array_list = helper.getAllContacts();

        name = findViewById(R.id.name);
        listView = findViewById(R.id.listView);
        imageView = findViewById(R.id.photoSource);
        pullToRefresh = findViewById(R.id.pullToRefresh);

        t = new TextToSpeech(this, this);

        mAdapter = new GameAdapter(GameActivity.this, R.layout.custom_list_element, array_list);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game toSpeak = (Game)adapterView.getAdapter().getItem(i);
                openDialog(toSpeak.getName(), toSpeak.getPhoto());
                t.setLanguage(new Locale("ro_RO"));
                t.speak(toSpeak.getName(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Game toSpeak = (Game)adapterView.getAdapter().getItem(i);
                helper.deleteContact(toSpeak);
                Toast.makeText(GameActivity.this, "'" + toSpeak.getName() + "' " + "șters", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            int refreshCounter = 1;

            @Override
            public void onRefresh() {
                array_list.clear();
                array_list.addAll(helper.getAllContacts());
                mAdapter.notifyDataSetChanged();
                listView.invalidateViews();
                listView.refreshDrawableState();
                refreshCounter++;
                pullToRefresh.setRefreshing(false);
            }
        });


    findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                findViewById(R.id.save).setVisibility(View.VISIBLE);
                findViewById(R.id.refresh).setVisibility(View.GONE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent
                        = new Intent(MediaStore
                        .ACTION_IMAGE_CAPTURE);

                startActivityForResult(camera_intent, pic_id);
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty()) {
                    if (helper.insert(name.getText().toString(), imageUri)) {
                        Toast.makeText(GameActivity.this, "Inserat", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(GameActivity.this, "Neinserat", Toast.LENGTH_LONG).show();
                    }
                } else {
                    name.setError("Specifică numele obiectului din fotografie");
                }
                name.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                findViewById(R.id.save).setVisibility(View.GONE);
                findViewById(R.id.refresh).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = t.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void openDialog(String text, String photo) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText(text);
        title.setPadding(20, 20, 20, 20);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(30);
        title.setTypeface(null, Typeface.BOLD);
        alertDialog.setCustomTitle(title);

        // Set Message
        ImageView image = new ImageView(this);
        image.setImageBitmap(stringToBitMap(photo));
        image.setPadding(10, 10, 10, 10);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        image.setLayoutParams(params);
        image.getLayoutParams().height = 200;
        image.getLayoutParams().width = 200;
        // Message Properties
        alertDialog.setView(image);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        new Dialog(getApplicationContext());
        alertDialog.show();

        alertDialog.getWindow().setAttributes(lp);
}

    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == pic_id) {

            Bitmap photo = (Bitmap)data.getExtras()
                    .get("data");

            imageUri = bitmapToString(photo);
        }
    }
}
