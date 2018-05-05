package com.dj.myMenu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;

import com.dj.djArcMap.map1;
import com.dj.djArcMap.myLayers;
import com.dj.djArcMap.myMapViewAction;
import com.dj.djArcMap.mySketchEditor;
import com.example.administrator.mymap.R;

/**
 * Created by Administrator on 2018/5/5.
 */

public class menuAction {
    private MenuItem addFeatureLayer;
    private MenuItem showLatLon;
    private Menu menu;
    private MenuInflater inflater;
    private mySketchEditor msketchEditor;
    private Activity activity;
    private myMapViewAction mapViewAction;
    private map1 mymap;

    public menuAction(Menu menu, MenuInflater inflater, mySketchEditor msketchEditor, map1 mymap, myMapViewAction mapViewAction){
        this.menu = menu;
        this.inflater = inflater;
        this.msketchEditor = msketchEditor;
        this.mymap = mymap;
        this.mapViewAction = mapViewAction;
    }

    /**
     * 创建菜单
     * @return
     */
    public Menu createMenu(){
        inflater.inflate(R.menu.undo_redo_stop_menu, menu);
        showLatLon = menu.getItem(6);
        showLatLon.setCheckable(true);
        //创建车型菜单
        SubMenu carStyle=menu.addSubMenu("车型");
        //弹出框的标题
        carStyle.setHeaderTitle("请选择车型");
        //弹出框的图标
        carStyle.setHeaderIcon(R.drawable.apple);
        carStyle.add(0,0x111,0,"奔驰");
        carStyle.add(0,0x112,0,"宝马");
        carStyle.add(0,0x113,0,"奥迪");
        carStyle.add(0,0x114,0,"大众");

        //设置carStyle菜单内0组的菜单项为单选菜单项
        /**
         * setGroupCheckAble(int group,boolean checkable,boolean exclusive)
         * 设置group组里的所有菜单项是否可以勾选
         * exclusive设为true，将是一组单选菜单
         */
        carStyle.setGroupCheckable(0, true, true);

        //创建地区菜单
        SubMenu area=menu.addSubMenu("地区");
        area.setHeaderIcon(R.drawable.banana);
        area.setHeaderTitle("选择地区");
        area.add(0, 0x211, 0, "北京").setCheckable(true);
        area.add(0, 0x212, 0, "上海").setCheckable(true);
        area.add(0, 0x213, 0, "山东").setCheckable(true);
        area.add(0, 0x214, 0, "杭州").setCheckable(true);
        return menu;
    }


    /**
     * 每个菜单项点击事件
     * @param item
     * @return
     */
    public boolean menuItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.undo:
                msketchEditor.undo();
                return true;
            case R.id.redo:
                msketchEditor.redo();
                return true;
            case R.id.stop:
                msketchEditor.stop();
                return true;
            case R.id.start:
                mymap.startEdit();
                return true;
            case R.id.query:
                mymap.QueryTest();
                return true;
            case R.id.addShapefile:
                mymap.addFeatureLayer();
                return true;
            case R.id.showLatLon:
                ChangeChecked(item);
                mapViewAction.edit_showLatLon();
                return true;
            case R.id.selectFeature:
                mapViewAction.edit_canSelect();
                return true;
            case 0x211:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                return true;
            case  0x212:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                return true;

            case  0x213:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                return true;

            case  0x214:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                return true;
            case  0x112:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                return true;
            case  0x113:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                return true;
            case  0x114:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                return true;
            case  0x111:
                if(item.isChecked()){
                    item.setChecked(false);
                }
                else{
                    item.setChecked(true);
                }
                return true;
        }
        return false;
    }

    /**
     * 改变菜单项选择的状态
     * @param item
     */
    private void ChangeChecked(MenuItem item){
        if(item.isChecked()){
            item.setChecked(false);
        }
        else{
            item.setChecked(true);
        }
    }

}
