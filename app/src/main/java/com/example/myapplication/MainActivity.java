package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openCamera(View view) {
        Intent intent=new Intent(getApplicationContext(),CameraUploadActivity.class);
        startActivity(intent);
    }


    public void uploadImage(View view) {
        Intent intent=new Intent(getApplicationContext(),ImageUploadActivity.class);
        startActivity(intent);
    }


    public void sendLocation(View view) {
        Intent intent=new Intent(getApplicationContext(),GetLocationActivity.class);
        startActivity(intent);
    }




}
