package utilHelper;
import android.app.AlertDialog.Builder;

import utilHelper.MySimpleAct;
public class MyActFactory {
	public static MySimpleAct GetInstance(int type,String title,
			String des,Builder mybuilder,InteractiveModule immm,int id_number){
		if (type==0){
			return new MySignAct(title,des,"Present","Absent",mybuilder,immm,id_number);
		}
		if (type==1){
			return new MyVoteAct(title, des, "Agree", "Disagree", mybuilder,immm,id_number);
		}
		else return new MyVoteAct(title, des, "Join", "Reject", mybuilder,immm,id_number);
	}
}
