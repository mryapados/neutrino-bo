<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<my:init test="${!initialized}"/>

<li uib-dropdown>
	<a href="#" id="my-menu" uib-dropdown-toggle role="button">${nDatas.title.value} <span class="caret"></span></a>
	<ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="my-menu">
		<c:forEach var="item" items="${nDatas.links.value}" varStatus="status">
			<li><a href="${item.value.url}">${item.value.title}</a></li>
		</c:forEach>
	</ul>
</li>


			