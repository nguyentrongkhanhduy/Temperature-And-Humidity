package com.example.temperature_humidity.ui.historyactivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.temperature_humidity.databinding.FragmentHistoryBinding;
import com.example.temperature_humidity.databinding.FragmentHistoryDetailBinding;
import com.example.temperature_humidity.databinding.FragmentUserhistoryBinding;
import com.example.temperature_humidity.model.HistoryDetailModel;
import com.example.temperature_humidity.model.HistoryUserModel;
import com.example.temperature_humidity.model.TimeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryDetailFragment extends Fragment {
    FragmentHistoryDetailBinding binding;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryDetailBinding.inflate(inflater, container, false);
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        String building_room = getArguments().getString("building_room");
        String date = getArguments().getString("date");
        String period = getArguments().getString("period");
        String hisID = getArguments().getString("hisID");
//        Toast.makeText(root.getContext(),building_room + date + period, Toast.LENGTH_SHORT).show();

        mData.child("Accounts").child(userID).child("History")
               .child(hisID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            HistoryUserModel historyUserModel = snapshot.getValue(HistoryUserModel.class);
                            binding.tvRoom.setText(building_room);
                            String[] arr = hisID.split("_");
                            String time = arr[0].substring(0,2) + "/" + arr[0].substring(2,4) + "/" + arr[0].substring(4,8) + " " +
                                    arr[1].substring(0,2) +":" + arr[1].substring(2,4) + ":" + arr[1].substring(4,6);
                            binding.tvTime.setText(time);
                            binding.tvType.setText(historyUserModel.getType());
                            binding.tvUID.setText(historyUserModel.getEmail());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        return root;
    }



}
