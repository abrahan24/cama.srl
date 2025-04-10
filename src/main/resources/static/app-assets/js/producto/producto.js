$(document).ready(function () {

    cargarTablaProductosPorNombreProducto();
    cargarTablaProductosPorCodigoProducto();


    $('#file').on('change', function () {
        const file = this.files[0];
        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();

            reader.onload = function (e) {
                $('#preview-image').attr('src', e.target.result);
                $('#preview-container').show();
            };

            reader.readAsDataURL(file);
        } else {
            $('#preview-container').hide();
            $('#preview-image').attr('src', '#');
        }
    });

    $('#remove-image').on('click', function () {
        $('#file').val(''); // Limpiar input
        $('#preview-container').hide();
        $('#preview-image').attr('src', '#');
    });

    $('#nom_pro, #cod_pro').on('input', function () {
        this.value = this.value.toUpperCase();
    });
    
});

$('#form1').submit(function (event) {
    event.preventDefault();

    var form = document.getElementById("form1");
    var formData = new FormData(form); // ✅ Esto incluye el archivo

    $.ajax({
        type: 'POST',
        url: $(this).attr('action'),
        data: formData,
        processData: false,  // 🔥 ¡Importante! Evita que jQuery procese los datos
        contentType: false,   // 🔥 ¡Importante! Deja que el navegador establezca el Content-Type
        success: function (response) {
            var mensaje = response;
            if (mensaje === "1") {
                Swal.fire({
                    title: 'Registro Exitoso!',
                    icon: "success",
                    showConfirmButton: false,
                    timer: 1500
                });
                // Opcional: Resetear el formulario después del éxito
                form.reset();
                $('#id_tipo_producto').val(null).trigger('change');
                // Ocultar vista previa de la imagen (si aplica)
                $('#preview-container').hide();
            }
        },
        error: function (xhr, status, error) {
            toastr.error('Ha ocurrido un error. Por favor, intenta nuevamente.');
            console.error("Error en AJAX:", error);
        }
    });
});

function cargarTablaProductosPorNombreProducto() {
    $('#nom_pro').on('keyup', function () {
        // Obtiene el valor del input actual (usando $(this).val() es más directo)
        var nom_producto = $(this).val();

        // Validación básica - no hacer la petición si está vacío
        if (!nom_producto || nom_producto.trim() === '') {
            $('#tbodyP').empty();
            return;
        }

        $.ajax({
            type: "POST",
            url: "/cagarTablaProductosPorNombreProducto/" + encodeURIComponent(nom_producto), // Codifica el parámetro
            success: function (response) {
                // console.log('Respuesta recibida:', response);
                // Aquí puedes procesar la respuesta (response) que viene de tu backend
                // Por ejemplo, llenar una tabla o mostrar los resultados

                // Ejemplo de cómo podrías mostrar los resultados:
                if (response && response.length > 0) {
                    $("#tableP").html(response);
                    $("#cod_pro").val('');
                
                    // Destruir DataTable si ya está inicializado
                    if ($.fn.DataTable.isDataTable('#tablePP')) {
                        $('#tablePP').DataTable().destroy();
                    }
                
                    // Inicializar nuevamente
                    $('#tablePP').DataTable({
                        dom: 'Bfrtip',
                        buttons: ['copy', 'csv', 'excel', 'pdf', 'print'],
                        language: {
                            "url": "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json"
                        },
                    });
                } else {
                    console.log('No se encontraron resultados');
                    $('#tableP tbody').empty(); // Limpiar tabla si no hay resultados
                    // Opcional: mostrar mensaje al usuario
                    Swal.fire({
                        title: 'No se encontraron coincidencias',
                        icon: 'info',
                        timer: 1500
                    });
                }
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
                } else {
                    Swal.fire({
                        title: 'Error en la búsqueda',
                        text: 'Ocurrió un error al realizar la búsqueda',
                        icon: 'error'
                    });
                }
            }
        });
    });
}

function cargarTablaProductosPorCodigoProducto() {
    $("#cod_pro").on("keyup", function () {
        // Obtiene el valor del input actual (usando $(this).val() es más directo)
        var cod_producto = $(this).val();

        // Validación básica - no hacer la petición si está vacío
        if (!cod_producto || cod_producto.trim() === "") {
            $("#tbodyP").empty();
            return;
        }

        $.ajax({
            type: "POST",
            url:
                "/cagarTablaProductosPorCodigoProducto/" +
                encodeURIComponent(cod_producto), // Codifica el parámetro
            success: function (response) {
                // console.log('Respuesta recibida:', response);
                // Aquí puedes procesar la respuesta (response) que viene de tu backend
                // Por ejemplo, llenar una tabla o mostrar los resultados

                // Ejemplo de cómo podrías mostrar los resultados:
                if (response && response.length > 0) {
                    $("#tableP").html(response);
                    $("#nom_pro").val("");

                    // Destruir DataTable si ya existe
                    if ($.fn.DataTable.isDataTable('#tablePP')) {
                        $('#tablePP').DataTable().destroy();
                    }

                    // Volver a crear DataTable
                    $('#tablePP').DataTable({
                        dom: "Bfrtip",
                        buttons: ["copy", "csv", "excel", "pdf", "print"],
                        language: {
                            url: "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json",
                        },
                    });
                } else {
                    console.log("No se encontraron resultados");
                    $("#tableP tbody").empty(); // Limpiar tabla si no hay resultados
                    // Opcional: mostrar mensaje al usuario
                    Swal.fire({
                        title: "No se encontraron coincidencias",
                        icon: "info",
                        timer: 1500,
                    });
                }
            },
            error: function (xhr, status, error) {
                console.error("Error en la petición AJAX:", error);
                if (xhr.status === 401) {
                    Swal.fire({
                        title: "Su sesión ha expirado!",
                        icon: "info",
                        showConfirmButton: false,
                        timer: 1500,
                    }).then(() => {
                        window.location.href = "/login";
                    });
                } else {
                    Swal.fire({
                        title: "Error en la búsqueda",
                        text: "Ocurrió un error al realizar la búsqueda",
                        icon: "error",
                    });
                }
            },
        });
    });
}