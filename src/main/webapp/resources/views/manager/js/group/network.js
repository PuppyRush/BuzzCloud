


		var _bands = new Array();
		var _rootsBand = new Array();
		var _rootBandIds = new Map();


		function bundleBand(){
				var fromBand;
				var toband;
		} 

		function band(){
			var id;
			var name;
		}	

		
		$(document).ready(function()	{
			initNetwork();
		});
	
		function initNetwork(){
			var comAjax = new ComAjax();
			comAjax.setUrl("/mainPage/initBandMap.ajax");
			comAjax.setCallback("callback_drawNetwork");
			comAjax.setType("post");
			comAjax.setAsync(false)
			comAjax.ajax();
		}
		
		
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
			
				if(data["rootBands"]){
					var rootBandIds = data["rootBands"];
					 for (var key in rootBandIds) {		
					 	var id = rootBandIds[key];	
					 _rootBandIds[id] = id;
					 }
				}
				
				drawVisualization();
		}
				 

		var selectedBandId=-1
		function onselect(){
			
		  	var sel = network.getSelection();
		  	selectedBandId = network.nodesTable[sel[0].row].id;
		  
				var comAjax2 = new ComAjax();
				comAjax2.setUrl("/managerPage/groupConfig/getBandInfo.ajax");
				comAjax2.addParam("bandId", selectedBandId);
				comAjax2.setCallback("callback_setSelectedBandInfo");
				comAjax2.setType("post");
				comAjax2.setAsync("false")
				comAjax2.ajax();
		  	
				var comAjax = new ComAjax();
				comAjax.setUrl("/managerPage/groupConfig/getSelectedBandMembers.ajax");
				comAjax.addParam("bandId", selectedBandId);
				comAjax.setCallback("callback_setSelectedBandMembers");
				comAjax.setType("post");
				comAjax.setAsync("false")
				comAjax.ajax();	  	
	  	}
		
		
   var nowUpperBandId;
		function callback_setSelectedBandInfo(data){
			
				var sel = network.getSelection();
		  	var selectedBandId = network.nodesTable[sel[0].row].id;
		  		
	  		nowUpperBandId = Number(data["upperBandId"]); 
    		$("#makeGroupForm #groupName").val(data["bandName"]);
    		$("#makeGroupForm #groupOwner").val(data["ownerNickname"]);
    		$("#makeGroupForm #administrator").val(data["adminNickname"]);
    		$("#makeGroupForm #groupCapacity").val(data["bandCapacity"]/1024/1024);
    		$("#makeGroupForm #groupContain").val(data["bandContains"]);
    		
    		$("#groupMember").empty();
	  		members = data["bandMembers"];
	  		for(i=0 ; i< members.length ; i++)
	  			for(var key in members[i])
	  				$("#groupMember").append('<option>'+members[i][key]);	   			
    			
  		bandAuths = data["bandAuthority"];
    $("#bandAuthority option").each(function(){
				for(i=0 ; i < bandAuths.length ; i++){
					
					if(bandAuths[i] == $(this).val()){				
						$(this).prop("class", "selected_option");
						$(this).attr("selected","selected");
					}
				}
		    	 
		    });
	  		
    fileAuths = data["fileAuthority"];
    $("#fileAuthority option").each(function(){
			for(i=0 ; i < fileAuths.length ; i++){
				
				if(fileAuths[i] == $(this).val()){
					$(this).prop("class", "selected_option");
					$(this).attr("selected","selected");
				}
			}
	    	 
	    });
    
    $("#selectGroup option").each(function(){
    			if( data["upperBandName"] == $(this).val() ){
    				$(this).prop("class", "selected_option");
    				$(this).attr("selected","selected");
    				return;
    				}
    	
    		});
    
				$("#makeSubmit").val("그룹 정보 변경하기");
				isChangingBand = true;
		}

		
		function callback_setSelectedBandMembers(data){
 			$("#memberTable").find("tbody").html("");
		    
			var i=0;
			
	    for(var key in data){
    			var member = data[key];
    			
    			var nickname = member["nickname"];
    			var joinDate = member["joinDate"];
    			var email = member["email"];
    			var memberAuth = member["memberAuth"];
    			var fileAuth = member["fileAuth"];
    			
    			var className;
    			if(i%2==0)
    				className = "odd";
    			else
    				className = "even";
    			i++;
    			
    			$("#memberTable > tbody:last").append('<tr class = ' + className + '><td>' + email +' </td><td> '+
    			nickname + '</td><td>' + memberAuth + '</td><td>' + fileAuth + '</td><td>' + joinDate + '</td></tr>' );
    			
	    		}
		}
	  	
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
				
				if(_rootBandIds[id] == id)
					nodesTable.addRow([ id , _band.name,"database"]);
				else
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
		
		window.onresize = function(){

		  	network.redraw();
	      };
		  	
	      