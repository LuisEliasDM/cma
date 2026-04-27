<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Capturar firma de responsable</title>
        <script>
            $(function(){
                $("select[name='idFirmaElectronica']").change(function() {
                    if ($(this).val() != "") {
                        if ($(".firmaElectronica_" + $(this).val() + "_key").val() == "true") {
                            $(".clavePrivada").hide();
                            $("input[name='clavePrivada']").prop("required", false);
                        } else {
                            $(".clavePrivada").fadeIn();
                            $("input[name='clavePrivada']").prop("required", true);
                        }
                    } else {
                        $(".clavePrivada").fadeIn();
                        $("input[name='clavePrivada']").prop("required", true);
                    }
                });
            });
        </script>
    </head>
    <body>
        <a href="#create-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="tituloElectronico" action="consultarTitulo" id="${tituloElectronicoInstance?.id}">Detalles del t&iacute;tulo</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_GESTOR">
                    <li><g:link class="list" action="index" id="${tituloElectronicoInstance?.id}">Listado de firmas de responsables</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                    <li><g:link class="create" controller="firmaElectronica" action="capturarFirmaElectronica">Capturar e.firma</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Firma de responsable de la institución para el título: </h1>                
            
                <h4 style="margin-left: 25px; margin-top: 20px; margin-bottom: 20px;">
                    <label style=" width: 140px;">Folio de control:</label> ${tituloElectronicoInstance?.folioControl}<br/>
                    <label style=" width: 140px;">Profesionista:</label> ${tituloElectronicoInstance?.profesionista?.getNombreCompleto()} - ${tituloElectronicoInstance?.profesionista?.curp}<br/>
                    <label style=" width: 140px;">Estatus:</label> #${tituloElectronicoInstance?.getEstatus()?.id} - ${tituloElectronicoInstance?.getEstatus()?.descripcion}
                </h4>
            
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${tituloElectronicoInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${tituloElectronicoInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </g:hasErrors>
            
            <g:if test="${estatusEFirma?.firmasCaducadas[0] > 0}">
                <div class="errors">
                    Se 
                    detect<g:if test="${estatusEFirma?.firmasCaducadas[0] > 1}">&aacute;ron</g:if><g:else>&oacute;</g:else>
                    ${estatusEFirma?.firmasCaducadas[0]}
                    e.firma<g:if test="${estatusEFirma?.firmasCaducadas[0] > 1}">s</g:if> 
                    que caduca<g:if test="${estatusEFirma?.firmasCaducadas[0] > 1}">n</g:if> en aproximadamente un mes.
                    Se requiere solicitar su renovaci&oacute;n ante el SAT.
                </div>
            </g:if>
            
            <g:if test="${estatusEFirma?.firmasProximasCaducar[0] > 0}">
                <div class="message">
                    Se 
                    detect<g:if test="${estatusEFirma?.firmasProximasCaducar[0] > 1}">&aacute;ron</g:if><g:else>&oacute;</g:else>
                    ${estatusEFirma?.firmasProximasCaducar[0]}
                    e.firma<g:if test="${estatusEFirma?.firmasProximasCaducar[0] > 1}">s</g:if> 
                    pr&oacute;xima<g:if test="${estatusEFirma?.firmasProximasCaducar[0] > 1}">s</g:if> 
                    a caducar.
                    Se recomienda solicitar su renovaci&oacute;n ante el SAT al menos un mes antes de su fecha de caducidad.
                </div>
            </g:if>
            
            <g:uploadForm controller="FirmaResponsable" action="guardarInformacion" id="${tituloElectronicoInstance?.id}">
                <fieldset class="form">
                    <label>A continuación, ingresa la información para la firma del título: </label><br/><br/>
                    <br/><h3>Informaci&oacute;n de la e.firma</h3>
                    <g:render template="datosCompletos/firmante"/>  
                    <br/><br/><br/><br/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Guardar datos de la firma" />
                </fieldset>
            </g:uploadForm>
            <g:each in="${firmasElectronicas}" var="fe">
                <input type="hidden" class="firmaElectronica_${fe?.id}_key" value="${fe.archivoKey? true: false}"/>
            </g:each>
        </div>
    </body>
</html>
