package com.example.danni.domiti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText eUsername, eCelular, ePass, ePass2, eEmail, eDireccion;
    private String Email,Password,Celular,foto;

    //Autenticacion Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private NuevoUsuario nuevoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();



        eUsername=(EditText)findViewById(R.id.eUsername);
        eCelular=(EditText)findViewById(R.id.eCelular);
        ePass=(EditText)findViewById(R.id.ePass);
        ePass2=(EditText)findViewById(R.id.ePass2);
        eEmail=(EditText)findViewById(R.id.eEmail);
        eDireccion=(EditText)findViewById(R.id.eDireccion);
        foto="https://firebasestorage.googleapis.com/v0/b/domiventas-66cdf.appspot.com/o/fotosusuarios%2Fusuario.jpg?alt=media&token=99bd721e-00fb-47e3-922e-db21cbad7782";

    }

    public void bRegister(View view) {
        if(eEmail.getText().toString().isEmpty()){eEmail.setError("Complete this field");return;}
        if(ePass.getText().toString().isEmpty()){ePass.setError("Complete this field");return;}
        if(ePass2.getText().toString().isEmpty()){ePass2.setError("Complete this field");return;}
        if(eCelular.getText().toString().isEmpty()){eCelular.setError("Complete this field");return;}
        if(eUsername.getText().toString().isEmpty()){eUsername.setError("Complete this field");return;}
        if(eDireccion.getText().toString().isEmpty()){eDireccion.setError("Complete this field");return;}
        if(!ePass.getText().toString().equals(ePass2.getText().toString())){ePass2.setError("Passwords don't match");return;}
        if (!validarEmail(eEmail.getText().toString())){eEmail.setError("Email isn't valid"); return; }
        Email=eEmail.getText().toString();
        Password=ePass.getText().toString();
        Celular=eCelular.getText().toString();

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(eCelular.getText().toString())
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference myRef = database.getReference("Usuarios").child(Celular);
                                                nuevoUsuario=new NuevoUsuario(eUsername.getText().toString(), Celular, eEmail.getText().toString(), eDireccion.getText().toString(), foto);
                                                myRef.setValue(nuevoUsuario);
                                                Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_SHORT).show();

                                                FirebaseAuth.getInstance().signOut();
                                                SharedPreferences sharedPrefs = getSharedPreferences("DomitiPreferences", RegisterActivity.MODE_PRIVATE);
                                                SharedPreferences.Editor editorSP= sharedPrefs.edit();
                                                editorSP.putInt("optLog",0);
                                                editorSP.commit();
                                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }
                                    });

                        }else{
                            Toast.makeText(RegisterActivity.this, "Registro Fallo",Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });


    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
