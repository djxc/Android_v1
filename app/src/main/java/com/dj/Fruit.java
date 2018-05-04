package com.dj;

/**
 * Created by Administrator on 2018/4/30.
 */

public class Fruit {

    private String name;
    private int imgId;

    public Fruit(String name, int imgId){
        this.name = name;
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

}
