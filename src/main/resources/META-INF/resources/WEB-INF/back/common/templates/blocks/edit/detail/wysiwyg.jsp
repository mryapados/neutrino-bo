<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div ng-controller="WysiwygEditorCtrl" class="app">
	<text-angular name="${finalField.name}">
		<c:out value="${finalObject}" escapeXml="false"/>
	</text-angular>
	<div>Editor <span>{{version}}</span></div>
</div>





