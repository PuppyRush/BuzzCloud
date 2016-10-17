
			 $(document).ready(function(){ 
	
					//검은 wrapLogonModal 눌렀을 때
					$('#mask').click(function () {  
						    $(this).hide();  
						    $('.issueModal').hide();
						    $('.supperNetworkModal').hide(); 
						    $('.subNetworkModal').hide(); 
						    $('#context-menu-icon').show();
						});
		 		})

				 function wrapIssueModal(){
					        //화면의 높이와 너비를 구한다.
		        var maskHeight = $(document).height();  
		        var maskWidth = $(window).width();  
		
		        			//마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
		        $('#mask').hide();
		        $('#mask').css({'width':maskWidth,'height':maskHeight});  
							$('#mask').fadeTo("slow",0.8);      
							$('#context-menu-icon').hide();
							$('.issueModal').show();
	
								
					}


			 function wrapSupperNetworkModal(){
				        //화면의 높이와 너비를 구한다.
	        var maskHeight = $(document).height();  
	        var maskWidth = $(window).width();  
	
	        			//마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
	        $('#mask').hide();
	        $('#mask').css({'width':maskWidth,'height':maskHeight});  
						$('#mask').fadeTo("slow",0.8);      
						$('#context-menu-icon').hide();
						$('.supperNotworkModal').show();

							
				}

			 

			 function wrapSubNetworkModal(){
				        //화면의 높이와 너비를 구한다.
	        var maskHeight = $(document).height();  
	        var maskWidth = $(window).width();  
	
	        			//마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채운다.
	        $('#mask').hide();
	        $('#mask').css({'width':maskWidth,'height':maskHeight});  
						$('#mask').fadeTo("slow",0.8);      
						$('#context-menu-icon').hide();
						$('.subNetworkModal').show();

							
				}

				 
					var network = null;
					GROUP_IMG_PATH = "image/groupImage.jpg"

	      google.load("visualization", "1");

	      // Set callback to run when API is loaded
	      google.setOnLoadCallback(drawVisualization);

	      // Called when the Visualization API is loaded.
	      function drawVisualization() {
	        // Create a datatable for the nodes.
	        var nodesTable = new google.visualization.DataTable();
	        nodesTable.addColumn('number', 'id');
	        nodesTable.addColumn('string', 'text');
	        nodesTable.addColumn('string', 'style');  // optional
	        nodesTable.addRow([1, "안드로이드 강의", "circle"]);
	        nodesTable.addRow([2, "팀1", "circle"]);
	        nodesTable.addRow([3, "팀2", "circle"]);
	        nodesTable.addRow([4, "팀3", "circle"]);
	        nodesTable.addRow([101, "UI", "circle"]);
	        nodesTable.addRow([102, "Database", undefined]);
	        nodesTable.addRow([103, "Server", undefined]);
	        

	        // create a datatable for the links between the nodes
	        var linksTable = new google.visualization.DataTable();
	        linksTable.addColumn('number', 'from');
	        linksTable.addColumn('number', 'to');
	        linksTable.addColumn('number', 'width');  // optional
	        linksTable.addRow([1, 2, 1]);
	        linksTable.addRow([1, 3, 1]);
	        linksTable.addRow([1, 4, 1]);
	        linksTable.addRow([2, 101, 1]);
	        linksTable.addRow([2, 102, 1]);
	        linksTable.addRow([2, 103, 1]);
	        // specify options
	        
	        var options = {

	          'width':  "100%",
	          'height': "100%",
	  					 'backgroundColor' : "#9ec5d1"
	          
	          					
						};

	        // Instantiate our network object.
	        network = new links.Network(document.getElementById("supperNetwork"));
	        google.visualization.events.addListener(network, 'select', onselect);
	        // Draw our network with the created data and options
	        network.draw(nodesTable, linksTable, options);
	        
	      }
	      
	      function onselect() {
	    	  		var sel = network.getSelection();

	      	/*$("#groupForm #groupId").val(sel[0].row);
	      	$("#groupForm").submit();*/
			          }
					          
					          
					      
					      
	      
	      window.onresize = function(){

	    	  	network.redraw();
	      			};
	   
