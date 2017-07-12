<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="row">

	<c:choose>
		<c:when test="${blockPreview}">
			<c:set var="hasBlockA" value="${true}"/>
			<c:set var="hasBlockB" value="${true}"/>
			<c:set var="hasBlockC" value="${true}"/>
		</c:when>
		<c:otherwise>
			<my:hasBlock position="@bo_nav" var="hasBlockA"/>
			<my:hasBlock position="@bo_article" var="hasBlockB"/>
			<my:hasBlock position="@bo_aside" var="hasBlockC"/>
		</c:otherwise>
	</c:choose>

	<c:set var="showColA" value="${hasBlockA ? 'X' : 'O'}"/>
	<c:set var="showColB" value="${hasBlockB ? 'X' : 'O'}"/>
	<c:set var="showColC" value="${hasBlockC ? 'X' : 'O'}"/>
	<c:set var="showCode" value="${showColA}${showColB}${showColC}"/>
	<c:choose>
		<c:when test="${showCode eq 'XXX'}">
			<c:set var="classA" value="col-lg-2 col-md-2 col-sm-3 col-xs-12"/>
			<c:set var="classB" value="col-lg-8 col-md-8 col-sm-6 col-xs-12"/>
			<c:set var="classC" value="col-lg-2 col-md-2 col-sm-3 col-xs-12"/>
		</c:when>
		<c:when test="${showCode eq 'XXO'}">
			<c:set var="classA" value="col-lg-2 col-md-2 col-sm-3 col-xs-12"/>
			<c:set var="classB" value="col-lg-10 col-md-10 col-sm-9 col-xs-12"/>
		</c:when>
		<c:when test="${showCode eq 'XOO'}">
			<c:set var="classA" value="col-lg-12 col-md-12 col-sm-12 col-xs-12"/>
		</c:when>
		<c:when test="${showCode eq 'OXX'}">
			<c:set var="classB" value="col-lg-10 col-md-10 col-sm-9 col-xs-12"/>
			<c:set var="classC" value="col-lg-2 col-md-2 col-sm-3 col-xs-12"/>
		</c:when>
		<c:when test="${showCode eq 'OOX'}">
			<c:set var="classC" value="col-lg-12 col-md-12 col-sm-12 col-xs-12"/>
		</c:when>
		<c:when test="${showCode eq 'XOX'}">
			<c:set var="classA" value="col-lg-6 col-md-6 col-sm-6 col-xs-12"/>
			<c:set var="classC" value="col-lg-6 col-md-6 col-sm-6 col-xs-12"/>
		</c:when>
		<c:when test="${showCode eq 'OXO'}">
			<c:set var="classB" value="col-lg-12 col-md-12 col-sm-12 col-xs-12"/>
		</c:when>
	</c:choose>
	
	<c:if test="${showColA eq 'X'}">
		<nav class="${classA}">
			<my:block position="@bo_nav" />
		</nav>
	</c:if>
	<c:if test="${showColB eq 'X'}">
		<article class="${classB}">
			<my:block position="@bo_article" />
		</article>
	</c:if>
	<c:if test="${showColC eq 'X'}">
		<aside class="${classC}">
			<my:block position="@bo_aside" />
		</aside>
	</c:if>
	
</div>