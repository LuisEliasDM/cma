<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'firmaElectronica.label', default: 'FirmaElectronica')}" />
        <title>Capturar e.firma</title>
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
        <a href="#create-firmaElectronica" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                    <li><g:link class="list" action="index">Listado de e.firmas</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Capturar e.firma</h1>                
            
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${firmaElectronicaInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${firmaElectronicaInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </g:hasErrors>
            <g:uploadForm controller="firmaElectronica" action="guardarInformacion" id="${firmaElectronicaInstance?.id}">
                <fieldset class="form">
                    <g:render template="datosCompletos/fiel"/>       
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Guardar datos de la e.firma" />
                </fieldset>
            </g:uploadForm>
        </div>
    </body>
</html>
