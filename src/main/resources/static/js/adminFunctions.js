$(document).ready(function () {
    $('#census').DataTable({
        order: [[0, 'desc']],
        language: {
            lengthMenu: 'Elementos por página: _MENU_ ',
            zeroRecords: 'No se encuentran resultados',
            info: 'Página _PAGE_ de _PAGES_',
            infoEmpty: 'No se encuentran resultados',
            infoFiltered: '',
            search: 'Buscar:',
            paginate: {
                next: ">",
                previous: "<"
            }
        },
    });
});

$('.selector > input').click(function() {
    if ($(this).is(':checked')) {
        $(this).siblings('fieldset').show();
    } else $(this).siblings('fieldset').hide();
});