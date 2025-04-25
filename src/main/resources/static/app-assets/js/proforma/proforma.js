
$(document).ready(function() {
    
    buscarClientesPorNombres();
    buscarClientesPorCodigoProducto();
    
    $("#tipo_proforma").select2({
      placeholder: "Seleccione...",
    });

    $('#nom_prod, #cod_prod').on('input', function () {
        this.value = this.value.toUpperCase();
    });

    if ( $.fn.DataTable.isDataTable('#tablaPS') ) {
        $('#tablaPS').DataTable().clear().destroy();
    }
    $('#btnGenerarProforma').on('click', generarProforma);

    // Aquí actualizas el tbody o reconstruyes la tabla
    
    // Luego vuelves a inicializar si es necesario
    $('#tablaPS').DataTable({
        language: {
            "url": "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json"
        },
        scrollY: '300px',
        scrollCollapse: true,
        paging: false,
        responsive: true,
    });

    $('#tipo_proforma').change(function() {
        var valor = $(this).val();
        const form = $('#form1');
        // Ejemplo de lógica condicional
        if(valor === 'CMO') {  //CMO Con Mano De Obra
            // Ejemplo: mostrar campos adicionales
            $.ajax({
                type: "POST",
                url: "/manejarTipoProforma/"+valor,  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
                success: function (response) {
                    $("#form_proforma").html(response);
                    $('#nom_cliente , #nota').on('input', function () {
                        this.value = this.value.toUpperCase();
                    });
                
                    // Destruir instancias previas si las hay, y volver a inicializar
                    $("#form_proforma .select2").each(function () {
                        if ($.fn.select2 && $(this).hasClass("select2-hidden-accessible")) {
                            $(this).select2('destroy');
                        }
                        $(this).select2({
                            placeholder: "Seleccione...",
                            width: '100%'
                        });
                    });
                },
                error: function () {
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
        } else if(valor === 'SMO') {  //SMO Sin Mano De Obra
            // Ejemplo: hacer una consulta AJAX para datos del cliente
            $.ajax({
                type: "POST",
                url: "/manejarTipoProforma/"+valor,  // Asegúrate de que esta URL apunte a tu controlador en Spring Boot
                success: function (response) {
                    $("#form_proforma").html(response);
                    $('#nom_cliente , #nota').on('input', function () {
                        this.value = this.value.toUpperCase();
                    });
                     // Destruir instancias previas si las hay, y volver a inicializar
                     $("#form_proforma .select2").each(function () {
                        if ($.fn.select2 && $(this).hasClass("select2-hidden-accessible")) {
                            $(this).select2('destroy');
                        }
                        $(this).select2({
                            placeholder: "Seleccione...",
                            width: '100%'
                        });
                    });
                },
                error: function () {
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
    });

      // Vector global para guardar los productos seleccionados
      let productosSeleccionados = [];

      // Evento al hacer clic en "Seleccionar"
      $(document).on('click', '.seleccionar-producto', function () {
          const fila = $(this).closest('tr'); // la fila actual
  
          // Obtener los valores de la fila
          const producto = {
              nombre: fila.find('td:eq(1)').text().trim(),
              codigo: fila.find('td:eq(2)').text().trim(),
              descripcion: fila.find('td:eq(3)').text().trim(),
              stock: parseInt(fila.find('td:eq(4)').text().trim(), 10),
              precio: parseFloat(fila.find('td:eq(5)').text().trim())
          };
  
          // SweetAlert para ingresar cantidad
          Swal.fire({
              title: 'Ingrese la cantidad',
              input: 'number',
              inputAttributes: {
                  min: 1,
                  max: producto.stock,
                  step: 1
              },
              inputValidator: (value) => {
                  if (!value || value <= 0) {
                      return 'Debe ingresar una cantidad válida';
                  }
                  if (value > producto.stock) {
                      return `No hay suficiente stock. Disponible: ${producto.stock}`;
                  }
                  return null;
              },
              showCancelButton: true,
              confirmButtonText: 'Agregar',
          }).then((result) => {
              if (result.isConfirmed) {
                  producto.cantidad = parseInt(result.value, 10);
                  productosSeleccionados.push(producto);
                  toastr.success("Producto " + producto.nombre + " Seleccionado Correctamente!",
                      {
                          timeOut: 2000,
                          hideMethod: "slideUp", 
                          positionClass: 'toast-bottom-right',
                          closeButton: false,
                          progressBar: true 
                      });
                  actualizarTablaSeleccionados();
              }
          });
      });
  
 // Mostrar productos seleccionados en otra tabla
 function actualizarTablaSeleccionados() {
    
     let tabla = $("#tablaPS").DataTable({
       createdRow: function (row, data, dataIndex) {
         $(row).css("font-size", "small");
       },
       autoWidth: false,
       destroy: true, // si necesitas reinicializar
       language: {
         url: "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json",
       },
       scrollY: "300px",
       scrollCollapse: true,
       paging: false,
       responsive: true,
     });
     tabla.clear().draw();
     productosSeleccionados.forEach((p, index) => {
         tabla.row
             .add([
                 index + 1,
                 p.nombre,
                 p.codigo,
                 p.descripcion,
                 p.precio,
                 p.cantidad,
                 `
         <div class="btn-group mr-1 mb-1">
            <button type="button" class="btn btn-icon btn-secondary dropdown-toggle"
                data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="la la-gear"></i>
            </button>

            <div class="dropdown-menu">
                <a class="dropdown-item btn-editar-cantidad" href="#" data-index="${index}">Editar Cantidad</a>
                <a class="dropdown-item btn-eliminar-producto" href="#" data-index="${index}">Eliminar</a>
            </div>
        </div>
        `,
             ])
             .draw(false);
     });
 }

        // Eliminar producto del vector y actualizar tabla
    $(document).on('click', '.btn-eliminar-producto', function (e) {
        e.preventDefault();
        const index = $(this).data('index');
        productosSeleccionados.splice(index, 1);
        actualizarTablaSeleccionados();
    });

    // Editar cantidad
    $(document).on('click', '.btn-editar-cantidad', function (e) {
        e.preventDefault();
        const index = $(this).data('index');
        const producto = productosSeleccionados[index];

        Swal.fire({
            title: `Editar cantidad para ${producto.nombre}`,
            input: 'number',
            inputLabel: 'Cantidad',
            inputValue: producto.cantidad,
            inputAttributes: {
                min: 1
            },
            showCancelButton: true,
            confirmButtonText: 'Actualizar',
            cancelButtonText: 'Cancelar',
            inputValidator: (value) => {
                if (!value || parseInt(value) < 1) {
                    return 'La cantidad debe ser mayor a 0';
                }
            }
        }).then((result) => {
            if (result.isConfirmed) {
                productosSeleccionados[index].cantidad = parseInt(result.value);
                actualizarTablaSeleccionados();
            }
        });
    });

    function generarProforma() {
        const tipoProforma = $('#tipo_proforma').val();

        if (!tipoProforma) {
            Swal.fire({
                title: '¡Campo requerido!',
                text: 'Por favor, seleccione un tipo de proforma antes de continuar.',
                icon: 'warning',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#3085d6',
            }).then(() => {
                $('#tipo_proforma').focus(); // Enfocar el select
            });
            return; // Detener la ejecución si no está seleccionado
        }
        // Obtener datos del cliente
        const nomCliente = $('input[name="nom_cliente"]').val();
        const descuento = $('input[name="descuento"]').val();
        const nota = $('input[name="nota"]').val();
        const tipo_predio = $('select[name="tipo_predio"]').val();
        const manoObraData = [];
        let manoObraValida = true;

        // Solo validar mano de obra si es tipo CMO
        if (tipoProforma === 'CMO') {
            $('.mano-obra-group').each(function (index) {
                const detalleInput = $(this).find('input[name^="mano_obra["][name$="[detalle]"]');
                const precioInput = $(this).find('input[name^="mano_obra["][name$="[precio]"]');
                const unidadSelect = $(this).find('select[name^="mano_obra["][name$="[unidad_medida]"]');

                const detalle = detalleInput.val().trim();
                const precio = precioInput.val().trim();
                const unidadMedida = unidadSelect.val();

                if (!detalle || !precio || !unidadMedida) {
                    manoObraValida = false;

                    Swal.fire({
                        title: '¡Campo requerido!',
                        text: 'Todos los campos de mano de obra son obligatorios.',
                        icon: 'warning',
                        confirmButtonText: 'Entendido',
                        confirmButtonColor: '#3085d6',
                    });

                    return false; // Rompe el bucle each
                }

                manoObraData.push({
                    detalle: detalle.toUpperCase(),
                    precio: precio,
                    unidad_medida: unidadMedida
                });
            });

            if (!manoObraValida) return;
        }
        
        // Recoger datos de productos de la tabla
        const productosData = [];
        $('#tablaPS tbody tr').each(function() {
            const cells = $(this).find('td');
            if (cells.length >= 6) { // Asegurarse que es una fila con datos
                const nombre = cells.eq(1).text() || cells.eq(1).find('input').val();
                const codigo = cells.eq(2).text() || cells.eq(2).find('input').val();
                const descripcion = cells.eq(3).text() || cells.eq(3).find('input').val();
                const precio = cells.eq(4).text() || cells.eq(4).find('input').val();
                const cantidad = cells.eq(5).text() || cells.eq(5).find('input').val();
                
                if (nombre && cantidad && precio) {
                    productosData.push({
                        nombre: nombre,
                        codigo: codigo || '',
                        descripcion: descripcion || '',
                        precio: precio,
                        cantidad: cantidad
                    });
                }
            }
        });
        
        // Validar datos mínimos
        if (!nomCliente) {
            Swal.fire({
                title: '¡Nombre requerido!',
                text: 'El nombre del cliente es obligatorio.',
                icon: 'error',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#3085d6',
            }).then(() => $('input[name="nom_cliente"]').focus());
            return;
        }

        if (!descuento) {
            Swal.fire({
                title: '¡Descuento Requerido!',
                text: 'El Campo Descuento es obligatorio.',
                icon: 'error',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#3085d6',
            }).then(() => $('input[name="descuento"]').focus());
            return;
        }
        if (!nota) {
            Swal.fire({
                title: '¡Nota Requerido!',
                text: 'El Campo Nota es obligatorio.',
                icon: 'error',
                confirmButtonText: 'Entendido',
                confirmButtonColor: '#3085d6',
            }).then(() => $('input[name="nota"]').focus());
            return;
        }
        
        if (tipoProforma === 'CMO') {
            if (manoObraData.length === 0 && productosData.length === 0) {
                Swal.fire({
                    title: '¡Datos incompletos!',
                    text: 'Debe agregar al menos un ítem (mano de obra o productos).',
                    icon: 'warning',
                    confirmButtonText: 'Entendido',
                    confirmButtonColor: '#3085d6',
                });
                return;
            }
        } else {
            if (productosData.length === 0) {
                Swal.fire({
                    title: '¡Datos incompletos!',
                    text: 'Debe agregar al menos un producto.',
                    icon: 'warning',
                    confirmButtonText: 'Entendido',
                    confirmButtonColor: '#3085d6',
                });
                return;
            }
        }
        
        // Enviar datos al servidor
        const formData = new FormData();
        formData.append('nom_cliente', nomCliente);
        formData.append('descuento', descuento);
        formData.append('nota', nota);
        formData.append('tipo_predio', tipo_predio);

        // Solo agregar mano de obra si es tipo CMO
        if (tipoProforma === 'CMO') {
            formData.append('mano_obra_json', JSON.stringify(manoObraData));
        }

        formData.append('productos_json', JSON.stringify(productosData));
        
        // Mostrar carga
        const $btnGenerar = $('#btnGenerarProforma');
        $btnGenerar.prop('disabled', true);
        $btnGenerar.html('<i class="fa fa-spinner fa-spin"></i> Generando...');
        
        toastr.success(
            "Generando Proforma...",
            { 
                showMethod: "slideDown", 
                hideMethod: "slideUp", 
                timeOut: 0, // Desactiva el cierre automático
                closeButton: false,
                progressBar: true 
            }
        );
       
        // Definir la URL dependiendo del tipo de proforma
        const urlAction = tipoProforma === 'CMO'
        ? '/generarProformaConManoDeObra'
        : '/generarProformaSinManoDeObra';

        // Enviar mediante AJAX con jQuery
        $.ajax({
            url: urlAction,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            xhrFields: {
                responseType: 'blob'
            },
            success: function(data) {
                const blobUrl = URL.createObjectURL(new Blob([data], { type: 'application/pdf' }));
                
                // Intentar abrir en nueva pestaña
                const pdfWindow = window.open(blobUrl, '_blank');
                
                // Fallback si bloquean popups
                if (!pdfWindow) {
                    const link = $('<a>')
                        .attr('href', blobUrl)
                        .attr('target', '_blank')
                        .text('Abrir Proforma PDF')
                        .css({
                            display: 'block',
                            margin: '10px',
                            padding: '10px',
                            background: '#007bff',
                            color: 'white',
                            textAlign: 'center',
                            borderRadius: '5px'
                        });
                    
                    $('body').append(link);
                    alert('Por favor haga clic en el enlace para ver la proforma');
                }
                // Cerrar toast y mostrar confirmación
                toastr.clear();
                toastr.success("Proforma generada correctamente", { timeOut: 2000 });
            },
            error: function(xhr, status, error) {
                console.error('Error:', error);
                toastr.clear();
                toastr.error('Error al generar la proforma: ' + error, { timeOut: 3000 });
            },
            complete: function() {
                $btnGenerar.prop('disabled', false);
                $btnGenerar.html('<i class="fa fa-file-pdf-o"></i> Generar Proforma');
            }
        });
    }
  
    $(document).on('input', 'input[name^="mano_obra["][name$="[detalle]"]', function () {
        this.value = this.value.toUpperCase();
    });
});

  

function buscarClientesPorNombres() {
    // Selecciona el input por su ID (#nom_prod) y asigna el evento change
    $('#nom_prod').on('keyup', function() {
        // Obtiene el valor del input actual (usando $(this).val() es más directo)
        var nombres = $(this).val();
        
        // Validación básica - no hacer la petición si está vacío
        if(!nombres || nombres.trim() === '') {
            console.log('El campo nombres está vacío');
            $('#tbodyPE').empty(); 
            return;
        }

        $.ajax({
            type: "POST",
            url: "/cagarTablaProformaPorNombreProducto/" + encodeURIComponent(nombres), // Codifica el parámetro
            success: function(response) {
                // Por ejemplo, llenar una tabla o mostrar los resultados
                // Ejemplo de cómo podrías mostrar los resultados:
                if(response && response.length > 0) {
                    $("#tablaPE").html(response);
                    $('#tablePEE').DataTable({
                        language: {
                            "url": "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json"
                        },
                        scrollY: '300px',
                        scrollCollapse: true,
                        paging: false,
                        responsive: true,
                    });
                    $("#cod_prod").val('');
                } else {
                    console.log('No se encontraron resultados');
                    $('#tablePEE tbodyPE').empty(); // Limpiar tabla si no hay resultados
                    // Opcional: mostrar mensaje al usuario
                    Swal.fire({
                        title: 'No se encontraron coincidencias',
                        icon: 'info',
                        timer: 1500
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                if(xhr.status === 401) {
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

function buscarClientesPorCodigoProducto() {
    // Selecciona el input por su ID (#nom_prod) y asigna el evento change
    $('#cod_prod').on('keyup', function() {
        // Obtiene el valor del input actual (usando $(this).val() es más directo)
        var cod_producto = $(this).val();
        
        // Validación básica - no hacer la petición si está vacío
        if(!cod_producto || cod_producto.trim() === '') {
            console.log('El campo nombres está vacío');
            $('#tbodyPE').empty(); 
            return;
        }

        $.ajax({
            type: "POST",
            url: "/cagarTablaProformaPorCodigoProducto/" + encodeURIComponent(cod_producto), // Codifica el parámetro
            success: function(response) {
                // Por ejemplo, llenar una tabla o mostrar los resultados
                // Ejemplo de cómo podrías mostrar los resultados:
                if(response && response.length > 0) {
                    $("#tablaPE").html(response);
                    $('#tablePEE').DataTable({
                        language: {
                            "url": "https://cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json"
                        },
                        scrollY: '300px',
                        scrollCollapse: true,
                        paging: false,
                        responsive: true,
                    });
                    $("#nom_prod").val('');
                } else {
                    console.log('No se encontraron resultados');
                    $('#tablePEE tbodyPE').empty(); // Limpiar tabla si no hay resultados
                    // Opcional: mostrar mensaje al usuario
                    Swal.fire({
                        title: 'No se encontraron coincidencias',
                        icon: 'info',
                        timer: 1500
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error('Error en la petición AJAX:', error);
                if(xhr.status === 401) {
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

