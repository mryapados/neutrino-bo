<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:init test="${!initialized}"/>

<jsp:include page="detail/init.jsp" />
<div class="view-object">
	<h1>
		<s:message code="bo.view" text="${objectType}" />
		<s:message code="bo.${objectType}.entity.name" text="${objectType}" />
	</h1>
	
	<c:if test="${not empty error}">
		<div class="alert alert-danger alert-dismissable">
			<strong><s:message code="bo.error.remove" text="Remove error : " /></strong><br />
			${error.message}
		</div>
	</c:if>
	
	<div class="row">
		<div class="col-xs-12">
			<jsp:include page="detail/toolbar.jsp">
				<jsp:param value="top" name="position"/>
			</jsp:include>
		</div>
	</div>

	<data-uib-tabset active="active">
	
		<c:forEach var="tab" items="${fields}" varStatus="status">
			<data-uib-tab index="${status.index}" heading="${not empty tab.key ? tab.key : 'data'}">
	
				<div class="container-fluid">
					<div class="row info">
						<div class="col-md-6 col-xs-12 text-left">
							<c:if test="${not empty objectName}">
								<s:message code="bo.field.name" text="Name" /> : <span>${objectName}</span>
							</c:if>
						</div>
						<ul class="col-md-6 col-xs-12 text-right">
							<li>Status : to do workflow</li>
							<li>
								<c:if test="${not empty objectLang}">
									<s:message code="bo.field.lang" text="Lang" /> : <span class="lang-sm lang-lbl" lang="${objectLang.code}"></span>
								</c:if>
							</li>
						</ul>
					</div>
				</div>

				<c:forEach var="group" items="${tab.value}">
				<div class="panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							${not empty group.key ? group.key : 'data'}
						</h4>
					</div>
					<table class="table table-striped" role="grid">
						<tbody>
							<c:forEach var="field" items="${group.value}">
								<c:if test="${field.inView}">
									<tr>
										<td class="col-md-3">
											<s:message var="defaultMessage" code="bo.field.${field.name}" text="${field.name}" />
											<s:message code="bo.${objectType}.field.${field.name}" text="${defaultMessage}" />
										</td>
										<td>
											<c:set var="finalParentObject" value="${objectView}" scope="request" />
											<c:set var="finalObject" value="${objectView[field.name]}" scope="request" />
											<c:set var="finalField" value="${field}" scope="request" />
											<c:set var="finalFieldType" value="${finalField.type}" scope="request" />
											
											<jsp:include page="detail/field.jsp" />
											
											<c:remove var="finalParentObject"/>
											<c:remove var="finalObject"/>
											<c:remove var="finalField"/>
											<c:remove var="finalFieldType"/>
										</td>
									</tr>
								</c:if>
								
							</c:forEach>
						</tbody>
					</table>
				</div>
				</c:forEach>

			</data-uib-tab>
		</c:forEach>
	
	</data-uib-tabset>
	
	<div class="row">
		<div class="col-xs-12">
			<jsp:include page="detail/toolbar.jsp">
				<jsp:param value="bottom" name="position"/>
			</jsp:include>
		</div>
	</div>
	
</div>