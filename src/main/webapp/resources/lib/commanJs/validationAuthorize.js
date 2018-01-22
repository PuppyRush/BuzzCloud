		$(document).ready(
		
		function() {
			
				var comAjax = new ComAjax();
				comAjax.setUrl("/entryPage/alreadyLogin.ajax");
				comAjax.setCallback("callback_AlreayLogin");
				comAjax.setType("get");
				comAjax.ajax();
			}
		})