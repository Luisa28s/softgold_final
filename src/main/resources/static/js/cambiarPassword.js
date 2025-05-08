document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-cambio");
    const nueva = document.getElementById("nueva");
    const confirmar = document.getElementById("confirmar");
    const alerta = document.getElementById("alerta");
    const toggleNueva = document.getElementById("toggle-nueva");
    const toggleConfirmar = document.getElementById("toggle-confirmar");

    // Validación del formulario
    form.addEventListener("submit", (e) => {
        alerta.innerText = "";
        alerta.classList.add("d-none");

        if (nueva.value.length < 8) {
            e.preventDefault();
            mostrarError("La contraseña debe tener al menos 8 caracteres.");
        } else if (nueva.value !== confirmar.value) {
            e.preventDefault();
            mostrarError("Las contraseñas no coinciden.");
        }
    });

    // Función para mostrar errores
    function mostrarError(mensaje) {
        alerta.innerText = mensaje;
        alerta.classList.remove("d-none");
        alerta.classList.add("alert", "alert-danger");
    }

    // Función para alternar visibilidad de la contraseña
    function togglePassword(input, toggleButton) {
        const icon = toggleButton.querySelector("i");
        if (input.type === "password") {
            input.type = "text";
            icon.classList.remove("bi-eye");
            icon.classList.add("bi-eye-slash");
        } else {
            input.type = "password";
            icon.classList.remove("bi-eye-slash");
            icon.classList.add("bi-eye");
        }
    }

    // Eventos para alternar visibilidad de las contraseñas
    toggleNueva.addEventListener("click", () => togglePassword(nueva, toggleNueva));
    toggleConfirmar.addEventListener("click", () => togglePassword(confirmar, toggleConfirmar));
});
