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
		this.cc1 = cc1;
		this.cc2 = cc2;
	}
	
	@Override
	public void run() {
		System.out.println("Waiting for messages ...");
		
		InputHandler inputHandler1 = new InputHandler(this.cc1, this.cc2);
		Thread thread1 = new Thread(inputHandler1);
		thread1.start();
		
		InputHandler inputHandler2 = new InputHandler(this.cc2, this.cc1);
		Thread thread2 = new Thread(inputHandler2);
		thread2.start();
		
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
