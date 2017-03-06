
		  
		  
	  $('#administrator').keyup(function() {
		  
		    var str = $(this).val();
		    		
		    		if(str.length>=4){
		    		
		    		 $.ajax({
		    		    url:'/configPage/groupCnfig/getMembersOfPart.jsp',
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
			  return '/autocomplete/getMemberNames.ajax';
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
			  	maxNumberOfElements : 10,
			  	match :{enabled:true},
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
		
		
		var options = {

				  url: function(phrase) {
					    return '/configPage/groupCnfig/getMembersOfPart.jsp';
					  },

					  getValue: function(element) {
					    return element.name;
					  },

					  ajaxSettings: {
					    dataType: "json",
					    method: "POST",
					    data: {
					      nickname:   $('#administrator').val()
					    }
					  },

					  preparePostData: function(data) {
					    data.phrase = $("#administrator").val();
					    return data;
					  },

				  list: {
				  maxNumberOfElements : 10,
				  	match :{enabled:true},
						onClickEvent: function() {
							var index = $("#administrator").getSelectedItemIndex();
							var nick = $("#administrator").getItemData(index).name;
							
							$("#groupMember").each(function(){
									if( $(this).val == nick){
											return;
									} 
							});
							
							$("#administrator").val(nick);
												
						}	
					},
					  
					  requestDelay: 400
			};

		
		$("#administrator").easyAutocomplete(options);

	 