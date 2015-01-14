package utilHelper;

import utilHelper.ScanClient;

import java.util.List;

/**
 * Created by Frank on 1/12/2015.
 */

//use to get the scan result of client who connect with ap host
public interface ScanListener {

    public void onFinishScan(List<ScanClient> clients);
}
