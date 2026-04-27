<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Cambio de contrase&ntilde;a</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" action="index">Perfil de usuario</g:link></li>
            </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Cambio de contrase&ntilde;a</h1>                                
            
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
            
            <g:form action="cambiarContrasena" id="${firmaElectronicaInstance?.id}">
                <fieldset class="alert">
                    Por favor considera las siguientes recomendaciones para cambiar tu contrase&ntilde;a: 
                    <br/><br/>
                    <b>- Tama&ntilde;o: </b> de 8 a 32 caracteres
                    <br/>
                    <b>- Letras: </b> de la A a la Z, mayúsculas y minúsculas
                    <br/>
                    <b>- N&uacute;meros: </b> del 0 al 9
                    <br/>
                    <b>- Simbolos:</b> ! " # $ % & ‘ ( ) * + , - . / : ; < = > ? @ [ \ ] ^ { | } ~
                    <br/>
                    <b>- Espacios:</b> no se pueden utilizar espacios al principio ni al final de la contrase&ntilde;a, pero s&iacute; se pueden incluir dentro de esta.
                    <br/><br/>
                    Recuerda que la contrase&ntilde;a es personal e intransferible.
                </fieldset>
                
                <fieldset class="form">
                    <g:render template="datosCompletos/contrasenaActual"/>
                    <g:render template="datosCompletos/contrasenaNueva"/>  
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Guardar cambios" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
