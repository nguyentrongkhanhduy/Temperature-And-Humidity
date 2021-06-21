package com.example.temperature_humidity.ui.historyactivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentUserhistoryBinding;
import com.example.temperature_humidity.model.HistoryUserModel;
import com.example.temperature_humidity.model.RequestModel;
import com.example.temperature_humidity.model.TimeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserHistoryFragment extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FragmentUserhistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserhistoryBinding.inflate(inflater, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);



        ListView lvHistory = binding.lvHistory;
        String userID = mAuth.getCurrentUser().getUid();

        mDatabase.child("Accounts").child(userID.toString()).child("History").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<HistoryUserModel> lst = new ArrayList<>();
                for(DataSnapshot post: snapshot.getChildren()){
                    HistoryUserModel historyUserModel = post.getValue(HistoryUserModel.class);
                    lst.add(historyUserModel);
                }
                ItemsAdapter item = new ItemsAdapter(getActivity(),lst);
                lvHistory.setAdapter(item);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = ((TextView)view.findViewById(R.id.tvType)).getText().toString();
                    String building_room = ((TextView)view.findViewById(R.id.tvRoom)).getText().toString();
                    String period = ((TextView)view.findViewById(R.id.tvPeriod)).getText().toString();
                    String date = ((TextView)view.findViewById(R.id.tvDate)).getText().toString();
                    String hisID = ((TextView)view.findViewById(R.id.tvHID)).getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("building_room", building_room);
                    bundle.putString("date", date);
                    bundle.putString("period", period);
                    bundle.putString("hisID", hisID);
                    Navigation.findNavController(root).navigate(R.id.to_history_detail, bundle);
            }
        });

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public class ItemsAdapter extends BaseAdapter{
        private List<HistoryUserModel> list;
        private Context context;
        public ItemsAdapter(Context context, List<HistoryUserModel> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_history, null);
            TextView tvType, tvRoom, tvPeriod, tvDate, tvHID;
            tvType = convertView.findViewById(R.id.tvType);
            tvRoom = convertView.findViewById(R.id.tvRoom);
            tvPeriod = convertView.findViewById(R.id.tvPeriod);
            tvDate = convertView.findViewById(R.id.tvDate);
            tvHID = convertView.findViewById(R.id.tvHID);
            TimeModel timeModel = list.get(position).getTimeModel();

            tvType.setText(list.get(position).getType());
            tvRoom.setText(list.get(position).getBuilding() + " - " + list.get(position).getRoom());
            tvDate.setText(timeModel.getDate());
            tvPeriod.setText(timeModel.getStartTime() + " - " + timeModel.getEndTime());
            tvHID.setText(list.get(position).getHisID());

            ImageView imDecline = convertView.findViewById(R.id.imDecline);

            String userID = mAuth.getCurrentUser().getUid();

            imDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("Accounts").child(userID.toString()).child("History").child(list.get(position).getHisID()).removeValue();
                    Toast.makeText(getContext(), "Đã xoá lịch sử", Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }
}
