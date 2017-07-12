<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:formatDate var="dateObj" value="${finalObject}" pattern="yyyy-MM-dd HH:mm:ss" />
<div ng-controller="DatepickerPopupCtrl" ng-init="init('${dateObj}')">
	<p class="input-group">
		<input type="text" 
			name="${finalField.name}"
			class="form-control"
			uib-datepicker-popup="{{format}}" 
			ng-model="dt"
			is-open="popup1.opened" 
			datepicker-options="dateOptions"
			ng-required="true" 
			close-text="Close"
			alt-input-formats="altInputFormats" 
		/>
		<span class="input-group-btn">
			<button type="button" class="btn btn-default" ng-click="open1()">
				<i class="glyphicon glyphicon-calendar"></i>
			</button>
		</span>
	</p>
</div>



