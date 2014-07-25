package com.jovanovicn96.sensorandconnectionapp;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jovanovicn96.sensorandconnectionapp.UDPClient;

public class MainActivity extends Activity implements SensorEventListener {

	private static final int SERVERPORT = 5000;
	private static String SERVER_IP = "192.168.0.156";
	private static final int ResiveMessageLength = 48;
	
	// UDP/IP initializations
	UDPClient udpClient;
	
	// Accelerometer X, Y, and Z values
	private TextView accelXValue;
	private TextView accelYValue;
	private TextView accelZValue;
	byte[] arg1 = new byte[8];
	byte[] arg2 = new byte[8];
	byte[] arg3 = new byte[8];
	
	// Orientation X, Y, and Z values
	private TextView orientXValue;
	private TextView orientYValue;
	private TextView orientZValue;
	byte[] arg4 = new byte[8];
	byte[] arg5 = new byte[8];
	byte[] arg6 = new byte[8];
	float angX=0, angY=0, angZ=0;
	long orientTime = 0;
	
	// seekBar
	private SeekBar seekBar;
	int progStat = 50;
	
	// IP UI
	private EditText IPadress;
	
	private SensorManager sensorManager = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //pregressBar
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        
        try {
			udpClient = new UDPClient(SERVER_IP, SERVERPORT, ResiveMessageLength);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
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
  
    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent) {
	    synchronized (this) {
		    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER ||
		    		sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
		    	if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				    accelXValue.setText(Float.toString(sensorEvent.values[0]));
				    accelYValue.setText(Float.toString(sensorEvent.values[1]));
				    accelZValue.setText(Float.toString(sensorEvent.values[2])); 
			    	arg1 = new byte[4];
			    	ByteBuffer.wrap(arg1).putInt(Math.round(sensorEvent.values[0] * 1000));
			    	arg2 = new byte[4];
			    	ByteBuffer.wrap(arg2).putInt(Math.round(sensorEvent.values[1] * 1000));
			    	arg3 = new byte[4]; 
			    	ByteBuffer.wrap(arg3).putInt(Math.round(sensorEvent.values[2] * 1000));
		    	}
		    	if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){
		    		if (orientTime != 0){
			    		double DTime = (sensorEvent.timestamp - orientTime)/1000000000.0f;
			    		orientTime = System.nanoTime();
					    angX += sensorEvent.values[0] * DTime * 180 / Math.PI;
					    angY += sensorEvent.values[1] * DTime * 180 / Math.PI;
					    angZ += sensorEvent.values[2] * DTime * 180 / Math.PI;
					    orientXValue.setText(Double.toString(angX));
					    orientYValue.setText(Double.toString(angY));
					    orientZValue.setText(Double.toString(angZ));
				    	arg4 = new byte[4];
				    	ByteBuffer.wrap(arg4).putInt(Math.round(angX * 1000));
				    	arg5 = new byte[4];
				    	ByteBuffer.wrap(arg5).putInt(Math.round(angY * 1000));
				    	arg6 = new byte[4]; 
				    	ByteBuffer.wrap(arg6).putInt(Math.round(angZ * 1000));
				    	progStat = -(int)angZ/2+50;
				    	try {
				    		seekBar.setProgress(progStat);
				    	} catch (NullPointerException e){
					    	e.printStackTrace();
				    	}
		    		}
		    		orientTime = sensorEvent.timestamp;
		    	}
		    	
		    	byte[] args = new byte[25]; 
		    	for (int i=0; i<4; i++) args[i] = arg1[i];
		    	for (int i=0; i<4; i++) args[i+4] = arg2[i];
		    	for (int i=0; i<4; i++) args[i+8] = arg3[i];
		    	for (int i=0; i<4; i++) args[i+12] = arg4[i];
		    	for (int i=0; i<4; i++) args[i+16] = arg5[i];
		    	for (int i=0; i<4; i++) args[i+20] = arg6[i];
		    	args[24] = 65;
		    	
			udpClient.send(args);
			System.out.println("RACEMANIA: Error while sending data");
			    
		    }
	    }
    }
  
    public void SetIP(View view){
    	SERVER_IP = IPadress.getText().toString();
    	udpClient.close();
	try {
		udpClient = new UDPClient(SERVER_IP, SERVERPORT, ResiveMessageLength);
	}
	catch (Exception e) 
	{
		System.out.println("RACEMANIA: Error while creating socket");
	}
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
	    // ...and the gyroscope sensor
	    sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);;
    }
  
    @Override
    protected void onStop() {
    	udpClient.close();
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
