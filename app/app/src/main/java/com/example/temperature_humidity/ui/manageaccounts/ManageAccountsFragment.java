package com.example.temperature_humidity.ui.manageaccounts;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

import com.example.temperature_humidity.DatabaseHelper;
import com.example.temperature_humidity.DbAccountModel;
import com.example.temperature_humidity.R;

import com.example.temperature_humidity.databinding.FragementListroomaddBinding;
import com.example.temperature_humidity.databinding.FragmentHomeAdminBinding;
import com.example.temperature_humidity.databinding.FragmentManageAccountsBinding;
import com.example.temperature_humidity.databinding.FragmentRoomnameBinding;
import com.example.temperature_humidity.model.AccountModel;
import com.example.temperature_humidity.model.ProfileModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ManageAccountsFragment extends Fragment {

    DatabaseReference mDatabase;
    private FragmentManageAccountsBinding binding;

    ListView listView_Accounts;
    ArrayAdapter accountModelArrayAdapter;
    DatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();



        binding = FragmentManageAccountsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        // Init listView and dbHelper
//        listView_Accounts = root.findViewById(R.id.listView_Accounts);
//        databaseHelper = new DatabaseHelper(container.getContext());

        // Get simple list of items from dbHelper's getAllAccounts()
        // TODO: change database to have methods for OTHER tables/SELECTs than just Account
//        accountModelArrayAdapter = new ArrayAdapter<>(container.getContext(),
//                android.R.layout.simple_list_item_1, databaseHelper.getAllAccounts());
//        listView_Accounts.setAdapter(accountModelArrayAdapter);

        //Toast.makeText(container.getContext(), "listView_Accounts activated", Toast.LENGTH_SHORT).show();

        binding.btnAddAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.to_add_account);
            }
        });

        listView_Accounts = binding.listViewAccounts;



        mDatabase.child("Accounts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<AccountModel> lst = new ArrayList<>();
                for (DataSnapshot post: snapshot.getChildren()){
                    String uid= post.getKey();
                    Boolean admin = post.child("isAdmin").getValue(Boolean.class);
                    ProfileModel pro5 = post.child("profileModel").getValue(ProfileModel.class);
                    AccountModel accountModel = new AccountModel(uid, admin, pro5);
                    System.out.println(accountModel.getUid());

                    lst.add(accountModel);
                }
                ItemsAdapter item = new ItemsAdapter(getActivity().getBaseContext(),lst);
                listView_Accounts.setAdapter(item);

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
        private List<AccountModel> list;
        private Context context;

        public ItemsAdapter(Context context, List<AccountModel> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_account, null);
            TextView tvName, tvEmail, tvID;
            ImageView image;
            tvName = convertView.findViewById(R.id.tvName);
            tvEmail = convertView.findViewById(R.id.tvEmail);
            tvID = convertView.findViewById(R.id.tvID);
            image = convertView.findViewById(R.id.image);
            ProfileModel pro5 = list.get(position).getProfileModel();
            tvName.setText(pro5.getName());
            tvEmail.setText(pro5.getEmail());
            tvID.setText(pro5.getId());
            if (list.get(position).getAdmin() == true){
                image.setImageResource(R.drawable.ic_admin);
            }
            return convertView;
        }
    }
}
