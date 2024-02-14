package com.curso.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.MySQLCodec;

import com.curso.models.User;
import com.curso.utils.DatabaseUtil;
import com.curso.utils.PasswordUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		String usernameSaneado = ESAPI.encoder().encodeForSQL(new MySQLCodec(MySQLCodec.Mode.STANDARD), username);
		String passwordSaneada = ESAPI.encoder().encodeForSQL(new MySQLCodec(MySQLCodec.Mode.STANDARD), password);
		System.out.println(usernameSaneado);
		System.out.println(passwordSaneada);
		
		
		Connection connection = null;
		
		try {
			connection = DatabaseUtil.getConnection();
			
//			String sql = "SELECT * FROM users WHERE username='" + usernameSaneado + "' and password='" + passwordSaneada + "'";
//			System.out.println("[+] SQL: " + sql);
//			String sql = "SELECT * FROM users WHERE username=? and password=?";
			String sql = "SELECT * FROM users WHERE username=?";
//			
//			Statement st = connection.createStatement();
//			ResultSet rs = st.executeQuery(sql);
			
			PreparedStatement pst = connection.prepareStatement(sql);
			pst.setString(1, username);
//			pst.setString(2, password);
			
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				String hashedPassword = rs.getString("password");
				if (!PasswordUtil.checkPassword(password, hashedPassword)) {
					resp.sendRedirect("login.html");
					return;
				}
				
				
				Integer id = rs.getInt("id");
				String name = rs.getString("name");
				username = rs.getString("username");
				String web = rs.getString("web");
				String role = rs.getString("role");
				
				User user = new User(id, name, username, null, web, role);
				
				HttpSession session = req.getSession(false);
				if (session != null) {
					session.invalidate();
				}
				
				session = req.getSession(true);
				
				session.setAttribute("user", user);
				
				
				Cookie galletita = new Cookie("mi-galleta", "con-pepitas-de-chocolate");
				
				galletita.setHttpOnly(true);
//				galletita.setMaxAge(40);
				
				resp.addCookie(galletita);
				
//				req.setAttribute("user", user);
//				req.getRequestDispatcher("authenticated/home.jsp").forward(req, resp);
				
				resp.sendRedirect("authenticated/home.jsp");
				
			} else {
				resp.sendRedirect("login.html");
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.sendRedirect("login-intruso.html");
		} finally {
			DatabaseUtil.closeConnection(connection);
		}
		
		
	}
	
}
