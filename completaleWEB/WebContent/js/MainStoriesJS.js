$(document).ready(function(){

	$("#closeSession").click(function(){
		var r = confirm("\u00BFEst\u00e1s seguro de que deseas cerrar la sesi\u00f3n?");
		if (r == true){
			window.location.assign("http://localhost:8080/completaleWEB/CloseSession");
		}
	});

	resize();
	if(screen.width==1920)$(".user").css("font-size","23em");
	else resizeUser();	
	$(window).resize(function(){
		resize();
		resizeUser();	
	});

	$("#AOne").click(function(){activeClass(0)});
	$("#ATwo").click(function(){activeClass(1)});
	$("#AThree").click(function(){activeClass(2)});

	$("#ATimelineOne").click(function(){activeSecondaryClass(0)});
	$("#ATimelineTwo").click(function(){activeSecondaryClass(1)});

	$("#changeDataA").click(function(){
		$("#closeAccountA").css("color","#404040");
		$("#changeDataA").css("color","grey");
		$("#closeAccount").hide(400, function(){
			$("#changeData").show(400);
		});
	});

	$("#closeAccountA").click(function(){
		$("#changeDataA").css("color","#404040");
		$("#closeAccountA").css("color","grey");
		$("#changeData").hide(400, function(){
			$("#closeAccount").show(400);
		});
	});

	$("input").on("invalid", function(){
  		return false;
	});	

	$("#formChangeData").submit(function (evt) {
	    evt.preventDefault();
	});
	
	$("#buttonChangeData").click(function(){
		usernameValidated = validation(document.getElementById("username2"), "DateDeAlta", "username", 2);
		emailValidated = validation(document.getElementById("email2"), "DateDeAlta", "email", 2);
		password1Validated = validationPassword21(document.getElementById("password21"));
		password2Validated = validationPassword22(document.getElementById("password21"), document.getElementById("password22"));
		if ((usernameValidated==true)&&(emailValidated==true)
			&&(password1Validated==true)&&(password2Validated==true)) document.getElementById("formChangeData").submit();
	});

	$("#closeChangeAndErase").click(function(){
		restore("DateDeAlta", "username", 2);
		restore("DateDeAlta", "email", 2);
		restorePassword21();
		restorePassword22();
		clearForm();
	});

	$("#configurationButton").click(function(){
		$.get("/completaleWEB/RetrieveDataUser?t="+Math.random(), function(data,status){
			if(status=="success"){
				var newData=JSON.parse(data);
				var inputs = $("#formChangeData").find("input");
				inputs[0].value=newData.username;
				inputs[1].value=newData.email;
			}
			else console.log("Error al obtener los datos del usuario mediante AJAX");
		});
	});

	var firstTime = true;
	var participants = [];
	var i = 0;
	$("#addUser").click(function(event){
		event.preventDefault();
		var value = $("#inputParticipantsStory").val();
		participants[i] = value.toString();
		if (value!="") {
			$("#divNames").html(function(i,origText){
			if (firstTime==true){
				firstTime=false;
				return value;
			}
			else return origText + ", " + value;
			});
		}
		i++;		
	});

	$("#formStories").submit(function(){
		for (var i=0; i<participants.length;i++){
			$("#adjustElementsInside").after("<input type=\"hidden\" name=" + i + "value=" + participants[i] +"\"");
		}	
		$("#adjustElementsInside").after("<input type=\"hidden\" name=" + numberOfParticipants + "value=" + i +"\"");	
	});

	$("#buttonSendStory").click(function(){
		var fragment = $("#adjustTextArea").val();
		var title = $("#adjustTitleStoryProperTitle").text();
		addNewFragment(title, fragment);
	});

	window.setInterval(updateData, 1000);

	var updatingStory = window.setInterval(function(){updateStory($("#adjustTitleStoryProperTitle").text())}, 1000);

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

	function resizeUser(){
		var fontSize = screen.width*0.01197916;
		$(".user").css("font-size", fontSize.toString()+"em");
	}

	var activeClass = function(x){
		switch (x){
			case 0:
				$("#LiOne").addClass("active");
				$("#LiTwo").removeClass("active");
				$("#LiThree").removeClass("active");
				$("#timeline").slideUp(400);
				$("#create").slideUp(400);
				$("#profile").slideDown(400);
				break;
			case 1:
				$("#LiOne").removeClass("active");
				$("#LiTwo").addClass("active");
				$("#LiThree").removeClass("active");
				$("#profile").slideUp(400);
				$("#create").slideUp(400);
				$("#timeline").slideDown(400);
				getTimeline();
				break;
			case 2:
				$("#LiOne").removeClass("active");
				$("#LiTwo").removeClass("active");
				$("#LiThree").addClass("active");
				$("#profile").slideUp(400);
				$("#timeline").slideUp(400);
				$("#create").slideDown(400);
				break;
			default:
				console.log("Algo ha ido mal");
		}
	};	

	var activeSecondaryClass = function(x){
		switch (x){
			case 0:
				$("#LiTimelineOne").addClass("active");
				$("#LiTimelineTwo").removeClass("active");
				$("#ChallengesTimeline").slideUp(400);
				$("#StoriesTimeline").slideDown(400);
				break;
			case 1:
				$("#LiTimelineOne").removeClass("active");
				$("#LiTimelineTwo").addClass("active");
				$("#StoriesTimeline").slideUp(400);
				$("#ChallengesTimeline").slideDown(400);
				break;
			default:
				console.log("Algo ha ido mal");
		}
	}

	function validation(input, typeForm, type, number){
		restore(typeForm, type, number);
		if (input.checkValidity() == false){
			$(".form" + typeForm).addClass("has-error");
			$(".form" + typeForm).addClass("has-feedback");
			$("#" + type + "Glyphicon" + number).removeClass("hyde");
			if (((input.validity.patternMismatch)||(input.validity.typeMismatch))
				||((input.validity.patternMismatch)&&(input.validity.typeMismatch))){
				$("#" + type + "Pattern" + number).removeClass("hyde");				
				$("#" + type + "Pattern" + number).css("color","rgb(255,0,0)");
			}
			else if (input.value == null || input.value == ""){
				$("#" + type + "Required" + number).removeClass("hyde");
				$("#" + type + "Required" + number).css("color","rgb(255,0,0)");	
			};
			return false;
		}else return true;
	}

	function validationPassword21(input){
		restorePassword21();
		if (input.checkValidity() == false){
			$("#passwordPattern21").addClass("hyde");
			$(".formDateDeAlta").addClass("has-error");
			$(".formDateDeAlta").addClass("has-feedback");
			$("#passwordGlyphicon21").removeClass("hyde");
			if (input.validity.patternMismatch){
				$("#passwordPattern21").removeClass("hyde");				
				$("#passwordPattern21").css("color","rgb(255,0,0)");
			}
			else if (input.value == null || input.value == ""){
				$("#passwordRequired21").removeClass("hyde");
				$("#passwordRequired21").css("color","rgb(255,0,0)");	
			};
			return false;
		}else return true;
	}

	function validationPassword22(input1, input2){
		restorePassword22();
		if (((input2.value == null || input2.value == "")||(input1.value != input2.value))
			||((input2.value == null || input2.value == "")&&(input1.value != input2.value))) {
			$(".formDateDeAlta").addClass("has-error");
			$(".formDateDeAlta").addClass("has-feedback");
			$("#passwordGlyphicon22").removeClass("hyde");
			if (input2.value == null || input2.value == "") {
				$("#passwordRequired22").removeClass("hyde");
				$("#passwordRequired22").css("color","rgb(255,0,0)");
			}
			else if(input1.value != input2.value){
				$("#passwordCoincidence22").removeClass("hyde");
				$("#passwordCoincidence22").css("color","rgb(255,0,0)");
			};
			return false;
		}else return true;
	}

	function restore (typeForm, type, number){
		$(".form" + typeForm).removeClass("has-error");
		$(".form" + typeForm).removeClass("has-feedback");
		$("#" + type + "Pattern" + number).addClass("hyde");
		$("#" + type + "Required" + number).addClass("hyde");
		$("#" + type + "Glyphicon" + number).addClass("hyde");		
	}

	function restorePassword21 (){
		$(".formDateDeAlta").removeClass("has-error");
		$(".formDateDeAlta").removeClass("has-feedback");
		$("#passwordGlyphicon21").addClass("hyde");
		$("#passwordRequired21").addClass("hyde");
		$("#passwordPattern21").removeClass("hyde");
		$("#passwordPattern21").css("color","grey");
	}

	function restorePassword22 (){
		$(".formDateDeAlta").removeClass("has-error");
		$(".formDateDeAlta").removeClass("has-feedback");
		$("#passwordGlyphicon22").addClass("hyde");
		$("#passwordRequired22").addClass("hyde");
		$("#passwordCoincidence22").addClass("hyde");
	}

	function clearForm (){		
		var inputs = $("#formChangeData").find("input");
		for(i=2;i<inputs.size();i++){
			inputs[i].value="";
		}		
	}

	function updateData(){
		$.get("/completaleWEB/UpdateData?t="+Math.random(), function(data,status){
			if(status=="success"){
				var newData=JSON.parse(data);
				$("#likes").text(newData.likes);
				$("#followers").text(newData.followers);
				$("#followedBy").text(newData.followedBy);
				$("#followingRequests").text(newData.followingRequests);
				$("#storyParticipationRequests").text(newData.storyParticipationRequests);
			}
			else console.log("Error al obtener los datos del usuario mediante AJAX");
		});
		var allData = {
			followingRequests: parseInt($("#followingRequests").text()),
		    storyParticipationRequests: parseInt($("#storyParticipationRequests").text()),
		}
		var suma = allData.followingRequests + allData.storyParticipationRequests;
		$("#badgeSum").text(suma.toString());
		if (suma != 0){
			$("#badgeSum").css("display","inline");
		};
	}

	function getTimeline(){
		$.get("/completaleWEB/RetrieveTimeline?t="+Math.random(),function(data,status){
			if (status=="success"){
				var newData=JSON.parse(data);
				var storiesTimeline ="";
				var challengesTimeline ="";
				for (var i=1;i<newData[0][0];i++){
					storiesTimeline += 
						"<div class=\"well well-xs adjustTimeline\">" +
							"<div class=\"sameRow\">" +
								"<a href=\"/completaleWEB/SeeStory?access=timeline&title=" + newData[i][0] + "\" class=\"titleHref\">" +
								"<h4 class=\"titleTimeline\">" + newData[i][0] + "</h4></a>" + 
							"</div>" +
							"<p> Participantes: " + newData[i][1] +
							"<span class=\"likes\">Likes: " +
							"<span class=\"badge\">" + newData[i][3] + 
							"</span></span><span class=\"state\">" + newData[i][4] + "</span></p>" +
						"</div>";
				}
				for (var i=newData[0][0];i<newData.length;i++){
					challengesTimeline +=
						"<div class=\"well well-xs adjustTimeline\">" + 
							"<div class=\"sameRow\">" +
								"<h4 class=\"titleTimeline\" id=\"titleTimelineChallenge\">" + newData[i][0] + ": " + newData[i][1] + "</h4>" + 
							"</div>" +
							"<p> Historias: " + newData[i][2] + "/" + newData[i][3] + 
							"<span class=\"dateTimeline\">Fecha finalizaci\u00f3n: " + newData[i][4] + 
							"</span><span class=\"state\">" + newData[i][5] + "</span></p>" +
						"</div>";
				}				
				$("#StoriesTimeline").html(storiesTimeline);
				$("#ChallengesTimeline").html(challengesTimeline);
			}
			else console.log("Error al obtener los datos del timeline mediante AJAX");
		});
	}

	function addNewFragment(title, fragment){

		$.post("/completaleWEB/AddNewFragment",
		{
			title: title,
			fragment: fragment
		},
		function (data, status){
			if(status=="success"){
				var newData = JSON.parse(data);
				var enabled = newData[0];
				var story = "";
				for (i=1;i<newData.length;i++){
					story += 
							"<div class=\"alert alert-success\">" +
								"<p class=\"limit\">" + newData[i] + "</p>" +
							"</div>";
				}
				if (story!="") $("#text").html(story);
				$("#adjustTextArea").val("");
				if (enabled=="false") $("#adjustTextArea").prop("disabled", true);
			}
			else console.log("Error al obtener los datos de la historia mediante AJAX");
		});
	}

	function updateStory(title){

		$.post("/completaleWEB/UpdateStory",
		{
			title: title
		},
		function (data, status){
			if(status=="success"){
				var newData = JSON.parse(data);
				var opened = newData[0];
				var enabled = newData[1];
				var story = "";
				for (i=2;i<newData.length;i++){
					story += 
							"<div class=\"alert alert-success\">" +
								"<p class=\"limit\">" + newData[i] + "</p>" +
							"</div>";
				}
				if (story!="") $("#text").html(story);
				if (enabled=="false") $("#adjustTextArea").prop("disabled", true);
				else if (enabled=="true") $("#adjustTextArea").prop("disabled", false);
				if (opened=="false"){
					window.clearInterval(updatingStory);					
					alert("Esta historia ha sido cerrada por otro usuario");
					window.location.assign("http://localhost:8080/completaleWEB/DataCharger");
				}
			}
			else console.log("Error al obtener la historia mediante AJAX");
		});
	}

