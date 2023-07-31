package com.qr_wm.view.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qr_wm.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BoardAdapter extends ArrayAdapter<BoardData> {
    Context mainContext;
    ArrayList<BoardData> arrayList;

    String[] statusList = {"대기", "사용", "A/S"};

    public BoardAdapter(@NonNull Context context, ArrayList<BoardData> arrayList) {
        super(context, R.layout.list_board_item, arrayList);
        this.mainContext = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_board_item, parent, false);
        }

        BoardData boardData = arrayList.get(position);

        TextView name = convertView.findViewById(R.id.list_name);
        TextView status = convertView.findViewById(R.id.list_status_str);
        TextView user = convertView.findViewById(R.id.list_user);
        ImageView status_img = convertView.findViewById(R.id.list_status_img);
        ImageButton setting = convertView.findViewById(R.id.list_setting);

        name.setText(boardData.getName());
        status.setText(boardData.getStatus());
        user.setText(boardData.getUser());

        if (boardData.getStatus().equals(statusList[0])) {
            status_img.setImageResource(R.drawable.ic_status_idle);
        } else if (boardData.getStatus().equals(statusList[1])) {
            status_img.setImageResource(R.drawable.ic_status_busy);
        } else {
            status_img.setImageResource(R.drawable.ic_status_stop);
        }

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* TODO: setting */
            }
        });

        return convertView;
    }
}