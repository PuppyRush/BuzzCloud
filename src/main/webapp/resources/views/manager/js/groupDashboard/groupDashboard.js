
		 $("#navPages li").on("click", function(){

				$("#managerForm #forwardPageName").val($(this).attr('id'));
				$("#managerForm").submit()			
				
	 	});
	 