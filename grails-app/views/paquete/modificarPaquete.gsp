<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'paquete.label', default: 'Paquete')}" />
        <title>Modificación de paquete</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" action="consultarPaquete" id="${paqueteInstance?.id}">Detalles del paquete</g:link></li>
            </ul>
        </div>
        <div id="create-tituloElectronico" class="content scaffold-create" role="main">
            <h1>Modificaci&oacute;n de paquete</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <g:form controller="paquete" action="actualizarRegistro" id="${paqueteInstance?.id}">
                <fieldset class="form">
                    <g:render template="form"/>     
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Guardar cambios" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
