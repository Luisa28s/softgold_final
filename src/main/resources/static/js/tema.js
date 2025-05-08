document.addEventListener('DOMContentLoaded', function () {
    const temaGuardado = localStorage.getItem('tema');
    if (temaGuardado) {
        document.body.classList.toggle('tema-oscuro', temaGuardado === 'oscuro');
    }

    const toggleBtn = document.getElementById('toggle-tema');
    if (toggleBtn) {
        toggleBtn.addEventListener('click', () => {
            const modoOscuro = document.body.classList.toggle('tema-oscuro');
            localStorage.setItem('tema', modoOscuro ? 'oscuro' : 'claro');
        });
    }
});
