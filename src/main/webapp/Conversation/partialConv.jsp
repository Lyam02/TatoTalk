<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="text-white p-3 shadow-sm border-bottom">
  <div class="d-flex align-items-center">
    <div class="rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center me-3 fw-bold" style="width: 45px; height: 45px;">
      ${fn:toUpperCase(fn:substring(employee.prenom, 0, 1))}${fn:toUpperCase(fn:substring(employee.nom, 0, 1))}
    </div>
    <div class="employee" data-employeeid="${employee.id}">
      <h5 class="mb-0 text-black">${employee.prenom} ${employee.nom}</h5>
    </div>
  </div>
</div>

<div class="flex-grow-1 overflow-auto p-4 bg-opacity-10" onload-load="mess?employeeId=${employee.id}" id="messages" style="max-height: calc(100vh - 200px);">

</div>

<div class="p-3 border-top">
  <div id="file-preview" class="d-none mb-2 bg-light p-2 rounded border position-relative">
    <div class="d-flex align-items-center">
      <i id="file-icon" class="bi fs-4 me-2"></i>
      <span id="file-name" class="text-truncate" style="max-width: 200px;">Nom du fichier</span>
      <button type="button" class="btn-close ms-auto" aria-label="Close" onclick="removeFile()"></button>
    </div>
  </div>

  <form action="mess" target="#messages" method="POST" enctype="multipart/form-data">
    <input type="file" id="hidden-file-input" name="fichierLink" style="display: none;" onchange="fileSelect(event)">

    <div class="input-group">

      <input id="send" type="text" class="form-control bg-white me-1" name="message" required placeholder="Tapez votre message...">
      <input type="hidden" value="${employee.id}" name="employeeId">

      <button class="btn px-2" type="button" onclick="document.getElementById('hidden-file-input').click()" style="background-color: #0b3d62">
        <i class="bi bi-paperclip text-white"></i>
      </button>

      <button class="btn px-2" type="submit" onclick="clearInput()" style="background-color: #0b3d62">
        <i class="bi bi-send text-white"></i>
      </button>

    </div>

  </form>

</div>

<script>
  function fileSelect(event) {
    var fileInput = event.target;
    var file = fileInput.files[0];

    var previewContainer = document.getElementById('file-preview');
    var fileNameSpan = document.getElementById('file-name');
    var fileIcon = document.getElementById('file-icon');

    if (file) {
      previewContainer.classList.remove('d-none');
      fileNameSpan.textContent = file.name;
      fileIcon.className = "bi fs-4 me-2 text-primary bi-file-earmark";
    }
  }

  function removeFile() {
    setTimeout(function (){$('#hidden-file-input').val('');},100)
    setTimeout(function (){$('#file-preview').addClass('d-none');},100)
  }

</script>