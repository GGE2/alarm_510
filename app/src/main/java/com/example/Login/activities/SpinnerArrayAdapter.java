package com.example.Login.activities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.annotations.NotNull;


public class    SpinnerArrayAdapter extends ArrayAdapter<String> {

    Context context;
    String[] items = new String[]{};


    public SpinnerArrayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.items = objects;
        this.context = context;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getDropDownView(int position, @Nullable @org.jetbrains.annotations.Nullable View convertView, @NonNull @NotNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);

        }
        TextView text = (TextView) convertView.findViewById(android.R.id.text1);

            text.setText(items[position]);
            text.setBackgroundColor(Color.WHITE);
            text.setTextColor(Color.BLACK);
            text.setTextSize(15);



        return convertView;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
