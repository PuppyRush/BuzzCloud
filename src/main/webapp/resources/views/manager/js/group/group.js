	 
   var isChangingBand = false
   var myBands;
   var maxCapacity;
   
	$(document).ready(function(){						
				
		initPage();
			
	});
	
	function initPage(){
		var comAjax = new ComAjax();
		comAjax.setUrl("/managerPage/groupConfig/initMyBandInformation.ajax");
		comAjax.setCallback("callback_initMyBandInfo");
		comAjax.setType("get");
		comAjax.ajax();
	}
	
	function callback_initMyBandInfo(data){
		
		$("#groupMember").empty();
		$("#memberTable").find("tbody").html("");
		$("#makeGroupForm #bandAuthority").empty();
		$("#makeGroupForm #fileAuthority").empty();
		$("#makeGroupForm #selectGroup").empty();
		
		bandMembers = data["bandMembers"];
	  for (var key in bandMembers)  
	   $("#groupMember").append('<option>'+key);	 
	  
	  myBands = data["subBands"];
	    for(var key in myBands) {		   
		  $("#selectGroup").append('<option>'+key);	   		 	
			 }
  
    maxCapacity = Number(data["maxCapacity"])/(1024*1024);
  	  $("#groupCapacity").attr("placeholder","할당할 용량 ( 가능한 최대 용량 :  " + maxCapacity+" )" );
    
  	bandAuthority = data["bandAuthority"];
  	 for (var key in bandAuthority) {		   
			$("#bandAuthority").append('<option>'+key);	   		 	
		 }
		fileAuthority = data["fileAuthority"];
		  for (var key in fileAuthority) {		   
	  		$("#fileAuthority").append('<option>'+key);	   		 	
		  	}
  	 
		  $("#groupOwner").val(data["groupOwner"]);
		  $("#administrator").val(data["administrator"]);
		  
	}
       
   var members = new Array();
		var bandMembers;
		
		var mebersOfPart = new Array();
		
		window.onresize = function(){
			
			var width = ($("#groupName").css("width"));
			width = width.split("px")[0];
			width = Number(width)+100;
			width+="px";

		  };				  	  
		
		function getBandInfoFromPage(){

			 var groupName = $("#makeGroupForm #groupName").val();
			 var groupOwner = $("#makeGroupForm #groupOwner").val();
			 var administrator = $("#makeGroupForm #administrator").val();
			 var groupCapacity = Number( $("#makeGroupForm #groupCapacity").val() )*1024*1024;
			 var groupContain = $("#makeGroupForm #groupContain").val();
			 var selectedGroup = $("#selectGroup option:selected").val();
			 
			 var groupMembers = new Array();  
			    $("#groupMember option").each(function(){
			    	var name = $(this).val();
			    	groupMembers.push(bandMembers[name]); 
			    });
			 
			 var selectedBandAuthority = new Array();  
			    $("#bandAuthority :selected").each(function(){
			    	selectedBandAuthority.push($(this).val()); 
			    });
			    
			 var selectedFileAuthority = new Array();  
			    $("#fileAuthority :selected").each(function(){
			    	selectedFileAuthority.push($(this).val()); 
			    });
						
			 var ary = new Object();
			 ary.bandName = groupName;
			 ary.members = groupMembers;
			 ary.bandOwner = groupOwner;
			 ary.administrator = administrator;
			 ary.bandCapacity = groupCapacity;
			 ary.bandContain = groupContain;
			 ary.fileAuthority = selectedFileAuthority;
			 ary.upperBand = Number( myBands[selectedGroup] );
			 ary.exUpperBand = nowUpperBandId;
			 ary.bandAuthority = selectedBandAuthority;
	 
			 	if(Number( myBands[selectedGroup]  == -1)){
			 		if( selectedBandAuthority.indexOf("ROOT") ==-1)
		 				selectedBandAuthority.push("ROOT");
		 		}
			 	
		   return ary
		
		}
				
	 $("#makeSubmit").click(function(){

		 b_ = getBandInfoFromPage();
		 
		 if(isChangingBand==false && checkBandForm(b_,maxCapacity)){

					var comAjax = new ComAjax();
					comAjax.setUrl("/band/makeBand.ajax");
					comAjax.addParam("bandName",b_.bandName);
					comAjax.addParam("bandOwner",b_.bandOwner);
					comAjax.addParam("administrator",b_.administrator);
					comAjax.addParam("bandCapacity",b_.bandCapacity);
					comAjax.addParam("bandContain",b_.bandContain);
					comAjax.addParam("fileAuthority",b_.fileAuthority);
					comAjax.addParam("bandAuthority",b_.bandAuthority);
					comAjax.addParam("upperBand",b_.upperBand);
					comAjax.addParam("members",b_.members);
					comAjax.setCallback("callback_makeBand");
					comAjax.setType("post");
					comAjax.ajax();
					
		 }
		else{ 	
	 	 var groupCapacity = Number( $("#makeGroupForm #groupCapacity").val() )*1024*1024;
			 
	 		if(groupCapacity < b_["bandCapacity"]){
	 			ohSnap("용량이 이전 크기보다 작을 수 없습니다. 드라이브를 정리 후 다시 설정하세요.", {color:"red"});
	 			return;
	 		}
	 		if(isExistBandName){
	 			ohSnap("그룹이름이 중복됩니다. 이름을 변경하세요. ", {color:"red"});
	 			return;
	 		}
	 		if(!isExistMemberName){
	 			ohSnap("관리자의 이름이 존재하지 않습니다. 존재하는 이름으로 변경하세요. ", {color:"red"});
	 			return;
	 		}
	 			 		

			var comAjax = new ComAjax();
			comAjax.setUrl("/band/updateBand.ajax");
			comAjax.addParam("bandId", selectedBandId);
			comAjax.addParam("bandName",b_.bandName);
			comAjax.addParam("bandOwner",b_.bandOwner);
			comAjax.addParam("administrator",b_.administrator);
			comAjax.addParam("bandCapacity",b_.bandCapacity);
			comAjax.addParam("bandContain",b_.bandContain);
			comAjax.addParam("fileAuthority",b_.fileAuthority);
			comAjax.addParam("bandAuthority",b_.bandAuthority);
			comAjax.addParam("upperBand",b_.upperBand);
			comAjax.addParam("exUpperBand",b_.exUpperBand);
			comAjax.addParam("members",b_.members);
			comAjax.setCallback("callback_updateBand");
			comAjax.setType("post");
			comAjax.ajax();		 
	
			
			
		}
	 });
	 
	 function callback_makeBand(data){
		 ohSnap(data["message"],{color: data["messageKind"]} );
			network.redraw();
	 }
	 
	 function callback_updateBand(data){

		 isChangingBand = false;
		 	$("#makeSubmit").val("그룹 만들기");
		
		 ohSnap(data["message"],{color: data["messageKind"]} );
		 
		 
			initPage();
	 }
	 
	 
		$("#removeMember").on("click", function(){
			$("#groupMember option:selected").remove();
		})

	 
	 $("#initFormButton").click(function(){
		 	
		 		$("#makeGroupForm #groupName").val("");
    		$("#makeGroupForm #groupOwner").val("");
    		$("#makeGroupForm #administrator").val("");
    		$("#makeGroupForm #groupCapacity").val("");
    		$("#makeGroupForm #groupContain").val("");  		
    		$("#groupMember").empty();
    		$("#memberTable").find("tbody").html("");
    		$("#makeGroupForm #bandAuthority").empty();
    		$("#makeGroupForm #fileAuthority").empty();
    		$("#makeGroupForm #selectGroup").empty();
    		
    	 	$("#makeSubmit").val("그룹 만들기");
    	 	isChangeBand = false
    	 	initPage();
    	 	
	 	});
	 

		 $("#navPages li").on("click", function(){

				$("#managerForm #forwardPageName").val($(this).attr('id'));
				$("#managerForm").submit()			
				
	 	});
	 
		 
		 isExistMemberName = true;
		  $('#administrator').focusout(function() {
				 
			 	if(!checkBandName( $(this).val()) )
			 		return;		 

				var comAjax = new ComAjax();
				comAjax.setUrl("/member/isExistMember.ajax");
				comAjax.addParam("memberName",$("#administrator").val());
				comAjax.setCallback("callback_isExistMemberName");
				comAjax.setType("get");
				comAjax.ajax();
			 
		 });
		 function callback_isExistMemberName(data){
			 isExistMemberName = data["isExist"];
			 ohSnap(data["message"],{color: data["messageKind"]} );
			 
		 }
		
		 
		 	isExistBandName = false;
		 $('#groupName').focusout(function() {
			 
			 	if(!checkBandName( $(this).val()) )
			 		return;		 

				var comAjax = new ComAjax();
				comAjax.setUrl("/band/isExistName.ajax");
				comAjax.addParam("bandName",$("#groupName").val());
				comAjax.setCallback("callback_isExistBandName");
				comAjax.setType("get");
				comAjax.ajax();
			 
		 });
		 
		 function callback_isExistBandName(data){
			 isExistBandName = data["isExist"];
			 ohSnap(data["message"],{color: data["messageKind"]} );
			 
		 }
		 
		  $('#searchMember').keyup(function() {
			  
			    var str = $(this).val();
			    		
			    		if(str.length>=3){
			    		
			    		 $.ajax({
			    		    url:'/autocomplete/getMemberNames.ajax',
			    		    data: { nickname: str },
			    		    dataType:'json',
			    		    success:function(data){
			    		    	
			    		    	 var members = new Array();
				    		    for (var key in data) {	    			
				    		    	mebersOfPart.push(key); 
				    		    			}

			    	           		  }
			    		 	})
	    			}
			    
			});
		 