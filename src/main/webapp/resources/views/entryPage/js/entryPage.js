		
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
		
		$(document).ready(function() {
			$('#loginTobuzzCloud').click(function(e) {
				e.preventDefault();
				wraploginModal();
				
			});
			
			$('.joinToBuzzCloud').click(function(e) {
				e.preventDefault();
				wrapJoinModal();
				
			});
			
			//닫기 버튼을 눌렀을 때
			$('.loginModal .close').click(function(e) {
				//링크 기본동작은 작동하지 않도록 한다.
				e.preventDefault();
				$('#mask, .loginModal').hide();
			});
			
			//닫기 버튼을 눌렀을 때
			$("#joinButton").click(function(e) {
				//링크 기본동작은 작동하지 않도록 한다.
				e.preventDefault();
				$('#mask, .joinModal').hide();
			});
			
			$('.joinModal .close').click(function(e) {
				//링크 기본동작은 작동하지 않도록 한다.
				e.preventDefault();
				$('#mask, .joinModal').hide();
			});
			
			//검은 wraploginModal 눌렀을 때
			$('#mask').click(function() {
				$(this).hide();
				$('.loginModal').hide();
				$('.joinModal').hide();
			});
		})
		
		$("#joinButton").click(function() {
			
			var idType = "NOTHING";
			var email = $("#innerJoinForm #email").val();
			var nickname = $("#innerJoinForm #nickname").val();
			var password = $("#innerJoinForm #password").val();
			
			var comAjax = new ComAjax();
			comAjax.setUrl("/entryPage/join.ajax'");
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
				ohSnap(data["message"], {
					color : "green"
				});
			
			if (data["isSuccess"]) {
			$("#preLoginForm").submit();
			}
		}
		
		function wraploginModal() {
			//화면의 높이와 너비를 구한다.
			var maskHeight = $(document).height();
			var maskWidth = $(window).width();
			
			//마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
			$('#mask, .joinModal').hide();
			$('#mask').css({
				'width' : maskWidth,
				'height' : maskHeight
			});
			$('#mask').fadeTo("slow", 0.8);
			
			$('.loginModal').show();
			
		}
		
		function wrapJoinModal() {
			//화면의 높이와 너비를 구한다.
			var maskHeight = $(document).height();
			var maskWidth = $(window).width();
			
			//마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
			$('#mask, .loginModal').hide();
			$('#mask').css({
				'width' : maskWidth,
				'height' : maskHeight
			});
			$('#mask').fadeTo("slow", 0.8);
			$('.joinModal').show();
			
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
