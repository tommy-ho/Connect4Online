package com.Connect4.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import com.Connect4.server.Connect4Board;

public class Connect4Server implements Runnable{
	
	private int serverPortNumber;
	private ArrayList<Writer> clientWriterArrayList;
	private int clientsConnected;
	private Connect4Board board;
	private static final String STARTUP = "start";
	private static final String MOVE = "move";
	private static final String WIN = "win";
	private static final String NEW = "new";
	private static final String FORFEIT = "forfeit";
	private static final String FAILURE = "failure";
	
	
	public Connect4Server(String port, String boardSize) {
		System.out.println(boardSize + " Server is started at port: " +  port);
		serverPortNumber = Integer.parseInt(port);
		clientsConnected = 0;
		board = new Connect4Board(boardSize);
	}

	@Override
	public void run() {
		clientWriterArrayList = new ArrayList<Writer>();
		try {
			ServerSocket serverSocket = new ServerSocket(serverPortNumber);
			while (clientsConnected != 2){
				Socket clientSocket = serverSocket.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientWriterArrayList.add(writer);
				Thread t = new Thread(new ClientManager(clientSocket));
				t.start();
				clientsConnected++;
				writer.println(STARTUP + ":" + clientsConnected + ":" + board.boardSize);
				writer.flush();
			}
			serverSocket.close(); 	//2 Clients have connected to server
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void publishMessage(String message){
		Iterator<Writer> it = clientWriterArrayList.iterator();
		while (it.hasNext()){
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private class ClientManager implements Runnable {
		
		private Socket clientSock;
		private BufferedReader reader;
		
		/**
		 * This constructor takes a socket and generates an InputStreamReader/BufferedReader
		 * for the ClientManager object.
		 *
		 * @param sock a socket for communication between the server and this particular client
		 * @return
		 * @see
		 */
		private ClientManager(Socket sock){
			try {
				clientSock = sock;
				InputStreamReader isReader = new InputStreamReader(clientSock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		/**
		 * The run() method here overrides the method inherited from the Runnable interface.
		 * Starting a new thread, it reads from the client via the ClientManager's InputStreamReader
		 * and passes the message to the outer class LockerRoomServer to handle. If it receives
		 * a disconnect message from the client, it will publish a message and terminate thread.
		 *
		 * @param
		 * @return
		 * @see #publishMessage()
		 */
		@Override
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null){
					//parse message here into color/column if it is that color's turn (switch variable?)
					String[] messageSplit = message.split(":");
					
					if (messageSplit[0].equals("New Game")){
						board.setEmpty();
						System.out.println("Board emptied on server");
						publishMessage(NEW + ":" + messageSplit[1] + ":" + -1);
						continue;
					} else if (messageSplit[0].equals("I Forfeit")){
						publishMessage(FORFEIT + ":" + messageSplit[1] + ":" + -1);
						continue;
					}
					
					int column = Integer.parseInt(messageSplit[0]);
					int userColor = Integer.parseInt(messageSplit[1]);
					System.out.println(userColor + " places in column " + column);
				
					boolean success = board.updateBoard(column, userColor);
					if (success){
						if (board.checkWinConditions(column, userColor)){
							//submit win/lose message back to the clients
							//append message with another section
							publishMessage(WIN + ":" + userColor + ":" + column);
						} else {
							//submit success message here back to the clients
							//to update the board. utilizes MOVE header
							publishMessage(MOVE + ":" + userColor + ":" + column);
						}
					} else if (!success){
						//submit failure to update back to client, prompt their turn again
						publishMessage(FAILURE + ":" + userColor + ":" + column);
					}
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}//end of inner class
}
