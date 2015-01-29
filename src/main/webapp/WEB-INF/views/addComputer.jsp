<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="include/header.jsp" />

<section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>Add computer</h1>
                   
                   <c:if test="${ errors != null && errors.size() > 0}">
	                   	<div class="alert alert-danger" id="msgErrors">
	             			<strong>Oh snap!</strong> It seems you left some mistakes on our sweet form.
	             			<c:forEach items="${ errors }" var="error">
	             				<p>${ error }</p>
	             			</c:forEach>
	             		</div>
                   </c:if>
                   
                   <spring:url value="/computers/add" htmlEscape="true" var="formAction" />
                   <form:form modelAttribute="computerDTO" action="${ formAction }" method="POST">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <form:input type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name"  path="name" value="${ computerNameValue }"/>
                                <form:errors path="name" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <form:input type="text" class="form-control" id="introduced" name="introduced" placeholder="Introduced date" path="introduced" value="${ dateIntroducedValue }" />
                                <form:errors path="introduced" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <form:input type="text" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date" path="discontinued" value="${ dateDiscontinuedValue }"/>
                                <form:errors path="discontinued" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <form:select class="form-control" id="companyId" name="companyId" path="companyId">
                                    <form:option value="-1">--</form:option>
                                    <c:forEach items="${ companies }" var="company">
                                        <c:choose>
                                            <c:when test="${ company.id == companyIdValue }">
                                                <form:option value="${ company.id }" selected="selected">${ company.name }</form:option>
                                            </c:when>
                                            <c:otherwise>
                                                <form:option value="${ company.id }">${ company.name }</form:option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </form:select>
                                <form:errors path="companyId" cssClass="error"></form:errors>
                            </div>  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary" id="submit">
                            or
                            <a href="<c:url value="/" />" class="btn btn-default">Cancel</a>
                        </div>
                   </form:form>
                </div>
            </div>            
        </div>
    </section>


<jsp:include page="include/footer.jsp" />