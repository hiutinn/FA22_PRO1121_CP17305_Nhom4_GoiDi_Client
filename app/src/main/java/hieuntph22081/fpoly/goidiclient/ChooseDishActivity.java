package hieuntph22081.fpoly.goidiclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.goidiclient.adapter.ChooseDishAdapter;
import hieuntph22081.fpoly.goidiclient.model.Dish;
import hieuntph22081.fpoly.goidiclient.model.OrderDish;

public class ChooseDishActivity extends AppCompatActivity {
    ChooseDishAdapter adapter;
    RecyclerView recyclerView;
    List<Dish> list= new ArrayList<>();
    Button btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dish);

        recyclerView = findViewById(R.id.recycleView_chooseDish);
        btn_ok= findViewById(R.id.btn_ok);
        getListDishFromFireBase();
        adapter = new ChooseDishAdapter(ChooseDishActivity.this, new ChooseDishAdapter.IClickListener() {
            @Override
            public void OnClickUpdateItem(List<OrderDish> list) {
                double temp=0;
                for (OrderDish orderDish : list) {
                    temp = temp + (orderDish.getDish().getGia()*orderDish.getQuantity());
                }
                Log.e("tong",temp+"");
                btn_ok.setText(String.valueOf(temp));
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChooseDishActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void getListDishFromFireBase(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Dish");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Dish dish = dataSnapshot.getValue(Dish.class);
                    list.add(dish);
                }
                adapter.setData(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}