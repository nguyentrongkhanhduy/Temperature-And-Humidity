package com.example.temperature_humidity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> roomName;

    public GridViewAdapter(Context context, List<String> roomName) {
        this.context = context;
        this.roomName = roomName;
    }


    @Override
    public int getCount() {
        return roomName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_button, null);
        TextView tv = (TextView)convertView.findViewById(R.id.tvRoomName);

        tv.setText(roomName.get(position));

        return convertView;
    }
}
