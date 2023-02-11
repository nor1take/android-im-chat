package com.example.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.pojo.Dialog;

import java.util.List;

public class DialogAdpter extends ArrayAdapter<Dialog> {
    private int resourceId;
    private List<Dialog> dialogList;

    public DialogAdpter(Context context, int textViewResourceId, List<Dialog> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        dialogList = objects;
    }

    public void updateListView(List<Dialog> list) {
        dialogList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Dialog dialog = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.uid = view.findViewById(R.id.uid);
            viewHolder.content = view.findViewById(R.id.content);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.uid.setText(dialog.getUidUser());
        viewHolder.content.setText(dialog.getMessage());
        return view;
    }

    class ViewHolder {
        TextView uid;
        TextView content;
    }
}
