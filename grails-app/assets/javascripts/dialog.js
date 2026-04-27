$(document).ready(function(){
    $(document).keydown(function(e) {
        if (e.keyCode == 27) return false;
    });
        
    $("a").click(function(e) {
        if ($(e.target).hasClass("confirm")) {
            confirmDialog(e);
        } else {
            loadingDialog(e);
        }
    });
    
    $("form").submit(function(e) {
        if ($(e.target).hasClass("confirm")) {
            confirmDialog(e);
        } else {
            loadingDialog(e);
        }
    });
    
    function loadingDialog(e) {
        var target = $(e.target).attr("target");
        if (typeof target !== typeof undefined && target !== false) {
        } else {
            var dialog = $("<div title='Procesando...' style='text-align: center;'><br/>Por favor, espere...</div>");
            $(dialog).dialog({
                modal: true,
                width: 350,
                height: 120,
                closeOnEscape: false,
                open: function(event, ui) {
                    $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
                }
            });
        }
    }
    
    function confirmDialog(e) {
        e.preventDefault();
        var dialog = $("<div title='Confirmacion...' style='text-align: center;'><br/>¿Esta seguro de realizar esta accion?</div>");
        $(dialog).dialog({
            modal: true,
            width: 350,
            height: 180,
            buttons: {
                Cancelar: function() {
                    $(this).dialog("close");
                },
                Continuar: function() {
                    $(e.target).removeClass("confirm");
                    $(this).dialog("close");
                    
                    if ($(e.target).is("a")) {
                        window.location = $(e.target).attr("href");
                    } else if ($(e.target).is("form")) {
                        $(e.target).submit();
                    }
                    
                    loadingDialog(e);
                    
                }
            } 
        });
        
        $(dialog).bind("dialogclose", function(event, ui) {
            $(".ui-dialog-content").dialog("close");
        });
    }
    
    
});