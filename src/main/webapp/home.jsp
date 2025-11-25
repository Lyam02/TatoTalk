<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Shared/header.jsp" %>

<div class="container-fluid d-flex flex-column p-0" style="height: 100vh;">
    <div class="d-flex justify-content-center my-3">
        <div class="input-group" style="width: 900px;">
                <span class="input-group-text border-end-0" style="font-size: 1.1rem; background-color: #f0f1f0;">
                    <i class="bi bi-search"></i>
                </span>
            <input id="search" type="search" class="form-control border-start-0" placeholder="Recherche" aria-label="Search" aria-describedby="search-addon" style="font-size: 1.1rem; padding: 0.6rem; background-color: #f0f1f0;" />
        </div>
    </div>

    <div class="row h-100 g-0 mt-2">
        <div class="col-6">
            <div class="overflow-auto flex-grow-1" style="max-height: calc(100vh - 135px);">
                <c:forEach var="employe" items="${employees}">
                    <c:if test="${employe.id ne sessionUserId}">
                        <div href="conv?employeeId=${employe.id}" target="#partialConv" class="mb-0 text-black text-decoration-none user d-flex align-items-center p-3"
                             data-username="${employe.prenom} ${employe.nom}"
                             data-employeeid="${employe.id}">
                            <div class="rounded-circle bg-secondary text-white d-flex align-items-center justify-content-center me-3 fw-bold" style="width: 50px; height: 50px; min-width: 50px;">
                                    ${fn:toUpperCase(fn:substring(employe.prenom, 0, 1))}${fn:toUpperCase(fn:substring(employe.nom, 0, 1))}
                            </div>
                            <div class="flex-grow-1 overflow-hidden">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div class="flex-grow-1">
                                        <h6>${employe.prenom} ${employe.nom}</h6>
                                    </div>
                                    <div class="ms-2 flex-shrink-0">
                                        <small class="text-muted message-time" id="message-time-${employe.id}"></small>
                                    </div>
                                </div>
                                <div>
                                    <small class="text-muted text-truncate d-block message-preview"
                                           id="message-preview-${employe.id}"
                                           style="max-width: 150px;"></small>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>

        <div class="col-6 d-flex overflow-auto flex-column border-start" id="partialConv" style="height: calc(100vh - 90px);">

        </div>
    </div>
</div>

<style>
    .hidden {
        display: none !important;
    }

    .user:hover {
        background-color: #e9f8f8 !important;
    }

</style>

<script>
    function editMessage(button){
        const messageId = $(button).data('message-id');
        const messageContent = $(button).data('message-content');

        $('#send').val(messageContent);
        $('#messId').val(messageId);
    }
</script>

<script>
    $(document).ready(function(){
        $('#search').on('input', function (){

            var searchtext = $(this).val().toLowerCase();

            if (searchtext === '') {
                $('.user').removeClass('hidden');
            } else {
                $('.user').each(function (){
                    var username = $(this).data('username').toLowerCase();

                    if (username.includes(searchtext)){
                        $(this).removeClass('hidden');
                    } else {
                        $(this).addClass('hidden');
                    }
                });
            }
        });
    });
</script>

<script>

    function loadLastMessages() {
        $.get('lastMessage', function(response) {

            $.each(response, function(employeeId, messageData) {
                $('#message-preview-' + employeeId).text(messageData.messageContent);

                // Mettre à jour l'heure
                $('#message-time-' + employeeId).text(messageData.createdAt);
            });
        }, 'json').fail(function() {
            console.error('Erreur lors du chargement des derniers messages');
        });
    }

    $(document).ready(function() {
        loadLastMessages();
    });
</script>

<script>
    setInterval(function refresh (){

        loadLastMessages();

        var lastMessageDiv = $('.message[data-messageid]').last();
        var messageid = lastMessageDiv.data('messageid');

        var employee = $('.employee[data-employeeid]').last();
        var employeeid = employee.data('employeeid');

        $.get('verif', {
            messageId: messageid,
            employeeId: employeeid
        },function (response){
            if (response.isTheNewest===false){
                $.get("mess",{
                    employeeId: employeeid
                },function (resp){
                    $("#messages").html(resp);
                })
            }
        },'json')
    }, 3000)

</script>

<script>
    function clearInput(){
        if ($('#send').val() !== ""){
            setTimeout(function(){$('#send').val('');},100)
            setTimeout(function (){$('#messId').val('0')}, 100)
            setTimeout(function (){$('#hidden-file-input').val('');},100)
            setTimeout(function (){$('#file-preview').addClass('d-none');},100)
        }
    }
</script>

<%@ include file="Shared/footer.jsp"%>