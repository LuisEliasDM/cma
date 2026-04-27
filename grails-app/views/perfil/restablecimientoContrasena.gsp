<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Restablecimiento de contrase&ntilde;a</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="#" class="home">Inicio</a></li>
            </ul>
        </div>
        <div id="create-tituloElectronico" class="content scaffold-create" role="main">
            <h1>Restablecimiento de contrase&ntilde;a</h1>                                

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

            <g:if test="${perfil}">
                <g:if test="${!perfil?.contrasenaRecuperada}">
                    <div class="loginBox"> <!--inner-->
                        <div class="message" style=" text-align: center;">
                            <b>Para recuperar su cuenta, es necesario establecer su contrase&ntilde;a nueva. Misma que le servir&aacute; para ingresar nuevamente al sistema.</b>
                        </div>

                        <g:form action="restablecerContrasena" method="POST" autocomplete='off'>
                            <input type="hidden" name="folio" value="${params.folio}"/>
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
                                <g:render template="datosCompletos/contrasenaNueva"/>       
                            </fieldset>                

                            <section style=" text-align: center;">
                                <g:submitButton name="create" class="save" value="Guardar contraseña" />
                            </section>                
                        </g:form>

                    </div>
                </g:if>
                <g:else>
                    <div class="loginBox"> <!--inner-->
                        <div class="message" style=" text-align: center;">
                            <b>La recuperaci&oacute;n de su contrase&ntilde;a se realiz&oacute; con &eacute;xito.</b>
                        </div>
                    </div>
                </g:else>
            </g:if>
            <g:else>
                <div class="loginBox"> <!--inner-->
                    <div class="message" style=" text-align: center;">
                        <b>La recuperaci&oacute;n de contrase&ntilde;a mediante este enlace, no se encuentra disponible</b>
                    </div>
                </div>
            </g:else>

        </div>
    </body>
</html>
