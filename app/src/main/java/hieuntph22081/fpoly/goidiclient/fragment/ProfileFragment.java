package hieuntph22081.fpoly.goidiclient.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidiclient.LoginActivity;
import hieuntph22081.fpoly.goidiclient.MainActivity;
import hieuntph22081.fpoly.goidiclient.R;
import hieuntph22081.fpoly.goidiclient.model.User;

public class ProfileFragment extends Fragment {
    TextView btnLogout;
    TextView tvName, tvPhone;
    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        getUserInfor();
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());
    }

    public void getUserInfor(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("users").child(MainActivity.userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null)
                    return;
                tvName.setText(user.getName());
                tvPhone.setText(user.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void logout() {
        String phone = "";
        String password = "";
        boolean status = false;
        List<Object> chkList;
        chkList = readPreference();
        if (chkList.size()>0) {
            if (Boolean.parseBoolean(chkList.get(3).toString())) {
                phone = chkList.get(1).toString();
                password = chkList.get(2).toString();
                status = Boolean.parseBoolean(chkList.get(3).toString());
            }
        }
        savePreference(MainActivity.userId,phone,password,status);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        Toast.makeText(getActivity(), "????ng xu???t th??nh c??ng!", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    void savePreference(String userId, String phone, String pw, boolean status) {
        SharedPreferences s = getActivity().getSharedPreferences("MY_FILE",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        if (!status) { // Khong luu
            editor.clear();
        } else { // luu
            editor.putString("userId",userId);
            editor.putString("phone",phone);
            editor.putString("password",pw);
            editor.putBoolean("CHK",status);
        }
        editor.commit();
    }

    List<Object> readPreference() {
        List<Object> ls = new ArrayList<>();
        SharedPreferences s = getActivity().getSharedPreferences("MY_FILE",Context.MODE_PRIVATE);
        ls.add(s.getString("userId",""));
        ls.add(s.getString("phone",""));
        ls.add(s.getString("password",""));
        ls.add(s.getBoolean("CHK",false));
        return ls;
    }
}