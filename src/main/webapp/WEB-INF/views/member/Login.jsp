
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.*"%>
<%

	request.setCharacterEncoding("UTF-8");

%>

<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>Widget Store - 로그인 </title>
<link href="/resources/views/member/css/bootstrap.min.css" rel="stylesheet">



<!-- Custom CSS -->
<link href="/library/popup/style.css" rel="stylesheet">
<link	href="/resources/views/member/css/main.css?<?=filemtime(\'./css/readizgen.css\')?"	rel="stylesheet" type="text/css">
<link	href="/resources/views/member/css/form.css" rel="stylesheet" type="text/css">
<link	href="/resources/views/member/css/stylish-portfolio.css" rel="stylesheet" type="text/css">
<link	href="/resources/views/member/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css">
<!--  -->

<link href="/resources/views/member/css/stylish-portfolio.css"	rel="stylesheet">
<!-- bootsnipp down -->
<link href="/resources/views/member/css/login.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="/resources/views/member/font-awesome/font-awesome.css"	rel="stylesheet" type="text/css">
<link
	href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700,300italic,400italic,700italic"
	rel="stylesheet" type="text/css">
</head>
<body>

	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" align="center">
				<img class="img-circle" id="img_logo2"
					src="/resources/image/logo.png">

			</div>
			<div id="div-forms">
				<form id="login-form" method="POST" ACTION="/member/login.do">
					<div class="modal-body">
						<div id="div-login-msg">
							<div id="icon-login-msg"
								class="glyphicon glyphicon-chevron-right"></div>
							<span id="text-login-msg">Type your username and password.</span>
						</div>
						<input type="hidden" name="idType" value="NOTHING">
						<input id="email" name="email"	class="form-control" type="text" placeholder="Username" required>
						<input id="password" name="password"			class="form-control" type="password" placeholder="Password"		required> 
						<input class="sessionId" name="sessionId"		type="hidden" /> 
						<div class="checkbox">
							<label> <input type="checkbox"> Remember me
							</label>
						</div>
					</div>

					<button type="submit" class="btn btn-primary btn-lg btn-block">Login</button>
				</form>
			</div>
		</div>
	</div>

	<div id="ohsnap"></div>


</body>





<script	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	type="text/javascript" charset="utf-8"></script>

<script language="Javascript" type="text/javascript"
	src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<!-- Bootstrap Core JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
<script src="http://code.jquery.com/jquery-1.9.1.js" type="text/javascript"></script>			

<script>

	window.onload=function(){
			
		
	}
	function match_Password(){
		var pw1 = $('register_edit_password1').val();
		var pw2 = $('register_edit_password2').val();

		if(pw1 == pw2){
			return true;
		}
		else{
			alert("í¨ì¤ìëê° ì¼ì¹íì§ ììµëë¤.");
			return false;
		}
	}
	</script>

</html>
