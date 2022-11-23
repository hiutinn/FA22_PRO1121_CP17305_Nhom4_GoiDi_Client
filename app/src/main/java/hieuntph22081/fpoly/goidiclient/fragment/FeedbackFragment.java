package hieuntph22081.fpoly.goidiclient.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import hieuntph22081.fpoly.goidiclient.R;
import hieuntph22081.fpoly.goidiclient.adapter.FeedbackAdapter;
import hieuntph22081.fpoly.goidiclient.model.FeedBack;

public class FeedbackFragment extends Fragment {
    RecyclerView recyclerViewFeedback;
    EditText edtFeedbackContent;
    Button btnSendFeedback;
    FeedbackAdapter adapter;
    List<FeedBack> feedBacks;
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


    }
}