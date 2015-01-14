package com.circle.circleFront;

import java.util.ArrayList;
import com.circle.circleFront.R;
import java.util.HashMap;

import utilHelper.InteractiveModule;
import utilHelper.MessageProto;
import utilHelper.MyActFactory;
import utilHelper.MySimpleAct;
import utilHelper.NewActionProto;
import utilHelper.UtilAssist;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;

/**
 * Created by LIUYU on 2014/12/27.
 */
public class RoomActivityFragment extends Fragment {
	private ArrayList<MySimpleAct> MyActList = new ArrayList<MySimpleAct>();
	private ArrayList<HashMap<String, String>> listitem = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter sad;
	private ListView lv;
	private String title="",description="";
	private Integer type_act=0;
	private View rootView;
	private Button addact_button;
	private InteractiveModule im;
	
	public void AddNewAction(String ti,String desc,int typ_act){
		HashMap<String, String> hsm = new HashMap<String, String>();
		hsm.put("Title", ti);
		listitem.add(hsm);
		Log.d("SIZE", MyActList.size()+"");
		MyActList.add(MyActFactory.GetInstance(typ_act, ti, desc, MakeAct(),im,MyActList.size()));
		sad.notifyDataSetChanged();
		Toast.makeText(getActivity(), "You have a new activity", Toast.LENGTH_SHORT).show();
	}
	
	public void UpdateActInform(int idn, String selection){
		Log.d("TEST11", "index "+idn+" Size: "+MyActList.size());
		
		MyActList.get(idn).NotifyResultChanged(selection);
	}
	
	private Builder MakeAct(){
		return new AlertDialog.Builder(getActivity());
	}
	
	private AlertDialog NewActDialog = null;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_room_activity, container, false);
        Initial();
        return rootView;
    }
	
	private void Initial(){
		lv = (ListView)rootView.findViewById(R.id.listView1);
		addact_button = (Button)rootView.findViewById(R.id.button1);
		addact_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddNewAct();
			}
		}); 
		sad = new SimpleAdapter(getActivity(), listitem, R.layout.mylistshow, 
				new String[]{"Title"}, new int[]{R.id.Title});
		lv.setAdapter(sad);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				MyActList.get(position).Display();
			}
		});
		im = (InteractiveModule)getActivity().getApplication();
	}
	private void AddNewAct(){
		LayoutInflater linf = LayoutInflater.from(getActivity());
		View NewActView = linf.inflate(R.layout.new_act, null);
		NewActDialog = new AlertDialog.Builder(getActivity())
		.setTitle("Add New Activity")
		.setView(NewActView)
		.setNeutralButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				EditText ed1 = (EditText)NewActDialog.getWindow().findViewById(R.id.editText1);
				EditText ed2 = (EditText)NewActDialog.getWindow().findViewById(R.id.editText2);
				title = ed1.getText().toString();
				description = ed2.getText().toString();
				if (title.equals("") || description.equals("")){
					Toast.makeText(getActivity(), "should not null", Toast.LENGTH_SHORT).show();
				}else{
					MessageProto mp = new MessageProto();
					mp.EventType = 2;
					NewActionProto nap = new NewActionProto();
					nap.descrip = description;
					nap.ti = title;
					nap.type_action = type_act;
					mp.EventData = UtilAssist.serialize(nap);
					im.getClient().broadcast(UtilAssist.serialize(mp));
					AddNewAction(title, description, type_act);	
				}
			}
		}).create();
		NewActDialog.show();
		Spinner myspin = (Spinner)NewActDialog.getWindow().findViewById(R.id.spinner1);
		myspin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				type_act = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.side_menu_activity, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_exit:
                im.getClient().exitRoom();
                Intent intent = new Intent(getActivity(), LinkActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


