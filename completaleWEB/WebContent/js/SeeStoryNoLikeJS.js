$(document).ready(function(){

	resize();
	$(window).resize(function(){
		resize();
	});

});

	function resize(){
		var screenWidthMargin = screen.width/10;
		var screenWidth = screen.width*0.8;
		var screenWidthMarginString = screenWidthMargin.toString() + "px";
		var screenWidthString = screenWidth.toString() + "px";
		$("#mainJumbotron").css("margin-right",screenWidthMarginString);
		$("#mainJumbotron").css("margin-left",screenWidthMarginString);
		$("#mainJumbotron").css("width",screenWidthString);
	}	
