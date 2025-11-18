<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html>
<head>

    <title>TatoTalk</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/vendor/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link href="${pageContext.request.contextPath}/vendor/bootstrap-5.3.8-dist/bootstrap-icons-1.13.1/bootstrap-icons.min.css" rel="stylesheet" />
    <link href="${pageContext.request.contextPath}/vendor/sircl-2.6.7/sircl-bundled.min.css" rel="stylesheet" />

    <script src="${pageContext.request.contextPath}/vendor/jquery/jquery-3.7.1.min.js"></script>

    <script src="${pageContext.request.contextPath}/vendor/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>

    <script src="${pageContext.request.contextPath}/vendor/sircl-2.6.7/sircl-bundled.min.js"></script>
    <script src="${pageContext.request.contextPath}/vendor/sircl-2.6.7/sircl-bootstrap5.min.js"></script>
</head>

<style>
    navbar {
        margin: 0;
    }

    .sidebar {
        width: 240px;
        min-height: 100vh;
        background-color: #051c33;
    }

    .sidebar-brand {
        font-size: 1.4rem;
        font-weight: 700;
        color: #ffffff;
    }

    .sidebar .nav-link {
        color: #cfd8e3;
        font-weight: 500;
        padding-left: 0;
    }

    .sidebar .nav-link.active,
    .sidebar .nav-link:hover {
        color: #ffffff;
        background-color: rgba(255, 255, 255, 0.06);
    }
</style>

<body class="p-0 m-0" style="background-color: #f8f9f8">

<div class="d-flex m-0 p-0" style="height: 100vh;">
    <div class="navbar d-flex m-0 p-0">
        <nav class="sidebar d-flex flex-column p-4">
            <div class="sidebar-brand mt-4 mb-5">
                <img src="${pageContext.request.contextPath}/Shared/images/logo.png" alt="Yato-Talk Logo" style="width: 50px; height: auto;">
                TatoTalk
            </div>

            <ul class="nav nav-pills flex-column gap-2">
                <li class="nav-item">
                    <a class="nav-link" href="#"><i class="fs-5 bi-chat-fill me-2" style="color: #53758d"></i>Messages</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#"><i class="fs-5 bi-people-fill me-2" style="color: #53758d"></i>Groupes</a>
                </li>
                <div class="dropup pb-4" style="margin-top: 680px">
                    <a class="text-decoration-none" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fs-2 bi-gear-wide-connected" style="color: #53758d; margin-left: 70px"></i>
                    </a>

                    <ul class="dropdown-menu mb-2" style="margin-left: 9px">
                        <li style="margin-left: 7px">
                            <a class="dropdown-item d-flex align-items-center gap-2" href="admin/addUser">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" class="bi bi-person-plus-fill" viewBox="0 0 16 16">
                                    <path fill="#0b3d62" d="M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/>
                                    <path fill="#d63939" fill-rule="evenodd" d="M13.5 5a.5.5 0 0 1 .5.5V7h1.5a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0V8h-1.5a.5.5 0 0 1 0-1H13V5.5a.5.5 0 0 1 .5-.5"/>
                                </svg>
                                <span>New User</span>
                            </a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item d-flex align-items-center gap-2" href="#"><i class="fs-4 bi-box-arrow-left"></i><span style="margin-left: 5px">Logout</span></a>
                        </li>
                    </ul>
                </div>
            </ul>
        </nav>
    </div>

