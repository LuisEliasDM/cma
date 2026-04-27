<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Acceso al sistema</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="#" class="home">Inicio</a></li>
            </ul>
        </div>
        <div id="create-tituloElectronico" class="content scaffold-create" role="main">
            <h1>Acceso al sistema</h1>                                

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>

            <div class="loginBox"> <!--inner-->
                <div class="message" style=" text-align: center;">
                    <b>Por favor, ingrese sus datos de acceso</b>
                </div>
                <form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
                    <section class="sectionLeft" style=" text-align: center;">
                        <input type="hidden" id="url" name="url" value="/j_spring_security_check">
                        <asset:image src="user-icon.png" alt="" style="display: inline; width: 150px;"/>
                    </section>
                    <section class="sectionRight">
                        <div class="fieldcontain">
                            <label for="username">Usuario:</label>
                            <input type="text" class="text_ expanded" name="j_username" required="" id="username" maxlength="64">
                        </div>

                        <div class="fieldcontain">
                            <label for="password">Contraseña:</label>
                            <input type="password" class="text_ expanded" name="j_password" required="" id="password" maxlength="64">
                        </div>
                        <br/>
                        <p id="remember_me_holder">
                            <input type="checkbox" class="chk" name="_spring_security_remember_me" id="remember_me">
                            <label for="remember_me">Mantener mi sesi&oacute;n inciada</label>
                            <input type="submit" id="submit" value="Ingresar" style=" float: right; margin-right: 10px;">
                        </p>
                    </section>
                    <div style="text-align: center">
                        <br>
                        <g:link controller="perfil" action="recuperacionContrasena">Olvid&eacute; mi contrase&ntilde;a</g:link>
                        
                        <br> <br>
                    </div>
                </form>


            </div>

        </div>
    </body>
</html>
