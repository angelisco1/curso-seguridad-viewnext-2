package com.curso.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.ESAPI;

import com.curso.models.User;
import com.curso.utils.DatabaseUtil;

@WebServlet("/authenticated/nuevo-informe")
public class NuevoInformeServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpSession session = req.getSession(false);
		String tokenCSRF = UUID.randomUUID().toString();
		session.setAttribute("tokenCSRF", tokenCSRF);
		
		req.getRequestDispatcher("nuevo-informe.jsp").forward(req, resp);
		
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String titulo = req.getParameter("titulo");
		String contenido = req.getParameter("contenido");
		String descripcion = req.getParameter("descripcion");
		String color = req.getParameter("color");
		
		String tokenCSRFForm = req.getParameter("tokenCSRF");
		
		Integer userId = null;
		String tokenCSRFSession = null;
		
		HttpSession session = req.getSession(false);
		if (session != null) {
			User user = (User) session.getAttribute("user");
			if (user != null) {
				userId = user.getId();
			}
			tokenCSRFSession = (String) session.getAttribute("tokenCSRF");
		}
		
		if (tokenCSRFForm == null || tokenCSRFSession == null || !tokenCSRFForm.equals(tokenCSRFSession)) {
			resp.sendRedirect("../login.html");
			return;
		}
		
		
		String tituloSaneado = ESAPI.encoder().encodeForHTML(titulo);
		String descripcionSaneada = ESAPI.encoder().encodeForHTML(descripcion);
		String contenidoSaneado = ESAPI.encoder().encodeForHTML(contenido);
		String colorSaneado = ESAPI.encoder().encodeForCSS(color);
		
		
		Connection connection = null;
		
		try {
			connection = DatabaseUtil.getConnection();
			
			String sql = "INSERT INTO informes (titulo, descripcion, contenido, color, userId) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pst = connection.prepareStatement(sql);
//			pst.setString(1, titulo);
//			pst.setString(2, descripcion);
//			pst.setString(3, contenido);
//			pst.setString(4, color);
			
			pst.setString(1, tituloSaneado);
			pst.setString(2, descripcionSaneada);
			pst.setString(3, contenidoSaneado);
			pst.setString(4, colorSaneado);
			
			pst.setInt(5, userId);
			
			pst.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeConnection(connection);
		}
		
		resp.sendRedirect("informes");
		
	}

	
	
	
}
