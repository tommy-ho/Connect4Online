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
	private static ArrayList<String> activeRooms;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		if (!checkServerExist(request.getParameter("signIn"))){
			startNewServer(request, response);
		}
	}
	
	private void startNewServer(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String roomName = request.getParameter("signIn");
		String boardSize = request.getParameter("boardSize");
		new Thread(new Connect4Server(roomName, boardSize)).start();
		activeRooms.add(roomName);
		
		try {
			request.getSession().setAttribute("status", "Server is started, please log in via GUI");
			request.getRequestDispatcher("connect4.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkServerExist(String name){
		if (activeRooms == null){
			activeRooms = new ArrayList<String>();
		}
		
		if (activeRooms.contains(name)){
			return true;
		} else {
			return false;
		}
	}

}