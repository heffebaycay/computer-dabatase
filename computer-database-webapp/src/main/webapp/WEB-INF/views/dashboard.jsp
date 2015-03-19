<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="/WEB-INF/utils.tld" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="include/header.jsp" />

<section id="main">
        <c:set var="searchQuery" value="${ u:escapeHtml( searchWrapper.searchQuery ) }" />
        <div class="container">
            <h1 id="homeTitle">
                <spring:message code="dashboard.n_computers_msg" arguments="${searchWrapper.totalCount}" />
            </h1>
            
            <c:if test="${ bRemoveSuccess == true }">
            	<div id="msgComputerAdded" class="alert alert-success">
                    <spring:message code="dashboard.delete_success_msg" arguments="<strong>,</strong>" />
            	</div>
            </c:if>
            
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <spring:message code="dashboard.search_placeholder" var="search_placeholder" />
                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="${ search_placeholder }" value="<c:out value="${searchQuery}"/>"/>
                        <spring:message code="dashboard.search_filter_button" var="search_filter_button" />
                        <input type="submit" id="searchsubmit" value="${ search_filter_button }" class="btn btn-primary" />
                    </form>
                </div>
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <div class="pull-right">
                        <a class="btn btn-success" id="addComputer" href="<c:url value="/computers/add" />"><spring:message code="dashboard.add_computer_button" /></a>
                        <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();"><spring:message code="dashboard.edit_button" /></a>
                    </div>
                </sec:authorize>
            </div>
        </div>

        <form id="deleteForm" action="<c:url value="/computers/delete"/>" method="POST">
            <input type="hidden" name="selection" value="">
        </form>

        <div class="container" style="margin-top: 10px;">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <!-- Variable declarations for passing labels as parameters -->
                        <!-- Table header for Computer Name -->

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                            <spring:message code="dashboard.name_label" /> <a href="<c:url value="${ u:generateDashboardRoute(1, searchQuery, 'name', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateDashboardRoute(1, searchQuery, 'name', 'desc') }" />">D</a>
                        </th>
                        <th>
                            <spring:message code="dashboard.introduced_label" /> <a href="<c:url value="${ u:generateDashboardRoute(1, searchQuery, 'introduced', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateDashboardRoute(1, searchQuery, 'introduced', 'desc') }" />">D</a>
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                            <spring:message code="dashboard.discontinued_label" /> <a href="<c:url value="${ u:generateDashboardRoute(1, searchQuery, 'discontinued', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateDashboardRoute(1, searchQuery, 'discontinued', 'desc') }" />">D</a>
                        </th>
                        <!-- Table header for Company -->
                        <th>
                            <spring:message code="dashboard.company_label" /> <a href="<c:url value="${ u:generateDashboardRoute(1, searchQuery, 'company', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateDashboardRoute(1, searchQuery, 'company', 'desc') }" />">D</a>
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                	<c:forEach items="${searchWrapper.results}" var="computer">
                		<tr>
                			<td class="editMode">
                				<input type="checkbox" name="cb" class="cb" value="${computer.id}" />
                			</td>
                			<td>
                				<a href="<c:url value="/computers/edit?id=${ computer.id }"/>" onclick=""><c:out value="${ computer.name }" /></a>
                			</td>
                			<td><c:out value="${ computer.introduced }" /></td>
                			<td><c:out value="${ computer.discontinued }"/></td>
                			<c:choose>
                				<c:when test="${ computer.company != null}">
                					<td><c:out value="${ computer.company.name }" /></td>
                				</c:when>
                				<c:otherwise>
                					<td>&nbsp;</td>
                				</c:otherwise>
                			</c:choose>
                		</tr>
                	</c:forEach>
                </tbody>
            </table>
        </div>
        
        <script type="text/javascript">
        	var strings = new Array();
        	strings['delete_confirm_msg'] = "<spring:message code='dashboard.delete_confirm_msg' javaScriptEscape='true' />";
        	strings['view_button'] = "<spring:message code='dashboard.view_button' javaScriptEscape='true' />";
        	strings['edit_button'] = "<spring:message code='dashboard.edit_button' javaScriptEscape='true' />";
        </script>
        
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
        	<c:set var="urlPattern" value="${ u:generateDashboardRoute(\"%d\", searchQuery, searchWrapper.sortCriterion, searchWrapper.sortOrder) }" />
        	<t:pagination urlPattern="${ urlPattern }" totalPage="${ searchWrapper.totalPage }" currentPage="${ searchWrapper.currentPage }" delta="${ 3 }"></t:pagination>
        
        </div>

    </footer>


<jsp:include page="include/footer.jsp" />

