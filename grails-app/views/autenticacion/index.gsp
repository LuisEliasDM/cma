
<%@ page import="estructuraXml.TituloElectronico" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Autenticación</title>
    </head>
    <body>
        <a href="#list-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="tituloElectronico" action="consultarTitulo" id="${tituloElectronicoInstance?.id}">Detalles del t&iacute;tulo</g:link></li>
                <sec:ifAnyGranted roles="ROLE_AUTENTICADOR">
                    <li><g:link class="create" action="capturarAutenticacion" id="${tituloElectronicoInstance?.id}">Capturar autenticación</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR">
                    <g:if test="${tituloElectronicoInstance?.autenticacion}">
                        <li><g:link class="list" controller="registro" action="index" id="${tituloElectronicoInstance?.id}">Registro DGP</g:link></li>
                    </g:if>
                </sec:ifAnyGranted>
            </ul>
            </div>
            <div id="list-tituloElectronico" class="content scaffold-list" role="main">
                <h1>Autenticación del título:</h1>
            
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
            
            <table>
                <thead>
                    <tr>
                        <th>Folio digital único (UUID)</th>
                        <th>Fecha de autenticación</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${tituloElectronicoInstance?.autenticacion}">
                        <tr class="even">
                            <td><g:link controller="Autenticacion" action="consultarAutenticacion" id="${tituloElectronicoInstance?.autenticacion?.id}">${tituloElectronicoInstance?.autenticacion?.folioDigital}</g:link></td>
                            <td><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${tituloElectronicoInstance?.autenticacion?.fechaAutenticacion}"/></td>
                            <td><g:link controller="Autenticacion" action="eliminarAutenticacion" id="${tituloElectronicoInstance?.autenticacion?.id}" class="confirm">Eliminar</g:link></td>
                        </tr>
                    </g:if>
                    <g:else>
                        <td colspan="5"><center>No se ha realizado la autenticación</center></td>
                    </g:else>
                </tbody>
            </table>
        </div>
    </body>
</html>
