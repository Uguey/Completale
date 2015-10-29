$(document).ready(function(){

	resize();
	$(window).resize(function(){
		resize();
	});

	$(".clickButtonAcceptFollowingRequest").click(function(){
		var follower = "";
		var maybeButton = this.nextSibling.nextSibling;
		if (maybeButton.nodeName=="SPAN"){
			follower = maybeButton.firstChild.nodeValue;
		};
		acceptFollowRequest(this, follower);	
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

	function acceptFollowRequest(button, follower){
		$.post("/completaleWEB/AcceptFollowRequest",
			{
				nickFollower: follower
			},
			function (data, status){
				if(status="success"){
					button.disabled=true;
					button.style.cursor="default";
				}
				else console.log("Error al insertar una petición de seguimiento mediante AJAX");
		});
	}

	
