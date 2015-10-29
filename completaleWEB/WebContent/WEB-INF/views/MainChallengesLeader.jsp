<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Completale</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- JQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<!-- Owner -->
<link rel="stylesheet" href="css/MainChallengesLeaderCSS.css">
<script src="js/MainChallengesLeaderJS.js" defer="true"></script>
</head>
<body>	
	<h5 id="mainTitle"> CompleTale </h5>
 	<div class="jumbotron" id="mainJumbotron">	 
 		<a data-toggle="tooltip" title="Cerrar sesión" class="mousePointer" id="closeSession"><span class="glyphicon glyphicon-off drawGlyphicon" id="closeSesionGlyphicon"></span></a>
 		<a data-toggle="modal" data-target="#configuration" class="mousePointer" id="configurationButton"><span data-toggle="tooltip" title="Configuración"><span class="glyphicon glyphicon-cog drawGlyphicon" id="configureGlyphicon"></span></span></a>
 		<div class="container">
		  	<div class="row">
			    <div class="col-xs-12">	
					<nav class="navbar navbar-inverse adjustNavBar">
					  	<div class="container">
					  		<div class="row">
							    <div class="col-xs-12">
							  		<div class="navbar-header">
								      	<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">
									        <span class="icon-bar"></span>
									        <span class="icon-bar"></span>
									        <span class="icon-bar"></span>  
							        	</button>     
								    </div>
					    			<div class="collapse navbar-collapse" id="navbar">
								      	<ul class="nav navbar-nav inline">
								        	<li class="mainLi" id="LiOne"><a id="AOne" href="#Perfil"><c:out value="${username}"></c:out> &nbsp
								        	<span class="badge" id="badgeSum">0</span></a></li>
								        	<li class="mainLi" id="LiTwo"><a id="ATwo" href="#Timeline">Nuevo</a></li>
								        	<li class="active mainLi" id="LiThree"><a id="AThree" href="#Crear">Crear</a></li> 
								      	</ul>
								    </div>
								</div>
							</div>
					  	</div>
					</nav>

					<!-- Profile -->
					<div class="col-xs-12" id="profile">
						<span class="glyphicon glyphicon-user user"></span>
						<div id="button1"><button type="button" class="btn btn-lg" id="button1Button">Likes: &nbsp
						<span class="badge" id="Likes"><c:out value="${Likes}"></c:out></span></button></div>
						<div id="adjustButtons23">
							<a href="/completaleWEB/RetrieveFollowersAndFollowings?active=followers"><button type="button" class="btn btn-primary btn-lg" id="button2">Followers: &nbsp
							<span class="badge" id="followers"><c:out value="${Followers}"></c:out></span></button></a>
							<a href="/completaleWEB/RetrieveFollowersAndFollowings?active=followings"><button type="button" class="btn btn-primary btn-lg" id="button3">Following: &nbsp
							<span class="badge" id="followedBy"><c:out value="${followedBy}"></c:out></span></button></a>
						</div>
						<div class="list-group col-xs-8 col-xs-offset-2 hola2">
							<div class="adjustList">
							    <a href="/completaleWEB/RetrieveFollowingRequests" class="list-group-item list-group-item-info">
						      		<h5 class="list-group-item-heading"><span class="adjustText">Peticiones de seguimiento recibidas: </span><span class="hiddenLetter">a</span>
						      		<span class="badge adjustBadge" id="followingRequests"><c:out value="${followingRequests}"></c:out></span></h5>
							    </a>
							    <a href="/completaleWEB/RetrieveStoriesParticipationRequests" class="list-group-item list-group-item-info">
						      		<h5 class="list-group-item-heading"><span class="adjustText">Peticiones de participación en historias recibidas: </span><span class="hiddenLetter">a</span>
						      		<span class="badge adjustBadge" id="storyParticipationRequests"><c:out value="${storyParticipationRequests}"></c:out></span></h5>
							    </a>
							</div>
					  	</div>
					</div>

					<!-- Timeline -->
					<div id="timeline" class="col-xs-12">
						<nav class="navbar navbar-inverse adjustNavBar">
						  	<div class="container">
						  		<div class="row">
								    <div class="col-xs-12">
								  		<div class="navbar-header">
									      	<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar">
										        <span class="icon-bar"></span>
										        <span class="icon-bar"></span>
										        <span class="icon-bar"></span>  
								        	</button>     
									    </div>
						    			<div class="collapse navbar-collapse" id="navbar">
									      	<ul class="nav navbar-nav inline">
									        	<li class="active secondLi" id="LiTimelineOne">
									        		<a id="ATimelineOne" href="#StoriesTimeline">Historias &nbsp<span class="badge" id="badgeSum">0</span></a>
									        	</li>
									        	<li class="secondLi" id="LiTimelineTwo">
									        		<a id="ATimelineTwo" href="#ChallengesTimeline">Retos</a>
									        	</li>
									      	</ul>
									    </div>
									</div>
								</div>
						  	</div>
						</nav>

						<div id="StoriesTimeline">
						</div>

						<div id="ChallengesTimeline">
						</div>
						
					</div>

					<!-- Creation of challenges -->
					<div class="container" id="create">
					  <div class="jumbotron" id="secondJumbotron">
					    <h1 id="challengeTitle"><c:out value="${title}"></c:out></h1> 
					    <c:set var="i" value="${0}"></c:set>
					    <c:forEach items="${titleStories}" var="element">
				    	<div class="alert alert-success adjustTimeline" id="storiesTimeline">
							<div class="sameRow">
								<a href="/completaleWEB/SeeStory?access=leader&title=<c:out value="${element}"></c:out>" class="titleHref">									
								<h4 class="titleTimeline"><c:out value="${element}"></c:out></h4></a>
							</div>
							<p> Participantes: <c:out value="${participantStories[i]}"></c:out>
							<span class="likes">Likes: 
							<span class="badge"><c:out value="${likes[i]}"></c:out></span></span>
							<span class="state"><c:out value="${state[i]}"></c:out></span></p>
						</div>
						<c:set var="i" value="${i+1}"></c:set>
						</c:forEach>
					</div>

					<!-- Configuration -->
					<div id="configuration" class="modal fade" role="dialog">
						<div class="modal-dialog">
					    	<div class="modal-content">
					      		<div class="modal-header">
					        		<button type="button" class="close" data-dismiss="modal" id="closeChangeAndErase">&times;</button>
				        			<ul class="nav nav-tabs">
								  		<li class="active col-xs-5"><a data-toggle="tab" href="#changeData" id="changeDataA"><b>Cambiar datos</b></a></li>
									  	<li class="col-xs-5"><a data-toggle="tab" href="#closeAccount" id="closeAccountA"><b>Eliminar cuenta</b></a></li>
									</ul>
					      		</div>
					      		<div class="tab-pane fade in active" id="changeData">
						      		<div class="panel-body tab-content">
										<form name="changeData" method="POST" action="/completaleWEB/ChangeDataUser" role="form" id="formChangeData">
											<div class="form-group formDateDeAlta">
											    <input type="text" class="form-control" placeholder="Nombre de usuario" id="username2"  name="usernameDarseDeAlta" pattern="[a-zA-Z0-9]*" value="" required>
											    <span class="help-block formatText hyde" id="usernamePattern2">Sólo debe tener números y letras</span>
											    <span class="help-block formatText hyde" id="usernameRequired2">Este campo es obligatorio</span>
											    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="usernameGlyphicon2"></span>
											    <span class="help-block formatText hyde">El nombre de usuario debe ser único</span>
									  		</div>
									  		<div class="form-group formDateDeAlta">
											    <input type="email" class="form-control" placeholder="Email" name="emailDarseDeAlta" id="email2" value="" required>
											    <span class="help-block formatText hyde" id="emailPattern2">La dirección de correo electrónico no es correcta</span>
											    <span class="help-block formatText hyde" id="emailRequired2">Este campo es obligatorio</span>
											    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="emailGlyphicon2"></span>
									  		</div>
										  	<div class="form-group formDateDeAlta">
											    <input type="password" class="form-control" placeholder="Contraseña" name="passwordDarseDeAlta" id="password21" pattern="((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{7,20})" value="" required>
											    <span class="help-block formatText" id="passwordPattern21">Debe tener como mínimo 7 caracteres, un número y una letra mayúscula</span>
											    <span class="help-block formatText hyde" id="passwordRequired21">Este campo es obligatorio</span>
											    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="passwordGlyphicon21"></span>
										  	</div>
										  	<div class="form-group formDateDeAlta">
											    <input type="password" class="form-control" placeholder="Repita Contraseña" name="passwordRepeatedDarseDeAlta" id="password22" value="" required>
											    <span class="help-block formatText hyde" id="passwordRequired22">Este campo es obligatorio</span>
											    <span class="help-block formatText hyde" id="passwordCoincidence22">Las contraseñas no coinciden</span>
											    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="passwordGlyphicon22"></span>
										  	</div>
									</div>
									<div class="panel-footer tab-content">
									  	<input type="submit" class="btn btn-default buttonChangeAndErase" id="buttonChangeData" name="submitChangeData" value="Aceptar"></input>
								  	</div>
								  		</form>
							  	</div>
								<div class="tab-pane fade in active" id="closeAccount">
									<div class="panel-body tab-content">
										<p id="adjustP"> ¿Seguro que te quieres ir? ¡Muchas gracias por haber estado con nosotros! ¡Te esperaremos y estaremos aquí cuando quieras! ¡Un saludo!</p>
									</div>
									<div class="panel-footer tab-content">
										<form name="eraseData" method="POST" action="" role="form">
									  		<input type="submit" class="btn btn-default buttonChangeAndErase" id="buttonUnsuscribe" value="Borrar datos"></input>
									  	</form>
								  	</div>
								</div>
					    	</div>
					  	</div>
					</div>
					
				</div>
			</div>
		</div>				
  	</div>
</body>
</html>