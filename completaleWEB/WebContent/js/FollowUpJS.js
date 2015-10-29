var actuallySearchDone = false;

$(document).ready(function(){

	resize();
	$(window).resize(function(){
		resize();
	});

	if ($("#LiOne").hasClass("active")==true) {
		$("#Following").css("display", "none");
	}
	else if ($("#LiTwo").hasClass("active")==true) {
		$("#Followers").css("display", "none");
	}
	else console.log("Algo ha ido mal con las clases activas");

	$("#AOne").click(function(){activeClass(0)});
	$("#ATwo").click(function(){activeClass(1)});

	$("#inputSearcher").on({
		focus : arrangeSearch,
		keyup : actuallySearch
	});

	$("#inputSearcher").blur(function(){
		var search = $("#inputSearcher").val();
		if (search == ""){
			$("#searchGlyphicon").css("opacity","0.5");
		};
	});	

	$("#searcherForm").submit(function (evt) {
	    evt.preventDefault();
	});

	window.setInterval(addListenerToFollowButton, 100);
	
});

	function resize(){
		var screenWidthMargin = screen.width/10;
		var screenWidth = screen.width*0.8;
		var screenWidthMarginString = screenWidthMargin.toString() + "px";
		var screenWidthString = screenWidth.toString() + "px";
		$(".jumbotron").css("margin-right",screenWidthMarginString);
		$(".jumbotron").css("margin-left",screenWidthMarginString);
		$(".jumbotron").css("width",screenWidthString);
	}


	var activeClass = function(x){
		switch (x){
			case 0:
				$("#LiOne").addClass("active");
				$("#LiTwo").removeClass("active");
				$("#Following").slideUp(400);
				$("#Searcher").slideUp(400);
				$("#Followers").slideDown(400);
				break;
			case 1:
				$("#LiOne").removeClass("active");
				$("#LiTwo").addClass("active");
				$("#Followers").slideUp(400);
				$("#Searcher").slideUp(400);
				$("#Following").slideDown(400);
				break;
			case 2:
				$("#LiOne").removeClass("active");
				$("#LiTwo").removeClass("active");
				$("#Followers").slideUp(400);
				$("#Following").slideUp(400);
				$("#Searcher").slideDown(400);
				break;
			default:
				console.log("Algo ha ido mal");
		}
	};

	function arrangeSearch(){
		activeClass(2);
		$("#searchGlyphicon").css("opacity","0.07");
	}

	function actuallySearch (){
		var search = $("#inputSearcher").val();
		$.get("/completaleWEB/RetrieveSearch?search="+search, function(data, status){
			if (status=="success"){
				var newData = JSON.parse(data);
				var searches = "";
				for (var i=0;i<parseInt(newData.number);i++){
					searches += 
					"<div class=\"well well-xs adjustWell\">" +
					 	"<button class=\"btn btn-primary btn-md adjustButtonFollowingRequest clickButtonFollowingRequest\">&iquest;Seguir?</button>";
					var following = newData["following"+i.toString()];
					if (following == "false") {
						searches +=	
						"<button class=\"btn btn-md adjustButtonFollowingSearcher\" style=\"visibility:hidden;\">Le sigues</button>"	
					}
					else if (following == "true") {
						searches +=	
						"<button class=\"btn btn-md adjustButtonFollowingSearcher\">Le sigues</button>"
					}
					else console.log("Error al leer el valor de following");
					searches += 	
						"<span class=\"title\">" + newData["username"+i.toString()] + " </span><span class=\"likesSearcher\">Likes: &nbsp" +
					 	"<span class=\"badge\" class=\"badgeLikes\">" + newData["likes"+i.toString()] + "</span></span>" +
					 	"<span class=\"hyde\">a</span>" +														
					"</div>";
				}
				$("#Searcher").html(searches);
				if(searches!="") actuallySearchDone = true;
			}
			else console.log("Error al obtener los datos de la búsqueda mediante AJAX");
		});
	}

	function addListenerToFollowButton(){
		if (actuallySearchDone == true) {
			var possibleButtons = document.getElementsByClassName("clickButtonFollowingRequest");

			for (i=0;i<possibleButtons.length;i++){
				possibleButtons[i].addEventListener("click", follow);
			}
			actuallySearchDone = false;
		}		
	}	

	function follow(){
		var possibleFollow = "";
		var maybeButton = this.nextSibling.nextSibling;
		if(maybeButton.nodeName=="BUTTON"){
			var span = maybeButton.nextSibling.nextSibling;
			possibleFollow = span.firstChild.nodeValue;
		}
		else if (maybeButton.nodeName=="SPAN"){
			possibleFollow = maybeButton.firstChild.nodeValue;
		};
		createFollowRequest(this, possibleFollow);
	};

	function createFollowRequest(button, possibleFollow){
		$.post("/completaleWEB/CreateFollowRequest",
			{
				nickPossibleFollow: possibleFollow
			},
			function (data, status){
				if(status="success"){
					button.disabled=true;
					button.style.cursor="default";
				}
				else console.log("Error al insertar una petición de seguimiento mediante AJAX");
		});
	}
	
