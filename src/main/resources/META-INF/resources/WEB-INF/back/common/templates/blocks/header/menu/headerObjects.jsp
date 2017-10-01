<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:init test="${!initialized}"/>

<li uib-dropdown>
	<a href="#" id="my-menu" uib-dropdown-toggle role="button"><s:message htmlEscape="false" code="bo.block.header.translatedObjets" text="Translated objects" /> <span class="caret"></span></a>
	<ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="my-menu">
		<% System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZ" + request.getAttribute("translationLinks")); %>
		<c:forEach var="item" items="${translationLinks}" varStatus="status">
			<s:message htmlEscape="false" var="i18nName" code="bo.${item}.entity.name" text="${item}" />
			<li><a href="<c:url value='${boContext}/list/?type=${item}'/>">${i18nName}</a></li>
		</c:forEach>
	</ul>
</li>
<li uib-dropdown>
	<a href="#" id="my-menu" uib-dropdown-toggle role="button"><s:message htmlEscape="false" code="bo.block.header.noTranslatedObjets" text="Not translated objects" /> <span class="caret"></span></a>
	<ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="my-menu">
		<c:forEach var="item" items="${noTranslationLinks}" varStatus="status">
			<s:message htmlEscape="false" var="i18nName" code="bo.${item}.entity.name" text="${item}" />
			<li><a href="<c:url value='${boContext}/list/?type=${item}'/>">${i18nName}</a></li>
		</c:forEach>
	</ul>
</li>

			