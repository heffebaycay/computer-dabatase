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
                    <h1><spring:message code="computer_form.add_computer_title" /></h1>
                   
                   <c:if test="${ msgValidationFailed == true}">
	                   	<div class="alert alert-danger" id="msgErrors">
	             			<p>
                                <spring:message code="computer_form.validation_failed_msg" arguments="<strong>,</strong>,<br />" />
                            </p>
	             		</div>
                   </c:if>
                   
                   <spring:url value="/computers/add" htmlEscape="true" var="formAction" />
                   <form:form modelAttribute="computerDTO" action="${ formAction }" method="POST" id="computerForm">
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName"><spring:message code="computer_form.name_label" /></label>
                                <spring:message code="computer_form.name_placeholder" var="name_placeholder" />
                                <form:input type="text" class="form-control" id="computerName" placeholder="${ name_placeholder }"  path="name"/>
                                <form:errors path="name" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="computer_form.introduced_label" /></label>
                                <spring:message code="computer_form.introduced_placeholder" var="introduced_placeholder"/>
                                <form:input type="text" class="form-control" id="introduced" placeholder="${ introduced_placeholder }" path="introduced"/>
                                <form:errors path="introduced" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="computer_form.discontinued_label" /></label>
                                <spring:message code="computer_form.discontinued_placeholder" var="discontinued_placeholder"/>
                                <form:input type="text" class="form-control" id="discontinued" placeholder="${ discontinued_placeholder }" path="discontinued"/>
                                <form:errors path="discontinued" cssClass="error"></form:errors>
                            </div>
                            <div class="form-group">
                                <label for="companyId"><spring:message code="computer_form.company_label" /></label>
                                <form:select class="form-control" id="companyId" path="companyId">
                                    <form:option value="-1">--</form:option>
                                    <c:forEach items="${ companies }" var="company">
                                        <c:choose>
                                            <c:when test="${ company.id == computerDTO.companyId }">
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
                            <input type="submit" value="<spring:message code="computer_form.add_button" />" class="btn btn-primary" id="submit">
                            <spring:message code="computer_form.button_separator" />
                            <a href="<c:url value="/" />" class="btn btn-default"><spring:message code="computer_form.cancel_button" /></a>
                        </div>
                   </form:form>
                </div>
            </div>            
        </div>
    </section>
    <spring:message code='binding.date.format' var="dateFormat"/>
    <script src="<c:url value="/js/jquery.validate.min.js"/>"></script>
    <script type="text/javascript">
    
    
        $(document).ready(function(){
            
            $.validator.addMethod("dateLocale", function(value, element) {
                var regex = /<spring:message code='binding.date.regex'/>/;
                return this.optional(element) || regex.test(value);
            }, "<spring:message code='front.validation.dateLocale.message' />");
            
            var validator = $("#computerForm").validate({
                rules: {
                    name: {
                        required: true,
                    },
                    introduced: {
                        dateLocale: true
                    },
                    discontinued: {
                        dateLocale: true
                    }
                },
                messages: {
                    name: "<spring:message code='front.validation.computer.name.required' javaScriptEscape='true' />",
                    introduced: "<spring:message code='front.validation.computer.introduced.format' arguments='${ dateFormat }' javaScriptEscape='true' />",
                    discontinued: "<spring:message code='front.validation.computer.discontinued.format' arguments='${ dateFormat }' javaScriptEscape='true' />"
                }
            });
        });
    
        
    </script>


<jsp:include page="include/footer.jsp" />