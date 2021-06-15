package com.example.temperature_humidity.ui.devicemanagement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentDevicemanagementBinding;
import com.example.temperature_humidity.databinding.FragmentRoomnameBinding;
import com.example.temperature_humidity.model.AccountModel;
import com.example.temperature_humidity.model.DeviceModel;
import com.example.temperature_humidity.model.ProfileModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeviceManagementFragment extends Fragment {
    FragmentDevicemanagementBinding binding;
    private DatabaseReference mData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDevicemanagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String building = getArguments().getString("building");
        String room = getArguments().getString("room");
        binding.tvRoom.setText(building + "-" + room);

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        binding.btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building",building);
                bundle.putString("room", room);
//                Toast.makeText(getActivity(),"Thêm phòng thành công",Toast.LENGTH_SHORT).show();
                Navigation.findNavController(root).navigate(R.id.to_add_device, bundle);
            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("building",building);
                bundle.putString("name", room);
//                Toast.makeText(getActivity(),"Thêm phòng thành công",Toast.LENGTH_SHORT).show();
                Navigation.findNavController(root).navigate(R.id.done_add_room, bundle);
            }
        });

        mData = FirebaseDatabase.getInstance().getReference();

        mData.child("Buildings").child(building).child(room).child("deviceModel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<DeviceModel> deviceModels = new ArrayList<>();
                for (DataSnapshot post: snapshot.getChildren()){
                    DeviceModel deviceModel = post.getValue(DeviceModel.class);
                    deviceModels.add(deviceModel);
                }

                ItemsAdapter itemsAdapter = new ItemsAdapter(root.getContext(), deviceModels);
                binding.lvDevice.setAdapter(itemsAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_device, null);
            TextView tvName, tvThreshold, tvID, tvData, tvUnit;
            ImageView image;
            tvName = convertView.findViewById(R.id.tvName);
            tvData = convertView.findViewById(R.id.tvData);
            tvID = convertView.findViewById(R.id.tvID);
            tvUnit = convertView.findViewById(R.id.tvUnit);
            tvThreshold = convertView.findViewById(R.id.tvThreshold);
            image = convertView.findViewById(R.id.image);

            tvName.setText("Tên: " + list.get(position).getName());
            tvID.setText("ID: " + list.get(position).getId());
            tvData.setText("Dữ liệu: " + list.get(position).getData());
            tvUnit.setText("Đơn vị: " + list.get(position).getUnit());
            if (list.get(position).getName().equals("RELAY")){
                tvThreshold.setVisibility(View.VISIBLE);
                image.setImageResource(R.drawable.fan);
            }
            else {
                tvThreshold.setVisibility(View.GONE);
                image.setImageResource(R.drawable.microchip);
            }
            return convertView;
        }
    }

}
