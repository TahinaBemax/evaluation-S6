// Fonction pour mettre à jour la barre de progression
function updateProgressBar(budgetInitial, montantConsomme, reste) {
    // Calcul du pourcentage
    const percentageConsumed = (montantConsomme / budgetInitial) * 100;
    const percentageRemaining = (reste / budgetInitial) * 100;

    // Mise à jour de la barre de progression
    const progressBar = document.getElementById('health-progress-bar');
    progressBar.style.width = `${percentageConsumed}%`;

    // Calcul de la couleur de la barre de progression
    let color = getColorBasedOnRemaining(percentageRemaining);

    // Application de la couleur dynamique et de la transparence
    progressBar.style.backgroundColor = color;
}

// Fonction pour déterminer la couleur en fonction du reste
function getColorBasedOnRemaining(percentageRemaining) {
    // Couleur de base pour le vert (rester complet)
    let green = 0;
    let red = 255;
    let transparency = 0.8; // Transparence de base

    // Si le reste est supérieur à 50 %, la couleur est plus verte
    if (percentageRemaining > 50) {
        green = 255;
        red = Math.floor(255 * (1 - (percentageRemaining - 50) / 50)); // Dégradé vers le rouge
        transparency = 0.6 + (percentageRemaining / 100) * 0.4; // Plus transparent quand plus de budget restant
    }
    // Si le reste est entre 20 et 50 %, on commence à devenir plus orange
    else if (percentageRemaining > 20) {
        green = Math.floor(255 * (percentageRemaining / 50)); // Dégradé de vert vers l'orange
        red = 255;
        transparency = 0.6 + (percentageRemaining / 100) * 0.4;
    }
    // Si le reste est inférieur à 20 %, on devient rouge
    else {
        green = Math.floor(255 * (percentageRemaining / 20)); // Dégradé de rouge vers plus de rouge
        red = 255;
        transparency = 0.4 + (percentageRemaining / 100) * 0.6;
    }

    // Renvoie la couleur au format RGBA avec transparence
    return `rgba(${red}, ${green}, 0, ${transparency})`;
}

// Exécution du script dès que le DOM est prêt
document.addEventListener('DOMContentLoaded', function() {

});
