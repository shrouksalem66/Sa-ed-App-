package com.example.android.sa3ed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;



public class itemsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 =inflater.inflate(R.layout.fragment_items, container, false);
        final RadioGroup radioGroup1;
        Button show1;
        radioGroup1=view1.findViewById(R.id.radiogroup_item);
        show1 = view1.findViewById(R.id.ShowResult_item);
        final RadioButton[] radioButton1 = new RadioButton[1];
        show1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int r_id = radioGroup1.getCheckedRadioButtonId();
                radioButton1[0] = view1.findViewById(r_id);
                Intent i=new Intent(getContext(),HomeActivity.class);
                //send value 1 to filter all posts by status items
                i.putExtra("RaioButtn","1"+radioButton1[0].getText().toString());
                startActivity(i);
            }
        });
        return view1;
    }
}