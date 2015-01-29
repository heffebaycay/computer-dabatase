<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<jsp:include page="include/header.jsp" />

<section id="main">
    <div class="container">
        <div class="alert alert-danger">
            <h2>400 - Bad request!</h2>
            <br/>
            <c:if test="${ requestScope['javax.servlet.error.message'] != null }">
            	<div style="font-size: large; padding-top: 2%">
                	<p>Reason: ${requestScope['javax.servlet.error.message']} </p>
                	<!-- stacktrace -->
            	</div>
            </c:if>
            
        </div>
    </div>
</section>

<jsp:include page="include/footer.jsp" />