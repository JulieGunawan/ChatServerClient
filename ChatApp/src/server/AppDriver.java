package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


import problemdomain.*;

public class AppDriver {
	public static void main (String[] args) throws IOException {
		ServerSocket listener = new ServerSocket(1234);
		
		System.out.println("Waiting for connecting port 1234...");
		
		ArrayList<ClientConnection> waitingClients = new ArrayList<ClientConnection>();
		
		while (listener.isBound()) {
			try {
				Socket client = listener.accept();
				System.out.println("Client connected.");
				
				InputStream inputStream = client.getInputStream();				
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				
				OutputStream outputStream = client.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
					
				ClientConnection clientConnection = new ClientConnection(client, objectInputStream, objectOutputStream);
				waitingClients.add(clientConnection);
				
				if(waitingClients.size()%2==0) {
					ClientConnection cc1 = waitingClients.get(0);
					ClientConnection cc2 = waitingClients.get(1);
					
					ChatHandler chatHandler1 = new ChatHandler(cc1, cc2);
					Thread thread = new Thread(chatHandler1);
					thread.start();
					
					ChatHandler chatHandler2 = new ChatHandler(cc2, cc1);
					Thread thread2 = new Thread(chatHandler2);
					thread2.start();
					
					waitingClients.remove(cc1);
					waitingClients.remove(cc2);
				}
				
				
			}
			catch (IOException ex) {			
			
			}
		}
		listener.close();
	}
}
