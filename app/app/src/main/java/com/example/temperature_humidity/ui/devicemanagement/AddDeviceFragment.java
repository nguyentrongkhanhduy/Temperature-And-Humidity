package com.example.temperature_humidity.ui.devicemanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.temperature_humidity.databinding.FragmentAdddeviceBinding;
import com.example.temperature_humidity.databinding.FragmentDevicemanagementBinding;
import com.example.temperature_humidity.model.DeviceModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AddDeviceFragment extends Fragment {
    FragmentAdddeviceBinding binding;
    Spinner spinner;
    private DatabaseReference mData;
    final String[] names = {"TEMP-HUMID", "RELAY"};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAdddeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String building = getArguments().getString("building");
        String room = getArguments().getString("room");

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        spinner = binding.spnName;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, names);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);

//        Toast.makeText(getActivity(), room + building, Toast.LENGTH_SHORT).show();

        mData = FirebaseDatabase.getInstance().getReference();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String deviceName = spinner.getSelectedItem().toString();
                if (deviceName.equals("RELAY")){
                    binding.llRelay.setVisibility(View.VISIBLE);
                }
                else{
                    binding.llRelay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String deviceName = spinner.getSelectedItem().toString();
                String deviceID = binding.edtID.getText().toString();

                String on = "";
                String off = "";

                String unit = "C-%";
                String data = "0";

                if (deviceName.equals("RELAY")){
                    on = binding.edtOnThreshold.getText().toString();
                    off = binding.edtOffThreshold.getText().toString();
                    unit = "";
                }
                DeviceModel deviceModel = new DeviceModel(deviceID, deviceName, data, unit, on, off);
//                        Toast.makeText(root.getContext(), selectedItem + " " + deviceID,Toast.LENGTH_SHORT).show();
                        mData.child("Buildings")
                                .child(building)
                                .child(room)
                                .child("deviceModel")
                                .child(deviceID)
                                .setValue(deviceModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(root.getContext(), "Thêm thiết bị thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
//                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        String selectedItem = parent.getItemAtPosition(position).toString();
//                        String deviceID = binding.edtID.getText().toString();
//                        DeviceModel deviceModel = new DeviceModel(deviceID, selectedItem, "", "C-%");
//                        Toast.makeText(root.getContext(), selectedItem + " " + deviceID,Toast.LENGTH_SHORT).show();
//                        mData.child("Buildings").child(building).child(room).child("deviceModel").push().setValue(deviceModel).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull @NotNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(root.getContext(), "Thêm phòng hoàn tất", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
            }
        });

        return root;
    }
}
