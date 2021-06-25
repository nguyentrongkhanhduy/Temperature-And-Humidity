package com.example.temperature_humidity.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentNotificationsBinding;
import com.example.temperature_humidity.model.NotificationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    //private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private DatabaseReference mDatabase;
    ListView listViewNoti;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //notificationsViewModel =
        //        new ViewModelProvider(this).get(NotificationsViewModel.class);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        // 

        listViewNoti = binding.listViewNoti;

        mDatabase.child("Notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<NotificationModel> lst = new ArrayList<>();
                for(DataSnapshot post: snapshot.getChildren()){
                    NotificationModel notificationModel = post.getValue(NotificationModel.class);
                    lst.add(notificationModel);
                }
                ItemsAdapter item = new ItemsAdapter(getActivity(),lst);
                listViewNoti.setAdapter(item);
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
        private List<NotificationModel> list;
        private Context context;

        public ItemsAdapter(Context context, List<NotificationModel> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_notification, null);

            TextView tvNotiTextRoom, tvNotiTime, tvNotiData;
            ImageView imageNoti;
            tvNotiTextRoom = convertView.findViewById(R.id.tvNotiTextRoom);
            tvNotiTime = convertView.findViewById(R.id.tvNotiTime);
            tvNotiData = convertView.findViewById(R.id.tvNotiData);
            imageNoti = convertView.findViewById(R.id.imageNoti);


            String notiTextRoom;
            if (list.get(position).getEvent().equals("hot")) {
                notiTextRoom = "Nhiệt độ cao ở phòng ";
                imageNoti.setImageResource(R.drawable.ic_hot);
            }
            else if (list.get(position).getEvent().equals("cold")) {
                notiTextRoom = "Nhiệt độ thấp ở phòng ";
                imageNoti.setImageResource(R.drawable.ic_cold);
            }
            else {
                notiTextRoom = "Cháy ở phòng ";
                imageNoti.setImageResource(R.drawable.ic_fire);
            }
            notiTextRoom = notiTextRoom + list.get(position).getBuilding() + "-" + list.get(position).getRoom();
            tvNotiTextRoom.setText(notiTextRoom);

            tvNotiTime.setText(list.get(position).getTime());

            String data = list.get(position).getTemp_humid().split("-")[0]
                            + " °"
                            + list.get(position).getTemp_humid_unit().split("-")[0];
            tvNotiData.setText(data);

            return convertView;
        }
    }


}

