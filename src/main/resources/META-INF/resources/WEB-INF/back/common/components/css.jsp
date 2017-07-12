<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%-- Here set all CSS files would be load to end of head --%>
<link href="<c:url value='/resources/src/lib/bower_components/bootstrap/dist/css/bootstrap.min.css'/>" rel="stylesheet">
<link href="<c:url value='/style/font-awesome/css/font-awesome.min.css'/>" rel="stylesheet">
<link href="<c:url value='/style/bootstrap-languages/languages.min.css'/>" rel="stylesheet"/>


<link href="<c:url value='/resources/src/lib/bower_components/textAngular/dist/textAngular.css'/>" rel="stylesheet"/>
<link href="<c:url value='/resources/src/lib/bower_components/angular-filemanager/dist/angular-filemanager.min.css'/>" rel="stylesheet">

<link href="<c:url value='/style/back.css'/>" rel="stylesheet">
<link href="<c:url value='/resources/src/bo/css/ui-file.css'/>" rel="stylesheet">

<c:forEach items="${boResources}" var="boResource" varStatus="status">
	<c:if test="${boResource.type eq 'CSS'}">
		<link href="<c:url value='${boResource.value}'/>" rel="stylesheet">
	</c:if>
</c:forEach>