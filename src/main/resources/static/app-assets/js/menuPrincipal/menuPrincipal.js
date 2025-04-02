$(document).ready(function () {
    menuPrincipal();
});

function menuPrincipal() {
    $.ajax({
        type: "POST",
        url: "/cargarMenuEnlaces",  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
        success: function (response) {
            $("#menuPrincipal").html(response);
        },
        error: function () {
            console.log("Error al cargar el menú");
        }
    });
}