package com.Connect4.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Connect4GUI {
	
    private int xsize;
    private int ysize;
    private String boardSize;
    private int currentPlayer;
    private String currentPlayerColor;
    private int playerTurn = 1; //Red starts first
    private int redWins;
    private int blackWins;
    private int portNumDisplay;
    private boolean gameOver;
	private JFrame frame;
    private JLabel[][] slots;
    private JButton[] buttons;
    private JPanel c4Panel;
    private JPanel optionsPanel;
    private JLabel statusLabel;
    private JLabel turnLabel;
    private JLabel recordLabel;
    private JLabel connectedToLabel;
	private JButton newGameButton;
	private JButton forfeitButton;
	private JButton disconnectButton;
    private Connect4BoardListener c4bl;
    private NewGameListener ngl;
	private PrintWriter writer;
	private Socket socket;
    
    public static void main(String[] args){
    	new Connect4GUI();
    }

	public Connect4GUI() {
		c4bl = new Connect4BoardListener();
		ngl = new NewGameListener();
		frame = new JFrame("Connect 4 Online by Tommy Ho");
		setConnectView(frame);
	}
	
	private void setConnectView(JFrame frame){
		JPanel panel = (JPanel) frame.getContentPane();
	    panel.setLayout(new GridLayout(2, 1));
		JTextField userTextField = new JTextField("Enter server name/port");
		
        userTextField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                userTextField.setText("");
            }
        });
		
		userTextField.setBorder(new LineBorder(Color.black));
		userTextField.addActionListener(c4bl);
		JLabel connectLabel = new JLabel("Press ENTER to connect");
		connectLabel.setBorder(new LineBorder(Color.black));
		panel.add(userTextField);
		panel.add(connectLabel);
		ImageIcon img = new ImageIcon("WebContent/images/c4.jpg");
		frame.setIconImage(img.getImage());
		frame.setContentPane(panel);
		frame.setSize(300, 100);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
		
	
	private void setConnect4View(){
		frame.setVisible(false);
		frame = new JFrame("Connect 4 Online by Tommy Ho");
	 	c4Panel = (JPanel) frame.getContentPane();
	    c4Panel.setLayout(new GridLayout(ysize + 1, xsize));
		slots = new JLabel[xsize][ysize];
		buttons = new JButton[xsize];
		for (int i = 0; i < xsize; i++) {
			buttons[i] = new JButton("" + (i + 1));
			buttons[i].addActionListener(c4bl);
			c4Panel.add(buttons[i]);
		}
		for (int row = 0; row < ysize; row++) {
			for (int col = 0; col < xsize; col++) {
				slots[col][row] = new PieceLabel("blank", boardSize);
				slots[col][row].setText("0");
				slots[col][row].setOpaque(true);
				slots[col][row].setBackground(Color.LIGHT_GRAY);
				slots[col][row].setHorizontalAlignment(SwingConstants.CENTER);
				slots[col][row].setBorder(new LineBorder(Color.black));
				c4Panel.add(slots[col][row]);
			}
		}
	        
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(4, 2));
		optionsPanel.setBorder(new LineBorder(Color.black));
		turnLabel = new JLabel();
		turnLabel.setBorder(new LineBorder(Color.black));
		turnLabel.setText("First turn is Red's turn");
		JLabel playerLabel = new JLabel();
		playerLabel.setBorder(new LineBorder(Color.black));
		playerLabel.setText("You are: " + currentPlayerColor);
		recordLabel = new JLabel();
		recordLabel.setBorder(new LineBorder(Color.black));
		recordLabel.setText("Current score");
		statusLabel = new JLabel();
		statusLabel.setBorder(new LineBorder(Color.black));
		statusLabel.setText("Connect 4 Online by Tommy Ho");
		newGameButton = new JButton("New Game");
		newGameButton.setBorder(new LineBorder(Color.black));
		newGameButton.addActionListener(ngl);
		forfeitButton = new JButton("I Forfeit");
		forfeitButton.setBorder(new LineBorder(Color.black));
		forfeitButton.addActionListener(ngl);
		connectedToLabel = new JLabel("You are connected to: Port " + portNumDisplay);
		connectedToLabel.setBorder(new LineBorder(Color.black));
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setBorder(new LineBorder(Color.black));
		disconnectButton.addActionListener(ngl);
		
		
		optionsPanel.add(newGameButton);
		optionsPanel.add(forfeitButton);
		optionsPanel.add(connectedToLabel);
		optionsPanel.add(disconnectButton);
		optionsPanel.add(playerLabel);
		optionsPanel.add(recordLabel);
		optionsPanel.add(turnLabel);
		optionsPanel.add(statusLabel);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));
		panel.add(optionsPanel);
		panel.add(c4Panel);
		ImageIcon img = new ImageIcon("WebContent/images/c4.jpg");
		frame.setIconImage(img.getImage());
		frame.setContentPane(panel);
		frame.setSize(500, 700);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void updateView(String returnMessage){
		System.out.println(returnMessage);
		String[] splitMessage = returnMessage.split(":");
		String command = splitMessage[0];
		String playerNum = splitMessage[1];
		String commandContent = splitMessage[2];
		
		switch (command){
			case "start":
				currentPlayer = Integer.parseInt(playerNum);
				if (currentPlayer == 1){
					currentPlayerColor = "Red";
				} else {
					currentPlayerColor = "Black";
				}
				boardSize = commandContent;
				setBoardSize(boardSize);
				setConnect4View();
				break;
				
			case "move":
				if (playerNum.equals("1")){
					playerTurn = 2;
					turnLabel.setText("Currently Black's turn");
				} else {
					playerTurn = 1;
					turnLabel.setText("Currently Red's turn");
				}
				updateBoardView(playerNum, commandContent, c4Panel); //pass player + column placed + this panel
				break;
				
			case "win":
				if (playerNum.equals("1")){
					statusLabel.setText("Red is the winner!");
					redWins++;
				} else {
					statusLabel.setText("Black is the winner!");
					blackWins++;
				}
				gameOver = true;
				recordLabel.setText("Red: " + redWins + "       Black: " + blackWins);
				updateBoardView(playerNum, commandContent, c4Panel);
				break;
				
			case "new":
				gameOver = false;
				System.out.println(gameOver);
				statusLabel.setText("A new game has started...");
				recordLabel.setText("Red: " + redWins + "       Black: " + blackWins);
				clearBoardView();
				break;
				
			case "forfeit":
				gameOver = true;
				if (playerNum.equals("2")){
					statusLabel.setText("Red is the winner!");
					redWins++;
				} else {
					statusLabel.setText("Black is the winner!");
					blackWins++;
				}
				recordLabel.setText("Red: " + redWins + "       Black: " + blackWins);
				break;
				
			case "disconnect":
				gameOver = true;
				if (currentPlayer == Integer.parseInt(playerNum)){
					frame.setVisible(false);
				} else {
					ArrayList<Component> panel = new ArrayList<>(Arrays.asList(c4Panel.getComponents()));
					ArrayList<Component> secondPanel = new ArrayList<>(Arrays.asList(optionsPanel.getComponents()));
					panel.addAll(secondPanel);
					for (Component b : panel){
						if (b instanceof JButton && !((JButton) b).getText().equals("Disconnect")){
							((JButton) b).removeActionListener(c4bl);
							((JButton) b).removeActionListener(ngl);
						}
					}
				}
				
				if (playerNum.equals("2")){
					statusLabel.setText("Black has disconnected!");
					redWins++;
				} else {
					statusLabel.setText("Red has disconnected!");
					blackWins++;
				}
				recordLabel.setText("Red: " + redWins + "       Black: " + blackWins);
				break;
				
			case "failure":
				if (Integer.parseInt(playerNum) == currentPlayer){
					statusLabel.setText("This column is full. Try again.");
				}
				break;
		}
		
		frame.revalidate();
		frame.repaint();
		
	}
	
	private void updateBoardView(String player, String columnPlaced, JPanel c4Panel){
		int columnIndex = Integer.parseInt(columnPlaced) - 1;
		JLabel[][] slots = this.slots;
		int rowIndex = slots[columnIndex].length - 1;
		boolean updated = false;
		while (updated != true){
			//iterates to find first empty slot in column and changes color
			if (slots[columnIndex][rowIndex].getText().equals("0")){
				slots[columnIndex][rowIndex].setText(player);
				if (player.equals("1")){
					((PieceLabel) slots[columnIndex][rowIndex]).changeColor("red");
				} else {
					((PieceLabel) slots[columnIndex][rowIndex]).changeColor("black");
				}
				updated = true;
			}
			rowIndex--;
		}
		if (!gameOver){
			statusLabel.setText("Piece placed in column " +  columnPlaced);
		}
	}
	
	private void clearBoardView(){
		for (int x = 0; x < xsize; x ++){
			for (int y = 0; y < ysize; y++){
				slots[x][y].setText("0");
				((PieceLabel) slots[x][y]).changeColor("blank");
			}
		}
	}
	
	
	private void setBoardSize(String size){
		if (size.equals("standard")){
			xsize = 7;
			ysize = 6;
		} else if (size.equals("epic")){
			xsize = 10;
			ysize = 9;
		}
	}

	private class Connect4BoardListener implements ActionListener {

		private boolean isConnected;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isConnected){
				connectToServer(e);
				isConnected = true;
			} else {
				if (currentPlayer == playerTurn){
					//send message to server with buttonPressed + player #
					String buttonPressed = ((JButton) e.getSource()).getText();
					writer.println(buttonPressed + ":" + currentPlayer);
					writer.flush();
				}
			}
		}
		
		private void connectToServer(ActionEvent e){
			String port = ((JTextField) e.getSource()).getText();
			int portNum =  Integer.parseInt(port);
			portNumDisplay = portNum;
			try {
				socket = new Socket("localhost", portNum);
				writer = new PrintWriter(socket.getOutputStream());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			new Thread(new ServerListenerThread(portNum, socket)).start();
		}
	}
	
	private class NewGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonPressed = ((JButton) e.getSource()).getText();
			if (buttonPressed.equals("New Game")){
				if (gameOver){ //Utilized socket connection in other button
					writer.println(buttonPressed + ":" + currentPlayer);
					writer.flush();
				} else { //Game is not over, user must forfeit to start new game
					statusLabel.setText("Forfeit to reset game");
				}
			} else if (buttonPressed.equals("I Forfeit")) {
				writer.println(buttonPressed + ":" + currentPlayer);
				writer.flush();
			} else if (buttonPressed.equals("Disconnect")){
				writer.println(buttonPressed + ":" + currentPlayer);
				writer.flush();
			}
		}
		
	}
	
	class ServerListenerThread implements Runnable {
		
		private InputStreamReader isReader;
		private BufferedReader reader;
		
		public ServerListenerThread(int port, Socket socket){
			try {
				isReader = new InputStreamReader(socket.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			String message;
			
			try {
				while ((message = reader.readLine()) != null){
					updateView(message);
					if (message.matches("disconnect*")){
						break;
					}
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	
	
}

