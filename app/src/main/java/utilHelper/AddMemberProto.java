package utilHelper;

import utilHelper.ScanClient;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Frank on 1/12/2015.
 */
public class AddMemberProto implements Serializable {
    public HashMap<String, ScanClient> memberList;
}
