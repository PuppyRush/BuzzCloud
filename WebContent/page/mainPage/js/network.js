	
		var bandsMap;
		var relationAry;


		function bundleBand(){
				var fromBand;
				var toband;
		} 

		function band(){
			var id;
			var name;
		}	
		
      function addBandToPage(ary, bands){
						
  		relationAry = ary;
  		bandsMap = bands;					
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
			alert(bandsMap.size());
			for(i=0 ; i < bandsMap.size() ; i++){
				nodesTable.addColumn([bandsMap.get(i).id, bandsMap.get(i).name,"circle"]);
			}
	
			
			// create a datatable for the links between the nodes
			var linksTable = new google.visualization.DataTable();
			linksTable.addColumn('number', 'from');
			linksTable.addColumn('number', 'to');
			linksTable.addColumn('number', 'width');  // optional
			for(i=0 ; i < relationAry.length ; i++){
				localBandAry = relationAry.get(i);
				for(l=0 ; l < localBandAry.length ; l++){
					alert(localBandAry.get(l).fromBand);
					linksTable.addRow([localBandAry.get(l).fromBand, localBandAry.get(l).toBand , 1]);							
				}
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

  
	}
  
      function onselect() {
  		var sel = network.getSelection();

      	$("#groupForm #groupId").val(sel[0].row);
      	$("#groupForm").submit();
      }
     
      window.onresize = function(){

	  	network.redraw();
      };
   
