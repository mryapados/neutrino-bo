<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>

<%-- Here set all necessary initialization for alone component like blocks --%>
<!-- jquery -->
<script src="<c:url value='/resources/src/lib/bower_components/jquery/dist/jquery.min.js'/>"></script>
<script src="<c:url value='/resources/src/lib/bower_components/bootstrap/dist/js/bootstrap.min.js'/>"></script>

<c:set var="nFrontAppModules" value="'boApp'" scope="request" />
<my:import url="/admin/components/angularLibs.jsp" />
<c:remove var="nFrontAppModules" />

<!-- bo libs -->
<script src="<c:url value='/resources/src/lib/bower_components/textAngular/dist/textAngular-rangy.min.js'/>"></script>
<script src="<c:url value='/resources/src/lib/bower_components/textAngular/dist/textAngular-sanitize.min.js'/>"></script>
<script src="<c:url value='/resources/src/lib/bower_components/textAngular/dist/textAngular.min.js'/>"></script>

<script src="<c:url value='/resources/src/lib/bower_components/ng-file-upload/ng-file-upload.min.js'/>"></script>
<script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/dist/angular-filemanager.min.js'/>"></script>

<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/app.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/directives/directives.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/filters/filters.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/providers/config.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/entities/chmod.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/entities/item.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/services/apihandler.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/services/apimiddleware.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/services/filenavigator.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/providers/translations.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/controllers/main.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/lib/bower_components/angular-filemanager/src/js/controllers/selector-controller.js'/>"></script> --%>


<!-- bo app -->
<script src="<c:url value='/resources/dist/bo/bo.js'/>"></script>

<%-- <script src="<c:url value='/resources/src/bo/js/app.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/bo/js/controller/datepickerPopupCtrl.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/bo/js/controller/wysiwygEditorCtrl.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/bo/js/service.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/bo/js/provider/config.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/bo/js/directives/uiAssignment.js'/>"></script> --%>
<%-- <script src="<c:url value='/resources/src/bo/js/directives/uiFile.js'/>"></script> --%>

<c:url var="templateUrl" value="/resources/src/bo/js/templates"/>
<c:url var="i18nUrl" value="/resources/src/bo/js/i18n"/>
<c:url var="fileUrl" value="/files"/>
<script>
	angular.module('boApp').config(['boConfigProvider', function (config) {
    var defaults = config.$get();
    config.set({
    	basePath: '${pageContext.request.contextPath}',
    	tplPath: '${templateUrl}',
    	i18nPath: '${i18nUrl}',
        filePath: '${fileUrl}'
    });
    
  }]);
</script>
