<%@page import="com.puppyrush.buzzcloud.entity.member.MemberController"%>
<%@page import="com.puppyrush.buzzcloud.page.VerifyPage, 
com.puppyrush.buzzcloud.page.enums.enumPage, java.util.ArrayList, java.util.HashMap , 
com.puppyrush.buzzcloud.entity.member.Member , 
com.puppyrush.buzzcloud.entity.band.Band, 
com.puppyrush.buzzcloud.entity.band.BandManager" %>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumCautionKind, 
com.puppyrush.buzzcloud.entity.band.Band.BundleBand,  
com.puppyrush.buzzcloud.property.tree.Tree, 
com.puppyrush.buzzcloud.property.tree.Node"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%

	request.setCharacterEncoding("UTF-8");

	Member member = null;
 	boolean isSuccessVerify = false;
	HashMap<String,Object> results =  VerifyPage.Verify(session.getId(), enumPage.GROUP_MANAGER);
	
	if((boolean)results.get("isSuccessVerify")){
	
		member = MemberController.getInstance().getMember(session.getId());
		isSuccessVerify = true;		
		
	}else{
		isSuccessVerify = false;
		enumPage to = (enumPage)results.get("to");
		
		request.setAttribute("message",  (String)results.get("message"));
		request.setAttribute("messageKind", results.get("messageKind"));
		response.sendRedirect(to.toString());
		return;
				
	}
	
%>
    


<!doctype html>
<html><head>
    <meta charset="utf-8">
    <title>BLOCKS - Bootstrap Dashboard Theme</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link rel="stylesheet" type="text/css" href="/page/manager/bootstrap/css/bootstrap.min.css" />
    
    <link href="/page/manager/css/main.css" rel="stylesheet">
    <link href="/page/manager/css/font-style.css" rel="stylesheet">
    <link href="/page/manager/css/register.css" rel="stylesheet">
    <link href="/page/manager/css/myaccount.css" rel="stylesheet">
				<!-- ohsnap css  -->
			<link href="/include/notificator/ohsnap.css" rel="stylesheet">
		


	<script type="text/javascript" src="/page/manager/js/jquery.js"></script>    
    <script type="text/javascript" src="/page/manager/bootstrap/js/bootstrap.min.js"></script>

    <style type="text/css">
      body {
        padding-top: 60px;
      }
    </style>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    
  	<!-- Google Fonts call. Font Used Open Sans & Raleway -->
	<link href="http://fonts.googleapis.com/css?family=Raleway:400,300" rel="stylesheet" type="text/css">
  	<link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
	</head>
  <body>

	<div id="ohsnap">	</div>


  	<!-- NAVIGATION MENU -->

    <div class="navbar-nav navbar-inverse navbar-fixed-top">
        <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          
        </div> 
          <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
              <li  class="active"><a href="/page/manager/MyAccount.jsp" id="myAccount"><i class="icon-home icon-white"></i>My Account</a></li>                            
     							<li> <a href="/page/manager/GroupDashboard.jsp" id="groupDashboard"><i class="icon-home icon-white"></i>GroupDashboard</a></li>
              <li><a href="/page/manager/Group.jsp" id="groupPage"><i class="icon-home icon-white"></i>Group</a></li>
              <li><a href="/page/manager/Member.jsp" id="memberPage" ><i class="icon-user icon-white"></i>Member</a></li>
									<li><a href="/page/mainPage/MainPage.jsp"><i class="icon-user icon-white"></i>Home</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
    </div>

    <div class="container">
        <div class="row">

        	<div class="col-lg-6">
        		
        		<div class="register-info-wraper">
        			<div id="register-info">
        				<div class="cont2">
        					<div class="thumbnail">
											<img src="" id="memberImage" name="memberImage" alt="Marcel Newman" class="img-rectagle">
											 <div class="footer">
											 		<br>
									 				<form id="upload-form" method="post" action="registerMemberFace.do"	enctype="multipart/form-data">
			 		          		  <button type="submit" class="btn btn-register">등록하기</button>
				          		  </form>
			  		        </div>
							</div><!-- /thumbnail -->
							<h2 id="fullname">Marcel Newman</h2>
        				</div>
        				<div class="row">
        					<div class="col-lg-5">
        						<div class="cont3">
        							<p id="nickname"><ok>Nickname:</ok> BASICOH</p>
        							<p id="email"><ok>Mail:</ok> hola@basicoh.com</p>
        						</div>
        					</div>
        
        				</div><!-- /inner row -->
						<hr>
						<div class="cont2">
							<h2>Choose Your Option</h2>
						</div>
						<br>
							<div class="info-user2">
								<span aria-hidden="true" class="li_user fs1"></span>
								<span aria-hidden="true" class="li_settings fs1"></span>
								<span aria-hidden="true" class="li_mail fs1"></span>
								<span aria-hidden="true" class="li_key fs1"></span>
								<span aria-hidden="true" class="li_lock fs1"></span>
								<span aria-hidden="true" class="li_pen fs1"></span>
							</div>
        			</div>
        		</div>

        	</div>

        	<div class="col-sm-6 col-lg-6">
        		<div id="register-wraper">
        		    <form id="register-form" class="profileForm">
        		        <legend>User Profile</legend>
        		    
        		    	    <div class="body">
        		        	<!-- first name -->
		    		        		<label for="name">First name</label>
		    		        		<input name="fistname"id="firstname"  class="input-huge" type="text">
		        		        	<!-- last name -->
		    		        		<label for="surname">Last name</label>
		    		        		<input name="lastname" id="lastname" class="input-huge" type="text">
        		        	<!-- username -->
        		        	<label>Nickname</label>
        		        	<input name="nickname" id="nickname" class="input-huge" type="text">
        		        	<!-- email -->
        		        	<label>E-mail</label>
        		        	<input name="email" id="email" class="input-huge" type="text" readonly="readonly">
        		        
        		        </div>
        		    
        		        <div class="footer">     		           
      		            <button type="button" id="submitProfile" class="btn btn-success">변경하기</button>
        		        </div>
        		    </form>
        		</div>
        	</div>

        </div>
    </div>

	<div id="footerwrap">
      	<footer class="clearfix"></footer>
      	<div class="container">
      		<div class="row">
      			<div class="col-sm-12 col-lg-12">
      			<p><img src="images/logo.png" alt=""></p>
      			<p>Blocks Dashboard Theme - Crafted With Love - Copyright 2013</p>
      			</div>

      		</div><!-- /row -->
      	</div><!-- /container -->		
	</div><!-- /footerwrap -->  
	

	
	<script>
	
	var memberId = <%=member.getId()%>;

	window.onload=function(){
	
	       
	}
	
	
	</script>
		
				

  	<!-- ohsnap -->
		<script type="text/javascript" charset="utf-8"	src="/include/notificator/ohsnap.js"	></script>
		
		<script src="http://code.jquery.com/jquery-1.9.1.js" type="text/javascript"></script>
  <script type="text/javascript" src="/commanJs/clientSideLibrary.js"></script>
	<script type="text/javascript" src="/page/manager/js/myaccount.js"></script>
	
</body>




</html>