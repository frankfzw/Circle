package utilHelper;

import android.util.Log;

import utilHelper.Client;
import utilHelper.CircleMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Frank on 1/12/2015.
 */
public class ProtocolParser {
    public static CircleMessage parseSocket(InputStream in){
        CircleMessage message = null;
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(in);
            message = (CircleMessage)objectInputStream.readObject();
            objectInputStream.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //Log.e(Configure.TAG, ""+Log.getStackTraceString(e));

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Client.TAG, "" + Log.getStackTraceString(e));
            return null;
        }
        return message;
    }

    public static Boolean wrap(CircleMessage message, OutputStream out){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(message);
            oos.flush();
            oos.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            //Log.e(Configure.TAG, "ProtocolParser write fails "+Log.getStackTraceString(e));
           // Logger.write(Configure.TAG, "ProtocolParser write fails "+Log.getStackTraceString(e));
            return false;
        }

    }


    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(out);
            os.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = null;
        Object ret = null;
        try {
            is = new ObjectInputStream(in);
            ret = is.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return ret;
    }
}
