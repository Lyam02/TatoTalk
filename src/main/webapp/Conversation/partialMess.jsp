<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:forEach var="mess" items="${messages}">
    <c:if test="${mess.sendTo.id eq sessionUserId}">
        <div class="d-flex justify-content-start mb-3 message" data-messageId="${mess.id}">
            <div class="mw-75">
                <div class="bg-white p-3 rounded-3 shadow-sm">
                    <input type="hidden" id="messageId" value="${mess.id}">
                        ${mess.message_content}
                </div>
                <small class="text-muted ms-2">${fn:substring(mess.edited_at, 11, 16)}</small>
            </div>
        </div>
    </c:if>

    <c:if test="${mess.sendTo.id eq employeeSendTo.id}">
        <div class="d-flex justify-content-end mb-3">
            <div class="d-flex flex-column align-items-end" style="max-width: 75%;">
                <div class=" text-white p-3 rounded-3" style="background: #3DB6AE">
                        ${mess.message_content}
                </div>
                <small class="text-muted me-2">${fn:substring(mess.edited_at, 11, 16)}</small>
            </div>
        </div>
    </c:if>
</c:forEach>

<script>
    (function() {
        var messagesDiv = document.getElementById('messages');
        if (messagesDiv) {
            setTimeout(function() {
                messagesDiv.scrollTop = messagesDiv.scrollHeight;
            }, 0);
        }
    })();
</script>