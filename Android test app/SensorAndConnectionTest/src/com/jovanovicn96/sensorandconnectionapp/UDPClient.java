package com.jovanovicn96.sensorandconnectionapp;

import java.io.*;
import java.net.*;

public class UDPClient {
	
	static BufferedReader inFromUser;
	static DatagramSocket clientSocket;
	static InetAddress IPAddress;
	static int port;
	
	public UDPClient(String IA, int po) throws Exception {
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(IA);
		port = po;
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
		byte[] receiveData = new byte[48]; //duzina niza promenljivih za resive u bajtovima
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
