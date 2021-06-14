package com.example.temperature_humidity.ui.registerroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentRoomtimeBinding;
import com.example.temperature_humidity.databinding.FragmentSelectroomBinding;
import com.example.temperature_humidity.model.ProfileModel;
import com.example.temperature_humidity.model.RequestModel;
import com.example.temperature_humidity.model.TimeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class RoomTimeFragment extends Fragment {

    private FragmentRoomtimeBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        binding = FragmentRoomtimeBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View root = binding.getRoot();

        Button btnDk = binding.btnDk;
        CalendarView calendarView = binding.calView;

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

//        System.out.println(getArguments().getString("roomname"));
        String building = getArguments().getString("building");
        String room = getArguments().getString("roomname");
        binding.tvBD.setText("Toà " + building);
        binding.tvRoom.setText(room);

        String userID = mAuth.getCurrentUser().getUid();


        mDatabase.child("Accounts").child(userID).child("profileModel").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        ProfileModel post = snapshot.getValue(ProfileModel.class);
                        binding.txtEmail.setText(post.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String tempDate = dayOfMonth + "/" + (month+1) + "/" + year + " ";
                binding.txtChoice.setText(tempDate);
                //Toast.makeText(getActivity(),date,Toast.LENGTH_SHORT).show();
            }
        });

        btnDk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText edtStart = (EditText) root.findViewById(R.id.edtStart);
                String startTime = binding.txtChoice.getText().toString() +
                                    edtStart.getText().toString();

                EditText edtEnd = (EditText) root.findViewById(R.id.edtEnd);
                String endTime = binding.txtChoice.getText().toString() +
                                    edtEnd.getText().toString();

                TimeModel timeModel = new TimeModel(startTime,endTime);

                // get user email, in txtEmail

                // get building + room, already in variables at line 59-60

                // create RequestModel

                String requestID = "No100";     // can only make 1 new request
                RequestModel requestModel = new
                        RequestModel(requestID,timeModel,binding.txtEmail.getText().toString(),room,building);

                mDatabase.child("Request").child(requestID).child("requestModel").setValue(requestModel);

                //Toast.makeText(getActivity(),timeModel.toString(), Toast.LENGTH_SHORT).show();

                // navigate back to RegisterRoomFragment
                // TODO
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
