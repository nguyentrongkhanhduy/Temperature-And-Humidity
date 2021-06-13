package com.example.temperature_humidity.ui.createroom;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.MQTTService;
import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentListroomaddBinding;
import com.example.temperature_humidity.databinding.FragmentRoomnameBinding;
import com.example.temperature_humidity.model.RoomModel;
import com.example.temperature_humidity.model.TimeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class RoomNameFragment extends Fragment {

    private FragmentRoomnameBinding binding;

    DatabaseReference mData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentRoomnameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String building = getArguments().getString("add");
        binding.textView6.setText("Toà " + building);

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        mData = FirebaseDatabase.getInstance().getReference();


        binding.btnCreate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.txtName.getText().toString();
                mData.child("Buildings").child(building).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        Boolean check = false;
                        for (DataSnapshot postsnapshot:snapshot.getChildren()){
                            if (name.equals(postsnapshot.getKey())) {
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            TimeModel time = new TimeModel("empty","empty");
                            mData.child("Buildings").child(building).child(name).child("timeModel").child("1").setValue(time);
                            mData.child("Buildings").child(building).child(name).child("idRoom").setValue(name);
                            Toast.makeText(getActivity(),"Thêm phòng thành công",Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("building",building);
                            Navigation.findNavController(root).navigate(R.id.done_add_room, bundle);
                        }
                        else {
                            Toast.makeText(getActivity(),"Phòng đã tồn tại",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.cancel_add_room);
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
