/**
 * 
 */
package client;

import java.io.*;
import java.net.Socket;

import problemdomain.Message;

/**
 * @author 751682
 *
 */
public class ServerHandler implements Runnable {

	private Socket socket;
	private ObjectInputStream ois;
	private ClientGUI gui;
	
	public ServerHandler(Socket socket, ObjectInputStream ois, ClientGUI gui) {
		this.socket=socket;
		this.ois = ois;
		this.gui =gui;
	}
	
	@Override
	public void run() {
		while(!socket.isClosed()) {
		//while(socket.isConnected()) {
			//Receive message from server
			try {
				Message received = (Message) this.ois.readObject();
				this.gui.addMessage(received.toString());
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
 
   
}
