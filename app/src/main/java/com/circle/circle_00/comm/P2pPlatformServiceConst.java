package com.circle.circle_00.comm;

/**
 * Created by 鏅� on 2014/12/30.
 */
public class P2pPlatformServiceConst {
    //You should register BroadcastReceiver to receive the following message
    public static final int ACTION_P2P_RECEIVE_MESSAGE = 3;
    //Parameter to give you
    public static final String RECEIVE_EVENT_NAME = "com.circle.circle_00.extra.RECEIVE_EVENT_NAME";
    public static final String RECEIVE_EVENT_DATA = "com.circle.circle_00.extra.RECEIVE_EVENT_DATA";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final int ACTION_P2P_EVENT_SERVICE = 0;
    public static final int ACTION_P2P_REGISTER_CLIENT = 6;

    //application should not reach below
    //kept by us
    public static final int ACTION_P2P_TO_APPLICATION = 7;

    // You should provide the following parameters
    public static final String EVENT_NAME = "com.circle.circle_00.extra.SEND_EVENT_NAME";
    public static final String EVENT_DATA = "com.circle.circle_00.extra.SEND_EVENT_DATA";
}
