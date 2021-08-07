package com.example.android.sa3ed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class offerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view3=inflater.inflate(R.layout.fragment_offer, container, false);
        final RadioGroup radioGroup3;
        Button show3;
        radioGroup3=view3.findViewById(R.id.radiogroup_offer);
        show3 = view3.findViewById(R.id.ShowResult_offer);
        final RadioButton[] radioButton3 = new RadioButton[1];
        show3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int r_id = radioGroup3.getCheckedRadioButtonId();
                radioButton3[0] = view3.findViewById(r_id);
                Toast.makeText(view3.getContext(), radioButton3[0].getText()+"OFFERS",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getContext(),HomeActivity.class);
                //send value 3 to filter all posts by status offer
                i.putExtra("RaioButtn","3"+radioButton3[0].getText().toString());
                startActivity(i);
            }

        });
        return view3;
    }
}