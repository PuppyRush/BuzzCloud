<%@ page import="com.puppyrush.buzzcloud.page.VerifyPage, com.puppyrush.buzzcloud.page.enums.enumPage, java.util.HashMap" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
<%

	request.setCharacterEncoding("UTF-8");


 	boolean isSuccessVerify = false;
	HashMap<String,Object> results =  VerifyPage.Verify(session.getId(), enumPage.ENTRYTOMAIN);
	
	if((boolean)results.get("isSuccessVerify")){
	
		
		isSuccessVerify = true;		
		
	}else{

		enumPage to = (enumPage)results.get("to");
		response.sendRedirect(to.toString());
		return;
				
	}
	
%>

<!DOCTYPE html>
<html class="full" lang="ko">


<head>

		<meta http-equiv = "Content-Type" content="text/html; charset=utf-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

<script type="text/javascript" charset="utf-8"		src="http://code.jquery.com/jquery-latest.js"></script>
    
</head>
<body>

			<form id="form" method="GET" ACTION="/main.do">
			</form>



</body>

<script>


window.onload=function(){

	<%
		if(isSuccessVerify){
			%>
				$("#form").submit();
			<%
			
		}
			
	
	%>

}

</script>


</html>