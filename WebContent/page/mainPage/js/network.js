
	var networkSwitch1 = false;
	var networkSwitch2 = false;


		function bundleBand(){
				var fromBand;
				var toband;
		} 

		function band(){
			var id;
			var name;
		}	

		

		   $.ajax({
		   url:'/page/mainPage/ajax/getSubBandRelation.jsp',
		   data : { memberId: memberId},
		   dataType:'json',
		   success:function(data){
		   	rootsBand = new Array(data.length);
		    	var i=0;
		   	for (var key in data) {		   
		 					var temp = data[key];
			  			rootsBand[i] = new bundleBand();
			  			rootsBand[i].fromBand = temp[0];
			  			rootsBand[i].toBand = temp[1];
			  			i++;
					 }
		   	
		   	networkSwitch1 = true;
		   	if(networkSwitch1 && networkSwitch2){
		   		drawVisualization();
		    		}
		          }
		   
		      });
		      
		      
		   $.ajax({
		   url:'/page/mainPage/ajax/getBands.jsp',
		   data : { memberId: memberId},
		   dataType:'json',
		   success:function(data){
		 	 	 bands = new Array(data.length);
		    var i=0;
		    for (var key in data) {		
						bands[i] = new band();
						bands[i].id = key;
						bands[i].name = data[key];
			  		i++;
					 }
		    
		    	networkSwitch2 = true;
		    		
		    	if(networkSwitch1 && networkSwitch2){
		    		drawVisualization();
		    		}
		          }
		      });
			
		 
		
		
		function onselect(){
		
		  	var sel = network.getSelection();
				var bandId = network.nodesTable[sel[0].row].id;
			  	$("#bandForm #bandId").val(bandId);
			  	$("#bandForm").submit();
	  	}
	 
		window.onresize = function(){

		  	network.redraw();
	      };
		   
		      
		

	  	
		  var network = null;
		var GROUP_IMG_PATH = "image/groupImage.jpg";

		
		google.load("visualization", "1");
		
		// Set callback to run when API is loaded
		google.setOnLoadCallback(drawVisualization);
		
		// Called when the Visualization API is loaded.
		function drawVisualization() {
			if(networkSwitch1==false || networkSwitch2 == false){
				return;
			}
			// Create a datatable for the nodes.
			var nodesTable = new google.visualization.DataTable();
			nodesTable.addColumn('number', 'id');
			nodesTable.addColumn('string', 'text');
			nodesTable.addColumn('string', 'style');  // optional
			for(i=0 ; i < bands.length ; i++){
				var _band = new band();
				_band = bands[i];
				var id = Number(_band.id);
				nodesTable.addRow([ id , _band.name,"circle"]);
			}

			
			// create a datatable for the links between the nodes
			var linksTable = new google.visualization.DataTable();
			linksTable.addColumn('number', 'from');
			linksTable.addColumn('number', 'to');
			linksTable.addColumn('number', 'width');  // optional
			for(i=0 ; i < rootsBand.length ; i++){
					localBand = rootsBand[i];
					linksTable.addRow([  localBand.fromBand  , localBand.toBand , 1] );							
				
			}
			
			// specify options
			
			var options = {
		
			  'width':  "100%",
			  'height': "100%",
			 'backgroundColor' : "#9ec5d1"					  
				 
			};
		
		// Instantiate our network object.
		network = new links.Network(document.getElementById("mynetwork"));
		google.visualization.events.addListener(network, 'select', onselect);
		
		// Draw our network with the created data and options
				network.draw(nodesTable, linksTable, options);
				
		}
		  	