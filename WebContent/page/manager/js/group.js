   

		//var members = new Array();
		var bandMembers;
		var bands = new Map();
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
		    		    url:'./ajax/getMembersOfPart.jsp',
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
	
	  $('#searchMember').keyup(function() {
		  
		    var str = $(this).val();
		    		
		    		if(str.length>=4){
		    		
		    		 $.ajax({
		    		    url:'./ajax/getMembersOfPart.jsp',
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

	    
	    $(function () {         
		    $('#administrator').autocomplete({
	    				
		        source: mebersOfPart,
		        select: function (event, ui) {
		        							//아이템 선택시 처리 코드
		        								
		        							},
		        selectFirst: true,
		        minLength: 4,
		        open: function () {
		        
		            $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
		        			},
		        close: function () {
		            $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
		                }
		            });
	        });
	  

		  $(function () {
		          
			    $('#searchMember').autocomplete({
		        source: mebersOfPart,
		        select: function (event, ui) {
		        							//아이템 선택시 처리 코드
		      
		            var selString = ui.item.value;          
		            alert(selString );
		            $("#groupMember").append('<option>'+selString);
		        							},
		        selectFirst: true,
		        minLength: 4,
		        open: function () {
		        				console.log(bandMembers.lenght);
		            $(this).removeClass("ui-corner-all").addClass("ui-corner-top");
		        			},
		        close: function () {
		            $(this).removeClass("ui-corner-top").addClass("ui-corner-all");
		                }
		            });
    		    
    		    
        });
		  
		  



  

	 $("#makeSubmit").on('click' ,function(){

		 var groupName = $("#makeGroupForm #groupName").val();
		 var groupOwner = $("#makeGroupForm #groupOwner").val();
		 var administrator = $("#makeGroupForm #administrator").val();
		 var groupCapacity = $("#makeGroupForm #groupCapacity").val();
		 var groupContain = $("#makeGroupForm #groupContain").val();
		 var selectedGroup = $("#selectGroup option:selected").val();
		 
		 var groupMembers = new Array();  
		    $("#groupMember option").each(function(){
		    	var name = $(this).val();
		    	groupMembers.push(bandMembers[name]); 
		    });
		 
		 var selectedBandAuthority = new Array();  
		    $("#groupAuthority :selected").each(function(){
		    	selectedBandAuthority.push($(this).val()); 
		    });
		    
			 var selectedFileAuthority = new Array();  
			    $("#fileAuthority :selected").each(function(){
			    	selectedFileAuthority.push($(this).val()); 
			    });
		 
		 if(groupName.length<4){
		 	ohSnap('그룹 이름은 네자 이상어야 합니다.', {color: 'red'});
		 	return;
		  }

		 var ary = new Object();
		 ary.groupName = groupName;
		 ary.members = groupMembers;
		 ary.groupOwner = groupOwner;
		 ary.administrator = administrator;
		 ary.groupCapacity = groupCapacity;
		 ary.groupContain = groupContain;
		 ary.fileAuthority = selectedFileAuthority;
		 ary.upperGroup = Number( bands[selectedGroup] );
		 ary.bandAuthority = selectedBandAuthority;

	   var json = JSON.stringify(ary);
		  
		 $.ajax({
		 		
	    url:'./ajax/makeBand.jsp',
	    data : {data : json},
	    dataType:'json',
	    success:function(data){
        
	    		if(data.get("result") == true)
	    				ohsnap("그룹 생성에 성공하였습니다.",{color: 'green'} );
	    		else
							ohsnap("그룹 생성에 실패하였습니다. 관리자에게 문의하세요.",{color: 'red'} );
            }
        })
	 
	 });
