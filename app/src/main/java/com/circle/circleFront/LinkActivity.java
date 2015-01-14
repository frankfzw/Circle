package com.circle.circleFront;

import java.util.ArrayList;
import utilHelper.InteractiveModule;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.circle.circleFront.R;

public class LinkActivity extends Activity {
	private ProgressDialog prdia;
	private InteractiveModule im;
	private ArrayList<String> rmlist = new ArrayList<>();
	private Handler myhandler = new Handler(){
		public void handleMessage(Message msg){
			prdia.dismiss();
			Bundle b = msg.getData();
			Intent intent = new Intent(getApplicationContext(),RoomActivity.class);
            intent.putExtras(b);
            startActivity(intent);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_link);
		//TelephonyManager tem = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		//tv1.setText(UtilAssist.getSpString(this, "username")+"@"+tem.getDeviceId());
		im = (InteractiveModule)getApplication();
		im.AddNewHandler("Search", myhandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.link, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void SearchRoom(View v){
        prdia = ProgressDialog.show(this, "Search Room", "Searching...");
        new Thread(){
            public void run(){
                try{
                    rmlist = (ArrayList<String>) im.getClient().searchRoom();
                    Message msg = Message.obtain();
                    Bundle b = new Bundle();
                    b.putStringArrayList("RoomList", rmlist);
                    msg.setData(b);
                    myhandler.sendMessage(msg);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
}
