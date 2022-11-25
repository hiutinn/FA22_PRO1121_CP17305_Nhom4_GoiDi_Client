package hieuntph22081.fpoly.goidiclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hieuntph22081.fpoly.goidiclient.adapter.ChooseDishAdapter;
import hieuntph22081.fpoly.goidiclient.adapter.OrderDishAdapter;
import hieuntph22081.fpoly.goidiclient.model.Order;
import hieuntph22081.fpoly.goidiclient.model.OrderDish;
import hieuntph22081.fpoly.goidiclient.model.User;

public class OrderActivity extends AppCompatActivity {
    EditText edtDate, edtStartTime, edtNote, edtNoP;
    Button btnChooseDish, btnOrder, btnCancel;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    User currentUser;
    int mHour, mMinute;
    RecyclerView recycleView_dishOrder;
    List<OrderDish> listOrderDish;
    OrderDishAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        recycleView_dishOrder = findViewById(R.id.recycleView_dishOrder);
        edtDate = findViewById(R.id.edtDate);
        edtStartTime = findViewById(R.id.edtStartTime);
        edtNote = findViewById(R.id.edtNote);
        edtNoP = findViewById(R.id.edtNoP);
        btnChooseDish = findViewById(R.id.btn_order_chooseDish);
        btnOrder = findViewById(R.id.btnOrder);
        btnCancel = findViewById(R.id.btnCancel);

         edtDate.setOnClickListener(v -> datePickerDialog(edtDate));
        edtStartTime.setOnClickListener(v -> timePickerDialog(edtStartTime));

       
        btnOrder.setOnClickListener(v -> sendOrder());
        btnCancel.setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, MainActivity.class));
            finish();
        });
      
        btnChooseDish.setOnClickListener(v -> {
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
            if (!listOrderDish.isEmpty())
                btnChooseDish.setText("Chọn lại");
            else
                btnChooseDish.setText("Chọn món");
        }

        recycleView_dishOrder.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new OrderDishAdapter(this);
        adapter.setData(listOrderDish);

        recycleView_dishOrder.setAdapter(adapter);


       
    }

    private void sendOrder() {
        String date = edtDate.getText().toString().trim();
        String startTime = edtStartTime.getText().toString().trim();
        String note = edtNote.getText().toString().trim();
        String numberOfPeople = edtNoP.getText().toString().trim();

        if (!validate()) return;

        myRef.child("users").child(MainActivity.userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                Order order = new Order();
                order.setId("order" + Calendar.getInstance().getTimeInMillis());
                order.setDate(date);
                order.setStartTime(startTime);

                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                try {
                    Date d = df.parse(startTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, 30);
                    String endTime = df.format(cal.getTime());
                    order.setEndTime(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                order.setDishes(listOrderDish);
                order.setNote(note);
                order.setNumberOfPeople(Integer.parseInt(numberOfPeople));
                order.setUser(currentUser);
                myRef.child("orders").child(order.getId()).setValue(order).addOnSuccessListener(unused
                        -> openSuccessDialog());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openSuccessDialog () {
        Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success_notification);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, MainActivity.class));
            finish();
            dialog.dismiss();
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.show();
    }

    public boolean validate() {
        String date = edtDate.getText().toString().trim();
        String startTime = edtStartTime.getText().toString().trim();
        String numberOfPeople = edtNoP.getText().toString().trim();
        if (date.length() == 0) {
            Toast.makeText(this, "Hãy nhập ngày!", Toast.LENGTH_SHORT).show();
            edtDate.requestFocus();
            return false;
        }

        if (startTime.length() == 0) {
            Toast.makeText(this, "Hãy nhập thời gian bắt đầu!", Toast.LENGTH_SHORT).show();
            edtStartTime.requestFocus();
            return false;
        }
        if (numberOfPeople.length() == 0) {
            Toast.makeText(this, "Hãy nhập số người!", Toast.LENGTH_SHORT).show();
            edtNoP.requestFocus();
            return false;
        }

        if (!validateTime(startTime, date)) {
            Toast.makeText(this, "Ngày, giờ bạn nhập không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void datePickerDialog(EditText editText){
        Calendar calendar = Calendar.getInstance();
        int day1,month1,year1;
        day1 = calendar.get(Calendar.DAY_OF_MONTH);
        month1 = calendar.get(Calendar.MONTH);
        year1 = calendar.get(Calendar.YEAR);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog( this, (view, year, month, dayOfMonth) -> {
            calendar.set(year,month,dayOfMonth);
            editText.setText(simpleDateFormat.format(calendar.getTime()));
        },year1,month1,day1);
        datePickerDialog.show();
    }

    private void timePickerDialog(EditText editText){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute) -> {
            mHour = hourOfDay;
            mMinute = minute;
            editText.setText(String.format(Locale.getDefault(), "%02d:%02d",hourOfDay,minute));
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private boolean validateTime (String time, String date) {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String mTime = date + " " + time;
        try {
            Date currentTime = Calendar.getInstance().getTime();
            Date ten = parser.parse(mTime);
            if (ten.compareTo(currentTime) < 0) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}