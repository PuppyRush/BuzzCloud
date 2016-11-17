	 
   
   var myBands;
   var maxCapacity;
   
	$.ajax({
	    url:'./ajax/group/getBandMembers.jsp',
	    data: { memberId: memberId},
	    dataType:'json',
	    success:function(data){
	  	  bandMembers = data;
	 	    for (var key in data)  
				   $("#groupMember").append('<option>'+key);	   				   		 	
	 			 
           }
       })
       
 $.ajax({
	    url:'./ajax/group/getSubBands.jsp',
	    data: { memberId: memberId},
	    dataType:'json',
	    success:function(data){
	    myBands = data;
	 	    for (var key in data) {		   
	  		  $("#selectGroup").append('<option>'+key);	   		 	
	 			 }
           }
      })
       
  $.ajax({
	    url:'./ajax/group/getMaxCapacity.jsp',
	    data: { memberId: memberId},
	    dataType:'json',
	    success:function(data){
	    	 maxCapacity = Number(data.capacity)/(1024*1024);
	  	  $("#groupCapacity").attr("placeholder","할당할 용량 ( 가능한 최대 용량 :  " + maxCapacity+" )" );
	 	   
           }
       })
       
   $.ajax({
	    url:'./ajax/group/getBandAuthority.jsp',
	    dataType:'json',
	    success:function(data){
		    for (var key in data) {		   
			  		$("#bandAuthority").append('<option>'+key);	   		 	
				 }
          }
      })
      
   $.ajax({
	    url:'./ajax/group/getFileAuthority.jsp',
	    dataType:'json',
	    success:function(data){
		    for (var key in data) {		   
			  		$("#fileAuthority").append('<option>'+key);	   		 	
				 }
          }
      })
  	   

	



		//var members = new Array();
		var bandMembers;
		
		var mebersOfPart = new Array();
		
		window.onresize = function(){
			
			var width = ($("#groupName").css("width"));
			width = width.split("px")[0];
			width = Number(width)+100;
			width+="px";

		  };
		
		  
		  
		  
		  
	  $('#administrator').keyup(function() {
		  
		    var str = $(this).val();
		    		
		    		if(str.length>=4){
		    		
		    		 $.ajax({
		    		    url:'./ajax/group/getMembersOfPart.jsp',
		    		    data: { nickname: str },
		    		    dataType:'json',
		    		    success:function(data){
		    		    	
		    		    	/* var members = new Array();*/
			    		    for (var key in data) {	    			
			    		    	mebersOfPart.push(key); 
			    		    			}

		    	           		  }
		    		 	})
    			}
		    
		});
	
	
		var options = {

			  url: function(phrase) {
				    return './ajax/group/getMembersOfPart.jsp';
				  },

				  getValue: function(element) {
				    return element.name;
				  },

				  ajaxSettings: {
				    dataType: "json",
				    method: "POST",
				    data: {
				      nickname:   $('#searchMember').val()
				    }
				  },

				  preparePostData: function(data) {
				    data.phrase = $("#searchMember").val();
				    return data;
				  },

			  list: {
					onClickEvent: function() {
						var index = $("#searchMember").getSelectedItemIndex();
						var nick = $("#searchMember").getItemData(index).name;
						
						$("#groupMember").each(function(){
								if( $(this).val == nick){
										return;
								} 
						});
						
						$("#groupMember").append('<option>'+nick);
											
					}	
				},
				  
				  requestDelay: 400
		};

		$("#searchMember").easyAutocomplete(options);
		$("#administrator").easyAutocomplete(options);

		
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
				 		
			    url:'./ajax/group/makeBand.jsp',
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
			 		
		    url:'./ajax/group/updateBand.jsp',
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
	 
