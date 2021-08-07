package com.example.android.sa3ed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class ShieldActivity extends AppCompatActivity implements OnMapReadyCallback{
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if (mLocationPermission) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

        }
    }
    private static final String TAG="ShieldActivity";
    private static final String FINE_LOCATION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE=1234;
    private static final float DEFAULT_ZOOM=15f;
    private Boolean mLocationPermission=false;
    private GoogleMap mMap;
    private int change;
    private boolean check=false;
    private boolean check2=false;
    private static int val;
    private static int val2;
    FusedLocationProviderClient mFusedLocationProviderClient;
    LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shield);
        ImageView homelogo=(ImageView) findViewById(R.id.homelogo);
        ImageView userlogo=(ImageView) findViewById(R.id.user1logo);
        ImageView wash_hand=(ImageView) findViewById(R.id.wash_hand);
        ImageView drink_hot=(ImageView) findViewById(R.id.drink_hot);
        homelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ShieldActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        userlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ShieldActivity.this,UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
        getlocationPermission();


        change=0;
        Dialog dialog = new Dialog(this);
        wash_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change=1;


                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(ShieldActivity.this,R.style.BottomSheetDialogTheme);
                View bottomSheetView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_wash_hand,(LinearLayout)findViewById(R.id.bottomSheetContainer));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                NumberPicker num= bottomSheetDialog.findViewById(R.id.numberminute);

                TextView minute=bottomSheetDialog.findViewById(R.id.texminute);
                TextView every=bottomSheetDialog.findViewById(R.id.textevery);
                num.setMaxValue(11);
                num.setMinValue(0);
                num.setDisplayedValues(new String[] {"10", "20", "30", "40", "50","60","70","80","90","100","110","120" });
                Switch aSwitch =(Switch) bottomSheetView.findViewById(R.id.switch_on);
                if(!check2){
                    aSwitch.setChecked(false);
                    num.setVisibility(View.INVISIBLE);
                    minute.setVisibility(View.INVISIBLE);
                    every.setVisibility(View.INVISIBLE);
                }else{
                    num.setValue(val2);
                }
                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        {
                            num.setVisibility(View.VISIBLE);
                            minute.setVisibility(View.VISIBLE);
                            every.setVisibility(View.VISIBLE);
                            check2=true;
                            aSwitch.setChecked(true);
                            val2=num.getValue();
                            startAlarm(0 ,  num.getValue());

                        }
                        else
                        {
                            num.setVisibility(View.INVISIBLE);
                            minute.setVisibility(View.INVISIBLE);
                            every.setVisibility(View.INVISIBLE);
                            check2=false;
                            startAlarm(0 ,  0);
                        }
                    }
                });
            }
        });

        drink_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change=2;
                BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(ShieldActivity.this,R.style.BottomSheetDialogTheme);
                View bottomSheetView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_drink_hot,(LinearLayout)findViewById(R.id.bottomSheetContainer));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                NumberPicker num= bottomSheetDialog.findViewById(R.id.numberhour);

                TextView hour=bottomSheetDialog.findViewById(R.id.texthour);
                TextView every=bottomSheetDialog.findViewById(R.id.textevery);
                num.setMaxValue(12);
                num.setMinValue(1);
                Switch aSwitch =(Switch) bottomSheetView.findViewById(R.id.switch_on);
                if(!check){
                    aSwitch.setChecked(false);
                    num.setVisibility(View.INVISIBLE);
                    hour.setVisibility(View.INVISIBLE);
                    every.setVisibility(View.INVISIBLE);
                }else{
                    num.setValue(val);
                }

                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        {
                            num.setVisibility(View.VISIBLE);
                            hour.setVisibility(View.VISIBLE);
                            every.setVisibility(View.VISIBLE);
                            check=true;
                            aSwitch.setChecked(true);
                            val=num.getValue();
                            startAlarm(num.getValue() ,  0);

                        }
                        else
                        {
                            num.setVisibility(View.INVISIBLE);
                            hour.setVisibility(View.INVISIBLE);
                            every.setVisibility(View.INVISIBLE);
                            check=false;
                            startAlarm(0 ,  0);
                        }
                    }
                });
            }
        });
    }

    private void getDeviceLocation()
    {
        mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermission)
            {
                final com.google.android.gms.tasks.Task<Location> location =mFusedLocationProviderClient.getLastLocation();
                ((com.google.android.gms.tasks.Task) location).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        if(task.isSuccessful()&& task.getResult()!=null)
                        {
                            Location currentLocation =(Location) task.getResult();
                            mLatLng=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            movecamera(mLatLng,DEFAULT_ZOOM);
                        }
                        else{
                            Toast.makeText(ShieldActivity.this,"Unable to get Current location",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }catch(SecurityException e)
        {
            Log.e(TAG,"getDeviceLocation: SecurityException "+e.getMessage());
        }
    }

    private void movecamera(LatLng latlng,float zoom)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
    }
    private void initMap()
    {
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(ShieldActivity.this);
    }

    private void getlocationPermission()
    {
        String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                initMap();
                mLocationPermission = true;
            }
            else{
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermission = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermission = false;
                            return;
                        }
                    }
                    mLocationPermission = true;
                    initMap();
                }
            }
        }
    }
    private void startAlarm(int hour, int minute) {
        if(change ==1) {
            Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ShieldActivity.this, 1, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            long timeAtButtonClick = System.currentTimeMillis();
            long tenSecond = 1000 * 60 * (minute+1)*10;
            if (minute==0) {
                alarmManager.cancel(pendingIntent);
                Toast.makeText(ShieldActivity.this, "Cancel Alarm", Toast.LENGTH_SHORT).show();
            }
            else {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecond, tenSecond, pendingIntent);
                Toast.makeText(ShieldActivity.this, "Working", Toast.LENGTH_SHORT).show();
            }
        }else if(change==2) {
            Intent intent = new Intent(getApplicationContext(), AlertReceiver2.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ShieldActivity.this, 1, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            long timeAtButtonClick = System.currentTimeMillis();
            long tenSecond = 1000 * 60 * (hour)*60;

            if (hour==0) {
                alarmManager.cancel(pendingIntent);
                Toast.makeText(ShieldActivity.this, " Cancel Alarm", Toast.LENGTH_SHORT).show();
            }
            else {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecond, tenSecond, pendingIntent);
                Toast.makeText(ShieldActivity.this, "Working", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
