
				var mail;
				var nick;
				
				var naver = new naver_id_login("Vf8cYbYQv2N0c_cSv_XA", "http://114.129.211.123:8181/");
				var state = naver.getUniqState();
				naver.setState(state);
				naver.setButton(BUTTON_COLOR_GREEN, BANNER_BIG_TYPE, 40);
				naver.setStateStore();
				naver.init_naver_id_login();	 


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
				 
		 					//네이버로그인 콜백인지 확인 
								var state = getParameter("state");
								if(state == null)
									console.log("error of urlpaser");
								var savedNaverState = naver.state;			
											
								if( state.match(savedNaverState) ){
										naver.get_naver_userprofile("setHiddenForm()");			
					}
								
								
								
								

								
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
								
					
								 $("#innerJoinForm submit").on('click' ,function(){

								 
								 	$(this).submit();
								 
								 
								 });
								 
									
								function setHiddenForm(){
									
									mail = naver.getProfileData('email');
									nick = naver.getProfileData('nickname');
									
									$("#loginidType").val("naver");
									$("#loginForm email").val(mail);
									$("#loginForm nickname").val(nick);
									$("#loginForm password").val("");
									$("#loginForm").submit();
								}
								

					