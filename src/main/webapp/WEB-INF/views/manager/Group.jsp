
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
{
	request.setCharacterEncoding("UTF-8");
				
}
	
%>
    

<!doctype html>
<html><head>
    <meta charset="utf-8">
    <title>BuzzCloud - MemberManager</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">


    <!-- Bootstrap Core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css"			href="//ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/themes/smoothness/jquery-ui.css">
    
			<!-- ohsnap css  -->
			<link href="/resources/lib/include/notificator/ohsnap.css" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="/resources/views/manager/bootstrap/css/bootstrap.min.css" />
		<link href="/resources/views/manager/css/main.css" rel="stylesheet">
		<link href="/resources/views/manager/css/group.css" rel="stylesheet">

    <!-- DATA TABLE CSS -->
    <link href="/resources/views/manager/css/table.css" rel="stylesheet"/>

    <!--  autocomplete -->
		<link href="/resources/lib/include/easyautocomplete/easy-autocomplete.min.css" rel="stylesheet"/>
		    
    <!-- network css  -->
    	<link href="/resources/views/manager/css/network.css" rel="stylesheet">

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
        
  	<!-- Google Fonts call. Font Used Open Sans -->
  	<link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    
  </head>
  <body>
  
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
              <ul class="nav navbar-nav" id="navPages">
              <li id="myaccount"><a href="#"><i class="icon-home icon-white"></i>My Account</a></li>                            
     							<li id="groupdashboard"> <a href="#"><i class="icon-home icon-white"></i>GroupDashboard</a></li>
              <li  class="active" id="group"><a href="#" ><i class="icon-home icon-white"></i>Group</a></li>
              <li id="member"><a href="#"><i class="icon-user icon-white"></i>Member</a></li>
									<li id="main"><a href="#"><i class="icon-user icon-white"></i>Home</a></li>
            </ul>   
          </div><!--/.nav-collapse -->
        </div>
    </div>
    
 	<form action="/makeGroup.do"  method="POST" id="makeGroupForm" name="makeGroupForm">
 	 <div class="container">
			<div class="row">
				<div class="col-sm-12 col-lg-12">
					<h4><strong>그룹 정보</strong></h4>
						<div class="cont">    
           	<input type="text" id="groupName" name="groupName" placeholder="그룹 명">
           	<input type="text" id="groupOwner" name="groupOwner" placeholder="소유자" readonly="readonly">
           	<input type="text" id="administrator" name="administrator" placeholder="관리자">
           	<input type="text" id="groupCapacity" name="groupCapacity" placeholder="할당할 용량">
           	<div class="textarea-container">
           	    <textarea id="groupContain" name="groupContain" placeholder="Message" rows="5"></textarea>
           	</div>
						</div>
				</div>
			</div>
			
			<div class="row">
				
				<div class="col-sm-3 col-lg-3">		
					<h4><strong><label>그룹원</label></strong></h4>
						<div class="container">
							      				
	 					  	 <select  class="form-control" id="groupMember" name="groupMember" size="4">
	 					  	 </select>	  	
	 					  	 <br>
	 					  	
	        
						</div>
				</div>
				
				
			<div class="col-sm-3 col-lg-3">	
					<h4><strong><label>그룹 권한</label></strong></h4>
						<div class="container">
									
							   <select class="form-control" id="bandAuthority" name="groupAuthority" size="4" multiple>	 
							   </select>
				
				   </div>
				</div>
	
				
	
					<div class="col-sm-3 col-lg-3">	
						<h4><strong><label>선택할 상위 그룹</label></strong></h4>
							<div class="container">
										
								   <select class="form-control" id="selectGroup" name="selectGroup" size="4"> 
								   </select>
					
					   </div>
					</div>
						
							<div class="col-sm-3 col-lg-3">	
						<h4><strong><label>파일 권한 </label></strong></h4>
							<div class="container">
										
								   <select class="form-control" id="fileAuthority" name="fileAuthority" size="4" multiple>
								   </select>
					
					   </div>
					</div>
						
			</div>
	
	 <div class="row" >				
		 <div class="col-sm-2 col-lg-2" id="buttonDiv">	
		 			<input type="text" id="searchMember" name="searchMember" placeholder="Search">
		 			
					</div>
					<div class="col-sm-1 col-lg-1" id="buttonDiv">
					<input type="button" class="btn"  id="removeMemberButton" name="removeMemberButton" value="삭제">
					</div>
					
				 <div class="col-sm-9 col-lg-9" id="removeDiv"></div>
  </div>

		<br><br>
		
	 	<div class="row">
	 		<div class="col-sm-4 col-lg-4"></div>
		
	 		<div class="col-sm-2 col-lg-2">	
				<input type="button" class="btn btn-success" id="makeSubmit" name="makeSubmit" value="그룹 만들기"  >
			</div>
			<div class="col-sm-2 col-lg-2">	
				<input type="button" class="btn btn-success" id="initFormButton" name="initFormButton" value="초기화"  >
			</div>
			
			<div class="col-sm-4 col-lg-4"></div>
			
		</div>
		
	</div>
</form>


<br><br><br><br>
	
	 	<div class="row">
	 		<div class="col-sm-12 col-lg-12">	
	 			<div id="mynetwork"></div>
		 	</div>
		</div>
	 <br><br>
      <!-- CONTENT -->
	<div class="row">
		<div class="col-sm-12 col-lg-12">
			<h4><strong>그룹 멤버</strong></h4>
			  <table class="display" id="memberTable">
	          <thead>
	            <tr>
	              <th>이메일</th>
	              <th>닉네임</th>
	              <th>멤버권한</th>
	              <th>파일권한</th>
	              <th>가입날짜</th>
	            </tr>
	          </thead>
	          <tbody>
	         
	          </tbody>
	         </table><!--/END First Table -->
			 <br>
			 <!--SECOND Table -->



		</div><!--/span12 -->
  </div><!-- /row -->
 
    	<br>	

      	<br>
	<!-- FOOTER -->	
	<div id="footerwrap">
      	<footer class="clearfix"></footer>
      	<div class="container">
      		<div class="row">
      			<div class="col-sm-12 col-lg-12">
      			<p><img src="/page/manager/images/logo.png" alt=""></p>
      			<p>Blocks Dashboard Theme - Crafted With Love - Copyright 2013</p>
      			</div>

      		</div><!-- /row -->
      	</div><!-- /container -->		
	</div><!-- /footerwrap -->


	<div id="ohsnap">	</div>

	<form id="managerForm" method="GET" ACTION="/managerPage/forwading.do">
		<input type="hidden" name="forwardPageName" id="forwardPageName">
	</form>

<script>


window.onload=function(){
		
		ohSnap("${message}",{'color': "${messageKind}" });
      
}

</script>


	<!-- jQuery and jQuery UI (REQUIRED) -->
	
		<script src="http://code.jquery.com/jquery-1.9.1.js" type="text/javascript"></script>
		<script	 src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/jquery-ui.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
				
		<!-- Bootstrap Core JavaScript -->
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>

    
    	<!-- ohsnap -->
		<script type="text/javascript" charset="utf-8"	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	></script>
	
<script type="text/javascript" src="/resources/lib/commanJs/commonAjax.js"></script>
		 <script type="text/javascript" src="/resources/lib/commanJs/clientSideLibrary.js"></script>
   <script type="text/javascript" src="/resources/views/manager/js/jquery.js"></script>    
   <script type="text/javascript" src="/resources/views/manager/js/admin.js"></script>


		<!-- autocomplete  -->
		<script type="text/javascript" src="/resources/lib/include/easyautocomplete/jquery.easy-autocomplete.js"></script>
		<script type="text/javascript" src="/resources/views/manager/js/group/autocomplete.js"></script>

				
		<!-- network js  -->
		<script type="text/javascript" src="/resources/lib/include/network-1.5.0/network.js"></script>
		<script type="text/javascript" src="http://www.google.com/jsapi"></script>
		<script type="text/javascript" src="/resources/views/manager/js/group/network.js"></script>
		
<script type="text/javascript" src="/resources/views/manager/js/group/group.js"></script>

</body>

</html>