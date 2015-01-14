package com.circle.circleFront.chatview;

/**
 * Created by Yu on 2014/12/13.
 */
public class ChatMsgEntity {
    private String name;
    private String date;
    private String message;
    private MsgType msgType;

    /*
    *   0: Receive;  1: Send
     */
    public static enum MsgType { Receive, Send }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public MsgType getMsgType() {
        return msgType;
    }
    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String message, MsgType msgType) {
        this.name = name;
        this.date = date;
        this.message = message;
        this.msgType = msgType;
    }
}

