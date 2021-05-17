package com.example.temperature_humidity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.temperature_humidity.databinding.ActivityMainBinding;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.json.*;

public class MainActivity extends AppCompatActivity {
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


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MQTTService mqttService = new MQTTService( this);
        mqttService.setCallback(new MqttCallbackExtended() {
            @Override public void connectComplete(boolean reconnect, String serverURI) {

            }
            @Override public void connectionLost( Throwable cause){

            }

            @Override public void messageArrived(String topic, MqttMessage message) throws Exception {
                String data_to_microbit = message.toString();
                //port.write(data_to_microbit.getBytes(),1000);
                Toast.makeText(MainActivity.this,data_to_microbit,Toast.LENGTH_LONG).show();
                System.out.println(data_to_microbit);
                Log.d("receive dc bo'","yoosho");
            }
            @Override public void deliveryComplete(IMqttDeliveryToken token)
            {
                Log.d("receive xong ho bo'","yoosho");
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while(mqttService.virgin){

            }
            sendDataMQTT("8",mqttService);
            sendDataMQTT("7",mqttService);
            sendDataMQTT("6",mqttService);
            sendDataMQTT("5",mqttService);
            sendDataMQTT("4",mqttService);
            sendDataMQTT("3",mqttService);
            sendDataMQTT("2",mqttService);
            sendDataMQTT("1",mqttService);

        });

        //Hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#C0F9FA"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        boolean isAdmin = intent.getBooleanExtra("isAdmin", false);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_profile, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
//
//


    }



}