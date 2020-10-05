/**
 * 
 */
package server;

import java.io.IOException;

import problemdomain.ClientConnection;
import problemdomain.Message;

/**
 * @author 751682
 *
 */
public class InputHandler implements Runnable {

	private ClientConnection cc1;
	private ClientConnection cc2;
	
	public InputHandler(ClientConnection clientConnection1, ClientConnection clientConnection2) {
		this.cc1 = clientConnection1;
		this.cc2 = clientConnection2;
	}

	@Override
	public void run() {
		try {
			while (this.cc1.getClient().isConnected() && this.cc2.getClient().isConnected()) {
				Message message = (Message) this.cc1.getObjectInputStream().readObject();
				System.out.println("Receive message: "+ message.toString());
				
				
				this.cc2.getObjectOutputStream().writeObject(message);
				
				//might want to implement the code below
				//Message byeMessage = new Message("Server","Bye!");
				//Message send = new Message("Server", "Okay!");
				/*if (message.getMessage().equalsIgnoreCase("goodbye")) {		
					//this.oos.writeObject(byeMessage);
					
					//client.close();					
					break;
				} 
				else			
					this.oos.writeObject(send);*/
				
				//Message received=(Message)this.cc2.getObjectInputStream().readObject();
				//System.out.println("Received message: "+received.toString());
				//this.cc1.getObjectOutputStream().writeObject(received);			
				
			}	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		
	}
}
