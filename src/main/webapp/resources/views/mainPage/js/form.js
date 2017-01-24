		
				
				 $(document).ready(function(){ 
		
							//닫기 버튼을 눌렀을 때
							$('.searchModal .close').click(function (e) {  
							    //링크 기본동작은 작동하지 않도록 한다.
							    e.preventDefault();  
							    $('#mask, .searchModal').hide();  
							});   
							
								
		
							
							//검은 wrapLogonModal 눌렀을 때
							$('#mask').click(function () {  
								    $(this).hide();  
								    $('.searchModal').hide();  
								    
								});
				 })
				 
	
				 function wrapSerachModal(){
					        //화면의 높이와 너비를 구한다.
		        var maskHeight = $(document).height();  
		        var maskWidth = $(window).width();  
		
		        		//마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
		        
		        $('#mask').css({'width':maskWidth,'height':maskHeight});  
							$('#mask').fadeTo("slow",0.8);      
								
							$('.searchModal').show();
								
					}
				 
				 $("#joinBandButton").on("click",function(){			 		 
					 
					   $.ajax({
					   url:'/page/mainPage/ajax/requestBandJoin.jsp',
					   data : { memberId: memberId, bandId : selectedBandIdOfAutoCompleted},
					   dataType:'json',
					   success:function(data){
			   				if(data["isSuccess"]){
			   						$('#mask').hide();
			   						$('.searchModal').hide();  
	   								$("#ohsnap").show();
			   						ohSnap("그룹에 가입이 신청되었습니다. 승인이 되면 사용이 가능합니다.", {color: 'green'});	   				
			   						}
					          }
					   
					      });
				 
				 });


				
