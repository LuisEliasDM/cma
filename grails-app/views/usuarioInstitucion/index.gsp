
<%@ page import="seguridad.UsuarioInstitucion" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'usuarioInstitucion.label', default: 'UsuarioInstitucion')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-usuarioInstitucion" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                </ul>
            </div>
            <div id="list-usuarioInstitucion" class="content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <table>
                <thead>
                    <tr>

                        <th><g:message code="usuarioInstitucion.usuario.label" default="Usuario" /></th>

                        <th><g:message code="usuarioInstitucion.institucionEducativa.label" default="Institucion Educativa" /></th>

                    </tr>
                </thead>
                <tbody>
                    <g:each in="${usuarioInstitucionInstanceList}" status="i" var="usuarioInstitucionInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                            <td><g:link action="show" id="${usuarioInstitucionInstance.id}">${fieldValue(bean: usuarioInstitucionInstance, field: "usuario")}</g:link></td>

                            <td>${fieldValue(bean: usuarioInstitucionInstance, field: "institucionEducativa")}</td>

                        </tr>
                    </g:each>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${usuarioInstitucionInstanceCount ?: 0}" />
            </div>
        </div>
    </body>
</html>
