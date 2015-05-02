<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://textillevel.com/tags" prefix="tl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<title>Insert title here</title>
</head>
<body>
	INDEX.JSP
	<br/>
	TAG:
	<br>
	<tl:surround sufijo="sufijo" prefijo="prefijo"/>
	<button onclick="ponerTextoJSON();">JSON</button>
	<br>
	<div id="texto"></div>
	<script type="text/javascript">
		function ponerTextoJSON(){
			var param = 4;
			$.ajax({
			  url: "../actions/personasJSON?parametro="+param,
			  beforeSend: function( xhr ){
			  	return true; //validacion, si retorna false no se envia
			  }
			}).done(function(data) {
				$('#texto').text(JSON.stringify(data));
			});
		}
	</script>
</body>
</html>