
					 $(document).ready(function(){ 
					
				    $(function() {
				        $.contextMenu({
				            selector: '.img-fluid.context-menu-one',
				    
				            callback: function(key, options) {
				                var selectedKey = key;
				                switch(selectedKey){
				                		case("menu_issue"):
				                			wrapIssueModal();
				                			break;
				                		
				                		case("menu_rootGroup"):
				                		
				                			break;
				                		case("menu_upperGroup"):
				                		wrapSupperNetworkModal();
			                			break;
				                		case("menu_subGroup"):
				                		wrapSubNetworkModal();
			                			break;
				                
				                
				                							}
				            					},
				            items: {
												"menu_issue": {"name": "이슈보기", "icon": "edit"},
	            			"sep1": "---------",
	            			"menu_rootGroup": {"name": "최상위 그룹보기", "icon": "edit"},
	            			"menu_upperGroup": {"name": "상위 그룹보기", "icon": "edit"},
	            			"menu_subGroup": {"name": "하위 그룹보기", "icon": "edit"}
						
										                }}
										            )
				    						});
			
						 });
				    
			