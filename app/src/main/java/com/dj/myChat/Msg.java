package com.dj.myChat;

/**
 * Created by Administrator on 2018/5/1.
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    private String content;
    private int type;

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public Msg(String content, int type){
        this.content = content;
        this.type = type;
    }

}
