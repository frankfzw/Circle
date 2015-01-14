package serviceFront;

import utilHelper.UtilAssist;



import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.circle.circleFront.R;

public class ServiceFunc {
	public static boolean createUsrname(Activity act, View view){
		EditText ed1 = (EditText) act.findViewById(R.id.editText1);
		if (ed1.getText().toString().equals(null))
			return false;
		else {
			UtilAssist.insSpName(act, ed1.getText().toString());
			return true;
		}
	}
	public static void Searching(ProgressDialog prodi,final Handler hle,Activity act){
		prodi = ProgressDialog.show(act, "Search Room", "Searching......");
		new Thread(){
			public void run(){
				try{
					Thread.sleep(3000);
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putInt("Count", 0);
					msg.setData(b);
					hle.sendMessage(msg);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
}
