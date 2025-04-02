

$(document).ready(function () {
    $('#form1').submit(function (event) {
        event.preventDefault();

        // Obtener los valores de los campos
        var user = $('#user').val();
        var password = $('#password').val();

        // Validar si los campos están vacíos
        if (user === '' || password === '') {
            Swal.fire({
                title: 'Error!',
                text: 'Por favor, complete todos los campos.',
                icon: 'error',
                confirmButtonColor: "#3085d6",
                confirmButtonText: 'Aceptar'
            });
            return; // Si hay un campo vacío, detenemos la ejecución del código
        }

        // Si los campos están completos, procedemos con la solicitud AJAX
        var form = document.getElementById("form1");
        var action = form.action;
        var formData = $(this).serialize();

        $.ajax({
            type: 'POST',
            url: $(this).attr('action'),
            data: formData,
            contentType: 'application/x-www-form-urlencoded',
            success: function (response) {
                var mensaje = response;
                if (mensaje === "1") {
                    Swal.fire({
                        title: 'Credenciales Correctas!',
                        icon: "success",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    setInterval(() => {
                        window.location.href = "/menu";
                    }, 1600);
                } 
                else if (mensaje === '2') {
                    Swal.fire({
                        title: 'Credenciales Incorrectas!',
                        icon: "error",
                        showConfirmButton: false,
                        timer: 1500
                    });
                }
                else if (mensaje === '3'){
                    Swal.fire({
                        title: 'Usuario Inhabilitado!',
                        icon: "error",
                        showConfirmButton: false,
                        timer: 1500
                    });
                }
            },
            error: function (xhr, status, error) {
                toastr.error('Ha ocurrido un error. Por favor, intenta nuevamente.' + xhr, status, error);
                console.error(error);
                // Manejo de errores
            }
        });

    });
});
