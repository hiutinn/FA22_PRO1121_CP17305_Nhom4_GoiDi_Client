package hieuntph22081.fpoly.goidiclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidiclient.adapter.ChooseDishAdapter;
import hieuntph22081.fpoly.goidiclient.adapter.OrderDishAdapter;
import hieuntph22081.fpoly.goidiclient.model.OrderDish;

public class OrderActivity extends AppCompatActivity {
    Button btn_chooseDish;
    RecyclerView recyclerView;
    OrderDishAdapter adapter;
    List<OrderDish> listOrderDish = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        recyclerView = findViewById(R.id.recycleView_dishOrder);
        btn_chooseDish = findViewById(R.id.btn_order_chooseDish);
        adapter = new OrderDishAdapter(OrderActivity.this);
        btn_chooseDish.setOnClickListener(v -> {
            Intent intent = new Intent(OrderActivity.this,ChooseDishActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("list", (Serializable) listOrderDish);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        listOrderDish();
    }
    public void listOrderDish(){
        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            listOrderDish = bundle.getParcelableArrayList("listOrderDish");
        }
        adapter.setData(listOrderDish);
        findViewById(R.id.btnCancel).setOnClickListener(v -> {

        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}