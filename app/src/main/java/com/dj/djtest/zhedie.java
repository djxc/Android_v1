package com.dj.djtest;

import android.app.ExpandableListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.administrator.mymap.R;

import java.util.ArrayList;
import java.util.List;

public class zhedie extends AppCompatActivity {

    private ExpandableListView mExpandableListView = null;
    // 列表数据
    private List<String> mGroupNameList = null;
    private List<List<String>> mItemNameList = null;
    // 适配器
    private MyAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhedie);

        // 获取组件
        mExpandableListView = (ExpandableListView) findViewById(R.id.expendablelistview);
        mExpandableListView.setGroupIndicator(null);

        // 初始化数据
        initData();

        // 为ExpandableListView设置Adapter
        mAdapter = new MyAdapter(this, mGroupNameList, mItemNameList);
        mExpandableListView.setAdapter(mAdapter);

        // 监听组点击
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if (mGroupNameList.get(groupPosition).isEmpty()) {
                    return true;
                }
                return false;
            }
        });

        // 监听每个分组里子控件的点击事件
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                Toast.makeText(zhedie.this,
                        mAdapter.getGroup(groupPosition) + ":"
                                +  mAdapter.getChild(groupPosition, childPosition) ,
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    // 初始化数据
    private void initData(){
        // 组名
        mGroupNameList = new ArrayList<String>();
        mGroupNameList.add("历代帝王");
        mGroupNameList.add("华坛明星");
        mGroupNameList.add("国外明星");
        mGroupNameList.add("政坛人物");

        mItemNameList = new  ArrayList<List<String>>();
        // 历代帝王组
        List<String> itemList = new ArrayList<String>();
        itemList.add("唐太宗李世民");
        itemList.add("秦始皇嬴政");
        itemList.add("汉武帝刘彻");
        itemList.add("明太祖朱元璋");
        itemList.add("宋太祖赵匡胤");
        mItemNameList.add(itemList);
        // 华坛明星组
        itemList = new ArrayList<String>();
        itemList.add("范冰冰 ");
        itemList.add("梁朝伟");
        itemList.add("谢霆锋");
        itemList.add("章子怡");
        itemList.add("杨颖");
        itemList.add("张柏芝");
        mItemNameList.add(itemList);
        // 国外明星组
        itemList = new ArrayList<String>();
        itemList.add("安吉丽娜•朱莉");
        itemList.add("艾玛•沃特森");
        itemList.add("朱迪•福斯特");
        mItemNameList.add(itemList);
        // 政坛人物组
        itemList = new ArrayList<String>();
        itemList.add("唐纳德•特朗普");
        itemList.add("金正恩");
        itemList.add("奥巴马");
        itemList.add("普京");
        mItemNameList.add(itemList);
    }


}
