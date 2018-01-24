
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.puppyrush.buzzcloud.page.enums.enumPage"%>
<%

	request.setCharacterEncoding("UTF-8");
	
%>

<!doctype html>
<html><head>
    <meta charset="utf-8">
    <title>BuzzCloud - GroupManager</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Carlos Alvarez - Alvarez.is">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
			
			<link href="/resources/bower_components/highcharts/css/highcharts.css" rel="stylesheet">
			<link href="/resources/lib/include/notificator/ohsnap.css" rel="stylesheet">
			<link href="/resources/views/manager/css/groupDashboard/form.css" rel="stylesheet">
    <link href="/resources/views/manager/css/groupDashboard/main.css?<?=filemtime(\'./css/readizgen.css\')?"" rel="stylesheet">
    <link href="/resources/views/manager/css/groupDashboard/font-style.css" rel="stylesheet">
    <link href="/resources/views/manager/css/groupDashboard/flexslider.css" rel="stylesheet">
    <link href="/resources/views/manager/css/groupDashboard/groupDashboard.css" rel="stylesheet">


  	<!-- Google Fonts call. Font Used Open Sans & Raleway -->
	<link href="http://fonts.googleapis.com/css?family=Raleway:400,300" rel="stylesheet" type="text/css">
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
    							<li class="active" id="groupdashboard"> <a href="#"><i class="icon-home icon-white"></i>GroupDashboard</a></li>
              <li id="group"><a href="#" ><i class="icon-home icon-white"></i>Group</a></li>
              <li id="member"><a href="#"><i class="icon-user icon-white"></i>Member</a></li>
									<li id="main"><a href="#"><i class="icon-user icon-white"></i>Home</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
    </div>


		
	<div id="mask"></div>

	<div class="groupModal">

		<div class="text-vertical-center" tabindex="-1" role="dialog"	 aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"	aria-hidden="true">×</button>
						<h3 class="text-center">그룹 가입하기</h3>
					</div>
					<div class="modal-body">

						<form class="form col-md-12 center-block" id="innerJoinForm"	>
																	
						
									
										<div class="form-group">
											<input type="text" name="bandAdminName" id="bandAdminName" title="관리자 명" class="form-control input-md"  readonly>
										</div>
									
										<div class="form-group">
											<input type="text" name="bandCreatedDate" id="bandCreatedDate" class="form-control input-md"	readonly>
										</div>
									
										<div class="form-group">
											<input type="text" name="subBandNumber" id="subBandNumber" class="form-control input-md"	readonly>
										</div>
										
										<div class="form-group">
											<input type="text" name="bandMembersAll" id="bandMembersAll" class="form-control input-md"	readonly>
										</div>
										
										
											<textarea id="bandContain" name="bandContain" placeholder="그룹 설명" rows="3" readonly ></textarea>
																												
										
										<div class="form-group">
											<button class="btn btn-primary btn-md btn-block" id="joinBandButton" type="button"	>닫기 </button>
										
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



    <div class="container">

	  <!-- FIRST ROW OF BLOCKS -->     
      <div class="row">

 
      <!-- GROUP PROFILE BLOCK -->
        <div class="col-sm-3 col-lg-3">
      		<div class="dash-unit">
      		<dtitle>소유 그룹 목록</dtitle>
      		<hr>
      		<div class="framemail">
    				<div class="window">
			        <ul class="mail" id="groups">
	
			        </ul>
    			</div>
    			  					
			</div>
			<br>
			<div id="groupPageNumber" class="col-centered">
	    					
 			</div>
		</div><!-- /dash-unit -->
		
        </div>


 <div class="col-sm-3 col-lg-3">
       <!-- MAIL BLOCK -->
      		<div class="dash-unit">
      		<dtitle>그룹 가입 대기열</dtitle>
      		<hr>
      		<div class="framemail">
    				<div class="window">
			        <ul class="mail" id="joins">
			           
			       
			        </ul>
    			</div>
			</div>
		</div><!-- /dash-unit -->
    </div><!-- /span3 -->


      <!-- DONUT CHART BLOCK -->
        <div class="col-sm-3 col-lg-3">
      		<div class="dash-unit">
		  		<dtitle>사용중인 그룹용량</dtitle>
		  		<hr>
	        	<div id="usage"></div>
	        	<h2 id="usageText">usage : 65%</h2>
						</div>
        </div>
        
        <div class="col-sm-3 col-lg-3">

      <!-- LOCAL TIME BLOCK -->
      		<div class="half-unit">
	      		<dtitle>서버 가동 시간</dtitle>
	      		<hr>
		      		<div class="clockcenter">
			      		<div id="createdDateGap">12:45:25</div>
		      		</div>
			</div>

      <!-- SERVER UPTIME -->
			<div class="half-unit">
	      		<dtitle>서버가 생성된 시간</dtitle>
	      		<hr>
		      		<div class="clockcenter">
			      		<div id="createdDate">12:45:25</div>
		      		</div>
			</div>

        </div>
      </div><!-- /row -->
      
      
	  <!-- SECOND ROW OF BLOCKS -->     
      <div class="row">
      

	  <!-- GRAPH CHART - lineandbars.js file -->     
        <div class="col-sm-3 col-lg-3">
      		<div class="dash-unit">
      		<dtitle>Other Information</dtitle>
      		<hr>
			    <div class="section-graph">
			      <div id="importantchart"></div>
			      <br>
			      <div class="graph-info">
			        <i class="graph-arrow"></i>
			        <span class="graph-info-big">634.39</span>
			        <span class="graph-info-small">+2.18 (3.71%)</span>
			      </div>
			    </div>
			</div>
        </div>

	  <!-- LAST MONTH REVENUE -->     
        <div class="col-sm-3 col-lg-3">
      		<div class="dash-unit">
	      		<dtitle>Last Month Revenue</dtitle>
	      		<hr>
	      		<div class="cont">
					<p><bold>$879</bold> | <ok>Approved</ok></p>
					<br>
					<p><bold>$377</bold> | Pending</p>
					<br>
					<p><bold>$156</bold> | <bad>Denied</bad></p>
					<br>
					<p><img src="/resources/views/manager/images/up-small.png" alt=""> 12% Compared Last Month</p>

				</div>

			</div>
        </div>
        
	  <!-- 30 DAYS STATS - CAROUSEL FLEXSLIDER -->     
        <div class="col-sm-3 col-lg-3">
      		<div class="dash-unit">
	      		<dtitle>Last 30 Days Stats</dtitle>
	      		<hr>
	      		<br>
	      		<br>
	            <div class="flexslider">
					<ul class="slides">
						<li><img src="/resources/views/manager/images/slide01.png" alt="slider"></li>
						<li><img src="/resources/views/manager/images/slide02.png" alt="slider"></li>
					</ul>
            </div>
				<div class="cont">
					<p>StatCounter Information</p>
				</div>   
			</div>
        </div>
      </div><!-- /row -->
     
 
	  <!-- THIRD ROW OF BLOCKS -->     
      <div class="row">
      	<div class="col-sm-3 col-lg-3">
	  
	  <!-- BARS CHART - lineandbars.js file -->     
      		<div class="half-unit">
	      		<dtitle>Stock Information</dtitle>
	      		<hr>
	      		<div class="cont">
	      		 <div class="info-aapl">
			        <h4>AAPL</h4>
			        <ul>
			          <li><span class="orange" style="height: 37.5%"></span></li>
			          <li><span class="orange" style="height: 47.5%"></span></li>
			          <li><span class="orange" style="height: 70%"></span></li>
			          <li><span class="orange" style="height: 85%"></span></li>
			          <li><span class="green" style="height: 75%"></span></li>
			          <li><span class="green" style="height: 50%"></span></li>
			          <li><span class="green" style="height: 15%"></span></li>
			        </ul>
			      </div>
			      </div>
      		</div>

	  <!-- TO DO LIST -->     
      		<div class="half-unit">
	      		<dtitle>To Do List</dtitle>
	      		<hr>
	      		<div class="cont">
					<p><bold>13</bold> | Pending Tasks</p>
				</div>
		             <div class="progress">
				        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width:60%;"><span class="sr-only">60% Complete</span>
					        
				        </div>
				     </div>
      		</div>
      	</div>

      	<div class="col-sm-3 col-lg-3">

	  <!-- LIVE VISITORS BLOCK -->     
      		<div class="half-unit">
	      		<dtitle>Live Visitors</dtitle>
	      		<hr>
	      		<div class="cont">
      			<p><bold>388</bold></p>
      			<p><img src="/resources/views/manager/images/up-small.png" alt=""> 412 Max. | <img src="/resources/views/manager/images/down-small.png" alt=""> 89 Min.</p>
	      		</div>
      		</div>
      		
	  <!-- PAGE VIEWS BLOCK -->     
      		<div class="half-unit">
	      		<dtitle>Page Views</dtitle>
	      		<hr>
	      		<div class="cont">
      			<p><bold>145.0K</bold></p>
      			<p><img src="/resources/views/manager/images/up-small.png" alt=""> 23.88%</p>
	      		</div>
      		</div>
      	</div>

      	<div class="col-sm-3 col-lg-3">
	  <!-- TOTAL SUBSCRIBERS BLOCK -->     
      		<div class="half-unit">
	      		<dtitle>Total Subscribers</dtitle>
	      		<hr>
	      		<div class="cont">
      			<p><bold>14.744</bold></p>
      			<p>98 Subscribed Today</p>
	      		</div>
      		</div>
      		
	  <!-- FOLLOWERS BLOCK -->     
      		<div class="half-unit">
	      		<dtitle>Twitter Followers</dtitle>
	      		<hr>
	      		<div class="cont">
      			<p><bold>17.833 Followers</bold></p>
      			<p>SomeUser</p>
	      		</div>
      		</div>
      	</div>

	  <!-- LATEST NEWS BLOCK -->     
      	<div class="col-sm-3 col-lg-3">
      		<div class="dash-unit">
	      		<dtitle>Latest News</dtitle>
	      		<hr>
				<div class="info-user">
					<span aria-hidden="true" class="li_news fs2"></span>
				</div>
				<br>
      			<div class="text">
      				<p><b>Alvarez.is:</b> A beautiful new Dashboard theme has been realised by Carlos Alvarez. Please, visit <a href="http://alvarez.is">Alvarez.is</a> for more details.</p>
      				<p><grey>Last Update: 5 minutes ago.</grey></p>
      			</div>
      		</div>
      	</div>
      </div><!-- /row -->
      
	  <!-- FOURTH ROW OF BLOCKS -->     
	<div class="row">
	
	 
	  <!-- NOTIFICATIONS BLOCK -->     
		<div class="col-sm-3 col-lg-3">
			<div class="dash-unit">
	      		<dtitle>Notifications</dtitle>
	      		<hr>
	      		<div class="info-user">
					<span aria-hidden="true" class="li_bubble fs2"></span>
				</div>
	      		<div class="cont">
	      			<p><button class="btnnew noty" data-noty-options="{&quot;text&quot;:&quot;This is a success notification&quot;,&quot;layout&quot;:&quot;topRight&quot;,&quot;type&quot;:&quot;success&quot;}">Top Right</button></p>
	      			<p><button class="btnnew noty" data-noty-options="{&quot;text&quot;:&quot;This is an informaton notification&quot;,&quot;layout&quot;:&quot;topLeft&quot;,&quot;type&quot;:&quot;information&quot;}">Top Left</button></p>
	      			<p><button class="btnnew noty" data-noty-options="{&quot;text&quot;:&quot;This is an alert notification with fade effect.&quot;,&quot;layout&quot;:&quot;topCenter&quot;,&quot;type&quot;:&quot;alert&quot;,&quot;animateOpen&quot;: {&quot;opacity&quot;: &quot;show&quot;}}">Top Center (fade)</button></p>
	      		</div>
			</div>
		</div>

	  <!-- SWITCHES BLOCK -->     
		<div class="col-sm-3 col-lg-3">
			<div class="dash-unit">
	      		<dtitle>Switches</dtitle>
	      		<hr>
	      		<div class="info-user">
					<span aria-hidden="true" class="li_params fs2"></span>
				</div>
				<br>
				<div class="switch">
					<input type="radio" class="switch-input" name="view" value="on" id="on" checked="">
					<label for="on" class="switch-label switch-label-off">On</label>
					<input type="radio" class="switch-input" name="view" value="off" id="off">
					<label for="off" class="switch-label switch-label-on">Off</label>
					<span class="switch-selection"></span>
				</div>
				<div class="switch switch-blue">
					<input type="radio" class="switch-input" name="view2" value="week2" id="week2" checked="">
					<label for="week2" class="switch-label switch-label-off">Week</label>
					<input type="radio" class="switch-input" name="view2" value="month2" id="month2">
					<label for="month2" class="switch-label switch-label-on">Month</label>
					<span class="switch-selection"></span>
				</div>
				
				<div class="switch switch-yellow">
					<input type="radio" class="switch-input" name="view3" value="yes" id="yes" checked="">
					<label for="yes" class="switch-label switch-label-off">Yes</label>
					<input type="radio" class="switch-input" name="view3" value="no" id="no">
					<label for="no" class="switch-label switch-label-on">No</label>
					<span class="switch-selection"></span>
				</div>
			</div>
		</div>

	  <!-- GAUGE CHART BLOCK -->     
		<div class="col-sm-3 col-lg-3">
			<div class="dash-unit">
	      		<dtitle>Gauge Chart</dtitle>
	      		<hr>
	      		<div class="info-user">
					<span aria-hidden="true" class="li_lab fs2"></span>
				</div>
				<canvas id="canvas" width="300" height="300">
			</canvas></div>
		</div>
	
	</div><!--/row -->     
      
 	  <!-- FOURTH ROW OF BLOCKS -->     
		<div class="row">

 	  <!-- ACCORDION BLOCK -->     
			<div class="col-sm-3 col-lg-3">
				<div class="dash-unit">
	      		<dtitle>Accordion</dtitle>
	      		<hr>
					<div class="accordion" id="accordion2">
		                <div class="accordion-group">
		                    <div class="accordion-heading">
		                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
		                        Collapsible Group Item #1
		                        </a>
		                    </div>
		                    <div id="collapseOne" class="accordion-body collapse in">
		                        <div class="accordion-inner">
								Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla iaculis mattis lorem.                        
								</div>
		                    </div>
		                </div>
		
		                <div class="accordion-group">
		                    <div class="accordion-heading">
		                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
		                        Collapsible Group Item #2
		                        </a>
		                    </div>
		                    <div id="collapseTwo" class="accordion-body collapse">
		                        <div class="accordion-inner">
								Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla iaculis mattis lorem.                      
								</div>
		                    </div>
		                </div>
		
		                 <div class="accordion-group">
		                    <div class="accordion-heading">
		                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseThree">
		                        Collapsible Group Item #3
		                        </a>
		                    </div>
		                    <div id="collapseThree" class="accordion-body collapse">
		                        <div class="accordion-inner">
								Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla iaculis mattis lorem.                        
		                        </div>
		                    </div>
		                </div>
		            </div><!--/accordion -->
				</div>
			</div>
			
			<div class="col-sm-3 col-lg-3">

 	  		<!-- LAST USER BLOCK -->     
				<div class="half-unit">
	      		<dtitle>Last Registered User</dtitle>
	      		<hr>
	      			<div class="cont2">
	      				<img src="/resources/views/manager/images/user-avatar.jpg" alt="">
	      				<br>
	      				<br>
	      				<p>Paul Smith</p>
	      				<p><bold>Liverpool, England</bold></p>
	      			</div>
				</div>
				
 	  		<!-- MODAL BLOCK -->     
				<div class="half-unit">
	      		<dtitle>Modal</dtitle>
	      		<hr>
		            <div class="cont">
		                <a href="#myModal" role="button" class="btnnew" data-toggle="modal">Example Modal Window</a>
		            </div>
				</div>
			</div>
			<!-- Modal -->
			  <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			    <div class="modal-dialog">
			      <div class="modal-content">
			        <div class="modal-header">
			          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			          <h4 class="modal-title">Modal title</h4>
			        </div>
			        <div class="modal-body">
			          ...
			        </div>
			        <div class="modal-footer">
			          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			          <button type="button" class="btn btn-primary">Save changes</button>
			        </div>
			      </div><!-- /.modal-content -->
			    </div><!-- /.modal-dialog -->
			  </div><!-- /.modal -->
 	  		<!-- FAST CONTACT BLOCK -->     
			<div class="col-sm-3 col-lg-3">
				<div class="dash-unit">
	      		<dtitle>Fast Contact</dtitle>
	      		<hr>
	      		<div class="cont">
                	<form action="#get-in-touch" method="POST" id="contact">
                    	<input type="text" id="contactname" name="contactname" placeholder="Name">
                    	<input type="text" id="email" name="email" placeholder="Email">
                    	<div class="textarea-container"><textarea id="message" name="message" placeholder="Message"></textarea></div>
                    	<input type="submit" id="submit" name="submit" value="Send">
                    </form>
				</div>
				</div>
			</div>

 	  		<!-- INFORMATION BLOCK -->     
			<div class="col-sm-3 col-lg-3">
				<div class="dash-unit">
	      		<dtitle>Block Dashboard</dtitle>
	      		<hr>
	      		<div class="info-user">
					<span aria-hidden="true" class="li_display fs2"></span>
				</div>
				<br>
				<div class="text">
				<p>Block Dashboard created by Basicoh.</p>
				<p>Special Thanks to Highcharts, Linecons and Bootstrap for their amazing tools.</p>
				</div>

				</div>
			</div>

		</div><!--/row -->
     
      
      
	</div> <!-- /container -->
	<div id="footerwrap">
      	<footer class="clearfix"></footer>
      	<div class="container">
      		<div class="row">
      			<div class="col-sm-12 col-lg-12">
      			<p><img src="/resources/views/manager/images/logo.png" alt=""></p>
      			<p>Blocks Dashboard Theme - Crafted With Love - Copyright 2013</p>
      			</div>

      		</div><!-- /row -->
      	</div><!-- /container -->		
	</div><!-- /footerwrap -->



<div id="ohsnap"></div>

	<form id="managerForm" method="GET" ACTION="/managerPage/forwading.do">
		<input type="hidden" name="forwardPageName" id="forwardPageName">
	</form>
	

	<script type="text/javascript" src="/resources/bower_components/jquery/jquery.js?<?=filemtime(\'./css/readizgen.css\')?"></script>    
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

	<script type="text/javascript" src="/resources/views/manager/js/lineandbars.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
    
	
	<script type="text/javascript" src="/resources/views/manager/js/gauge.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	
	<!-- NOTY JAVASCRIPT -->
	<script type="text/javascript" src="/resources/views/manager/js/noty/jquery.noty.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	<script type="text/javascript" src="/resources/views/manager/js/noty/layouts/top.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	<script type="text/javascript" src="/resources/views/manager/js/noty/layouts/topLeft.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	<script type="text/javascript" src="/resources/views/manager/js/noty/layouts/topRight.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	<script type="text/javascript" src="/resources/views/manager/js/noty/layouts/topCenter.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	
	<!-- You can add more layouts if you want -->
	<script type="text/javascript" src="/resources/views/manager/js/noty/themes/default.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
    <!-- <script type="text/javascript" src="assets/js/dash-noty.js"></script> This is a Noty bubble when you init the theme-->
	<script type="text/javascript" src="/resources/bower_components/highcharts/highcharts.js"></script>
	<script src="/resources/views/manager/js/jquery.flexslider.js" type="text/javascript"></script>

 <script type="text/javascript" src="/resources/views/manager/js/admin.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
	<script type="text/javascript" src="/resources/views/issuePage/js/message.js?<?=filemtime(\'./css/readizgen.css\')?"></script>


    	<!-- ohsnap -->
		<script type="text/javascript" charset="utf-8"	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	></script>
	
		<script type="text/javascript" src="/resources/lib/commanJs/commonAjax.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		<script type="text/javascript" src="/resources/lib/commanJs/formValidator.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		 <script type="text/javascript" src="/resources/lib/commanJs/clientSideLibrary.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		 <script type="text/javascript" src="/resources/views/manager/js/groupDashboard/charts.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		<script type="text/javascript" src="/resources/views/manager/js/groupDashboard/groupDashboard.js?<?=filemtime(\'./css/readizgen.css\')?"></script>
		<script type="text/javascript" src="/resources/views/manager/js/groupDashboard/form.js?<?=filemtime(\'./css/readizgen.css\')?"></script>

<script>
	var bandId = ${bandId};

	window.onload=function(){		
		if(!"${message}"=="")
			ohSnap("${message}",{'color': "${messageKind}" });
		verifyPage("<%=(String)enumPage.MAIN.name()%>");
	}
</script>

</body>


</html>