// ============================================
// GESTION DES MODALES DE CONFIRMATION
// ============================================

function openModal(id) {
    const modal = document.getElementById('deleteModal' + id);
    if (modal) {
        modal.classList.add('active');
        document.body.style.overflow = 'hidden'; // Empêche le scroll
    }
}

function closeModal(id) {
    const modal = document.getElementById('deleteModal' + id);
    if (modal) {
        modal.classList.remove('active');
        document.body.style.overflow = 'auto'; // Réactive le scroll
    }
}

// Fermeture de la modale en cliquant à l'extérieur
window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.classList.remove('active');
        document.body.style.overflow = 'auto';
    }
}

// Fermeture avec la touche Échap
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        const activeModal = document.querySelector('.modal.active');
        if (activeModal) {
            activeModal.classList.remove('active');
            document.body.style.overflow = 'auto';
        }
    }
});

// ============================================
// TRI DE TABLE
// ============================================

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

// ============================================
// RÉINITIALISATION DES FILTRES
// ============================================

function resetFilters() {
    // Vider tous les champs du formulaire
    document.getElementById('search').value = '';
    document.getElementById('typeFilter').value = '';
    document.getElementById('sortBy').value = '';
    document.getElementById('direction').value = 'asc';

    // Rediriger vers la page sans paramètres
    window.location.href = '/produits';
}

// ============================================
// AFFICHAGE DYNAMIQUE DU NOMBRE DE RÉSULTATS
// ============================================

function updateResultsCount() {
    const tableRows = document.querySelectorAll('tbody tr');
    const countElement = document.querySelector('.results-count');

    if (countElement && tableRows.length > 0) {
        const count = tableRows.length;
        countElement.textContent = `${count} produit${count > 1 ? 's' : ''} trouvé${count > 1 ? 's' : ''}`;
        countElement.style.animation = 'fadeIn 0.5s';
    }
}

// ============================================
// INDICATEURS VISUELS DE TRI ACTIF
// ============================================

function updateSortIndicators() {
    const urlParams = new URLSearchParams(window.location.search);
    const activeSortField = urlParams.get('sortBy');
    const activeSortDirection = urlParams.get('direction') || 'asc';

    // Réinitialiser tous les indicateurs
    document.querySelectorAll('th.sortable').forEach(th => {
        th.classList.remove('sort-active', 'sort-asc', 'sort-desc');
        const icon = th.querySelector('.sort-icon');
        if (icon) icon.remove();
    });

    // Ajouter l'indicateur sur la colonne active
    if (activeSortField) {
        const sortableHeaders = document.querySelectorAll('th.sortable');
        sortableHeaders.forEach(th => {
            const onclick = th.getAttribute('onclick');
            if (onclick && onclick.includes(`'${activeSortField}'`)) {
                th.classList.add('sort-active', `sort-${activeSortDirection}`);

                // Ajouter l'icône de tri
                const icon = document.createElement('i');
                icon.className = `fas fa-sort-${activeSortDirection === 'asc' ? 'up' : 'down'} sort-icon`;
                th.appendChild(icon);
            }
        });
    }

    // Mettre à jour aussi les selects du formulaire
    const sortBySelect = document.getElementById('sortBy');
    const directionSelect = document.getElementById('direction');

    if (sortBySelect && activeSortField) {
        sortBySelect.value = activeSortField;
        sortBySelect.classList.add('active-filter');
    }

    if (directionSelect && activeSortDirection) {
        directionSelect.value = activeSortDirection;
        directionSelect.classList.add('active-filter');
    }
}

// ============================================
// MISE EN ÉVIDENCE DES FILTRES ACTIFS
// ============================================

function highlightActiveFilters() {
    const urlParams = new URLSearchParams(window.location.search);

    // Recherche
    const searchInput = document.getElementById('search');
    if (searchInput && searchInput.value) {
        searchInput.classList.add('active-filter');
    }

    // Type de thé
    const typeFilter = document.getElementById('typeFilter');
    if (typeFilter && typeFilter.value) {
        typeFilter.classList.add('active-filter');
    }

    // Afficher/masquer le bouton réinitialiser selon s'il y a des filtres
    const hasFilters = urlParams.toString().length > 0;
    const resetBtn = document.getElementById('resetBtn');
    if (resetBtn) {
        resetBtn.style.display = hasFilters ? 'inline-block' : 'none';
    }
}

// ============================================
// AUTO-FERMETURE DES ALERTES
// ============================================

function initAlertAutoClose() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.animation = 'slideOut 0.3s ease-out';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });
}

// ============================================
// ANIMATIONS D'ENTRÉE
// ============================================

function animateElements() {
    // Animation des lignes du tableau
    const tableRows = document.querySelectorAll('tbody tr');
    tableRows.forEach((row, index) => {
        row.style.animation = `fadeInUp 0.3s ease-out ${index * 0.05}s both`;
    });
}

// ============================================
// INITIALISATION AU CHARGEMENT DE LA PAGE
// ============================================

document.addEventListener('DOMContentLoaded', function() {
    // Initialiser toutes les fonctionnalités
    updateResultsCount();
    updateSortIndicators();
    highlightActiveFilters();
    initAlertAutoClose();
    animateElements();

    console.log('✅ Scripts UX chargés avec succès');
});

// ============================================
// ANIMATION SLIDE OUT POUR LES ALERTES
// ============================================

const style = document.createElement('style');
style.textContent = `
    @keyframes slideOut {
        from {
            transform: translateY(0);
            opacity: 1;
        }
        to {
            transform: translateY(-20px);
            opacity: 0;
        }
    }
    
    @keyframes fadeInUp {
        from {
            transform: translateY(20px);
            opacity: 0;
        }
        to {
            transform: translateY(0);
            opacity: 1;
        }
    }
`;
document.head.appendChild(style);