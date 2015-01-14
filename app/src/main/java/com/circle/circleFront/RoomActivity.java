package com.circle.circleFront;

import java.util.ArrayList;
import utilHelper.InteractiveModule;
import android.app.Activity;
import android.app.AlertDialog;

import com.circle.circleFront.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circleFront.roomview.CircleImageView;
import com.circle.circleFront.roomview.CircleLayout;
import com.circle.circleFront.roomview.CircleLayout.OnItemClickListener;
import com.circle.circleFront.roomview.CircleLayout.OnItemSelectedListener;


public class RoomActivity extends Activity implements OnItemSelectedListener, OnItemClickListener{
    private TextView selectedTextView;
    private Context mContext;
    private InteractiveModule im;
    private String SelectedName="";
    private boolean HasCreated = false;
    private ArrayList<String> rmlist = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Bundle b = getIntent().getExtras();
		rmlist = b.getStringArrayList("RoomList");
		//HashMap<String, String> outRooms = new HashMap<String, String>();
		//outRooms.put("I1", "N1");
		//outRooms.put("I2", "N2");
		
        im = (InteractiveModule)getApplication();
        int count = rmlist.size();
        Toast.makeText(this,"Number: "+count ,Toast.LENGTH_SHORT).show();
        
        mContext = getApplicationContext();
        CircleLayout circleMenu = (CircleLayout)findViewById(R.id.main_circle_layout);
        circleMenu.setOnItemSelectedListener(this);
        circleMenu.setOnItemClickListener(this);

        for (String s:rmlist) {
        	addOneRoom("0",s);
        }
        selectedTextView = (TextView)findViewById(R.id.main_selected_textView);
        if(count != 0)
            selectedTextView.setText("Name:"+((CircleImageView)circleMenu.getSelectedItem()).getName());

    }

//    private void addOneRoom(){
//    	addOneRoom("MyId", "MyName");
//    }
    private void addOneRoom(String rid, String name){
        CircleLayout circleLayout = (CircleLayout) findViewById(R.id.main_circle_layout);
        CircleImageView newItem = new CircleImageView(mContext);
        newItem.setImageResource(R.drawable.icon_room);
        newItem.setRid(rid);
        newItem.setName(name);
        circleLayout.addView(newItem);
    }

    @Override
    public void onItemSelected(View view, int position, long id, String rid, String name) {
    	if(view == null)
            return ;
    	selectedTextView.setText("Name:"+name);
        this.SelectedName = name;
    }

    @Override
    public void onItemClick(View view, int position, long id, String rid, String name) {
    	if(view == null)
            return ;
    	Log.d("FUCK", "Here is Ok");
        this.SelectedName = name;
        final EditText ed1 = new EditText(this);
        ed1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog enterAD = new AlertDialog.Builder(this)
        					.setTitle("Please enter the password")
        					.setView(ed1)
        					.setPositiveButton("Ok", new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									String pwd = ed1.getText().toString();
									boolean res = im.getClient().enterRoom(SelectedName, pwd);
									if (res){
										Toast.makeText(getApplicationContext(), "Enter room success!", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(RoomActivity.this,SideMenuActivity.class);
										startActivity(intent);
									}else{
										Toast.makeText(getApplicationContext(), "Enter room failed!", Toast.LENGTH_SHORT).show();
									}
								}
							}).create();
        enterAD.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_room) {
        	if (HasCreated == false){
        		LayoutInflater linf = LayoutInflater.from(mContext);
        		View NewRoomView = linf.inflate(R.layout.new_room, null);
        		AlertDialog roomAd = new AlertDialog.Builder(this)
        		.setTitle("Please enter the name and password of the room")
        		.setView(NewRoomView)
        		.setPositiveButton("Ok", new OnClickListener() {
				
        			@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
        				EditText ed1 = (EditText)(((AlertDialog)dialog).getWindow().findViewById(R.id.editText1));
        				EditText ed2 = (EditText)(((AlertDialog)dialog).getWindow().findViewById(R.id.editText2));
        				if (ed1.getText().toString().equals("") || ed2.getText().toString().length()<8){
        					Toast.makeText(getApplicationContext(), "SSID should not be null and password should more than 8 character", Toast.LENGTH_SHORT).show();
        				}
        				else{
        					boolean res = im.getClient().createRoom(ed1.getText().toString(), ed2.getText().toString());
        					if (res){
        						addOneRoom("0", ed1.getText().toString());
        						HasCreated = true;
        						Intent intent = new Intent(RoomActivity.this,SideMenuActivity.class);
								startActivity(intent);
        					}else{
        						Toast.makeText(getApplicationContext(), "Create Failed!", Toast.LENGTH_SHORT).show();
        					}
        				}
        			}
        		})
        		.create();
        		roomAd.show();
        		return true;
        	}else{
        		Toast.makeText(this, "You have already had one", Toast.LENGTH_SHORT).show();
        	}
        }

        return super.onOptionsItemSelected(item);
    }
}
