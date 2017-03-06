	$.ajax({
	    url:'./ajax/groupDashboard/getBandMembers.jsp',
	    data: { bandId: bandId},
	    dataType:'json',
	    success:function(data){
	  	  bandMembers = data;
	 	    for (var key in data)  
				   $("#groupMember").append('<option>'+key);	   				   		 	
	 			 
           }
       })
       

   $("#navPages li").on("click", function(){

			$("#managerForm #forwardPageName").val($(this).attr('id'));
			$("#managerForm").submit()			
			
 	});
 