package com.circle.circle_00.comm;

/**
 * Created by 鏅� on 2014/11/16.
 */
public class Event {

    //Here defines SYSTEM level events

    //#notice# events in this level should NOT notice the users
    //thus only need to call handleBySystem


    public static String ADD_NEW_MEMBER = "__ADD_NEW_MEMBER__";
    public static String UPDATE_MEMBER_LIST = "__UPDATE_MEMBER_LIST__";




    //Here defines USER level events
    //Basic function we provide to users
    //#notice# events in this level should notice the users
    public static String UPDATE_ROOM_LIST = "__UPDATE_ROOM_LIST__";
    public static String CREATE_ROOM = "__CREATE_ROOM__";
    public static String ROOM_DISCONNECT = "__ROOM_DISCONNECTED__";
    public static String ENTER_ROOM = "__ENTER_ROOM__";
    public static String LEAVE_ROOM = "__LEAVE_ROOM__";
    public static String P2P = "__P2P__";
    public static String BOARDCAST = "__BOARDCAST__";

    //
}
