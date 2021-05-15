package com.example.temperature_humidity.ui.registerroom;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentExampleBinding;
import com.example.temperature_humidity.databinding.FragmentRegisterroomBinding;

public class ExampleFragment extends Fragment {
    private FragmentExampleBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        binding = FragmentExampleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
