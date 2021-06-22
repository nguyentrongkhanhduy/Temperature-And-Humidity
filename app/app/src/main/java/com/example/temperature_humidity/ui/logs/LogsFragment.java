package com.example.temperature_humidity.ui.logs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentLogsBinding;
import com.example.temperature_humidity.databinding.FragmentLookuproomBinding;
import com.example.temperature_humidity.model.DeviceModel;
import com.example.temperature_humidity.model.HistoryDetailModel;
import com.example.temperature_humidity.model.HistoryUserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LogsFragment extends Fragment {
    FragmentLogsBinding binding;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    private String userID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLogsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mData.child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<HistoryDetailModel> lst = new ArrayList<>();
                for (DataSnapshot post: snapshot.getChildren()){
                    HistoryDetailModel historyUserModel = post.getValue(HistoryDetailModel.class);
                    lst.add(historyUserModel);
                }
                ItemsAdapter itemsAdapter = new ItemsAdapter(root.getContext(), lst);
                binding.lvLogs.setAdapter(itemsAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return root;
    }

    public class ItemsAdapter extends BaseAdapter {
        private List<HistoryDetailModel> list;
        private Context context;

        public ItemsAdapter(Context context, List<HistoryDetailModel> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_history_detail, null);
            TextView tvContent, tvTemp, tvHumid, tvTime, tvMode, tvEmail, tvDV;
            LinearLayout llEmail;
            llEmail = convertView.findViewById(R.id.llEmail);
            tvDV = convertView.findViewById(R.id.tvDV);
            tvContent = convertView.findViewById(R.id.tvContent);
            tvEmail = convertView.findViewById(R.id.tvEmail);
            tvHumid = convertView.findViewById(R.id.tvHumid);
            tvTemp = convertView.findViewById(R.id.tvTemp);
            tvMode = convertView.findViewById(R.id.tvMode);
            tvTime = convertView.findViewById(R.id.tvTime);
            String data_text = "";
            if (list.get(position).getData().equals("0"))
            {
                data_text = "Tắt";
            }
            else if (list.get(position).getData().equals("1")){
                data_text = "Bật" ;
            }

            tvContent.setText(list.get(position).getName() + list.get(position).getId() + ": " + data_text);
            tvMode.setText(list.get(position).getMode());
            if (list.get(position).getMode().equals("auto")){
                llEmail.setVisibility(View.GONE);
            }
            else {
                llEmail.setVisibility(View.VISIBLE);
            }

            mData.child("Accounts").child(list.get(position).getUser()).child("profileModel").child("email").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String email = snapshot.getValue(String.class);
                    tvEmail.setText(email);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });


            String temphumid = list.get(position).getTemp_humid();
            String[] arr_th = temphumid.split("-");
            String temp = arr_th[0];
            String humid = arr_th[1];
            tvTemp.setText(temp);
            tvHumid.setText(humid);

            tvDV.setText(list.get(position).getTemp_humid_unit());

            tvTime.setText(list.get(position).getTime());

            return convertView;
        }
    }

}
