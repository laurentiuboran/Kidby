package com.bach.kidby;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameAdapter extends ArrayAdapter<Game> {
    private ArrayList<Game> games;

    public GameAdapter(Context context, int textViewResourceId, ArrayList<Game> games) {
        super(context, textViewResourceId, games);
        this.games = games;
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

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_list_element, null);
        }

        Game i = games.get(position);

        if (i != null) {
            TextView name = v.findViewById(R.id.name_game);
            ImageView image = v.findViewById(R.id.image_game);

            name.setText(i.getName());
            //image.setImageResource(getContext().getResources().getIdentifier(i.getPhoto(), null, getContext().getPackageName()));
            image.setImageBitmap(stringToBitMap(i.getPhoto()));
        }

        return v;
    }
}
