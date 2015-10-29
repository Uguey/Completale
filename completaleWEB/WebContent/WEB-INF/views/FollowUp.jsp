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
<link rel="stylesheet" href="css/FollowUpCSS.css">
<script src="js/FollowUpJS.js" defer="true"></script>
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
								        	<li 
								        	<c:if test="${FollowersOrFollowings eq 'followers'}"> class="active" </c:if>
								        	id="LiOne"><a id="AOne" href="#Followers">Followers</a></li>
								        	<li 
								        	<c:if test="${FollowersOrFollowings eq 'followings'}"> class="active" </c:if>
								        	id="LiTwo"><a id="ATwo" href="#Following">Following</a></li>
								        	<li id="LiThree">
									        	<form role="form" action="" id="searcherForm">
								        			<span class="glyphicon glyphicon-search" id="searchGlyphicon"></span>
								        			<input type="text" class="form-control" id="inputSearcher" size="100" value=""/> 
									        	</form>		
								        	</li>						        	
								      	</ul>
								    </div>
								</div>
							</div>
					  	</div>
					</nav>

					<div class="col-xs-12" id="Followers">
						<c:set var="i" value="${0}"></c:set>
							<c:forEach items="${Followers}" var="element">
									<div class="well well-xs adjustWell">
										<c:if test="${ifIFollowFollower[i] == true}">
											<button class="btn btn-md adjustButtonFollowing">Le sigues</button>
										</c:if>
										<c:if test="${ifIFollowFollower[i] == false}">
											<button class="btn btn-md adjustButtonFollowing" style="visibility: hidden">Le sigues</button>
										</c:if>
										<span class="title"><c:out value="${element}"></c:out> </span>
										<span class="likes">Likes: &nbsp
										<span class="badge" class="badgeLikes"><c:out value="${followerLikes[i]}"></c:out></span></span>		
										<span class="hyde">a</span>						
									</div>
								<c:set var="i" value="${i+1}"></c:set>
							</c:forEach>
					</div>		

					<div class="col-xs-12" id="Following">
						<c:forEach items="${Followings}" var="element">
							<c:set var="i" value="${0}"></c:set>
								<div class="well well-xs adjustWell">
									<span class="title"><c:out value="${element}"></c:out> </span>
									<span class="likesFollowing">Likes: &nbsp
									<span class="badge" class="badgeLikes"><c:out value="${followingLikes[i]}"></c:out></span></span>		
									<span class="hyde">a</span>						
								</div>
							<c:set var="i" value="${i+1}"></c:set>
						</c:forEach>
					</div>		

					<div class="col-xs-12" id="Searcher">
					</div>		

				</div>
			</div>
		</div>				
  	</div>
</body>
</html>