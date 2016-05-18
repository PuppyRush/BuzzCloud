<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

		<input type="BUTTON" name="innerJoin" value="가입하기"onClick="innerJoin()"> 

		<div>
			<form method="GET" ACTION="/logon.do" id="hiddenForm">
				<input type = "hidden" name = "email" value = "puppyrush@naver.com" >
				<input type = "hidden" name = "nickname" value = "PuppyRush" >
				<input type = "hidden" name = "password" value = "1234" >
				<input type = "hidden" name = "idType" value = "inner" >
				<input type = "hidden" name = "reg_date" value = "" >
			</form>
		</div>

<script>

	function innerJoin(){
	
			document.forms["hiddenForm"].submit();
		
	}

</script>

</body>
</html>
