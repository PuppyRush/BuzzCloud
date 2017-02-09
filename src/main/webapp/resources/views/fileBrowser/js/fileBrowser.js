
					 


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
 /*

			
			(function($){
				var i18nPath = 'js/i18n',
					start = function(lng) {
						$().ready(function() {
							var i18nFolderMsgs = {
								'ko': {
									'volume_Demo': 'For Demo',
									'folder_BasicAuthExample': 'Basic Auth Example',
								},
							};
							// Documentation for client options:
							// https://github.com/Studio-42/elFinder/wiki/Client-configuration-options
							var elfinderInstance = $('#elfinder').elfinder({
								commandsOptions : {
									quicklook : {
										googleDocsMimes : ['application/pdf', 'image/tiff', 'application/vnd.ms-office', 'application/msword', 'application/vnd.ms-word', 'application/vnd.ms-excel', 'application/vnd.ms-powerpoint', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet']
									}
								},
								uiOptions: {
									cwd: {
										getClass : function(file) {
											return file.name.match(/photo|picture|image/i)? 'picture-folder' : '';
										}
									}
								},
								handlers : {
									init : function(e, fm) {
									
										$.extend(fm.messages, i18nFolderMsgs.en, i18nFolderMsgs[fm.lang] || {});
									}
								},
								sync: 5000,
								resizable: true,
								height: $(window).height() - 200,
								ui  : ['toolbar', 'places', 'tree', 'path', 'stat'],
								url : '/elfinder-servlet/connector',  // connector URL (REQUIRED)
								lang: lng                     // language (OPTIONAL)
							}).elfinder('instance');

							// set document.title dynamically etc.
							var title = document.title;
							elfinderInstance.bind('open', function(event) {
								var data = event.data || null;
								var path = '';
								
								if (data && data.cwd) {
									path = elfinderInstance.path(data.cwd.hash) || null;
								}
								document.title =  path? path + ':' + title : title;
							});

							// fit to window.height on window.resize
							var resizeTimer = null;
							$(window).resize(function() {
								resizeTimer && clearTimeout(resizeTimer);
								if (! $('#elfinder').hasClass('elfinder-fullscreen')) {
									resizeTimer = setTimeout(function() {
										var h = parseInt($(window).height());
										if (h != parseInt($('#elfinder').height())) {
											elfinderInstance.resize('100%', h);
										}
									}, 200);
								}
							});
						});
					},
					loct = window.location.search,
					full_lng, locm, lng;
				
				// detect language
				if (loct && (locm = loct.match(/lang=([a-zA-Z_-]+)/))) {
					full_lng = locm[1];
				} else {
					full_lng = (navigator.browserLanguage || navigator.language || navigator.userLanguage);
				}
				lng = full_lng.substr(0,2);
				if (lng == 'ja') lng = 'jp';
				else if (lng == 'pt') lng = 'pt_BR';
				else if (lng == 'ug') lng = 'ug_CN';
				else if (lng == 'zh') lng = (full_lng.substr(0,5) == 'zh-tw')? 'zh_TW' : 'zh_CN';

				if (lng != 'en') {
					$.ajax({
						url : i18nPath+'/elfinder.'+lng+'.js',
						cache : true,
						dataType : 'script'
					})
					.done(function() {
						start(lng);
					})
					.fail(function() {
						start('en');
					});
				} else {
					start(lng);
				}
			})(jQuery);
			
					*/
		
		
		
		$(document).ready(function() {
		    $('#fullpage').fullpage();
		});


			