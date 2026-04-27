<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
        <title>Registro de usuario</title>
    <asset:javascript src="jquery.curp-1.2.2.js"/>
    <script>
        $(function(){
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
        readOnly: false //Todos los campos de solo lectura
        });
        });
    </script>
</head>
<body>
    <a href="#create-usuario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
        <ul>
            <li><a href="#" class="home">Inicio</a></li>
        </ul>
    </div>
    <div id="create-usuario" class="content scaffold-create" role="main">
        <h1>Registro de usuario administrador</h1>
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
        <g:form url="[resource:usuarioInstance, action:'registrarAdministrador']" >
            <fieldset class="form">
                <g:render template="form" model="[admin: true]"/>
            </fieldset>
            <fieldset class="buttons">
                <g:submitButton name="create" class="save" value="Guardar cambios" />
            </fieldset>
        </g:form>
    </div>
</body>
</html>
