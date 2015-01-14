package utilHelper;

import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import utilHelper.ScanClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 1/11/2015.
 */
public class WifiAdmin {
    private Context context;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private List<ScanResult> wifiList;
    private List<WifiConfiguration> wifiConfigurations;

    private int netID;

    //private List<String> apList;

    public WifiAdmin(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.context = context;
    }

    public void scan() {
        if (wifiManager.isWifiEnabled() == false) {
            wifiManager.setWifiEnabled(true);
            //Toast.makeText(context,"Open WiFi", Toast.LENGTH_LONG).show();
        }

        wifiManager.startScan();
        wifiList = wifiManager.getScanResults();
        wifiConfigurations = wifiManager.getConfiguredNetworks();

        //update wifiInfo
        wifiInfo = wifiManager.getConnectionInfo();
    }

    public List<String> scanResult() {
        if (wifiList == null)
            return null;

        List <String> res = new ArrayList<>();
        for (ScanResult r : wifiList) {
            Log.d("WifiAdmin scan: ", r.SSID);
            res.add(r.SSID);
        }
        return res;
    }

    public boolean startAp(String name, String pwd, boolean enable) {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        try {
            WifiConfiguration wifiConfiguration = setConfig(name, pwd);
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean res = (Boolean)method.invoke(wifiManager, wifiConfiguration, enable);

            //update wifiInfo
            wifiInfo = wifiManager.getConnectionInfo();
            return res;
        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
            return false;
        }

    }

    public boolean enterAp(String ssid, String pwd) {
        //refresh at first
        scan();
        String realSSID = "\"" + ssid + "\"";
        String realPWD = "\"" + pwd + "\"";
        WifiConfiguration config = setConfig(realSSID, realPWD);
        netID = wifiManager.addNetwork(config);
        boolean flag = wifiManager.enableNetwork(netID, true);
        return flag;
    }

    public int getIP() {
        DhcpInfo info = wifiManager.getDhcpInfo();
        return info.ipAddress;
    }

    public int getHostIP() {
        DhcpInfo info = wifiManager.getDhcpInfo();
        return info.serverAddress;
    }

    public boolean disableWifi() {
        wifiManager.disableNetwork(netID);
        wifiManager.removeNetwork(netID);
        wifiManager.saveConfiguration();
        return wifiManager.setWifiEnabled(false);
    }
    /**
     *
     * @param onlyReachable set true to return the reachable clients
     * @param reachableTimeout time out of reachable
     */

    public List<ScanClient> getClientList(boolean onlyReachable, int reachableTimeout) {
        BufferedReader br = null;
        final ArrayList<ScanClient> result = new ArrayList<ScanClient>();
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");

                if ((splitted != null) && (splitted.length >= 4)) {
                    // Basic sanity check
                    String mac = splitted[3];

                    if (mac.matches("..:..:..:..:..:..")) {
                        boolean isReachable = InetAddress.getByName(splitted[0]).isReachable(reachableTimeout);

                        if (!onlyReachable || isReachable) {
                            //TODO
                            //set mac address device temporarily
                            result.add(new ScanClient(splitted[0], splitted[3], splitted[3], isReachable));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.toString());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                Log.e("CIRCLE ERROR", e.getMessage());
            }

            return result;
        }
    }

    public WifiConfiguration setConfig(String ssid, String pwd) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = ssid;
        wifiConfiguration.preSharedKey = pwd;
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        return wifiConfiguration;
    }


}
