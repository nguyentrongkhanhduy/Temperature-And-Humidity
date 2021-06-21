package com.example.temperature_humidity.ui.roombrowsing;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentLookupdateBinding;
import com.example.temperature_humidity.databinding.FragmentRoombrowsingBinding;
import com.example.temperature_humidity.model.AccountModel;
import com.example.temperature_humidity.model.ApprovedModel;
import com.example.temperature_humidity.model.HistoryUserModel;
import com.example.temperature_humidity.model.ProfileModel;
import com.example.temperature_humidity.model.RequestModel;
import com.example.temperature_humidity.model.TimeModel;
import com.example.temperature_humidity.model.UsableRooms;
import com.example.temperature_humidity.ui.manageaccounts.ManageAccountsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RoomBrowsingFragment extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FragmentRoombrowsingBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRoombrowsingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ListView lvDuyet = binding.lvDuyet;

        mDatabase.child("Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<RequestModel> lst = new ArrayList<>();
                for (DataSnapshot post: snapshot.getChildren()){
                    RequestModel requestModel = post.getValue(RequestModel.class);
                    lst.add(requestModel);
                }
                ItemsAdapter item = new ItemsAdapter(getActivity(),lst);
                lvDuyet.setAdapter(item);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public class ItemsAdapter extends BaseAdapter {
        private List<RequestModel> list;
        private Context context;

        public ItemsAdapter(Context context, List<RequestModel> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_duyet, null);
            TextView tvRoom, tvPeriod, tvDate, tvEmail;
            tvRoom = convertView.findViewById(R.id.tvRoom);
            tvPeriod = convertView.findViewById(R.id.tvPeriod);
            tvDate = convertView.findViewById(R.id.tvDate);
            tvEmail = convertView.findViewById(R.id.tvEmail);
            TimeModel timeModel = list.get(position).getTimeModel();

            tvRoom.setText(list.get(position).getBuilding() + " - " + list.get(position).getRoom());
            tvDate.setText(timeModel.getDate());
            tvPeriod.setText(timeModel.getStartTime() + " - " + timeModel.getEndTime());
            tvEmail.setText(list.get(position).getEmail());

            ImageView imApprove, imDecline;
            imApprove = convertView.findViewById(R.id.imApprove);
            imDecline = convertView.findViewById(R.id.imDecline);

            imApprove.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    String[] dateFormat = list.get(position).getTimeModel().getDate().split("/");
                    String ca = dateFormat[0] + "-" + dateFormat[1] + "-" + dateFormat[2] + " " +
                            list.get(position).getTimeModel().getStartTime() + "-" +
                            list.get(position).getTimeModel().getEndTime();
                    ApprovedModel approvedModel = new ApprovedModel(list.get(position).getEmail(), list.get(position).getUid(), list.get(position).getTimeModel());
                    mDatabase.child("Buildings")
                            .child(list.get(position).getBuilding())
                            .child(list.get(position).getRoom())
                            .child("approvedModel")
                            .child(ca).setValue(approvedModel);
                    UsableRooms usableRooms = new UsableRooms(list.get(position).getBuilding(),list.get(position).getRoom());
                    mDatabase.child("Accounts")
                            .child(list.get(position).getUid())
                            .child("usableRooms")
                            .child(usableRooms.getBuilding() + "-" + usableRooms.getRoom())
                            .setValue(usableRooms);

                    mDatabase.child("Accounts")
                            .child(list.get(position).getUid())
                            .child("usableRooms")
                            .child(usableRooms.getBuilding() + "-" + usableRooms.getRoom())
                            .child("TimeList")
                            .child(dateFormat[0] + "-" + dateFormat[1] + "-" + dateFormat[2] + " " +
                                    list.get(position).getTimeModel().getStartTime() + "-" +
                                    list.get(position).getTimeModel().getEndTime())
                            .setValue(list.get(position).getTimeModel());
                    mDatabase.child("Request").child(list.get(position).getReqID()).removeValue();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
                    LocalDateTime now = LocalDateTime.now();
                    String historyID = dtf.format(now);
                    HistoryUserModel historyUserModel= new
                            HistoryUserModel(historyID,
                            list.get(position).getTimeModel(),
                            list.get(position).getEmail(),list.get(position).getRoom(),
                            list.get(position).getBuilding(),list.get(position).getUid(),
                            "Sử dụng");

                    mDatabase.child("Accounts").child(list.get(position).getUid()).child("History").child(historyID).setValue(historyUserModel);
                    Toast.makeText(getContext(), "Đã chấp nhận yêu cầu", Toast.LENGTH_SHORT).show();
                }
            });
            imDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("Request").child(list.get(position).getReqID()).removeValue();
                    Toast.makeText(getContext(), "Đã xoá yêu cầu", Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
    }

}
