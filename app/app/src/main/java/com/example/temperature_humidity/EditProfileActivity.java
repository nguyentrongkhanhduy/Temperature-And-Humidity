package com.example.temperature_humidity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.temperature_humidity.databinding.ActivityEditprofileBinding;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditprofileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        //Hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityEditprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            if(bundle.getString("some") != null){
                Toast.makeText(getApplicationContext(), "data:" + bundle.getString("some")
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
