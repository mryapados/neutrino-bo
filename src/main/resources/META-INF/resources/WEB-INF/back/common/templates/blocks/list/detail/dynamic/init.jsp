<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="displayActionBtn" value="${false}" scope="request" />
<c:set var="displayPagination" value="${true}" scope="request" />
<c:set var="displayPageTools" value="${true}" scope="request" />
<c:set var="linkTarget" value="_blank" scope="request" />

<c:set var="pNumber" value="${objectDatas.number}" scope="request"/>
<c:set var="pNumberOfElements" value="${objectDatas.numberOfElements}" scope="request"/>

<c:set var="pSize" value="${objectDatas.size}" scope="request"/>

<c:set var="pSort" value="${objectDatas.sort}" scope="request"/>
<c:set var="pSortPart" value="${fn:split(pSort, ',')}" scope="request"/>

<c:set var="pFirstSort" value="${pSortPart[0]}" scope="request"/>
<c:set var="pFirstSortPart" value="${fn:split(pFirstSort, ':')}" scope="request"/>

<c:set var="pFirstSortName" value="${pFirstSortPart[0]}" scope="request"/>
<c:set var="pFirstSortDirection" value="${fn:trim(pFirstSortPart[1])}" scope="request"/>

<c:set var="pTotalElements" value="${objectDatas.totalElements}" scope="request"/>
<c:set var="pTotalPages" value="${objectDatas.totalPages}" scope="request"/>

<c:set var="pBegin" value="${pNumber * pSize}" scope="request"/>
<c:set var="pEnd" value="${pBegin + pNumberOfElements}" scope="request"/>

<c:set var="finalNMaxPages" value="3" scope="request"/>