package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    EditText et_firstname,et_lastname,et_contactno,et_email;
    Button btn_submit;
    SPLib splib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        splib=new SPLib(this);
        et_firstname=findViewById(R.id.et_firstname);
        et_lastname=findViewById(R.id.et_lastname);
        et_contactno=findViewById(R.id.et_contactno);
        et_email=findViewById(R.id.et_emailaddress);
        btn_submit=findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splib.sharedpreferences.edit().putString(SPLib.Key.Sp_firstname,et_firstname.getText().toString()).commit();
                splib.sharedpreferences.edit().putString(SPLib.Key.Sp_lastname,et_lastname.getText().toString()).commit();
                splib.sharedpreferences.edit().putString(SPLib.Key.Sp_contactno,et_contactno.getText().toString()).commit();
                splib.sharedpreferences.edit().putString(SPLib.Key.Sp_email,et_email.getText().toString()).commit();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
