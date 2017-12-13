package com.Connect4.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class StartupGUI {
	
	private JFrame frame;
    private int xsize = 2;
    private int ysize = 1;
	
	public static void main(String[] args) {
		new StartupGUI();

	}

	public StartupGUI() {
		frame = new JFrame("Connect 4");
	    JPanel panel = (JPanel) frame.getContentPane();
	    panel.setLayout(new GridLayout(ysize, xsize));
	    
		JTextField userTextField = new JTextField("Enter server name");
		
        userTextField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                userTextField.setText("");
            }
        });
		
		
		userTextField.setBorder(new LineBorder(Color.black));
		JButton connectButton = new JButton("Connect to server");
		connectButton.setBorder(new LineBorder(Color.black));
		
		panel.add(userTextField);
		panel.add(connectButton);
		ImageIcon img = new ImageIcon("WebContent/images/c4.jpg");
		frame.setIconImage(img.getImage());
		frame.setContentPane(panel);
		frame.setSize(300, 100);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public class ConnectListener implements ActionListener {

		private PrintWriter writer;
		private Socket socket;
		
		public ConnectListener(){
			try {
				socket = new Socket("localhost", 5000);
				writer = new PrintWriter(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String buttonPressed = ((JButton) e.getSource()).getText();
			System.out.println(buttonPressed);
			//send message to server with buttonPressed # + player #
			//writer.println(buttonPressed + "||" + currentPlayer);
			//writer.flush();
			
		}
		
	}

}
