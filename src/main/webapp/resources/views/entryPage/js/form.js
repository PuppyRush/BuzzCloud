
		
		$(document).ready(function() {
			$('#loginTobuzzCloud').click(function(e) {
				e.preventDefault();
				wraploginModal();
				
			});
			
			$('#contact').click(function(e) {
				e.preventDefault();
				wrapContactModal();
				
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
			
			$('.contactModal .close').click(function(e) {
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
				$('.contactModal').hide();
			});
		})
		

		
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
		
		
		function wrapContactModal() {
			//화면의 높이와 너비를 구한다.
			var maskHeight = $(document).height();
			var maskWidth = $(window).width();
			
			//마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
			$('#mask, .contcatModal').hide();
			$('#mask').css({
				'width' : maskWidth,
				'height' : maskHeight
			});
			$('#mask').fadeTo("slow", 0.8);
			$('.contactModal').show();
			
		}
		
