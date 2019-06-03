package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.Manifest.permission.SEND_SMS;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class GetLocationActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION=1;
    TextView textlocation;
    LocationManager locationManager;
    ConnectivityManager connectivityManager;
    String lattitude,longitude;
    Location location;
    SPLib splib;

    private static final int REQUEST_SMS = 0;
    private BroadcastReceiver sentStatusReceiver, deliveredStatusReceiver;
    public static int counter=0;
    private Vibrator vibrator;

    boolean doubleBackOnce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        splib=new SPLib(this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textlocation=(TextView)findViewById(R.id.text_location);
        //text1=(TextView)findViewById(R.id.text1);
        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        getInternetMessage();
        getGeoLocation();

        //setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
    }

    //Volume Press Events
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            //textView.setText("Counter : " + String.valueOf(++counter));


            getSmsProgramtically();
            Toast.makeText(this, "SMS Sent Successfully...", Toast.LENGTH_LONG)
                    .show();
            vibrator.vibrate(1500);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            //textView.setText("Counter : " + String.valueOf(--counter));

            getSmsProgramtically();
            Toast.makeText(this, "SMS Sent Successfully...", Toast.LENGTH_LONG)
                    .show();
            vibrator.vibrate(1500);


            return true;
        }

        else {
            return super.onKeyDown(keyCode, event);
        }
    }



    //Checking Internet is ON or OFF
    public void getInternetMessage(){
        connectivityManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)){
            buildAlertMessageNoInternet();
        }else {
            //finish();
        }
    }

    //Getting Location
    public void getGeoLocation(){
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLocation();
        }
    }

    //Accessing current location....
    public  void getLocation()
    {
        if(ActivityCompat.checkSelfPermission(GetLocationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GetLocationActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(GetLocationActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }



        else{
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);



            if(location !=null) {
              //  String vehicle_number=splib.sharedpreferences.getString(SPLib.Key.Sp_textvalue,"Sp_textvalue");
                String firstname=splib.sharedpreferences.getString(SPLib.Key.Sp_firstname,"Sp_Firstname");
                String lastname=splib.sharedpreferences.getString(SPLib.Key.Sp_lastname,"Sp_Lastname");
                double latti = location.getLatitude();
                double longi = location.getLongitude();

                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                //lt.success();
                textlocation.setText("Hello,i am "+firstname+"  "+lastname+"\n please help me \n" + "my current location is \n https://maps.google.com/maps?q="+lattitude+","+longitude+ "\n");
            }else{
                Toast.makeText(this, "Unable to trace your loacation", Toast.LENGTH_LONG).show();
                //lt.error();
            }
        }
    }


    //Getting SMS using our own app
    public void getSmsProgramtically(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasSMSPermission = checkSelfPermission(SEND_SMS);
            if (hasSMSPermission != PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(SEND_SMS)) {
                    showMessageOKCancel("You need to allow access to Send SMS",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[] {SEND_SMS},
                                                REQUEST_SMS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[] {SEND_SMS},
                        REQUEST_SMS);
                return;
            }
            sendMySMS();
        }
    }

    //Getting Permission of sms using alert dialog
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(GetLocationActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void sendMySMS() {

        String strnum1=splib.sharedpreferences.getString(SPLib.Key.Sp_contactno,"Sp_Contactno");
        String message = textlocation.getText().toString();
        //    String strnum1="9527956414";
        //Check if the phoneNumber is empty
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (strnum1.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
            } else {

                SmsManager sms = SmsManager.getDefault();
                // if message length is too long messages are divided
                List<String> messages = sms.divideMessage(message);
                for (String msg : messages) {

                    PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                    PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
                    sms.sendTextMessage(strnum1, null, msg, sentIntent, deliveredIntent);

                }
            }
        }
    }



    //Getting Permission to access sms services of app
    //@RequiresApi(api = Build.VERSION_CODES.DONUT)
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SMS:
                if (grantResults.length > 0 &&  grantResults[0] == PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access sms", Toast.LENGTH_SHORT).show();
                    sendMySMS();

                }else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and sms", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(SEND_SMS)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{SEND_SMS},
                                                        REQUEST_SMS);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    //Getting GPS setting using alert dialogue
    protected void buildAlertMessageNoGps(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Please turn ON your GPS connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert=builder.create();
        alert.show();
    }


    protected void buildAlertMessageNoInternet(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Please enable mobile data manually........")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
                        dialog.cancel();
                    }
                });
                /*.setNegativeButton("", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });*/
        final AlertDialog alert=builder.create();
        alert.show();
    }

    //Message status
    public void onResume() {
        super.onResume();
        sentStatusReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Unknown Error";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Sent Successfully !!";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        s = "Generic Failure Error";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        s = "Error : No Service Available";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        s = "Error : Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        s = "Error : Radio is off";
                        break;
                    default:
                        break;
                }
                Toast.makeText(GetLocationActivity.this, ""+s, Toast.LENGTH_LONG).show();


            }
        };
        deliveredStatusReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String s = "Message Not Delivered";
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        s = "Message Delivered Successfully";
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                Toast.makeText(GetLocationActivity.this, ""+s, Toast.LENGTH_LONG).show();

                /*phoneEditText.setText("");
                messageEditText.setText("");*/
            }
        };
        registerReceiver(sentStatusReceiver, new IntentFilter("SMS_SENT"));
        registerReceiver(deliveredStatusReceiver, new IntentFilter("SMS_DELIVERED"));
    }


    public void onPause() {
        super.onPause();
        unregisterReceiver(sentStatusReceiver);
        unregisterReceiver(deliveredStatusReceiver);
    }

    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), SEND_SMS ) == PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{SEND_SMS}, REQUEST_SMS);
    }



}