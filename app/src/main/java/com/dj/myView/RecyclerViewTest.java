package com.dj.myView;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dj.Fruit;
import com.dj.FruitAdapter;
import com.example.administrator.mymap.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTest extends AppCompatActivity {
    private Activity activity;
    private RecyclerView recyclerView;
    private List<Fruit> fruitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        activity = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initFruitList();
        FruitAdapterRV adapter = new FruitAdapterRV(fruitList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        /**
         * listView 点击每个item的事件
         */
//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Fruit fruit = fruitList.get(position);
//                Toast.makeText(activity, fruit.getName(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    /**
     * 初始化fruitList
     */
    private void initFruitList(){
        for(int i=0; i<2; i++){
            Fruit apple = new Fruit("apple", R.drawable.apple);
            fruitList.add(apple);
            Fruit flower1 = new Fruit("flower1", R.drawable.flower1);
            fruitList.add(flower1);
            Fruit flower2 = new Fruit("flower2", R.drawable.flower2);
            fruitList.add(flower2);
            Fruit gress = new Fruit("gress", R.drawable.gress);
            fruitList.add(gress);
            Fruit orange = new Fruit("orange", R.drawable.orange);
            fruitList.add(orange);
            Fruit orange1 = new Fruit("orange1", R.drawable.orange1);
            fruitList.add(orange1);
            Fruit banana = new Fruit("banana", R.drawable.banana);
            fruitList.add(banana);
        }
    }

}
