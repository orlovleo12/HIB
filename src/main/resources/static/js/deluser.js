function deluser(id) {

    var user = JSON.stringify({
        id: id

    });

    $.ajax({

        url: '/admin/del',
        datatype: 'json',
        type: 'post',
        contentType: "application/json; charset=utf-8",
        data: user,
        success: function (data) {}


    });



    var elem=document.getElementById(id);
    elem.parentNode.removeChild(elem);


// удалить строку из html
    // при update изменять строку
}