package com.qr_wm.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.qr_wm.R;
import com.qr_wm.data.db.UserDB;
import com.qr_wm.view.MainView;

import androidx.annotation.NonNull;

public class UserDialog extends Dialog {
    Context mainContext;
    UserDB userDB;

    String[] userGroup = {"일반", "관리자"};

    EditText name;
    EditText phone;
    EditText email;
    RadioGroup group;
    Button cancel;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user);

         name = findViewById(R.id.user_name);
         phone = findViewById(R.id.user_phone);
         email = findViewById(R.id.user_email);
         group = findViewById(R.id.user_group);
         cancel = findViewById(R.id.user_cancel);
         save = findViewById(R.id.user_ok);

         /* Click Listener */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("") || phone.getText().toString().equals("")) {
                    Toast.makeText(mainContext, "Please fill in the required fields.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (userDB.getID(name.getText().toString(), phone.getText().toString()) < 0) {
                    int index = group.indexOfChild(findViewById(group.getCheckedRadioButtonId()));
                    userDB.insertData(name.getText().toString(), phone.getText().toString(), email.getText().toString(), userGroup[index]);
                } else {
                    Toast.makeText(mainContext, "This user already exists.", Toast.LENGTH_LONG).show();
                }
                dismiss();

                /* Update List */
                userDB.updateArrayData(userDB);
                MainView.userAdapter.notifyDataSetChanged();
            }
        });
    }

    public UserDialog(@NonNull Context context, UserDB userDB) {
        super(context);
        this.mainContext = context;
        this.userDB = userDB;
    }
}
