
	//init page
	getMemberInfo();

	
	

	function getMemberInfo(){
		 $.ajax({
		 url:'/configPage/MyAccount/getMyAccountInfo.ajax',
		 data: { memberId: memberId},
		 dataType:'json',
		 success:function(data){

				$("#memberImage").attr("src", data["representiveImage"]);
				
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
		})
	}
	



 $("#submitProfile").on("click", function(){
	 	
	 	$("this").submit();
 
	  	var nickname = 	$(".profileForm #nickname").val();
  		var lastname = 	$("#lastname").val();
  		var firstname =  	$("#firstname").val();
  		
		 if(isValidateString(nickname)==false){
			 ohSnap("변경할 값에 and or = 혹은 특수문자가 들어갈 수 없습니다.",{color:"red"});
			 return;
		  }
		 if(isValidateString(lastname)==false){
		 ohSnap("변경할 값에 and or = 혹은 특수문자가 들어갈 수 없습니다.",{color:"red"});
		 return;
		 }
		 if(isValidateString(firstname)==false){
		 ohSnap("변경할 값에 and or = 혹은 특수문자가 들어갈 수 없습니다.",{color:"red"});
		 return;
		 }
		 
		 
	 	 $.ajax({
	 	 url:'/configPage/MyAccount/setProfile.ajax',
	 	 data: { memberId: memberId, nickname:nickname , firstname : firstname , lastname : lastname  },
	 	 dataType:'json',
	 	 success:function(data){
		 	 		
		 	 	if(data["isSuccess"]){
		 	 		ohSnap("변경에 성공하였습니다.",{color:"green"});
		 	 		getMemberInfo();		 	 		
		 	 	}
		 	 	else
		 	 		ohSnap("변경에 실패하였습니다. 관리자에게 문의하세요",{color:"red"});

	 	 }
 	 })
 
 	})
 