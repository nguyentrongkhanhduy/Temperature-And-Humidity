package com.example.temperature_humidity.ui.devicecontrol;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.MQTTService;
import com.example.temperature_humidity.MainActivity;
import com.example.temperature_humidity.MainAdminActivity;
import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentFanControlBinding;
import com.example.temperature_humidity.databinding.FragmentRegisterroomBinding;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class FanControlFragment extends Fragment {

    int i = 0;
    private ReentrantLock mutex = new ReentrantLock();
    private void sendDataMQTT(String data, MQTTService mqttService ){
        try {
            mutex.lock();
            MqttMessage msg = new MqttMessage();
            msg.setId(i++);
            msg.setQos(0);
            msg.setRetained(true);
            byte[] b = data.getBytes(Charset.forName("UTF-8"));
            msg.setPayload(b);
            Log.d("ABC", "Publish:" + msg);
            try {
                mqttService.mqttAndroidClient.publish("dadn/feeds/bbc-led", msg);

            } catch (MqttException e) {

            }
        }
        finally {
            try {
                Thread.sleep(1000);
            }catch(Exception e){

            }
            mutex.unlock();
        }


    }

    private FragmentFanControlBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFanControlBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MQTTService mqttService = new MQTTService( (MainAdminActivity)getActivity());
        mqttService.setCallback(new MqttCallbackExtended() {
            @Override public void connectComplete(boolean reconnect, String serverURI) {

            }
            @Override public void connectionLost( Throwable cause){

            }


            @Override public void messageArrived(String topic, MqttMessage message) throws Exception {
                String data_to_microbit = message.toString();
                if (data_to_microbit.equals("1")){
                    binding.switch2.setChecked(true);
                }
                else if (data_to_microbit.equals("0")){
                    binding.switch2.setChecked(false);

                }
                //port.write(data_to_microbit.getBytes(),1000);
                Toast.makeText((MainAdminActivity)getActivity(),data_to_microbit,Toast.LENGTH_LONG).show();
                System.out.println(data_to_microbit);
            }
            @Override public void deliveryComplete(IMqttDeliveryToken token)
            {

            }
        });



        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);




        binding.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(() -> {
                        while(mqttService.virgin){

                        }
                        sendDataMQTT("1",mqttService);
                    });
                }
                else {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(() -> {
                        while(mqttService.virgin){

                        }
                        sendDataMQTT("0",mqttService);
                    });
                }
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
