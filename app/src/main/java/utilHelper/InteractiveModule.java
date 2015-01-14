package utilHelper;

import java.util.HashMap;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class InteractiveModule extends Application{
	private HashMap<String, Handler> MyHandlerPool = new HashMap<String, Handler>();
	private Handler MainHandler;
	private ClientImpl client_wifi;
	public Handler getMainHandler(){
		return this.MainHandler;
	}
	public void AddNewHandler(String key, Handler h){
		this.MyHandlerPool.put(key, h);
	}
	public ClientImpl getClient(){
		return this.client_wifi;
	}
	public void onCreate(){
		super.onCreate();
		MainHandler = new Handler(){
			public void handleMessage(Message m){
				if (m.what == utilHelper.ClientImpl.UPDATE_ROOM_MEMBER){
					Bundle b = m.getData();
					Message neo_m = Message.obtain();
					neo_m.setData(b);
					neo_m.what = utilHelper.ClientImpl.UPDATE_ROOM_MEMBER;
					Log.d("PPPPP", b.getStringArrayList("devices").size()+"");
					MyHandlerPool.get("Home").sendMessage(neo_m);
				}else if (m.what == utilHelper.ClientImpl.RECEIVE_DATA_ACTION){
					Bundle b = m.getData();
					MessageProto msg_type = (MessageProto)(UtilAssist.deserialize((b.getByteArray(ClientImpl.RECEIVE_DATA_KEY))));
					if (msg_type.EventType == 0){
						Message neo_m = Message.obtain();
						neo_m.what = utilHelper.ClientImpl.RECEIVE_DATA_ACTION;
						Bundle bb = new Bundle();
						bb.putString("EventName", "SEND");
						bb.putByteArray("EventData", msg_type.EventData);
						neo_m.setData(bb);
						MyHandlerPool.get("Home").sendMessage(neo_m);
					}else if(msg_type.EventType == 1){
						Message neo_m = Message.obtain();
						neo_m.what = utilHelper.ClientImpl.RECEIVE_DATA_ACTION;
						Bundle bb = new Bundle();
						bb.putString("EventName", "BROADCAST");
						bb.putByteArray("EventData", msg_type.EventData);
						neo_m.setData(bb);
						MyHandlerPool.get("Home").sendMessage(neo_m);
					}else if(msg_type.EventType == 2){
						Log.d("TOT1", "Here Ok");
						Message neo_m = Message.obtain();
						neo_m.what = utilHelper.ClientImpl.RECEIVE_DATA_ACTION;
						Bundle bb = new Bundle();
						bb.putString("EventName", "NEW_ACT");
						bb.putByteArray("EventData", msg_type.EventData);
						neo_m.setData(bb);
						MyHandlerPool.get("Home").sendMessage(neo_m);
					}else{
						Message neo_m = Message.obtain();
						neo_m.what = utilHelper.ClientImpl.RECEIVE_DATA_ACTION;
						Bundle bb = new Bundle();
						bb.putString("EventName", "UPDATE_ACT");
						bb.putByteArray("EventData", msg_type.EventData);
						neo_m.setData(bb);
						MyHandlerPool.get("Home").sendMessage(neo_m);
					}
				}else if (m.what == utilHelper.ClientImpl.RECEIVE_FILE_ACTION){
					Log.d("CIRCLE", "RECV OK");
					Message neo_m = Message.obtain();
					Bundle b = m.getData();
					neo_m.what = utilHelper.ClientImpl.RECEIVE_FILE_ACTION;
					neo_m.setData(b);
					MyHandlerPool.get("Home").sendMessage(neo_m);
				}
			}
		};
		Log.d("FUCK", "MMMMMMM");
		client_wifi = new ClientImpl(getApplicationContext());
		Log.d("FUCK", "OOOOOOO");
	}
}
