<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta charset="utf-8">
		<title>Computer Database</title>
		<!-- Bootstrap -->
		<link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet" media="screen">
		<link href="<c:url value="/css/font-awesome.css" />" rel="stylesheet" media="screen">
		<link href="<c:url value="/css/main.css" />" rel="stylesheet" media="screen">
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/dashboard.js"/>"></script>
	</head>
	<body>
		<header class="navbar navbar-inverse navbar-fixed-top">
        	<div class="container">
                <div class="navbar-header">
                    <a class="navbar-brand" href="<c:url value="/" />"> Application - Computer Database </a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><spring:message code="header.lang_dropdown" /><span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li><a href="?lang=fr" onclick="changeLanguage('fr'); return false;" ><img src="<c:url value="/png/fr.png" />" />  <spring:message code="header.lang_french" /></a></li>
                                <li><a href="?lang=en" onclick="changeLanguage('en'); return false;" ><img src="<c:url value="/png/gb.png" />" />  <spring:message code="header.lang_english" /></a></li>
                            </ul>
                        </li>
                    </ul>
                    
                </div>
        	</div>
    	</header>