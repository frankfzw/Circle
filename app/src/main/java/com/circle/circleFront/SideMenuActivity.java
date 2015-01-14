package com.circle.circleFront;

import java.util.ArrayList;
import com.circle.circleFront.R;
import java.util.List;

import utilHelper.InteractiveModule;
import utilHelper.NewActionProto;
import utilHelper.SendMessage;
import utilHelper.UpadteActProto;
import utilHelper.UtilAssist;

import com.circle.circleFront.sidemenuview.NavDrawerItem;
import com.circle.circleFront.sidemenuview.NavDrawerListAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class SideMenuActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private String[] mNavMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private List<NavDrawerItem> mNavDrawerItems;
    private TypedArray mNavMenuIconsTypeArray;
    private NavDrawerListAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;

    private RoomHomeFragment roomHomeFragment;
    private RoomMemberFragment roomMemberFragment;
    private RoomActivityFragment roomActivityFragment;
    private RoomAboutFragment roomAboutFragment;
    private RoomFileFragment roomFileFragment;
    
    private InteractiveModule im;
    private Handler MyHomeHandler = new Handler(){
    	public void handleMessage(Message m){
    		Bundle b = m.getData();
    		int type = m.what;
    		switch(type){
    		case 1:
    			Log.d("TOT2", "WTFFFF");
    			String tmp = b.getString("EventName");
    			if (tmp.equals("SEND")){
    				byte[] EventData = b.getByteArray("EventData");
                    SendMessage message = (SendMessage)(UtilAssist.deserialize(EventData));
                    roomHomeFragment.receive("SomeOne", message.content);
    			}else if(tmp.equals("BROADCAST")){
    				byte[] EventData = b.getByteArray("EventData");
                    SendMessage message = (SendMessage)(UtilAssist.deserialize(EventData));
                    roomHomeFragment.receive("Broadcast", message.content);
    			}else if(tmp.equals("NEW_ACT")){
    				NewActionProto nap = (NewActionProto)
    						(UtilAssist.deserialize(b.getByteArray("EventData")));
    				roomActivityFragment.AddNewAction(nap.ti, nap.descrip, nap.type_action);
    			}else if (tmp.equals("UPDATE_ACT")){
    				Log.d("ToT3", "hhhhhhhh");
    				UpadteActProto uap = (UpadteActProto)
    						(UtilAssist.deserialize(b.getByteArray("EventData")));
    				roomActivityFragment.UpdateActInform(uap.position, uap.Selection);
    			}
    			break;
    		case 2:
    			Log.d("CIRCLE", "RECV in handler");
    			String AllPath = b.getString(utilHelper.ClientImpl.RECEIVE_FILE_KEY);
    			Log.d("VALUE", AllPath);
    			roomFileFragment.RecvFile(AllPath);
    			break;
    		case 3:
    			Log.d("WTggggg", "Here OK");
    			roomMemberFragment.updateRoomMemberList(b.getStringArrayList("devices"));
    			break;
    		default:
    			Log.d("NONONO", "HEHEEE");
    			break;
    		}
    	}
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        findView();

        if (savedInstanceState == null) {
            selectItem(0);
        }
        im = (InteractiveModule)getApplication();
        im.getClient().setHandler(im.getMainHandler());
        im.AddNewHandler("Home", MyHomeHandler);
    }

    @SuppressLint("NewApi")
    private void findView() {

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        mNavMenuIconsTypeArray = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mNavDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Room Home
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0], mNavMenuIconsTypeArray
                .getResourceId(0, -1)));
        // Room Member
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavMenuIconsTypeArray
                .getResourceId(1, -1)));
        // Room Activity
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavMenuIconsTypeArray
                .getResourceId(2, -1)));
        // Room File
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3], mNavMenuIconsTypeArray
                .getResourceId(3, -1)));
        // Room About
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[4], mNavMenuIconsTypeArray
                .getResourceId(4, -1), true, "1"));

        // Recycle the typed array
        mNavMenuIconsTypeArray.recycle();

        // setting the nav drawer list adapter
        mAdapter = new NavDrawerListAdapter(getApplicationContext(),
                mNavDrawerItems);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(this);


        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        roomHomeFragment = new RoomHomeFragment();
        roomMemberFragment = new RoomMemberFragment();
        roomActivityFragment = new RoomActivityFragment();
        roomFileFragment = new RoomFileFragment();
        roomAboutFragment = new RoomAboutFragment();
        transaction.add(R.id.content_frame, roomHomeFragment, "room_home");
        transaction.add(R.id.content_frame, roomMemberFragment, "room_member");
        transaction.add(R.id.content_frame, roomActivityFragment, "room_activity");
        transaction.add(R.id.content_frame, roomFileFragment, "room_file");
        transaction.add(R.id.content_frame, roomAboutFragment, "room_about");
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_exit:
                im.getClient().exitRoom();
                Intent intent = new Intent(this, LinkActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectItem(position);
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     * */
//    private void selectItem(int position) {
//        // update the main content by replacing fragments
//        android.support.v4.app.Fragment fragment = null;
//        switch (position) {
//            case 0:
//                fragment = new RoomHomeFragment();
//                break;
//            case 1:
//                fragment = new RoomMemberFragment();
//                break;
//            case 2:
//                fragment = new RoomActivityFragment();
//                break;
//            case 3:
//                fragment = new RoomAboutFragment();
//                break;
//            default:
//                break;
//        }
//
//        if (fragment != null) {
//            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction()
//                    .replace(R.id.content_frame, fragment).commit();
//
//            // update selected item and title, then close the drawer
//            mDrawerList.setItemChecked(position, true);
//            mDrawerList.setSelection(position);
//            setTitle(mNavMenuTitles[position]);
//            mDrawerLayout.closeDrawer(mDrawerList);
//        } else {
//            // error in creating fragment
//            Log.e("SideMenuActivity", "Error in creating fragment");
//        }
//    }

    private void selectItem(int position) {
        if(position<0 || position>4)
            return ;
        setFragment(position);

        //update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(mNavMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void setFragment(int index){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index){
            case 0:
                if (roomHomeFragment == null) {
                    roomHomeFragment = new RoomHomeFragment();
                    transaction.add(R.id.content_frame, roomHomeFragment, "room_home");
                } else {
                    transaction.show(roomHomeFragment);
                }
                break;
            case 1:
                if (roomMemberFragment == null) {
                    roomMemberFragment = new RoomMemberFragment();
                    transaction.add(R.id.content_frame, roomMemberFragment, "room_member");
                } else {
                    transaction.show(roomMemberFragment);
                }
                break;
            case 2:
                if (roomActivityFragment == null) {
                    roomActivityFragment = new RoomActivityFragment();
                    transaction.add(R.id.content_frame, roomActivityFragment, "room_activity");
                } else {
                    transaction.show(roomActivityFragment);
                }
                break;
            case 3:
            	if (roomFileFragment == null) {
            		roomFileFragment = new RoomFileFragment();
                    transaction.add(R.id.content_frame, roomFileFragment, "room_file");
                } else {
                    transaction.show(roomFileFragment);
                }
                break;
            case 4:
                if (roomAboutFragment == null) {
                    roomAboutFragment = new RoomAboutFragment();
                    transaction.add(R.id.content_frame, roomAboutFragment, "room_about");
                } else {
                    transaction.show(roomAboutFragment);
                }
                break;
            default:
                if (roomHomeFragment == null) {
                    roomHomeFragment = new RoomHomeFragment();
                    transaction.add(R.id.content_frame, roomHomeFragment, "room_home");
                } else {
                    transaction.show(roomHomeFragment);
                }
                break;
        }

        transaction.commit();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (roomHomeFragment != null) {
            transaction.hide(roomHomeFragment);
        }
        if (roomMemberFragment != null) {
            transaction.hide(roomMemberFragment);
        }
        if (roomActivityFragment != null) {
            transaction.hide(roomActivityFragment);
        }
        if(roomFileFragment != null){
        	transaction.hide(roomFileFragment);
        }
        if (roomAboutFragment != null) {
            transaction.hide(roomAboutFragment);
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    }
}