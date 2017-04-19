
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html class="full" lang="ko">

<!-- Make sure the <html> tag is set to the .full CSS class. Change the background image in the full.css file. -->

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
  <title>BuzzCloud</title>

<!-- Bootstrap Core CSS -->
<link	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"	rel="stylesheet"	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">

<!-- Custom CSS -->
<link	href="/resources/views/member/css/main.css?<?=filemtime(\'./css/readizgen.css\')?"	rel="stylesheet" type="text/css">
<link	href="/resources/views/member/css/form.css" rel="stylesheet" type="text/css">
<link	href="/resources/views/member/css/stylish-portfolio.css" rel="stylesheet" type="text/css">
<link	href="/resources/views/member/font-awesome/css/font-awesome.css" rel="stylesheet" type="text/css">
<!--  -->

<!--  notificator include -->
<script src="/resources/lib/include/notificator/ohsnap.js" type="text/javascript"	charset="utf-8"></script>
<link rel="stylesheet" type="text/css"	href="/resources/lib/include/notificator/ohsnap.css" />
 
    <!-- Custom Fonts -->
    <link href="http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,700,300italic,400italic,700italic" rel="stylesheet" type="text/css">
 </head>
 <body>
		<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" align="center">
				<img class="img-circle" id="img_logo2"				src="/resources/image/logo.png">

			</div>
			<div id="div-forms">
				<form id="findForm" method="GET" ACTION="/member/findPassword.do">
					<div class="modal-body">
						<div id="div-login-msg">
							<div id="icon-login-msg"								class="glyphicon glyphicon-chevron-right"></div>
							<span id="text-login-msg">이메일을 입력하세요.</span>
						</div>
						<input type="hidden" name="idType" value="nothing">
						<input id="email" name="email"	class="form-control" type="text" placeholder="Email" required>
					</div>

					<button type="submit" class="btn btn-primary btn-lg btn-block">메일 전송</button>
				</form>
			</div>
		</div>
	</div>
	<!--  static library -->

	<script type="text/javascript" charset="utf-8"		src="http://code.jquery.com/jquery-latest.js"></script>
	<script type="text/javascript" charset="utf-8"		src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
	<script type="text/javascript" charset="utf-8"		src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.2.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script 	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

	<!-- custom library  -->

<script type="text/javascript" charset="utf-8"		src="/resources/lib/commanJs/commonAjax.js"></script>
	<script type="text/javascript" charset="utf-8"		src="/resources/lib/commanJs/clientSideLibrary.js"></script>
 </body>
</html>
