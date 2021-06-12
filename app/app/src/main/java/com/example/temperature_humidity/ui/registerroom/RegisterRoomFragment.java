package com.example.temperature_humidity.ui.registerroom;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.MQTTService;
import com.example.temperature_humidity.MainActivity;
import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentEditprofileBinding;
import com.example.temperature_humidity.databinding.FragmentRegisterroomBinding;
import com.example.temperature_humidity.ui.profile.DashboardViewModel;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


        binding.btnH1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building", "H1"); // Put anything what you want

                SelectRoomFragment fragment2 = new SelectRoomFragment();
                fragment2.setArguments(bundle);
                Navigation.findNavController(root).navigate(R.id.to_select_room, bundle);
            }
        });

        binding.btnH2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building", "H2"); // Put anything what you want

                SelectRoomFragment fragment2 = new SelectRoomFragment();
                fragment2.setArguments(bundle);
                Navigation.findNavController(root).navigate(R.id.to_select_room, bundle);
            }
        });

        binding.btnH3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building", "H3"); // Put anything what you want

                SelectRoomFragment fragment2 = new SelectRoomFragment();
                fragment2.setArguments(bundle);
                Navigation.findNavController(root).navigate(R.id.to_select_room, bundle);
            }
        });

        binding.btnH6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building", "H6"); // Put anything what you want

                SelectRoomFragment fragment2 = new SelectRoomFragment();
                fragment2.setArguments(bundle);
                Navigation.findNavController(root).navigate(R.id.to_select_room, bundle);
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
