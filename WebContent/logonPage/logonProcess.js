/**
 * 		로그인 과정을 처리하기 위한 함수들
 */
  document.write('<script src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.2.js"></script>');

  function checkDepdency(){
	  
	  if(typeof(Storage) !== "undefined") {
		    // Code for localStorage/sessionStorage.
		} else {
		    // Sorry! No Web Storage support..
		}
	  
  	}
  
  
function loginProcess(kind){
	
	checkDepency();
	
	boolean isauto = (boolean)localStorage.setItem('isAutoLogon');
	if(isauto){
		
	}
	else
	{
		switch(kind){
		
				case naver:
					 loginNaver();
					break;
					
				case google:
					
					break;
					
				case facebook:
					
					break;
					
					default:
										
				
				}
	}
	
}
  
  //새로운 네이버 로그인에 대한 처리 
	function loginNaver() {
		
		
		var naver_state = naver.getUniqState();
		
		//기존의 state에 덮어씌움 
		naver.setState(state);
		//
		var link = (string)naver.getNaverIdLoginLink();
		
	}

	    	
	



		
	
	
	
