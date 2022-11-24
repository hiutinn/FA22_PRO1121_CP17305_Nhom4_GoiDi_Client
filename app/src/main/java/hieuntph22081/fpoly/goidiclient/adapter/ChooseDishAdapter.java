package hieuntph22081.fpoly.goidiclient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import hieuntph22081.fpoly.goidiclient.R;
import hieuntph22081.fpoly.goidiclient.model.Dish;
import hieuntph22081.fpoly.goidiclient.model.OrderDish;

public class ChooseDishAdapter extends RecyclerView.Adapter<ChooseDishAdapter.userViewHolder> {
    private List<Dish> list;
    private Context context;
    List<OrderDish> orderDishes = new ArrayList<>();
    OrderDish orderDish;
    IClickListener iClickListener;

    public interface IClickListener{
        void OnClickUpdateItem(List<OrderDish> list);
    }
    public ChooseDishAdapter(Context context,IClickListener listener) {
        this.context = context;
        this.iClickListener = listener;
    }

    public void setData(List<Dish> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_dish,parent,false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {

        Dish dish = list.get(position);
        Glide.with(context).load(dish.getImg()).into(holder.img_monAn);
        ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_XY;
        holder.img_monAn.setScaleType(scaleType);
        holder.tv_tenMonAn.setText(dish.getTen());
        holder.tv_gia.setText(String.valueOf(dish.getGia()));
        AtomicInteger soLuong = new AtomicInteger();
        soLuong.set(dish.getSoLuong());

        holder.btn_giam.setVisibility(View.INVISIBLE);
        holder.tv_soLuong.setVisibility(View.INVISIBLE);

        holder.btn_tang.setOnClickListener(v -> {
            orderDish = new OrderDish();
            soLuong.getAndIncrement();
            holder.tv_soLuong.setText(String.valueOf(soLuong.get()));
            holder.btn_giam.setVisibility(View.VISIBLE);
            holder.tv_soLuong.setVisibility(View.VISIBLE);
            orderDish.setDish(dish);
            orderDish.setQuantity(soLuong.get());

            for (OrderDish orderDish1 : orderDishes) {
                if (orderDish1.getDish().getId().equals(orderDish.getDish().getId())) {
                    orderDish1.setQuantity(soLuong.get());
//                    for(OrderDish orderDish : orderDishes){
//                        Log.e("ten",orderDish.getDish().getTen()+"");
//                        Log.e("so luong",orderDish.getQuantity()+"");
//                    }
                    iClickListener.OnClickUpdateItem(orderDishes);
                    return;
                }
            }
            orderDishes.add(orderDish);
            iClickListener.OnClickUpdateItem(orderDishes);
//            for(OrderDish orderDish : orderDishes){
//                Log.e("ten",orderDish.getDish().getTen()+"");
//                Log.e("so luong",orderDish.getQuantity()+"");
//            }
        });

        holder.btn_giam.setOnClickListener(v -> {
            if(soLuong.get()<2){
                holder.btn_giam.setVisibility(View.INVISIBLE);
                holder.tv_soLuong.setVisibility(View.INVISIBLE);
            }
            soLuong.getAndDecrement();
            holder.tv_soLuong.setText(String.valueOf(soLuong.get()));
            for (OrderDish orderDish1 : orderDishes) {
                if (orderDish1.getDish().getId().equals(orderDish.getDish().getId())) {
                    orderDish1.setQuantity(soLuong.get());
                    if(orderDish1.getQuantity()==0){
                        orderDishes.remove(orderDish1);
                    }
                    iClickListener.OnClickUpdateItem(orderDishes);
                    return;
                }
            }
            iClickListener.OnClickUpdateItem(orderDishes);
//            for(OrderDish orderDish : orderDishes){
//                Log.e("ten",orderDish.getDish().getTen()+"");
//                Log.e("so luong",orderDish.getQuantity()+"");
//            }
        });
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class userViewHolder extends RecyclerView.ViewHolder {
        ImageView img_monAn;
        TextView tv_tenMonAn,tv_gia,tv_soLuong;
        Button btn_tang,btn_giam;
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            img_monAn = itemView.findViewById(R.id.img_chooseDish);
            tv_tenMonAn = itemView.findViewById(R.id.tv_chooseDish_ten);
            tv_gia = itemView.findViewById(R.id.tv_chooseDish_gia);
            tv_soLuong = itemView.findViewById(R.id.tv_soLuong);
            btn_tang = itemView.findViewById(R.id.btn_tang);
            btn_giam = itemView.findViewById(R.id.btn_giam);
        }
    }
}
