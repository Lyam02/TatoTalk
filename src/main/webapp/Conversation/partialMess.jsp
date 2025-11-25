<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>


<c:set var="currentDate" value=""/>

<c:forEach var="mess" items="${messages}" varStatus="status">

    <c:set var="messageDate" value="${fn:substring(mess.edited_at, 0, 10)}" />

    <c:if test="${messageDate != currentDate}">
        <c:if test="${!status.first}">
            <div class="date-separator">
                <div class="date-line"></div>
                <c:set var="dateParts" value="${fn:split(messageDate, '-')}" />

                <span class="date-text">
                         ${dateParts[2]}-${dateParts[1]}-${dateParts[0]}
                </span>
                <div class="date-line"></div>
            </div>
        </c:if>
        <c:set var="currentDate" value="${messageDate}" />
    </c:if>

    <c:if test="${mess.sendTo.id eq sessionUserId}">
        <div class="message d-flex justify-content-start mb-3" data-messageid="${mess.id}">
            <div style="max-width: 75%;">
                <div class="bg-white p-3 rounded-3 shadow-sm" style="word-wrap: break-word; overflow-wrap: break-word;">
                    <input type="hidden" id="messageId" value="${mess.id}">
                        ${mess.message_content}
                        <c:if test="${mess.fichier != null}">
                            <div class="mt-2 pt-2 border-top">
                                <a href="${mess.fichier.stockage_url}" target="_blank" class="btn btn-sm btn-light border d-inline-flex align-items-center text-decoration-none text-dark w-100 justify-content-start mb-1">
                                    <i class="bi bi-paperclip text-secondary me-2"></i>
                                    <span class="text-truncate">${mess.fichier.file_name}</span>
                                    <%--<i class="bi bi-download ms-auto text-secondary"></i>--%>
                                </a>
                            </div>
                        </c:if>
                    </div>
                <small class="text-muted ms-2 text">${fn:substring(mess.edited_at, 11, 16)}</small>
            </div>
        </div>
    </c:if>

    <c:if test="${mess.sendTo.id eq employeeSendTo.id}">
        <div class="d-flex justify-content-end mb-3">
            <div style="max-width: 75%;" class="message-container">
                <button onclick="editMessage(this)" data-message-content="${mess.message_content}" data-message-id="${mess.id}"  class="edit-mess btn btn-sm position-absolute end-0 hover-btn" style="top: 0;">
                    <i class="fs-6 bi-pencil-fill"></i>
                </button>
                <div class="text-white p-3 rounded-3 text" style="background: #3DB6AE; word-wrap: break-word; overflow-wrap: break-word;">
                    ${mess.message_content}
                    <c:if test="${mess.fichier != null}">
                        <div class="mt-2 pt-2 border-top border-white-50">
                            <a href="${mess.fichier.stockage_url}" target="_blank" class="btn btn-sm btn-light border d-inline-flex align-items-center text-decoration-none text-dark w-100 justify-content-start mb-1">
                                <i class="bi bi-paperclip text-secondary me-2"></i>
                                <span class="text-truncate">${mess.fichier.file_name}</span>
                                <%--<i class="bi bi-download ms-auto text-secondary"></i>--%>
                            </a>
                        </div>
                    </c:if>
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

<style>

    .date-separator {
        display: flex;
        align-items: center;
        margin: 20px 0;
        gap: 15px;
    }

    .date-line {
        flex: 1;
        height: 1px;
        background-color: #ddd;
    }

    .date-text {
        color: #888;
        font-size: 0.85rem;
        font-weight: 500;
        white-space: nowrap;
        padding: 5px 10px;
        background-color: #f5f5f5;
        border-radius: 12px;
    }

    .message-container {
        position: relative;
    }

    .message-container .hover-btn {
        opacity: 0;
        transition: opacity 0.2s ease;
        pointer-events: none;
        transform: translateY(-50%);
    }

    .message-container:hover .hover-btn {
        opacity: 1;
        pointer-events: auto;
    }
</style>