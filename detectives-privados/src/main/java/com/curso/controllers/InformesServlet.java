package com.curso.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.owasp.esapi.reference.RandomAccessReferenceMap;

import com.curso.models.Informe;
import com.curso.utils.DatabaseUtil;


@WebServlet("/authenticated/informes")
public class InformesServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Informe> informes = new ArrayList<>();
		Connection connection = null;
		
		try {
			connection = DatabaseUtil.getConnection();
			
			RandomAccessReferenceMap armap = new RandomAccessReferenceMap();
			
			String sql = "SELECT * FROM informes";
			
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next()) {
				String id = rs.getString("id");
				String titulo = rs.getString("titulo");
				String descripcion = rs.getString("descripcion");
				String contenido = rs.getString("contenido");
				String color = rs.getString("color");
				Integer userId = rs.getInt("userId");
				
				String indirectId = armap.addDirectReference(id);
				
				informes.add(new Informe(indirectId, titulo, descripcion, contenido, color, userId));				
			}
			
			HttpSession session = req.getSession(false);
			session.setAttribute("armap", armap);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeConnection(connection);
		}
		
		req.setAttribute("informes", informes);
		req.getRequestDispatcher("informes.jsp").forward(req, resp);
		
	}
	
}
