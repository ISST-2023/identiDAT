$(document).ready(function () {
    $('#Perfiles').DataTable({
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

$(document).ready(function () {
    $('#tokensTable').DataTable({
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

