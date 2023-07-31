package com.qr_wm.view.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qr_wm.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserAdapter extends ArrayAdapter<UserData> {
    Context mainContext;
    ArrayList<UserData> arrayList;

    public UserAdapter(@NonNull Context context, ArrayList<UserData> arrayList) {
        super(context, R.layout.list_user_item, arrayList);
        this.mainContext = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_user_item, parent, false);
        }

        UserData userData = arrayList.get(position);

        TextView name = convertView.findViewById(R.id.list_user_name);
        TextView phone = convertView.findViewById(R.id.list_user_phone);
        TextView group = convertView.findViewById(R.id.list_user_group);

        name.setText(userData.getName());
        phone.setText(userData.getPhone());
        group.setText(userData.getGroup());

        return convertView;
    }
}
