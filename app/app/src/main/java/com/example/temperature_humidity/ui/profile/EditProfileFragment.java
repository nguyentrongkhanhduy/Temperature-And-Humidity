package com.example.temperature_humidity.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentEditprofileBinding;
import com.example.temperature_humidity.model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class EditProfileFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DashboardViewModel dashboardViewModel;
    private FragmentEditprofileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        binding = FragmentEditprofileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String userID = mAuth.getCurrentUser().getUid();
        System.out.println(userID);

        mDatabase.child("Accounts").child(userID).child("profileModel").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ProfileModel post = snapshot.getValue(ProfileModel.class);
                binding.edtID.setText(post.getId());
                binding.edtBornYear.setText(post.getBornYear());
                binding.edtEmail.setText(post.getEmail());
                binding.edtName.setText(post.getName());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.edtID.toString()) || TextUtils.isEmpty(binding.edtBornYear.toString()) ||
                        TextUtils.isEmpty(binding.edtName.toString()) || TextUtils.isEmpty(binding.edtEmail.toString()))
                {
                    Toast.makeText(getActivity(), "Không được để trống trường nào",Toast.LENGTH_SHORT).show();
                }
                else {
                    String id = binding.edtID.getText().toString();
                    String bornyear = binding.edtBornYear.getText().toString();
                    String name = binding.edtName.getText().toString();
                    String email = binding.edtEmail.getText().toString();
                    System.out.println(id + " " + bornyear + " " + name + " " + email);
                    ProfileModel edtPro5 = new ProfileModel(id, email, name, bornyear);
                    mDatabase.child("Accounts").child(userID).child("profileModel").setValue(edtPro5);
                    Navigation.findNavController(root).navigate(R.id.editprofile_to_profile);
                }
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