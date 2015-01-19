<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" uri="/WEB-INF/utils.tld" %>

<jsp:include page="include/header.jsp" />

<section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${ computer.id }
                    </div>
                    <h1>Edit Computer</h1>
                   
                   <c:if test="${ errors != null }">
                   	<c:choose>
                   		<c:when test="${ errors.size() > 0 }">
                   			<div class="alert alert-danger">
                   				<strong>Oh snap!</strong> It seems you left some mistakes on our sweet form.
                   				<c:forEach items="${ errors }" var="error">
                   					<p>${ error }</p>
                   				</c:forEach>
                   			</div>
                   		</c:when>
                   		<c:otherwise>
                   			<div class="alert alert-success">
                   				<strong>Success!</strong> Your modifications were saved successfully.
                   			</div>
                   		</c:otherwise>
                   	</c:choose>
                   </c:if>
                   
                   <c:if test="${ bAddSuccess != null && bAddSuccess == true }">
                   		<div class="alert alert-success">
                   			<strong>Success!</strong> Computer was added successfully. Feel free to take a moment to check for any typo!
                   		</div>
                   </c:if>
                   
                    <form action="<c:url value="/computers/edit?id=${ computer.id }" />" method="POST">
                        <input type="hidden" value="0"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name" value="<c:choose><c:when test="${ computerNameValue != null }">${ computerNameValue }</c:when><c:otherwise><c:if test="${ computer != null }">${ computer.name }</c:if></c:otherwise></c:choose>">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" id="introduced" name="introduced" placeholder="Introduced date" value="<c:choose><c:when test="${ dateIntroducedValue != null }">${ dateIntroducedValue }</c:when><c:otherwise><c:if test="${ computer != null }">${ u:formatDateTime(computer.introduced) }</c:if></c:otherwise></c:choose>">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date" value="<c:choose><c:when test="${ dateDiscontinuedValue != null }">${ dateDiscontinuedValue }</c:when><c:otherwise><c:if test="${ computer != null }">${ u:formatDateTime(computer.discontinued) }</c:if></c:otherwise></c:choose>">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="companyId">
                                    <option value="-1">--</option>
                                    <c:forEach items="${ companies }" var="company">
                                    	<option value="${ company.id }"
                                    		<c:choose>
                                    			<c:when test="${ companyIdValue != null }">
                                    				<c:if test="${ company.id == companyIdValue }">
                                    					selected="selected"
                                    				</c:if>
                                    			</c:when>
                                    			<c:otherwise>
                                    				<c:if test="${ computer.company != null && company.id == computer.company.id }">
                                    					selected="selected"
                                    				</c:if>
                                    			</c:otherwise>
                                    		</c:choose> > ${company.name } </option>
                                    </c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                        	<input type="hidden" name="computerId" value="${ computer.id }" />
                            <input type="submit" value="Edit" class="btn btn-primary">
                            or
                            <a href="dashboard.html" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>

<jsp:include page="include/footer.jsp" />