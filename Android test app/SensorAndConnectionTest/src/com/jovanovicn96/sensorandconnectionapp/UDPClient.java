package com.jovanovicn96.sensorandconnectionapp;

import java.io.*;
import java.net.*;

public class UDPClient {
	
	BufferedReader inFromUser;
	DatagramSocket clientSocket;
	InetAddress IPAddress;
	int port, resivemessln;
	
	public UDPClient(String IA, int po, int resmessln) throws Exception {
		System.out.println("RACEMANIA: Creating socket");
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(IA);
		port = po;
		resivemessln = resmessln;
	}
	
	public void send(byte message[]) throws IOException
	{
		System.out.println("RACEMANIA: Sending data");
		byte[] sendData = message;
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
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
		System.out.println("RACEMANIA: Closing socket");
		clientSocket.close();
	}
}
