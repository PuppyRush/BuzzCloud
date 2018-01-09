
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
		
		setCapacityUsage(data);
		setCreatedTime(data["createdDate"]);
		setCreatedTimeByNow(data["createdDateByLong"]);
		
	
	}	
	
	function setCapacityUsage(data){

		use = Math.round(data["usingCapacity"]/1024/1024);
		max = Math.round(data["maxCapacity"]/1024/1024);
		per = Math.round(max/100);
		useper = per * use;
		restper = 100-useper;
		title = data["title"];
		
		$("#usageText").text("usage : " + useper+ "%");
				
		
		info = new Highcharts.chart('usage', {
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: null,
	        plotShadow: false,
	        type: 'pie',
    				backgroundColor:"#3d3d3d",
	        height:200
	    },
	    title: {
	        text: title
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
	
	function setCreatedTime(time){
			var date = new Date();
			var y = date.getFullYear();
			var d = date.getDay();
			
			var now = date.toDateString();
			
			var current_time = [h,m,s].join(':');
			
			$("#createdDate").text(now);
	}
	
	function setCreatedTimeByNow(list){
		var now = new Date();
		
		var past = new Date(list[0],list[1],list[2],list[3],list[4]);
		var gap = now - past;
		
		var currentMin = 60*1000;
		var min = parseInt(gap/currentMin);
		
		var currentHour = 60*60*1000;
		var hour = parseInt(gap/currentHour);
		
		var currentDay = 24*currentHour;
		var day = parseInt(gap/currentDay);
		
		var currentMonth = currentDay*30;
		var month = parseInt(gap/currentMonth);
		
		var currentYear = currentMonth*12;
		var year = parseInt(gap/currentYear);
		
		var str = "";
		if(year>0){
			str+= year+"년 ";
		}
		if(month>0){
			str+= month+"개월 ";
		}
		if(day>0){
			str+= day+"일 ";
		}
		if(hour>0){
			str+= hour+"시간 ";
		}
		
		$("#createdDateGap").text(str);
		
	}