package com.circle.circleFront;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import utilHelper.Configure;
import utilHelper.InteractiveModule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * Created by Yu on 2015/1/13.
 */
public class RoomFileFragment extends Fragment {
	private View rootView;
	private ListView lv;
	private SimpleAdapter sad;
	private File MyPath;
	private InteractiveModule im;
	private int pos=0;
	private File[]	files;
	private ArrayList<HashMap<String, String>> listitem = new ArrayList<>();
	public void RecvFile(String s){
		files = MyPath.listFiles();
		Log.d("files'size", files.length+"");
		listitem.clear();
		for (int i=0;i!=files.length;i++){
			HashMap<String, String> hm = new HashMap<>();
			hm.put("file_name", files[i].getName());
			listitem.add(hm);
		}
		Log.d("lt's size", listitem.size()+"");
		sad.notifyDataSetChanged();
		Toast.makeText(getActivity(), "You have received a new file "+s, Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

			rootView = inflater.inflate(R.layout.fragment_room_file, container, false);
			try {
				Initial();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return rootView;
	}
	
	private void Initial() throws IOException{
		
		lv = (ListView)rootView.findViewById(R.id.listView1);
		sad = new SimpleAdapter(getActivity(), listitem, R.layout.list_file, 
				new String[]{"file_name"}, new int[]{R.id.file_name});
		if (android.os.Build.VERSION.SDK_INT < 19){
			MyPath = new File(Environment.getExternalStorageDirectory().getPath()
					+"/Android/data/com.circle.circleFront/files");
		}else{
			MyPath = getActivity().getExternalFilesDir(null);
		}
		File Mydir = new File(MyPath.getPath()+"/sb.txt");
		Log.d("Path", MyPath.getAbsolutePath());
		FileOutputStream fos = new FileOutputStream(Mydir);
		fos.write("fdsjfsdf".getBytes());
		fos.close();
		files = MyPath.listFiles();
		Log.d("Path", Mydir.getAbsolutePath());
		Configure.TEMP_FILE_PATH = MyPath.getPath()+"/";
		for (int i=0;i!=files.length;i++){
			HashMap<String, String> hm = new HashMap<>();
			hm.put("file_name", files[i].getName());
			listitem.add(hm);
		}
		im = (InteractiveModule)getActivity().getApplication();
		lv.setAdapter(sad);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				pos = position;
				AlertDialog FileAD = new AlertDialog.Builder(getActivity())
				.setTitle("Make a choice")
				.setMessage("Do you want to transfer this file?")
				.setNegativeButton("No", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						((AlertDialog)dialog).dismiss();
					}
				})
				.setPositiveButton("Ok", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						im.getClient().transferFile(files[pos]);
					}
				})
				.create();
				FileAD.show();
			}
		});
	}
}
