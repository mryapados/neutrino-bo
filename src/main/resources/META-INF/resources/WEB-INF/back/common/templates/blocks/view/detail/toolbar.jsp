<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="deleteUrl" value="${boContext}/remove/?type=${objectType}"/>
<form:form id="delete" action="${deleteUrl}" method="post">
	<input type="hidden" name="id" value="${object.id}"/>

	<c:set var="position" value="${param.position}"/>
	<div class="btn-toolbar" role="toolbar" style="${position eq 'top' ? ' margin-bottom:15px;' : ' margin-top:15px;'}">
		<div class="btn-group" role="group" aria-label="...">
			<div class="btn-group" role="group">
			
				<a href="<c:url value='${boContext}/edit/?type=${objectType}&id=${objectView.id}' />" class="btn btn-primary">
					<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
					<s:message code="bo.view.button.edit" text="Edit" />
				</a>
				<a href="<c:url value='${boContext}/new/?type=${objectType}&id=${objectView.id}' />" class="btn btn-primary">
					<span class="glyphicon glyphicon-paste" aria-hidden="true"></span>
					<s:message code="bo.view.button.duplicate" text="Duplicate" />
				</a>

				<c:if test="${not empty objectLang}">
					<div class="btn-group${position eq 'bottom' ? ' dropup' : ''}" role="group" uib-dropdown >
						<button id="add_button" class="btn btn-primary" uib-dropdown-toggle>
	                       <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Translate <span class="lang-sm" lang="${language}"></span> <span class="caret"></span>
	                   	</button>
						<ul uib-dropdown-menu class="dropdown-menu" role="menu">
							<c:forEach var="item" items="${langs}" varStatus="status">
								<li>
									<c:url var="url" value="${boContext}/new/translation/">
										<c:param name="type" value="${objectType}" />
										<c:param name="lg" value="${item.code}" />
										<c:param name="id" value="${objectView.id}"/>
									</c:url> <a href="${url}"><span class="lang-sm lang-lbl-full" lang="${item.code}"></span></a>
								</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>

				<button id="delete_button" type="submit" class="btn btn-danger">
					<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Remove
				</button>
				
				
				<c:if test="${not empty boViewUrl}">
					<c:if test="${true}">
						<c:catch var="exception">
							<c:set var="folders" value="${objectView.folders}"/>
							<div class="btn-group${position eq 'bottom' ? ' dropup' : ''}" role="group" uib-dropdown >
								<button id="add_button" class="btn btn-success" uib-dropdown-toggle>
			                       <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span> <s:message code="bo.view.button.viewUrl" text="See on front office" /> <span class="caret"></span>
			                   	</button>
								<ul uib-dropdown-menu class="dropdown-menu" role="menu">
									<c:forEach var="folder" items="${folders}">
										<li>
											<my:url var="url" value="${boViewUrl}" bean="${objectView}">
												<my:param name="servername" value="${folder.name}" />
											</my:url>
											<a href="<%= pageContext.getAttribute("url") %>" target="_blank"><c:out value="${folder.name}"/></a>
										</li>
									</c:forEach>
								</ul>
							</div>
						</c:catch>
					</c:if>
					<c:if test="${not empty exception}">
						<c:catch var="exception">
							<c:set var="folder" value="${objectView.folder}"/>
							<my:url var="url" value="${boViewUrl}" bean="${objectView}">
								<my:param name="servername" value="${folder.name}" />
							</my:url>
							<a href="${url}" target="_blank" class="btn btn-success">
								<span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
								<s:message code="bo.view.button.viewUrl" text="See on front office" />
							</a>
						</c:catch>
					</c:if>
					<c:if test="${not empty exception}">
						<my:url var="url" value="${boViewUrl}" bean="${objectView}"/>
						<a href="${url}" target="_blank" class="btn btn-success">
							<span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
							<s:message code="bo.view.button.viewUrl" text="See on front office" />
						</a>
					</c:if>
				</c:if>

			</div>
		</div>
		
		<div class="btn-group pull-right" role="group" aria-label="...">
			<div class="btn-group" role="group">
			
				<a href="<c:url value='${boContext}/list/?type=${objectType}' />" class="btn btn-primary">
					<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>
					<s:message code="bo.edit.button.goback" text="Come back" />
				</a>
	
			</div>
		</div>
	</div>
</form:form>