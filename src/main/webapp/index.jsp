<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
</head>
<body>
  <%
    int unePage = 1;
    if (request.getParameter("unePage") != null) {
      unePage = Integer.parseInt(request.getParameter("unePage"));
    }

    switch (unePage) {
      case 1:%><jsp:include page="connexion/connexion.jsp" /><% break;
      case 2:%><jsp:include page="connexion/mdpOublie.jsp" /><% break;
      case 3:%><jsp:include page="connexion/mdpOublie.jsp" /><% break;
      case 4:break;
    }
  %>
</body>
</html>
