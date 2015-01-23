<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                   
                    <form action="<c:url value="/computers/add" />" method="POST">
                        <input type="hidden" value="0"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" id="computerName" name="computerName" placeholder="Computer name" value="<c:if test="${ computerNameValue != null }" ><c:out value="${ computerNameValue }" /></c:if>">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="text" class="form-control" id="introduced" name="introduced" placeholder="Introduced date" value="<c:if test="${ dateIntroducedValue != null }"><c:out value="${ dateIntroducedValue }" /> </c:if>">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="text" class="form-control" id="discontinued" name="discontinued" placeholder="Discontinued date" value="<c:if test="${ dateDiscontinuedValue != null }"><c:out value="${ dateDiscontinuedValue }" /> </c:if>">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="companyId">
                                    <option value="-1">--</option>
                                    <c:forEach items="${ companies }" var="company">
                                    	<option value="${ company.id }" <c:if test="${ companyIdValue != null && company.id == companyIdValue }"></c:if>><c:out value="${ company.name }" /></option>
                                    </c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary" id="submit">
                            or
                            <a href="<c:url value="/" />" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>


<jsp:include page="include/footer.jsp" />