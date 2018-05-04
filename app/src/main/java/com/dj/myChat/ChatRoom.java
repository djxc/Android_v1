package com.dj.myChat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.mymap.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgrecyclerView;
    private MsgAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        initMsg();
        inputText = (EditText) findViewById(R.id.inputText);
        send = (Button) findViewById(R.id.send);
        msgrecyclerView = (RecyclerView) findViewById(R.id.msg_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgrecyclerView.setLayoutManager(layoutManager);
        msgAdapter = new MsgAdapter(msgList);
        msgrecyclerView.setAdapter(msgAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!content.equals("")){
                    Msg msg = new Msg(content,1);
                    msgList.add(msg);
                    msgAdapter.notifyItemInserted(msgAdapter.getItemCount()-1);
                    msgrecyclerView.scrollToPosition(msgAdapter.getItemCount()-1);
                    inputText.setText("");
                }
            }
        });

    }

    private void initMsg(){
        Msg msg1 = new Msg("hello!",1);
        msgList.add(msg1);
        Msg msg2 = new Msg("干什么呢!",0);
        msgList.add(msg2);
        Msg msg3 = new Msg("看电视呢，你在干涉呢？",1);
        msgList.add(msg3);
        Msg msg4 = new Msg("你猜",0);
        msgList.add(msg4);
    }

}
