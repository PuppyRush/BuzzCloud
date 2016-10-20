
var selectedSubGroup;
var selectedsuperGroup;

					 $(document).ready(function(){ 
					
				    $(function() {
				        $.contextMenu({
				            selector: '.img-fluid.context-menu-one',
				    
				            callback: function(key, options) {
				                var selectedKey = key;
				                alert(key);
				                switch(selectedKey){
				                		case("menu_superIssue"):
				                			
				                			break;
				                		
				                		case("menu_rootGroup"):
				                		
				                			break;
				                		case("menu_upperGroup"):
				           
				                			break;
				                		case("menu_subGroup"):
				    
			                				break;
				                		
				                		default:
				                		
				                			break;
				                
				                
				                							}
				            					},
				            items: {
				            "issue": {
			                "name": "이슈", 
			                "items": {
					                menu_superIssue: {
					                name: "상위그룹 함께 보기", 
					                type: 'checkbox', 
					                selected: true,
					                events: {
					                	click: function(event) { 
					                								
						                							}
					                							}
				                							},			                							
            							menu_subIssue: {
					                name: "하위그룹 함께 보기", 
					                type: 'checkbox', 
					                selected: true
				                							},
			        							menu_viewGroup: {
					                name: "이슈보기", 
					                callback: $.noop
				                			            }
									                }
									            },
	            			"sep1": "---------",
	            			"menu_rootGroup": {"name": "최상위 그룹보기", "icon": "edit"},
	            			"menu_upperGroup": {"name": "상위 그룹보기", "icon": "edit"},
	            			"menu_subGroup": {"name": "하위 그룹보기", "icon": "edit"}
						
										                }}
										            )
				    						});
			
						 });
				    
					 
					 
					 
			
			