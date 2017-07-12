<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="robots" content="noindex">

    <title>Parallax Login Form - Bootsnipp.com</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="<c:url value='/style/login.css'/>" rel="stylesheet">
	
	<link href="<c:url value='/resources/src/lib/bower_components/bootstrap/dist/css/bootstrap.min.css'/>" rel="stylesheet">
</head>
<body>



	<div class="container">
	    <div class="row vertical-offset-100">
	    	<div class="col-md-4 col-md-offset-4">
	    		<div class="panel panel-default">
				  	<div class="panel-heading">
				    	<h3 class="panel-title">Please sign in</h3>
				 	</div>
				  	<div class="panel-body">
				  		<c:url var="loginUrl" value="/login" />
				    	<form:form action="${loginUrl}" accept-charset="UTF-8" role="form">
	                    <fieldset>
				    	  	<div class="form-group">
				    		    <input type="text" class="form-control" id="username" name="username" placeholder="Enter Username" required>
				    		</div>
				    		<div class="form-group">
				    			<input type="password" class="form-control" id="password" name="password" placeholder="Enter Password" required>
				    		</div>
				    		<div class="checkbox">
				    	    	<label>
				    	    		<input name="remember-me" type="checkbox"> Remember Me
				    	    	</label>
				    	    </div>
				    		<input class="btn btn-lg btn-success btn-block" type="submit" value="Login">
				    	</fieldset>
				      	</form:form>
				    </div>
				</div>
			</div>
		</div>
	</div>
	
	<script src="<c:url value='/resources/src/lib/bower_components/jquery/dist/jquery.min.js'/>"></script>
	<script src="<c:url value='/resources/vendor/TweenLite.min.js'/>"></script>
	<script type="text/javascript">
	$(document).ready(function(){
	  $(document).mousemove(function(e){
	     TweenLite.to($('body'), 
	        .5, 
	        { css: 
	            {
	                backgroundPosition: ""+ parseInt(event.pageX/8) + "px "+parseInt(event.pageY/'12')+"px, "+parseInt(event.pageX/'15')+"px "+parseInt(event.pageY/'15')+"px, "+parseInt(event.pageX/'30')+"px "+parseInt(event.pageY/'30')+"px"
	            }
	        });
	  });
	});
	</script>
</body>
</html>
