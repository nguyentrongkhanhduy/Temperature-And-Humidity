package com.example.temperature_humidity.ui.manageaccounts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.temperature_humidity.R;
import com.example.temperature_humidity.databinding.FragmentAddAccountBinding;
import com.example.temperature_humidity.databinding.FragmentEditprofileBinding;
import com.example.temperature_humidity.model.AccountModel;
import com.example.temperature_humidity.model.ProfileModel;
import com.example.temperature_humidity.ui.profile.DashboardViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AddAccountFragment extends Fragment {

    private DatabaseReference mDatabase;
    private DashboardViewModel dashboardViewModel;
    private FragmentAddAccountBinding binding;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        binding = FragmentAddAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);


        CheckBox cbIsAdmin = binding.cbIsAdmin;
        Button btnConfirm = binding.btnConfirmAddAccount;


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtEmail = binding.edtEmail;
                EditText edtPassword = binding.edtPassword;
                EditText edtRePassword = binding.edtRePassword;
                EditText edtID = binding.edtID;
                EditText edtName = binding.edtName;
                EditText edtBornYear = binding.edtBornYear;
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String repassword = edtRePassword.getText().toString();
                String id = edtID.getText().toString();
                String name = edtName.getText().toString();
                String bornyear = edtBornYear.getText().toString();

                if (TextUtils.isEmpty(edtEmail.getText()) || TextUtils.isEmpty(edtPassword.getText())||
                    TextUtils.isEmpty(edtRePassword.getText()) || TextUtils.isEmpty(edtID.getText())||
                    TextUtils.isEmpty(edtBornYear.getText()) || TextUtils.isEmpty(edtName.getText()))
                {
                    Toast.makeText(getActivity(),"Không được để trống trường nào !", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (password.equals(repassword)){
                        if (cbIsAdmin.isChecked()){
                            Signup(email,password,id,name,bornyear, true);
                        }
                        else{
                            Signup(email,password,id,name,bornyear, false);
                        }
                    }
                    else {
                        Toast.makeText(getActivity(),"Mật khẩu nhập lại không trùng khớp !", Toast.LENGTH_SHORT).show();
                    }
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

    private void Signup(String email, String pass, String id, String name, String year, Boolean isAdmin){
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(),"Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = task.getResult().getUser();
                    String uid = user.getUid();
                    ProfileModel pro5 = new ProfileModel(id, email, name, year);
                    AccountModel accountModel = new AccountModel(uid, isAdmin, pro5);
//                    mDatabase.child("Accounts").child(uid).setValue(accountModel);
                    mDatabase.child("Accounts").child(uid).child("uid").setValue(uid);
                    mDatabase.child("Accounts").child(uid).child("isAdmin").setValue(isAdmin);
                    mDatabase.child("Accounts").child(uid).child("profileModel").setValue(pro5);
                }
                else{
                    Toast.makeText(getActivity(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}