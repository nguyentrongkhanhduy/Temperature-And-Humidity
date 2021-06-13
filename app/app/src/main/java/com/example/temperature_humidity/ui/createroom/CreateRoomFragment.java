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

import com.example.temperature_humidity.databinding.FragmentCreateroomBinding;
import com.example.temperature_humidity.databinding.FragmentRoomnameBinding;

public class CreateRoomFragment extends Fragment {
    private FragmentCreateroomBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        binding = FragmentCreateroomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        binding.h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building", "H1"); // Put anything what you want

                Navigation.findNavController(root).navigate(R.id.to_list_room,bundle);
            }
        });

        binding.h2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building", "H2"); // Put anything what you want

                Navigation.findNavController(root).navigate(R.id.to_list_room,bundle);
            }
        });

        binding.h3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building", "H3"); // Put anything what you want

                Navigation.findNavController(root).navigate(R.id.to_list_room,bundle);
            }
        });

        binding.h6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building", "H6"); // Put anything what you want

                Navigation.findNavController(root).navigate(R.id.to_list_room,bundle);
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
