$(document).ready(function () {

    cargarTablaProductosPorNombreProducto();
    cargarTablaProductosPorCodigoProducto();
    $('#id_tipo_producto').select2({
        placeholder : "Seleccione..."
    })

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
            // alert(mensaje);
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
                        buttons: [
                            'copy', 'csv', 'excel',
                            {
                                text: 'PDF',
                                action: function () {
                                    exportPDFWithImages();
                                }
                            },
                        ],
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
                        dom: 'Bfrtip',
                        buttons: [
                            'copy', 'csv', 'excel',
                            {
                                text: 'PDF',
                                action: function () {
                                    exportPDFWithImages();
                                }
                            },
                        ],
                        language: {
                            "url": "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json"
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

function cargarModalProducto(id_producto){
    $.ajax({
        type: "POST",
        url: "/cargarModalProducto/" +  encodeURIComponent(id_producto), // Codifica el parámetro
        success: function (response) {
            $("#modal_content").html(response);
           
                $(".select2").select2({
                    placeholder: "Seleccione...",
                    width: '100%'
                });
          
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
}


function convertImgToBase64(imgElement) {
    return new Promise((resolve, reject) => {
        const img = new Image();
        img.crossOrigin = "Anonymous";

        img.onload = function () {
            try {
                const canvas = document.createElement("canvas");
                canvas.width = this.naturalWidth;
                canvas.height = this.naturalHeight;

                const ctx = canvas.getContext("2d");
                ctx.drawImage(this, 0, 0);
                resolve(canvas.toDataURL("image/png"));
            } catch (err) {
                reject(err);
            }
        };

        img.onerror = reject;
        img.src = imgElement.src;
    });
}

function getBase64ImageFromURL(url) {
    return new Promise((resolve, reject) => {
        const img = new Image();
        img.crossOrigin = "Anonymous";

        img.onload = function () {
            try {
                const canvas = document.createElement("canvas");
                canvas.width = img.width;
                canvas.height = img.height;
                const ctx = canvas.getContext("2d");
                ctx.drawImage(img, 0, 0);
                resolve(canvas.toDataURL("image/png"));
            } catch (err) {
                reject(err);
            }
        };

        img.onerror = function (e) {
            reject(new Error(`No se pudo cargar la imagen desde ${url}`));
        };

        img.src = url;
    });
}

async function exportPDFWithImages() {
    const rows = [];
    const headers = ['#', 'Nombre Producto', 'Código', 'Descripción', 'Marca', 'Stock', 'Precio', 'Imagen'];

    // Encabezados
    rows.push(headers.map(h => ({ text: h, bold: true })));

    // Obtener la instancia de la tabla DataTable
    const table = $('#tablePP').DataTable();

    // Obtener todos los nodos de filas visibles en todas las páginas
    const allRows = table.rows({ page: 'all' }).nodes();

    // Procesar cada fila y convertir imagen (si existe)
    const promises = Array.from(allRows).map(async (row) => {
        const cols = $(row).find('td');
        const imgElement = $(cols[7]).find('img')[0];
        let imgData = "Sin imagen";

        if (imgElement) {
            try {
                imgData = await convertImgToBase64(imgElement);
            } catch (e) {
                console.error("Error al convertir imagen:", e);
            }
        }

        return [
            $(cols[0]).text().trim(),
            $(cols[1]).text().trim(),
            $(cols[2]).text().trim(),
            $(cols[3]).text().trim(),
            $(cols[4]).text().trim(),
            $(cols[5]).text().trim(),
            $(cols[6]).text().trim(),
            imgElement ? { image: imgData, width: 50 } : "Sin imagen"
        ];
    });

    const resolvedRows = await Promise.all(promises);
    rows.push(...resolvedRows);

    const base64Logo = await getBase64ImageFromURL('/app-assets/images/logo/LOGO_BINS_R4.png');

    const docDefinition = {
        pageSize: 'LETTER',
        pageOrientation: 'landscape',
        pageMargins: [20, 20, 20, 20],
        content: [
            {
                columns: [
                    {
                        image: base64Logo,
                        width: 50,
                        margin: [0, 0, 10, 5]
                    },
                    {
                        text: 'Lista de Productos',
                        style: 'header',
                        alignment: 'center',
                        margin: [0, 15, 0, 0]
                    }
                ]
            },
            {
                table: {
                    headerRows: 1,
                    widths: [20, 70, 60, '*', 60, 40, 50, 60],
                    body: rows
                },
                layout: {
                    fillColor: function (rowIndex) {
                        return (rowIndex === 0) ? '#eeeeee' : null;
                    }
                },
                dontBreakRows: true
            }
        ],
        styles: {
            header: {
                fontSize: 16,
                bold: true,
                margin: [0, 0, 0, 10]
            }
        },
        defaultStyle: {
            fontSize: 8
        },
        pageBreakBefore: function(currentNode, followingNodesOnPage, nodesOnNextPage, previousNodesOnPage) {
            return false; // <- No forzar saltos raros (mejor dejar control natural)
        }
    };

    pdfMake.createPdf(docDefinition).open();
}

