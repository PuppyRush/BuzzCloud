<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html class="full" lang="ko">

<!-- Make sure the <html> tag is set to the .full CSS class. Change the background image in the full.css file. -->

<head>

			<meta http-equiv = "Content-Type" content="text/html; charset=utf-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>버즈클라우드에 오신걸 환영합니다!</title>

    <!-- Bootstrap Core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    
    <!-- Custom CSS -->
			<link href="/page/issuePage/css/message.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

<script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>




<!-- Page Content -->
<div class="container">
  <div class="row">

    <div class="col-xs-12 page-header">
      <h1>
        <span class="glyphicon glyphicon-star"></span>
        Message Wall
      </h1>

      <p class="lead">Send messages to our server and get updates here.</p>
    </div>

    <div class="col-xs-12 col-md-4">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">
            <span class="glyphicon glyphicon-pencil"></span>
            Send Message
          </h3>
        </div>
        <div class="panel-body">
          <form role="form" id="message-form">
            <div class="form-group">
              <textarea class="form-control" name="m" id="message"
                        placeholder="Enter message"></textarea>
            </div>
            <button id="message-send" type="submit" class="btn btn-primary">
              Submit
            </button>
            <button id="message-reset" type="button" class="btn btn-info">
              Clear
            </button>
          </form>

          <p id="sent-result" class="alert alert-success"></p>
          <p id="sent-fail" class="alert alert-danger"></p>

        </div>
      </div>
    </div>

    <div class="col-xs-12 col-md-8">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">
            <span class="glyphicon glyphicon-list-alt"></span>
            Messages
          </h3>
        </div>
        <div class="panel-body">
          <ul class="list-group" id="message-container">
            <li class="list-group-item">Placeholder message</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
  <!-- /.row -->
</div>
<!-- /.container -->


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
			<div class="collapse navbar-collapse"	id="bs-example-navbar-collapse-1">
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



<script type="text/javascript" src="/page/issuePage/js/message.js"></script>




</body>
</html>