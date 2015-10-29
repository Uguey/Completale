$(document).ready(function(){
	
	var usernameValidated;
	var emailValidated;
	var password1Validated;
	var password2Validated;

	$("input").on("invalid", function(){
  		return false;
	});

	$("form").submit(function (evt) {
	    evt.preventDefault();
	});

	$("#IniciaSesionA").click(function(){
		$("#DateDeAltaA").css("color","#404040");
		$("#IniciaSesionA").css("color","grey");
		$("#DateDeAlta").hide(400, function(){
			$("#IniciaSesion").show(400);
		});
	});

	$("#DateDeAltaA").click(function(){
		$("#IniciaSesionA").css("color","#404040");
		$("#DateDeAltaA").css("color","grey");
		$("#IniciaSesion").hide(400, function(){
			$("#DateDeAlta").show(400);
		});
	});
	
	$("#buttonSubmit1").click(function(){
		usernameValidated = validation(document.getElementById("username1"), "InicioSesion", "username", 1);
		password1Validated = validation(document.getElementById("password1"), "InicioSesion", "password", 1);
		restore("DateDeAlta", "username", 2);
		restore("DateDeAlta", "email", 2);
		restorePassword21();
		restorePassword22();
		if ((usernameValidated==true)&&(password1Validated==true)) document.getElementsByTagName("form")[0].submit();
	});
	$("#buttonSubmit2").click(function(){
		usernameValidated = validation(document.getElementById("username2"), "DateDeAlta", "username", 2);
		emailValidated = validation(document.getElementById("email2"), "DateDeAlta", "email", 2);
		password1Validated = validationPassword21(document.getElementById("password21"));
		password2Validated = validationPassword22(document.getElementById("password21"), document.getElementById("password22"));
		restore("InicioSesion", "username", 1);
		restore("InicioSesion", "password", 1);
		if ((usernameValidated==true)&&(emailValidated==true)
			&&(password1Validated==true)&&(password2Validated==true)) document.getElementsByTagName("form")[1].submit();	
	});
});
		
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
		$("#passwordPattern21").css("color","white");
	}

	function restorePassword22 (){
		$(".formDateDeAlta").removeClass("has-error");
		$(".formDateDeAlta").removeClass("has-feedback");
		$("#passwordGlyphicon22").addClass("hyde");
		$("#passwordRequired22").addClass("hyde");
		$("#passwordCoincidence22").addClass("hyde");
	}

	
