package hieuntph22081.fpoly.goidiclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hieuntph22081.fpoly.goidiclient.model.User;

public class SignupActivity extends AppCompatActivity {
    TextView tvError,tvLogin;
    EditText signupTxtPhone,signupTxtPassword,signupTxtRePassword,signupTxtName;
    Button btnSignup;
    List<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvError = findViewById(R.id.tvError);
        tvLogin = findViewById(R.id.tvLogin);
        signupTxtPhone = findViewById(R.id.signupTxtPhone);
        signupTxtPassword = findViewById(R.id.signupTxtPassword);
        signupTxtRePassword = findViewById(R.id.signupTxtRePassword);
        btnSignup = findViewById(R.id.btnSignup);
        signupTxtName = findViewById(R.id.signupTxtName);
        hideError();
        btnSignup.setOnClickListener(v -> signup());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }

    public void hideError() {
        signupTxtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signupTxtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signupTxtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signupTxtRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void signup() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");
        String phone = signupTxtPhone.getText().toString().trim();
        String password = signupTxtPassword.getText().toString().trim();
        String rePassword = signupTxtRePassword.getText().toString().trim();
        String name = signupTxtName.getText().toString().trim();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    users.add(user);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        for (User user : users) {
            if (phone.equals(user.getPhone())) {
                tvError.setText("S??? ??i???n tho???i n??y ???? ???????c ????ng k?? t??i kho???n!");
                tvError.setVisibility(View.VISIBLE);
                return;
            }
        }
        if (!password.equals(rePassword)) {
            tvError.setText("M???t kh???u nh???p l???i kh??ng tr??ng kh???p!");
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        if (phone.length() == 0 || password.length() == 0 || rePassword.length() == 0 || name.length() == 0) {
            tvError.setText("Kh??ng ????? tr???ng th??ng tin!");
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        User user = new User();
        user.setId("user" + Calendar.getInstance().getTimeInMillis());
        user.setName(name);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(1);
        user.setSoLan(0);
        myRef.child(user.getId()).setValue(user).addOnSuccessListener(unused -> {
            Toast.makeText(SignupActivity.this, "????ng k?? th??nh c??ng!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }
}