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

<title>버즈클라우드에 오신걸 환영합니다!</title>
		
		<!-- Bootstrap Core CSS -->
		<link
			href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
			rel="stylesheet"
			integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
			crossorigin="anonymous">
		
		
<!-- Section CSS -->
		
		<!-- custom CSS -->
				<link rel="stylesheet" type="text/css"			href="/page/fileBrowser/css/filebrowser.css">
		
		<!-- jQuery UI (REQUIRED) -->
		<link rel="stylesheet" type="text/css"			href="//ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/themes/smoothness/jquery-ui.css">
		
		<!-- elFinder CSS (REQUIRED) -->
		<link rel="stylesheet" type="text/css"
			href="/include/elFinder-2.1.16/css/elfinder.min.css">		<!-- 	<link rel="stylesheet" type="text/css" href="css/theme.css"> -->
		<link rel="stylesheet" type="text/css" media="screen"			href="/include/elFinder-2.1.16/theme_win10/css/theme.css">
		
		<!-- Section JavaScript -->
		<!-- jQuery and jQuery UI (REQUIRED) -->
		<script type="text/javascript" charset="utf-8"			src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript" charset="utf-8"			src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
		
		<script			src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/jquery-ui.min.js"></script>
		<link rel="stylesheet" type="text/css"			href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/themes/smoothness/jquery-ui.css">
		
		
		<!-- elFinder JS (REQUIRED) -->
		<script src="/include/elFinder-2.1.16/js/elfinder.min.js"></script>
		
		
		<!-- Bootstrap Core JavaScript -->
		<script
			src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		


<!-- GoogleDocs Quicklook plugin for GoogleDrive Volume (OPTIONAL) -->
<!--<script src="js/extras/quicklook.googledocs.js"></script>-->

<!-- elFinder translation (OPTIONAL) -->
<!--<script src="js/i18n/elfinder.ru.js"></script>-->


<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>



	<nav class="navbar navbar-inverse navbar-fixed-bottom"
		role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">BuzzCloud</a>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a href="#">버즈클라우드?</a></li>
					<li><a href="#">어떻게 사용하죠?</a></li>
					<li><a href="#">문제점보고와 건의사항</a></li>
					<li><a href="#">개발자와 연락하기</a></li>
					<li><a href="#" class="joinToBuzzCloud">버즈클라우드에 가입하기 </a></li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>


	<div id="elfinder"></div>

	<script type="text/javascript" charset="utf-8">
			// Documentation for client options:
			// https://github.com/Studio-42/elFinder/wiki/Client-configuration-options
			$(document).ready(function() {
				$('#elfinder').elfinder({
					url : '/elfinder/servlet-connector'  // connector URL (REQUIRED)
					// , lang: 'ru'                    // language (OPTIONAL)
				});
			});
		</script>


</body>
</html>