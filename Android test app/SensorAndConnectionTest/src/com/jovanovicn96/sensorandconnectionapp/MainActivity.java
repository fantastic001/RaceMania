package com.jovanovicn96.sensorandconnectionapp;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jovanovicn96.sensorandconnectionapp.UDPClient;

public class MainActivity extends Activity implements SensorEventListener {

	private static final int SERVERPORT = 5000;
	private static final String SERVER_IP = "10.0.2.2";
	
	// TCP/IP initializations
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
	double angX=0, angY=0, angZ=0;
	long orientTime = 0;
	
	// seekBar
	private SeekBar seekBar;
	int progStat = 50;
	
	private SensorManager sensorManager = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //pregressBar
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        
        try {
			udpClient = new UDPClient(SERVER_IP, SERVERPORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
      
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
			    	arg1 = new byte[8];
			    	ByteBuffer.wrap(arg1).putDouble(sensorEvent.values[0]);
			    	arg2 = new byte[8];
			    	ByteBuffer.wrap(arg2).putDouble(sensorEvent.values[1]);
			    	arg3 = new byte[8]; 
			    	ByteBuffer.wrap(arg3).putDouble(sensorEvent.values[2]);
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
				    	arg4 = new byte[8];
				    	ByteBuffer.wrap(arg4).putDouble(angX);
				    	arg5 = new byte[8];
				    	ByteBuffer.wrap(arg5).putDouble(angY);
				    	arg6 = new byte[8]; 
				    	ByteBuffer.wrap(arg6).putDouble(angZ);
				    	progStat = -(int)angZ/2+50;
				    	try {
				    		seekBar.setProgress(progStat);
				    	} catch (NullPointerException e){
				    		Log.w("myApp", "NullPointe!!!!!");
				    		orientXValue.setText("asd");
				    	}
		    		}
		    		orientTime = sensorEvent.timestamp;
		    	}
		    	
		    	byte[] args = new byte[48]; 
		    	for (int i=0; i<8; i++) args[i] = arg1[i];
		    	for (int i=0; i<8; i++) args[i+8] = arg2[i];
		    	for (int i=0; i<8; i++) args[i+16] = arg3[i];
		    	for (int i=0; i<8; i++) args[i+24] = arg4[i];
		    	for (int i=0; i<8; i++) args[i+32] = arg5[i];
		    	for (int i=0; i<8; i++) args[i+40] = arg6[i];
		    	
			    try {
			    	udpClient.send(args);
			    } catch (Exception e) {
			    	e.printStackTrace();
			    }
			    
		    }
	    }
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
