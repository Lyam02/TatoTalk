<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:forEach var="mess" items="${messages}">
    <c:if test="${mess.sendTo.id eq sessionUserId}">
        <div class="d-flex justify-content-start mb-3" data-messageid="${mess.id}">
            <div style="max-width: 75%;">
                <div class="bg-white p-3 rounded-3 shadow-sm" style="word-wrap: break-word; overflow-wrap: break-word;">
                    <input type="hidden" id="messageId" value="${mess.id}">
                        ${mess.message_content}
                </div>
                <small class="text-muted ms-2 text">${fn:substring(mess.edited_at, 11, 16)}</small>
            </div>
        </div>
    </c:if>

    <c:if test="${mess.sendTo.id eq employeeSendTo.id}">
        <div class="d-flex justify-content-end mb-3">
            <div style="max-width: 75%;">
                <div class="text-white p-3 rounded-3 text" style="background: #3DB6AE; word-wrap: break-word; overflow-wrap: break-word;">
                        ${mess.message_content}
                </div>
                <small class="text-muted me-2 text d-block text-end">${fn:substring(mess.edited_at, 11, 16)}</small>
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