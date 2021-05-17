package com.example.temperature_humidity.ui.manageaccounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class ManageAccountsFragment extends Fragment {

    private FragmentManageAccountsBinding binding;

    ListView listView_Accounts;
    ArrayAdapter accountModelArrayAdapter;
    DatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {




        binding = FragmentManageAccountsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //hien thi action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //xoa nut back tren action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        // Init listView and dbHelper
        listView_Accounts = root.findViewById(R.id.listView_Accounts);
        databaseHelper = new DatabaseHelper(container.getContext());

        // Get simple list of items from dbHelper's getAllAccounts()
        // TODO: change database to have methods for OTHER tables/SELECTs than just Account
        accountModelArrayAdapter = new ArrayAdapter<>(container.getContext(),
                android.R.layout.simple_list_item_1, databaseHelper.getAllAccounts());
        listView_Accounts.setAdapter(accountModelArrayAdapter);

        //Toast.makeText(container.getContext(), "listView_Accounts activated", Toast.LENGTH_SHORT).show();

        binding.btnAddAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.to_add_account);
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
