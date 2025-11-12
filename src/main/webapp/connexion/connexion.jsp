<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Connexion - TatoTalk</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/bootstrap-5.3.8-dist/css/bootstrap.min.css">
  <script src="${pageContext.request.contextPath}/vendor/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
  <style>
    /* 1. Couleur de fond personnalisée pour le panneau de gauche */
    .bg-custom-dark-blue {
      background-color: #2c3e50;
    }

    /* 2. Styles du logo (dimensions et icône) */
    .logo-circle {
      width: 120px;
      height: 120px;
    }
    .logo-circle img {
      width: 80%;
      height: 80%;
      object-fit: scale-down;
      margin-left: 4px;
    }
    .logo-circle i {
      font-size: 60px;
      color: #2c3e50; /* L'icône reprend la couleur du panneau */
    }

    /* 3. Espacement des lettres pour le titre */
    .app-title {
      letter-spacing: 2px;
    }

    /* 4. Votre bouton personnalisé (couleurs de marque) */
    .btn-custom {
      background-color: #5AA1C4;
      border-color: #8E9BAF;
      color: #FFFFFF;
    }
    .btn-custom:hover {
      background-color: #2F6F92;
      border-color: #7A8A9E;
      color: #FFFFFF;
    }
    .fond-bleu-logo{
      background: #083352;
    }
  </style>
</head>

<body class="d-flex align-items-center justify-content-center bg-white min-vh-100">

<main class="container">
  <div class="row justify-content-center">
    <div class="col-lg-7 col-xl-7">
      <div class="card shadow-lg border-0" style="border-radius: 10px; overflow: hidden;">
        <div class="row g-0">

          <div class="col-md-4 d-flex flex-column justify-content-center align-items-center text-white p-5 fond-bleu-logo">

            <div class="logo-circle d-flex align-items-center justify-content-center bg-white rounded-circle mb-3">
              <img src="${pageContext.request.contextPath}/Shared/images/logo.png" alt="Logo-TatoTalk">
            </div>

            <h1 class="h2 text-uppercase fw-bold app-title">TatoTalk</h1>

          </div>

          <div class="col-md-8 p-5 bg-white position-relative">

            <h2 class="h3 fw-bold mb-4">Connectez-vous</h2>

            <form action="votre-url-de-login" method="post">

              <div class="mb-3">
                <input type="email" class="form-control form-control-lg bg-light border-0" id="email" name="email" placeholder="E-mail" required>
              </div>

              <div class="mb-3">
                <input type="password" class="form-control form-control-lg bg-light border-0" id="password" name="password" placeholder="Mot de passe" required>
              </div>

              <div class="d-grid mt-4">
                <button type="submit" class="btn btn-custom btn-lg fw-bold text-uppercase">
                  Connexion
                </button>
              </div>

            </form>

            <div class="text-center mt-4">
              <a href="${pageContext.request.contextPath}/mdp-oublie" class="text-decoration-none text-muted">Vous avez oublié votre mot de passe ?</a>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</main>

</body>
</html>