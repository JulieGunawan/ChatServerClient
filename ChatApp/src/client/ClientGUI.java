/**
 * 
 */
package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

import problemdomain.Message;

/**
 * @author 751682
 *1:33:23
 */
public class ClientGUI {
	
	private String username;
	private JFrame frame;
	private JList chatList;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private Socket socket;
	private DefaultListModel chatListModel;
	
	public ClientGUI() {
		this.frame = new JFrame("Chat App");
		
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		this.frame.setSize(400, 400);
		
		JPanel gamepanel = this.createGamePanel();
		this.frame.add(gamepanel, BorderLayout.NORTH);
		
		JPanel chatPanel = this.createChatPanel();
		this.frame.add(chatPanel, BorderLayout.CENTER);
	
		JPanel buttonsPanel = this.createButtonsPanel();
		this.frame.add(buttonsPanel, BorderLayout.SOUTH);
		
	}	
	
	/*private JPanel createBoardPanel() {
		JPanel panel = new JPanel();
		
		return panel;
	}*/
	
	private JPanel createGamePanel() {
		JPanel boardGame = new JPanel(new BorderLayout());
		
		JLabel player1Label = new JLabel("Player 1");
		JPanel player1Board = new JPanel(new BorderLayout());
		
		player1Board.add(player1Label);
		
		JLabel player2Label = new JLabel ("Player 2");
		JPanel player2Board = new JPanel(new BorderLayout());
	    player2Board.add(player2Label);
		
		boardGame.add(player1Board, BorderLayout.NORTH);
		boardGame.add(player2Board, BorderLayout.CENTER);
		
		return boardGame;
	}
	
		
	private JPanel createChatPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel msgBoardLabel = new JLabel("Message Board");
		
		this.chatListModel = new DefaultListModel();
		this.chatList = new JList(this.chatListModel);		
		
		panel.add(msgBoardLabel, BorderLayout.NORTH);
		panel.add(chatList, BorderLayout.CENTER);
		
		JPanel sendTextPanel = new JPanel(new BorderLayout());
		
		JLabel textInputLabel = new JLabel("Message to Send");
		JTextField textField = new JTextField();
		JButton sendButton = new JButton("Send");
		
		sendButton.addActionListener((ActionEvent evt) -> {
			
			String text = textField.getText();		
			Message msg = new Message(username, text); 
			
			if(this.oos == null) {
				JOptionPane.showMessageDialog(this.frame,"Please connect first");
				textField.setText("");
			}
			
			try {			
				this.oos.writeObject(msg);
				this.chatListModel.addElement(msg);
				
				if (msg.getMessage().equals("goodbye")) {
					chatListModel.addElement("Disconnected");
				}
				textField.setText("");
			} catch (IOException e) {			
				e.printStackTrace();
				JOptionPane.showMessageDialog(this.frame, "Unable to send message.");
			}	
			
			//Receive message from server
			try {
				Message received = (Message) this.ois.readObject();
				this.chatListModel.addElement(received);
				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		
		
		sendTextPanel.add(textInputLabel,BorderLayout.NORTH);
		sendTextPanel.add(textField, BorderLayout.CENTER);
		sendTextPanel.add(sendButton, BorderLayout.EAST);
		
		panel.add(sendTextPanel, BorderLayout.SOUTH);
		return panel;
	}
	
	private JPanel createButtonsPanel() {
		JPanel panel = new JPanel(new GridLayout(1,2));
		
		JButton connectButton = new JButton("Connect");		
		panel.add(connectButton);
		
		JButton disconnectButton = new JButton("Disconnect");
		panel.add(disconnectButton);
		disconnectButton.setEnabled(false);
		
		connectButton.addActionListener((ActionEvent evt) -> {
			
			String ip = JOptionPane.showInputDialog(this.frame, "Enter ip address or hostname: ");
			int port = Integer.parseInt(JOptionPane.showInputDialog(this.frame, "Enter port number: "));
		
			try {
				socket = new Socket(ip, port);
				
				OutputStream outputStream = socket.getOutputStream();
				this.oos = new ObjectOutputStream(outputStream);
				
				InputStream inputStream = socket.getInputStream();
				this.ois = new ObjectInputStream(inputStream);
				
				this.chatListModel.addElement("Connected");
				disconnectButton.setEnabled(true);
				
			} catch (IOException e) {
				this.chatListModel.addElement("Unable to connect");
				e.printStackTrace();
			}
			
		});		
		
		disconnectButton.addActionListener((ActionEvent evt)-> {
			try {
				
				this.ois.close();
				this.oos.close();
				this.socket.close();				
				this.chatListModel.addElement("Disconnected.");
				disconnectButton.setEnabled(false);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.chatListModel.addElement("Unable to disconnect");
			}			
		});
				
		return panel;
	}
	
	public void display() {
		this.frame.setVisible(true);
		this.username = JOptionPane.showInputDialog(this.frame, "Enter username");
	}
}
