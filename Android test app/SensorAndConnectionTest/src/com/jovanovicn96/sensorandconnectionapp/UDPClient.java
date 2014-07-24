package com.jovanovicn96.sensorandconnectionapp;

import java.io.*;
import java.net.*;

public class UDPClient {
	
	BufferedReader inFromUser;
	DatagramSocket clientSocket;
	InetAddress IPAddress;
	int port, resivemessln;
	
	public UDPClient(String IA, int po, int resmessln) throws Exception {
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(IA);
		port = po;
		resivemessln = resmessln;
	}
	
	public void send(byte message[]){
		byte[] sendData = message;
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		try {
			clientSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] resive(){
		byte[] receiveData = new byte[resivemessln];
		for (int i=0; i<48; i++) receiveData[i] = (byte) 0;
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			clientSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return receiveData;
	}
	
	public void close(){
		clientSocket.close();
	}
}