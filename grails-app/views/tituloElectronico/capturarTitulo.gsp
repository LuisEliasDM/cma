<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Captura de titulo electrónico</title>
        <asset:javascript src="jquery.curp-1.2.2.js"/>
        <asset:javascript src="tituloElectronico.js"/>
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
        <a href="#create-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Título Electrónico</h1>                
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:form controller="TituloElectronico" action="guardarInformacion">
                <fieldset class="form">
                    <label>A continuación, captura la información del título electrónico: </label><br/><br/>
                    <br/><br/><h3>Información de la institución</h3>
                    <g:render template="datosCompletos/institucion"/>  
                    <br/><br/><h3>Información de la carrera cursada</h3>
                    <g:render template="datosCompletos/carrera"/>       
                    <br/><br/><h3>Información del profesionista</h3>
                    <g:render template="datosCompletos/profesionista"/>        
                    <br/><br/><h3>Información sobre la expedición del título</h3>
                    <g:render template="datosCompletos/expedicion"/>     
                    <br/><br/><h3>Información de antecedentes de estudios del profesionista</h3>
                    <g:render template="datosCompletos/antecedente"/>
                    <br/><br/><h3>Información adicional</h3>
                    <g:render template="datosCompletos/adicional"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Guardar datos del título electrónico" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
