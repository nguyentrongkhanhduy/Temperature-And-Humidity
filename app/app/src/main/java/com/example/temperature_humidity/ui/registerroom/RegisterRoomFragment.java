package com.example.temperature_humidity.ui.registerroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentEditprofileBinding;
import com.example.temperature_humidity.databinding.FragmentRegisterroomBinding;
import com.example.temperature_humidity.ui.profile.DashboardViewModel;

public class RegisterRoomFragment extends Fragment {
    private FragmentRegisterroomBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        binding = FragmentRegisterroomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        binding.h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.to_select_room);
            }
        });

        binding.h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.to_select_room);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
