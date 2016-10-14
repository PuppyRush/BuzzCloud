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

        <link href="stylesheet.css" media="screen" rel="stylesheet" type="text/css" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js" type="text/javascript"></script>
        <script src="application.js" type="text/javascript"></script>
        
        <base target="_blank">
</head>
<body>

	<div id="content">
           <dl id="rank-list">
               <dd>
					<ol>
					<%
					Document document = Jsoup.connect("http://news.naver.com/").get();
					if (null != document)
					{
					  	Elements elements = document.select("div.lnb_today li");
						System.out.println(elements.size());
						for(int i = 0; i < elements.size(); i++)
							out.println("<li>" + elements.get(i).html() + "</li>");
					}
					%>
					</ol>
               </dd>
           </dl>
       </div>

</body>
</html>