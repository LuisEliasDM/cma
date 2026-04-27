<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Rechazo de paquete de t&iacute;tulos electr&oacute;nicos</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="paquete" action="consultarPaquete" id="${paqueteInstance?.id}">Detalles del paquete</g:link></li>
            </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Anulaci&oacute;n de rechazo del paquete: </h1>                
            
                <h4 style="margin-left: 25px; margin-top: 20px; margin-bottom: 20px;">
                    <label style=" width: 140px;">Folio:</label> ${paqueteInstance?.folio}<br/>
                    <label style=" width: 140px;">Instituci&oacute;n:</label> ${paqueteInstance?.institucionEducativa}<br/>
                    <label style=" width: 140px;">Estatus:</label> ${paqueteInstance?.estatus}<br/>
                </h4>
            
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${paqueteInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${tituloElectronicoInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </g:hasErrors>
            
            <g:form controller="paquete" action="anularRechazoCotejo" id="${paqueteInstance?.id}">
                <fieldset class="form">
                    <g:render template="datosCompletos/anulacionRechazoCotejo"/>     
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Anular rechazo" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
