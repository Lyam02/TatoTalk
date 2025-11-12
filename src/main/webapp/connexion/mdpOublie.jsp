<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Mot de passe oublié - TatoTalk</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/bootstrap-5.3.8-dist/css/bootstrap.min.css">
  <script src="${pageContext.request.contextPath}/vendor/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>

  <style>
    /* 1. Couleur de fond personnalisée pour le panneau de gauche */
    .fond-bleu-logo {
      background: #083352;
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

            <h2 class="h3 fw-bold mb-3">Mot de passe oublié ?</h2>

            <p class="text-muted mb-4">
              Entrez votre e-mail pour réinitialiser votre mot de passe.
            </p>
            <form action="votre-url-reset-mdp" method="post">

              <div class="mb-3">
                <input type="email" class="form-control form-control-lg bg-light border-0" id="email" name="email" placeholder="E-mail" required>
              </div>

              <div class="d-grid mt-4">
                <button type="submit" class="btn btn-custom btn-lg fw-bold text-uppercase">
                  Réinitialiser mon mot de passe
                </button>
              </div>

            </form>

            <div class="text-center mt-4">
              <a href="index.jsp?unePage=1" class="text-decoration-none text-muted">Retour à la connexion</a>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</main>

</body>
</html>