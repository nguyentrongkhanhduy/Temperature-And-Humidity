package com.example.temperature_humidity.ui.usablerooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentListTimeBinding;
import com.example.temperature_humidity.databinding.FragmentUsableroomsBinding;
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

public class ListTimeFragment extends Fragment {
    FragmentListTimeBinding binding;

    private DatabaseReference mData;
    private FirebaseAuth mAuth;
    GridView gridView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        binding = FragmentListTimeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        String building_room = getArguments().getString("building_room");
        String[] arr = building_room.split("-", 2);
        String building = arr[0];
        String room = arr[1];

        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String userID = mAuth.getCurrentUser().getUid();

        mData.child("Accounts").child(userID).child("usableRooms").child(building_room).child("TimeList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        ArrayList<String> lst = new ArrayList<>();

                        for (DataSnapshot post: snapshot.getChildren()){
                            lst.add(post.getKey());
                        }
                        ArrayAdapter strAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, lst);
                        binding.lvListTime.setAdapter(strAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        binding.lvListTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ca = parent.getItemAtPosition(position).toString();
                Bundle bundle = new Bundle();
                bundle.putString("building_room", building_room);
                bundle.putString("ca", ca);
                Navigation.findNavController(root).navigate(R.id.to_under_using_room, bundle);
            }
        });

        return root;
    }
}
