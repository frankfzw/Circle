package utilHelper;

import java.io.Serializable;

public class MessageProto implements Serializable{
	//0=send
	//1=broadcast
	//2=new act
	//3=update act
	public int EventType;
	
	public byte[] EventData;
}
