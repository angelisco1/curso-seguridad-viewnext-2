package com.curso.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

import org.owasp.esapi.ESAPI;

import com.curso.models.Informe;
import com.curso.utils.DatabaseUtil;

@WebServlet("/authenticated/buscar-informes")
public class BuscarInformesServlet extends HttpServlet {

	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String busqueda = req.getParameter("busqueda");
		String busquedaSaneada = ESAPI.encoder().encodeForHTML(busqueda);
		
		List<Informe> informes = new ArrayList<>();
		Connection connection = null;
		
		try {
			connection = DatabaseUtil.getConnection();
			
			String sql = "SELECT * FROM informes WHERE titulo LIKE ? OR descripcion LIKE ?";
			PreparedStatement pst = connection.prepareStatement(sql);
//			pst.setString(1, "%"+busqueda+"%");
//			pst.setString(2, "%"+busqueda+"%");
			pst.setString(1, "%"+busquedaSaneada+"%");
			pst.setString(2, "%"+busquedaSaneada+"%");
			
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				String id = rs.getString("id");
				String titulo = rs.getString("titulo");
				String descripcion = rs.getString("descripcion");
				String contenido = rs.getString("contenido");
				String color = rs.getString("color");
				Integer userId = rs.getInt("userId");
				
				informes.add(new Informe(id, titulo, descripcion, contenido, color, userId));				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeConnection(connection);
		}
		
		req.setAttribute("busqueda", busquedaSaneada);
		req.setAttribute("informes", informes);
		req.getRequestDispatcher("busqueda-informes.jsp").forward(req, resp);
		
	}
	
}
