package com.example.temperature_humidity.ui.devicemanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentDevicemanagementBinding;
import com.example.temperature_humidity.databinding.FragmentEditDeviceBinding;
import com.example.temperature_humidity.model.DeviceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class EditDeviceFragment extends Fragment {
    FragmentEditDeviceBinding binding;
    private DatabaseReference mData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        String[] device = getArguments().getStringArray("device");
        String building = device[0];
        String room = device[1];
        String deviceName = device[2];
        String deviceID = device[3];

        binding.tvName.setText(deviceName);
        binding.tvID.setText("ID: " + deviceID);

//        Toast.makeText(root.getContext(), building + room + deviceName + deviceID, Toast.LENGTH_SHORT).show();

        mData = FirebaseDatabase.getInstance().getReference();


        mData.child("Buildings").child(building).child(room).child("deviceModel")
                .child(deviceName)
                .child(deviceID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            DeviceModel deviceModel = snapshot.getValue(DeviceModel.class);
                            binding.edtOnThreshold.setText(deviceModel.getOnThreshold());
                            binding.edtOffThreshold.setText(deviceModel.getOffThreshold());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean successful_on = false;
                boolean successful_off = false;
                mData.child("Buildings").child(building).child(room).child("deviceModel")
                        .child(deviceName)
                        .child(deviceID)
                        .child("onThreshold").setValue(binding.edtOnThreshold.getText().toString());
                mData.child("Buildings").child(building).child(room).child("deviceModel")
                        .child(deviceName)
                        .child(deviceID)
                        .child("offThreshold").setValue(binding.edtOffThreshold.getText().toString());
                mData.child("Devices").child(deviceName)
                        .child(deviceID)
                        .child("onThreshold").setValue(binding.edtOnThreshold.getText().toString());
                mData.child("Devices").child(deviceName)
                        .child(deviceID)
                        .child("offThreshold").setValue(binding.edtOffThreshold.getText().toString());
                Toast.makeText(root.getContext(),"Thay đổi thành công", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("building", building);
                bundle.putString("room", room);
                Navigation.findNavController(root).navigate(R.id.to_device_management, bundle);
            }
        });

        return root;
    }

}
