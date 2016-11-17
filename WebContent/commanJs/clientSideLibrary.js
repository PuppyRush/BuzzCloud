/**
 * 
 */

	function isValidateString(str){

		var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\+<>@\#$%&\\\=\(\'\"]/gi;
		
		if(str =="and" || str == "or" || str == "=" )
			return false;
		else if(regExp.test(str))
			return false;
	}	


		getParameter = function(param){
				    full_url=location.href;
				    
				  			//파라미터가 하나도 없을때
				    if(full_url.search("&") == -1)
				        return false;
		
				  			if(full_url.indexOf("#") != -1)
				    				search=full_url.split("#");
				  			else if(full_url.indexOf("?") != -1)
				  					search=full_url.split("?");
				  			else
				  					return null;
				  			
						    //해당하는 파라미터가 없을때.
				    if(search[1].indexOf(param)==(-1)){
				        
				        return "";w
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
		
		
		

Map = function(){
	 this.map = new Object();
	};   
	Map.prototype = {   
	    put : function(key, value){   
	        this.map[key] = value;
	    },   
	    get : function(key){   
	        return this.map[key];
	    },
	    containsKey : function(key){    
	     return key in this.map;
	    },
	    containsValue : function(value){    
	     for(var prop in this.map){
	      if(this.map[prop] == value) return true;
	     }
	     return false;
	    },
	    isEmpty : function(key){    
	     return (this.size() == 0);
	    },
	    clear : function(){   
	     for(var prop in this.map){
	      delete this.map[prop];
	     }
	    },
	    remove : function(key){    
	     delete this.map[key];
	    },
	    keys : function(){   
	        var keys = new Array();   
	        for(var prop in this.map){   
	            keys.push(prop);
	        }   
	        return keys;
	    },
	    values : function(){   
	     var values = new Array();   
	        for(var prop in this.map){   
	         values.push(this.map[prop]);
	        }   
	        return values;
	    },
	    size : function(){
	      var count = 0;
	      for (var prop in this.map) {
	        count++;
	      }
	      return count;
	    }
	};