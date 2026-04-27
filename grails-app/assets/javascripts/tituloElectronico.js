$(function () {
    var callingCarreras = 0;
    $("select[name='cveInstitucion']").change(function () {

        if (callingCarreras === 0) {
            callingCarreras = 1;

            $("select[name='cveCarrera']").html("<option value=''>Cargando carreras...</option>");

            $.ajax({
                url: "institucionEducativa/carreras",
                data: {
                    "id": $(this).val()
                },
                type: "POST",
                timeout: 20000
            }).done(function (result) {
                if (result.length > 0) {
                    $("select[name='cveCarrera']").html("<option value=''>Selecciona...</option>");
                    $(result).each(function (index, element) {
                        var option = $("<option value='" + element.id + "'>" + element.clave + " - " +element.descripcion + " - RVOE: " + element.rvoe + "</option>");
                        $("select[name='cveCarrera']").append(option);
                    });
                } else {
                    $("select[name='cveCarrera']").html("<option value=''>No se encontraron carreras</option>");
                }
                
            }).error(function (data, textStatus, jqXHR) {
                console.log("Ocurrió un error: " + textStatus);
            }).always(function () {
                callingCarreras = 0;
            });
        } else {
            return false;
        }
    });
    
    $("select[name='idModalidadTitulacion']").change(function () {
        if ($(this).val() == 1) {
            $(".feep").hide();
            $("select[name='fechaExencionExamenProfesionalDia']").removeAttr("required");
            $("select[name='fechaExencionExamenProfesionalMes']").removeAttr("required");
            $("select[name='fechaExencionExamenProfesionalAnio']").removeAttr("required");
            
            $(".fep").fadeIn();
            $("select[name='fechaExamenProfesionalDia']").attr("required", "");
            $("select[name='fechaExamenProfesionalMes']").attr("required", "");
            $("select[name='fechaExamenProfesionalAnio']").attr("required", "");
            
        } else {
            $(".fep").hide();
            $("select[name='fechaExamenProfesionalDia']").removeAttr("required");
            $("select[name='fechaExamenProfesionalMes']").removeAttr("required");
            $("select[name='fechaExamenProfesionalAnio']").removeAttr("required");
            
            $(".feep").fadeIn();
            $("select[name='fechaExencionExamenProfesionalDia']").attr("required", "");
            $("select[name='fechaExencionExamenProfesionalMes']").attr("required", "");
            $("select[name='fechaExencionExamenProfesionalAnio']").attr("required", "");
        }
    });
});