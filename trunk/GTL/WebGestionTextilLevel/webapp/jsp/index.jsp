<%@page import="ar.com.textillevel.entidades.portal.Modulo"%>
<%@page import="ar.com.textillevel.entidades.portal.UsuarioSistema"%>
<%@page import="ar.com.textillevel.web.util.SessionConstants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s" %>  
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>

<!DOCTYPE html>
<html>
<head>
	<title>GTL - Gestion</title>
	<meta http-equiv="Content-Type" content="text/html;">
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
	<script src="../../js/general.js"></script>
	<sj:head jqueryui="true" jquerytheme="redmond"/>
</head>
<body>
	<nav class="navbar navbar-default" style="background-color: white;">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<ul class="nav navbar-nav navbar-right">
					<li><a><span class="glyphicon glyphicon-user"></span> <s:property value="#session.usuarioSistema.usrName"/></a></li>
					<li><a href="<s:url action="../../struts/actions/logout"/>"><span class="glyphicon glyphicon-log-in"></span> Salir</a></li>
				</ul>
				<a class="navbar-brand">
					<img id="imgLogin" class="img-rounded" src="../../img/logogtl.jpg" width="50">
					Textil Level S.A
				</a>
			</div>
			<div class="collapse navbar-collapse" id="myNavbar">
				<ul class="nav navbar-nav">
					<s:iterator value="#session.modulos">
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#">
								<s:property value="key" /><span class="caret"></span>
							</a>
							<ul class="dropdown-menu">
								<s:iterator value="value">
									<li><a href="#"><s:property value="nombre" /></a></li>
								</s:iterator>
							</ul></li>
					</s:iterator>
				</ul>
			</div>
		</div>
	</nav>
	<div id="content" class="container">
		<sj:a onclick="getJSON();" button="true">Click para Spring + JSON ===> </sj:a>
		<span id="json_result"></span><br> 
		<select name="theme" id="selected_theme" onchange="changeTheme(this.value);">
			<option value="hot-sneaks">The hot-sneaks Theme</option>
			<option value="redmond">The redmond Theme</option>
			<option value="humanity">The humanity Theme</option>
			<option value="swanky-purse">The swanky-purse Theme</option>
			<option value="blitzer">The blitzer Theme</option>
			<option value="eggplant">The eggplant Theme</option>
			<option value="smoothness">The smoothness Theme</option>
			<option value="excite-bike">The excite-bike Theme</option>
			<option value="pepper-grinder">The pepper-grinder Theme</option>
			<option value="sunny">The sunny Theme</option>
			<option value="ui-lightness">The lightness Theme</option>
			<option value="cupertino">The cupertino Theme</option>
			<option value="flick">The flick Theme</option>
			<option value="mint-choc">The mint-choc Theme</option>
			<option value="trontastic">The trontastic Theme</option>
			<option value="showcase" selected="selected">The custom Showcase Theme</option>
			<option value="overcast">The overcast Theme</option>
			<option value="dark-hive">The dark-hive Theme</option>
			<option value="vader">The vader Theme</option>
			<option value="black-tie">The black-tie Theme</option>
			<option value="start">The start Theme</option>
			<option value="south-street">The south-street Theme</option>
			<option value="dot-luv">The dot-luv Theme</option>
			<option value="le-frog">The le-frog Theme</option>
			<option value="ui-darkness">The darkness Theme</option>
		</select>
		<sj:datepicker id="date0" label="Select a Date" />
		<sj:spinner name="spinner2" id="spinner2" min="5" max="50" step="2" value="25" />
		<sj:dialog id="myclickdialog" autoOpen="false" modal="true" title="Modal Dialog">
			HOLA NACHO
	    </sj:dialog>
		<sj:a openDialog="myclickdialog" button="true" buttonIcon="ui-icon-newwin">
	    	Open modal dialog
	    </sj:a>
		<div style="overflow-y: scroll;height: 500px;margin-bottom: 20px;margin-top: 20px;">
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th>Nombre</th>
					</tr>
				</thead>
				<tbody style="overflow-y: scroll;height: 200px;">
					<s:iterator value="#session.modulos">
						<s:iterator value="value">
							<tr>
								<td><s:property value="nombre" /></td>
							</tr>
						</s:iterator>
					</s:iterator>
				</tbody>
			</table>
		</div>
	</div>
	<div id="footer" class="containter">
		<div class="col-sm-12" style="padding-bottom:0px;padding-top:20px;text-align:center;background-color: red;">
			<form id="frmLogout" action="../../struts/actions/logout" style="margin-bottom: 20px;">
				<a onclick="$('#frmLogout').submit();" style="color: white;font-weight: bold;cursor: pointer;">Salir</a>
				<a href="<s:url action="../../struts/actions/logout"/>" style="color: white;font-weight: bold;">LOGOUT HREF!</a>
			</form>
		</div>
	</div>
	
</body>
</html>