package com.jovanovicn96.sensorandconnectionapp;

import java.io.*;
import java.net.*;

public class UDPClient {
	
	DatagramSocket clientSocket;
	int port;
	
	public UDPClient(int po) throws Exception {
		clientSocket = new DatagramSocket(po);
		clientSocket.setSoTimeout(200);
		port = po;
	}
	
	public void send(byte message[], String IA) { 
		try {
			InetAddress IPAddress;
			IPAddress = InetAddress.getByName(IA);
			byte[] sendData = message;
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public byte[] resive(int resivemessln) throws IOException {
		byte[] receiveData = new byte[resivemessln];
		for (int i=0; i<resivemessln; i++) receiveData[i] = (byte) 0;
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		return receiveData;
	}
	
	public void close(){
		clientSocket.close();
	}
}
