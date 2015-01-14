package utilHelper;


import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Frank on 1/11/2015.
 */
public class Client {
    private String id;
    private int myIP;
    private int hostIP;
    private String roomName;
    private ArrayList<String> roomList;
    private HashMap<String, ScanClient> memberList;
    private Role myRole;

    public enum Role {
        Invalid,
        Client,
        Host
    }

    public static final String TAG = "CIRCLE";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMyIP() {
        return myIP;
    }

    public void setMyIP(int myIP) {
        this.myIP = myIP;
    }

    public int getHostIP() {
        return hostIP;
    }

    public void setHostIP(int hostIP) {
        this.hostIP = hostIP;
    }

    public ArrayList<String> getRoomList() {
        return roomList;
    }

    public void setRoomList(ArrayList<String> roomList) {
        this.roomList = roomList;
    }

    public HashMap<String, ScanClient> getMemberList() {
        return memberList;
    }

    public void setMemberList(HashMap<String, ScanClient> memberList) {
        this.memberList = memberList;
    }

    public Role getMyRole() {
        return myRole;
    }

    public void setMyRole(Role myRole) {
        this.myRole = myRole;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
