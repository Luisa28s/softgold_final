document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const email = document.getElementById("email");
    const password = document.getElementById("passwordPlano");
    const confirmar = document.getElementById("confirmar");
    const errorEmail = document.getElementById("errorEmail");
    const errorPassword = document.getElementById("errorPassword");
    const errorConfirmar = document.getElementById("errorConfirmar");

    if (!form || !email || !password) return;

    // --- Validación en tiempo real para email ---
    email.addEventListener("input", () => {
        if (!email.value) {
            errorEmail.textContent = "Debes completar el correo electrónico.";
        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            errorEmail.textContent = "Ingresa un correo electrónico válido.";
        } else {
            errorEmail.textContent = "";
        }
    });
    email.addEventListener("blur", () => {
        if (!email.value) {
            errorEmail.textContent = "Debes completar el correo electrónico.";
        }
    });

    // --- Validación en tiempo real para contraseña ---
    password.addEventListener("input", () => {
        if (!password.value) {
            errorPassword.textContent = "Debes completar la contraseña.";
        } else if (!/^(?=.*[A-Z])(?=.*\d).{8,}$/.test(password.value)) {
            errorPassword.textContent = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.";
        } else if (confirmar && confirmar.value && password.value !== confirmar.value) {
            errorConfirmar.textContent = "Las contraseñas no coinciden.";
            errorPassword.textContent = "";
        } else {
            errorPassword.textContent = "";
            if (errorConfirmar) errorConfirmar.textContent = "";
        }
    });
    password.addEventListener("blur", () => {
        if (!password.value) {
            errorPassword.textContent = "Debes completar la contraseña.";
        }
    });

    // --- Validación en tiempo real para confirmar contraseña ---
    if (confirmar && errorConfirmar) {
        confirmar.addEventListener("input", () => {
            if (!confirmar.value) {
                errorConfirmar.textContent = "Debes confirmar la contraseña.";
            } else if (password.value !== confirmar.value) {
                errorConfirmar.textContent = "Las contraseñas no coinciden.";
            } else {
                errorConfirmar.textContent = "";
            }
        });
        confirmar.addEventListener("blur", () => {
            if (!confirmar.value) {
                errorConfirmar.textContent = "Debes confirmar la contraseña.";
            }
        });
    }

    // --- Validación al enviar el formulario ---
    form.addEventListener("submit", (e) => {
        let hayError = false;
        if (!email.value) {
            errorEmail.textContent = "Debes completar el correo electrónico.";
            hayError = true;
        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            errorEmail.textContent = "Ingresa un correo electrónico válido.";
            hayError = true;
        }
        if (!password.value) {
            errorPassword.textContent = "Debes completar la contraseña.";
            hayError = true;
        } else if (!/^(?=.*[A-Z])(?=.*\d).{8,}$/.test(password.value)) {
            errorPassword.textContent = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.";
            hayError = true;
        }
        if (confirmar && (!confirmar.value || password.value !== confirmar.value)) {
            errorConfirmar.textContent = "Las contraseñas no coinciden.";
            hayError = true;
        }
        if (hayError) e.preventDefault();
    });
});