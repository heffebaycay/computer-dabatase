<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="u" uri="/WEB-INF/utils.tld" %>

<jsp:include page="include/header.jsp" />

<section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${ computer.id }
                    </div>
                    <h1><spring:message code="computer_form.edit_computer_title"/></h1>
                   
                   <c:if test="${ msgValidationFailed == true }">
                        <div id="msgErrors" class="alert alert-danger">
                            <p>
                                <spring:message code="computer_form.validation_failed_msg" arguments="<strong>,</strong>,<br />" />
                            </p>
                        </div>
                   </c:if>
                   <c:if test="${ msgSuccess == true }">
                        <div id="msgComputerUpdated" class="alert alert-success">
                            <p>
                                <spring:message code="computer_form.update_success_msg" arguments="<strong>,</strong>" />
                            </p>
                        </div>
                   </c:if>
                   <c:if test="${ bAddSuccess != null && bAddSuccess == true }">
                   		<div id="msgComputerAdded" class="alert alert-success">
                            <spring:message code="computer_form.creation_success_msg" arguments="<strong>,</strong>,<br />" />
                   		</div>
                   </c:if>
                   
                   <spring:url value="/computers/edit?id=${ computer.id }" htmlEscape="true" var="formAction" />
                   
                   <form:form action="${ formAction }" method="POST" modelAttribute="computerDTO">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><spring:message code="computer_form.name_label" /></label>
                                <c:choose>
                                    <c:when test="${ computerNameValue == null }">
                                        <c:set var="computerNameValue" value="${ computer.name }" />
                                    </c:when>
                                </c:choose>
                                <spring:message code="computer_form.name_placeholder" var="name_placeholder" />
                                <form:input type="text" class="form-control" id="computerName" name="computerName" placeholder="${ name_placeholder }" path="name" value="${ computerNameValue }" />
                                <form:errors path="name" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="computer_form.introduced_label" /></label>
                                <c:choose>
                                    <c:when test="${ dateIntroducedValue == null }">
                                        <c:set var="dateIntroducedValue" value="${ computer.introduced }" />
                                    </c:when>
                                </c:choose>
                                <spring:message code="computer_form.introduced_placeholder" var="introduced_placeholder"/>
                                <form:input type="text" class="form-control" id="introduced" name="introduced" placeholder="${ introduced_placeholder }" path="introduced" value="${ dateIntroducedValue }" />
                                <form:errors path="introduced" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="computer_form.discontinued_label" /></label>
                                <c:choose>
                                    <c:when test="${ dateDiscontinuedValue == null }">
                                        <c:set var="dateDiscontinuedValue" value="${ computer.discontinued }" />
                                    </c:when>
                                </c:choose>
                                <spring:message code="computer_form.discontinued_placeholder" var="discontinued_placeholder"/>
                                <form:input type="text" class="form-control" id="discontinued" name="discontinued" placeholder="${ discontinued_placeholder }" path="discontinued" value="${ dateDiscontinuedValue }" />
                                <form:errors path="discontinued" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="companyId"><spring:message code="computer_form.company_label" /></label>
                                <form:select class="form-control" id="companyId" name="companyId" path="companyId">
                                    <form:option value="-1">--</form:option>
                                    <c:forEach items="${ companies }" var="company">
                                        <c:choose>
                                            <c:when test="${ companyIdValue == null }">
                                                <c:choose>
                                                    <c:when test="${ company.id == computer.companyId }">
                                                        <form:option value="${ company.id }" selected="selected">${ company.name }</form:option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form:option value="${ company.id }">${ company.name }</form:option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${ company.id == companyIdValue }">
                                                        <form:option value="${ company.id }" selected="selected">${ company.name }</form:option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form:option value="${ company.id }">${ company.name }</form:option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </form:select>
                                <form:errors path="companyId" cssClass="error"></form:errors>
                            </div>
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="hidden" name="computerId" id="computerId" value="${ computer.id }" />
                            <input type="submit" value="<spring:message code="computer_form.edit_button" />" class="btn btn-primary">
                            or
                            <a href="<c:url value="/" />" class="btn btn-default"><spring:message code="computer_form.cancel_button" /></a>
                        </div>
                   </form:form>
                </div>
            </div>
        </div>
    </section>

<jsp:include page="include/footer.jsp" />