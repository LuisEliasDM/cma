<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Verificaci&oacute;n de t&iacute;tulo electr&oacute;nico</title>
    </head>
    <body>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Verificaci&oacute;n de t&iacute;tulo electr&oacute;nico</h1>    
            
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:if test="${flash.error}">
                    <div class="errors" role="status">${flash.error}</div>
                </g:if>
            
                <g:if test="${tituloElectronicoInstance}">
                    <fieldset class="form">

                        <g:render template="datosCompletos/datosTitulo" model="[verificacion: true]"/>  

                    </fieldset>
                </g:if>
            </div>
    </body>
</html>
