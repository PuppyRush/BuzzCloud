		
		var naver = new naver_id_login("Vf8cYbYQv2N0c_cSv_XA",
				"http://210.179.101.193:8100/");
		var state = naver.getUniqState();
		naver.setState(state);
		naver.setButton(BUTTON_COLOR_GREEN, BANNER_BIG_TYPE, 40);
		naver.setStateStore();
		naver.init_naver_id_login();
		
		var naverLoginState = getParameter("state");
		
		$(document).ready(
		
		function() {
			
			var mail;
			var nick;
			
			//네이버로그인 콜백인지 확인 
			
			if (naverLoginState == null)
				console.log("error of urlpaser");
			
			if (naverLoginState!=false && naverLoginState.match(naver.state)) {
					naver.get_naver_userprofile("setHiddenForm()");
			} else {
				
				var comAjax = new ComAjax();
				comAjax.setUrl("/entryPage/alreadyLogin.ajax");
				comAjax.setCallback("callback_AlreayLogin");
				comAjax.setType("get");
				comAjax.ajax();
			}
		})
	
		$(".contactModal #send").click(function() {
			var comAjax = new ComAjax();
			comAjax.setUrl("/entryPage/contact.ajax");
			comAjax.addParam("from", $(".contactModal #email").val());
			comAjax.addParam("contain", $(".contactModal #contain").val());
			comAjax.addParam("subject", $(".contactModal #subject").val());
			comAjax.setCallback("callback_contact");
			comAjax.setType("post");
			comAjax.ajax();			
		});
		
		function callback_contact(data) {
			ohSnap(data["message"], {		color : data["messageKind"]});
		}
		
		
		$(".findPassword").on("click", function() {
			$("#forwardPage #page").val("INPUT_MAIL");
			$("#forwardPage").submit();			
		});
		
		
		
		$("#joinButton").click(function() {
			
			var idType = "NOTHING";
			var email = $("#joinForm #email").val();
			var nickname = $("#joinForm #nickname").val();
			var password = $("#joinForm #password").val();
			
			var comAjax = new ComAjax();
			comAjax.setUrl("/member/join.ajax");
			comAjax.addParam("idType", idType);
			comAjax.addParam("email", email);
			comAjax.addParam("nickname", nickname);
			comAjax.addParam("password", password);
			comAjax.setCallback("callback_join");
			comAjax.setType("post");
			comAjax.ajax();
			
		});
		
		function callback_AlreayLogin(data) {
			if (data["alreadyLogin"]) {
			$("#alreadyLoginForm").submit();
			}
		}
		
		function callback_join(data) {
			
			if (data["message"] != undefined)
				ohSnap(data["message"], {color:data["messageKind"]} );
			
			if (data["isSuccess"]) {
			$("#preLoginForm").submit();
			}
		}
		
		
		function setHiddenForm() {
			
			mail = naver.getProfileData('email');
			nick = naver.getProfileData('nickname');
			
			$("#loginForm").find("#idType").val("NAVER");
			$("#loginForm").find("#email").val(mail);
			$("#loginForm").find("#nickname").val(nick);
			$("#loginForm").find("#password").val("");
			$("#loginForm").submit();
		}
