<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    <%@page import="org.jsoup.*"%>
    <%@page import="org.jsoup.nodes.Document"%>
    <%@page import="org.jsoup.select.Elements"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">

<!-- 
<link rel="stylesheet" type="text/css" href="http://192.168.0.4/html/naver%20top%2010/mystyle.css">
 -->
<style>
body {
		margin: 0px;
	}
table.mytable {
	    font-family: verdana, arial, sans-serif;
        font-size: 13px;
        font-weight: bold;
        color: #333333;
        border-width: 1px;
        border-color: #3A3A3A;
        border-collapse: collapse;
        width: 100%;
}

table.mytable td {
        border-style: solid;
        border-color: #00C9D4;
        background-color: #E5FAFB;
        width: 33%;
	}
</style>
<%
String[][] sthtoeat = new String[3][8];
%>
<title>Insert title here</title>

<!-- 
<script src="http://192.168.0.4/html/naver%20top%2010/myfunction.js"></script>
 -->




</head>
<body>

<table class="mytable">
<tr>
	<td>아침</td>
	<td>점심</td>
	<td>저녁</td>
</tr>
<tr>
<%

Document document = Jsoup.connect("http://dorm.knu.ac.kr/scdorm/").get();

if (null != document)
{
  //Elements elements = document.select("td[width=33%],td[width=34%]");
  Elements elements = document.select("td[width=160]");
  //System.out.println(elements.size());
  String[] textArr;
  for(int i = 0 ; i < elements.size(); i++)
  {
	  out.println("<td>");
	  textArr = elements.get(i).html().split("<br> ");
	  //System.out.println(elements.get(i).html());
	  for(int j = 0; j < textArr.length; j++)
	  {
		  sthtoeat[i][j] =textArr[j];
		  out.println(sthtoeat[i][j] + "</br>");
	  }
	  out.println("</td>");
	   
  }
}
	%>
	</tr>
	</table>
</body>
</html>