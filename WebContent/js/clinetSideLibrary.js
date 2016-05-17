/**
 * 
 */


getParameter = function(url, param){
    full_url=url;
    
  			//파라미터가 하나도 없을때
    if(full_url.search("&") == -1)
        return false;

    search=full_url.split("#");
		    //해당하는 파라미터가 없을때.
    if(search[1].indexOf(param)==(-1)){
        
        return "";
        return;
		    }
    
    search=search[1].split("&");

   		 //한개의 파라미터일때.
    if(search.length<3){
       
        data=search[1].split("=");
        return data[1];
		    }
    else{
		    //여러개의 파라미터 일때.
	    var i=0;
    		for(i=0 ; i < search.length ; i++){
    				data = search[i].split("=");
	    			if(data[0].match(param))
	    				return data[1];
    			}
	    	if(i==search.length)
    				return NULL;
    		}
    
}