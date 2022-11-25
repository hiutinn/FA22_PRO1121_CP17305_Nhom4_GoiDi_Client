package hieuntph22081.fpoly.goidiclient.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hieuntph22081.fpoly.goidiclient.MainActivity;
import hieuntph22081.fpoly.goidiclient.OrderActivity;
import hieuntph22081.fpoly.goidiclient.R;
import hieuntph22081.fpoly.goidiclient.adapter.FeedbackAdapter;
import hieuntph22081.fpoly.goidiclient.model.FeedBack;
import hieuntph22081.fpoly.goidiclient.model.User;

public class FeedbackFragment extends Fragment {
    RecyclerView recyclerViewFeedback;
    EditText edtFeedbackContent;
    Button btnSendFeedback;
    FeedbackAdapter adapter;
    List<FeedBack> feedBacks = new ArrayList<>();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    // Appearance
    public FeedbackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewFeedback = view.findViewById(R.id.recyclerViewFeedback);
        edtFeedbackContent = view.findViewById(R.id.edtFeedbackContent);
        btnSendFeedback = view.findViewById(R.id.btnSendFeedback);
        showListFeedback();
        btnSendFeedback.setOnClickListener(v -> sendFeedback());
    }

    private void sendFeedback() {
        if (edtFeedbackContent.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Bạn chưa nhập gì cả !!!", Toast.LENGTH_SHORT).show();
            return;
        }

        myRef.child("users").child(MainActivity.userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(getContext(), "Không lấy được user", Toast.LENGTH_SHORT).show();
                    return;
                }
                FeedBack feedBack = new FeedBack();
                feedBack.setId("feedback" + Calendar.getInstance().getTimeInMillis());
                feedBack.setContent(edtFeedbackContent.getText().toString().trim());
                String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
                feedBack.setDate(date);
                feedBack.setUser(user);
                myRef.child("feedbacks").child(feedBack.getId()).setValue(feedBack).addOnSuccessListener(unused
                        -> openSuccessDialog("Nhận xét của bạn đã được gửi đi, cảm ơn đã góp ý."));
                edtFeedbackContent.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void openSuccessDialog (String text) {
        Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success_notification);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvNotifyContent = dialog.findViewById(R.id.tvNotifyContent);
        tvNotifyContent.setText(text);
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
            dialog.dismiss();
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();
    }

    public void showListFeedback() {
        adapter = new FeedbackAdapter(getContext(), feedBacks);
        getAllFeedbacks();
        recyclerViewFeedback.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerViewFeedback.setAdapter(adapter);
    }

    public void getAllFeedbacks() {
        myRef.child("feedbacks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FeedBack feedBack = snapshot.getValue(FeedBack.class);
                if (feedBack != null) {
                    feedBacks.add(feedBack);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FeedBack feedBack = snapshot.getValue(FeedBack.class);
                if (feedBack == null || feedBacks == null || feedBacks.isEmpty()) {
                    return;
                }
                for (int i = 0; i < feedBacks.size(); i++) {
                    if (feedBack.getId().equals(feedBacks.get(i).getId())) {
                        feedBacks.set(i, feedBack);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                FeedBack feedBack = snapshot.getValue(FeedBack.class);
                if (feedBack == null || feedBacks == null || feedBacks.isEmpty()) {
                    return;
                }
                for (int i = 0; i < feedBacks.size(); i++) {
                    if (feedBack.getId().equals(feedBacks.get(i).getId())) {
                        feedBacks.remove(feedBacks.get(i));
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}