<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'usuarioInstitucion.label', default: 'UsuarioInstitucion')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-usuarioInstitucion" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR">
                    <li><g:link class="list" controller="usuario" action="index">Listado de usuarios</g:link></li>
                    <li><g:link class="list" controller="usuario" action="show" id="${params?.id}">Detalles del usuario</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="create-usuarioInstitucion" class="content scaffold-create" role="main">
                <h1>Asignar instituci&oacute;n</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${usuarioInstitucionInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${usuarioInstitucionInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                </ul>
            </g:hasErrors>
            <g:form url="[resource:usuarioInstitucionInstance, action:'save']" >
                <fieldset class="form">
                    <g:render template="form"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Guardar cambios" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
