document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form"); // Cualquier formulario
    const email = document.querySelector("input[type='email']");
    const password = document.querySelector("input[type='password']");
    const alerta = document.getElementById("alerta");
    const confirmar = document.getElementById("confirmar"); 

    if (!form || !email || !password) return;

    form.addEventListener("submit", (e) => {
        if (alerta) {
            alerta.classList.add("d-none");
            alerta.innerText = "";
        }

        const emailValido = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value);
        const passwordValida = /^(?=.*[A-Z])(?=.*\d).{8,}$/.test(password.value);

        if (!emailValido) {
            e.preventDefault();
            mostrarError("Ingresa un correo electrónico válido.");
        } else if (!passwordValida) {
            e.preventDefault();
            mostrarError("La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.");
        } else if (confirmar && password.value !== confirmar.value) {
            e.preventDefault();
            mostrarError("Las contraseñas no coinciden.");
        }
    });

    function mostrarError(mensaje) {
        if (!alerta) return;
        alerta.innerText = mensaje;
        alerta.classList.remove("d-none");
        alerta.classList.add("alert", "alert-danger");
    }
});
