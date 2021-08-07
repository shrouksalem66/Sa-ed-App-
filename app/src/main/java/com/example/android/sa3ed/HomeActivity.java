package com.example.android.sa3ed;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final ArrayList<card> data = new ArrayList<>();
    String nammme;
    int val;
    Integer CNT = 0;
    private static final String TAG = "HomeActivity";
    DatabaseReference reference = FirebaseDatabase.getInstance("https://sa3ed-194e7-default-rtdb.firebaseio.com/").getReference();
    String[] radioarr = new String[2];

    FirebaseAuth auth;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    public static final String[] items = new String[]{"Face Mask", "Hand Sanitizer", "Oxygen Tank", "COVID-19 Medicals", "Car ride"};
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        radioarr[0]="0";
        radioarr[1]="0";
        ImageView homelogo = (ImageView) findViewById(R.id.homelogo);
        ImageView notification = (ImageView) findViewById(R.id.notification);
        ImageView shieldlogo = (ImageView) findViewById(R.id.shieldlogo);
        ImageView userlogo = (ImageView) findViewById(R.id.user1logo);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swip_refresh);
        Button Needhelp = (Button) findViewById(R.id.Needhelp_btn);
        Button Offerhelp = (Button) findViewById(R.id.OfferHelp_btn);
        Button help=(Button) findViewById(R.id.help_btn);
        TextView name = (TextView) findViewById(R.id.Name);
        auth = FirebaseAuth.getInstance();
        Dialog dialog = new Dialog(this);
        if (isServicesOK()) {
            shieldlogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, ShieldActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        userlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Notification_page.class);
                startActivity(intent);
                finish();
            }
        });



        ValueEventListener namelistener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String namee = dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).child("Name").getValue(String.class);
                String[] _fname = namee.split("\\s");
                String fname = _fname[0];
                name.setText(fname+"?");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addValueEventListener(namelistener);


        Needhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.need_help_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final AutoCompleteTextView text = (AutoCompleteTextView) dialog.findViewById(R.id.choosehelp);
                ImageView image_down = dialog.findViewById(R.id.down);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, items);
                text.setAdapter(adapter);
                ImageView imageViewClose = dialog.findViewById(R.id.imageViewClose);
                final EditText quantity = (EditText) dialog.findViewById(R.id.quantity_number);
                final EditText city = (EditText) dialog.findViewById(R.id.city_string);
                final EditText destrict = (EditText) dialog.findViewById(R.id.district_string);
                dialog.show();
                image_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        text.showDropDown();
                    }
                });

                imageViewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });
                final Button postNeed = (Button) dialog.findViewById(R.id.postNEED);
                postNeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                CNT=dataSnapshot.child("Counter").child("Count").getValue(Integer.class);
                                //Toast.makeText(HomeActivity.this,"inside "+CNT,Toast.LENGTH_LONG).show();
                                CNT++;
                                //    reference.child("Count").setValue(1);
                                card d = new card(R.drawable.carpic);
                                String nameee=name.getText().toString();
                                nameee=nameee.substring(0,nameee.length()-1);
                                d.setName(nameee);
                                d.setQuantity(quantity.getText().toString());
                                d.setDestrict(destrict.getText().toString() + ", " + city.getText().toString());
                                d.setStatus("NEED");
                                d.setTime("1 min ago");
                                d.setItem(text.getText().toString());
                                if(text.getText().toString().equals("Face Mask"))
                                {
                                    d.setCardimage(R.drawable.facemask);
                                }
                                else if (text.getText().toString().equals("Hand Sanitizer"))
                                {

                                    d.setCardimage(R.drawable.santizier);
                                }
                                else if (text.getText().toString().equals("Oxygen Tank"))
                                {

                                    d.setCardimage(R.drawable.gastank);
                                }
                                else if (text.getText().toString().equals("COVID-19 Medicals"))
                                {

                                    d.setCardimage(R.drawable.medical);
                                }
                                else if (text.getText().toString().equals("Car ride"))
                                {

                                    d.setCardimage(R.drawable.carpic);
                                }
                                data.add(d);
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Name", nameee);
                                map.put("Quantity", quantity.getText().toString());
                                map.put("District",destrict.getText().toString() + ", " + city.getText().toString());
                                map.put("Status","NEED");
                                Calendar rightNow = Calendar.getInstance();
                                int day=rightNow.get(Calendar.DAY_OF_YEAR);
                                int hour=rightNow.get(Calendar.HOUR_OF_DAY);
                                int minute=rightNow.get(Calendar.MINUTE);
                                map.put("Time",String.valueOf(day)+","+String.valueOf(hour)+","+String.valueOf(minute));
                                map.put("Item",text.getText().toString());
                                reference.child("Cards").child("card"+CNT.toString()).setValue(map);
                                reference.child("Cards").child("card"+CNT).child("UserId").setValue(auth.getCurrentUser().getUid());
                                reference.child("Users").child(auth.getCurrentUser().getUid()).child("UserCards").child("cards").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String value=dataSnapshot.getValue(String.class);
                                        reference.child("Users").child(auth.getCurrentUser().getUid()).child("UserCards").child("cards").setValue(value+CNT+",");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                HashMap<String ,Integer>count=new HashMap<>();
                                count.put("Count",CNT);
                                reference.child("Counter").setValue(count);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });

        Offerhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.offer_help_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                final AutoCompleteTextView text = (AutoCompleteTextView) dialog.findViewById(R.id.choosehelp);
                ImageView image_down = dialog.findViewById(R.id.down);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, items);
                text.setAdapter(adapter);
                ImageView imageViewClose = dialog.findViewById(R.id.imageViewClose);
                final EditText quantity = (EditText) dialog.findViewById(R.id.quantity_number);
                final EditText city = (EditText) dialog.findViewById(R.id.city_string);
                final EditText destrict = (EditText) dialog.findViewById(R.id.district_string);
                dialog.show();
                image_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        text.showDropDown();

                    }
                });

                imageViewClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });

                final Button postOffer = (Button) dialog.findViewById(R.id.postOFFER);
                postOffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                CNT=dataSnapshot.child("Counter").child("Count").getValue(Integer.class);
                                //Toast.makeText(HomeActivity.this,"inside "+CNT,Toast.LENGTH_LONG).show();
                                CNT++;
                                //    reference.child("Count").setValue(1);
                                card d = new card(R.drawable.carpic);
                                String nameee=name.getText().toString();
                                nameee=nameee.substring(0,nameee.length()-1);
                                d.setName(nameee);
                                d.setQuantity(quantity.getText().toString());
                                d.setDestrict(destrict.getText().toString() + ", " + city.getText().toString());
                                d.setStatus("OFFER");
                                d.setTime("1 min ago");
                                d.setItem(text.getText().toString());
                                if(text.getText().toString().equals("Face Mask"))
                                {
                                    d.setCardimage(R.drawable.facemask);
                                }
                                else if (text.getText().toString().equals("Hand Sanitizer"))
                                {
                                    d.setCardimage(R.drawable.santizier);
                                }
                                else if (text.getText().toString().equals("Oxygen Tank"))
                                {
                                    d.setCardimage(R.drawable.gastank);
                                }
                                else if (text.getText().toString().equals("COVID-19 Medicals"))
                                {
                                    d.setCardimage(R.drawable.medical);
                                }
                                else if (text.getText().toString().equals("Car ride"))
                                {
                                    d.setCardimage(R.drawable.carpic);
                                }
                                data.add(d);
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Name", nameee);
                                map.put("Quantity", quantity.getText().toString());
                                map.put("District",destrict.getText().toString() + ", " + city.getText().toString());
                                map.put("Status","OFFER");
                                Calendar rightNow = Calendar.getInstance();
                                int day=rightNow.get(Calendar.DAY_OF_YEAR);
                                int hour=rightNow.get(Calendar.HOUR_OF_DAY);
                                int minute=rightNow.get(Calendar.MINUTE);
                                map.put("Time",String.valueOf(day)+","+String.valueOf(hour)+","+String.valueOf(minute));
                                map.put("Item",text.getText().toString());
                                reference.child("Cards").child("card"+CNT.toString()).setValue(map);
                                reference.child("Cards").child("card"+CNT).child("UserId").setValue(auth.getCurrentUser().getUid());
                                reference.child("Users").child(auth.getCurrentUser().getUid()).child("UserCards").child("cards").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String value=dataSnapshot.getValue(String.class);
                                        reference.child("Users").child(auth.getCurrentUser().getUid()).child("UserCards").child("cards").setValue(value+CNT+",");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                HashMap<String ,Integer>count=new HashMap<>();
                                count.put("Count",CNT);
                                reference.child("Counter").setValue(count);
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        });
        final Button filter=(Button) findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {

            Intent intent=new Intent(HomeActivity.this,FilterActivity.class);
            startActivity(intent);
        }
        });

        final RecyclerView recyclerView;
        final Adapter[] adapter = new Adapter[1];
        /*card d1=new card();
        card d2=new card();
        card d3=new card();
        card d4=new card();
        d1.setName("Shrouk");
        d2.setName("AHD");
        d3.setName("Safea");
        d4.setName("Azab");

        d1.setQuantity("12");
        d2.setQuantity("13");
        d3.setQuantity("14");
        d4.setQuantity("15");

        d1.setTime("1");
        d2.setTime("2");
        d3.setTime("3");
        d4.setTime("4");

        d1.setDestrict("ain, shams");
        d2.setDestrict("abasya, cairo");
        d3.setDestrict("naser, city");
        d4.setDestrict("alahram, giza");

        d1.setStatus("NEED");
        d2.setStatus("NEED");
        d3.setStatus("OFFER");
        d4.setStatus("OFFER");

        d1.setItem("Face Mask");
        d2.setItem("Oxygen Tank");
        d3.setItem("COVID-19 Medical");
        d4.setItem("Face Mask");

        data.add(d1);
        data.add(d2);
        data.add(d3);
        data.add(d4);*/

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=1;
                for(DataSnapshot snapshot:dataSnapshot.child("Cards").getChildren()){
                    card c =new card(R.drawable.carpic);
                    c.setId(i);
                    String userId=dataSnapshot.child("Cards").child("card"+i).child("UserId").getValue(String.class);
                    c.setUserId(userId);
                    String name = dataSnapshot.child("Cards").child("card"+i).child("Name").getValue(String.class);
                    c.setName(name);
                    String district = dataSnapshot.child("Cards").child("card"+i).child("District").getValue(String.class);
                    c.setDestrict(district);
                    String item = dataSnapshot.child("Cards").child("card"+i).child("Item").getValue(String.class);
                    c.setItem(item);
                    if(item.equals("Face Mask"))
                    {
                        c.setCardimage(R.drawable.facemask);
                    }
                    else if(item.equals("Hand Sanitizer"))
                    {
                        c.setCardimage(R.drawable.santizier);
                    }
                    else if(item.equals("Oxygen Tank"))
                    {
                        c.setCardimage(R.drawable.gastank);
                    }
                    else if(item.equals("COVID-19 Medicals"))
                    {
                        c.setCardimage(R.drawable.medical);
                    }
                    else if(item.equals("Car ride"))
                    {
                        c.setCardimage(R.drawable.carpic);
                    }
                    else
                    {
                        c.setCardimage(R.drawable.ic_launcher_background);
                    }
                    String quantity = dataSnapshot.child("Cards").child("card"+i).child("Quantity").getValue(String.class);
                    c.setQuantity(quantity);
                    String time = dataSnapshot.child("Cards").child("card"+i).child("Time").getValue(String.class);
                    String[] listtime = time.split(",");
                    Calendar rightNow = Calendar.getInstance();
                    int day=rightNow.get(Calendar.DAY_OF_YEAR);
                    int hour=rightNow.get(Calendar.HOUR_OF_DAY);
                    int minute=rightNow.get(Calendar.MINUTE);
                    if(String.valueOf(day).equals(listtime[0]) && String.valueOf(hour).equals(listtime[1]))
                    {
                        int timemin=minute-Integer.parseInt(listtime[2]);
                        c.setTime(String.valueOf(timemin)+"min ago.");
                    }
                    else if(String.valueOf(day).equals(listtime[0]) && (!String.valueOf(hour).equals(listtime[1])))
                    {
                        int timehour=hour-Integer.parseInt(listtime[1]);
                        c.setTime(String.valueOf(timehour)+"hour ago.");
                    }
                    else
                    {
                        int timeday=day-Integer.parseInt(listtime[0]);
                        c.setTime(String.valueOf(timeday)+" day ago.");
                    }
                    String status = dataSnapshot.child("Cards").child("card"+i).child("Status").getValue(String.class);
                    c.setStatus(status);
                    data.add(c);
                    adapter[0]=cardsview(radioarr,data);
                    recyclerView.setAdapter(adapter[0]);
                    i++;
                    System.out.println(name);
                    //     Toast.makeText(HomeActivity.this, (k.getName()), Toast.LENGTH_SHORT).show();

                    //  Toast.makeText(HomeActivity.this, (" count  "+ dataSnapshot.child("Cards").getChildrenCount()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        try {
            String Radio_Button=this.getIntent().getExtras().getString("RaioButtn");
            radioarr = substring(Radio_Button);
            adapter[0]=cardsview(radioarr,data);
            recyclerView.setAdapter(adapter[0]);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    recyclerView.setAdapter(adapter[0]);
                    adapter[0].notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        catch(Exception e) {
            adapter[0]= new Adapter(this,data);
            recyclerView.setAdapter(adapter[0]);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    recyclerView.setAdapter(adapter[0]);
                    adapter[0].notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK:checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(HomeActivity.this);
        if (available == ConnectionResult.SUCCESS) { //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK:Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK:an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "We can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    //to sub string in two index of array
    public String[] substring(String Radio) {
        String[] radioarr = new String[2];
        radioarr[0] = Radio.substring(0, 1);
        radioarr[1] = Radio.substring(1);
        return radioarr;
    }

    //radioarr[0] is 1 = items , 2=neeed , 3=offers
//radio[1] is value of radio button
    public Adapter cardsview(String[] radioarr, ArrayList<card> data) {
        //new arrays
        final ArrayList<card> data1 = new ArrayList<>();
        final Adapter adapter;
        //switch of item or need or offer
        switch (radioarr[0]) {
            //items
            case "1":
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getItem().equals(radioarr[1])) {
                        card d = new card(R.drawable.carpic);
                        d.setItem(data.get(i).getItem());
                        d.setName(data.get(i).getName());
                        d.setQuantity(data.get(i).getQuantity());
                        d.setTime(data.get(i).getTime());
                        d.setStatus(data.get(i).getStatus());
                        d.setDestrict(data.get(i).getDestrict());
                        d.setCardimage(data.get(i).getCardimage());
                        data1.add(d);
                    }
                }
                adapter = new Adapter(this,data1);
                break;
            //needs
            case "2":
                //needs and show all needs
                switch (radioarr[1]) {
                    case "Show All Needs":
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getStatus().equals("NEED")) {
                                card d = new card(R.drawable.carpic);
                                d.setItem(data.get(i).getItem());
                                d.setName(data.get(i).getName());
                                d.setQuantity(data.get(i).getQuantity());
                                d.setTime(data.get(i).getTime());
                                d.setStatus(data.get(i).getStatus());
                                d.setDestrict(data.get(i).getDestrict());
                                d.setCardimage(data.get(i).getCardimage());
                                data1.add(d);
                            }
                        }
                        adapter = new Adapter(this, data1);
                        break;

                    default:
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getItem().equals(radioarr[1]) && data.get(i).getStatus().equals("NEED")) {
                                card d = new card(R.drawable.carpic);
                                d.setItem(data.get(i).getItem());
                                d.setName(data.get(i).getName());
                                d.setQuantity(data.get(i).getQuantity());
                                d.setTime(data.get(i).getTime());
                                d.setStatus(data.get(i).getStatus());
                                d.setDestrict(data.get(i).getDestrict());
                                d.setCardimage(data.get(i).getCardimage());
                                data1.add(d);
                            }
                        }
                        adapter = new Adapter(this, data1);
                }
                break;
            //offers
            case "3":
                //offer and show all offers
                switch (radioarr[1]) {
                    case "Show All Offers":
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getStatus().equals("OFFER")) {
                                card d = new card(R.drawable.carpic);
                                d.setItem(data.get(i).getItem());
                                d.setName(data.get(i).getName());
                                d.setQuantity(data.get(i).getQuantity());
                                d.setTime(data.get(i).getTime());
                                d.setStatus(data.get(i).getStatus());
                                d.setDestrict(data.get(i).getDestrict());
                                d.setCardimage(data.get(i).getCardimage());
                                data1.add(d);
                            }
                        }
                        adapter = new Adapter(this, data1);
                        break;

                    default:
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getItem().equals(radioarr[1]) && data.get(i).getStatus().equals("OFFER")) {
                                card d = new card(R.drawable.carpic);
                                d.setItem(data.get(i).getItem());
                                d.setName(data.get(i).getName());
                                d.setQuantity(data.get(i).getQuantity());
                                d.setTime(data.get(i).getTime());
                                d.setStatus(data.get(i).getStatus());
                                d.setDestrict(data.get(i).getDestrict());
                                d.setCardimage(data.get(i).getCardimage());
                                data1.add(d);
                            }
                        }
                        adapter = new Adapter(this, data1);
                }
                break;

            default:
                adapter = new Adapter(this, data);
        }

        return adapter;
    }


}
