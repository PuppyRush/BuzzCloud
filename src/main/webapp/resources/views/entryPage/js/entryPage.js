				var mail;
				var nick;
				
				var naver = new naver_id_login("Vf8cYbYQv2N0c_cSv_XA", "http://210.179.101.193:8100/");
				var state = naver.getUniqState();
				naver.setState(state);
				naver.setButton(BUTTON_COLOR_GREEN, BANNER_BIG_TYPE, 40);
				naver.setStateStore();
				naver.init_naver_id_login();	 


				

				$("#joinButton").click(function(){

							var idType = "NOTHING";
					 		var email = $("#innerJoinForm #email").val();
					 		var nickname = $("#innerJoinForm #nickname").val();
					 		var password = $("#innerJoinForm #password").val();
					
					   $.ajax({
						   type:"POST",
						   url:'/page/entryPage/ajax/join.jsp',
						   data : { email: email, nickname:nickname, password:password, idType:idType},
						   dataType:'json',
						   success:function(data){
						   	if(data["message"]!=undefined)
						   		ohSnap(data["message"],{color:"green"});
						   	
				   			if(data["isSuccess"]){
				   						$("#preLoginForm").submit();
				   					}
						   	
					          }
					      });
				
				
					});
				
				
				 $(document).ready(function(){ 
									$(	'#loginTobuzzCloud').click(function(e){
										e.preventDefault();
										wrapLogonModal();
									  
									});
									
									$(	'.joinToBuzzCloud').click(function(e){
										e.preventDefault();
										wrapJoinModal();
										  
									});
									
									//닫기 버튼을 눌렀을 때
									$('.logonModal .close').click(function (e) {  
									    //링크 기본동작은 작동하지 않도록 한다.
									    e.preventDefault();  
									    $('#mask, .logonModal').hide();  
									});   
									
									//닫기 버튼을 눌렀을 때
									$("#joinButton").click(function (e) {  
									    //링크 기본동작은 작동하지 않도록 한다.
									    e.preventDefault();  
									    $('#mask, .joinModal').hide();  
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
				 
	
								 function wrapLogonModal(){
									        //화면의 높이와 너비를 구한다.
						        var maskHeight = $(document).height();  
						        var maskWidth = $(window).width();  
						
						        //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
						        $('#mask, .joinModal').hide();
						        $('#mask').css({'width':maskWidth,'height':maskHeight});  
											$('#mask').fadeTo("slow",0.8);      
												
											$('.logonModal').show();
												
									}
								 
								 function wrapJoinModal(){
									        //화면의 높이와 너비를 구한다.
										 var maskHeight = $(document).height();  
										 var maskWidth = $(window).width();  
										
										 //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
										 $('#mask, .logonModal').hide();
										 $('#mask').css({'width':maskWidth,'height':maskHeight});  
											$('#mask').fadeTo("slow",0.8);      
											$('.joinModal').show();
											
									}
								
	
									
								function setHiddenForm(){
									
									mail = naver.getProfileData('email');
									nick = naver.getProfileData('nickname');
									
									$("#loginForm").find("#idType").val("NAVER");
									$("#loginForm").find("#email").val(mail);
									$("#loginForm").find("#nickname").val(nick);
									$("#loginForm").find("#password").val("");
									$("#loginForm").submit();
								}
								
			 					//네이버로그인 콜백인지 확인 
									var state = getParameter("state");
									if(state == null)
										console.log("error of urlpaser");
									var savedNaverState = naver.state;			
												
									if( state.match(savedNaverState) ){
											naver.get_naver_userprofile("setHiddenForm()");			
						}
									
									