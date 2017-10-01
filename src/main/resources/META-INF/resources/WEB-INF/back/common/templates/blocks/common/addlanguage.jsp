<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<button id="add_button" class="btn btn-primary" uib-dropdown-toggle>
	<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Translate <span class="lang-sm" lang="${language}"></span> <span class="caret"></span>
</button>
<ul uib-dropdown-menu class="dropdown-menu" role="menu">
	<jsp:useBean id="translations" class="java.util.LinkedHashMap"/>
	<my:translations beanId="${objectView.id}" var="ts" active="false" />
	<c:forEach var="t" items="${ts}" varStatus="status">
		<c:set target="${translations}" property="${t.lang.code}">
			<c:url value="${boContext}/view">
				<c:param name="type" value="${t.objectType}" />
				<c:param name="id" value="${t.id}"/>
			</c:url>
		</c:set>
	</c:forEach>
	<c:forEach var="lang" items="${langs}" varStatus="status">
		<c:choose>
			<c:when test="${lang eq objectLang}">
			
			</c:when>
			<c:when test="${not empty translations[lang.code]}">
				<li>
					<a href="${translations[lang.code]}"><span class="lang-sm lang-lbl-full" lang="${lang.code}"></span></a>
				</li>
			</c:when>
			<c:otherwise>
				<li>
					<c:url var="url" value="${boContext}/new/translation">
						<c:param name="type" value="${objectType}" />
						<c:param name="lg" value="${lang.code}" />
						<c:param name="id" value="${objectView.id}"/>
					</c:url>
					<a href="<%= pageContext.getAttribute("url") %>"><span class="lang-sm lang-lbl-full" lang="${lang.code}"></span></a>
				</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>