package problemdomain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message  implements Serializable{
	private Date date;
	private String username;
	private String message;
	
	public Message() {
		
	}
	
	public Message(String username, String message) {
		this.username=username;
		this.date = new Date();
		this.message=message;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public String getUsername() {
		return this.username;
	}
	public String getMessage() {
		return this.message;
	}
	
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return String.format("[%s] %s sent: %s", format.format(this.date), this.username, this.message);
		
	}
}