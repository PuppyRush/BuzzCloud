   

		//var members = new Array();
		var groupMembers;
		var groups; 

		window.onresize = function(){
			
			var width = ($("#bandName").css("width")).split("px")[0];
			width = Number(width)+100;
			width+="px";
		
		/*	$(".display").css("width","400px");*/
			
		  };
		
		  $('#administrator').keyup(function() {
			    var str = $(this).val();
			    		
			    		if(str.length>=4){
			    		
			    		 $.ajax({
			    		    url:'./ajax/getMembersOfPart.jsp',
			    		    data: { nickname: str },
			    		    dataType:'json',
			    		    success:function(data){
			    		    	
			    		    	 var members = new Array();
				    		    for (var key in data) {	    			
				    		    		members.push(key); 
				    		    			}

				    		    
			    	           		  }
			    		 	})
	    			}
			    
			});
		

		  $(function () {
		          
			    $('#searchMember').autocomplete({
		        source: members,
		        select: function (event, ui) {
		        							//아이템 선택시 처리 코드
		      
		            var selString = ui.item.value;          
		            alert(selString );
		            $("#groupMember").append('<option>'+selString);
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
			    $('#administrator').autocomplete({
  		    				
			        source: members,
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
		  


  

	 $("#makeSubmit").on('click' ,function(){

		 var groupName = $("#makeGroupForm #groupName").val();
		 var groupOwner = $("#makeGroupForm #groupOwner").val();
		 var administrator = $("#makeGroupForm #administrator").val();
		 var groupCapacity = $("#makeGroupForm #groupCapacity").val();
		 
		 var slectedMembers = $("#groupMember option:selected").text();
		 var selectedGroup = $("#selectGroup option:selected").text();
		 var selectedAuthority = $("#groupAuthority option:selected").text();
		 var groupId = 3;
		 
		 if(groupName.length<4){
		 	ohSnap('그룹 이름은 네자 이상어야 합니다.', {color: 'red'});
		 	return;
		  }

		 if(selectedGroup<0){
		 	ohSnap('그룹이 속할 그룹을 선택하세요.', {color: 'red'});
		 	return;
		  }
		 
		 $.ajax({
	    url:'./ajax/makeGroup.jsp',
	    data: { groupName: groupName, groupOwner : groupOwner , admin : administrator , capacity : groupCapacity , members : slectedMembers,
	    						selectedGroup : selectedGroup , auth : selectedAuthority	  },
	    dataType:'json',
	    success:function(data){
        
        for(var name in data){
            str += '<li>'+data[name]+'</li>';
        				}
        $('#timezones').html('<ul>'+str+'</ul>');
            }
        })
	 
	 });
