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
					<li>
						<jsp:include page="../url.jsp">
							<jsp:param name="size" value="10" />
							<jsp:param name="expr" value="10" />
						</jsp:include>
					</li>
					<li>
						<jsp:include page="../url.jsp">
							<jsp:param name="size" value="25" />
							<jsp:param name="expr" value="25" />
						</jsp:include>
					</li>
					<li>
						<jsp:include page="../url.jsp">
							<jsp:param name="size" value="50" />
							<jsp:param name="expr" value="50" />
						</jsp:include>
					</li>
					<li>
						<jsp:include page="../url.jsp">
							<jsp:param name="size" value="100" />
							<jsp:param name="expr" value="100" />
						</jsp:include>
					</li>
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
								<jsp:include page="../url.jsp">
									<jsp:param name="sort" value="${field.name},${pFirstSortDirection}" />
									<jsp:param name="expr" value="${fieldName}" />
								</jsp:include>
							</li>
						</c:if>
					</c:forEach>
				</ul>
		    </div>
	
			<jsp:include page="../url.jsp">
				<jsp:param name="sort" value="${pFirstSortName},ASC" />
				<jsp:param name="expr" value="ASC" />
				<jsp:param name="css" value="btn btn-default${pFirstSortDirection eq 'ASC' ? ' active' : ''}" />
				<jsp:param name="role" value="button" />
			</jsp:include>
			<jsp:include page="../url.jsp">
				<jsp:param name="sort" value="${pFirstSortName},DESC" />
				<jsp:param name="expr" value="DESC" />
				<jsp:param name="css" value="btn btn-default${pFirstSortDirection eq 'DESC' ? ' active' : ''}" />
				<jsp:param name="role" value="button" />
			</jsp:include>
	
		</div>
	</c:if>
	<c:if test="${not empty displayActionBtn &&  displayActionBtn}">
		<div class="btn-group" role="group" aria-label="...">
			<div class="btn-group" role="group">
				<button id="delete_button" type="submit" class="btn btn-danger">
					<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Remove
				</button>


				<c:choose>
					<c:when test="${objectBaseType eq 'Translation'}">
						<div class="btn-group${position eq 'bottom' ? ' dropup' : ''}" role="group" uib-dropdown >
							<button id="add_button" class="btn btn-primary" uib-dropdown-toggle>
		                       <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add <span class="lang-sm" lang="${language}"></span> <span class="caret"></span>
		                   	</button>
		                    <ul uib-dropdown-menu class="dropdown-menu" role="menu">
								<c:forEach var="item" items="${langs}" varStatus="status">
									<li>
										<c:url var="url" value="${boContext}/new/translation/" scope="request">
											<c:param name="type" value="${objectType}"/>
											<c:param name="lg" value="${item.code}"/>
										</c:url>
										<a href="${url}"><span class="lang-sm lang-lbl-full" lang="${item.code}"></span></a>
									</li>
								</c:forEach>
		                    </ul>
						</div>
					</c:when>
					<c:otherwise>
						<c:url var="url" value="${boContext}/new/" scope="request">
							<c:param name="type" value="${objectType}"/>
						</c:url>
						<a href="${url}" role="button" class="btn btn-primary">
							<span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add
						</a>
					</c:otherwise>
				</c:choose>





			</div>
		</div>
	</c:if>
	
	<c:if test="${not empty displayPagination &&  displayPagination}">
	    <div class="btn-group pull-right">
			<jsp:include page="pagination.jsp" />
	    </div>
    </c:if>
</div>