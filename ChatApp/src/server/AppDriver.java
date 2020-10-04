package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import problemdomain.Message;

public class AppDriver {
	public static void main (String[] args) throws IOException {
		ServerSocket listener = new ServerSocket(1234);
		
		System.out.println("Waiting for connecting port 1234...");
		
		while (listener.isBound()) {
			try {
				Socket client = listener.accept();
				System.out.println("Client connected.");
				
				InputStream inputStream = client.getInputStream();				
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				
				OutputStream outputStream = client.getOutputStream();
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
					
				System.out.println("Waiting for messages ...");
				while (client.isConnected()) {
					Message message = (Message) objectInputStream.readObject();
					System.out.println("Receive message: "+ message.toString());
					Message byeMessage = new Message("Server","Bye!");
					Message send = new Message("Server", "Okay!");
					
					
					if (message.getMessage().equalsIgnoreCase("goodbye")) {		
						objectOutputStream.writeObject(byeMessage);
						
						client.close();
						break;
					}
					
					objectOutputStream.writeObject(send);
					
				}
			}
			catch (IOException ex) {
				
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		listener.close();
	}
}
