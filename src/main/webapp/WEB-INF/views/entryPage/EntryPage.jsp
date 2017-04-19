
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

<title>Start BuzzCloud</title>

<!-- Bootstrap Core CSS -->
<link	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"	rel="stylesheet"	integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
	crossorigin="anonymous">

<!-- Custom CSS -->
<link	href="/resources/views/entryPage/css/mainpage.css?<?=filemtime(\'./css/readizgen.css\')?"	rel="stylesheet" type="text/css">
<link	href="/resources/views/entryPage/css/form.css" rel="stylesheet" type="text/css">
<!--  -->

<!--  notificator include -->
<script src="/resources/lib/include/notificator/ohsnap.js" type="text/javascript"	charset="utf-8"></script>
<link rel="stylesheet" type="text/css"	href="/resources/lib/include/notificator/ohsnap.css" />

</head>

<body>

	<div id="mask"></div>
	<div class="joinModal">

		<div class="text-vertical-center" tabindex="-1" role="dialog"		aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"	aria-hidden="true">×</button>
						<h1 class="text-center">버즈클라우드에 가입하기</h1>
					</div>
					<div class="modal-body">

						<form class="form col-md-12 center-block" id="joinForm"		method="POST" ACTION="/member/join.do">
										<div class="form-group">
											<input type="text" name="email" id="email" class="form-control input-lg"	placeholder="Email">
										</div>
										<div class="form-group">
											<input type="text" name="nickname" id="nickname" class="form-control input-lg"			placeholder="Nickname">
										</div>
										<div class="form-group">
											<input type="password" name="password"	 id="password"	class="form-control input-lg" placeholder="Password">
										</div>
										<div class="form-group">
											<input type="password" name="password2"	id="password2"		class="form-control input-lg" placeholder="Rewrite Password">
										</div>

										<div class="form-group">
											<button class="btn btn-primary btn-lg btn-block" id="joinButton" type="button"	>가입하기 </button>
										
										</div>
			
										<input type="hidden" id="idType" name="idType" value="NOTHING">
						</form>

					</div>
					<div class="modal-footer">
						<div class="col-md-12">
							<button class="close" data-dismiss="modal" aria-hidden="true">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<div class="loginModal">
		<div class="text-vertical-center" tabindex="-1" role="dialog"		aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"				aria-hidden="true">×</button>
						<h1 class="text-center">버즈클라우드에 로그인</h1>
					</div>
					<div class="modal-body">

						<form class="form col-md-12 center-block" id="loginForm"	method="POST" ACTION="/member/login.do">
							<div class="form-group">
								<input type="text" name="email"	 id="email"		class="form-control input-lg" placeholder="Email">
							</div>
							<div class="form-group">
								<input type="password" name="password"	id="password"		class="form-control input-lg" placeholder="Password">
							</div>
							<div class="form-group">
								<button class="btn btn-primary btn-lg btn-block" type="submit"	>로그인하기</button>
								<span class="pull-right"><a href="#" class="joinToBuzzCloud">가입하기  </a></span>  
								<span class="pull-right"><a href="#" class="findPassword">비밀번호 찾기  /</a></span>
							</div>

							<input type="hidden"  id="idType" name="idType" value="NOTHING">
							<input type="hidden"  id="nickname" name="nickname" >
						</form>

					</div>
					<div class="modal-footer">
						<div class="col-md-12">
							<button class="close" data-dismiss="modal" aria-hidden="true">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="contactModal">
		<div class="text-vertical-center" tabindex="-1" role="dialog"		aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h1 class="text-center">무엇을 도와드릴까요?!</h1>
					</div>
					<div class="modal-body">

						<form class="form col-md-12 center-block" id="contactForm"	method="POST" ACTION="/entryPage/contact.ajax">
							<div class="form-group">
								<input type="text" name="email"	 id="email"		class="form-control input-lg" placeholder="email">
							</div>
							<div class="form-group">
								<input type="text" name="subject"	 id="subject"		class="form-control input-lg" placeholder="subject">
							</div>												
							<textarea id="contain" name="contain" rows="4"></textarea>
							<div class="form-group">
								<button class="btn btn-primary btn-lg btn-block" type="button"	id="send">메일 보내기</button>
							</div>
						</form>
						

					</div>
					<div class="modal-footer">
						<div class="col-md-12">
							<button class="close" data-dismiss="modal" aria-hidden="true">Cancel</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Navigation -->

	<nav class="navbar navbar-inverse navbar-fixed-bottom"	role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> 
					<span	class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span	class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">BuzzCloud</a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"			id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a href="#">버즈클라우드?</a></li>
					<li><a href="#">어떻게 사용하죠?</a></li>
					<li><a href="#" id="contact">개발자와 연락하기</a></li>
					<li><a href="#" class="joinToBuzzCloud">버즈클라우드에 가입하기 </a></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>



	<!-- Page Content -->

	<header id="top" class="header">
		<div class="text-vertical-center">

			<h1>버즈클라우드</h1>
			<h3>
				생산적인 팀 프로젝트를 위해<br> 사용해보세요
			</h3>
			<br> <br> <input type="BUTTON" id="loginTobuzzCloud"		class="btn btn-dark btn-lg" value="버즈클라우드 로그인하기 "> <br> <br>
			<div id="naver_id_login"></div>
		</div>
	</header>
	<!-- /.container -->

	<div id="ohsnap"></div>

	<form id="alreadyLoginForm" method="GET" action="/entryPage/postAlreadyLogin.do">
	</form>  
	
	<form id="forwardPage" method="GET" action="/forwardPage.do">
		<input type="hidden"  id="page" name="page" >
	</form>  

	<!--  static library -->

	<script type="text/javascript" charset="utf-8"		src="http://code.jquery.com/jquery-latest.js"></script>
	<script type="text/javascript" charset="utf-8"		src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
	<script type="text/javascript" charset="utf-8"		src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.2.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script 	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

	<!-- custom library  -->

<script type="text/javascript" charset="utf-8"		src="/resources/lib/commanJs/commonAjax.js"></script>
	<script type="text/javascript" charset="utf-8"		src="/resources/lib/commanJs/clientSideLibrary.js"></script>
	<script type="text/javascript" charset="utf-8" src="/resources/views/entryPage/js/entryPage.js"></script>
	<script type="text/javascript" charset="utf-8" src="/resources/views/entryPage/js/form.js"></script>
	


<script>

	window.onload=function(){
		
		message = "${message}";
		if(message !="" ){
			popup_color = "${messageKind}";
			ohSnap(message,{color:popup_color});
		}
				
	}
	
</script>
	



</body>

</html>
