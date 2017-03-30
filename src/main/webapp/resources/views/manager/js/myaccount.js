	
	$(document).ready(function() {
		
		setMyAccount();

	});
	
	function setMyAccount(){

		var comAjax = new ComAjax();
		comAjax.setUrl("/managerPage/myAccount/getMyAccountInfo.ajax");
		comAjax.setCallback("callback_setMyAccount");
		comAjax.setType("get");
		comAjax.ajax();
		
	}
	
	function callback_setMyAccount(data){
		
		$("#memberImage").attr("src", data["image"]);
		
		$(".cont3 #email").text("");
		$(".cont3 #email").append("<ok>Email: </ok> " + ' ' +data["email"]);
		
		$("#firstname").val(data["firstname"]);
		$("#lastname").val(data["lastname"]);
		
		$(".profileForm #nickname").val(data["nickname"]);
		$(".profileForm #email").val(data["email"]);
		
		$(".cont3 #nickname").text("");
		$(".cont3 #nickname").append( "<ok>Nickname :</ok> " + ' ' +  data["nickname"]  );
		
		$("#fullname").text(data["lastname"] +' ' + data["firstname"]);
		
	}


 $("#submitProfile").on("click", function(){
	 		
	 		if(!checkProfileForm(this.form)){
	 			return
	 		}
	 
			var comAjax = new ComAjax();
			comAjax.setUrl("/managerPage/myAccount/setProfile.ajax");
			comAjax.setCallback("callback_submitProfile")
			comAjax.addParam("firstname",$(".profileForm #firstname").val() );
			comAjax.addParam("lastname",$(".profileForm #lastname").val());
			comAjax.addParam("nickname",$(".profileForm #nickname").val());
			comAjax.setType("post");
			comAjax.ajax();

 		});
 
 function callback_submitProfile(data){
	 ohSnap(data["message"], {color:data["messageKind"]});
	 setMyAccount();
 	}
 
 
 
/* $("#imageForm").on("click", function(){
		
		var comAjax = new ComAjax();
		comAjax.setUrl("/managerPage/myAccount/registerMemberFace.ajax");
		comAjax.setCallback("callback_setImage")
		comAjax.setType("post");
		comAjax.ajax();

	});

	function callback_setImage(data){
		ohSnap(data["message"], {color:data["messageKind"]});
		$("memberImage").prop("src",data["imagePath"]);
	}
*/
 

 $("#navPages li").on("click", function(){

			$("#managerForm #forwardPageName").val($(this).attr('id'));
			$("#managerForm").submit()			
			
 	});
 
