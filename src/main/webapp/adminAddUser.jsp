<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Shared/header.jsp" %>

    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        form { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px; }
        div { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="email"], input[type="password"], select {
            width: 95%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;
        }
        button { padding: 10px 15px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
        .error { color: red; font-weight: bold; }
    </style>

<h1>Ajouter un nouvel employé</h1>

<c:if test="${not empty errorMessage}">
    <p class="error">${errorMessage}</p>
</c:if>

<%-- Le action doit pointer vers l'URL de votre servlet --%>
<form action="${pageContext.request.contextPath}/admin/addUser" method="POST">

    <div>
        <label for="nom">Nom :</label>
        <input type="text" id="nom" name="nom" required>
    </div>
    <div>
        <label for="prenom">Prénom :</label>
        <input type="text" id="prenom" name="prenom" required>
    </div>
    <div>
        <label for="email">Email :</label>
        <input type="email" id="email" name="email" required>
    </div>
    <div>
        <label for="samaccountname">Login (samaccountname) :</label>
        <input type="text" id="samaccountname" name="samaccountname" required>
    </div>
    <div>
        <label for="password">Mot de passe initial :</label>
        <input type="password" id="password" name="password" required>
    </div>
    <div>
        <label for="role_id">Rôle :</label>
        <select id="role_id" name="role_id" required>
            <option value="">-- Choisir un rôle --</option>
            <%-- Boucle sur la liste des rôles passée par le servlet (doGet) --%>
            <c:forEach var="role" items="${rolesList}">
                <%-- Hypothèse : votre entité Roles a getId() et getRoleName() --%>
                <option value="${role.id}">${role.name}</option>
            </c:forEach>
        </select>
    </div>

    <hr>
    <h3>Informations optionnelles</h3>

    <div>
        <label for="displayname">Nom d'affichage (displayname) :</label>
        <input type="text" id="displayname" name="displayname">
    </div>
    <div>
        <label for="department">Département :</label>
        <input type="text" id="department" name="department">
    </div>
    <div>
        <label for="service">Service :</label>
        <input type="text" id="service" name="service">
    </div>

    <button type="submit">Ajouter l'employé</button>
</form>

<%@ include file="Shared/footer.jsp"%>