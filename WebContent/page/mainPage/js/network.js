

		function bundleBand(){
				var fromBand;
				var toband;
		} 

		function band(){
			var id;
			var name;
		}	
      
      
  function onselect() {
		var sel = network.getSelection();
		
  	$("#groupForm #groupId").val(sel[0].row);
  	$("#groupForm").submit();
  	}
 
      window.onresize = function(){

	  	network.redraw();
      };
   
