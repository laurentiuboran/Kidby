//documented

package com.bach.kidby;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainMenuAdapter extends ArrayAdapter<Menu> {
    private ArrayList<Menu> menuItems;

    Button btnP;

    public MainMenuAdapter(Context context, int textViewResourceId, ArrayList<Menu> menuItems) {
        super(context, textViewResourceId, menuItems);
        this.menuItems = menuItems;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_menu_element, null);
        }

        final Menu i = menuItems.get(position);

        if (i != null) {
            TextView name = v.findViewById(R.id.titleText);
            TextView desc = v.findViewById(R.id.descText);
            ImageView img = v.findViewById(R.id.imageMenu);
            btnP = v.findViewById(R.id.buttonPlay);

            name.setText(i.getName());
            desc.setText(i.getContent());
            if (position == 0) img.setImageResource(R.drawable.joc1);
            else if (position == 1) img.setImageResource(R.drawable.joc2);
        }

        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), i.getClassMenu());
                view.getContext().startActivity(intent);
            }
        });

        return v;
    }
}
