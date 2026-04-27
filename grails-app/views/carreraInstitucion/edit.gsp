<%@ page import="catalogo.CarreraInstitucion" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'carreraInstitucion.label', default: 'CarreraInstitucion')}" />
        <title>Modificación de carrera</title>
    </head>
    <body>
        <a href="#edit-carreraInstitucion" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR">
                    <li><g:link class="list" controller="institucionEducativa" action="index">Listado de instituciones</g:link></li>
                    <li><g:link class="list" controller="institucionEducativa" action="show" id="${params?.idInstitucion}">Detalles de la instituci&oacute;n</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="edit-carreraInstitucion" class="content scaffold-edit" role="main">
                <h1>Modificación de carrera</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${carreraInstitucionInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${carreraInstitucionInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                </ul>
            </g:hasErrors>
            <g:form url="[resource:carreraInstitucionInstance, action:'update']" method="PUT" >
                <g:hiddenField name="version" value="${carreraInstitucionInstance?.version}" />
                <fieldset class="form">
                    <g:render template="form"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:actionSubmit class="save" action="update" value="Guardar cambios" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
