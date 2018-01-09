
		 $("#navPages li").on("click", function(){

				$("#managerForm #forwardPageName").val($(this).attr('id'));
				$("#managerForm").submit()			
				
	 	});
	 
	

		/*    $(window).load(function () {

		        $('.flexslider').flexslider({
		            animation: "slide",
		            slideshow: true,
		            start: function (slider) {
		                $('body').removeClass('loading');
		            }
		        });
		    });*/
			
		 	myBandIds = new Array()
			$(document).ready(function() {
				
				initPage();
				setMyGroups();

		        $("#btn-blog-next").click(function () {
		            $('#blogCarousel').carousel('next')
		        });
		        $("#btn-blog-prev").click(function () {
		            $('#blogCarousel').carousel('prev')
		        });

		        $("#btn-client-next").click(function () {
		            $('#clientCarousel').carousel('next')
		        });
		        $("#btn-client-prev").click(function () {
		            $('#clientCarousel').carousel('prev')
		        });
				
			});
			
			
			function initPage(){
				
				$("#groups").html("");
				
			}
			
			function setMyGroups(){

				var comAjax = new ComAjax();
				comAjax.setUrl("/managerPage/groupDashboard/getMyGroups.ajax");
				comAjax.setCallback("callback_setMyGroups");
				comAjax.setType("get");
				comAjax.ajax();
			}
			
			function callback_setMyGroups(data){
				if(data["groups"]!=null){
					ary = data["groups"];
					
					for(i=0 ; i < ary.length ; i++ ){
						
						band = ary[i];
						myBandIds.push(band["bandId"]);
						$("#groups").append('<li id="'+band["bandId"]+'">  <i class="unread"></i> <img class="avatar" src="'+band["image"]+'" alt="avatar">'
								+ '<p class="name" id="name">이름 : '+band["bandName"]+ ' </p>'
								+'<p class="content" id="size"><strong>그룹원 수</strong> ' +band["memberCount"]+'명 </p>'+
								'<div class="actions"><a id='+band["adminId"]+' onClick="groupModal(this)"><img src="http://findicons.com/files/icons/2151/snow/32/information.png" alt="reply"></a></div></li>');
										
					}
					if(myBandIds.length>0)
						getCharts(myBandIds[0]);
				
				}
			}
			
			function groupModal(a){
				wrapGroupModal();
				bandId = a.id;
				
				var comAjax = new ComAjax();
				comAjax.setUrl("/managerPage/groupDashboard/getBandInfo.ajax");
				comAjax.setCallback("callback_setBandInfo");
				comAjax.addParam("bandId",bandId);
				comAjax.setType("GET");
				comAjax.ajax();
			}
			


			function callback_setBandInfo(data){
			
								
				$("#bandAdminName").val( "그룹 관리자 명 :" + data["bandAdminName"]);
				$("#bandCreatedDate").val("그룹 생성 날자 : " +data["bandCreatedDate"]);
				$("#subBandNumber").val("하위 그룹 수(최상위 포함): "+data["subBandNumber"]);
				$("#bandMembersAll").val("가입자 수(하위 그룹 포함) : "+data["bandMembersAll"]);
				$("#bandContain").val(data["bandContain"]);
				
			}
			
			selectedBandId=0;
			$("#groups").on("click","li",function(){
				
				selectedBandId = bandId = $(this).attr("id");
				
				getCharts(selectedBandId);
				
				var comAjax = new ComAjax();
				comAjax.setUrl("/managerPage/groupDashboard/getDashboardInfoAll.ajax");
				comAjax.setCallback("callback_setDashboard");
				comAjax.addParam("bandId",bandId);
				comAjax.setType("GET");
				comAjax.ajax();
				
			});
			
			function callback_setDashboard(data){
				
				$("#joins").html("");
				setRequestedJoinMembers(data["requestedJoin"]);

				
				
			}
			
			function setRequestedJoinMembers(data){
				for(var member in data){
					band = data[member];
					$("#joins").append('<li id='+band["memberId"]+'><i class="unread"></i><img class=avatar src='+band["imagePath"]+' alt="avatar">'
							+'<p class="sender">사용자 명 : '+band["nickname"]+'</p> <p class="content">요청 날짜 : '+ band["date"] +'</p>'
							+' <div class="actions"><a id='+band["memberId"]+' onClick="acceptMember(this)"><img src="http://findicons.com/files/icons/2226/matte_basic/16/accept.png" alt="accept" ></a>'
							+'<a id='+band["memberId"]+' onClick="denyMember(this)"><img src="http://findicons.com/files/icons/573/must_have/48/remove.png" alt="delete" ></a></div></li>');
														
				}
			}
			
			function acceptMember(data){
				memberId = data.id;
				var comAjax = new ComAjax();
				comAjax.setUrl("/managerPage/groupDashboard/acceptMember.ajax");
				comAjax.setCallback("callback_considerMember");
				comAjax.addParam("memberId",memberId);
				comAjax.addParam("bandId",selectedBandId);
				comAjax.addParam("isAccept","true");
				comAjax.setType("GET");
				comAjax.ajax();
			}
			
			function denyMember(data){
				memberId = data.id;
				var comAjax = new ComAjax();
				comAjax.setUrl("/managerPage/groupDashboard/denyMember.ajax");
				comAjax.setCallback("callback_considerMember");
				comAjax.addParam("memberId",memberId);
				comAjax.addParam("bandId",selectedBandId);
				comAjax.addParam("isAccept",false);
				comAjax.setType("GET");
				comAjax.ajax();
			}
			
			
			function callback_considerMember(data){
				ohSnap(data["message"],{color: data["messageKind"]} );
				
				memberId = data["memberId"];
				$("#joins li").each(function(idx){
						id = $(this).attr('id');
						if(id==memberId){
							$(this).remove();
						}
				})
				
				
				
			}
			

	