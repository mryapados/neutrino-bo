<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- Block for ROLE_ADMIN	-->
<c:set var="blockPreviewActivated" value="" />
<c:if test="${blockPreview}">
	<c:set var="blockPreviewActivated" value="active" />
</c:if>
<div id="block-preview">
	<c:url var="formAction" value="/@action/setpreview"/>
	<form:form action="${formAction}" method="post">
		<input type="hidden" name="preview" value="${!blockPreview}">
		<button type="submit" class="btn btn-primary ${blockPreviewActivated}">
			<span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
		</button>
	</form:form>
</div>

			
			