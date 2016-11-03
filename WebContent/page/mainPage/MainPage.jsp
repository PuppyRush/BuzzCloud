<%@page import="entity.member.MemberController"%>
<%@ page import="page.VerifyPage, page.enums.enumPage, java.util.ArrayList, java.util.HashMap , entity.member.Member , entity.band.Band, entity.band.BandManager" %>
<%@ page import="page.enums.enumCautionKind, entity.band.Band.BundleBand,  property.tree.Tree, property.tree.Node"%>	
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
  
<%

	request.setCharacterEncoding("UTF-8");

	Member member = null;
 	boolean isSuccessVerify = false;
	HashMap<String,Object> results =  VerifyPage.Verify(session.getId(), enumPage.MAIN);
	
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
			<link href="/page/mainPage/css/main.css?<?=filemtime(\'./css/readizgen.css\')?" rel="stylesheet" type="text/css">
			
			<!-- ohsnap css  -->
			<link href="/include/notificator/ohsnap.css" rel="stylesheet">
		
			<!-- context menu css  -->
    <link href="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.css" rel="stylesheet" type="text/css"/>
		    

			<script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-latest.js"></script>
			<script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
			
			<!-- Bootstrap Core JavaScript -->
			<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js?<?=filemtime(\'./css/readizgen.css\')?"></script>


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

	<div id="mynetwork"></div>
			
	
	<div class="container">

		<div class="row">
	    <div class="col-md-11"></div>
					<div id="context-menu-icon" class="col-md-1">
						<img src="/image/icon/option_blue.jpg"	class="img-fluid context-menu-one btn btn-neutral">
					</div>
		</div>
	</div>

	<div id="ohsnap"></div>

	<form id="groupForm" method="GET" ACTION="/viewFileBrowser.do">
		<input type="hidden" name="groupId" id="groupId">
	</form>
	
	<form id="managerForm" method="POST" ACTION="/manager.do">
		<input type="hidden" name="toPage" id="toPage">
	</form>


	 		<!-- context menu js  -->
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.contextMenu.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/dist/jquery.ui.position.min.js" type="text/javascript"></script>
    <script src="https://swisnl.github.io/jQuery-contextMenu/js/main.js" type="text/javascript"></script>
    <script type="text/javascript" src="/page/mainPage/js/contextMenu.js"></script>
	
		<!-- network js  -->
		<script type="text/javascript" src="/include/network-1.5.0/network.js"></script>
		<script type="text/javascript" src="http://www.google.com/jsapi"></script>
		<script type="text/javascript" src="/page/mainPage/js/network.js"></script>
			
		<!-- ohsnap -->
		<script type="text/javascript" charset="utf-8"	src="https://rawgithub.com/justindomingue/ohSnap/master/ohsnap.js"	></script>
		
		
		<!-- custom js  -->
		<script type="text/javascript" src="/commanJs/clientSideLibrary.js"></script>
	

<script>

	var bands;
	var rootsBand = new Array();


	window.onload=function(){
	
		//메세지
		var message;
		var popup_color;
		<% 
			if(request.getAttribute("message")!=null && request.getAttribute("messageKind") !=null){
				enumCautionKind kind = (enumCautionKind)request.getAttribute("messageKind");	
		%>
			  message = "<%=(String)request.getAttribute("message")%>";
			  popup_color = "<%=(String)kind.getString()%>";
			  ohSnap(message,{color:popup_color});
		<%
			}
	
		if(isSuccessVerify){
		
			ArrayList<Band> bands = BandManager.getInstance().getAdministeredBandsOfRoot(member.getId());
			if(bands.size()>0){
				%>
				rootsBand = new Array(<%=bands.size()%>);
				
				<%
				for(int i=0 ; i < bands.size() ; i++){
				
					Tree<Band> tree = BandManager.getInstance().getSubBands(bands.get(i).getBandId());
					ArrayList<BundleBand> subBands = tree.getSubRelationNodes();
					ArrayList<Band> localBands = tree.getDatas();
					
					%>
						bands = new Array(<%=localBands.size()%>);
						var _bundleBand = new Array( <%=subBands.size()%>);
					<%
					for(int l=0 ; l < subBands.size() ; l++){
						%>		
						_bundleBand[<%=l%>] = new bundleBand();
						_bundleBand[<%=l%>].fromBand = <%=subBands.get(l).fromBand.getBandId()%>;
						_bundleBand[<%=l%>].toBand = <%=subBands.get(l).toBand.getBandId()%>;
						
						<%
					}
					%>
					
					rootsBand[<%=i%>] = _bundleBand;
					
					<%
					for(int t=0 ; t < localBands.size() ; t++){
						%>
						bands[<%=t%>] = new band();
						bands[<%=t%>].id = <%=localBands.get(t).getBandId()%>;
						bands[<%=t%>].name = "<%=localBands.get(t).getBandName()%>";
						
						<%
					}
				}
				%>
				
				<%
					
			}
		}
		%>
			
			
	}
			

	var network = null;
	var GROUP_IMG_PATH = "image/groupImage.jpg";

	
	google.load("visualization", "1");
	
	// Set callback to run when API is loaded
	google.setOnLoadCallback(drawVisualization);
	
	// Called when the Visualization API is loaded.
	function drawVisualization() {
		// Create a datatable for the nodes.
		var nodesTable = new google.visualization.DataTable();
		nodesTable.addColumn('number', 'id');
		nodesTable.addColumn('string', 'text');
		nodesTable.addColumn('string', 'style');  // optional
		for(i=0 ; i < bands.length ; i++){
			var _band = new band();
			_band = bands[i];
			nodesTable.addRow([_band.id, _band.name,"circle"]);
		}

		
		// create a datatable for the links between the nodes
		var linksTable = new google.visualization.DataTable();
		linksTable.addColumn('number', 'from');
		linksTable.addColumn('number', 'to');
		linksTable.addColumn('number', 'width');  // optional
		for(i=0 ; i < rootsBand.length ; i++){
			localBandAry = rootsBand[i];
			for(l=0 ; l < localBandAry.length ; l++){
				linksTable.addRow( [localBandAry[l].fromBand, localBandAry[l].toBand , 1] );							
			}
		}
		
		// specify options
		
		var options = {
	
		  'width':  "100%",
		  'height': "100%",
		 'backgroundColor' : "#9ec5d1"					  
			 
		};
	
		// Instantiate our network object.
		network = new links.Network(document.getElementById("mynetwork"));
		google.visualization.events.addListener(network, 'select', onselect);
		
		// Draw our network with the created data and options
				network.draw(nodesTable, linksTable, options);
				
	}


	
	
	////member가져오기


</script>


</body>
</html>