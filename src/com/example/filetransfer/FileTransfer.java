package com.example.filetransfer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;






import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class FileTransfer extends ListActivity {
	
	private List<String> item = null;
	private List<String> path = null;
	private List<String> selected = null ;
	
	
	BluetoothAdapter mBluetoothAdapter = null;
	private String root;
	
	private TextView myPath;
	EditText inputSearch;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_file_transfer);
		  myPath = (TextView)findViewById(R.id.path);
	        
	        root = Environment.getExternalStorageDirectory().getPath();
	        inputSearch = (EditText) findViewById(R.id.inputSearch);
	        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			 if (mBluetoothAdapter == null) {
			 Toast.makeText(this,
			 "Bluetooth is not available.",
			 Toast.LENGTH_LONG).show();
			 finish();
			 return;
			 }
			 
			 if (!mBluetoothAdapter.isEnabled()) {
				 Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
				 startActivityForResult(enableBtIntent, 1);
				 }
	        
	        getDir(root);

	}
	  @Override 
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   // TODO Auto-generated method stub
	   if(requestCode == 1){
	   if (resultCode == RESULT_OK){
	   Toast.makeText(getApplicationContext(), "BlueTooth is now Enabled", Toast.LENGTH_LONG).show();
	   getDir(root);
	   }
	    if(resultCode == RESULT_CANCELED){
	   Toast.makeText(getApplicationContext(), "Error occured while enabling.Leaving the application..", Toast.LENGTH_LONG).show();
	    finish();
	    return;
	    }
	    }
	    }
	    
	    private void getDir(String dirPath)
	    {
	    	myPath.setText("Location: " + dirPath);
	    	item = new ArrayList<String>();
	    	path = new ArrayList<String>();
	    	selected = new ArrayList<String>();
	    	File f = new File(dirPath);
	    	File[] files = f.listFiles();
	    	
	    	if(!dirPath.equals(root))
	    	{
	    		item.add(root);
	    		path.add(root);
	    		item.add("../");
	    		path.add(f.getParent());	
	    	}
	    	
	    	for(int i=0; i < files.length; i++)
	    	{
	    		File file = files[i];
	    		
	    		if(!file.isHidden() && file.canRead()){
	    			path.add(file.getPath());
	        		if(file.isDirectory()){
	        			item.add(file.getName() + "/");
	        		}else{
	        			item.add(file.getName());
	        		}
	    		}	
	    	}

	    	adapter =	new ArrayAdapter<String>(this, R.layout.row, item);
	    	setListAdapter(adapter);	
	    	
	    	
	    	inputSearch.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					FileTransfer.this.adapter.getFilter().filter(s);
					
					
					
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
	    }
	    
		@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			
			File file = new File(path.get(position));
			
					
			if (file.isDirectory())
			{
				if(file.canRead()){
					getDir(path.get(position));
					
				}else{
					new AlertDialog.Builder(this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("[" + file.getName() + "] folder can't be read!")
						.setPositiveButton("OK", null).show();	
				}	
			}else {
				selected.add(path.get(position));
				String fpath= file.getAbsolutePath();
				//Toast.makeText(getApplicationContext(), fpath, 2).show();
				/*MainActivityacc obj= new MainActivityacc();
			    obj.onCreate(new Bundle());*/
				Intent m=new Intent(getApplicationContext(),FileSend.class);
				
				m.putExtra("fname",fpath );
				startActivity(m);
				/*new AlertDialog.Builder(this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("[" + file.getName() + "]")
						.setPositiveButton("OK", null).show();*/
			//Toast.makeText(getApplicationContext(), "Hello",0).show();*/
			  }
		}



}
