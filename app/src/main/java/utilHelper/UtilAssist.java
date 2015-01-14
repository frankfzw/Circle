package utilHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class UtilAssist {
	public static String getSpString(Activity act, String key){
		SharedPreferences sp = act.getSharedPreferences("usr_info", 0);
		return sp.getString(key, "");
	}
	public static void insSpName(Activity act, String name){
		SharedPreferences sp = act.getSharedPreferences("usr_info", 0);
		SharedPreferences.Editor sped = sp.edit();
		sped.putString("username", name);
		sped.commit();
	}
	public static void goAnother(Activity act, Class<?> cla){
		Intent intent = new Intent(act, cla);
		act.startActivity(intent);
		act.finish();
	}
	public static void goAnotherWithP(Context act, Class<?> cla, Bundle b){
		Intent intent = new Intent(act, cla);
		intent.putExtras(b);
		act.startActivity(intent);
	}
	public static Object deserialize(byte[] data){
        try{
        	ByteArrayInputStream in = new ByteArrayInputStream(data);
        	ObjectInputStream is = new ObjectInputStream(in);
        	Log.d("CIRCLE", "FUCKKKKKKKK");
        	return is.readObject();
        }catch(Exception e){
        	e.printStackTrace();
        }
		return null;
    }
	public static byte[] serialize(Object obj) {
        try{
        	ByteArrayOutputStream out = new ByteArrayOutputStream();
        	ObjectOutputStream os = new ObjectOutputStream(out);
        	Log.d("CIRCLE", "MTFUCKER3");
        	os.writeObject(obj);
        	return out.toByteArray();
        }catch(Exception e){
        	e.printStackTrace();
        }
        return null;
    }
}
