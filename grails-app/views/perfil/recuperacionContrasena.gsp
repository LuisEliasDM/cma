<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Recuperaci&oacute;n de contrase&ntilde;a</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="#" class="home">Inicio</a></li>
            </ul>
        </div>
        <div id="create-tituloElectronico" class="content scaffold-create" role="main">
            <h1>Recuperaci&oacute;n de contrase&ntilde;a</h1>                                

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>

            <div class="loginBox"> <!--inner-->
                <div class="message" style=" text-align: center;">
                    <b>Por favor, ingrese su correo electr&oacute;nico para recuperar su contrase&ntilde;a</b>
                </div>
                <g:form action="recuperarContrasena" method="POST" autocomplete='off'>
                    <section style=" text-align: center;">
                        <br/>
                        <g:field type="email" name="correoElectronico" required="" style="width: 400px;"/>
                        
                        <br/><br/><br/>
                        <g:submitButton name="create" class="save" value="Recuperar contraseña" />
                        <br/><br/>
                    </section>                
                </g:form>


            </div>

        </div>
    </body>
</html>
