$(document).ready(function(){

	resize();
	$(window).resize(function(){
		resize();
	});

	$("form").submit(function(){
		var text = $("#title").text();
		$("#submitButton").after("<input type=\"hidden\" name=\"Title\" value=\"" + text + "\">");
	});

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


	
