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


public class needsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view2 =inflater.inflate(R.layout.fragment_needs, container, false);
        final RadioGroup radioGroup2;
        Button show2;
        radioGroup2=view2.findViewById(R.id.radiogroup_needs);
        show2 = view2.findViewById(R.id.ShowResult_need);
        final RadioButton[] radioButton2 = new RadioButton[1];
        show2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int r_id = radioGroup2.getCheckedRadioButtonId();
                radioButton2[0] = view2.findViewById(r_id);
               // Toast.makeText(view2.getContext(), radioButton2[0].getText()+"NEEDS",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getContext(),HomeActivity.class);
                //send value 2 to filter all posts by status need
                i.putExtra("RaioButtn","2"+radioButton2[0].getText().toString());
                startActivity(i);
            }

        });
        return view2;
    }
}