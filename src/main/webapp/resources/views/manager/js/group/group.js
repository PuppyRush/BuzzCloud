	 
   
   var myBands;
   var maxCapacity;
   
	$(document).ready(function(){						
				
			var comAjax = new ComAjax();
			comAjax.setUrl("/managerPage/groupConfig/initMyBandInformation.ajax");
			comAjax.setCallback("callback_initMyBandInfo");
			comAjax.setType("get");
			comAjax.ajax();
			
	});
		
   
	function callback_initMyBandInfo(data){
		
		bandMembers = data["bandMembers"];
	  for (var key in bandMembers)  
	   $("#groupMember").append('<option>'+key);	 
	  
	  myBands = data["subBands"];
	    for (var key in myBands) {		   
		  $("#selectGroup").append('<option>'+key);	   		 	
			 }
  
    maxCapacity = Number(data["maxCapacity"])/(1024*1024);
  	  $("#groupCapacity").attr("placeholder","할당할 용량 ( 가능한 최대 용량 :  " + maxCapacity+" )" );
    
  	bandAuthority = data["bandAuthority"];
  	 for (var key in data) {		   
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
			 var groupCapacity = Number( $("#makeGroupForm #groupCapacity").val() );
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
			 ary.bandAuthority = selectedBandAuthority;
	 
			 	if(Number( myBands[selectedGroup]  == -1)){
			 		if( selectedBandAuthority.indexOf("ROOT") ==-1)
		 				selectedBandAuthority.push("ROOT");
		 		}
			 	
		   return ary
		
		}
				
	 $("#makeSubmit").click(function(){

		 bandInfo = getBandInfoFromPage();
		 
		 if(changeBand==false){

				 if(groupName.length<4){
				 	ohSnap('그룹 이름은 네자 이상어야 합니다.', {color: 'red'});
				 	return;
				  }
		
				 if(isValidateString(bandInfo.bandName)){
				 		ohSnap('변경할 값에 and or = 혹은 특수문자가 들어갈 수 없습니다.', {color: 'red'});
				 		return;
				 	}
				 if(isValidateString(bandInfo.bandContain)){
			 		ohSnap('변경할 값에 and or = 혹은 특수문자가 들어갈 수 없습니다.', {color: 'red'});
			 		return;
			 	}
				 
				 if( isNaN(Number(bandInfo.bandCapacity)) ){
				 			ohSnap('허용 용량엔 숫자만 입력바랍니다.', {color: 'red'});
				 		return;
				 	}
				 if(bandInfo.bandCapacity > maxCapacity){
				 		ohSnap('허용용량이 최대용량보다 클 수 없습니다.', {color: 'red'});
				 		return;
				 	}
				 
				 str =  JSON.stringify(bandInfo);
				 $.ajax({
				 		
			    url:'/configPage/groupCnfig/makeBand.jsp',
			    data : {data : str},
			    dataType:'json',
			    success:function(data){
		        
			    		if(data["isSuccess"]){
			    				ohSnap("그룹 생성에 성공하였습니다.",{color: 'green'} );
			    				network.redraw();
			    			}
			    		else
									ohSnap("그룹 생성에 실패하였습니다. 관리자에게 문의하세요.",{color: 'red'} );
		            }
		        })
		 }
		else{
				
			 	
	 	 var groupCapacity = Number( $("#makeGroupForm #groupCapacity").val() )*1024*1024;
			 
			 
	 		if(groupCapacity < selectedBandInfo["bandCapacity"]){
	 			ohSnap("용량이 이전 크기보다 작을 수 없습니다. 드라이브를 정리 후 다시 설정하세요.", {color:"red"});
	 			return;
	 		}

	 			str =  JSON.stringify(bandInfo);
			 $.ajax({
			 		
		    url:'/configPage/groupCnfig/updateBand.jsp',
		    data : {data : str},
		    dataType:'json',
		    success:function(data){
	        
		    
					 changeBand = false;
					 	$("#makeSubmit").val("그룹 만들기");
					
		    
		    		if(data["isSuccess"]){
		    				ohSnap("그룹 생성에 성공하였습니다.",{color: 'green'} );
		    				network.redraw();
		    			}
		    		else
								ohSnap("그룹 생성에 실패하였습니다. 관리자에게 문의하세요.",{color: 'red'} );
	            }
			 })
		}
	 });
	 
		$("#removeMember").on("click", function(){

				
			
		})

	 
	 $("#initFormButton").click(function(){
		 	
		 		$("#makeGroupForm #groupName").val("");
    		$("#makeGroupForm #groupOwner").val("");
    		$("#makeGroupForm #administrator").val("");
    		$("#makeGroupForm #groupCapacity").val("");
    		$("#makeGroupForm #groupContain").val("");  		
    		$("#groupMember").empty();
    	 	$("#makeSubmit").val("그룹 만들기");
		 
	 	});
	 

		 $("#navPages li").on("click", function(){

				$("#managerForm #forwardPageName").val($(this).attr('id'));
				$("#managerForm").submit()			
				
	 	});
	 