package utilHelper;

/**
 * Created by Frank on 1/12/2015.
 */

import java.io.Serializable;

/**
 * this entity is used to save the scan result for future using.
 */
public class ScanClient implements Serializable{
    private String ipAddr;
    private String macAddr;
    private String device;
    private boolean isReachable;

    public ScanClient(String ipAddr, String macAddr, String device, boolean isReachable) {
        this.ipAddr = ipAddr;
        this.macAddr = macAddr;
        this.device = device;
        this.isReachable = isReachable;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public boolean isReachable() {
        return isReachable;
    }

    public void setReachable(boolean isReachable) {
        this.isReachable = isReachable;
    }
}
