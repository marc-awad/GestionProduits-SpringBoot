// Tri de table
function sortTable(field) {
    const urlParams = new URLSearchParams(window.location.search);
    const currentSort = urlParams.get('sortBy');
    const currentDirection = urlParams.get('direction') || 'asc';

    let newDirection = 'asc';
    if (currentSort === field && currentDirection === 'asc') {
        newDirection = 'desc';
    }

    urlParams.set('sortBy', field);
    urlParams.set('direction', newDirection);
    window.location.search = urlParams.toString();
}

// Gestion des modales
function openModal(id) {
    document.getElementById('deleteModal' + id).classList.add('active');
}

function closeModal(id) {
    document.getElementById('deleteModal' + id).classList.remove('active');
}

// Fermeture de la modale en cliquant à l'extérieur
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.classList.remove('active');
    }
}

// Auto-fermeture des alertes
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.animation = 'slideOut 0.3s ease-out';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });
});