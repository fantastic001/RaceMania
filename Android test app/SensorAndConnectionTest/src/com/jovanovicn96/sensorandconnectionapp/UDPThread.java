package com.jovanovicn96.sensorandconnectionapp;

import com.jovanovicn96.sensorandconnectionapp.UDPClient;
import java.net.*;
import java.io.*;

public class UDPThread extends Thread {
	
	private MainActivity act;
	private  boolean running = true;
	UDPClient udpClient;

	public UDPThread(MainActivity act) {
    	this.act = act;
		try {
			udpClient = new UDPClient(5001);
		} catch (Exception e) 
		{
			System.out.println("RACEMANIA: Error while creating recieving UDP client");
		}
	}
	public void terminate(){
		running = false;
	}
	
	@Override
	public void run() {
		while (running && !act.isFinishing()){
			byte[] rec;
			final int N = 3;
			try {
				rec = udpClient.resive(N);
			} 
			catch (SocketTimeoutException e) 
			{
				rec = new byte[N];
				for (int i = 0; i<N; i++) rec[i] = 0;
			}
			catch (IOException e) 
			{
				rec = new byte[N];
				for (int i = 0; i<N; i++) rec[i] = 0;
			}
			act.onReciveUDPmessage(rec);
		}
		udpClient.close();
		udpClient = null;
	}
		
}
