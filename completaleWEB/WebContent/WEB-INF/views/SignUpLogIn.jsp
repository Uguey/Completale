<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bienvenido!</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- JQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<!-- Owner -->
<script src="js/SignUpLogInJS.js" defer="true"></script>
<link rel="stylesheet" href="css/SignUpLogInCSS.css">
<script defer="true">
	<c:if test="${not empty fail}">
		alert("${fail}");
	</c:if>
</script>
</head>
<body>
	<div class="container">
		<div id="adjustTitle">
			<h1> CompleTale </h1>
		</div>
		<div class="row">
			<div class="col-xs-6 col-xs-offset-6">
				<div id="form1">
					<div class="panel">
						<div class="panel-heading">
							<div class="container-fluid">
								<ul class="nav nav-tabs">
							  		<li class="active"><a data-toggle="tab" href="#IniciaSesion" id="IniciaSesionA"><b class="bigger">Inicia Sesión</b></a></li>
								  	<li><a data-toggle="tab" href="#DateDeAlta" id="DateDeAltaA"><b class="bigger">Date de alta</b></a></li>
								</ul>
							</div>
						</div>
						<div class="panel-body tab-content">
							<form name="LogIn" method="POST" action="/completaleWEB/DataCharger" role="form">
								<div class="tab-pane fade in active" id="IniciaSesion">
									<div class="form-group formInicioSesion">
									    <input type="text" class="form-control" id="username1" placeholder="Nombre de usuario" name="usernameInicioSesion" pattern="[a-zA-Z0-9]*" value="" required>
									    <span class="help-block formatText hyde" id="usernamePattern1">Sólo debe tener números y letras</span>
									    <span class="help-block formatText hyde" id="usernameRequired1">Este campo es obligatorio</span>
									    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="usernameGlyphicon1"></span>
							  		</div>
								  	<div class="form-group formInicioSesion">
									    <input type="password" class="form-control" id="password1" placeholder="Contraseña" name="passwordInicioSesion" pattern="((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{7,20})" value="" required>
									    <span class="help-block formatText hyde" id="passwordPattern1">Debe tener como entre 7 y 20 caracteres, un número, una letra mayúscula y una minúscula</span>
									    <span class="help-block formatText hyde" id="passwordRequired1">Este campo es obligatorio</span>
									    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="passwordGlyphicon1"></span>
								  	</div>
								  	<div class="checkbox">
								    	<label><input type="checkbox" name="rememberMe">Recuérdame</label>
								  	</div>
								  	<input type="hidden" name="LogIn" value="LogIn">
								  	<input type="submit" class="btn btn-default adjustSubmit" name="buttonSubmit1" value="Acceder" id="buttonSubmit1">
								</div>
							</form>
							<form name="SignUp" method="POST" action="/completaleWEB/DataCharger" role="form">
								<div class="tab-pane fade" id="DateDeAlta">
									<div class="form-group formDateDeAlta">
									    <input type="text" class="form-control" placeholder="Nombre de usuario" id="username2"  name="usernameDarseDeAlta" pattern="[a-zA-Z0-9]*" value="" required>
									    <span class="help-block formatText hyde" id="usernamePattern2">Sólo debe tener números y letras</span>
									    <span class="help-block formatText hyde" id="usernameRequired2">Este campo es obligatorio</span>
									    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="usernameGlyphicon2"></span>
							  		</div>
							  		<div class="form-group formDateDeAlta">
									    <input type="email" class="form-control" placeholder="Email" name="emailDarseDeAlta" id="email2" value="" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,3}$" required>
									    <span class="help-block formatText hyde" id="emailPattern2">La dirección de correo electrónico no es correcta</span>
									    <span class="help-block formatText hyde" id="emailRequired2">Este campo es obligatorio</span>
									    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="emailGlyphicon2"></span>
							  		</div>
								  	<div class="form-group formDateDeAlta">
									    <input type="password" class="form-control" placeholder="Contraseña" name="passwordDarseDeAlta" id="password21" pattern="((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{7,20})" value="" required>
									    <span class="help-block formatText" id="passwordPattern21">Debe tener como entre 7 y 20 caracteres, un número, una letra mayúscula y una minúscula</span>
									    <span class="help-block formatText hyde" id="passwordRequired21">Este campo es obligatorio</span>
									    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="passwordGlyphicon21"></span>
								  	</div>
								  	<div class="form-group formDateDeAlta">
									    <input type="password" class="form-control" placeholder="Repita Contraseña" name="passwordRepeatedDarseDeAlta" id="password22" value="" required>
									    <span class="help-block formatText hyde" id="passwordRequired22">Este campo es obligatorio</span>
									    <span class="help-block formatText hyde" id="passwordCoincidence22">Las contraseñas no coinciden</span>
									    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="passwordGlyphicon22"></span>
								  	</div>
								  	<div class="checkbox">
								    	<label><input type="checkbox" name="rememberMe">Recuérdame</label>
								  	</div>
								  	<input type="hidden" name="SignUp" value="SignUp">
								  	<input type="submit" class="btn btn-default adjustSubmit" name="buttonSubmit2" value="Acceder" id="buttonSubmit2">
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
  	</div>
</body>
</html>