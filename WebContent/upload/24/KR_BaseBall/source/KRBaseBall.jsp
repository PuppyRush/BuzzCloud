<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
        <%@page import="org.jsoup.*"%>
    <%@page import="org.jsoup.nodes.Document"%>
    <%@page import="org.jsoup.select.Elements"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="http://192.168.0.4/html/KR_BaseBall/Style.css">

<style>
	.large_box{
	border:1px solid;
	width: 100%;
	height: 504px;
	//max-width: 300px;
	}
	.box{
	border:1px solid;
	width: 100%;
	height: 100px;
	}
	.left-box{
	width: 40%;
	float:left;
	text-align:center;
	}
	.right-box
	{
	width: 40%;
	float:left;
	text-align:center;
	}
	.mid-box
	{
	width: 20%;
	float:left;
	text-align:center;
	}
	//#box{border:1px dotted green; border-bottom:5px solid blue }

ul,li { list-style:none; }
a { text-decoration:none; color:#000; }
.tab { border:1px solid #ddd; border-left:none; background:#fff; overflow:hidden; }
.tab li { float:left; width:33.3%; border-left:1px solid #ddd; text-align:center; box-sizing:border-box; }
.tab li { display:inline-block; padding:20px; cursor:pointer; }
.tab li.on { background-color:#eee; color:#f00; }
.tab_con { clear:both; margin-top:5px; border:1px solid #ddd; }
.tab_con div { display:none; height:100px; background:#fff; line-height:100px; text-align:center; }

table{
	width:100%;
}
td,th { border:1px solid; padding:1px; }

th {
font-weight:bold;s
}
</style>

<script type="text/javascript">

function ShowTabex(val)
{
  for (i=0; i<2; i++) 
  {
    var tb = document.getElementById('tab_' + i);

    if(i != val) tb.style.display = "none";
    else tb.style.display = "block";
    /*
    if(i != val)
    	tb.style.display = "none";
    else
	   	tb.style.display = "block";
    */
  }
}

</script>



</head>
<body>

 
<span onclick="ShowTabex('0')" style="border: 1px solid; padding: 0pt 5px; cursor: pointer;">경기 일정</span>
<span onclick="ShowTabex('1')" style="border: 1px solid; padding: 0pt 5px; cursor: pointer;">팀 순위</span>
 
<br>
  
<%@ page import="java.util.*, java.text.*" %>
   <% 
      SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
      String strdate = simpleDate.format(new Date());
   %>
   오늘 날짜는 <%= strdate%> 입니다.
<%
//오늘 일정/결과
Document document = Jsoup.connect("http://sports.news.naver.com/kbaseball/index.nhn").get();
if (null != document)
{
  //Elements elements = document.select("td[width=33%],td[width=34%]");
  Elements elements = document.select("li.hmb_list_items div.inner span");
  Elements timeelements = document.select("li.hmb_list_items div.inner em");
  
  out.println("<div class=\"large_box\" id=\"tab_0\" style=\"display: block;\">");
  for(int i = 0 ; i < timeelements.size() ; i++)
  {
	//out.println("<div class=\"box\" id=\"tab_0\" style=\"display: block; background-color: red;\"");
	out.println("<div class=\"box\">");
	out.println("<div class=\"left-box\">");
	//out.println("<div class=\"left-box\" style=\"background-img:url(\"http://192.168.0.4/html/KR_BaseBall/logo/" + elements.get(i*4).text()  + ".png\">");
	
	out.println("<img src=\"http://192.168.0.4/html/KR_BaseBall/logo/"+ elements.get(i*4).text()  + ".png\">");
	out.println("<br>");
	
	out.println(elements.get(i*4).text());
	out.println("<br>");
	out.println(elements.get(i*4+1).text());
	out.println("</div>");
	
	out.println("<div class=\"mid-box\">");
	out.println(timeelements.get(i).text());
	out.println("</div>");
	
	out.println("<div class=\"right-box\">");
	out.println("<img src=\"http://192.168.0.4/html/KR_BaseBall/logo/"+ elements.get(i*4+2).text()  + ".png\">");
	out.println("<br>");
	out.println(elements.get(i*4+2).text());
	out.println("<br>");
	out.println(elements.get(i*4+3).text());
	out.println("</div>");
	out.println("</div>");
	  
  	
  }
  out.println("</div>");
}


%>


<div id="tab_1"  style="display: none;">
<%
//팀순위/////////////////////////////////////////////
if (null != document)
{
  //Elements elements = document.select("td[width=33%],td[width=34%]");
  Elements elements = document.select("table.kbo tr td");
  //System.out.println(elements.size());
  
  out.println("<table style=\"border:1px solid;\">");
  out.println("<th>팀명</th>");
  out.println("<th>경기</th>");
  out.println("<th>승</th>");
  out.println("<th>무</th>");
  out.println("<th>패</th>");
  out.println("<th>승률</th>");
  out.println("<th>게임차</th>");
  for(int i =0 ; i < 10; i++)
  {
	  out.println("<tr>");
	  
	  for(int j = 0; j < 7; j++)
	  {
		  out.println("<td>");
		  if(j == 0)
			  out.println("<img src=\"http://192.168.0.4/html/KR_BaseBall/logo/"+ elements.get(i*7).text()  + ".png\">");
		  out.println(elements.get(i*7+j).text() + "</td>");
	  }
	  out.println("</tr>");	  
  }
  out.println("</table>");
}

%>
</div>


</body>
</html>