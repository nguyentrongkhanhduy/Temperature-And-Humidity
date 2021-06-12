package com.example.temperature_humidity.ui.registerroom;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.GridViewAdapter;
import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentRegisterroomBinding;
import com.example.temperature_humidity.databinding.FragmentSelectroomBinding;
import com.example.temperature_humidity.model.RoomModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectRoomFragment extends Fragment {
    private FragmentSelectroomBinding binding;
    private DatabaseReference mDatabase;


    GridView gridView;
    static final String[] rooms = new String[] {
            "101", "102", "103", "104", "105",
            "201", "202", "203", "204", "205",
            "301", "302", "303", "304", "305",
            "401", "402", "403", "404", "405",
            "501", "502", "503", "504", "505"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSelectroomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        gridView = binding.gv1;
        GridViewAdapter gridViewAdapter = new GridViewAdapter(getActivity().getBaseContext(),rooms);


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, rooms);

//        View view = inflater.inflate(R.layout.fragment_selectroom, container, false);

//        List<String> btns = Arrays.asList("101","102","103","201","202","203");

//        ButtonsAdapter btnsAdapter = new ButtonsAdapter(btns,getActivity());

        gridView.setAdapter(gridViewAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(getActivity(),
//                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
        String building = getArguments().getString("building");
        binding.tvBuilding.setText("Toà " + building);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getBaseContext(),rooms[position], Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("roomname",rooms[position]); // Put anything what you want
                bundle.putString("building", building);
                RoomTimeFragment fragment2 = new RoomTimeFragment();
                fragment2.setArguments(bundle);
                Navigation.findNavController(root).navigate(R.id.to_room_time, bundle);
            }
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//    public class ButtonsAdapter extends BaseAdapter {
//        private List<String> list;
//        private Context context;
//
//        public ButtonsAdapter(List<String> list, Context context) {
//            this.list = list;
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return list.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup parent) {
//            if(view == null)
//                view = LayoutInflater.from(context).inflate(R.layout.item_button, null);
//
//            Button roomName;
//            roomName = view.findViewById(R.id.btnName);
//            roomName.setText(list.get(position));
//            return view;
//        }
//    }

}

