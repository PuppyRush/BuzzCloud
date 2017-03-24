
$(document).ready(function() {

	$(function() {
		$.contextMenu({
			selector : '.img-fluid.context-menu-one',

			callback : function(key, options) {

				switch(key){

					case "logout":
						$("#logoutForm").submit();
						break;
						
					case "searchBand":
						wrapSerachModal();
						break;
					
					case "myaccount":
					case "group":
					case "groupdashboard":
					case "member":
					
				  	$("#managerForm #forwardPageName").val(key);
				  	$("#managerForm").submit();
						break;
						
				}
		
		
		

			},
			items : {
					"myaccount" : {
					"name" : "내 계정 관리",
					"icon" : "edit"
				},
				"group" : {
					"name" : "그룹 생성하기",
					"icon" : "edit"
				},
				"groupdashboard" : {
					"name" : "그룹 보드",
					"icon" : "edit"
				},
				"member" : {
					"name" : "그룹원 관리",
					"icon" : "edit"
				},
					"sep1" : "-----------",
					"searchBand" : {
					"name" : "그룹찾기"
				},
				"sep1" : "-----------",
				"logout" : {
					"name" : "로그아웃"
				},

			}
		})
	});

});
