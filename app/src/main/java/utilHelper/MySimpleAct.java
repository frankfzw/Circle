package utilHelper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

public abstract class MySimpleAct{
	protected int id;
	protected String title;
	protected Builder AleBuilder;
	protected String Description;
	protected String PosSelection;
	protected String NegSelection;
	protected boolean completed=false;
	protected AlertDialog MyInform;
	protected int VotePos=0;
	protected int VoteNeg=0;
	protected InteractiveModule imm;
	protected abstract void init();
	public abstract void NotifyResultChanged(String selection1);
	public void Display(){
		this.MyInform.show();
	}
	public String GetTitle(){
		return this.title;
	}
	public String GetDescription(){
		return this.Description;
	}
	public String GetPosSelection(){
		return this.PosSelection;
	}
	public String GetNegSelection(){
		return this.NegSelection;
	}
	public boolean IsCompleted(){
		return this.completed;
	}
	public int GetVotePos(){
		return this.VotePos;
	}
	public int GetVoteNeg(){
		return this.VoteNeg;
	}
	public MySimpleAct(String t,String des,String sepos,String seneg
			,Builder mybuilder ,InteractiveModule immm,int id_number){
		this.title=t;
		this.Description=des;
		this.PosSelection=sepos;
		this.NegSelection=seneg;
		this.AleBuilder= mybuilder;
		this.imm = immm;
		this.id = id_number;
	}
}
