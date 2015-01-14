package com.circle.circleFront;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.circle.circleFront.chatview.ChatMsgEntity;
import com.circle.circleFront.chatview.ChatMsgViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utilHelper.InteractiveModule;
import utilHelper.MessageProto;
import utilHelper.SendMessage;
import utilHelper.UtilAssist;

/**
 * Created by Yu on 2014/12/25.
 */
public class RoomHomeFragment extends Fragment{

    private View rootView;
    private ListView mListView;
    private EditText mEditTextContent;
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    private ChatMsgViewAdapter mAdapter;
    private InteractiveModule im;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_room_home, container, false);
        initView();
        initData();
        mListView.setSelection(mAdapter.getCount() - 1);

        im = (InteractiveModule)getActivity().getApplication();

        return rootView;
    }

    public void initView() {
        mListView = (ListView) rootView.findViewById(R.id.listview);
        Button mBtnSend = (Button) rootView.findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        mEditTextContent = (EditText) rootView.findViewById(R.id.et_sendmessage);
    }

    /**
     * Faked data
     */
    private String[] nameArray = new String[]{"Circle"};
    private String[] msgArray = new String[]{"Hello"};
    private String[] dateArray = new String[]{getDate()};
    private final static int COUNT = 1;

    public void initData() {
        for (int i = 0; i < COUNT; i++) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(dateArray[i]);
            if (i % 2 == 0) {
                entity.setName(nameArray[0]);
                entity.setMsgType(ChatMsgEntity.MsgType.Receive);
            } else {
                entity.setName(nameArray[1]);
                entity.setMsgType(ChatMsgEntity.MsgType.Send);
            }
            entity.setMessage(msgArray[i]);
            mDataArrays.add(entity);
        }
        mAdapter = new ChatMsgViewAdapter(rootView.getContext(), mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    private void send() {
        String chatString = mEditTextContent.getText().toString();
        if (chatString.length() > 0) {
            ChatMsgEntity entity = new ChatMsgEntity("Me", getDate(), chatString, ChatMsgEntity.MsgType.Send);
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);

            SharedPreferences sp = getActivity().getSharedPreferences("usr_info", 0);
            int currentSendType = sp.getInt("currentSendType", 0);
            if(currentSendType == 0)
                broadcast(chatString);
            else{
                String toUserId = sp.getString("chosenUserId", "");
                Log.d("SP ID", toUserId);
                send(toUserId, chatString);
            }
        }
    }

    public void broadcast(String message){
    	MessageProto mp = new MessageProto();
    	mp.EventType = 1;
    	SendMessage sm = new SendMessage();
    	sm.content = message;
    	mp.EventData = UtilAssist.serialize(sm);
        im.getClient().broadcast(UtilAssist.serialize(mp));
    }

    public void send(String userId, String message){
    	MessageProto mp = new MessageProto();
    	mp.EventType = 0;
    	SendMessage sm = new SendMessage();
    	sm.content = message;
    	mp.EventData = UtilAssist.serialize(sm);
    	Log.d("UserId", userId);
        im.getClient().sendMessage(userId, UtilAssist.serialize(mp));
    }

    private void receive(){
        String receiveString = "Test";
        ChatMsgEntity entity = new ChatMsgEntity("Circle", getDate(), receiveString, ChatMsgEntity.MsgType.Receive);

        mDataArrays.add(entity);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount() - 1);
    }

    public void receive(String userId, String message){
        String receiveString = message;
        ChatMsgEntity entity = new ChatMsgEntity(userId, getDate(), receiveString, ChatMsgEntity.MsgType.Receive);

        mDataArrays.add(entity);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount() - 1);
    }

    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date());
    }

}
