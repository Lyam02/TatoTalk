<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="text-white p-3 shadow-sm border-bottom">
  <div class="d-flex align-items-center">
    <div class="rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center me-3 fw-bold" style="width: 45px; height: 45px;">
      ${fn:toUpperCase(fn:substring(employee.prenom, 0, 1))}${fn:toUpperCase(fn:substring(employee.nom, 0, 1))}
    </div>
    <div>
      <h5 class="mb-0 text-black">${employee.prenom} ${employee.nom}</h5>
    </div>
  </div>
</div>

<div class="flex-grow-1 overflow-auto p-4 bg-opacity-10" onload-load="mess?employeeId=${employee.id}" id="messages" style="max-height: calc(100vh - 200px);">

</div>

<div class="p-3 border-top">
  <form action="mess" target="#messages">
    <div class="input-group">
      <input type="text" class="form-control bg-white me-1" name="message" placeholder="Tapez votre message...">
      <input type="hidden" value="${employee.id}" name="employeeId">
      <button class="btn px-2" type="submit" style="background-color: #5aa1c3"><i class="bi bi-send"></i></button>
    </div>
  </form>
</div>
