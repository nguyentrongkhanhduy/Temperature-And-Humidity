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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
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

//        try {
//            URL url = new URL("http:192.168.1.8:8080");
//            URLConnection urlConnection = url.openConnection();
//            InputStreamReader inputStreamReader = new InputStreamReader((InputStream)urlConnection.getContent());
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String str;
//            while ((str=bufferedReader.readLine()) != null){
//                System.out.println(str);
//            }
//            bufferedReader.close();
//            System.out.println("getAllowUserInteraction()"+urlConnection.getAllowUserInteraction());
//            System.out.println("getDoInput()"+urlConnection.getDoInput());
//            System.out.println("getDOutput()"+urlConnection.getDoOutput());
//            System.out.println("getIfModifiedSince()"+urlConnection.getIfModifiedSince());
//            System.out.println("getUseCaches()"+urlConnection.getUseCaches());
//            System.out.println("getURL()"+urlConnection.getURL());
//            System.out.println("getContentEncoding()"+urlConnection.getContentEncoding());
//            System.out.println("getLastModified()"+urlConnection.getLastModified());
//            Map<String, List<String>> map = urlConnection.getHeaderFields();
//            for (Map.Entry<String,List<String>> me: map.entrySet()){
//                String key = me.getKey();
//                List<String> valueList = me.getValue();
//                System.out.println("\n Key: " + key);
//                for (String value: valueList){
//                    System.out.println("value: " + value + " " );
//                }
//                System.out.println();
//            }
//
//        } catch (MalformedURLException e) {
//            System.out.println(e.getMessage());
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }


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
