

var selectedBandIdOfAutoCompleted;

							var options = {

							  url: function(phrase) {
								    return '/autocomplete/getBandNames.ajax';
								  },

								  getValue: function(element) {
								    return element.name;
								  },

								  ajaxSettings: {
								    dataType: "json",
								    method: "GET",
								    data: {
								      bandName: $('#searchBand').val()
								    }
								  },

	/*							  preparePostData: function(data) {
								    data.phrase = $("#searchBand").val();
								    return data;
								  },*/
							
							  template:{
							  			type : "description",
							  			fields :{description:"root"}
								  
								  },
								  
							  theme:"round",
								  
							  list: {
							  maxNumberOfElements : 10,
							  	match :{enabled:true},
									onClickEvent: function() {

										
										var bandId= $("#searchBand").getSelectedItemData().selectedBandId;
										var rootBandId= $("#searchBand").getSelectedItemData().rootBandId;
										
										selectedBandIdOfAutoCompleted = bandId
										
										var comAjax = new ComAjax();
										comAjax.setUrl("/autocomplete/getSerachedBandInfo.ajax");
										comAjax.setCallback("callback_setBandInfo");
										comAjax.addParam("bandId",bandId);
										comAjax.setType("GET");
										comAjax.ajax();
										
				
								       
			
									}	
								},
								  
								  requestDelay: 500
						};

			$("#searchBand").easyAutocomplete(options);
							
			function callback_setBandInfo(data){
				
				$("#rootBandName").val( "최상위 그룹 이름 : " + data["rootBandName"]);
  	  			$("#bandAdminName").val( "검색된 밴드 관리자 : " + data["bandAdminName"]);
  	  			$("#bandOwnerName").val( "검색된 밴드 소유주 :" + data["bandOwnerName"]);
  	  			$("#bandContain").val(data["bandContain"]);
				
			}
							
