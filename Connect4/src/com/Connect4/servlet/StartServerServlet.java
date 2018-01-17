package com.Connect4.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Connect4.server.Connect4Server;

@WebServlet("/StartServerServlet")
public class StartServerServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static ArrayList<String> activeRooms = new ArrayList<String>();;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if (!checkServerExist(request.getParameter("signIn"))){
			startNewServer(request, response);
		} else {
			try {
				request.getSession().setAttribute("status", "Port occupied. Please try another.");
				request.getRequestDispatcher("retry.jsp").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void startNewServer(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String roomName = request.getParameter("signIn");
		String boardSize = request.getParameter("boardSize");
		new Thread(new Connect4Server(roomName, boardSize)).start();
		
		try {
			request.getSession().setAttribute("status", "Server is started, please log in via GUI");
			request.getRequestDispatcher("connect4.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkServerExist(String name){
		System.out.println(activeRooms);
		synchronized(activeRooms){
			if (activeRooms.contains(name)){
				return true;
			} else {
				activeRooms.add(name);
				return false;
			}
		}
	}

}