
<%@ page import="catalogo.CarreraInstitucion" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'carreraInstitucion.label', default: 'CarreraInstitucion')}" />
        <title>Detalles de la carrera</title>
    </head>
    <body>
        <a href="#show-carreraInstitucion" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index">Lista de carreras</g:link></li>
                <li><g:link class="create" action="create">Capturar carrera</g:link></li>
                </ul>
            </div>
            <div id="show-carreraInstitucion" class="content scaffold-show" role="main">
                <h1>Detalles de la carrera</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <ol class="property-list carreraInstitucion">

                <g:if test="${carreraInstitucionInstance?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="carreraInstitucion.id.label" default="Id" /></span>

                        <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${carreraInstitucionInstance}" field="id"/></span>

                    </li>
                </g:if>

                <g:if test="${carreraInstitucionInstance?.descripcion}">
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label"><g:message code="carreraInstitucion.descripcion.label" default="Descripcion" /></span>

                        <span class="property-value" aria-labelledby="descripcion-label"><g:fieldValue bean="${carreraInstitucionInstance}" field="descripcion"/></span>

                    </li>
                </g:if>

                <g:if test="${carreraInstitucionInstance?.autorizacionReconocimiento}">
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label">
                            Tipo de autorizaci&oacute;n o reconocimiento de estudios
                        </span>

                        <span class="property-value" aria-labelledby="rvoe-label">
                            ${carreraInstitucionInstance?.autorizacionReconocimiento?.id} - ${carreraInstitucionInstance?.autorizacionReconocimiento?.descripcion}
                        </span>

                    </li>
                </g:if>

                <g:if test="${carreraInstitucionInstance?.rvoe}">
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label"><g:message code="carreraInstitucion.rvoe.label" default="RVOE" /></span>

                        <span class="property-value" aria-labelledby="rvoe-label"><g:fieldValue bean="${carreraInstitucionInstance}" field="rvoe"/></span>

                    </li>
                </g:if>

                <g:if test="${carreraInstitucionInstance?.institucionEducativa}">
                    <li class="fieldcontain">
                        <span id="institucionEducativa-label" class="property-label"><g:message code="carreraInstitucion.institucionEducativa.label" default="Institucion Educativa" /></span>

                        <span class="property-value" aria-labelledby="institucionEducativa-label"><g:link controller="institucionEducativa" action="show" id="${carreraInstitucionInstance?.institucionEducativa?.id}">${carreraInstitucionInstance?.institucionEducativa?.encodeAsHTML()}</g:link></span>

                    </li>
                </g:if>

            </ol>
            <g:form url="[resource:carreraInstitucionInstance, action:'delete']" method="DELETE">
                <fieldset class="buttons">
                    <g:link class="edit" action="edit" resource="${carreraInstitucionInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
