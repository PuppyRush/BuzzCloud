<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>BuzzCloud</title>	
		
    <script type="text/javascript" charset="utf-8" src="http://code.jquery.com/jquery-2.2.3.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.0/jquery.cookie.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.2.js"></script>

		</head>	
		
	<body>
	
	</div>
	<div id="naver_id_login">
		
 	 <!-- <input type="BUTTON" value="네이버로 로그인" name="LoginToNaver" class="NAVER_BUTTON"  onclick='loginProcess()'></input> -->
		
		</div>
		<div><input type="BUTTON" value="정보보기" onclick='load()'></input>
		
		<div><h3 id="result"></h3></div>
		
		
		
<script type="text/javascript">

		var mail;
		var nick;

		var naver = new naver_id_login("Vf8cYbYQv2N0c_cSv_XA", "http://114.129.211.33:8181/");
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
		
		function setHiddenForm(){
			
			mail = naver.getProfileData('email');
			nick = naver.getProfileData('nickname');
			document.getElementsByName("email")[0].value = mail;
			document.getElementsByName("nickname")[0].value = nick;
			document.getElementsByName("password")[0].value = "";
			document.getElementsByName("reg_date")[0].value = "";
			document.getElementsByName("idType")[0].value = "naver";
			console.log("네이버 프로필 보내기 : " + mail + " , " + nick);
			document.forms["hiddenForm"].submit();
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
						
						session.setAttribute("id", "naverLogin");
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
</script>

		<input type="BUTTON" name="innerJoin" value="가입하기 " onClick="innerJoin()"><br>
				<input type="BUTTON" name="innerLogon" value="로그인하기 " onClick="innerLogon()">로그인하기 

		<div>
			<form method="GET" ACTION="/logon.do" id="hiddenForm">
				<input type = "hidden" name = "email" value = "" >
				<input type = "hidden" name = "nickname" value = "" >
				<input type = "hidden" name = "password" value = "" >
				<input type = "hidden" name = "idType" value = "" >
				<input type = "hidden" name = "reg_date" value = "" >
			</form>
		</div>
		
		<script>
		
			function innerJoin(){
			
					location.href = "/innerJoin.do";
				
			}
		
		</script>
		
	</body>
</html>