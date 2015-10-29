var firstTime = true;

$(document).ready(function(){

	resize();
	$(window).resize(function(){
		resize();
	});

	var titleStory = $("#AOne").text();	

	if (firstTime == true)	$("#like").click(function(){addLike(titleStory)});

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

	function addLike (titleStory){
		if (firstTime == true) {
			$.post("/completaleWEB/AddLike",
				{
					title: titleStory
				},
				function (data, status) {
					if (status=="success")	$("span").addClass("fillHeart");
					else console.log("Error al añadir un like mediante AJAX");
			});
			firstTime = false;
		};		
	}


	
