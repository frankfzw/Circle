package utilHelper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class MyVoteAct extends MySimpleAct{

	public MyVoteAct(String t, String des, String sepos, String seneg
			,Builder myBuilder,InteractiveModule immm,int id_number) {
		super(t, des, sepos, seneg, myBuilder,immm,id_number);
		// TODO Auto-generated constructor stub
		init();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		MyInform = this.AleBuilder
				.setTitle(this.title)
				.setMessage(Description)
				.setPositiveButton(PosSelection+" "+VotePos, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(completed==false){
							VotePos++;
							completed = true;
							((AlertDialog)dialog)
							.getButton(AlertDialog.BUTTON_POSITIVE)
							.setText(PosSelection+" "+VotePos);
							((AlertDialog)dialog)
							.setTitle(title+"(Completed)");
							((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE)
							.setEnabled(false);
							((AlertDialog)dialog)
							.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
							
							
							
							MessageProto mp = new MessageProto();
							mp.EventType = 3;
							UpadteActProto uap = new UpadteActProto();
							uap.position = id;
							uap.Selection="P";
							mp.EventData = UtilAssist.serialize(uap);
							imm.getClient().broadcast(UtilAssist.serialize(mp));
						}
					}
				})
				.setNegativeButton(NegSelection+" "+VoteNeg, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(completed==false){
							VoteNeg++;
							completed = true;
							((AlertDialog)dialog)
							.getButton(AlertDialog.BUTTON_NEGATIVE)
							.setText(NegSelection+" "+VoteNeg);
							((AlertDialog)dialog)
							.setTitle(title+"(Completed)");
							((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE)
							.setEnabled(false);
							((AlertDialog)dialog)
							.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
							
							
							MessageProto mp = new MessageProto();
							mp.EventType = 3;
							UpadteActProto uap = new UpadteActProto();
							uap.position = id;
							uap.Selection="N";
							mp.EventData = UtilAssist.serialize(uap);
							imm.getClient().broadcast(UtilAssist.serialize(mp));
						}
					}
				})
				.create();
		MyInform.show();
		MyInform.dismiss();
	}

	@Override
	public void NotifyResultChanged(String selection1) {
		// TODO Auto-generated method stub
		if (selection1.equals("P")){
			VotePos++;
			MyInform
			.getButton(AlertDialog.BUTTON_POSITIVE)
			.setText(PosSelection+" "+VotePos);
		}else{
			VoteNeg++;
			MyInform
			.getButton(AlertDialog.BUTTON_NEGATIVE)
			.setText(NegSelection+" "+VoteNeg);
		}
	}
}
