<%@ page import="java.net.InetAddress" %>
<%@ include file="../Shared/header.jsp" %>

<div class="container mt-4">
  <div class="card shadow">
    <div class="card-header bg-primary text-white">
      <h5 class="mb-0"><i class="bi bi-server me-2"></i>Server Information</h5>
    </div>
    <div class="card-body">
      <div class="row g-3">
        <div class="col-md-6">
          <div class="card h-100 border-0 bg-light">
            <div class="card-body">
              <h6 class="text-muted text-uppercase small">Hostname</h6>
              <p class="fs-5 mb-0 font-monospace">tatotalk-node-01</p>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="card h-100 border-0 bg-light">
            <div class="card-body">
              <h6 class="text-muted text-uppercase small">IP Address</h6>
              <p class="fs-5 mb-0 font-monospace">172.18.0.5</p>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="card h-100 border-0 bg-light">
            <div class="card-body">
              <h6 class="text-muted text-uppercase small"><i class="bi bi-pc-display me-1"></i>Operating System</h6>
              <p class="fs-5 mb-0">Linux 5.15.0-91-generic (amd64)</p>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="card h-100 border-0 bg-light">
            <div class="card-body">
              <h6 class="text-muted text-uppercase small"><i class="bi bi-cup-hot me-1"></i>Java Version</h6>
              <p class="fs-5 mb-0">21.0.2</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>


<%@ include file="../Shared/footer.jsp" %>