

	var networkSwitch1 = false;
	var networkSwitch2 = false;

	var bands;
	var rootsBand = new Array();

		var changeBand = false;
		var selectedBandInfo;

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
		   url:'/page/mainPage/ajax/getMyBands.jsp',
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
		
		
		function bundleBand(){
				var fromBand;
				var toband;
		} 

		function band(){
			var id;
			var name;
		}	

		
		function onselect(){
		
		  	var sel = network.getSelection();
		  	var selectedBandId = network.nodesTable[sel[0].row].id;
				$.ajax({
				    url:'/page/manager/ajax/group/getBandInfo.jsp',
				    data: { bandId:selectedBandId},
				    dataType:'json',
				    success:function(data){
				    		
				    		selectedBandInfo = data;
				    		selectedBandInfo.upperBandId = selectedBandId;
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
				    			
		  					$("#makeSubmit").val("그룹 정보 변경하기");
		  					changeBand = true;
				    		}
			       })
			       
        $.ajax({
				 		
			    url:'/page/manager/ajax/group/getSelectedBandMembers.jsp',
			    data : {bandId : selectedBandId},
			    dataType:'json',
			    success:function(data){

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
        			});
		
			  	
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
		  	