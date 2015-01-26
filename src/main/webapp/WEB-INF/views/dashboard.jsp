<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="/WEB-INF/utils.tld" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
.
<jsp:include page="include/header.jsp" />

<section id="main">
        <div class="container">
            <h1 id="homeTitle">
                ${searchWrapper.totalCount} Computers found
            </h1>
            
            <c:if test="${ bRemoveSuccess == true }">
            	<div id="msgComputerAdded" class="alert alert-success">
            		<strong>Success!</strong> The computers you selected were successfully deleted.
            	</div>
            </c:if>
            
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="<c:url value="/computers/add" />">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
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
                            Computer name <a href="<c:url value="${ u:generateDashboardRoute(1, searchWrapper.searchQuery, 'name', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateDashboardRoute(1, searchWrapper.searchQuery, 'name', 'desc') }" />">D</a>
                        </th>
                        <th>
                            Introduced date <a href="<c:url value="${ u:generateDashboardRoute(1, searchWrapper.searchQuery, 'introduced', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateDashboardRoute(1, searchWrapper.searchQuery, 'introduced', 'desc') }" />">D</a>
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                            Discontinued date <a href="<c:url value="${ u:generateDashboardRoute(1, searchWrapper.searchQuery, 'discontinued', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateDashboardRoute(1, searchWrapper.searchQuery, 'discontinued', 'desc') }" />">D</a>
                        </th>
                        <!-- Table header for Company -->
                        <th>
                            Company <a href="<c:url value="${ u:generateDashboardRoute(1, searchWrapper.searchQuery, 'company', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateDashboardRoute(1, searchWrapper.searchQuery, 'company', 'desc') }" />">D</a>
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
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
        
        	<c:set var="urlPattern" value="${ u:generateDashboardRoute(\"%d\", searchWrapper.searchQuery, searchWrapper.sortCriterion, searchWrapper.sortOrder) }" />
        	<t:pagination urlPattern="${ urlPattern }" totalPage="${ searchWrapper.totalPage }" currentPage="${ searchWrapper.currentPage }" delta="${ 3 }"></t:pagination>

	        <div class="btn-group btn-group-sm pull-right" role="group" >
	            <button type="button" class="btn btn-default">10</button>
	            <button type="button" class="btn btn-default">50</button>
	            <button type="button" class="btn btn-default">100</button>
	        </div>
        
        </div>

    </footer>


<jsp:include page="include/footer.jsp" />

