package com.dj;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.mymap.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewTest extends AppCompatActivity {
    private Activity activity;
    private ListView listView;
    private List<Fruit> fruitList = new ArrayList<>();
    private String[] data = {"apple", "banana", "orange", "watermalon", "pear", "grape", "mango", "cherry",
            "apple", "banana", "orange", "watermalon", "pear", "grape", "mango", "cherry"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_test);
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        activity = this;
        listView = (ListView) findViewById(R.id.list_item);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
//        listView.setAdapter(arrayAdapter);
        initFruitList();
        FruitAdapter adapter = new FruitAdapter(activity, R.layout.fruit_item, fruitList);
        listView.setAdapter(adapter);
        /**
         * listView 点击每个item的事件
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fruit fruit = fruitList.get(position);
                Toast.makeText(activity, fruit.getName(), Toast.LENGTH_LONG).show();
            }
        });
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
