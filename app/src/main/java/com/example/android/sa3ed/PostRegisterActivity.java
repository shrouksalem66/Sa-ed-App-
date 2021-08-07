package com.example.android.sa3ed;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostRegisterActivity extends AppCompatActivity {
private Button continuee;
private EditText ssn;
private EditText day;
private EditText month;
private EditText year;
private EditText district;
private EditText city;
private EditText picture;
private RadioButton male;
private RadioButton female;
private FirebaseAuth auth;
private DatabaseReference reference;
private StorageReference storage ;
private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postregister);
        ssn = (EditText) findViewById(R.id.ssn_edittext);
        day = (EditText) findViewById(R.id.day);
        month = (EditText) findViewById(R.id.month);
        year = (EditText) findViewById(R.id.year);
        district = (EditText) findViewById(R.id.district);
        city = (EditText) findViewById(R.id.city);
        male = (RadioButton) findViewById(R.id.button_male);
        female = (RadioButton) findViewById(R.id.button_female);
        continuee = (Button) findViewById(R.id.cont_btn);
        picture=(EditText) findViewById(R.id.picture);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance("https://sa3ed-194e7-default-rtdb.firebaseio.com/").getReference();
        storage = FirebaseStorage.getInstance("gs://sa3ed-194e7.appspot.com/").getReference();
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });

        continuee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssnt = ssn.getText().toString();
                String dayt = day.getText().toString();
                String montht = month.getText().toString();
                String yeart = year.getText().toString();
                String districtt = district.getText().toString();
                String cityt = city.getText().toString();
                String picturet = picture.getText().toString();
                String gendert;
                if (ssn.equals("") || day.equals("") || month.equals("") || year.equals("") || district.equals("") || city.equals("")  || (!female.isChecked() && !male.isChecked())) {
                    Toast.makeText(PostRegisterActivity.this, "Empty Creditianls", Toast.LENGTH_SHORT).show();
                } else if (ssn.length() < 14) {
                    Toast.makeText(PostRegisterActivity.this, "SSN is too short", Toast.LENGTH_SHORT).show();
                }
                else if(dayt.length()>=2 && (Integer.parseInt(dayt)>=31))
                {
                    Toast.makeText(PostRegisterActivity.this, "Day Formation is Wrong" , Toast.LENGTH_SHORT).show();
                }
                else if(montht.length()>=2 && (Integer.parseInt(montht)>=12))
                {
                    Toast.makeText(PostRegisterActivity.this, "Month Formation is Wrong", Toast.LENGTH_SHORT).show();
                }
                else if(yeart.length()>=4 && (Integer.parseInt(yeart)>=2021))
                {
                    Toast.makeText(PostRegisterActivity.this, "Year Formation is Wrong", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (female.isChecked()) {
                        gendert = "female";
                    } else {
                        gendert = "male";
                    }
                    completeRegisterUser(ssnt, dayt, montht, yeart, districtt, cityt,gendert,imageUri);

                }
            }
        });
    }
    private void completeRegisterUser(final String ssn, final String day, final String month, final String year, final String district, final String city, final String gender,final Uri uri) {


        Bundle bundle = getIntent().getExtras();
        String id_google = bundle.getString("Id_Google");
        if (id_google.length() > 1) {
            reference.child("Users").child(id_google).child("SSN").setValue(ssn);
            reference.child("Users").child(id_google).child("Day").setValue(day);
            reference.child("Users").child(id_google).child("Month").setValue(month);
            reference.child("Users").child(id_google).child("Year").setValue(year);
            reference.child("Users").child(id_google).child("District").setValue(district);
            reference.child("Users").child(id_google).child("City").setValue(city);
            reference.child("Users").child(id_google).child("Gender").setValue(gender);
            //reference.child("Users").child(id_google).child("IDuser").setValue(auth.getCurrentUser().getUid());
            final StorageReference fileRef = storage.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            reference.child("Users").child(id_google).child("picture").setValue(uri.toString());
                            //Toast.makeText(PostRegisterActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(PostRegisterActivity.this, "Uploading Failed !", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(PostRegisterActivity.this, HomeActivity.class);

            Toast.makeText(PostRegisterActivity.this, "Enjoy for help people", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } else if (id_google.equals("")) {
            reference.child("Users").child(auth.getCurrentUser().getUid()).child("SSN").setValue(ssn);
            reference.child("Users").child(auth.getCurrentUser().getUid()).child("Day").setValue(day);
            reference.child("Users").child(auth.getCurrentUser().getUid()).child("Month").setValue(month);
            reference.child("Users").child(auth.getCurrentUser().getUid()).child("Year").setValue(year);
            reference.child("Users").child(auth.getCurrentUser().getUid()).child("District").setValue(district);
            reference.child("Users").child(auth.getCurrentUser().getUid()).child("City").setValue(city);
            reference.child("Users").child(auth.getCurrentUser().getUid()).child("Gender").setValue(gender);

            final StorageReference fileRef = storage.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            reference.child("Users").child(auth.getCurrentUser().getUid()).child("picture").setValue(uri.toString());
                           // Toast.makeText(PostRegisterActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   // Toast.makeText(PostRegisterActivity.this, "Uploading Failed !", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(PostRegisterActivity.this, LoginActivity.class);
            Toast.makeText(PostRegisterActivity.this, "Login with Your New Account", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            picture.setText("               Uploded Successful!");
            picture.setTextColor(Color.GREEN);
        }
    }
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    }

