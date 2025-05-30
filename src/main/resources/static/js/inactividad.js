let timeout;

function resetTimer() {
    clearTimeout(timeout);
    startInactivityTimer();
}

function startInactivityTimer() {
    timeout = setTimeout(() => {
        window.location.href = "/login"; 
    }, 15 * 60 * 1000);
    //5000   15 * 60 * 1000
}

window.addEventListener("DOMContentLoaded", () => {
    ["mousemove", "keydown", "scroll", "click"].forEach(event => {
        document.addEventListener(event, resetTimer);
    });

    startInactivityTimer();
});
