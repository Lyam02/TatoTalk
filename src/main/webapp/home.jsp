<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Shared/header.jsp" %>

<div class="container-fluid vh-100 d-flex flex-column p-0">

    <a href="logout">Logout</a>

    <div class="row h-100 g-0">
        <div class="col-6">
            <div class="overflow-auto flex-grow-1">
                <c:forEach var="employe" items="${employees}">
                    <div class="d-flex align-items-center p-3">
                        <div class="rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center me-3 fw-bold" style="width: 50px; height: 50px; min-width: 50px;">
                                ${fn:toUpperCase(fn:substring(employe.prenom, 0, 1))}${fn:toUpperCase(fn:substring(employe.nom, 0, 1))}
                        </div>
                        <div class="flex-grow-1 overflow-hidden">
                            <div class="d-flex justify-content-between align-items-start">
                                <div class="flex-grow-1">
                                    <h6><a href="conv?employeeId=${employe.id}" target="#partialConv" class="mb-0 text-black text-decoration-none">${employe.prenom} ${employe.nom}</a></h6>
                                </div>
                                <div class="ms-2 flex-shrink-0">
                                    <small class="text-muted">12:34</small>
                                </div>
                            </div>
                            <div>
                                <small class="text-muted text-truncate d-block" style="max-width: 150px;">hhhhhhhhhhhhhhhhhhhhhhhhhhhh</small>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="col-6 d-flex overflow-auto flex-column border-start" id="partialConv">

        </div>
    </div>


</div>



<%@ include file="Shared/footer.jsp"%>
