/**
 * 
 */
package problemdomain;

import java.io.*;
import java.net.Socket;

/**
 * @author 751682
 *
 */
public class ClientConnection {
	private Socket client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ClientConnection(Socket client, ObjectInputStream ois, ObjectOutputStream oos) {
		this.client = client;
		this.ois=ois;
		this.oos=oos;
	}
	
	public Socket getClient() {
		return this.client;
	}
	
	public ObjectInputStream getObjectInputStream() {
		return this.ois;
	}
	
	public ObjectOutputStream getObjectOutputStream() {
		return this.oos;
	}
}
