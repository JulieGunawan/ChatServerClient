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
 *
 */
public class ClientGUI {
	
	private String username;
	private JFrame frame;
	private JList chatList;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private Socket socket;
	private DefaultListModel chatListModel;
	
	private final static int WIDTH=10;
	private final static int LENGTH=10;
	
	
	public ClientGUI() {
		this.frame = new JFrame("Chat App");
		
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		this.frame.setSize(800, 800);
		
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
		JPanel boardGame = new JPanel(new BorderLayout(20,20));
		
		JLabel player1Label = new JLabel("Player 1");
		JPanel player1Board = new JPanel(new BorderLayout(10,10));
		JPanel player1Battleship = this.createBoardPanel(WIDTH, LENGTH);
		
		player1Board.add(player1Label, BorderLayout.NORTH);
		player1Board.add(player1Battleship, BorderLayout.CENTER);
		
		JLabel player2Label = new JLabel ("Player 2");
		JPanel player2Board = new JPanel(new BorderLayout(10,10));
		JPanel player2Battleship = this.createBoardPanel(WIDTH, LENGTH);
		
	    player2Board.add(player2Label, BorderLayout.NORTH);
		player2Board.add(player2Battleship, BorderLayout.CENTER);
		
		boardGame.add(player1Board, BorderLayout.NORTH);
		boardGame.add(player2Board, BorderLayout.CENTER);
		
		return boardGame;
	}
	
	private JPanel createBoardPanel(int width, int length) {
		JPanel battleshipBoard = new JPanel(new GridLayout(width, length));
		JButton [][] grid = new JButton[width][length];
		
		for (int i=0; i<length; i++) {
			for(int j=0; j<width; j++) {
				grid[i][j] = new JButton("("+(i+1)+","+(j+1)+")");
				battleshipBoard.add(grid[i][j]);
			}
		}
				
		return battleshipBoard;
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
			
			
		});
				
		sendTextPanel.add(textInputLabel,BorderLayout.NORTH);
		sendTextPanel.add(textField, BorderLayout.CENTER);
		sendTextPanel.add(sendButton, BorderLayout.EAST);
		
		panel.add(sendTextPanel, BorderLayout.SOUTH);
		return panel;
	}
	
	public void addMessage(String message) {
		this.chatListModel.addElement(message);
		
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
				
				ServerHandler serverHandler = new ServerHandler(socket, ois, this);
				Thread thread = new Thread(serverHandler);
				
				thread.start();
				
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
