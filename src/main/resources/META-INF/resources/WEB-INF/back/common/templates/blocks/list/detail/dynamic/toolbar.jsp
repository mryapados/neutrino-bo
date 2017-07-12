<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<c:set var="position" value="${param.position}"/>
<div class="btn-toolbar" role="toolbar">

	<c:if test="${not empty displayPageTools &&  displayPageTools}">
		<div class="btn-group" role="group" aria-label="...">
		    <div class="btn-group${position eq 'bottom' ? ' dropup' : ''}" role="group" uib-dropdown >
			    <button id="${position}-page-size-btn" type="button" class="btn btn-default" uib-dropdown-toggle>
			      	<strong><c:out value="${pSize}"/></strong> by page <span class="caret"></span>
			    </button>
				<ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="${position}-page-size-btn">
					<li><a href="#" data-ng-click="updateUrlPageableSize(5)">5</a></li>
					<li><a href="#" data-ng-click="updateUrlPageableSize(10)">10</a></li>
					<li><a href="#" data-ng-click="updateUrlPageableSize(25)">25</a></li>
					<li><a href="#" data-ng-click="updateUrlPageableSize(50)">50</a></li>
					<li><a href="#" data-ng-click="updateUrlPageableSize(100)">100</a></li>
				</ul>
		    </div>
		    
			<div class="btn-group${position eq 'bottom' ? ' dropup' : ''}" role="group" uib-dropdown >
			    <button id="${position}-sort-btn" type="button" class="btn btn-default" uib-dropdown-toggle>
					<s:message var="defaultMessage" code="bo.field.${pFirstSortName}" text="${pFirstSortName}" />
					<s:message var="fieldName" code="bo.${objectType}.field.${pFirstSortName}" text="${defaultMessage}" />
					Sort by <strong><c:out value="${fieldName}"/></strong> <span class="caret"></span>
			    </button>
				<ul class="dropdown-menu" uib-dropdown-menu role="menu" aria-labelledby="${position}-sort-btn">
					<c:forEach var="field" items="${fields}" varStatus="status">
						<c:if test="${field.inList && field.type ne 'COLLECTION'}">
							<s:message var="defaultMessage" code="bo.field.${field.name}" text="${field.name}" />
							<s:message var="fieldName" code="bo.${objectType}.field.${field.name}" text="${defaultMessage}" />
							<li>
								<a href="#" data-ng-click="updateUrlPageableSort('${field.name},${pFirstSortDirection}')">${fieldName}</a>
							</li>
						</c:if>
					</c:forEach>
				</ul>
		    </div>
			
			<a href="#" class="btn btn-default${pFirstSortDirection eq 'ASC' ? ' active' : ''}" data-ng-click="updateUrlPageableSort('${pFirstSortName},ASC')">ASC</a>
			<a href="#" class="btn btn-default${pFirstSortDirection eq 'DESC' ? ' active' : ''}" data-ng-click="updateUrlPageableSort('${pFirstSortName},DESC')">DESC</a>

		</div>
	</c:if>
	<c:if test="${not empty displayActionBtn &&  displayActionBtn}">
		<div class="btn-group" role="group" aria-label="...">
			<div class="btn-group" role="group">
				<button id="delete_button" type="submit" class="btn btn-danger">
					<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Remove
				</button>
				
				<c:url var="url" value="${boContext}/new/" scope="request">
					<c:param name="type" value="${objectType}"/>
				</c:url>
				<a href="${url}" role="button" class="btn btn-primary">
					<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add
				</a>
			</div>
		</div>
	</c:if>
	
	<c:if test="${not empty displayPagination &&  displayPagination}">
	    <div class="btn-group pull-right">
			<jsp:include page="pagination.jsp" />
	    </div>
    </c:if>
</div>