<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Detalles de autenticación de autoridad competente</title>
    </head>
    <body>
        <a href="#create-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="tituloElectronico" action="consultarTitulo" id="${tituloElectronicoInstance?.id}">Detalles del t&iacute;tulo</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR">
                    <li><g:link class="list" action="index" id="${tituloElectronicoInstance?.id}">Autenticación de autoridad</g:link></li>
                </sec:ifAnyGranted>
            </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Autenticación de autoridad para el título: </h1>                
            
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
            
            <fieldset class="form">
                <h3>Información de la autenticación</h3>
                <div class="fieldcontain">
                    <label>Versión:</label>
                    ${autenticacionInstance?.version}
                </div>
                <div class="fieldcontain">
                    <label>Folio digital único (UUID):</label>
                    ${autenticacionInstance?.folioDigital}
                </div>
                <div class="fieldcontain">
                    <label>Fecha de autenticación:</label>
                    <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${autenticacionInstance?.fechaAutenticacion}"/>
                </div>
                <br/>
                
                <h3>Firma electrónica</h3>
                <div class="fieldcontain">
                    <label>Sellos digitales de los firmantes:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 100px; color: #333333;" readonly="">${autenticacionInstance?.selloTitulo}</textarea>
                </div>
                <div class="fieldcontain">
                    <label>Número de serie del certificado:</label>
                    ${autenticacionInstance?.noCertificadoAutoridad}
                </div>
                <div class="fieldcontain">
                    <label>Cadena original de la autenticación:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 100px; color: #333333;" readonly="">${autenticacionInstance?.cadenaOriginalSelloAutenticacion}</textarea>
                </div>
                <div class="fieldcontain">
                    <label>Sello digital de la autenticación:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 100px; color: #333333;" readonly="">${autenticacionInstance?.selloAutenticacion}</textarea>
                </div>
                
            </fieldset>
            <fieldset class="buttons">
                <sec:ifAnyGranted roles="ROLE_AUTENTICADOR">
                    <g:link class="save confirm" controller="Autenticacion" action="eliminarAutenticacion" id="${autenticacionInstance?.id}">Eliminar</g:link>
                </sec:ifAnyGranted>
            </fieldset>
        </div>
    </body>
</html>
