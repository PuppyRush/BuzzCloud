
		function bundleBand(){
				var fromBand;
				var toband;
		} 

		function band(){
			var id;
			var name;
		}	

		
		$(document).ready(function() 
		{
										
					var comAjax = new ComAjax();
					comAjax.setUrl("/mainPage/initBandMap.ajax");
					comAjax.setCallback("callback_drawNetwork");
					comAjax.setType("post");
					comAjax.ajax();
		
		});
	
		
		function callback_drawNetwork(data){
			
				if(data== null || data.length == 0)
					return;
				
				if(data["subBandRelation"]){
					var subBandRelation = data["subBandRelation"];
					_rootsBand = new Array(subBandRelation.length);
		    	var i=0;
		   	for (var key in subBandRelation) {		   
		 					var temp = subBandRelation[key];
			  			_rootsBand[i] = new bundleBand();
			  			_rootsBand[i].fromBand = temp[0];
			  			_rootsBand[i].toBand = temp[1];
			  			i++;
					 }
				}
				
				if(data["myAllBand"]){
					var myBands = data["myAllBand"];
				 _bands = new Array(myBands.length);
				    var i=0;
				    for (var key in myBands) {		
								_bands[i] = new band();
								_bands[i].id = key;
								_bands[i].name = myBands[key];
					  		i++;
							 }
				}
			
				drawVisualization();
		}
		
		
		 
		
		
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
		
			// Create a datatable for the nodes.
			var nodesTable = new google.visualization.DataTable();
			nodesTable.addColumn('number', 'id');
			nodesTable.addColumn('string', 'text');
			nodesTable.addColumn('string', 'style');  // optional
			for(i=0 ; i < _bands.length ; i++){
				var _band = new band();
				_band = _bands[i];
				var id = Number(_band.id);
				nodesTable.addRow([ id , _band.name,"circle"]);
			}

			
			// create a datatable for the links between the nodes
			var linksTable = new google.visualization.DataTable();
			linksTable.addColumn('number', 'from');
			linksTable.addColumn('number', 'to');
			linksTable.addColumn('number', 'width');  // optional
			for(i=0 ; i < _rootsBand.length ; i++){
					localBand = _rootsBand[i];
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
		  	