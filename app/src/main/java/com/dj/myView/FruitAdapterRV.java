package com.dj.myView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dj.Fruit;
import com.example.administrator.mymap.R;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public class FruitAdapterRV extends RecyclerView.Adapter<FruitAdapterRV.ViewHolder> {
    private List<Fruit> fruitList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        View fruitView;

        public ViewHolder(View view){
            super(view);
            fruitView = view;
            imageView = (ImageView) view.findViewById(R.id.fruit_image);
            textView = (TextView) view.findViewById(R.id.fruit_name);
        }
    }

    public FruitAdapterRV(List<Fruit> fruitList){
        this.fruitList = fruitList;
    }

    /**
     * viewholder用来保存布局格式
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Fruit fruit = fruitList.get(position);
                Toast.makeText(v.getContext(), fruit.getName() + "嘿嘿", Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Fruit fruit = fruitList.get(position);
                Toast.makeText(v.getContext(), fruit.getName() + "图片", Toast.LENGTH_LONG).show();
            }
        });
        viewHolder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Fruit fruit = fruitList.get(position);
                Toast.makeText(v.getContext(), fruit.getName() + "整体", Toast.LENGTH_LONG).show();
            }
        });
        return viewHolder;
    }

    /**
     * 每个子项滑进屏幕时触发该事件，然后对view进行赋值
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = fruitList.get(position);
        holder.textView.setText(fruit.getName());
        holder.imageView.setImageResource(fruit.getImgId());
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }
}
