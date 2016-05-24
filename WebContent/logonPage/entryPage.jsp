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

    <title>The Big Picture - Start Bootstrap Template</title>



    <!-- Bootstrap Core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">

    <!-- Custom CSS -->
    <link href="/css/logonPage/mainpage.css?<?=filemtime(\'./css/readizgen.css\')?" rel="stylesheet" type="text/css">
			<link href="/css/logonPage/form.css?<?=filemtime(\'./css/readizgen.css\')?" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>


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
                        <a href="#">문제점과 건의사항</a>
                    </li>
                    <li>
                        <a href="#">개발자와 연락하기</a>
                    </li>
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
		        		<h3>생산적인 팀 프로젝트를 위해<br> 
		        					사용해보세요</h3>
    							<br><br>
    							<div id="naver_id_Logon"></div>
    							<br>
      						<input type="BUTTON" class="button" id="toJoin" name="innerJoin" value="가입하기 " onClick="innerJoin()"><br>
									<input type="BUTTON" class="button" id="toLogon"  name="innerLogon" value="로그인하기 " onClick="innerLogon()">  


						
					

		    </div>
		    
		    
    </header>
    <!-- /.container -->


<div id="LogonModal" class="modal show" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
  <div class="modal-content">
      <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
          <h1 class="text-center">Logon</h1>
      </div>
      <div class="modal-body">
          <form class="form col-md-12 center-block">
            <div class="form-group">
              <input type="text" class="form-control input-lg" placeholder="Email">
            </div>
            <div class="form-group">
              <input type="password" class="form-control input-lg" placeholder="Password">
            </div>
            <div class="form-group">
              <button class="btn btn-primary btn-lg btn-block">Sign In</button>
              <span class="pull-right"><a href="#">Register</a></span><span><a href="#">Need help?</a></span>
            </div>
          </form>
      </div>
      <div class="modal-footer">
          <div class="col-md-12">
          <button class="btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
		  </div>	
      </div>
  </div>
  </div>
</div>


<script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-2.2.3.min.js"></script>
<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
<script type="text/javascript" charset="utf-8" src="https://static.nid.naver.com/js/naverLogon_implicit-1.0.2.js"></script>
<script type="text/javascript" charset="utf-8" src="js/clientSideLibrary.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>


		
<script type="text/javascript">

		
		var mail;
		var nick;
		
		var naver = new naver_id_Logon("Vf8cYbYQv2N0c_cSv_XA", "http://114.129.211.33:8181/");
		var state = naver.getUniqState();
		naver.setState(state);
		naver.setButton(BUTTON_COLOR_GREEN, BANNER_BIG_TYPE, 40);
		naver.setStateStore();
		naver.init_naver_id_Logon();	 


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
		
		
		function setHiddenForm(){
			
			mail = naver.getProfileData('email');
			nick = naver.getProfileData('nickname');
			document.getElementsByName("email")[0].value = mail;
			document.getElementsByName("nickname")[0].value = nick;
			document.getElementsByName("password")[0].value = "";
			document.getElementsByName("reg_date")[0].value = "";
			document.getElementsByName("idType")[0].value = "naver";
			console.log("네이버 프로필 보내기 : " + mail + " , " + nick);
			document.forms["hiddenForm_oauth"].submit();
		}
		
		
		window.onload=function(){

			<%
	
			/*  logonBean으로 부터 넘어온 값을 읽어 로그인 성공이 된 경우 로그인유지를 위해 세션에 별도의 값을 저장한다. */
			
				try{
				   
					if( session.getAttribute("id") != null)
						response.sendRedirect("/main.do");
					
					if( (request.getAttribute("oauthJoin")!= null || request.getAttribute("innerJoin") != null) )	{
						%>
							alert( <%= (String)request.getAttribute("message") %> );
						<%
						
						session.setAttribute("id", "naverLogon");
						response.sendRedirect("/main.do");

				}else{
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
		
		
		
		 jQuery(function($){  
			 function layer_open(el){  		
				 $('.layer').fadeIn(); 
				 
				 var temp = $('#' + el);  
				 if (temp.outerHeight() < $(document).height() ) temp.css('margin-top', '-'+temp.outerHeight()/2+'px');  
				 else temp.css('top', '0px');  
				 if (temp.outerWidth() < $(document).width() ) temp.css('margin-left', '-'+temp.outerWidth()/2+'px');  
				 else temp.css('left', '0px');  
			 }  

			 $('#toLogon').click(function(){  
				layer_open('LogonModal');  
				return false;  
			 });  
			
			 $('#layer_close').click(function(){  
				 $('.layer').fadeOut();  
				 return false;  
			 });  
			 $(document).ready(function(){  
			 });  
		 });  
		
		
	function innerJoin(){
			
			location.href = "/innerJoin.do";
		
	}
	
	function innerLogon(){
		
		document.forms["hiddenForm_Logon"].submit();
	}
	
</script>

		<div>
			<form method="GET" ACTION="/logon.do" id="hiddenForm_Logon">
						<input type = "hidden" name = "kind" value = "inner_logon" >
						<input type="TEXT" name="input_text" value="아이디">
						<input type="TEXT" name="input_pw">
				</form>
		</div>

		<div>
		
			<form method="GET" ACTION="/join.do" id="hiddenForm_oauth">
				<input type = "hidden" name = "email" value = "" >
				<input type = "hidden" name = "nickname" value = "" >
				<input type = "hidden" name = "password" value = "" >
				<input type = "hidden" name = "idType" value = "" >
				<input type = "hidden" name = "reg_date" value = "" >
			</form>
		</div> 
		
		
	


</body>

</html>
