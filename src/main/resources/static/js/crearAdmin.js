document.addEventListener('DOMContentLoaded', function () {
    const passwordInput = document.getElementById('passwordPlano');
    const togglePasswordBtn = document.getElementById('togglePassword');
    const toggleIcon = togglePasswordBtn?.querySelector('i');

    // Mostrar/ocultar contraseña
    if (togglePasswordBtn && passwordInput && toggleIcon) {
        togglePasswordBtn.addEventListener('click', () => {
            const isPassword = passwordInput.type === 'password';
            passwordInput.type = isPassword ? 'text' : 'password';
            toggleIcon.classList.toggle('bi-eye');
            toggleIcon.classList.toggle('bi-eye-slash');
        });
    }

});
