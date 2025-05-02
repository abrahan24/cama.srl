$(document).ready(function () {
    // Obtener ID del enlace activo
    const activeLink = $('.nav-link.active');
    const fullId = activeLink.attr('id'); // Ej: account-pill-general-45
    const id_usuario = fullId.split('-').pop();
    cargarSeccionGeneral(id_usuario);

    // Clic en los tabs
    $(document).on('click', '.nav-link', function () {
        const fullId = $(this).attr('id');
        const beforeDash = fullId.replace(/-.*/, ''); // elimina el primer guion y todo lo que le sigue
        const id_usuario = fullId.split('-').pop();
        cargarSeccion(id_usuario,beforeDash);
    });

    
    // Clic en "Reenviar confirmación"
    $(document).on('click', '#reenviar-confirmacion', function (e) {
        e.preventDefault();
    
        $.ajax({
            type: "POST",
            url: "/reenviar-confirmacion",
            success: function (mensaje) {
                toastr.success(mensaje);
            },
            error: function (xhr) {
                let mensaje = xhr.responseText || "Error al reenviar el correo.";
                if (xhr.status === 401) {
                    toastr.warning(mensaje);
                    // Espera 2 segundos y luego redirige
                    setTimeout(function () {
                        window.location.href = "/login";
                    }, 2000);
                } else if (xhr.status === 400) {
                    toastr.warning(mensaje); // mensaje de negocio
                } else {
                    toastr.error("Error inesperado.");
                }
            }
        });
    });

    $('#form-cambiar-password').on('submit', function(e) {
        e.preventDefault();
    
        const formData = $(this).serialize(); // Toma los campos con 'name'
    
        $.ajax({
            type: "POST",
            url: "/cambiar_password",
            data: formData,
            success: function (mensaje) {
                toastr.success(mensaje);
                $('#form-cambiar-password')[0].reset();
            },
            error: function (xhr) {
                let msg = xhr.responseText || "Ocurrió un error inesperado.";
                if (xhr.status === 400) {
                    toastr.warning("⚠️ " + msg);
                } else if (xhr.status === 401) {
                    toastr.error("🔒 " + msg);
                    setTimeout(() => {
                        window.location.href = "/login";
                    }, 2000);
                } else {
                    toastr.error(msg);
                }
            }
        });
    });

});

function cargarSeccionGeneral(id_usuario){

    $.ajax({
        type: "POST",
        url: "/seccionGeneral/" + encodeURIComponent(id_usuario), // Codifica el parámetro
        success: function (response) {
            $("#general").html(response);
        },
        error: function (xhr, status, error) {
            console.error('Error en la petición AJAX:', error);
            if (xhr.status === 401) {
                Swal.fire({
                    title: 'Su sesión ha expirado!',
                    icon: "info",
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    window.location.href = "/login";
                });
            } 
        }
    });

}

function cargarSeccion(id_usuario,seccion){

    if(seccion === 'account_pill_general'){
        $.ajax({
            type: "POST",
            url: "/seccionGeneral/" + encodeURIComponent(id_usuario), // Codifica el parámetro
            success: function (response) {
                $("#general").html(response);
            },
            error: function (xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                if (xhr.status === 401) {
                    Swal.fire({
                        title: 'Su sesión ha expirado!',
                        icon: "info",
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => {
                        window.location.href = "/login";
                    });
                } 
            }
        });
    }else if(seccion === 'account_pill_info'){
        $.ajax({
            type: "POST",
            url: "/seccionInformacion/" + encodeURIComponent(id_usuario), // Codifica el parámetro
            success: function (response) {
                $("#informacion").html(response);
            },
            error: function (xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                if (xhr.status === 401) {
                    Swal.fire({
                        title: 'Su sesión ha expirado!',
                        icon: "info",
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => {
                        window.location.href = "/login";
                    });
                } 
            }
        });
    }

   

}