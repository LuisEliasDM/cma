<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'ActionLoggingEvent')}" />
        <title>Detalles de la bit&aacute;cora</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Detalles de la bit&aacute;cora</h1>                

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>

            <fieldset class="form">
                <div class="fieldcontain">
                    <label>Fecha:</label>
                    <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${actionLoggingEventInstance?.date}"/>
                </div>
                <div class="fieldcontain">
                    <label>Usuario:</label>
                    <g:if test="${actionLoggingEventInstance?.usuario}">
                        <g:link controller="usuario" action="show" id="${actionLoggingEventInstance?.usuario?.id}">${actionLoggingEventInstance?.usuario?.perfil?.nombreCompleto} (${actionLoggingEventInstance?.usuario?.username})</g:link>
                        <span class="property-value" aria-labelledby="password-label">
                            <ul>
                                <g:each in="${actionLoggingEventInstance?.usuario?.roles}" var="usuarioRol">
                                    <li>
                                        ${usuarioRol.rol}
                                    </li>
                                </g:each>
                            </ul>
                        </span>
                    </g:if>
                </div>
                <div class="fieldcontain">
                    <label>Tipo de acci&oacute;n:</label>
                    ${actionLoggingEventInstance?.actionType}
                </div>
                <div class="fieldcontain">
                    <label>Nombre de la acci&oacute;n:</label>
                    ${actionLoggingEventInstance?.customActionName}
                </div>
                <div class="fieldcontain">
                    <label>Estatus:</label>
                    ${actionLoggingEventInstance?.status}
                </div>
                <div class="fieldcontain">
                    <label>Controlador:</label>
                    ${actionLoggingEventInstance?.controllerName}
                </div>
                <div class="fieldcontain">
                    <label>Acci&oacute;n:</label>
                    ${actionLoggingEventInstance?.actionName}
                </div>
                <div class="fieldcontain">
                    <label>Tiempo de inicio:</label>
                    ${actionLoggingEventInstance?.startTime}
                </div>
                <div class="fieldcontain">
                    <label>Tiempo de finalizaci&oacute;n:</label>
                    ${actionLoggingEventInstance?.endTime}
                </div>
                <div class="fieldcontain">
                    <label>Tiempo total:</label>
                    ${actionLoggingEventInstance?.totalTime}
                </div>
                <div class="fieldcontain">
                    <label>URI proveniente:</label>
                    ${actionLoggingEventInstance?.forwardURI}
                </div>
                <div class="fieldcontain">
                    <label>Host remoto:</label>
                    ${actionLoggingEventInstance?.remoteHost}
                </div>
                <div class="fieldcontain">
                    <label>Us&oacute; ajax?:</label>
                    ${actionLoggingEventInstance?.ajax}
                </div>
                <div class="fieldcontain">
                    <label>Id de Usuario:</label>
                    ${actionLoggingEventInstance?.userId}
                </div>
                <div class="fieldcontain">
                    <label>Parametros:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 200px; color: #333333;" readonly="">${actionLoggingEventInstance?.params}</textarea>
                </div>
                <br/>
                <div class="fieldcontain">
                    <label>Descripci&oacute;n de la excepci&oacute;n:</label>
                    ${actionLoggingEventInstance?.exceptionMessage}
                </div>
                <div class="fieldcontain">
                    <label>Detalle de la excepci&oacute;n:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 200px; color: #333333;" readonly="">${actionLoggingEventInstance?.exceptionStackTrace}</textarea>
                </div>
                <br/>
                <div class="fieldcontain">
                    <label>Registro:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 500px; color: #333333;" readonly="">${actionLoggingEventInstance?.customLog}</textarea>
                </div>

            </fieldset>
            </div>
        </body>
    </html>
