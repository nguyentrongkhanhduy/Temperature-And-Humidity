package com.example.temperature_humidity.ui.usablerooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.GridViewAdapter;
import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentUsableroomsBinding;
import com.example.temperature_humidity.model.UsableRooms;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UsableRoomsFragment extends Fragment {
    private FragmentUsableroomsBinding binding;
    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    GridView gridView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        binding = FragmentUsableroomsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        gridView = binding.gv2;
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        mData.child("Accounts").child(userID).child("usableRooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<String> lst = new ArrayList<>();
                for (DataSnapshot post: snapshot.getChildren()){
                    UsableRooms usableRoom = post.getValue(UsableRooms.class);
                    lst.add(usableRoom.getBuilding() + "-" + usableRoom.getRoom());
                }
                GridViewAdapter gridViewAdapter = new GridViewAdapter(root.getContext(), lst);
                gridView.setAdapter(gridViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String roomname = ((TextView)view.findViewById(R.id.tvRoomName)).getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("building_room", roomname);
                Navigation.findNavController(root).navigate(R.id.to_under_using_room, bundle);
//                Toast.makeText(root.getContext(), roomname, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
