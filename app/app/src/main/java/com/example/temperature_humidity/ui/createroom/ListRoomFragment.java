package com.example.temperature_humidity.ui.createroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;

import com.example.temperature_humidity.databinding.FragmentListroomaddBinding;
import com.example.temperature_humidity.databinding.FragmentRoomnameBinding;

public class ListRoomFragment extends Fragment {
    private FragmentListroomaddBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        binding = FragmentListroomaddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        binding.btnAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.to_room_name);
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
