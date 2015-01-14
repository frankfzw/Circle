package utilHelper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import utilHelper.Client;
import utilHelper.CircleMessage;
import utilHelper.ScanClient;
import utilHelper.AddMemberProto;
import utilHelper.ProtocolParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Frank on 1/11/2015.
 */
public class ClientImpl extends Client{
    private Context context;
    private WifiAdmin wifiAdmin;
    private static final int PORT = 9999;

    //thread of socket server
    private Thread sockThread;

    //thread of updateMemberList
    private Thread updateMemberListThread;

    //private send buffer
    private ArrayBlockingQueue <CircleMessage> sendmessageQueue;

    public static final String PREFIX = "_ADHOC_";

    public static String RECEIVE_DATA_KEY = "__WHAT_THE_FUCK__";

    public static String RECEIVE_FILE_KEY = "__WHAT_THE_FUCK_2__";

    public static int RECEIVE_DATA_ACTION = 1;
    
    public static int UPDATE_ROOM_MEMBER = 3;

    public static int RECEIVE_FILE_ACTION = 2;

    private static Handler handler;

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        ClientImpl.handler = handler;
    }

    private String depUniqueId = "###";
    private static String DEPENDENCY = "###";

    private Hashtable<String, CircleMessage> recvMessageBuffer;

    private ClientImpl me = this;



    public ClientImpl(Context context) {
        this.context = context;
        wifiAdmin = new WifiAdmin(context);
        this.handler = null;

        setMyRole(Role.Invalid);
        setMyIP(0);
        setHostIP(0);
        setMemberList(new HashMap<String, ScanClient>());
        setRoomList(new ArrayList<String>());

        //set two threads to null
        sockThread = null;
        updateMemberListThread = null;

        sendmessageQueue = new ArrayBlockingQueue<CircleMessage>(16);
        recvMessageBuffer = new Hashtable<String, CircleMessage>();
        //new a thread to do socket listening
        //runSocketServer();

    }

    public List<String> searchRoom() {
        //Toast.makeText(context, "searching...", Toast.LENGTH_LONG).show();
        wifiAdmin.scan();
        List<String> temp = wifiAdmin.scanResult();
        ArrayList<String> res = new ArrayList<>();
        if (temp == null) {
            //Toast.makeText(context, "Search failed", Toast.LENGTH_SHORT).show();
            return res;
        }
        for (String s : temp) {
            if (s.length() < 8) continue;

            if (s.substring(0, 7).equals(PREFIX)) {
                res.add(s.substring(7));
            }
        }
        setRoomList(res);
        return res;
    }

    public boolean createRoom(String name, String pwd) {

        //check the status of ClientImpl
        if (getMyRole() != Role.Invalid) {
            Log.d(Client.TAG, "create room, role check failed!!");
            return false;
        }
        if (updateMemberListThread != null) {
            Log.d(Client.TAG, "create room, update member list thread is not be recall correctly!!");
            return false;
        }
        if (sockThread != null) {
            Log.d(Client.TAG, "create room, socket server thread is not be recall correctly!!");
            return false;
        }

        String realName = PREFIX + name;
        //String realName = name;
        boolean res = wifiAdmin.startAp(realName, pwd, true);
        if (res) {
            setMyRole(Role.Host);
            //start to keep updating member list
            updateMemberList();
            //Toast.makeText(context, "create successfully: " + name, Toast.LENGTH_LONG).show();
            //check and start the socket server
            runSocketServer();
            Log.d(Client.TAG, "create room succeeded!!!");
        } else {
            Log.d(Client.TAG, "create room failed!!!");
        }
        return res;


    }

    public boolean enterRoom(String name, String pwd) {
        //check the status of ClientImpl
        if (getMyRole() != Role.Invalid) {
            Log.d(Client.TAG, "create room, role check failed!!");
            return false;
        }
        if (updateMemberListThread != null) {
            Log.d(Client.TAG, "create room, update member list thread is not be recall correctly!!");
            return false;
        }
        if (sockThread != null) {
            Log.d(Client.TAG, "create room, socket server thread is not be recall correctly!!");
            return false;
        }

        String realName = PREFIX + name;
        boolean flag = wifiAdmin.enterAp(realName, pwd);
        if (flag == false) {
            //Toast.makeText(context, "enter room failed" + name, Toast.LENGTH_LONG).show();
            Log.d(Client.TAG, "enter room failed!!!");
            return false;
        } else {
            //Toast.makeText(context, "enter room successfully" + name, Toast.LENGTH_LONG).show();
            //setMyIP(wifiAdmin.getIP());
            //setHostIP(wifiAdmin.getHostIP());
            Log.d(Client.TAG, "enter room succeeded!!!");
            setMyRole(Role.Client);

            //start socket server
            runSocketServer();
            return true;
        }
    }

    public boolean exitRoom() {
        boolean res;
        if (getMyRole() == Role.Host) {
            res = wifiAdmin.startAp(getRoomName(), "", false);
            if (updateMemberListThread != null) {
                updateMemberListThread.interrupt();
                updateMemberListThread = null;
            }
        } else {
            res = wifiAdmin.disableWifi();
        }

        if (sockThread != null) {
            sockThread.interrupt();
            sockThread = null;
        }

        //clear status and buffer
        setMyRole(Role.Invalid);
        setMyIP(0);
        setHostIP(0);
        setMemberList(new HashMap<String, ScanClient>());
        setRoomList(new ArrayList<String>());
        sendmessageQueue.clear();
        return res;
    }

    public void sendMessage(final String name,  final byte[] data) {
        realSendMessage(name, data, false);
    }

    public void broadcast(byte [] data){
        realBroadcast(data, false);
    }

    public void transferFile(File file){

        Iterator<Map.Entry<String, ScanClient>> iterator = getMemberList().entrySet().iterator();
        while(iterator.hasNext()){
            String to = iterator.next().getValue().getIpAddr();
            CircleMessage circleMessage = new CircleMessage();
            circleMessage.isSystem = false;
            circleMessage.from = ipStr(getMyIP());
            circleMessage.to = to;
            circleMessage.type = CircleMessage.TYPE.FILE;
            circleMessage.detail = file.getName();

            InputStream fileStream = null;
            try {
                fileStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, Log.getStackTraceString(e));
                return;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int r = 0;
                try {
                    r = fileStream.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, Log.getStackTraceString(e));
                    break;
                }
                if (r == -1) break;
                out.write(buffer, 0, r);
            }

            circleMessage.content = out.toByteArray();
            sendmessageQueue.offer(circleMessage);

        }
        sendMessageHelper();
    }

    public String getMyIPStr() {
        return ipStr(getMyIP());
    }

    public String getHostIPStr() {
        return ipStr(getHostIP());
    }

    public String ipStr(int ip) {
        return String.format("%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));
    }

    private void realSendMessage(final String name,  final byte[] data, final boolean isSystem) {
        if (getMemberList().get(name) == null) {
            Log.d(Client.TAG, "No member connect: " + name);
            return;
        }
        CircleMessage message = new CircleMessage();
        message.type = CircleMessage.TYPE.NORMAL;
        message.content = data;
        message.from = getMyIPStr();
        message.to = getMemberList().get(name).getIpAddr();
        message.isSystem = isSystem;
        sendmessageQueue.offer(message);
        sendMessageHelper();
    }

    private void realBroadcast(byte [] data, boolean isSystem) {
        for (Map.Entry<String, ScanClient> client: getMemberList().entrySet()) {
            realSendMessage(client.getValue().getDevice(), data, isSystem);
        }
    }

    private boolean runSocketServer(){
        try{
            //Log.d(TAG, "run socket server");
            Log.d(Client.TAG, "socket server running .....");

            final ServerSocket serverSocket = new ServerSocket(PORT);
            sockThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {

                        try {
                            Socket client = serverSocket.accept();
                            Log.d(Client.TAG, "hello~ receive packet from "+ client.getInetAddress());
                            InputStream in = client.getInputStream();
                            CircleMessage message = ProtocolParser.parseSocket(in);
                            synchronized (this){

                            }
                            if(message.isSystem){
                                //default system is handle new member
                                handleAddNewMember(message);
                            } else if (message.type == CircleMessage.TYPE.FILE) {
                                FileOutputStream fos = new FileOutputStream(Configure.TEMP_FILE_PATH + message.detail);
                                fos.write(message.content);
                                fos.close();
                                android.os.Message osMessage = android.os.Message.obtain();
                                osMessage.what = RECEIVE_FILE_ACTION;
                                Bundle bundle= new Bundle();
                                bundle.putString(ClientImpl.RECEIVE_FILE_KEY,message.detail);
                                osMessage.setData(bundle);
                                handler.sendMessage(osMessage);
                            }
                            else{
                                if (handler != null) {
                                    android.os.Message osMessage = android.os.Message.obtain();
                                    osMessage.what = RECEIVE_DATA_ACTION;
                                    Bundle bundle= new Bundle();
                                    bundle.putByteArray(ClientImpl.RECEIVE_DATA_KEY,message.content);
                                    osMessage.setData(bundle);
                                    handler.sendMessage(osMessage);
                                }
                            }
                            in.close();
                            client.close();
                        }
                        catch (IOException e){
                            //Log.e(TAG, "Run socket server meets IO error "+Log.getStackTraceString(e));
                            Log.d(Client.TAG, "Run socket server meets IO error "+Log.getStackTraceString(e));
                        }
                    }

                }

            });
            sockThread.start();

        }
        catch (Exception e){
            //Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return true;


    }

    private void updateMemberList () {
        if (getMyRole() == Role.Host) {
            updateMemberListThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        final List<ScanClient> result = wifiAdmin.getClientList(true, 300);

                        //check if the member list does change
                        if (result.size() == getMemberList().size()) {
                            //should check the information of every member, but ignore it at first
                            //TODO
                            Log.d(Client.TAG, "member list didn't change" + result.size());
                            SystemClock.sleep(5000);
                            continue;

                        } else {
                            //this is the data which should be pass to the front end
                            ArrayList<String> devices = new ArrayList<String>();

                            HashMap<String, ScanClient> newMemberList = new HashMap<String, ScanClient>();
                            for (ScanClient sc : result) {
                                newMemberList.put(sc.getDevice(), sc);
                                devices.add(sc.getDevice());
                                Log.d(Client.TAG, "new client: " + sc.getDevice() + " : " +sc.getIpAddr());
                            }
                            setMemberList(newMemberList);

                            //post to MainActivity
                            if (handler != null) {
                                Message message = Message.obtain();
                                Bundle bundle = new Bundle();
                                message.what = UPDATE_ROOM_MEMBER;
                                bundle.putStringArrayList("devices", devices);
                                message.setData(bundle);
                                handler.sendMessage(message);
                            }
                            //boardcast to ever member
                            addNewMememberRequest();
                        }
                    }

                }
            });
            updateMemberListThread.start();
        } else {
            return;
        }

    }

    private void handleAddNewMember(CircleMessage message){

        if(getMyRole() == Role.Host) {
            Log.d(Client.TAG, "Impossible!!!!!!!");
            return;
        }
        //this time the ip address has to be correct
        setMyIP(wifiAdmin.getIP());
        setHostIP(wifiAdmin.getHostIP());
        //I'm a client
        AddMemberProto addMemberProto = (AddMemberProto)ProtocolParser.deserialize(message.content);
        HashMap<String, ScanClient> newMemberList = addMemberProto.memberList;

        //add host here since there isn't a entry of host itself in the member list
        newMemberList.put("host", new ScanClient(getHostIPStr(), "", "host", true));

        ArrayList<String> devices = new ArrayList<String>();

        //find client itself in the map and remove
        //use the device name as id which is assigned by host
        Log.d(Client.TAG, "my ip address " + getMyIPStr());
        String deviceName = "";
        for (Map.Entry<String, ScanClient> c : newMemberList.entrySet()) {
            if (c.getValue().getIpAddr().equals(getMyIPStr())) {
                deviceName = c.getKey();
                continue;
            }
            devices.add(c.getValue().getDevice());
            Log.d(Client.TAG, "get member list member: " + c.getValue().getDevice() + " " + c.getValue().getIpAddr());
        }
        newMemberList.remove(deviceName);
        setId(deviceName);
        //post to MainActivity
        if (handler != null) {
            Message msg = Message.obtain();
            msg.what = UPDATE_ROOM_MEMBER;
            Bundle bundle = new Bundle();
            msg.what = UPDATE_ROOM_MEMBER;
            bundle.putStringArrayList("devices", devices);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        setMemberList(newMemberList);


    }

    private void addNewMememberRequest(){
        AddMemberProto addMemberProto = new AddMemberProto();
        addMemberProto.memberList = getMemberList();
        realBroadcast(ProtocolParser.serialize(addMemberProto), true);

    }

    private void sendMessageHelper(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!sendmessageQueue.isEmpty()){
                    Socket socket = new Socket();
                    CircleMessage message = sendmessageQueue.poll();
                    if(message == null)
                        return;
                    try {
                        byte[] mData = ProtocolParser.serialize(message);
                        socket.connect(new InetSocketAddress(message.to, PORT), 2000);
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(mData);
                        outputStream.close();
                    }catch (IOException e){
                        //Log.e(TAG, Log.getStackTraceString(e));
                        sendmessageQueue.offer(message);
                        SystemClock.sleep(1000);
                    }
                }
            }
        }).start();
    }



}
