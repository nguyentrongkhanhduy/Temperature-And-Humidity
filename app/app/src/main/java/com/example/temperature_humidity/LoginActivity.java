package com.example.temperature_humidity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    TextInputEditText edtUsername, edtPassword;
    Button btnLogin;
    private FirebaseAuth mAuth;
    private boolean loginsuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        //Hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        edtUsername = binding.edtUsername;
        edtPassword = binding.edtPassword;;


        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdmin = false;
                if(edtUsername.getText().toString()!=null&&edtPassword.getText().toString()!=null){
                    Login(edtUsername.getText().toString(),edtPassword.getText().toString());
                }
            }
        });;
    }

    public void Login(String username,String Password){
        mAuth.signInWithEmailAndPassword(username, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (binding.checkkbox.isChecked()) {
                                Intent intent = new Intent(getBaseContext(), MainAdminActivity.class);
                                startActivity(intent);
                                }
                            else {
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                }
                        }
                        else{
                            edtUsername.requestFocus();
                            Toast.makeText(LoginActivity.this, "Login Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
