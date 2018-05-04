package com.dj;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mymap.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/30.
 */

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int resourceID;

    public FruitAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Fruit> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    /**
     * getView表示当viewitem滑动到屏幕内部可见时所触发的事件
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fruit fruit = getItem(position);
        View view;
        //当convertView不为空时直接赋值给view，这样可以提高效率
        if(convertView!=null){
            view = convertView;
        }else {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
        }
        TextView fruitName = (TextView) view.findViewById(R.id.fruit_name);
        ImageView fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
        fruitImage.setImageResource(fruit.getImgId());
        fruitName.setText(fruit.getName());
        return view;
    }
}
