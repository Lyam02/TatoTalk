<%@ page import="java.net.InetAddress" %>
<%@ include file="../Shared/header.jsp" %>

<div class="position-fixed top-0 end-0 m-3 badge bg-primary" style="z-index: 9999; font-size: 14px;">
  <i class="bi bi-server"></i> <%= InetAddress.getLocalHost().getHostName() %>
</div>

<%@ include file="../Shared/footer.jsp" %>