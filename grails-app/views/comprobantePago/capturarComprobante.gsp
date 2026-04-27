<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'paquete.label', default: 'Paquete')}" />
        <title>Captura de comprobante de pago</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
            </ul>
        </div>
        <div id="create-paquete" class="content scaffold-create" role="main">
            <h1>Captura de comprobante de pago</h1>                
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:form controller="comprobantePago" action="guardarInformacion">
                <fieldset class="form">
                    <g:render template="form"/>  
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Guardar datos del comprobante" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
