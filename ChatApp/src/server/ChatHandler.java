/**
 * 
 */
package server;

import java.io.*;
import java.net.*;

import problemdomain.*;

/**
 * @author 751682
 *
 */
public class ChatHandler implements Runnable {
	
	private ClientConnection cc1;
	private ClientConnection cc2;
	
	public ChatHandler(ClientConnection cc1, ClientConnection cc2) {
		this.cc1=cc1;
		this.cc2=cc2;
	}
	
	@Override
	public void run() {
		System.out.println("Waiting for messages ...");
		try {
			while (this.cc1.getClient().isConnected() && this.cc2.getClient().isConnected()) {
				Message message = (Message) this.cc1.getObjectInputStream().readObject();
				System.out.println("Receive message: "+ message.toString());
				//Message byeMessage = new Message("Server","Bye!");
				//Message send = new Message("Server", "Okay!");
				
				this.cc2.getObjectOutputStream().writeObject(message);
				
				//might want to implement the code below
				/*if (message.getMessage().equalsIgnoreCase("goodbye")) {		
					//this.oos.writeObject(byeMessage);
					
					//client.close();					
					break;
				} 
				else			
					this.oos.writeObject(send);*/
				
				Message received=(Message)this.cc2.getObjectInputStream().readObject();
				System.out.println("Received message: "+received.toString());
				this.cc1.getObjectOutputStream().writeObject(received);
				
				
			}	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
