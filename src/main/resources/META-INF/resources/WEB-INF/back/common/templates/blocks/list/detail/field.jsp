<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



<c:set var="finalMaxElement" value="3" />
<c:set var="finalLength" value="200" />

<c:choose>
	<c:when test="${finalFieldType eq 'ICON'}">
		<span><i class="<c:out value='${finalObject}'/>" aria-hidden="true"></i><span><c:out value="${finalObject}"/></span></span>
	</c:when>
	<c:when test="${finalFieldType eq 'PASSWORD'}">
		<c:out value="********"/>
	</c:when>
	<c:when test="${finalFieldType eq 'HTML'}">
		<c:out value="${fn:substring(finalObject, 0, finalLength)}"/>
	</c:when>
	<c:when test="${finalFieldType eq 'DATETIME'}">
		<fmt:formatDate value="${finalObject}"/>
	</c:when>
	<c:when test="${finalFieldType eq 'TOBJECT' || finalFieldType eq 'NTOBJECT'}">
		<a class="linked" href="<c:url value='${boContext}/view/?type=${finalObject.objectType}&id=${finalObject.id}' />"><c:out value="${finalObject.name}"/></a>
	</c:when>
	<c:when test="${finalFieldType eq 'OBJECT'}">
		<c:choose>
			<c:when test="${empty finalObject}">
				<span class="empty-field"><s:message code="bo.field.empty" text="Empty..." /></span>
			</c:when>
			<c:when test="${finalObject.objectType eq 'Lang'}">
				<span class="lang-sm lang-lbl" lang="${finalObject.code}"></span>
			</c:when>
			<c:when test="${finalObject.objectType eq 'MapTemplate'}">
				<c:choose>
					<c:when test="${finalParentObject.id eq finalObject.model.id}">
						<c:set var="template" value="${finalObject.block}" />
					</c:when>
					<c:otherwise>
						<c:set var="template" value="${finalObject.model}" />
					</c:otherwise>
				</c:choose>
				
				<my:bind var="templateName" type="Translation" beanId="${template.id}" field="name" cache="false" />
<%-- 				<my:bind var="positionName" type="Position" beanId="${finalObject.position.id}" field="name" cache="false" /> --%>

				<a class="linked" href="<c:url value='${boContext}/view/?type=Template&id=${template.id}' />"><c:out value="${templateName}"/></a> / <a class="linked" href="<c:url value='${boContext}/view/?type=Position&id=${finalObject.position.id}' />"><c:out value="${finalObject.position.name}"/></a>
			</c:when>
			<c:when test="${finalObject.objectType eq 'Folder'}">
				<a class="linked" href="<c:url value='${boContext}/view/?type=${finalObject.objectType}&id=${finalObject.id}' />"><c:out value="${finalObject.name}"/></a>
			</c:when>
			<c:otherwise>
				<a class="linked" href="<c:url value='${boContext}/view/?type=${finalObject.objectType}&id=${finalObject.id}' />"><c:out value="object"/></a>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${finalFieldType eq 'COLLECTION'}">
		<c:set var="collection" value="${finalObject}" />
		<c:set var="size" value="${fn:length(collection)}" />
		<c:if test="${size > 0}">
			<ul class="linked">
				<c:set var="max" value="${size}" />
				
				<c:if test="${max > finalMaxElement}">
					<c:set var="max" value="${finalMaxElement}" />
				</c:if>
				<c:forEach var="i" begin="0" end="${max - 1}">
					<c:set var="finalObject" value="${collection.toArray()[i]}" scope="request" />
					<c:set var="finalFieldType" value="${finalField.ofType}" scope="request" />
					<li>
						<jsp:include page="field.jsp" />
					</li>
					<c:remove var="finalObject"/>
					<c:remove var="finalFieldType"/>
				</c:forEach>
				<c:if test="${size > finalMaxElement}">
					<li>
						<strong><a href="#"><c:out value="${size - finalMaxElement}"/> autres résultats...</a></strong>
					</li>
				</c:if>
			</ul>
		</c:if>
	</c:when>
	<c:otherwise>
		<c:out value="${finalObject}"/>
	</c:otherwise>
</c:choose>
