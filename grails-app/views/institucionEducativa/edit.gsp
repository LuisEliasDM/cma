<%@ page import="catalogo.InstitucionEducativa" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'institucionEducativa.label', default: 'InstitucionEducativa')}" />
        <title>Modificación de institución educativa</title>
    </head>
    <body>
        <a href="#edit-institucionEducativa" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR">
                    <li><g:link class="list" controller="institucionEducativa" action="index">Listado de instituciones</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="edit-institucionEducativa" class="content scaffold-edit" role="main">
                <h1>Modificación de institución educativa</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${institucionEducativaInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${institucionEducativaInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                </ul>
            </g:hasErrors>
            <g:form url="[resource:institucionEducativaInstance, action:'update']" method="PUT" >
                <g:hiddenField name="version" value="${institucionEducativaInstance?.version}" />
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
