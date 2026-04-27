
<%@ page import="estructuraXml.TituloElectronico" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Firmas de responsables</title>
    </head>
    <body>
        <a href="#list-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="tituloElectronico" action="consultarTitulo" id="${tituloElectronicoInstance?.id}">Detalles del t&iacute;tulo</g:link></li>
                <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                    <li><g:link class="create" action="capturarFirma" id="${tituloElectronicoInstance?.id}">Capturar firma</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                    <g:if test="${tituloElectronicoInstance?.firmaResponsables?.firmaResponsable}">
                        <li><g:link class="list" controller="registro" action="index" id="${tituloElectronicoInstance?.id}">Registro DGP</g:link></li>
                    </g:if>
                </sec:ifAnyGranted>
            </ul>
            </div>
            <div id="list-tituloElectronico" class="content scaffold-list" role="main">
                <h1>Listado de firmas de responsables del titulo:</h1>
            
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
                        <th>Nombre</th>
                        <th>CURP</th>
                        <th>Cargo</th>
                        <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                            <th></th>
                        </sec:ifAnyGranted>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${tituloElectronicoInstance?.firmaResponsables?.firmaResponsable}">
                        <g:each in="${tituloElectronicoInstance?.firmaResponsables?.firmaResponsable}" status="i" var="firmaResponsableInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <td>
                                    <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_GESTOR">
                                        <g:link controller="FirmaResponsable" action="consultarFirma" id="${firmaResponsableInstance?.id}">${firmaResponsableInstance?.getNombreCompleto()}</g:link>
                                    </sec:ifAnyGranted>
                                    <sec:ifNotGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_GESTOR">
                                        ${firmaResponsableInstance?.getNombreCompleto()}
                                    </sec:ifNotGranted>
                                </td>
                                <td>${firmaResponsableInstance?.curp}</td>
                                <td>${firmaResponsableInstance?.cargo}</td>
                                <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                                    <td><g:link controller="FirmaResponsable" action="eliminarFirma" id="${firmaResponsableInstance?.id}" class="confirm">Eliminar</g:link></td>
                                </sec:ifAnyGranted>
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                        <td colspan="5"><center>No hay firmas registradas</center></td>
                    </g:else>
                </tbody>
            </table>
        </div>
    </body>
</html>
