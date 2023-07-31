package com.qr_wm.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qr_wm.R;
import com.qr_wm.data.DataAPI;
import com.qr_wm.data.db.BoardDB;
import com.qr_wm.data.db.UserDB;
import com.qr_wm.view.dialog.BoardDialog;
import com.qr_wm.view.dialog.UserDialog;
import com.qr_wm.view.listview.BoardAdapter;
import com.qr_wm.view.listview.BoardData;
import com.qr_wm.view.listview.UserAdapter;
import com.qr_wm.view.listview.UserData;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainView extends AppCompatActivity {
    /* mainview data */
    ImageButton switchCamera;
    Spinner typeSpinner;
    ImageButton optionButton;
    static ListView listView;

    /* listview data */
    public static ArrayList<BoardData> boardList = new ArrayList<>();
    public static BoardAdapter boardAdapter;
    public static ArrayList<UserData> userList = new ArrayList<>();
    public static UserAdapter userAdapter;

    /* database */
    BoardDB boardDB;
    UserDB userDB;

    String[] typeList = {"Board List", "Device List", "User List"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        switchCamera = findViewById(R.id.main_camera);
        typeSpinner = findViewById(R.id.main_type);
        optionButton = findViewById(R.id.main_option);
        listView = findViewById(R.id.main_list);

        /* Set View */
        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
        typeSpinner.setAdapter(actionAdapter);

        /* Set EventListener */
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanView();
            }
        });
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshView(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeSpinner .getSelectedItemPosition() == 0 ||
                        typeSpinner .getSelectedItemPosition() == 1) {
                    int position = typeSpinner.getSelectedItemPosition();
                    String fileName = position == 0 ? "board.csv" : "device.csv";
                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE));
                        if (position == 0) {
                            for (int i = 0; i < boardList.size(); i++) {
                                outputStreamWriter.append(boardList.get(i).getAllCSV());
                            }
                        } else {
                            /* TODO: device list... */
                        }
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AlertDialog.Builder msgBuilder = new AlertDialog.Builder(MainView.this)
                            .setMessage("파일 경로 : " + getFileStreamPath(fileName))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {  }
                            });
                    AlertDialog msgDlg = msgBuilder.create();
                    msgDlg.show();
                } else {
                    UserDialog userDialog = new UserDialog(MainView.this, userDB);
                    userDialog.setCancelable(false);
                    userDialog.setCanceledOnTouchOutside(false);
                    userDialog.show();
                }
            }
        });

        /* Set DB */
        boardDB = new BoardDB(this);
        userDB = new UserDB(this);

        scanView();
    }

    public void scanView() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt("Scan the qrcode attached to set or device");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled Scan", Toast.LENGTH_SHORT).show();
                typeSpinner.setSelection(0);
                refreshView(0);
            } else {
                String[] scanData = DataAPI.splitScanData(intentResult.getContents());
                if (DataAPI.checkScanData(scanData)) {
                    if (scanData[0].toUpperCase().equals(BoardDB.PREFIX_BOARD)) {
                        /* MainView Set */
                        typeSpinner.setSelection(0);
                        refreshView(0);

                        /* Popup StatusDialog */
                        scanData[1] = scanData[1].toUpperCase();
                        BoardDialog boardDialog = new BoardDialog(this, scanData, boardDB, userDB);
                        boardDialog.setCancelable(false);
                        boardDialog.setCanceledOnTouchOutside(false);
                        boardDialog.show();
                    } else {
                        /* MainView Set */
                        typeSpinner.setSelection(1);
                        refreshView(1);

                        // TODO: device dialog...
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Data in qr code is abnormal.", Toast.LENGTH_LONG).show();
                    scanView(); /* Retry Scan... */
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void refreshView(int type) {
        if (type == 0) {
            optionButton.setImageResource(R.drawable.ic_button_save);
            boardDB.updateArrayData(boardDB);
            boardAdapter = new BoardAdapter(this, boardList);
            listView.setAdapter(boardAdapter);
            boardAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            optionButton.setImageResource(R.drawable.ic_button_save);
            /* TODO: device type info */
        } else {
            optionButton.setImageResource(R.drawable.ic_button_user);
            userDB.updateArrayData(userDB);
            userAdapter = new UserAdapter(this, userList);
            listView.setAdapter(userAdapter);
            userAdapter.notifyDataSetChanged();
        }
    }
}