
		  

		var options = {

			  url: function(phrase) {
			  return '/autocomplete/getMemberNames.ajax';
				  },

				  getValue: function(element) {
				    return element.name;
				  },

				  ajaxSettings: {
				    dataType: "json",
				    method: "GET",
				    data: {
				      nickname:   $('#searchMember').val()
				    }
				  },

			  theme:"round",
				  
				  preparePostData: function(data) {
				    data.phrase = $("#searchMember").val();
				    return data;
				  },

			  list: {
			  	maxNumberOfElements : 15,
			  	match :{enabled:true},
					onClickEvent: function() {
						var index = $("#searchMember").getSelectedItemIndex();
						var nick = $("#searchMember").getItemData(index).name;
						var id = $("#searchMember").getItemData(index).id;
						
						var isDup=false
						$("#groupMember option").each(function(){
								if( this.value == nick){
									isDup = true
									return;
								} 
						});
						
						if(!isDup){
							$("#groupMember").append('<option>'+nick);
							bandMembers[nick] = id;
						}
											
					},
			
				},
				  
				  requestDelay: 400
		};

		$("#searchMember").easyAutocomplete(options);
		
		
		
		
		var options = {

				  url: function(phrase) {
					    return '/autocomplete/getMemberNames.ajax';
					  },

					  getValue: function(element) {
					    return element.name;
					  },

					  ajaxSettings: {
					    dataType: "json",
					    method: "GET",
					    data: {
					      nickname:   $('#administrator').val()
					    }
					  },

				  theme:"round",
					  
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
							$("#administrator").val(nick);
												
						}	
					},
					  
					  requestDelay: 400
			};

		
		$("#administrator").easyAutocomplete(options);

	 