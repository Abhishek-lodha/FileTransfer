package com.example.filetransfer;

import java.io.File;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FileSend extends Activity implements SensorEventListener   {

	int i;
	short min=-100;
	BluetoothAdapter bt;
	SensorManager sm;
	BluetoothDevice bd;
	BluetoothDevice device;
	String filepath;
/*	TextView x1;
	TextView y1;
	TextView z1;*/
	TextView bdnam;
	String name;
	TextView fpath;
	TextView rotate;
	ImageView img;
	int bdfound;

	
	Sensor acc;

	//Button bt;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accelerometer);
		i=1;
		bdfound=0;
		//bdnam=(TextView)findViewById(R.id.bdname);
		//bdnam.setText("(Searching for device)");
		//bdnam.setTextColor(Color.DKGRAY);
		bt=BluetoothAdapter.getDefaultAdapter();
		//Set<BluetoothDevice> pairedDevices=bt.getBondedDevices();
		fpath=(TextView)findViewById(R.id.filepath);
		img=(ImageView)findViewById(R.id.fimage);
		/*x1=(TextView)findViewById(R.id.x);
		y1=(TextView)findViewById(R.id.y);
		z1=(TextView)findViewById(R.id.z);*/
		 sm = (SensorManager)getSystemService(SENSOR_SERVICE);
	     acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		//sm.registerListener(this,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),sm.SENSOR_DELAY_NORMAL);
		Intent i=getIntent();
	    filepath=i.getExtras().getString("fname");
	   //Toast.makeText(getApplicationContext(), filepath, 0).show();
	    fpath.setText(filepath);
	    fpath.setTextColor(Color.BLUE);
	   
	    if(filepath.contains("pdf"))
	    	img.setImageResource(R.drawable.pdf);
	    else if(filepath.contains("apk"))
	    		img.setImageResource(R.drawable.apk);
	    else if(filepath.contains("jpg"))
	    {
	    	 Bitmap myBitmap = BitmapFactory.decodeFile(filepath);
	    	 img.setImageBitmap(myBitmap);
	    	 
	    }
	    else
	    	img.setImageResource(R.drawable.noffound);
	    
	   	
	    
	    
		//Iterator<BluetoothDevice> j= pairedDevices.iterator();
		// bd=j.next();
	   // bt.startDiscovery();
		  registerReceiver(receiver, new IntentFilter(bd.ACTION_FOUND));
		 
		/* for(int c=0;c<=pairedDevices.size();++c)
		 {*/
		// Intent intent = new Intent(bd.ACTION_FOUND);
		// short rssi = intent.getShortExtra(bd.EXTRA_RSSI,Short.MIN_VALUE);
			//Toast.makeText(getApplicationContext(),rssi+" "+bd.toString(), 2).show();
			//Toast.makeText(getApplicationContext(), bd.toString(),0).show();
		// }
		 
		 
		 //Toast.makeText(getApplicationContext(),bd.toString(), 0).show();
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		super.onBackPressed();
		bt.cancelDiscovery();
		this.finish();
		//Toast.makeText(getApplicationContext(), "Back pressed", 2).show();
		
	}
	private final BroadcastReceiver receiver = new BroadcastReceiver(){
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	
	    	
	    	
	        String action = intent.getAction();
	        if(bd.ACTION_FOUND.equals(action)) {
	            short rssi = intent.getShortExtra(bd.EXTRA_RSSI,Short.MIN_VALUE);
	           name = intent.getStringExtra(bd.EXTRA_NAME);
	            
	            
	            if(rssi>min)
	            {
	            	//Toast.makeText(getApplicationContext(),name+"  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
	            	min=rssi;
	            	device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	 	           bdfound=1;
	            	//device=intent.getStringExtra(bd.EXTRA_NAME);
	       //    	bdnam.setText(name);
	        //   	bdnam.setTextColor(Color.BLUE);
	           	
	            	// Toast.makeText(getApplicationContext(),name+"  "+ device.toString(), 2).show();
	            }
	        }
	      //  bt.startDiscovery();
	    }
	};
	
	


	 protected void onResume() {
         super.onResume();
         sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
     }

	
	protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
	if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			
			// assign directions
			float x=event.values[0];
			float y=event.values[1];
			float z=event.values[2];
			/*x1.setText("X: "+Float.toString(x));
			y1.setText("Y: "+Float.toString(y));
			z1.setText("Z: "+Float.toString(z));*/
			
			
			if(((x>6&&y<2)||(y<2&&x<-6))&&i==1)
			{
				i=0;
				
				bt.startDiscovery();
			new android.os.Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(bdfound==1)
					   transfer();
					else
						Toast.makeText(getApplicationContext(), "No device found!!!Try Again", 3).show();
					//Toast.makeText(getApplicationContext(), "Hi", 0).show();
				}
			},5000);
				
			//	i=0;
			//	bt.cancelDiscovery();
			//	rotate=(TextView)findViewById(R.id.rts);
				
				
			//Toast.makeText(getApplicationContext(), "Chal ja", 0).show();
			/*Intent in=new Intent(Intent.ACTION_SEND);
			
				File f=new File(filepath);
				Uri ur=Uri.fromFile(f);
				in.setType("image/*");
				in.putExtra(Intent.EXTRA_STREAM,ur);
				in.putExtra(BluetoothDevice.EXTRA_DEVICE, device.toString());
				//in.putExtra(BluetoothDevice.EXTRA_DEVICE, bd);
				in.setPackage("com.android.bluetooth");*/
			
				
			/*	rotate.setText(" ");
				Toast.makeText(getApplicationContext(), "Transfer Initiated ", 2).show();
				
				
				//in.setComponent(new ComponentName("com.android.bluetooth","com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
				//startActivity(Intent.createChooser(in, "Send Image"));
				//sendBroadcast(in);
				File f=new File(filepath);
				ContentValues values = new ContentValues();
				values.put(BluetoothShare.URI, Uri.fromFile(f).toString());
				values.put(BluetoothShare.DESTINATION, device.toString());
				values.put(BluetoothShare.DIRECTION, BluetoothShare.DIRECTION_OUTBOUND);
				//Long ts = System.currentTimeMillis();
				//values.put(BluetoothShare.TIMESTAMP, ts);
				getContentResolver().insert(BluetoothShare.CONTENT_URI, values);
				//getContentResolver().cancelSync(BluetoothShare.CONTENT_URI);
				//getContentResolver().cancelSync(null, null);
				
				//getContentResolver().delete(BluetoothShare.CONTENT_URI, null, null);*/
				
				
			}
			
		
		}	
	}
	public void transfer() {
		
		File f=new File(filepath);
		ContentValues values = new ContentValues();
		values.put(BluetoothShare.URI, Uri.fromFile(f).toString());
		values.put(BluetoothShare.DESTINATION, device.toString());
		values.put(BluetoothShare.DIRECTION, BluetoothShare.DIRECTION_OUTBOUND);
		//Long ts = System.currentTimeMillis();
		//values.put(BluetoothShare.TIMESTAMP, ts);
		getContentResolver().insert(BluetoothShare.CONTENT_URI, values);
		Toast.makeText(getApplicationContext(), "Transferring to : "+device.getName(), 2).show();
		
	}

	//@Override
	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1)
		{
			finishActivity(1);
		}
	}*/

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	
	}


}
