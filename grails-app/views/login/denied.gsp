<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Acceso denegado</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
            </ul>
        </div>
        <div id="create-tituloElectronico" class="content scaffold-create" role="main">
            <h1>Acceso no permitido</h1>                                

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>

            <div class="loginBox"> <!--inner-->
                <div class="errors" style=" text-align: center;">
                    <b>
                        <g:message code="springSecurity.denied.message" />
                    </b>
                </div>
            </div>

        </div>
    </body>
</html>
