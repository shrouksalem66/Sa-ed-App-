package com.example.android.sa3ed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class FilterActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(FilterActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        final Button filteritems=(Button) findViewById(R.id.btnitemFilter);
        final Button filterneeds=(Button) findViewById(R.id.btnneedfilter);
        final Button filteroffer=(Button) findViewById(R.id.btnofferFilter);
        final Button clear_button=(Button) findViewById(R.id.clear_all);
        Bundle bundle = new Bundle();
        bundle.putInt("0", 0);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_filter, itemsFragment.class, bundle).addToBackStack(null).commit();


        //button filter (items)
        filteritems.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                filteritems.setBackgroundResource(R.drawable.editt);
                filterneeds.setBackgroundResource(R.color.offcolor);
                filteroffer.setBackgroundResource(R.color.offcolor);
                Bundle bundle = new Bundle();
                /* key to open another fragment
                 * value 0 not affect the fragment that shown*/
                bundle.putInt("0", 0);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_filter, itemsFragment.class, bundle).addToBackStack(null).commit();
            }
        });

        //button filter (needs)
        filterneeds.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                filterneeds.setBackgroundResource(R.drawable.editt);
                filteritems.setBackgroundResource(R.color.offcolor);
                filteroffer.setBackgroundResource(R.color.offcolor);
                Bundle bundle = new Bundle();
                /* key to open another fragment
                 * value 0 not affect the fragment that shown*/
                bundle.putInt("0", 0);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_filter, needsFragment.class, bundle).addToBackStack(null).commit();
            }
        });
        //button filter (offer)
        filteroffer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                filteroffer.setBackgroundResource(R.drawable.editt);
                filterneeds.setBackgroundResource(R.color.offcolor);
                filteritems.setBackgroundResource(R.color.offcolor);
                /* key to open another fragment
                 * value 0 not affect the fragment that shown*/
                Bundle bundle = new Bundle();
                bundle.putInt("0", 0);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_filter, offerFragment.class, bundle).addToBackStack(null).commit();

            }
        });


        /*clear _ button
         * clear all data & send default value 00 to set all posts at home activity*/
        clear_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                Intent i=new Intent(FilterActivity.this,HomeActivity.class);
                i.putExtra("RaioButtn","00");
                startActivity(i);
            }
        });
        //button close
        ImageView imageViewClose = findViewById(R.id.imageViewClose);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}