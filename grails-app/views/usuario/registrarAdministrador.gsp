<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Confirmaci&oacute;n de correo electr&oacute;nico</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="#" class="home">Inicio</a></li>
            </ul>
        </div>
        <div id="create-tituloElectronico" class="content scaffold-create" role="main">
            <h1>Confirmaci&oacute;n de correo electr&oacute;nico</h1>                                

            <div class="loginBox"> <!--inner-->
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:if test="${flash.error}">
                    <div class="errors" role="status">${flash.error}</div>
                </g:if>
            </div>

        </div>
    </body>
</html>
