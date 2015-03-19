<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<script type="text/javascript">
    var params = new Array();
    params['language.switch.url'] = '<c:url value="/SetLanguage" />'
    function changeLanguage(targetLanguage) {
        $.ajax({
            url: params['language.switch.url'] + '?lang=' + targetLanguage
        }).done(function(){
            window.location.reload();
        })
        ;
    }
</script>

</body>
</html>

