<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Detalles de titulo electrónico</title>
    </head>
    <body>
        <a href="#create-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_GESTOR">
                    <li><g:link class="list" controller="firmaResponsable" action="index" id="${tituloElectronicoInstance?.id}">Firmas de responsables</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_FIRMANTE">
                    <li><g:link class="list" controller="registro" action="index" id="${tituloElectronicoInstance?.id}">Registro DGP</g:link></li>
                </sec:ifAnyGranted>
            </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Detalles del titulo electrónico</h1>    
            
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:if test="${flash.error}">
                    <div class="errors" role="status">${flash.error}</div>
                </g:if>
                
                <g:if test="${tituloElectronicoInstance?.getEstatus()?.inconcistencia}">
                    <div class="errors">${tituloElectronicoInstance?.getEstatus()?.inconcistencia}</div>
                </g:if>
            
                <fieldset class="form">                    
                    <g:render template="datosCompletos/datosTitulo"/>  
                </fieldset>
                <fieldset class="buttons">
                    <sec:ifAnyGranted roles="ROLE_GESTOR">
                        <g:link class="edit" controller="TituloElectronico" action="modificarTitulo" id="${tituloElectronicoInstance?.id}">Modificar</g:link>
                    </sec:ifAnyGranted>
                    <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                        <g:link class="save" controller="FirmaResponsable" action="capturarFirma" id="${tituloElectronicoInstance?.id}">Capturar firma</g:link>
                    </sec:ifAnyGranted>
                </fieldset>
    </body>
</html>
