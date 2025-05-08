document.addEventListener('DOMContentLoaded', function () {
    const inputBusqueda = document.querySelector('#busquedaRapida input');
    const resultados = document.getElementById('resultadosBusqueda');

    // Si el campo de búsqueda o los resultados no existen salimos del script
    if (!inputBusqueda || !resultados) return;

    inputBusqueda.addEventListener('input', async () => {
        const query = inputBusqueda.value.trim();

        if (query.length < 2) {
            resultados.style.display = 'none';
            resultados.innerHTML = '';
            return;
        }

        try {
            const response = await fetch(`/api/busqueda?q=${encodeURIComponent(query)}`);
            const datos = await response.json();

            resultados.innerHTML = '';

            if (datos.length === 0) {
                resultados.innerHTML = '<div class="list-group-item disabled">Sin resultados</div>';
            } else {
                datos.forEach(item => {
                    const div = document.createElement('div');
                    div.classList.add('list-group-item', 'list-group-item-action');
                    div.textContent = item;
                    resultados.appendChild(div);
                });
            }

            resultados.style.display = 'block';
        } catch (error) {
            console.error('Error al buscar:', error);
        }
    });

    // Cierra resultados si se hace clic fuera
    document.addEventListener('click', (e) => {
        if (!inputBusqueda.contains(e.target) && !resultados.contains(e.target)) {
            resultados.style.display = 'none';
        }
    });
});
