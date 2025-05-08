document.addEventListener('keydown', function (event) {
    const isMac = navigator.platform.toUpperCase().indexOf('MAC') >= 0;
    const ctrlKey = isMac ? event.metaKey : event.ctrlKey;

    // Ctrl + B = Mostrar campo de búsqueda
    if (ctrlKey && event.key.toLowerCase() === 'b') {
        event.preventDefault();
        const buscador = document.getElementById('busquedaRapida');
        buscador.style.display = 'block';
        buscador.querySelector('input').focus();
    }

    // Ctrl + / = Mostrar modal de atajos
    if (ctrlKey && event.key === '/') {
        event.preventDefault();
        const modal = new bootstrap.Modal(document.getElementById('modalAtajos'));
        modal.show();
    }

    // Escape = Ocultar búsqueda rápida
    if (event.key === 'Escape') {
        const buscador = document.getElementById('busquedaRapida');
        buscador.style.display = 'none';
    }
});
