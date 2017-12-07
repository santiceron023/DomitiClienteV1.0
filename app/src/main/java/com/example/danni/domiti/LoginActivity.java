package com.example.danni.domiti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText eEmail, ePassword;
    private String Email, Password;

    //Autenticacion con firebase
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        ePassword=(EditText)findViewById(R.id.ePass);
        eEmail=(EditText)findViewById(R.id.eEmail);


    }

    public void bLogin(View view) {
        if(eEmail.getText().toString().isEmpty()){eEmail.setError("Complete this field");return;}
        if(ePassword.getText().toString().isEmpty()){ePassword.setError("Complete this field");return;}
        if (!validarEmail(eEmail.getText().toString())){eEmail.setError("Email isn't valid");}
        Email=eEmail.getText().toString();
        Password=ePassword.getText().toString();
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }


                    }


                });

    }
    @Override

    public void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }
    private void updateUI(FirebaseUser user) {
        if(user!=null){
            SharedPreferences sharedPrefs = getSharedPreferences("DomitiPreferences", this.MODE_PRIVATE);
            SharedPreferences.Editor editorSP= sharedPrefs.edit();
            editorSP.putInt("optLog",1);
            editorSP.commit();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();


        }else{
           //Toast.makeText(this,"Fallo",Toast.LENGTH_SHORT).show();
        }
    }

    public void bRegister(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
