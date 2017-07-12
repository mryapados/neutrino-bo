<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<my:init test="${!initialized}"/>

<!DOCTYPE html>
<html lang="${language}">
<my:head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags --%>
	<meta name="description" content="">
	<meta name="author" content="Cédric Sevestre">
	<link rel="icon" href="favicon.ico">
	
	<title><spring:message code="project.pages.home.title" arguments="${project}" /></title>
</my:head>
<my:body>

	<angular-filemanager navbar multi-select />

</my:body>
</html>

<!-- 	<angular-filemanager multi-select="false"/> -->




































