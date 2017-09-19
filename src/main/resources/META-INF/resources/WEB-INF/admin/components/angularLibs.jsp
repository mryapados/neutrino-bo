<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>

<my:discrimin name="angularLibs">
	<!-- Angular libs -->
	<c:url var="basePath" value='/resources/src/lib/bower_components/'/>
	<script src="${basePath}angular/angular.min.js"></script>
	<script src="${basePath}angular-sanitize/angular-sanitize.min.js"></script>
	<script src="${basePath}angular-translate/angular-translate.min.js"></script>
	<script src="${basePath}angular-cookies/angular-cookies.js"></script>
	<script src="${basePath}angular-resource/angular-resource.js"></script>
	<script src="${basePath}angular-ui-bootstrap/dist/ui-bootstrap-tpls-2.5.0.min.js"></script>
	<%-- Overide this angularjs module to use angularjs on your website --%>
	<script>
		(function() {
			angular.module('frontApp', [${nFrontAppModules}]);
		}());
	</script>
</my:discrimin>
		
			