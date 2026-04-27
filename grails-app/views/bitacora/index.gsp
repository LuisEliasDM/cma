
<%@ page import="org.mirzsoft.grails.actionlogging.ActionLoggingEvent" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'actionLoggingEvent.label', default: 'ActionLoggingEvent')}" />
        <title>Bit&aacute;cora de acciones de usuario</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                </ul>
            </div>
            <div id="list-institucionEducativa" class="content scaffold-list" role="main">
                <h1>Bit&aacute;cora de acciones de usuario</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <div style=" text-align: right; margin-right: 20px; margin-bottom: 20px;">
                <g:form controller="bitacora" action="index" style="display: inline-block;">
                    <input type="text" name="referencia" required="" placeholder="Buscar..." value="${params.referencia}" style=" width: 380px;"/>
                    <g:submitButton name="buscar" class="list" value="Buscar" />
                </g:form>
                    
                <g:form controller="bitacora" action="index" style="display: inline-block;">
                    <g:submitButton name="xfiltros" class="list" value="Quitar filtros" />
                </g:form>
                
            </div>
            
            <div class="pagination">
                <span class="step">
                    <a href="${createLink(action: "index", params: [referencia: referencia])}">&Uacute;ltimos registros</a>
                </span>
            </div>
            
            
            
            <table>
                <thead>
                    <tr>
                        <th>Fecha</th>
                        <th>Usuario</th>
                        <th>Acci&oacute;n</th>
                        <th>Estatus</th>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${actionLoggingEventInstanceList}">
                        <g:each in="${actionLoggingEventInstanceList}" status="i" var="actionLoggingEventInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <td><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${actionLoggingEventInstance?.date}"/></td>
                                <td>
                                    <g:if test="${actionLoggingEventInstance?.usuario}">
                                        <g:link controller="usuario" action="show" id="${actionLoggingEventInstance?.usuario?.id}">${actionLoggingEventInstance?.usuario?.perfil?.nombreCompleto} (${actionLoggingEventInstance?.usuario?.username})</g:link>
                                        <br/>
                                        <g:each in="${actionLoggingEventInstance?.usuario?.roles}" var="usuarioRol">
                                            - ${usuarioRol.rol}<br/>
                                        </g:each>
                                    </g:if>
                                </td>
                                <td><g:link action="detalle" id="${actionLoggingEventInstance?.id}">${actionLoggingEventInstance.customActionName}</g:link></td>
                                <td>${actionLoggingEventInstance.status}</td>
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                        <tr>
                            <td colspan="5"><center>No se encontraron registros</center></td>
                        </tr>
                    </g:else>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${actionLoggingEventInstanceCount ?: 0}" params="${[referencia: params.referencia]}"/>
            </div>
        </div>
    </body>
</html>
