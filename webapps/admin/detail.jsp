<%@page import="test.PhoneModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="test.JsoupTest" %>
<%@page import="test.PhoneModel" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ShowDetails</title>
<style>  
    #tbNew {
       border: #000000 1px solid;}
    #tbNew td{border: #0092d2 1px solid;
     text-align:center;
     font-size:14px;}
    table,tr,th{  
        border:1px solid #ccd;  
        border-collapse:collapse;  
    }  
    table{  
        width:100%;  
    }  
    th{  
        height:24px;  
        width:50px;/** 不管是固定像素或是百分比，应与对应数据列的宽度一致 */  
        line-height:24px;  
        background-color:#cfc;  
    }  
</style>  
<script language="javascript">  
      
    var XMLHttpReq;  
        //创建XMLHttpRequest对象         
        function createXMLHttpRequest() {  
            if(window.XMLHttpRequest) { //Mozilla 浏览器  
                XMLHttpReq = new XMLHttpRequest();  
            }  
            else if (window.ActiveXObject) { // IE浏览器  
                try {  
                    XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");  
                } catch (e) {  
                    try {  
                        XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");  
                    } catch (e) {}  
                }  
            }  
        }  
        //发送请求函数  
        function sendRequest() {  
            createXMLHttpRequest();  
            var url = "ajax.jsp";  
            XMLHttpReq.open("GET", url, true);  
            XMLHttpReq.onreadystatechange = processResponse;//指定响应函数  
            XMLHttpReq.send(null);  // 发送请求  
        }  
        // 处理返回信息函数  
        function processResponse() {  
            if (XMLHttpReq.readyState == 4) { // 判断对象状态  
                if (XMLHttpReq.status == 200) { // 信息已经成功返回，开始处理信息  
                	addRow(tbNew);  
                    setTimeout("sendRequest()", 3000);  
                } else { //页面不正常  
                    window.alert("您所请求的页面有异常。");  
                }  
            }  
        }  
        function addRow(tableid){
        	var element = XMLHttpReq.responseXML.getElementsByTagName("str");
        	if(element.length==0)
        		return;
        	var name = XMLHttpReq.responseXML.getElementsByTagName("name")[0].firstChild.nodeValue;  
            var id = XMLHttpReq.responseXML.getElementsByTagName("id")[0].firstChild.nodeValue; 
            var brand = XMLHttpReq.responseXML.getElementsByTagName("brand")[0].firstChild.nodeValue;  
           // var color = XMLHttpReq.responseXML.getElementsByTagName("color")[0].firstChild.nodeValue; 
            var orign = XMLHttpReq.responseXML.getElementsByTagName("orign")[0].firstChild.nodeValue;  
           // var system = XMLHttpReq.responseXML.getElementsByTagName("system")[0].firstChild.nodeValue; 
            var time = XMLHttpReq.responseXML.getElementsByTagName("time")[0].firstChild.nodeValue; 
            var price = XMLHttpReq.responseXML.getElementsByTagName("price")[0].firstChild.nodeValue; 
            var url = XMLHttpReq.responseXML.getElementsByTagName("url")[0].firstChild.nodeValue; 
        	//添加行
        	var newTr = tableid.insertRow();
        	//添加列
        	var newTd0 = newTr.insertCell();
        	var newTd1 = newTr.insertCell();
       		var newTd2 = newTr.insertCell();
       		var newTd3 = newTr.insertCell();
        	var newTd4 = newTr.insertCell();
       		var newTd5 = newTr.insertCell();
       		var newTd6 = newTr.insertCell();
       		//var newTd7 = newTr.insertCell();
        	//设置列内容和属性
        	newTd0.innerHTML = id;
        	newTd1.innerHTML = name;
        	newTd2.innerHTML = brand;
        	newTd3.innerHTML = time;
        	newTd4.innerHTML = orign;
        	//newTd5.innerHTML = system;
        	//newTd6.innerHTML = color;
        	newTd5.innerHTML = price;    
        	newTd6.innerHTML = url;
        	}
    </script>  
</head>
<body onload=sendRequest()>
<h1 align="center">京东百货</h1>
        <TABLE id="tbNew">
         <thead><th>商品编号</th><th>商品名称</th><th>品牌</th><th>上架时间</th><th>产地</th><th>价格</th><th>链接</th></thead>  
        </TABLE> 
        
</body>
</html>