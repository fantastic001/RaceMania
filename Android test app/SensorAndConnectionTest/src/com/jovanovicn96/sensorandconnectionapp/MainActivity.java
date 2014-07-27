package com.jovanovicn96.sensorandconnectionapp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jovanovicn96.sensorandconnectionapp.UDPClient;
import com.jovanovicn96.sensorandconnectionapp.UDPThread;

public class MainActivity extends Activity implements SensorEventListener {

	private static final int SERVERPORT = 5000;
	private static String SERVER_IP = "0.0.0.0";
	
	boolean listening = true; 

	// Accelerometer X, Y, and Z values
	private TextView accelXValue;
	private TextView accelYValue;
	private TextView accelZValue;
	byte arg1, arg2, arg3;
	
	// Orientation X, Y, and Z values
	private TextView orientXValue;
	private TextView orientYValue;
	private TextView orientZValue;
	byte[] arg4 = new byte[4];
	byte[] arg5 = new byte[4];
	byte[] arg6 = new byte[4];
	float angX=0, angY=0, angZ=0;
	long orientTime = 0;
	
	byte[] byteSwap = new byte[256];
	
	// seekBar
	private SeekBar seekBar;
	int progStat = 50;
	
	// IP UI
	private EditText IPadress;
	
	private SensorManager sensorManager = null;
		
	// UDP/IP initializations
	UDPClient udpClient;
	UDPThread udpThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //pregressBar
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        // Capture IP UI
        IPadress = (EditText) findViewById(R.id.editText1);
      
        // Capture accelerometer related view elements
        accelXValue = (TextView) findViewById(R.id.accel_x_value);
        accelYValue = (TextView) findViewById(R.id.accel_y_value);
        accelZValue = (TextView) findViewById(R.id.accel_z_value);
      
        // Capture orientation related view elements
        orientXValue = (TextView) findViewById(R.id.orient_x_value);
        orientYValue = (TextView) findViewById(R.id.orient_y_value);
        orientZValue = (TextView) findViewById(R.id.orient_z_value);
      
        // Initialize accelerometer related view elements
        accelXValue.setText("0.00");
        accelYValue.setText("0.00");
        accelZValue.setText("0.00");
      
        // Initialize orientation related view elements
        orientXValue.setText("0.00");
        orientYValue.setText("0.00");
        orientZValue.setText("0.00");
    }
    
    
    public void onReciveUDPmessage(byte[] mess){

	    final int x = (int) mess[0]; 
	    final int y = (int) mess[1];
	    final int z = (int) mess[2];

	    runOnUiThread(new Runnable(){
	        @Override
		public void run(){
			
			orientXValue.setText(Integer.toString(x));
			orientYValue.setText(Integer.toString(y));
			orientZValue.setText(Integer.toString(z));
		}});

    }
  
    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent) {
	    synchronized (this) {
		    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && listening) {
		    	if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				    accelXValue.setText(Float.toString(sensorEvent.values[0]));
				    accelYValue.setText(Float.toString(sensorEvent.values[1]));
				    accelZValue.setText(Float.toString(sensorEvent.values[2]));
				    arg1 = (byte) Math.round(sensorEvent.values[0]*8);
				    arg2 = (byte) Math.round(sensorEvent.values[1]*8);
				    arg3 = (byte) Math.round(sensorEvent.values[2]*8);

			    	progStat = (int)arg2/2+50;
			    	try {
			    		seekBar.setProgress(progStat);
			    	} catch (NullPointerException e){
				    	e.printStackTrace();
			    	}
		    	}
		  
		    	byte[] args = new byte[4];
		    	args[0] = arg1;
		    	args[1] = arg2;
		    	args[2] = arg3;
		    	args[3] = 65;
		    	udpClient.send(args, SERVER_IP);
		    }
	    }
    }
  
    public void SetIP(View view){
    	SERVER_IP = IPadress.getText().toString();
    	Toast.makeText(MainActivity.this, "IP Changed", Toast.LENGTH_SHORT).show();
    }
    
    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    	// TODO Auto-generated method stub
	}
	  
    @Override
    protected void onResume() {
	    super.onResume();
	    // Register this class as a listener for the accelerometer sensor
	    sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    udpThread = new UDPThread(this); 
	    udpThread.start();
	    
        try {
    		udpClient = new UDPClient(SERVERPORT);
		listening = true; 
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
  
    @Override
	protected void onPause() {
		super.onPause();
		udpThread.terminate(); 
		try 
		{
			udpThread.join();
		} 
		catch (Exception e) 
		{
			System.out.println("RACEMANIA: Cannot join to thread");
		}
		udpThread = null;
		listening = false; // Turn off listening for sensor
		udpClient.close();
		udpClient = null;
	}

	@Override
    protected void onStop() {
	    super.onStop();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
