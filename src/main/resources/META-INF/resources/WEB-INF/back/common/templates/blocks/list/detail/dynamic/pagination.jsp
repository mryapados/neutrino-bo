<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="my" uri="/WEB-INF/taglibs/neutrino.tld" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ page import="java.util.SortedSet" %>
<%@ page import="java.util.TreeSet" %>

<ul class="pagination">
	<c:set var="first" value="1" />
	<c:set var="last" value="${pTotalPages}" />
	<c:set var="max" value="${finalNMaxPages}" />
	<c:set var="active" value="${pNumber + 1 >= last ? last : pNumber + 1}" />
	<%
		int active = Integer.parseInt(pageContext.getAttribute("active").toString());
		int first = Integer.parseInt(pageContext.getAttribute("first").toString());
		int last = Integer.parseInt(pageContext.getAttribute("last").toString());
		int max = Integer.parseInt(pageContext.getAttribute("max").toString());
		int count = max;

		SortedSet<Integer> r = new TreeSet<Integer>();
		r.add(first);
		r.add(active);
		r.add(last);
		
		int p = 0;
		int sens = -1;
		if (active >= last / 2){
			sens = 1;
		}
		for (int m = 1; m <= max; m++){
			for (int i = 0; i <= 1; i++){
				if (count == 0) break;
				p = active + (m * sens);
				if (p < last && p > first){
					count--;
					r.add(p);
				}
				sens = -sens;
			}
		}
		pageContext.setAttribute("resultSet", r);
	%>
	
	<li class="paginate_button previous${active eq 1 ? ' disabled' : ''}">
		<a href="#" data-ng-click="updateUrlPageablePage(${active - 2})">Previous</a>
	</li>
	<c:forEach var="page" items="${resultSet}" varStatus="status">
		<c:set var="classActive" value="" />
		<c:if test="${page - previous > 1}">
			<li class="disabled"><a href="">...</a></li>
		</c:if>
		<li${page eq active ? ' class=\"active\"' : ''}>
			<a href="#" data-ng-click="updateUrlPageablePage(${page - 1})">${page}</a>
		</li>
		<c:set var="previous" value="${page}" />
	</c:forEach>
	<li class="paginate_button next${active eq last ? ' disabled' : ''}">
		<a href="#" data-ng-click="updateUrlPageablePage(${active})">Next</a>
	</li>
</ul>