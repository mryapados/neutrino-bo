<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>

<!-- Back office ROLE_ADMIN	-->
<div id="back-management">
    <div class="block-selection" data-ng-controller="BlockSelectionCtrl">
        <div class="outside">
            <div class="edit" data-ng-click="switchDisplay()">
                    
            </div>
        </div>
       <div class="inside closed" data-ng-class="{opened : openValue, closed : !openValue}">
            <div class="contain">
			    <h3><spring:message code="block-selection.inside.title" /></h3>
			    <h4><spring:message code="block-selection.inside.subtitle" /></h4>

				<div class="row">
					<div class="col-lg-12">
						<div class="input-group">
							<div class="input-group-btn">
								<button type="button" class="btn btn-default dropdown-toggle"
									data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="false">
									<span ng-if="currentLang == ''">
										{{'block-selection.lang.all' | translate}}
									</span>
									<span ng-if="currentLang != ''">
										{{currentLang}} 
									</span>
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownLang">
									<li><a data-ng-click="switchLang('')">{{'block-selection.lang.all' | translate}}</a></li>
									<li role="separator" class="divider"></li>
									<li data-ng-repeat="lang in langs | orderBy: 'code'">
										<a data-ng-click="switchLang(lang.code)">{{lang.code | uppercase}}</a>
									</li>
								</ul>
							</div>
							<input type="text" class="form-control" placeholder="Recherche" data-ng-model="search">
						</div>
					</div>
				</div>

				<div class="col-xs-12">
					<ul class="list-block">
						<li data-ng-repeat="block in templates | orderBy: 'name' | filter : {lang:{code:currentLang}, $ : search} ">
							<div drag="block" dragStyle="columnDrag">
								<p class="block-title"><strong>{{block.name}}</strong></p>
								<p><strong>{{'block-selection.block.path' | translate}} : </strong>{{block.path}}</p>
								<p><strong>{{'block-selection.block.dateadd' | translate}} : </strong>{{block.dateAdd | date}}</p>
								<p><strong>{{'block-selection.block.description' | translate}} : </strong>{{block.description}}</p>
								<p><strong>{{'block-selection.block.lang' | translate}} : </strong>{{block.lang.code}}</p>
							</div>
						</li>
					</ul>
				</div>

				
            </div>
        </div>
    </div>
    <div class="debug-info">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<h3>Debug info</h3>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
					<p>User : <em><c:out value="${surfer.name}"/></em></p>
					<p>Role admin : <em><c:out value="${surfer.admin}"/></em></p>
					<p>Preview : <em><c:out value="${blockPreview}"/></em></p>
				</div>
				<div class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
					<p>Langue : <em><spring:message code="language" /></em></p>
					<sec:authorize access="isRememberMe()">
						<p><spring:message code="debug-info.RememberMe" /></p>
					</sec:authorize>
					<sec:authorize access="isFullyAuthenticated()">
						<p><spring:message code="debug-info.isFullyAuthenticated" /></p>
					</sec:authorize>
				</div>
			</div>
		</div>
	</div>
</div>


<my:import url="/admin/components/angularLibs.jsp" />

<!-- BlockManagement app -->
<script src="<c:url value='/resources/src/back/js/app.js'/>"></script>	
<script src="<c:url value='/resources/src/back/js/controller/blockManagementCtrl.js'/>"></script>	
<script src="<c:url value='/resources/src/back/js/controller/blockSelectionCtrl.js'/>"></script>
<script src="<c:url value='/resources/src/back/js/controller/templateModalCtrl.js'/>"></script>
<script src="<c:url value='/resources/src/back/js/controller/uiPositionCtrl.js'/>"></script>
<script src="<c:url value='/resources/src/back/js/filters/backFilters.js'/>"></script>
<script src="<c:url value='/resources/src/back/js/service.js'/>"></script>
<script src="<c:url value='/resources/src/back/js/services/repositories.js'/>"></script>
<script src="<c:url value='/resources/src/back/js/services/business.js'/>"></script>
<script src="<c:url value='/resources/src/back/js/provider/config.js'/>"></script>
<script src="<c:url value='/resources/src/back/js/directives/backDirectives.js'/>"></script>

<c:url var="serverRestUrl" value="/@back"/>	
<c:url var="templateUrl" value="/resources/src/back/js/templates"/>
<c:url var="i18nUrl" value="/resources/src/back/js/i18n"/>
<script>
	angular.module('backApp').config(['backConfigProvider', function (config) {
    var defaults = config.$get();
    config.set({
    	basePath: '${pageContext.request.contextPath}',
    	serverRest: '${serverRestUrl}',
    	tplPath: '${templateUrl}',
    	i18nPath: '${i18nUrl}'
    });
    
  }]);
</script>
