package com.example.android.sa3ed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Notification_page extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    DatabaseReference reference = FirebaseDatabase.getInstance("https://sa3ed-194e7-default-rtdb.firebaseio.com/").getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);
        ArrayList<Notification_Manger> notificationList = new ArrayList<>();
    /*    notificationList.add(new Notification_Manger(R.drawable.facemask,"Safiya","Asked","for","10X Face Mask"));
        notificationList.add(new Notification_Manger(R.drawable.santizier,"Safiya","Offered","you","10X Hand Senitizer"));
        notificationList.add(new Notification_Manger(R.drawable.gastank,"Shrouk","Asked","for","14X Oxygen Tank"));
        notificationList.add(new Notification_Manger(R.drawable.medical,"Shrouk","Offered","you","100X Covied Medical"));
*/

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=1;
                for(DataSnapshot snapshot:dataSnapshot.child("noti_acti").getChildren()){
                    String from_id = dataSnapshot.child("noti_acti").child("noti" + i).child("From").getValue(String.class);
                    if(from_id.equals(auth.getCurrentUser().getUid())){
                        i++;
                        continue;
                    }
                    else {
                        Notification_Manger NM = new Notification_Manger(R.drawable.facemask, "safiya", "need", "you", "10X", "face mask");
                        String from_name = dataSnapshot.child("Users").child(from_id).child("Name").getValue(String.class);
                        NM.setName(from_name);
                        String offered_qua = dataSnapshot.child("noti_acti").child("noti" + i).child("Offered quantity").getValue(String.class);
                        NM.setQuatntity(offered_qua);
                        String card_id = dataSnapshot.child("noti_acti").child("noti" + i).child("Card id").getValue(String.class);
                        String item = dataSnapshot.child("Cards").child(card_id).child("Item").getValue(String.class);
                        NM.setItem(item);
                        String status = dataSnapshot.child("Cards").child(card_id).child("Status").getValue(String.class);
                        if (status.equals("need")) {
                            NM.setStatus("Offer");
                            NM.setYou_for("You");
                        } else {
                            NM.setStatus("Asked");
                            NM.setYou_for("For");
                        }
                        if (item.equals("Face Mask")) {
                            NM.setImg(R.drawable.facemask);
                        } else if (item.equals("Hand Sanitizer")) {
                            NM.setImg(R.drawable.santizier);
                        } else if (item.equals("Oxygen Tank")) {
                            NM.setImg(R.drawable.gastank);
                        } else if (item.equals("COVID-19 Medicals")) {
                            NM.setImg(R.drawable.medical);
                        } else if (item.equals("Car ride")) {
                            NM.setImg(R.drawable.carpic);
                        } else {
                            NM.setImg(R.drawable.ic_launcher_background);
                        }
                  /*      String time = dataSnapshot.child("Cards").child("card"+i).child("Time").getValue(String.class);
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
                        }*/
                        notificationList.add(NM);
                        i++;
                        recyclerView = findViewById(R.id.recyclerView_notification);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(Notification_page.this);
                        adapter = new Notification_adapter(notificationList);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     /*   recyclerView = findViewById(R.id.recyclerView_notification);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new Notification_adapter(notificationList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
*/
    }
    public void onBackPressed() {
        Intent intent=new Intent(Notification_page.this,HomeActivity.class);
        startActivity(intent);
    }
}