package com.qr_wm.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qr_wm.R;
import com.qr_wm.data.DataAPI;
import com.qr_wm.data.db.BoardDB;
import com.qr_wm.data.db.UserDB;
import com.qr_wm.view.MainView;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;

public class BoardDialog extends Dialog {
    Context mainContext;
    BoardDB boardDB;
    UserDB userDB;
    String[] scanData;

    String[] actionList = {"대여", "반납", "반입", "반출", "입고", "출고"};
    String[] statusList = {"대기", "사용", "A/S"};
    int[] statusMatrix = {1, 0, 0, 2, 0, 0/*-1*/};

    int selectAction;

    TextView sampleTextview;
    Spinner actionSpinner;
    Spinner userSppinner;
    EditText descEditText;
    Button cancelButton;
    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_board);

        sampleTextview = findViewById(R.id.status_dialog_sample);
        actionSpinner = findViewById(R.id.status_dialog_action);
        userSppinner = findViewById(R.id.status_dialog_user);
        descEditText = findViewById(R.id.status_dialog_desc);
        cancelButton = findViewById(R.id.status_dialog_cancel);
        okButton = findViewById(R.id.status_dialog_ok);

        selectAction = getDefalutAction(scanData[1], scanData[2]);
        userDB.updateArrayData(userDB);

        /* Set Dialog View */
        descEditText.setText(boardDB.getData(scanData[1], scanData[2]).get(7));
        sampleTextview.setText(DataAPI.getSetName(scanData[1], scanData[2]));

        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this.mainContext, android.R.layout.simple_spinner_item, actionList);
        actionSpinner.setAdapter(actionAdapter);
        actionSpinner.setSelection(selectAction);

        String[] userStrArr = new String[MainView.userList.size()];
        for (int i = 0; i < userStrArr.length ; i++) {
            userStrArr[i] = MainView.userList.get(i).getName();
        }
        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this.mainContext, android.R.layout.simple_spinner_item, userStrArr);
        userSppinner.setAdapter(userAdapter);
        switch (selectAction) {
            case 0:
                userSppinner.setEnabled(true);
                break;
            case 1:
                String user = boardDB.getUser(scanData[1], scanData[2]);
                for (int j = 0; j < userStrArr.length ; j++) {
                    if (userStrArr[j].equals(user)) {
                        userSppinner.setSelection(j);
                    }
                }
                userSppinner.setEnabled(false);
                break;
            default:
                userSppinner.setEnabled(false);
                break;
        }

        /* Click Listener */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = String.valueOf(new Date().getTime());
                int position = actionSpinner.getSelectedItemPosition();
                switch (position) {
                    case 0: // 대여
                    case 1: // 반납
                    case 2: // 반입
                    case 3: // 반출
                        String user;
                        if (position == 0) {
                            user = userSppinner.getSelectedItem().toString();
                        } else {
                            user = "NONE";
                        }
                        ArrayList<String> arrayList = boardDB.getData(scanData[1], scanData[2]);
                        boardDB.updateData(scanData[1], scanData[2], statusList[statusMatrix[position]], user, arrayList.get(5), date, descEditText.getText().toString());
                        break;
                    case 4: // 입고
                        boardDB.insertData(scanData[1], scanData[2], statusList[0], "NONE", date, date, "");
                        break;
                    case 5: // 출고
                        boardDB.deleteData(scanData[1], scanData[2]);
                        break;
                }
                Toast.makeText(mainContext, DataAPI.getSetName(scanData[1], scanData[2]) + " '" + actionList[position] +"' 되었습니다.", Toast.LENGTH_LONG).show();
                dismiss();

                /* Update List */
                boardDB.updateArrayData(boardDB);
                MainView.boardAdapter.notifyDataSetChanged();
            }
        });
    }

    public int getDefalutAction(String name, String number) {
        if (boardDB.getID(name, number) >= 0) {
            String status = boardDB.getStatus(name, number);
            if (status.equals(statusList[0])) {
                return 0;   /* 대여 */
            } else if (status.equals(statusList[1])) {
                return 1;   /* 반납 */
            } else {
                return 2;   /* 반입 */
            }
        }
        return 4;   /* 입고 */
    }

    public BoardDialog(@NonNull Context context, String[] scanData, BoardDB boardDB, UserDB userDB) {
        super(context);
        this.mainContext = context;
        this.scanData = scanData;
        this.boardDB = boardDB;
        this.userDB = userDB;
    }
}