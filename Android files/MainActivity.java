package com.jovanovicn96.sensorandconnectionapp;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	// TCP/IP initializations
	private Socket socket;
	
	private static final int SERVERPORT = 5000;
	private static final String SERVER_IP = "10.0.2.2"; 
	
	// Accelerometer X, Y, and Z values
	private TextView accelXValue;
	private TextView accelYValue;
	private TextView accelZValue;
	// Orientation X, Y, and Z values
	/**private TextView orientXValue;
	private TextView orientYValue;
	private TextView orientZValue;*/
	private SensorManager sensorManager = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.activity_main);
      
        // Capture accelerometer related view elements
        accelXValue = (TextView) findViewById(R.id.accel_x_value);
        accelYValue = (TextView) findViewById(R.id.accel_y_value);
        accelZValue = (TextView) findViewById(R.id.accel_z_value);
      
        // Capture orientation related view elements
        /**orientXValue = (TextView) findViewById(R.id.orient_x_value);
        orientYValue = (TextView) findViewById(R.id.orient_y_value);
        orientZValue = (TextView) findViewById(R.id.orient_z_value);*/
      
        // Initialize accelerometer related view elements
        accelXValue.setText("0.00");
        accelYValue.setText("0.00");
        accelZValue.setText("0.00");
      
        // Initialize orientation related view elements
        /**orientXValue.setText("0.00");
        orientYValue.setText("0.00");
        orientZValue.setText("0.00");*/
        
        // start ClientThread
        new Thread(new ClientThread()).start();
    }
    
    class ClientThread implements Runnable {
        @Override
        public void run() {
        	try {
        		InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
        		socket = new Socket(serverAddr, SERVERPORT);
        		} catch (UnknownHostException e1) {
        			e1.printStackTrace();
        		} catch (IOException e1) {
        			e1.printStackTrace();
        		}
        }		 
    }
  
    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent) {
	    synchronized (this) {
		    if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			    accelXValue.setText(Float.toString(sensorEvent.values[0]));
			    accelYValue.setText(Float.toString(sensorEvent.values[1]));
			    accelZValue.setText(Float.toString(sensorEvent.values[2]));    
			    
			    try {
			    	byte[] arg1 = new byte[8];
			    	ByteBuffer.wrap(arg1).putDouble(sensorEvent.values[0]);
			    	byte[] arg2 = new byte[8];
			    	ByteBuffer.wrap(arg2).putDouble(sensorEvent.values[1]);
			    	byte[] arg3 = new byte[8]; 
			    	ByteBuffer.wrap(arg3).putDouble(sensorEvent.values[2]);
			    	byte[] args = new byte[24]; 
			    	for (int i=0; i<8; i++) args[i] = arg1[i];
			    	for (int i=0; i<8; i++) args[i+8] = arg2[i];
			    	for (int i=0; i<8; i++) args[i+16] = arg3[i];
			    	OutputStream out = socket.getOutputStream();
			    	out.write(args);
			    } catch (UnknownHostException e) {
			    	e.printStackTrace();
			    } catch (IOException e) {
			    	e.printStackTrace();
			    } catch (Exception e) {
			    	e.printStackTrace();
			    }
			    
		    }
    
		    /**if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			    orientXValue.setText(Float.toString(sensorEvent.values[0]));
			    orientYValue.setText(Float.toString(sensorEvent.values[1]));
			    orientZValue.setText(Float.toString(sensorEvent.values[2]));    
		    }*/
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
	    // ...and the orientation sensor
	    //sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }
  
    @Override
    protected void onStop() {
	    // Unregister the listener
	    //sensorManager.unregisterListener((SensorListener) this);
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
