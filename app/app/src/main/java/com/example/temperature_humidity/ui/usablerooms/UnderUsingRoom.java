package com.example.temperature_humidity.ui.usablerooms;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentSelectroomBinding;
import com.example.temperature_humidity.databinding.FragmentUnderUsingRoomBinding;
import com.example.temperature_humidity.model.DeviceModel;
import com.example.temperature_humidity.model.HistoryDetailModel;
import com.example.temperature_humidity.model.HistoryUserModel;
import com.example.temperature_humidity.model.TimeModel;
import com.example.temperature_humidity.ui.devicemanagement.DeviceManagementFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UnderUsingRoom extends Fragment {
    FragmentUnderUsingRoomBinding binding;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    String building_room;
    String ca;
    String historyID;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentUnderUsingRoomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        building_room = getArguments().getString("building_room");
        String[] arr = building_room.split("-", 2);
        String building = arr[0];
        String room = arr[1];
        ca = getArguments().getString("ca");

//        Toast.makeText(root.getContext(), building_room + ca, Toast.LENGTH_SHORT).show();

        binding.tvRoomname.setText(building_room);

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mData.child("Buildings").child(building).child(room).child("deviceModel").child("TEMP-HUMID")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                        List<DeviceModel> lst = new ArrayList<>();
                        for (DataSnapshot post: snapshot.getChildren()){
                            DeviceModel deviceModel = post.getValue(DeviceModel.class);
                            lst.add(deviceModel);
                        }
                        String[] data_arr = lst.get(0).getData().split("-", 2);
                        String[] unit_arr = lst.get(0).getUnit().split("-", 2);
                        binding.tvHumid.setText(data_arr[1]);
                        binding.tvTemp.setText(data_arr[0]);
                        binding.tvHumidUnit.setText(unit_arr[1]);
                        binding.tvTempUnit.setText(unit_arr[0]);
                        }
                        else {
                            binding.tvHumid.setText("");
                            binding.tvTemp.setText("");
                            binding.tvHumidUnit.setText("");
                            binding.tvTempUnit.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        mData.child("Buildings").child(building).child(room).child("deviceModel").child("RELAY")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                        List<DeviceModel> lst = new ArrayList<>();
                        for (DataSnapshot post: snapshot.getChildren()){
                            DeviceModel deviceModel = post.getValue(DeviceModel.class);
                            lst.add(deviceModel);
                        }
                        ItemsAdapter itemsAdapter = new ItemsAdapter(root.getContext(), lst);
                        binding.lvRelay.setAdapter(itemsAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        binding.btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.child("Accounts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("usableRooms")
                        .child(building_room)
                        .removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                                Toast.makeText(root.getContext(), "Đã kết thúc phiên sử dụng phòng", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(root).navigate(R.id.to_usableRooms);
                            }
                        });
            }
        });

        return root;
    }

    public class ItemsAdapter extends BaseAdapter {
        private List<DeviceModel> list;
        private Context context;

        public ItemsAdapter(Context context, List<DeviceModel> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(convertView == null)
                convertView = LayoutInflater.from(context).inflate(R.layout.item_using_device, null);
            TextView tvNameID, tvOnThreshold, tvOffThreshold, tvID, tvData, tvDV1, tvDV2;
            Switch swt;
            ImageView image;
            tvNameID = convertView.findViewById(R.id.tvNameID);
            tvOnThreshold = convertView.findViewById(R.id.tvOnThreshold);
            tvOffThreshold = convertView.findViewById(R.id.tvOffThreshold);
            tvDV1 = convertView.findViewById(R.id.tvDV1);
            tvDV2 = convertView.findViewById(R.id.tvDV2);
            swt = convertView.findViewById(R.id.swt);
            String data = list.get(position).getData();

            String id = list.get(position).getId();
            String name = list.get(position).getName();
            String unit = list.get(position).getUnit();
            String building = list.get(position).getBuilding();
            String room = list.get(position).getRoom();
            String userID = mAuth.getCurrentUser().getUid();
            String email = mAuth.getCurrentUser().getEmail();
            Calendar c = Calendar.getInstance();

            tvNameID.setText("Thiết bị: " + list.get(position).getName() + " - ID: " +list.get(position).getId());





            tvOnThreshold.setText(list.get(position).getOnThreshold());
            tvOffThreshold.setText(list.get(position).getOffThreshold());

            if (data.equals("0")){
                swt.setChecked(false);
            }
            else{
                swt.setChecked(true);
            }


//            mData.child("Buildings").child(list.get(position).getBuilding())
//                    .child(list.get(position).getRoom())
//                    .child("deviceModel")
//                    .child("TEMP-HUMID")
//                    .child(list.get(0).getId())
//                    .child("unit").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                    String dv = snapshot.getValue(String.class);
//                    if (!dv.equals("C-%")) {
//                        if (dv.equals("F-%")){
//                            Double onT = Double.parseDouble(tvOnThreshold.getText().toString());
//                            Double offT = Double.parseDouble(tvOffThreshold.getText().toString());
//                            Double new_onT = onT * 1.8 + 32;
//                            Double new_offT = offT * 1.8 + 32;
//                            tvOnThreshold.setText(new_onT.toString());
//                            tvOffThreshold.setText(new_offT.toString());
//                            tvDV1.setText("F-%");
//                            tvDV2.setText("F-%");
//                        }
//                        else if (dv.equals("K-%")){
//                            Double onT = Double.parseDouble(tvOnThreshold.getText().toString());
//                            Double offT = Double.parseDouble(tvOffThreshold.getText().toString());
//                            Double new_onT = onT + 273.15;
//                            Double new_offT = offT * 273.15;
//                            tvOnThreshold.setText(new_onT.toString());
//                            tvOffThreshold.setText(new_offT.toString());
//                            tvDV1.setText("K-%");
//                            tvDV2.setText("K-%");
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                }
//            });


            swt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swt.isChecked()){
                        mData.child("Buildings").child(list.get(position).getBuilding())
                                .child(list.get(position).getRoom())
                                .child("deviceModel")
                                .child("RELAY")
                                .child(list.get(position).getId())
                                .child("data").setValue("1");
                        mData.child("Devices").child("RELAY").child(list.get(position).getId())
                                .child("data").setValue("1");


                        String type = "Bật "+ list.get(position).getName() + " " + list.get(position).getId();
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
                        LocalDateTime now = LocalDateTime.now();
                        String historyID = dtf.format(now);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String ondate = sdf.format(c.getTime());
                        TimeModel timeModel = new TimeModel("","",ondate);
                        HistoryUserModel historyUserModel = new
                                HistoryUserModel(historyID, timeModel,email, room, building, userID, type);
                        mData.child("Accounts").child(userID.toString()).child("History").child(historyID).setValue(historyUserModel);
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(() -> {
                            try {
                                String query = String.format("{ \"id\":\"%s\", " +
                                        "\"name\":\"%s\", " +
                                        "\"data\":\"%s\", " +
                                        "\"unit\":\"%s\", " +
                                        "\"building\":\"%s\", " +
                                        "\"room\":\"%s\", " +
                                        "\"user\":\"%s\" }"
                                ,id, name, "1", unit, building, room, userID);
                                String url = "http://192.168.1.10:8080/";
                                URLConnection connection = new URL(url + query).openConnection();
                                InputStream in = connection.getInputStream();
                                //connection.setRequestProperty("Accept-Charset", "UTF-8");
                            }
                            catch(Exception e){
                                System.out.println("__________________________");
                                e.printStackTrace();
                            }

                        });
                    }
                    else{
                        mData.child("Buildings").child(list.get(position).getBuilding())
                                .child(list.get(position).getRoom())
                                .child("deviceModel")
                                .child("RELAY")
                                .child(list.get(position).getId())
                                .child("data").setValue("0");
                        mData.child("Devices").child("RELAY").child(list.get(position).getId())
                                .child("data").setValue("0");

                        String type = "Tắt "+ list.get(position).getName() + " " + list.get(position).getId();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String offdate = sdf.format(c.getTime());
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
                        LocalDateTime now = LocalDateTime.now();
                        String historyID = dtf.format(now);
                        TimeModel timeModel = new TimeModel("","",offdate);
                        HistoryUserModel historyUserModel = new
                                HistoryUserModel(historyID, timeModel,email, room, building, userID, type);
                        mData.child("Accounts").child(userID.toString()).child("History").child(historyID).setValue(historyUserModel);

                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.submit(() -> {
                            try {
                                String query = String.format("{ \"id\":\"%s\", " +
                                                "\"name\":\"%s\", " +
                                                "\"data\":\"%s\", " +
                                                "\"unit\":\"%s\", " +
                                                "\"building\":\"%s\", " +
                                                "\"room\":\"%s\", " +
                                                "\"user\":\"%s\" }"
                                        ,id, name, "0", unit, building, room, userID);
                                String url = "http://192.168.1.10:8080/";
                                URLConnection connection = new URL(url + query).openConnection();
                                InputStream in = connection.getInputStream();
                                //connection.setRequestProperty("Accept-Charset", "UTF-8");
                            }
                            catch(Exception e){
                                System.out.println("__________________________");
                                e.printStackTrace();
                            }

                        });
                    }
                }
            });

            return convertView;
        }
    }

}
