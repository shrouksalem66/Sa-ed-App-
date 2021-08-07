package com.example.android.sa3ed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText re_password;
    private EditText phone;
    private Button reg_btn;
    private TextView already;
    private FirebaseAuth auth; //2
    private GoogleSignInClient googleApiClient; //1
    private static final int SIGN_UP = 1;
    private ImageView google_icon;
    private DatabaseReference reference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.username_edittext);
        email = (EditText) findViewById(R.id.email_edittext);
        password = (EditText) findViewById(R.id.password_edittext);
        re_password = (EditText) findViewById(R.id.re_password_edittext);
        phone = (EditText) findViewById(R.id.phone_edittext);
        reg_btn = (Button) findViewById(R.id.reg_btn);
        google_icon = (ImageView) findViewById(R.id.ic_google);
        auth = FirebaseAuth.getInstance(); //3
        already = (TextView) findViewById(R.id.already);
        reference= FirebaseDatabase.getInstance("https://sa3ed-194e7-default-rtdb.firebaseio.com/").getReference();
        pd=new ProgressDialog(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        googleApiClient=GoogleSignIn.getClient(this,gso);
        google_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up();
            }
        });
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernamee = username.getText().toString();
                String emaill = email.getText().toString();
                String passwordd = password.getText().toString();
                String re_passwordd = re_password.getText().toString();
                String phonee = phone.getText().toString();
                if (usernamee.equals("") || emaill.equals("") || passwordd.equals("") || re_passwordd.equals("") || phonee.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Empty Creditianls", Toast.LENGTH_SHORT).show();
                } else if (!passwordd.equals(re_passwordd)) {
                    Toast.makeText(RegisterActivity.this, "Password Doesn't match", Toast.LENGTH_SHORT).show();
                } else if (passwordd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else if (phonee.length() != 11) {
                    Toast.makeText(RegisterActivity.this, "Phone length is wrong", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(emaill, passwordd, phonee, usernamee);
                }
            }
        });
    }

    private void sign_up() {
        Intent intent=googleApiClient.getSignInIntent();
        startActivityForResult(intent,SIGN_UP);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_UP){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
             if(completedTask.isSuccessful()) {
               GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
               Toast.makeText(RegisterActivity.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                  FirebaseGoogleAuth(acc);
                  String personName = acc.getDisplayName();
                  String personEmail = acc.getEmail();
                  String personId = acc.getId();
                   reference.child("Users").child(personId).child("Name").setValue(personName);
                   reference.child("Users").child(personId).child("E-Mail").setValue(personEmail);
                   reference.child("Users").child(personId).child("Id").setValue(personId);

                    Intent intent = new Intent(RegisterActivity.this, PostRegisterActivity.class);
                     Bundle bundle = new Bundle();
                     bundle.putString("Id_Google",personId);
                     intent.putExtras(bundle);
                     startActivity(intent);
                     finish();

}
        }
        catch (ApiException e){
            Toast.makeText(RegisterActivity.this,"Sign Up Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //check if the account is null
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        //updateUI(user);
                       // Toast.makeText(RegisterActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
                    } else {
                       // Toast.makeText(RegisterActivity.this, "Failedkjnj", Toast.LENGTH_SHORT).show();
                       // updateUI(null);
                    }
                }
            });
        }
        else{
            Toast.makeText(RegisterActivity.this, "Account failed", Toast.LENGTH_SHORT).show();
        }
    }

    /* private void updateUI(FirebaseUser fUser){
         GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
         if(account !=  null){
             String personName = account.getDisplayName();
             //String personGivenName = account.getGivenName();
             //String personFamilyName = account.getFamilyName();
             String personEmail = account.getEmail();
             String personId = account.getId();
             reference.child("Users").child(fUser.getUid()).child("Name").setValue(personName);
             reference.child("Users").child(fUser.getUid()).child("E-Mail").setValue(personEmail);
             reference.child("Users").child(fUser.getUid()).child("Id").setValue(fUser.getUid());
         }
         }
         */

    private void registerUser(final String email, final String password, final String phone, final String username) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Register Successfully,Please check email for verification", Toast.LENGTH_SHORT).show();
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("Name", username);
                                    map.put("Phone", phone);
                                    map.put("E-mail", email);
                                    map.put("ID",auth.getCurrentUser().getUid());
                                    map.put("IdGoogle","");
                                    reference.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                pd.dismiss();
                                                Intent intent = new Intent(RegisterActivity.this, PostRegisterActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("Id_Google","");
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    public void onBackPressed() {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}

