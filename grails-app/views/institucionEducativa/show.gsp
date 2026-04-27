
<%@ page import="catalogo.InstitucionEducativa" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'institucionEducativa.label', default: 'InstitucionEducativa')}" />
        <title>Detalles de la institución educativa</title>
    </head>
    <body>
        <a href="#show-institucionEducativa" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR">
                    <li><g:link class="list" controller="institucionEducativa" action="index">Listado de instituciones</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="show-institucionEducativa" class="content scaffold-show" role="main">
                <h1>Detalles de la institución educativa</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <ol class="property-list institucionEducativa">

                <li class="fieldcontain">
                    <span id="descripcion-label" class="property-label">Clave</span>

                    <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${institucionEducativaInstance}" field="clave"/></span>

                </li>

                <li class="fieldcontain">
                    <span id="descripcion-label" class="property-label"><g:message code="institucionEducativa.descripcion.label" default="Descripcion" /></span>

                    <span class="property-value" aria-labelledby="descripcion-label"><g:fieldValue bean="${institucionEducativaInstance}" field="descripcion"/></span>

                </li>

                <li class="fieldcontain">
                    <span id="carrerasInstitucion-label" class="property-label">Carreras</span>

                    <g:each in="${institucionEducativaInstance.carrerasInstitucion}" var="carreraInstitucion">
                        <span class="property-value">
                            ${carreraInstitucion}
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                                -
                                <g:link class="edit" controller="carreraInstitucion" action="edit" id="${carreraInstitucion.id}" params="${["idInstitucion": institucionEducativaInstance.id]}">Editar</g:link>
                                |
                                <g:link class="delete confirm" controller="carreraInstitucion" action="delete" id="${carreraInstitucion.id}">Eliminar</g:link>
                            </sec:ifAnyGranted>
                        </span>
                    </g:each>
                    <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                        <br/>
                        <span class="property-value">
                            <g:link class="create" controller="carreraInstitucion" action="create" params="${["idInstitucion": institucionEducativaInstance.id]}">Agregar carrera</g:link>
                        </span>
                    </sec:ifAnyGranted>
                </li>

            </ol>
            <g:form url="[resource:institucionEducativaInstance, action:'delete']" method="DELETE">
                <fieldset class="buttons">
                    <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                        <g:link class="edit" action="edit" resource="${institucionEducativaInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </sec:ifAnyGranted>
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
