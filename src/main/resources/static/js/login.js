document.addEventListener('DOMContentLoaded', function () {
    const emailInput = document.getElementById('email');
    const emailIcon = document.getElementById('email-icon');
    const form = document.getElementById('loginForm');
    const passwordInput = document.getElementById('password');

    // Validación de correo en tiempo real
    function validateEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    function updateValidation(input, icon, isValid) {
        if (isValid) {
            input.classList.add("valido");
            input.classList.remove("invalido");
            icon.textContent = "✔️";
        } else {
            input.classList.add("invalido");
            input.classList.remove("valido");
            icon.textContent = "❌";
        }
    }

    emailInput.addEventListener('input', () => {
        updateValidation(emailInput, emailIcon, validateEmail(emailInput.value));
    });

    form.addEventListener('submit', (e) => {
        const isEmailValid = validateEmail(emailInput.value);
        updateValidation(emailInput, emailIcon, isEmailValid);

        if (!isEmailValid) {
            e.preventDefault();
        }
    });

    // Ver/ocultar contraseña
    const togglePassword = document.getElementById('togglePassword');
    const toggleIcon = document.getElementById('toggleIcon');

    togglePassword.addEventListener('click', () => {
        const isPassword = passwordInput.type === 'password';
        passwordInput.type = isPassword ? 'text' : 'password';
        toggleIcon.classList.toggle('bi-eye');
        toggleIcon.classList.toggle('bi-eye-slash');
    });
});
