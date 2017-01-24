

var selectedBandIdOfAutoCompleted;

							var options = {

							  url: function(phrase) {
								    return '/page/mainPage/ajax/getBandAll.jsp';
								  },

								  getValue: function(element) {
								    return element.name;
								  },

								  ajaxSettings: {
								    dataType: "json",
								    method: "POST",
								    data: {
								      bandName: $('#searchBand').val()
								    }
								  },

								  preparePostData: function(data) {
								    data.phrase = $("#searchBand").val();
								    return data;
								  },
							
								  
								  
							  list: {
							  maxNumberOfElements : 10,
							  	match :{enabled:true},
									onClickEvent: function() {
										var index = $("#searchBand").getSelectedItemIndex();
										var bandId= $("#searchBand").getItemData(index).id;
										
										selectedBandIdOfAutoCompleted = bandId
										
										$.ajax({
									    url:'/page/mainPage/ajax/getSerachedBandInfo.jsp',
									    data: { bandId: bandId},
									    dataType:'json',
									    success:function(data){
							  	  			$("#rootBandName").val(data["rootBandName"]);
							  	  			$("#bandAdminName").val(data["bandAdminName"]);
							  	  			$("#bandOwnerName").val(data["bandOwnerName"]);
							  	  			$("#bandContain").val(data["bandContain"]);
							  	  			
									 			 
								           }
								       })
								       
			
									}	
								},
								  
								  requestDelay: 500
						};

						$("#searchBand").easyAutocomplete(options);