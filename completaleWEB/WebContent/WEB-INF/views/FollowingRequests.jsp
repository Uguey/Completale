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
<link rel="stylesheet" href="css/FollowingRequestsCSS.css">
<script src="js/FollowingRequestsJS.js" defer="true"></script>
</head>
<body>	
	<h5 id="mainTitle"> Completale </h5>
 	<div class="jumbotron">	 

 		<a href="/completaleWEB/"><span data-toggle="tooltip" title="Volver Atrás"><span class="glyphicon glyphicon-arrow-left" id="backGlyphicon"></span></span></a>

 		<div class="container">
		  	<div class="row">
			    <div class="col-xs-12">	
					<nav class="navbar navbar-inverse" id="adjustNavBar">
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
								        	<li class="active" id="LiOne"><a id="AOne" href="#FollowersRequests">Peticiones de seguimiento recibidas</a></li>
								      	</ul>
								    </div>
								</div>
							</div>
					  	</div>
					</nav>
					
					<div class="col-xs-12" id="FollowersRequests">
					<c:set var="i" value="${0}"></c:set>
					<c:forEach items="${possibleFollowersName}" var="elements">					
						<div class="well well-xs adjustWell">
							<button class="btn btn-primary btn-md adjustButtonFollowingRequest clickButtonAcceptFollowingRequest">&iquest;Aceptar?</button>
							<span class="title"><c:out value="${elements}"></c:out></span>
							<span class="likes">Likes: &nbsp
							<span class="badge" class="badgeLikes"><c:out value="${possibleFollowersLikes[i]}"></c:out></span>
							</span>
							<span class="hyde">a</span>														
						</div>
						<c:set var="i" value="${i+1}"></c:set>
					</c:forEach>
					</div>		

				</div>
			</div>
		</div>				
  	</div>
</body>
</html>
