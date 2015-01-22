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
                ${totalCount} Companies found
            </h1>
            
            <c:if test="${ bRemoveSuccess == true }">
            	<div id="msgCompanyAdded" class="alert alert-success">
            		<strong>Success!</strong> The companies you selected were successfully deleted.
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
                    <a class="btn btn-success" id="addComputer" href="<c:url value="/companies/add" />">Add Company</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="<c:url value="/companies/delete"/>" method="POST">
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
                        	Company Id <a href="<c:url value="${ u:generateCompanyRoute(1, searchQuery, 'id', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateCompanyRoute(1, searchQuery, 'id', 'desc') }" />">D</a>
                        </th>
                        <th>
                            Company name <a href="<c:url value="${ u:generateCompanyRoute(1, searchQuery, 'name', 'asc') }" />">A</a> / <a href="<c:url value="${ u:generateCompanyRoute(1, searchQuery, 'name', 'desc') }" />">D</a>
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                	<c:forEach items="${requestScope.companies}" var="company">
                		<tr>
                			<td class="editMode">
                				<input type="checkbox" name="cb" class="cb" value="${company.id}" />
                			</td>
                			<td>
                				<c:out value="${ company.id }" />
                			</td>
                			<td>
                				<a href="<c:url value="/companies/edit?id=${ company.id }"/>" onclick=""><c:out value="${ company.name }" /></a>
                			</td>
                		</tr>
                	</c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <footer class="navbar-fixed-bottom">
        <div class="container text-center">
        
        	<c:set var="urlPattern" value="${ u:generateCompanyRoute(\"%d\", searchQuery, sortCriterion, sortOrder) }" />
        	<t:pagination urlPattern="${ urlPattern }" totalPage="${ totalPage }" currentPage="${ currentPage }" delta="${ 3 }"></t:pagination>

	        <div class="btn-group btn-group-sm pull-right" role="group" >
	            <button type="button" class="btn btn-default">10</button>
	            <button type="button" class="btn btn-default">50</button>
	            <button type="button" class="btn btn-default">100</button>
	        </div>
        
        </div>

    </footer>


<jsp:include page="include/footer.jsp" />

