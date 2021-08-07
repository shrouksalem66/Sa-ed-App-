package com.example.android.sa3ed;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<card> data;
    private Context context;
    DatabaseReference reference = FirebaseDatabase.getInstance("https://sa3ed-194e7-default-rtdb.firebaseio.com/").getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Adapter(Context context, List<card> data){
        this.layoutInflater= LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.cards,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.name.setText(data.get(i).getName());
        viewHolder.X.setText(data.get(i).getQuantity());
        viewHolder.time.setText(data.get(i).getTime());
        viewHolder.status.setText(data.get(i).getStatus());
        viewHolder.item.setText(data.get(i).getItem());
        viewHolder.destrict.setText(data.get(i).getDestrict());
        viewHolder.cardimagee.setImageResource(data.get(i).getCardimage());
        viewHolder.HELPBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.status.getText().toString().equals("NEED")){
                    Intent ii=new Intent(context,offerHelp_btn.class);
                    String s=String.valueOf(data.get(i).getId());
                    ii.putExtra("helpinfo",viewHolder.name.getText().toString()+" need "+viewHolder.X.getText().toString()+" "+viewHolder.item.getText().toString()+"");
                    ii.putExtra("cardnum",s);
                    ii.putExtra("nameowner",viewHolder.name.getText().toString());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(data.get(i).getUserId().equals(dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).getKey())){
                                Toast.makeText(context,"Invalid",Toast.LENGTH_LONG).show();
                            }
                            else{
                                context.startActivity(ii);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if (viewHolder.status.getText().toString().equals("OFFER")){
                    Intent ii=new Intent(context,needHelp_btn.class);
                    String s=String.valueOf(data.get(i).getId());
                    ii.putExtra("helpinfo",viewHolder.name.getText().toString()+" offer "+viewHolder.X.getText().toString()+" "+viewHolder.item.getText().toString());
                    ii.putExtra("cardnum",s);
                    ii.putExtra("nameowner",viewHolder.name.getText().toString());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(data.get(i).getUserId().equals(dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).getKey())){
                                Toast.makeText(context,"Invalid",Toast.LENGTH_LONG).show();
                            }
                            else{
                                context.startActivity(ii);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
        });
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,time,destrict,status,X,item;
        ImageView cardimagee;
        Button HELPBTN;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time=itemView.findViewById(R.id.time);
            destrict=itemView.findViewById(R.id.destrict);
            status=itemView.findViewById(R.id.status);
            X=itemView.findViewById(R.id.x);
            cardimagee=itemView.findViewById(R.id.cardImaGe);
            item=itemView.findViewById(R.id.item);
            HELPBTN = (Button) itemView.findViewById(R.id.help_btn);
        }
    }
}
