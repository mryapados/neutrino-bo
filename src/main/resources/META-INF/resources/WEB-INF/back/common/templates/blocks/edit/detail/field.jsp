<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<c:set var="assignType" value="text" />
<my:debug>
	<p>EDIT<p>
	<p>finalField.type = ${finalField.type}<p>
	<p>finalField.name = ${finalField.name}<p>
	<p>${objectType}_${finalParentObject.id}_${finalField.name}</p>
	<p>finalFieldType = ${finalFieldType}</p>
	<c:set var="assignType" value="text" />
</my:debug> 

<c:set var="FINAL_MAX_ELEMENT" value="3" />
<c:choose>
	<c:when test="${finalFieldType eq 'BOOLEAN'}">
		<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
		<div class="">
			<form:checkbox path="${finalField.name}" />
		</div>
		<c:if test="${not empty fieldError}">
			<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'INTEGER'}">
		<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
		<div class="form-group${not empty fieldError ? ' has-error has-feedback' : ''}">
			<form:input type="number" cssClass="form-control" path="${finalField.name}" />
			<c:if test="${not empty fieldError}">
				<span class="glyphicon glyphicon-remove form-control-feedback"></span>
			</c:if>
		</div>
		<c:if test="${not empty fieldError}">
			<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'VARCHAR50' || finalFieldType eq 'COLOR' || finalFieldType eq 'ICON'}">
		<c:choose>
			<c:when test="${not empty finalField.enumDatas}">
				<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
				<div class="form-group${not empty fieldError ? ' has-error has-feedback' : ''}">
					<form:select class="form-control" path="${finalField.name}">
					    <form:option value="0" label="Select One" />
					    <form:options items="${finalField.enumDatas}" />
					</form:select>
					<c:if test="${not empty fieldError}">
						<span class="glyphicon glyphicon-remove form-control-feedback"></span>
					</c:if>
				</div>
				<c:if test="${not empty fieldError}">
					<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
				</c:if>
			</c:when>
			<c:otherwise>
				<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
				<div class="form-group${not empty fieldError ? ' has-error has-feedback' : ''}">
					<form:input cssClass="form-control" type="text" path="${finalField.name}"/>
					<c:if test="${not empty fieldError}">
						<span class="glyphicon glyphicon-remove form-control-feedback"></span>
					</c:if>
				</div>
				<c:if test="${not empty fieldError}">
					<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
				</c:if>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${finalFieldType eq 'PASSWORD'}">
		<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
		<div class="form-group${not empty fieldError ? ' has-error has-feedback' : ''}">
			<form:input cssClass="form-control" type="password" path="${finalField.name}"/>
			<c:if test="${not empty fieldError}">
				<span class="glyphicon glyphicon-remove form-control-feedback"></span>
			</c:if>
		</div>
		<c:if test="${not empty fieldError}">
			<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'VARCHAR255'}">
		<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
		<div class="form-group${not empty fieldError ? ' has-error has-feedback' : ''}">
			<form:textarea cssClass="form-control" rows="2" path="${finalField.name}"/>
			<c:if test="${not empty fieldError}">
				<span class="glyphicon glyphicon-remove form-control-feedback"></span>
			</c:if>
		</div>
		<c:if test="${not empty fieldError}">
			<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'TEXT'}">	
		<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
		<div class="form-group${not empty fieldError ? ' has-error has-feedback' : ''}">
			<form:textarea cssClass="form-control" rows="5" path="${finalField.name}"/>
			<c:if test="${not empty fieldError}">
				<span class="glyphicon glyphicon-remove form-control-feedback"></span>
			</c:if>
		</div>
		<c:if test="${not empty fieldError}">
			<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'HTML'}">
		<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
		<div class="form-group${not empty fieldError ? ' has-error has-feedback' : ''}">
			<jsp:include page="wysiwyg.jsp" />
		</div>
		<c:if test="${not empty fieldError}">
			<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'DATETIME'}">
		<jsp:include page="datetime.jsp" />
	</c:when>
	<c:when test="${finalFieldType eq 'TOBJECT' || finalFieldType eq 'NTOBJECT'}">
		<c:if test="${empty isInCollection || not isInCollection}">
			<form:input cssClass="form-control" type="${assignType}" path="${finalField.name}" ng-model="${finalField.name}" assign="${objectType}_${finalParentObject.id}_${finalField.name}" />
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'FILE'}">
		<c:if test="${empty isInCollection || not isInCollection}">
			<form:input cssClass="form-control" type="${assignType}" path="${finalField.name}" ng-model="${finalField.name}" assign="${objectType}_${finalParentObject.id}_${finalField.name}" kind="file"/>
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'IMAGE'}">
		<c:if test="${empty isInCollection || not isInCollection}">
			<form:input cssClass="form-control" type="${assignType}" path="${finalField.name}" ng-model="${finalField.name}" assign="${objectType}_${finalParentObject.id}_${finalField.name}" kind="file"/>
		</c:if>
	</c:when>
	<c:when test="${finalFieldType eq 'OBJECT'}">
		<c:if test="${empty finalObject}">
			<span class="empty-field"><s:message code="bo.field.empty" text="Empty..." /></span>
		</c:if>
		<c:choose>
			<c:when test="${finalField.className eq 'Lang'}">
				<h1>NOTHING TO DO</h1>
			</c:when>
			<c:when test="${finalField.className eq 'MapTemplate'}">
				<c:choose>
					<c:when test="${finalParentObject.id eq finalObject.model.id}">
						<c:set var="template" value="${finalObject.block}" />
					</c:when>
					<c:otherwise>
						<c:set var="template" value="${finalObject.model}" />
					</c:otherwise>
				</c:choose>
				<a class="linked" href="<c:url value='${boContext}/view/?type=Template&id=${template.id}' />"><c:out value="${template.name}"/></a> / <a class="linked" href="<c:url value='${boContext}/view/?type=Position&id=${finalObject.position.id}' />"><c:out value="${finalObject.position.name}"/></a>
			</c:when>
			<c:when test="${finalField.className eq 'Folder'}">
				<form:input cssClass="form-control" type="${assignType}" path="${finalField.name}" ng-model="${finalField.name}" assign="${finalParentObject.objectType}_${finalParentObject.id}_${finalField.name}" />
			</c:when>
			<c:otherwise>
				<a class="linked" href="<c:url value='${boContext}/view/?type=${finalObject.objectType}&id=${finalObject.id}' />"><c:out value="object"/></a>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${finalFieldType eq 'COLLECTION'}">
		<c:choose>
			<c:when test="${finalField.ofType eq 'FILE'}">
				<form:input cssClass="form-control" type="${assignType}" path="${finalField.name}" ng-model="${finalField.name}" assign="${objectType}_${finalParentObject.id}_${finalField.name}" kind="file" many="true" />
			</c:when>
			<c:otherwise>
				<form:input cssClass="form-control" type="${assignType}" path="${finalField.name}" ng-model="${finalField.name}" assign="${objectType}_${finalParentObject.id}_${finalField.name}" />
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${finalFieldType eq 'ENUM'}">
		<c:set var="fieldError"><form:errors path="${finalField.name}"/></c:set>
		<div class="form-group${not empty fieldError ? ' has-error has-feedback' : ''}">
			<c:forEach var="item" items="${finalField.enumDatas}">
				<label class="radio-inline"><form:radiobutton path="${finalField.name}" value="${item}" /><s:message code="bo.field.enum.${item}" text="${item}" /></label>
			</c:forEach>
		</div>
		<c:if test="${not empty fieldError}">
			<div class="alert alert-danger"><strong>Error !</strong> ${fieldError}</div>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:out value="${finalObject}"/>
	</c:otherwise>
</c:choose>
