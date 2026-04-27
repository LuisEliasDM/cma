
<%@ page import="seguridad.UsuarioInstitucion" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'usuarioInstitucion.label', default: 'UsuarioInstitucion')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-usuarioInstitucion" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                </ul>
            </div>
            <div id="show-usuarioInstitucion" class="content scaffold-show" role="main">
                <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <ol class="property-list usuarioInstitucion">

                <g:if test="${usuarioInstitucionInstance?.usuario}">
                    <li class="fieldcontain">
                        <span id="usuario-label" class="property-label"><g:message code="usuarioInstitucion.usuario.label" default="Usuario" /></span>

                        <span class="property-value" aria-labelledby="usuario-label"><g:link controller="usuario" action="show" id="${usuarioInstitucionInstance?.usuario?.id}">${usuarioInstitucionInstance?.usuario?.encodeAsHTML()}</g:link></span>

                    </li>
                </g:if>

                <g:if test="${usuarioInstitucionInstance?.institucionEducativa}">
                    <li class="fieldcontain">
                        <span id="institucionEducativa-label" class="property-label"><g:message code="usuarioInstitucion.institucionEducativa.label" default="Institucion Educativa" /></span>

                        <span class="property-value" aria-labelledby="institucionEducativa-label"><g:link controller="institucionEducativa" action="show" id="${usuarioInstitucionInstance?.institucionEducativa?.id}">${usuarioInstitucionInstance?.institucionEducativa?.encodeAsHTML()}</g:link></span>

                    </li>
                </g:if>

            </ol>
            <g:form url="[resource:usuarioInstitucionInstance, action:'delete']" method="DELETE">
                <fieldset class="buttons">
                    <g:link class="edit" action="edit" resource="${usuarioInstitucionInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
