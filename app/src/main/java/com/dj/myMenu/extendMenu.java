package com.dj.myMenu;

import android.app.Activity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.dj.djArcMap.map1;
import com.dj.djArcMap.myLayers;
import com.dj.djtest.MyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/5.
 * 折叠菜单
 */

public class extendMenu {
    private ExpandableListView mExpandableListView = null;
    // 列表数据
    private List<String> mGroupNameList = null;
    private List<List<String>> mItemNameList = null;
    private MyAdapter mAdapter;
    private Activity activity;
    private map1 djmap;

    public extendMenu(Activity activity, ExpandableListView mExpandableListView, map1 djmap){
        this.activity = activity;
        this.mExpandableListView = mExpandableListView;
        this.djmap = djmap;

        initData();
        // 为ExpandableListView设置Adapter
        mAdapter = new MyAdapter(activity, mGroupNameList, mItemNameList);
        mExpandableListView.setAdapter(mAdapter);

        mExpandableListView.setOnGroupClickListener(createGroupLister());
        mExpandableListView.setOnChildClickListener(createChildListener());

    }

    /**
     * 菜单组点击事件
     * @return
     */
    private  ExpandableListView.OnGroupClickListener createGroupLister(){
        ExpandableListView.OnGroupClickListener groupListener = new  ExpandableListView.OnGroupClickListener(){
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (mGroupNameList.get(groupPosition).isEmpty()) {
                    return true;
                }
                return false;
            }
        };
        return groupListener;
    }

    /**
     * 菜单每个子项点击事件
     * @return
     */
    private ExpandableListView.OnChildClickListener createChildListener(){
        ExpandableListView.OnChildClickListener listener = new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(activity,
                        mAdapter.getGroup(groupPosition) + ":"
                                +  mAdapter.getChild(groupPosition, childPosition) ,
                        Toast.LENGTH_SHORT).show();
                switch (mAdapter.getChild(groupPosition, childPosition)){
                    case "添加栅格图层":
                        djmap.addRaster();
                        break;
                }
                djmap.closeDrawer();
                return false;
            }
        };
        return listener;
    }

    // 初始化数据
    private void initData(){
        // 组名
        mGroupNameList = new ArrayList<String>();
        mGroupNameList.add("图层操作");
        mGroupNameList.add("华坛明星");
        mGroupNameList.add("国外明星");
        mGroupNameList.add("政坛人物");

        mItemNameList = new  ArrayList<List<String>>();
        // 历代帝王组
        List<String> itemList = new ArrayList<String>();
        itemList.add("添加矢量图层");
        itemList.add("添加栅格图层");
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
