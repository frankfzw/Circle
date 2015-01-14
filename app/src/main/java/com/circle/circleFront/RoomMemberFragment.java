package com.circle.circleFront;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Yu on 2014/12/25.
 */
public class RoomMemberFragment extends ListFragment {

    private ArrayList<HashMap<String, Object>> userList;
    private SimpleAdapter sa;
    private String[] userChatNames;
    private String[] userChatSigns;
    private TypedArray userChatAvatars;

    private SendType currentSendType;
    private int chosnUserPosition;
    private String chosnUserId;
    //private static Set chosenSet = new HashSet<Integer>();

    // Broadcast: 0;
    // Single: 1;
    public static enum SendType { Broadcast, Single }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userChatNames = getActivity().getResources().getStringArray(R.array.user_chat_names);
        userChatSigns = getActivity().getResources().getStringArray(R.array.user_chat_signs);
        userChatAvatars = getActivity().getResources().obtainTypedArray(R.array.user_chat_avatars);
        //userChatAvatars.recycle();
        currentSendType = SendType.Broadcast;
        SharedPreferences sp = getActivity().getSharedPreferences("usr_info", 0);
        SharedPreferences.Editor edit = sp.edit();
        
        chosnUserPosition = -1;
        chosnUserId = "";
        edit.putInt("currentSendType", currentSendType.ordinal());
        edit.putString("chosenUserId", chosnUserId);
        edit.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.fragment_room_member, container, false);

        userList = new ArrayList<HashMap<String,Object>>();
        sa = new SimpleAdapter(getActivity(), userList, R.layout.fragment_room_member_entity,
                new String[]{"ib_photo", "tv_name", "tv_sign"},
                new int[]{R.id.ib_photo, R.id.tv_name, R.id.tv_sign});
        setListAdapter(sa);

        return rootView;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(currentSendType == SendType.Broadcast){
            currentSendType = SendType.Single;
            String userId = ((TextView)v.findViewById(R.id.tv_name)).getText().toString();
            Toast.makeText(getActivity(),
                    userId + " Chosen",
                    Toast.LENGTH_SHORT).show();
            chosnUserId = userId;
            chosnUserPosition = position;
            v.setBackgroundResource(R.color.room_member_chosen);
        }
        else{
            String userId = ((TextView)v.findViewById(R.id.tv_name)).getText().toString();
            if(position == chosnUserPosition && userId.equals(chosnUserId)){
                currentSendType = SendType.Broadcast;
                chosnUserPosition = -1;
                chosnUserId = "";
                Toast.makeText(getActivity(),
                    userId + " Not Chosen",
                    Toast.LENGTH_SHORT).show();

                v.setBackgroundResource(R.color.room_member_unchosen);
            }
            else{
                for(int i=0; i<getListView().getChildCount(); i++)
                    getListView().getChildAt(i).setBackgroundResource(R.color.room_member_unchosen);
                currentSendType = SendType.Single;
                chosnUserId = userId;
                chosnUserPosition = position;

                Toast.makeText(getActivity(),
                        userId + " Chosen",
                        Toast.LENGTH_SHORT).show();

                v.setBackgroundResource(R.color.room_member_chosen);
            }
        }
        SharedPreferences sp = getActivity().getSharedPreferences("usr_info", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("currentSendType", currentSendType.ordinal());
        edit.putString("chosenUserId", chosnUserId);
        edit.commit();    
    }

    public void updateRoomMemberList(ArrayList<String> roomMemberList){
        for(int i=0; i<getListView().getChildCount(); i++)
            getListView().getChildAt(i).setBackgroundResource(R.color.room_member_unchosen);

        userList.clear();
        currentSendType = SendType.Broadcast;
        chosnUserPosition = -1;
        chosnUserId = "";
        
        Log.d("FUCK", ""+roomMemberList.size());
        
        for(int i=0; i<roomMemberList.size(); i++)
        {
            String userId = roomMemberList.get(i);
            int resourceId = Math.abs(userId.hashCode()) % 5;
            //int randomId = (int)(Math.random()*5);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ib_photo", userChatAvatars.getResourceId(resourceId, -1));
            map.put("tv_name", userId);
            map.put("tv_sign", userChatSigns[resourceId]);
            userList.add(map);
        }

        sa.notifyDataSetChanged();
    }
}
