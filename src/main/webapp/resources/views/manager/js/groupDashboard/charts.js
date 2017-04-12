
/*** second Chart in Dashboard page ***/

	function getCharts(bandId){
		var comAjax = new ComAjax();
		comAjax.setUrl("/managerPage/groupDashboard/getCharts.ajax");
		comAjax.setCallback("callback_setCharts");
		comAjax.addParam("bandId",bandId);
		comAjax.setType("GET");
		comAjax.ajax();		
	}
	
	
	function callback_setCharts(data){
		
		use = Math.round(data["usingCapacity"]/1024/1024);
		max = Math.round(data["maxCapacity"]/1024/1024);
		per = Math.round(max/100);
		useper = per * use;
		restper = 100-useper;
		
		$("#usageText").text("usage : " + useper+ "%");
		
		
		info = new Highcharts.chart('usage', {
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: null,
	        plotShadow: false,
	        type: 'pie',
    				backgroundColor:null,
	        height:200
	    },
	    title: {
	        text: null
	    },
	    tooltip: {
	        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	    },
	    plotOptions: {
	        pie: {
	            allowPointSelect: true,
	            cursor: 'pointer',
	            dataLabels: {
	                enabled: true,
	                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
	                style: {
	                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                }
	            }
	        }
	    },
	    series: [{
	        name: 'usage',
	        colorByPoint: true,
	        data: [{
	            name: 'usage',
	            y: useper
	        }, {
	            name: 'rest',
	            y: restper,
	            sliced: true,
	            selected: true
	        }]
	    }]
	});
		
	
	}	
	