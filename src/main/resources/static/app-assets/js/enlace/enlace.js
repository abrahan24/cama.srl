
$(document).ready(function() {
    cargarTabla();
    
});

function modalRol() {
    $.ajax({
        type: "POST",
        url: "/cargarFormRol",  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
        success: function (response) {
            $("#modalRol").html(response);
        },
        error: function () {
            console.log("Error al cargar el menú");
            Swal.fire({
                title: 'Su Session Expiro!',
                icon: "info",
                showConfirmButton: false,
                timer: 1500
            });
            setInterval(() => {
                window.location.href = "/login";
            }, 1600);
        }
    });
}

function modalEnlace() {
    $.ajax({
        type: "POST",
        url: "/cargarFormEnlace",  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
        success: function (response) {
            $("#modalEnlace").html(response);
        },
        error: function () {
            console.log("Error al cargar el menú");
            Swal.fire({
                title: 'Su Session Expiro!',
                icon: "info",
                showConfirmButton: false,
                timer: 1500
            });
            setInterval(() => {
                window.location.href = "/login";
            }, 1600);
        }
    });
}

function modalSubEnlace() {
    $.ajax({
        type: "POST",
        url: "/cargarFormSubEnlace",  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
        success: function (response) {
            $("#modalSubEnlace").html(response);
        },
        error: function () {
            console.log("Error al cargar el menú");
            Swal.fire({
                title: 'Su Session Expiro!',
                icon: "info",
                showConfirmButton: false,
                timer: 1500
            });
            setInterval(() => {
                window.location.href = "/login";
            }, 1600);
        }
    });
}

function cargarTabla(){
    $.ajax({
        type: "POST",
        url: "/cagarTabla",  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
        success: function (response) {
            $("#tablaEnlace").html(response);
            $('#tablaE').DataTable({
                dom: 'Bfrtip',
                buttons: ['copy', 'csv', 'excel', 'pdf', 'print'],
                language: {
                    "url": "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json"
                },
            });

        },
        error: function () {
            console.log("Error al cargar el menú");
            Swal.fire({
                title: 'Su Session Expiro!',
                icon: "info",
                showConfirmButton: false,
                timer: 1500
            });
            setInterval(() => {
                window.location.href = "/login";
            }, 1600);
        }
    });
}