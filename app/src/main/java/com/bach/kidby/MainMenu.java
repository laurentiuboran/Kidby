//documented

package com.bach.kidby;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    private ListView listView;

    private MainMenuAdapter mmAdapter;

    protected void onCreate(Bundle readInstanceState) {
        super.onCreate(readInstanceState);

        setContentView(R.layout.main_menu);

        getSupportActionBar().setTitle("Bun venit la Kidby!");

        final ArrayList<Menu> array_list = new ArrayList<Menu>();

        Menu m1 = new Menu("Jocul obiectelor", "Hai sa învățăm cuvinte noi!", GameActivity.class);
        Menu m2 = new Menu("Puzzle", "Hai să recunoaștem obiectele învățate!", PuzzleGameActivity.class);
        array_list.add(m1);
        array_list.add(m2);

        listView = findViewById(R.id.menuList);

        mmAdapter = new MainMenuAdapter(MainMenu.this, R.layout.custom_menu_element, array_list);
        listView.setAdapter(mmAdapter);
    }

    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
