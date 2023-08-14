package com.example.calculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> names;
    private ArrayList<String> bmrs;

    public CustomListAdapter(Context context, ArrayList<String> names, ArrayList<String> bmrs) {
        super(context, 0, names);
        this.context = context;
        this.names = names;
        this.bmrs = bmrs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.name_text);
        TextView bmrTextView = convertView.findViewById(R.id.bmr_text);

        nameTextView.setText(names.get(position));
        bmrTextView.setText(bmrs.get(position));

        return convertView;
    }

    public void removeItem(int position) {
        names.remove(position);
        bmrs.remove(position);
        notifyDataSetChanged();
    }
}
