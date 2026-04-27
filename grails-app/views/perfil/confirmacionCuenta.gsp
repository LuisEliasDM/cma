<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Confirmaci&oacute;n de cuenta</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="#" class="home">Inicio</a></li>
            </ul>
        </div>
        <div id="create-tituloElectronico" class="content scaffold-create" role="main">
            <h1>Confirmaci&oacute;n de cuenta</h1>                                

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
                <g:if test="${!perfil?.confirmado}">
                    <div class="loginBox"> <!--inner-->
                        <div class="message" style=" text-align: center;">
                            <b>Para confirmar su cuenta es necesario establecer su contrase&ntilde;a nueva, misma que le servir&aacute; para ingresar al sistema.</b>
                        </div>

                        <g:form action="confirmarCuenta" method="POST" autocomplete='off'>
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
                            <b>La confirmaci&oacute;n de su cuenta se realiz&oacute; con &eacute;xito.</b>
                        </div>
                        <g:form action="descargarAcuseConfirmacion" method="POST" autocomplete='off' target="_blank">
                            <input type="hidden" name="folio" value="${params.folio}"/>
                            <section style=" text-align: center;">
                                <g:submitButton name="create" class="save" value="Descargar acuse" />
                            </section>                
                        </g:form>
                    </div>
                </g:else>
            </g:if>
            <g:else>
                <div class="loginBox"> <!--inner-->
                    <div class="message" style=" text-align: center;">
                        <b>La confirmaci&oacute;n de cuenta mediante este enlace, no se encuentra disponible</b>
                    </div>
                </div>
            </g:else>

        </div>
    </body>
</html>
