<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
        <%@page import="org.jsoup.*"%>
    <%@page import="org.jsoup.nodes.Document"%>
    <%@page import="org.jsoup.select.Elements"%>
    <%@ page import="java.util.*, java.text.*" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>

<style>

html,body,div,table{
	margin : 0px;
	width : 100%;
	height: 100%;
}

span{
	display: inline-block;
  vertical-align: middle;
  line-height: normal;      
}
div{
	float : left;
	font-weight: bold;
	font-family: "Lato","sans-serif";
}

.div_inner{
	height: 33%;
font-size : 1.3em;

}

table td{
	text-align:center;
	padding : 0px;
	border-spacing:0px;
	width: 33%;
}

table tr:nth-child(even) {            /* added all even rows a #eee color  */
	background-color : #DEDEDE;
}

 

table tr:nth-child(odd) {            /* added all odd rows a #fff color  */
	background-color : #CCCCCC;
}


.div_logo
{
	background-position:center center; 
	background-repeat: no-repeat; 
	background-size: contain;
	background-color: white;
}

.some_text
{
	font-weight: bold; 
	background: rgba(255, 255, 255, 0.5);
	//padding: 1.3em 0px 0px 0px;

}

.some_time
{
	//font-size : 1.3em;
}


</style>


<base target="_blank">
<%
ArrayList<ArrayList<String>> MatchInfo = null;
ArrayList<String> MatchData = null;
%>

</head>
<body>



<table>
<%

Document document = Jsoup.connect("https://search.naver.com/search.naver?ie=utf8&sm=stp_hty&where=se&query=%ED%95%B4%EC%99%B8%EC%B6%95%EA%B5%AC%EC%9D%BC%EC%A0%95").get();



if (null != document)
{
	MatchInfo = new ArrayList<ArrayList<String>>();
	MatchData = new ArrayList<String>();
	System.out.println("333333333333333333333");
	
	//0///////////////////매치 시간////////////////////
	Elements elements = document.select("div.db_area div.db_list span.bg_none");
	  
	if(elements.size() != 0)	//오늘 경기가 있는지 없는지0
	{
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).text());
		MatchInfo.add(MatchData);
	/////////////////////매치 시간////////////////////	
		
	
	//1///////////////////리그 정보////////////////////
		elements = document.select("div.db_area div.db_list p.league_ellipsis");
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).text());
		MatchInfo.add(MatchData);	
	/////////////////////리그 정보////////////////////
	
	//2///////////////////왼쪽 팀////////////////////
		elements = document.select("div.db_area div.db_list td.l_team");
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).text());
		MatchInfo.add(MatchData);	
	/////////////////////왼쪽 팀////////////////////
	
	//3///////////////////왼쪽 팀 로고////////////////////
		elements = document.select("div.db_area div.db_list td.l_team img");
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).attr("src"));
		MatchInfo.add(MatchData);	
	/////////////////////왼쪽 팀 로고////////////////////
	
	//4///////////////////오른쪽 팀////////////////////
		elements = document.select("div.db_area div.db_list td.r_team");
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).text());
		MatchInfo.add(MatchData);	
	/////////////////////오른쪽 팀////////////////////
	
	//5///////////////////오른쪽 팀 로고////////////////////
		elements = document.select("div.db_area div.db_list td.r_team img");
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).attr("src"));
		MatchInfo.add(MatchData);	
	/////////////////////오른쪽 팀 로고////////////////////
	
	//6///////////////////점수////////////////////
		elements = document.select("div.db_area div.db_list td.score");
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).text());
		MatchInfo.add(MatchData);	
	/////////////////////점수////////////////////
	
	//7///////////////////경기장////////////////////
		elements = document.select("div.db_area div.db_list td.place");
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).text());
		MatchInfo.add(MatchData);	
	/////////////////////경기장////////////////////

	//8,9///////////////////정보1////////////////////
		elements = document.select("div.db_area div.db_list td.btns a");
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
				MatchData.add(elements.get(i).attr("href"));
		MatchInfo.add(MatchData);
		
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
		{
			if(elements.get(i).attr("href").equals("#"))
				MatchData.add(elements.get(i).attr("onclick"));	
			else
				MatchData.add(" ");
		}
			
		MatchInfo.add(MatchData);

	/////////////////////정보1////////////////////
	
	//10,11///////////////////정보2////////////////////
		elements = document.select("div.db_area div.db_list td.btns img");

		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).attr("src"));
		MatchInfo.add(MatchData);
		
		MatchData = new ArrayList<String>();
		for(int i = 0; i < elements.size(); i++)
			MatchData.add(elements.get(i).attr("alt"));
		MatchInfo.add(MatchData);
	/////////////////////정보2////////////////////
	
	
		////출력///
		/*
	  	for(int i = 0; i < MatchInfo.size(); i++)
		{
			for(int j = 0 ; j < MatchData.size(); j++)
				System.out.print("(" + i + "/" + j + " " + MatchInfo.get(i).get(j) + ")");
			System.out.println("");
		}
	*/
		for(int i = 0; i < MatchData.size(); i++)
	  	{
		  	out.println("<tr title=\"" + MatchInfo.get(1).get(i)  +  "&#10경기장 : " + MatchInfo.get(7).get(i) + "\">");

			  	
			  	out.println("<td>");
				  	out.println("<div class=\"div_logo\"  style=\"background-image:url(\'" + MatchInfo.get(3).get(i) + "\');\">");
					  	//out.println("<div class=\"some_text\">" + MatchInfo.get(2).get(i) + "</div>");
					  	out.println("<div class=\"some_text\">" + "<span>" + MatchInfo.get(2).get(i) + "</span></div>");
					  	//out.println("<img src=\"" + MatchInfo.get(3).get(i) + "\" >");
			  		out.println("</div>");
			  	out.println("</td>");
			  	
			  	out.println("<td>");
			  	out.println("<div>");
			  		out.println("<div class=\"div_inner\">" + MatchInfo.get(0).get(i) + "</div>");
		  		  	out.println("<div class=\"div_inner\">" + MatchInfo.get(6).get(i) + "</div>");
		  		  	out.println("<div class=\"div_inner\">");
					  	out.println("<img src=\"" + MatchInfo.get(10).get(i) + "\"");
					  	if(MatchInfo.get(8).get(i).equals("#"))
							out.println("onclick=\"" + MatchInfo.get(9).get(i) + "\";>");
					  	else
					  		out.println("onclick=\"window.open(\'" + MatchInfo.get(8).get(i) + "\')\";>");
		  			out.println("</div>");
			  	out.println("</div>");
		  		out.println("</td>");
		  		
		  		out.println("<td>");
				  	out.println("<div class=\"div_logo\" style=\"background-image:url(\'" + MatchInfo.get(5).get(i) + "\');\">");
					  	out.println("<div class=\"some_text\">" + MatchInfo.get(4).get(i) + "</div>");
			  		out.println("</div>");
		  		out.println("</td>");

			out.println("</tr>");
	  	}
	}
	else
	{
		out.println("경기없음");
	}
	


  	
  	
}

%>


</table>

</body>
</html>

