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
<link rel="stylesheet" href="css/SeeStoryCSS.css">
<script src="js/SeeStoryJS.js" defer="true"></script>
</head>
<body>	
	<h5 id="mainTitle"> Completale </h5>
 	<div class="jumbotron" id="mainJumbotron">	 

 		<a href="/completaleWEB/DataCharger"><span data-toggle="tooltip" title="Volver Atrás"><span class="glyphicon glyphicon-arrow-left" id="backGlyphicon"></span></span></a>

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
								        	<li class="active" id="LiOne"><a id="AOne" href="#Story"><c:out value="${title }"></c:out></a></li>
								      	</ul>
								    </div>
								</div>
							</div>
					  	</div>
					</nav>					
					<div class="jumbotron" id="secondJumbotron">
						<a id="like"><span class="glyphicon glyphicon-heart-empty like"></span>
							<span class="glyphicon glyphicon-heart like hiddenElement"></span></a>
						<p><c:out value="${story }"></c:out></p>													
					</div>
				</div>
			</div>
		</div>				
  	</div>
</body>
</html>