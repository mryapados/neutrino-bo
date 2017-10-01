<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<my:init test="${!initialized}" />

<c:if test="${!blockPreview}"><c:set var="fixedClass" value=" navbar-fixed-top"/></c:if>
<nav class="navbar navbar-default${fixedClass}">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">Neutrino</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li class="active"><a href="#">Home</a></li>
				<li uib-dropdown>
					<a href="#" uib-dropdown-toggle role="button">Support <span class="caret"></span></a>
					<ul uib-dropdown-menu class="dropdown-menu">
						<li><a href="#">eDoc</a></li>
						<li><a href="#">Javadoc</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="#">Contact</a></li>
					</ul>
				</li>
				<my:block position="@bo_headerMenu" />
			</ul>
			
			<ul class="nav navbar-nav navbar-right">
				<li uib-dropdown>
                    <a href="#" uib-dropdown-toggle>
                        <span class="fa fa-user fa-fw"></span> <span class="fa fa-caret-down"></span>
                    </a>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-user">
                        <li><a href="#"><span class="fa fa-user fa-fw"></span> <s:message htmlEscape="false" code="bo.connexion.user-profile" /></a></li>
                        <li><a href="#"><span class="fa fa-gear fa-fw"></span> <s:message htmlEscape="false" code="bo.connexion.settings" /></a></li>
                        <li class="divider"></li>
                        <li><a href="<c:url value='/logout'/>"><span class="fa fa-sign-out fa-fw"></span> <s:message htmlEscape="false" code="bo.connexion.logout" /></a></li>
                    </ul>
                </li>
                
                
				<li uib-dropdown>
                    <a href="#" uib-dropdown-toggle>
                       <span class="lang-sm lang-lbl-full" lang="${language}"></span> <span class="caret"></span>
                    </a>
                    <ul uib-dropdown-menu class="dropdown-menu" role="menu">
						<c:forEach var="item" items="${langs}" varStatus="status">
							<li>
								<a href="<c:url value='${boContext}/language/?language=${item.code}'/>">
								<span class="lang-sm lang-lbl-full" lang="${item.code}"></span></a>
							</li>
						</c:forEach>
                    </ul>
				</li>
			</ul>
		</div>

	</div>
</nav>



