package utilHelper;

import java.io.Serializable;

/**
 * Created by Frank on 1/12/2015.
 */
public class CircleMessage implements Serializable{

    //the message should be held by ourself
    public enum TYPE {
        NORMAL,
        FILE
    }

    public boolean isSystem;
    //can be used for ip address
    //or MAC
    public TYPE type;//0:normal 1:file
    public String detail;//0:meanless 1:filename
    public String from;
    public String to;

    public byte[] content;
}
