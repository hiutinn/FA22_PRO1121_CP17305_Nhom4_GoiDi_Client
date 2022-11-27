package hieuntph22081.fpoly.goidiclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.List;

import hieuntph22081.fpoly.goidiclient.R;
import hieuntph22081.fpoly.goidiclient.model.Order;
import hieuntph22081.fpoly.goidiclient.model.Table;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder>{
    Context context;
    List<Order> orders;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    public MyOrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyOrdersViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersViewHolder holder, int position) {
        Order order = orders.get(position);
        String tablesStr = "Bàn: ";
        List<Table> mTables = order.getTables();
        if (mTables != null) {
//            Toast.makeText(context, ""+mTables.size(), Toast.LENGTH_SHORT).show();
            for (Table table : mTables) {
                tablesStr += table.getNumber();
                if (mTables.indexOf(table) == mTables.size()-1)
                    break;
                tablesStr +=  " , ";
            }
            holder.tvOrderTable.setText(tablesStr);
        }
        holder.tvOrderDate.setText(order.getDate());
        holder.tvOrderTime.setText(order.getStartTime() + " - " + order.getEndTime());
        switch (order.getStatus()) {
            case 0:
                holder.tvOrderStatus.setText("Đang chờ");
                holder.tvOrderStatus.setTextColor(Color.YELLOW);
                holder.tvHuy.setVisibility(View.VISIBLE);
                holder.tvHuy.setOnClickListener(v -> {
                    myRef.child("orders").child(order.getId()).child("status").setValue(3);
                });
                break;
            case 1:
                holder.tvOrderStatus.setText("Đang dùng");
                holder.tvOrderStatus.setTextColor(Color.BLUE);
                break;
            case 2:
                holder.tvOrderStatus.setText("Đã xong");
                holder.tvOrderStatus.setTextColor(Color.GREEN);
                holder.tvOrderStatus.setVisibility(View.VISIBLE);
                holder.tvOrderTotal.setText("Tổng tiền: " + formatCurrency(order.getTotal()));
                break;
            case 3:
                holder.tvOrderStatus.setText("Hủy");
                holder.tvOrderStatus.setTextColor(Color.RED);
                holder.itemView.setEnabled(false);
                break;
        }
    }

    public String formatCurrency(double money) {
        String pattern="###,###.### VNĐ";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(money);
    }

    @Override
    public int getItemCount() {
        if (orders != null)
            return orders.size();
        return 0;
    }

    class MyOrdersViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderTable, tvOrderDate, tvOrderTime, tvOrderStatus, tvHuy, tvOrderTotal;
        public MyOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderTable = itemView.findViewById(R.id.tvOrderTable);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvHuy = itemView.findViewById(R.id.tvHuy);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
        }
    }
}
