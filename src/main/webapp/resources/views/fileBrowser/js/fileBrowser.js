
    
    "use strict";
    (function(){
  

    		// JavaScript loader (REQUIRED)
    		load = function() {
    			require(
    				[
    					'elfinder'
    					, 'extras/editors.default.min'
    					, 'i18nfmsg'
    					, 'extOpts'
    					, 'extras/quicklook.googledocs.min'                    // optional GoogleDocs preview
    					, 'elfinderBasicAuth'
    					, xdr
    					, 'blockchain'
    				],
    				start,
    				function(error) {
    					alert(error.message);
    				}
    			);
    		},
    
    	// load JavaScripts (REQUIRED)
    	load();

    });
					 


    $(document).ready(function() {
    	
				var com = new ComAjax();
				com.addParam("bandId", bandId);
				com.setAsync(false);
				com.setUrl("/elfinder-servlet/connector/init");
				com.setType("post");
				com.ajax();
    	
		    $('#elfinder').elfinder({
		        url : '/elfinder-servlet/connector?bandId='+bandId,
			            });
			        });

		$(document).ready(function() {
			
			define('extOpts', {
				height: '80%',
				resizable: false
			});
		
			$('#fullpage').fullpage({
			    sectionsColor: ['#FFFFFF', '#4BBFC3'],
			    anchors: ['firstPage', 'secondPage'],
			    menu: '#menu',
			    css3: true,
			    scrollingSpeed: 1000

		   });
		});

		$(document).ready(function() {
			
				$('#issueTable').DataTable({
		     "processing": true,
        "serverSide": true,
        "bSort" : false,
        "ordering": false,
        "info": false,
        "type":"POST",
        "ajax": {
        "url": "/dataTables/init.ajax"
        				}
					
				});
		});
		
		
		

			