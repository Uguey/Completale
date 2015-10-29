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
<script src="js/MainJS.js" defer="true"></script>
<link rel="stylesheet" href="css/MainCSS.css">
<script defer="true">
	<c:if test="${not empty fail}">
		alert("${fail}");
	</c:if>
	<c:if test="${not empty failChangeData}">
		alert("${failChangeData}");
	</c:if>
	<c:if test="${not empty failCreateStory}">
		alert("${failCreateStory}");
	</c:if>
</script>
</head>
<body>	

	<h5 id="mainTitle"> CompleTale </h5>
 	<div class="jumbotron">	 
 		<a data-toggle="tooltip" title="Cerrar sesi�n" class="mousePointer" id="closeSession"><span class="glyphicon glyphicon-off drawGlyphicon" id="closeSesionGlyphicon"></span></a>
 		<a data-toggle="modal" data-target="#configuration" class="mousePointer" id="configurationButton"><span data-toggle="tooltip" title="Configuraci�n"><span class="glyphicon glyphicon-cog drawGlyphicon" id="configureGlyphicon"></span></span></a>
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
								        	<li class="active mainLi" id="LiOne"><a id="AOne" href="#Perfil"><c:out value="${username}"></c:out> &nbsp
								        	<span class="badge" id="badgeSum">0</span></a></li>
								        	<li class="mainLi" id="LiTwo"><a id="ATwo" href="#Timeline">Nuevo</a></li>
								        	<li class="mainLi" id="LiThree"><a id="AThree" href="#Crear">Crear</a></li> 
								      	</ul>
								    </div>
								</div>
							</div>
					  	</div>
					</nav>
					
					<!-- Profile -->
					<div class="col-xs-12" id="profile">
						<span class="glyphicon glyphicon-user user"></span>
						<div id="button1"><button type="button" class="btn btn-lg" id="button1Button">Likes: &nbsp<span class="badge" id="Likes">
						<c:out value="${Likes}"></c:out></span></button></div>
						<div id="adjustButtons23">
							<a href="/completaleWEB/RetrieveFollowersAndFollowings?active=followers"><button type="button" class="btn btn-primary btn-lg" id="button2">Followers: &nbsp<span class="badge" id="followers">
							<c:out value="${Followers}"></c:out></span></button></a>
							<a href="/completaleWEB/RetrieveFollowersAndFollowings?active=followings"><button type="button" class="btn btn-primary btn-lg" id="button3">Following: &nbsp<span class="badge" id="followedBy">
							<c:out value="${followedBy}"></c:out></span></button></a>
						</div>
						<div class="list-group col-xs-8 col-xs-offset-2 hola2">
							<div class="adjustList">
							    <a href="/completaleWEB/RetrieveFollowingRequests" class="list-group-item list-group-item-info">
						      		<h5 class="list-group-item-heading"><span class="adjustText">Peticiones de seguimiento recibidas: </span><span class="hiddenLetter">a</span>
						      		<span class="badge adjustBadge" id="followingRequests"><c:out value="${followingRequests}"></c:out></span></h5>
							    </a>
							    <a href="/completaleWEB/RetrieveStoriesParticipationRequests" class="list-group-item list-group-item-info">
						      		<h5 class="list-group-item-heading"><span class="adjustText">Peticiones de participaci�n en historias recibidas: </span><span class="hiddenLetter">a</span>
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
						
						<div id="storyForChallenge" class="modal fade" role="dialog">
							<div class="modal-dialog">
						    	<div class="modal-content">
						      		<div class="modal-header">
						        		<button type="button" class="close" data-dismiss="modal" id="storiesErase">&times;</button>
					        			<h2 class="modal-title" id="adjustTitleModal">Crear historia</h2>
						      		</div>
						      		<form class="form-horizontal" role="form" action="/completaleWEB/ParticipateInChallenge" id="formTimelineChallenge" autocomplete="off">
							      		<div class="modal-body">
							      			<div class="form-group">
										    	<label class="control-label col-xs-2" for="titleStory"><span class="adjustLabel">T�tulo:</span></label>
											    <div class="col-xs-10">
											      	<input type="text" class="form-control" id="titleStory" name="titleStory">
											    </div>
										  	</div>
										  	<div class="form-group">
										    	<label class="control-label col-xs-7" for="participantsStory">
										    		<span class="adjustLabel">Otros participantes en tu historia: </span>
										    	</label>
											    <div class="col-xs-5">
											      	<input list="participantsStory" class="form-control" id="inputParticipantsStory">
											      	<datalist id="participantsStory">
													</datalist> 
											    </div>
											</div>
											<div id="adjustElementsInside">
										    	<p id="divNames"></p>
										    	<button class="btn btn-sm btn-success" id="addUser">A�adir usuario</button>
										    </div>
										    <div id="addChallenge"></div>
							      		</div>
							      		<div class="modal-footer">
								        	<input type="submit" class="btn btn-primary" value="Crear historia" id="buttonModalStoryChallenge">
							      		</div>
						      		</form>
						    	</div>
						  	</div>
						</div>
						
					</div>
					
					<!-- Creation -->
					<div class="col-xs-12" id="create">
						<button class="btn btn-default btn-lg btn-block" id="adjustButton1" data-toggle="modal" data-target="#Story">
							<p id="adjustTitleCreate1">Crear historia</p>
						</button>
						<button class="btn btn-default btn-lg btn-block" data-toggle="modal" data-target="#Challenge">
							<p id="adjustTitleCreate2">Crear reto</p>
						</button>
					</div>

					<!--Story-->
					<div id="Story" class="modal fade" role="dialog">
						<div class="modal-dialog">
					    	<div class="modal-content">
					      		<div class="modal-header">
					        		<button type="button" class="close" data-dismiss="modal" id="storiesEraseTwo">&times;</button>
				        			<h2 class="modal-title" id="adjustTitleModal">Crear historia</h2>
					      		</div>
					      		<form class="form-horizontal" role="form" action="/completaleWEB/CreateStory" id="formStories" autocomplete="off">
						      		<div class="modal-body">
						      			<div class="form-group">
									    	<label class="control-label col-xs-2" for="titleStoryTwo"><span class="adjustLabel">T�tulo:</span></label>
										    <div class="col-xs-10">
										      	<input type="text" class="form-control" id="titleStoryTwo" name="titleStory">
										    </div>
									  	</div>
									  	<div class="form-group">
									    	<label class="control-label col-xs-7" for="participantsStoryTwo">
									    		<span class="adjustLabel">Otros participantes en tu historia: </span>
									    	</label>
										    <div class="col-xs-5">
										      	<input list="participantsStoryTwo" class="form-control" id="inputParticipantsStoryTwo">
										      	<datalist id="participantsStoryTwo">
												</datalist> 
										    </div>
										</div>
										<div id="adjustElementsInsideTwo">
									    	<p id="divNamesTwo"></p>
									    	<button class="btn btn-sm btn-success" id="addUserTwo">A�adir usuario</button>
									    </div>
						      		</div>
						      		<div class="modal-footer">
							        	<input type="submit" class="btn btn-primary" value="Crear historia" id="buttonModalStory">
						      		</div>
					      		</form>
					    	</div>
					  	</div>
					</div>

					<!--Challenge-->
					<div id="Challenge" class="modal fade" role="dialog">
						<div class="modal-dialog">
					    	<div class="modal-content">
					      		<div class="modal-header">
					        		<button type="button" class="close" data-dismiss="modal">&times;</button>
				        			<h2 class="modal-title" id="adjustTitleModal">Crear reto</h2>
					      		</div>
					      		<form class="form-horizontal" role="form" action="/completaleWEB/CreateChallenge" method="post">
						      		<div class="modal-body">
						      			<div class="form-group">
									    	<label class="control-label col-xs-2" for="titleChallenge"><span class="adjustLabel">T�tulo:</span></label>
										    <div class="col-xs-10">
										      	<input type="text" class="form-control" id="titleChallenge" name="titleChallenge">
										    </div>
									  	</div>
									  	<div class="form-group">
									    	<label class="control-label col-xs-2" for="descriptionChallenge"><span class="adjustLabel">Descripci�n:</span></label>
										    <div class="col-xs-9 col-xs-offset-1">
										      	<textarea class="form-control" rows="2" id="descriptionChallenge" name="descriptionChallenge"></textarea>
										    </div>
									  	</div>
								  		<div class="form-group">
									    	<label class="control-label col-xs-3" for="winnerMessageChallenge"><span class="adjustLabel" id="adjustWinnerMessage">Mensaje a los ganadores:</span></label>
										    <div class="col-xs-9">
										      	<textarea class="form-control" rows="2" id="winnerMessageChallenge" name="winnerMessageChallenge"></textarea>
										    </div>
									  	</div>
									  	<div class="form-group">
									    	<label class="control-label col-xs-9" for="maxStoriesChallenge"><span class="adjustLabel">M�ximo n�mero de historias en tu reto:</span></label>
										    <div class="col-xs-3">
										      	<select class="form-control" id="maxStoriesChallenge" name="maxStoriesChallenge">
												    <option value="1">1</option>
												    <option value="2">2</option>
												    <option value="3">3</option>
												    <option value="4">4</option>
												    <option value="5">5</option>
											  	</select>
										    </div>
									  	</div>
									  	<div class="form-group">
									    	<label class="control-label col-xs-9" for="endDateChallenge"><span class="adjustLabel">Duraci�n del reto:</span></label>
										    <div class="col-xs-3">
										      	<select class="form-control" id="endDateChallenge" name="endDateChallenge">
												    <option value="1">Un d�a</option>
												    <option value="2">Dos d�as</option>
												    <option value="3">Tres d�as</option>
												    <option value="4">Cuatro d�as</option>
												    <option value="5">Cinco d�as</option>
											  	</select>
										    </div>
									  	</div>
						      		</div>
						      		<div class="modal-footer">
							        	<input type="submit" class="btn btn-primary" value="Crear reto" id="buttonModalChallenge">
						      		</div>
					      		</form>
					    	</div>
					  	</div>
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
											    <span class="help-block formatText hyde" id="usernamePattern2">S�lo debe tener n�meros y letras</span>
											    <span class="help-block formatText hyde" id="usernameRequired2">Este campo es obligatorio</span>
											    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="usernameGlyphicon2"></span>
											    <span class="help-block formatText hyde">El nombre de usuario debe ser �nico</span>
									  		</div>
									  		<div class="form-group formDateDeAlta">
											    <input type="email" class="form-control" placeholder="Email" name="emailDarseDeAlta" id="email2" value="" required>
											    <span class="help-block formatText hyde" id="emailPattern2">La direcci�n de correo electr�nico no es correcta</span>
											    <span class="help-block formatText hyde" id="emailRequired2">Este campo es obligatorio</span>
											    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="emailGlyphicon2"></span>
									  		</div>
										  	<div class="form-group formDateDeAlta">
											    <input type="password" class="form-control" placeholder="Contrase�a" name="passwordDarseDeAlta" id="password21" pattern="((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{7,20})" value="" required>
											    <span class="help-block formatText" id="passwordPattern21">Debe tener como m�nimo 7 caracteres, un n�mero y una letra may�scula</span>
											    <span class="help-block formatText hyde" id="passwordRequired21">Este campo es obligatorio</span>
											    <span class="glyphicon glyphicon-remove form-control-feedback hyde" id="passwordGlyphicon21"></span>
										  	</div>
										  	<div class="form-group formDateDeAlta">
											    <input type="password" class="form-control" placeholder="Repita Contrase�a" name="passwordRepeatedDarseDeAlta" id="password22" value="" required>
											    <span class="help-block formatText hyde" id="passwordRequired22">Este campo es obligatorio</span>
											    <span class="help-block formatText hyde" id="passwordCoincidence22">Las contrase�as no coinciden</span>
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
										<p id="adjustP"> �Seguro que te quieres ir? �Muchas gracias por haber estado con nosotros! �Te esperaremos y estaremos aqu� cuando quieras! �Un saludo!</p>
									</div>
									<div class="panel-footer tab-content">
										<form name="eraseData" method="POST" action="/completaleWEB/Unsuscribe" role="form">
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