package com.circle.circleFront.chatview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.circle.circleFront.R;

import java.util.List;


/**
 * Created by Yu on 2014/12/13.
 */
public class ChatMsgViewAdapter extends BaseAdapter {

    public static class IMsgViewType {
        static int CHAT_RECEIVE_MSG = 0;
        static int CHAT_SEND_MSG = 1;
    }

    private static final int TYPECOUNT = 2;
    private List<ChatMsgEntity> coll;
    private LayoutInflater mInflater;

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return coll.size();
    }

    @Override
    public Object getItem(int position) {
        return coll.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMsgEntity entity = coll.get(position);
        return entity.getMsgType() == ChatMsgEntity.MsgType.Receive ? IMsgViewType.CHAT_RECEIVE_MSG : IMsgViewType.CHAT_SEND_MSG;
    }

    @Override
    public int getViewTypeCount() {
        return TYPECOUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMsgEntity entity = coll.get(position);
        ChatMsgEntity.MsgType msgType = entity.getMsgType();
        ViewHolder viewHolder;

        if (convertView == null) {
            if (msgType == ChatMsgEntity.MsgType.Receive) {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_left, null);
            } else {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_right, null);
            }
            viewHolder = new ViewHolder();
            viewHolder.msgSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            viewHolder.msgUserName = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.msgContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            viewHolder.msgType = msgType;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.msgSendTime.setText(entity.getDate());
        viewHolder.msgUserName.setText(entity.getName());
        viewHolder.msgContent.setText(entity.getMessage());
        viewHolder.msgType = entity.getMsgType();
        return convertView;
    }

    public static class ViewHolder {
        public TextView msgSendTime;
        public TextView msgUserName;
        public TextView msgContent;
        public ChatMsgEntity.MsgType msgType = ChatMsgEntity.MsgType.Receive;
    }

}
