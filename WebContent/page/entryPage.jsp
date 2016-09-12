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

    <title>Start BuzzCloud</title>

    <!-- Bootstrap Core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

    <!-- Custom CSS -->
    <link href="/css/entryPage/mainpage.css?<?=filemtime(\'./css/readizgen.css\')?" rel="stylesheet" type="text/css">
			<link href="/css/entryPage/form.css?<?=filemtime(\'./css/readizgen.css\')?" rel="stylesheet" type="text/css"><!--  -->

    	<!--  notificator include -->
    	<script src="/include/notificator/ohsnap.js" type="text/javascript" charset="utf-8"></script>
   <link rel="stylesheet" type="text/css" href="/include/notificator/ohsnap.css" />
        
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

			<div id="mask">
			</div> 

			<div class="joinModal">
			
				<div class="text-vertical-center" tabindex="-1" role="dialog" aria-hidden="true">
				  <div class="modal-dialog">
				  <div class="modal-content">
				      <div class="modal-header">
				          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				          <h1 class="text-center">버즈클라우드에 가입하기 </h1>
				      </div>
				      <div class="modal-body">
				      
				          <form class="form col-md-12 center-block" id="innerJoinForm" method="GET" ACTION="join.do">
				            <div class="form-group">
         							 <input type="text" name="email" class="form-control input-lg" placeholder="Email">
				            </div>
				            <div class="form-group">
         							 <input type="text" name="nickname" class="form-control input-lg" placeholder="Nickname">
				            </div>
				            <div class="form-group"> 
				             <input type="password" name="password" class="form-control input-lg" placeholder="Password">
				            </div>
				             <div class="form-group"> 
				              <input type="password" name="inner_password2" class="form-control input-lg" placeholder="Rewrite Password">
				            </div>
				            <div class="form-group">
				              <button class="btn btn-primary btn-lg btn-block" onClick="innerJoin()" >가입하기</button>
				              <span class="pull-right"><a href="#">로그인하기</a></span><span><a href="#">가입하면 무엇이 좋나요?</a></span>
				            </div>
				            			
				            	<input type="hidden" name="idType" value="inner">
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

			<div class="logonModal"> 
				<div class="text-vertical-center" tabindex="-1" role="dialog" aria-hidden="true">
				  <div class="modal-dialog">
				  <div class="modal-content">
				      <div class="modal-header">
				          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				          <h1 class="text-center">버즈클라우드에 로그인</h1>
				      </div>
				      <div class="modal-body">
				      
				          <form class="form col-md-12 center-block"  id="innerLogonForm" method="GET" ACTION="innerLogon.do" >
				            <div class="form-group">
				              <input type="text" name="inner_email" class="form-control input-lg" placeholder="Email">
				            </div>
				            <div class="form-group"> 
				              <input type="password" name="inner_password" class="form-control input-lg" placeholder="Password">
				            </div>
				            <div class="form-group">
				              <button class="btn btn-primary btn-lg btn-block" onClick="innerLogon()" >로그인하기</button>
				              <span class="pull-right"><a href="#">가입하기</a></span><span><a href="#">가입하면 무엇이 좋나요?</a></span>
				            </div>
				            
				            	<input type="hidden" name="kind" value="inner">
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

    <nav class="navbar navbar-inverse navbar-fixed-bottom" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">BuzzCloud</a>
            </div>
            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li>
                        <a href="#">버즈클라우드?</a>
                    </li>                 
                    <li>
                        <a href="#">어떻게 사용하죠?</a>
                    </li>
                    <li>
                        <a href="#">문제점보고와 건의사항</a>
                    </li>
                    <li>
                        <a href="#">개발자와 연락하기</a>
                    </li>		
                     <li>
                        <a href="#" id="toJoin" >버즈클라우드에 가입하기 </a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </div>
        <!-- /.container -->
    </nav>

	<section>
			
      <div class="tn-box tn-box-color-1">
					<p>Your settings have been saved successfully!</p>
					<div class="tn-progress"></div>
				</div>
				
				<div class="tn-box tn-box-color-2">
					<p>Yummy! I just ate your settings! They were delicious!</</p>
					<div class="tn-progress"></div>
				</div>
				
				<div class="tn-box tn-box-color-3">
					<p>Look at me! I take much longer!<p>
					<div class="tn-progress"></div>
				</div>
				
			</section>


    <!-- Page Content -->
					    
					<header id="top" class="header">
				    <div class="text-vertical-center">

									<h1>버즈클라우드</h1>									
		        		<h3>생산적인 팀 프로젝트를 위해<br> 
		        					사용해보세요</h3>
    							<br><br>
    							
									<input type="BUTTON" id="toLogon" class="btn btn-dark btn-lg"  value="사이트 로그인하기 "  >  <br><br>
									<div id="naver_id_login"></div>
			    </div> 
	    </header>
    <!-- /.container -->

<div id="ohsnap"></div>

<script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-latest.js"></script>
<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
<script type="text/javascript" charset="utf-8" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.2.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>


		
<script type="text/javascript">
$('#red').on('click', function() { ohSnap('Oh Snap! You can\'t access this page!', {'color':'red'})});
$('#green').on('click', function() { ohSnap('Ahh Yeah! Your account was created.', {'color':'green'})});
$('#blue').on('click', function() { ohSnap('We are closed right now. Come back tomorrow.', {'color':'blue'})});
$('#yellow').on('click', function() { ohSnap('A yellow alert for your... yellow needs...', {color:'yellow'})});
$('#orange').on('click', function() { ohSnap('A fast one', {'color': 'orange', 'duration':'100'})});


	
		var mail;
		var nick;
		
		var naver = new naver_id_login("Vf8cYbYQv2N0c_cSv_XA", "http://114.129.211.33:8383/");
		var state = naver.getUniqState();
		naver.setState(state);
		naver.setButton(BUTTON_COLOR_GREEN, BANNER_BIG_TYPE, 40);
		naver.setStateStore();
		naver.init_naver_id_login();	 
 
		getParameter = function(param){
		    full_url=location.href;
		    
		  			//파라미터가 하나도 없을때
		    if(full_url.search("&") == -1)
		        return false;

		  			if(full_url.indexOf("#") != -1)
		    				search=full_url.split("#");
		  			else if(full_url.indexOf("?") != -1)
		  					search=full_url.split("?");
		  			else
		  					return null;
		  			
				    //해당하는 파라미터가 없을때.
		    if(search[1].indexOf(param)==(-1)){
		        
		        return "";
		        return;
				    }
		    
		    search=search[1].split("&");

		   		 //한개의 파라미터일때.
		    if(search.length<3){
		       
		        data=search[1].split("=");
		        return data[1];
				    }
		    else{
				    //여러개의 파라미터 일때.
			    var i=0;
		    		for(i=0 ; i < search.length ; i++){
		    				data = search[i].split("=");
			    			if(data[0].match(param))
			    				return data[1];
		    			}
			    	if(i==search.length)
		    				return NULL;
		    		}
		    
		}
		
		window.onload=function(){
			ohSnap('Oh Snap! I cannot process your card...', {color: 'red'});
			<%
	
			/*  logonBean으로 부터 넘어온 값을 읽어 로그인 성공이 된 경우 로그인유지를 위해 세션에 별도의 값을 저장한다. */
		
				try{ 
					//이미 로그인 했던 기록이 있다면 자동로그인 한다.   
					if( session.getAttribute("alreadyLogon") != null &&
							((String) session.getAttribute("alreadyLogon")).equals("true")){
									response.sendRedirect("/main.do");
					}
					//Oauth 콜백을 처리 후 가입결과를 반환받아 성공하였다면 로그인처리한다.
					else if( request.getAttribute("oauthJoin")!= null ||	request.getAttribute("innerJoin") != null) 	{
						%>
							alert( <%= (String)request.getAttribute("message") %> );
						<%			
						session.setAttribute("alreadyLogon", "true");
						response.sendRedirect("/main.do");

					}
					else if( request.getAttribute("innerLogon")!= null){
						if( 	((String)request.getAttribute("innerLogon")).equals("true")){
							%>
							alert( "add" );
					<%
							session.setAttribute("alreadyLogon", "true");
							response.sendRedirect("/main.do");
						}
						else if(	((String)request.getAttribute("innerLogon")).equals("false")){
							%>
									alert( <%= (String)request.getAttribute("message") %> );
							<%
														
						}
					}
					
					else{
					%>
			//네이버로그인 콜백인지 확인 
						var state = getParameter("state");
						if(state == null)
							console.log("error of urlpaser");
						var savedNaverState = naver.state;			
									
						if( state.match(savedNaverState) ){
								naver.get_naver_userprofile("setHiddenForm()");			
			}
			
			<%
				}//else
			}//try
			catch(Exception e){
				e.printStackTrace();}
					
			%>
		}
		
		 function wrapLogonModal(){
			        //화면의 높이와 너비를 구한다.
        var maskHeight = $(document).height();  
        var maskWidth = $(window).width();  

        //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
        $('#mask').css({'width':maskWidth,'height':maskHeight});  
					$('#mask').fadeTo("slow",0.8);      
						$('.logonModal').show();
			}
		 
		 function wrapJoinModal(){
			        //화면의 높이와 너비를 구한다.
				 var maskHeight = $(document).height();  
				 var maskWidth = $(window).width();  
				
				 //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
				 $('#mask').css({'width':maskWidth,'height':maskHeight});  
					$('#mask').fadeTo("slow",0.8);      
					$('.joinModal').show();
			}
		
		 
		 $(document).ready(function(){ 
							$(	'#toLogon').click(function(e){
								e.preventDefault();
								wrapLogonModal();
							});
							
							$(	'#toJoin').click(function(e){
								e.preventDefault();
								wrapJoinModal();
							});
							
							//닫기 버튼을 눌렀을 때
							$('.logonModal .close').click(function (e) {  
							    //링크 기본동작은 작동하지 않도록 한다.
							    e.preventDefault();  
							    $('#mask, .logonModal').hide();  
							});   
				
							$('.joinModal .close').click(function (e) {  
								    //링크 기본동작은 작동하지 않도록 한다.
								    e.preventDefault();  
								    $('#mask, .joinModal').hide();  
								});   
							
							//검은 wrapLogonModal 눌렀을 때
							$('#mask').click(function () {  
								    $(this).hide();  
								    $('.logonModal').hide();  
								    $('.joinModal').hide();
								});
		 })
			
		function setHiddenForm(){
			
			mail = naver.getProfileData('email');
			nick = naver.getProfileData('nickname');
			document.getElementsByName("email")[1].value = mail;
			document.getElementsByName("nickname")[1].value = nick;
			document.getElementsByName("password")[1].value = "";
			document.getElementsByName("idType")[1].value = "naver";
			document.forms["joinForm"].submit();
		}
		
		 
	function innerJoin(){
			 
				document.getElementsByName("email")[1].value = document.getElementById("inner_email").value;
				document.getElementsByName("nickname")[1].value = document.getElementById("inner_nickname").value;
				document.getElementsByName("password")[1].value = document.getElementById("inner_password").value;
				document.getElementsByName("idType")[1].value = "inner";
				document.forms["innerJoinForm"].submit();
	}
	
	function innerLogon(){

		document.forms["innerLogonForm"].submit();
	}
	
</script>

	<div>
			<form method="GET" ACTION="/join.do" id="joinForm">
				<input type = "hidden" name = "email" value = "" >
				<input type = "hidden" name = "nickname" value = "" >
				<input type = "hidden" name = "password" value = "" >
				<input type = "hidden" name = "idType" value = "" >
			</form>
	</div>
			
	
</body>

</html>
