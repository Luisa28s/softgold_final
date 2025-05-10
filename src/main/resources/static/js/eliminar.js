  function abrirModalEliminar(boton) {
    const url = boton.getAttribute("data-urlEliminar");
    const formEliminar = document.getElementById('formEliminar');
    formEliminar.action = url;
    const modal = new bootstrap.Modal(document.getElementById('confirmarEliminacionModal'));
    modal.show();
  }