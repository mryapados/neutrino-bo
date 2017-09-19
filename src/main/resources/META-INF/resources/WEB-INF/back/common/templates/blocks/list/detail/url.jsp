<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<c:url var="url" value="" scope="request">
	<c:param name="type" value="${param.type}"/>
	<c:if test="${not empty param.lang}">
		<c:param name="lang" value="${param.lang}"/>
	</c:if>
	<c:if test="${not empty param.page}">
		<c:param name="page" value="${param.page}"/>
	</c:if>
	<c:if test="${not empty param.size}">
		<c:param name="size" value="${param.size}"/>
	</c:if>
	<c:if test="${not empty param.sort}">
		<c:param name="sort" value="${param.sort}"/>
	</c:if>
</c:url>
<c:if test="${empty param.print || param.print eq true}">
	<c:if test="${not empty param.css}">
		<c:set var="css" value='class="${param.css}"'/>
	</c:if>
	<c:if test="${not empty param.role}">
		<c:set var="role" value='role="${param.role}"'/>
	</c:if>
	<a ${css}href="<%= request.getAttribute("url") %>"${role}>${param.expr}</a>
</c:if>
