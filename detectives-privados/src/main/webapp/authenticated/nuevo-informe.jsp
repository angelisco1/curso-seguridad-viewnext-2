<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
</head>
<body>
  
	<h1>Nuevo informe</h1>
	
	<a href="nuevo-informe">Nuevo informe</a>
	<a href="informes">Informes</a>
	<a href="logout">Logout</a>
	
	<form action="nuevo-informe" method="POST">
		<div>
			<label for="titulo">Titulo:</label>
			<input type="text" id="titulo" name="titulo"> 
		</div>
		<div>
			<label for="descripcion">Descripcion:</label>
			<input type="text" id="descripcion" name="descripcion"> 
		</div>
		<div>
			<label for="contenido">Contenido:</label>
			<textarea id="contenido" name="contenido"></textarea>
		</div>
		<div>
			<label for="color">Color:</label>
			<input type="text" id="color" name="color"> 
		</div>
		
		<input type="text" name="tokenCSRF" value="${tokenCSRF}"  hidden />
		
		<button type="submit">Guardar</button>
	</form>
  
</body>
</html>