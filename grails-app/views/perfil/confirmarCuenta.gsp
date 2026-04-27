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


            <div class="loginBox"> <!--inner-->
                <div class="message" style=" text-align: center;">
                    <g:if test="${confirmado}">
                        <b>La confirmaci&oacute;n de su cuenta se realiz&oacute; con &eacute;xito.</b>
                    </g:if>
                    <g:else>
                        <b>Lo sentimos, no se pudo realizar la confirmaci&oacute;n de su cuenta</b>
                    </g:else>
                </div>
                <g:if test="${confirmado}">
                    <g:form action="descargarAcuseConfirmacion" method="POST" autocomplete='off' target="_blank">
                        <input type="hidden" name="folio" value="${params.folio}"/>
                        <section style=" text-align: center;">
                            <g:submitButton name="create" class="save" value="Descargar acuse" />
                        </section>                
                    </g:form>
                </g:if>
            </div>

        </div>
    </body>
</html>
