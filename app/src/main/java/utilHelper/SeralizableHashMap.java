package utilHelper;

import java.io.Serializable;
import java.util.HashMap;

public class SeralizableHashMap implements Serializable{
	private HashMap<String, Object> map;
	public void setMap(HashMap<String, Object> mmap){
		this.map=mmap;
	}
	public HashMap<String, Object> getMap(){
		return this.map;
	}
}
