<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%
String referer = request.getHeader("referer");
if (referer == null) {
	//TODO LOG 
} else {
	session.setAttribute("blockPreview", Boolean.parseBoolean(request.getParameter("preview")));
	response.sendRedirect(referer);
}
%>



