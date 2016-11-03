<%@page import="entity.member.MemberController"%>
<%@page import="page.VerifyPage, page.enums.enumPage, java.util.ArrayList, java.util.HashMap , entity.member.Member , entity.band.Band, entity.band.BandManager" %>
<%@page import="page.enums.enumCautionKind, entity.band.Band.BundleBand,  property.tree.Tree, property.tree.Node"%>

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
    <title>BuzzCloud - MemberManager</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">


    <!-- Bootstrap Core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css"			href="//ajax.googleapis.com/ajax/libs/jqueryui/1.12.0/themes/smoothness/jquery-ui.css">
    
    		    			<!-- context menu css  -->
    <link href="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.css" rel="stylesheet" type="text/css"/>
		    
    
			<!-- ohsnap css  -->
			<link href="/include/notificator/ohsnap.css" rel="stylesheet">
		

 		
 		
    <!-- Le styles -->
    <link rel="stylesheet" type="text/css" href="/page/manager/bootstrap/css/bootstrap.min.css" />
		<link href="/page/manager/css/main.css" rel="stylesheet">
		<link href="/page/manager/css/group.css" rel="stylesheet">

    <!-- DATA TABLE CSS -->
    <link href="/page/manager/css/table.css" rel="stylesheet">

    <!--  autocomplete -->
		<link rel="stylesheet" type="text/css" href="/autocomplete/jquery/jquery.autocomplete.css" />
		    

    
    <style type="text/css">
      body {
        padding-top: 60px;
      }
    </style>

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
            <ul class="nav navbar-nav">
              <li><a href="/page/manager/MyAccount.jsp"><i class="icon-home icon-white"></i>My Account</a></li>                            
              <li><a href="/page/manager/GroupDashboard.jsp"><i class="icon-home icon-white"></i>GroupDashboard</a></li>
              <li class="active"><a href="/page/manager/Group.jsp"><i class="icon-home icon-white"></i>Group</a></li>
              <li><a href="/page/manager/Member.jsp"><i class="icon-user icon-white"></i>Member</a></li>
									<li><a href="/page/mainPage/MainPage.jsp"><i class="icon-user icon-white"></i>Home</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
    </div>
    
 	<form action="/makeGroup.do"  method="POST" id="makeGroupForm" name="makeGroupForm">
 	 <div class="container">
			<div class="row">
				<div class="col-sm-3 col-lg-3">
					<h4><strong>새로운 그룹 정보</strong></h4>
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
			
			<div class="col-sm-2 col-lg-2">		
				<h4><strong><label>그룹원</label></strong></h4>
					<div class="container">
						      				
 					  	 <select  class="form-control" id="groupMember" name="groupMember" size="5" multiple>
 					  	 </select>	  					
 					  	 <br>
        			<input type="text" id="searchMember" name="searchMember" placeholder="Name">
     			  	   
        
					</div>
			</div>
			
			
		<div class="col-sm-3 col-lg-3">	
				<h4><strong><label>그룹 권한</label></strong></h4>
					<div class="container">
								
						   <select class="form-control" id="groupAuthority" name="groupAuthority" size="5" multiple>
				 
						   </select>
			
			   </div>
			</div>
		
			
				<div class="col-sm-3 col-lg-3">	
					<h4><strong><label>선택할 그룹</label></strong></h4>
						<div class="container">
									
							   <select class="form-control" id="selectGroup" name="selectGroup" size="6">
					 
							   </select>
				
				   </div>
				</div>
			</div>
		
		
	 	<div class="row">
	 		<div class="col-sm-12 col-lg-12">	
				<input type="button" id="makeSubmit" name="makeSubmit" value="그룹 만들기"  >
			</div>
		</div>
	</div>
</form>


<br><br>

	 
      <!-- CONTENT -->
	<div class="row">
		<div class="col-sm-12 col-lg-12">
			<h4><strong>Basic Table</strong></h4>
			  <table class="display">
	          <thead>
	            <tr>
	              <th>그룹 명</th>
	              <th>소유주</th>
	              <th>관리자</th>
	              <th>할당된 용량</th>
	              <th>가입자 수</th>
	            </tr>
	          </thead>
	          <tbody>
	            <tr class="odd">
	              <td>Queen</td>
	              <td>Brian May</td>
	              <td>Guitar</td>
	              <td class="center"> 7</td>
	              <td class="center">9</td>
	            </tr>
	            <tr class="even">
	              <td>Queen</td>
	              <td>Roger Taylor</td>
	              <td>Drums</td>
	              <td class="center">5</td>
	              <td class="center">7</td>
	            </tr>
	            <tr class="odd">
	              <td>Beatles</td>
	              <td>Paul McCartney</td>
	              <td>Guitar &amp; Piano</td>
	              <td class="center">8</td>
	              <td class="center">9</td>
	            </tr>
	            <tr class="even">
	              <td>Adele</td>
	              <td>Adele</td>
	              <td>None</td>
	              <td class="center">8</td>
	              <td class="center">8</td>
	            </tr>
	            <tr class="odd">
	              <td>Britney Spears</td>
	              <td>Britney Spears</td>
	              <td>None</td>
	              <td class="center">7</td>
	              <td class="center">-5</td>
	            </tr>
	          </tbody>
	         </table><!--/END First Table -->
			 <br>
			 <!--SECOND Table -->


		<h4><strong>Data Table</strong></h4>

		<table class="display" id="dt1">
        <thead>
          <tr>
            <th>Rendering engine</th>
            <th>Browser</th>
            <th>Platform(s)</th>
            <th>Engine version</th>
            <th>CSS grade</th>
          </tr>
        </thead>
        <tbody>
          <tr class="odd gradeX">
            <td>Trident</td>
            <td>Internet Explorer 4.0</td>
            <td>Win 95+</td>
            <td class="center"> 4</td>
            <td class="center">X</td>
          </tr>
       
        </tbody>
      </table><!--/END SECOND TABLE -->
	
		</div><!--/span12 -->
      </div><!-- /row -->
     </div> <!-- /container -->
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

	<!-- jQuery and jQuery UI (REQUIRED) -->
	
		<script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-latest.js"></script>
			<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
		<script	 src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.0/jquery-ui.min.js"></script>
		
				
			<!-- Bootstrap Core JavaScript -->
			<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>

    
    	<!-- ohsnap -->
		<script type="text/javascript" charset="utf-8"	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	></script>
		
<!-- 
    <script type="text/javascript" src="/page/manager/js/jquery.js"></script>    
    <script type="text/javascript" src="/page/manager/js/admin.js"></script>
     -->

	 		<!-- context menu js  -->
    <!-- <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.ui.position.min.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/js/main.js" type="text/javascript"></script>
    <script type="text/javascript" src="/page/manager/js/contextMenu.js"></script>
     -->
  

		<!-- autocomplete  -->
		
		<script src="http://code.jquery.com/jquery-1.9.1.js" type="text/javascript"></script>
		<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js" type="text/javascript"></script>
    <script type="text/javascript" src="/page/manager/js/group.js"></script>
    


<script>



window.onload=function(){

		$("#groupOwner").val("<%=member.getNickname()%>");
		$("#administrator").val("<%=member.getNickname()%>");

	 $.ajax({
	    url:'./ajax/getGroupMembers.jsp',
	    data: { memberId: <%=member.getId()%>},
	    dataType:'json',
	    success:function(data){
	  	  groupMembers = data;
	 	    for (var key in data) {		  
				   $("#groupMember").append('<option>'+key);	   				   		 	
	 			 }
           }
       })
       
     $.ajax({
	    url:'./ajax/getSubGroups.jsp',
	    data: { memberId: <%=member.getId()%>},
	    dataType:'json',
	    success:function(data){
	  	  groups = data;
	 	    for (var key in data) {		   
	  		  $("#selectGroup").append('<option>'+key);	   		 	
	 			 }
           }
      })
       
    $.ajax({
	    url:'./ajax/getMaxCapacity.jsp',
	    data: { memberId: <%=member.getId()%>},
	    dataType:'json',
	    success:function(data){
	    	 var cap = Number(data.capacity)/(1024*1024);
	  	  $("#groupCapacity").attr("placeholder","할당할 용량 ( 가능한 최대 용량 :  " + cap+" )" );
	 	   
           }
       })
       
   $.ajax({
	    url:'./ajax/getBandAuthority.jsp',
	    data: { memberId: <%=member.getId()%>},
	    dataType:'json',
	    success:function(data){
		    for (var key in data) {		   
			  		$("#groupAuthority").append('<option>'+key);	   		 	
				 }
          }
      })
      
}

</script>

</body>

</html>