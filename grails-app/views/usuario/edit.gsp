<%@ page import="seguridad.Usuario" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
        <title>Modificaci&oacute;n de usuario</title>
        <asset:javascript src="jquery.curp-1.2.2.js"/>
        <script>
            $(function(){
                var initData = {
                    curp: "${usuarioInstance?.perfil?.curp}",
                    nombre: "${usuarioInstance?.perfil?.nombre}",
                    aPaterno: "${usuarioInstance?.perfil?.primerApellido}",
                    aMaterno: "${usuarioInstance?.perfil?.segundoApellido}",
                    sexo: "${usuarioInstance?.perfil?.sexo}",
                    fNacimiento: {
                        dia: "${usuarioInstance?.perfil?.fechaNacimiento? (usuarioInstance?.perfil?.fechaNacimiento[Calendar.DAY_OF_MONTH]).toString().padLeft(2,"0"):""}",
                        mes: "${usuarioInstance?.perfil?.fechaNacimiento? (usuarioInstance?.perfil?.fechaNacimiento[Calendar.MONTH] + 1).toString().padLeft(2,"0"):""}",
                        anio: "${usuarioInstance?.perfil?.fechaNacimiento? (usuarioInstance?.perfil?.fechaNacimiento[Calendar.YEAR]).toString().padLeft(4,"0"):""}"
                    },
                    eNacimiento: "${usuarioInstance?.perfil?.estadoNacimiento}",
                    validate: false //Obtener los datos automaticamente a partir de la CURP
                };
            
                $(".pluginCurp").validaCurp({
                    hidden: false, //Mostrar u ocultar eL campo curp
                    inputName: {//Define el valor del atributo "name" de los campos
                        curp: "curp",
                        nombre: "nombre",
                        aPaterno: "primerApellido",
                        aMaterno: "segundoApellido",
                        sexo: "sexo",
                        fNacimiento: "fechaNacimiento",
                        eNacimiento: "estadoNacimiento"
                    },
                    columns: false, //Mostrar campos en 2 columnas
                    initData: initData, //Mapa de datos iniciales
                    readOnly: false //Todos los campos de solo lectura
                });
            });
        </script>
    </head>
    <body>
        <a href="#edit-usuario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="usuario">Lista de usuarios</g:link></li>
                <li><g:link class="list" controller="usuario" action="show" id="${usuarioInstance.id}">Detalles del usuario</g:link></li>
                </ul>
            </div>
            <div id="edit-usuario" class="content scaffold-edit" role="main">
                <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${usuarioInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${usuarioInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                </ul>
            </g:hasErrors>
            <g:form url="[resource:usuarioInstance, action:'update']" method="PUT" >
                <g:hiddenField name="version" value="${usuarioInstance?.version}" />
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
