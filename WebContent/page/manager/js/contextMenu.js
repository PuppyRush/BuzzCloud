
$(document).ready(function() {

	$(function() {
		$.contextMenu({
			selector : '#groupMember',

			callback : function(key, options) {


			},
			items : {
					"myaccount" : {
					"name" : "내 계정 관리",
					"icon" : "edit"
				},
				"group" : {
					"name" : "그룹 관리",
					"icon" : "edit"
				},
				"groupdashbaord" : {
					"name" : "그룹 관리",
					"icon" : "edit"
				},
				"member" : {
					"name" : "그룹원 관리",
					"icon" : "edit"
				},


			}
		})
	});

});
