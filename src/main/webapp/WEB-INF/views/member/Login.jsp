
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
<link href="../WidgetClientPage/css/bootstrap.min.css" rel="stylesheet">



<!-- Custom CSS -->
<link href="/library/popup/style.css" rel="stylesheet">
<link href="../WidgetClientPage/css/stylish-portfolio.css"
	rel="stylesheet">
<!-- bootsnipp down -->
<link href="../WidgetClientPage/css/login.css" rel="stylesheet">
<!-- Custom Fonts -->
<link href="../WidgetClientPage/font-awesome/font-awesome.css"
	rel="stylesheet" type="text/css">
<link
	href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700,300italic,400italic,700italic"
	rel="stylesheet" type="text/css">
</head>
<body>

	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" align="center">
				<img class="img-circle" id="img_logo2"
					src="../WidgetClientPage/img/test1.jpg">

			</div>
			<div id="div-forms">
				<form id="login-form" method="POST" ACTION="login.do">
					<div class="modal-body">
						<div id="div-login-msg">
							<div id="icon-login-msg"
								class="glyphicon glyphicon-chevron-right"></div>
							<span id="text-login-msg">Type your username and password.</span>
						</div>
						<input type="hidden" name="idType" value="nothing">
						<input id="login_username" name="login_username"
							class="form-control" type="text" placeholder="Username" required>
						<input id="login_password" name="login_password"
							class="form-control" type="password" placeholder="Password"
							required> <input class="sessionId" name="sessionId"
							type="hidden" /> <input type="hidden" name="idType"
							value="inner">
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
	src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script language="Javascript" type="text/javascript"
	src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

<script src="../WidgetClientPage/js/jquery.js"></script>
<script src="../WidgetClientPage/js/bootstrap.min.js"></script>

<script>

	var error_color;
	var id = "<%=session.getId() %>";
	var from = "<%=(String)session.getAttribute("from") %>";
	var message = "<%=(String)session.getAttribute("message") %>";
	
	window.onload=function(){
			

		<% if(request.getAttribute("message")!=null && request.getAttribute("messageKind") !=null){
			enumCautionKind kind = (enumCautionKind)request.getAttribute("messageKind");	
		%>
		  message = "<%=(String)request.getAttribute("message")%>";
		  popup_color = "<%=(String)kind.getString()%>";
		  ohSnap(message,{color:popup_color});
		<%
		}
		%>
		
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
