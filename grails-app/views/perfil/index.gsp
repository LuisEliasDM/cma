<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>Perfil de usuario</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
            </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Perfil de usuario</h1>                                
            
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <fieldset class="form">
                <div class="fieldcontain">
                    <label>Nombre de usuario:</label>
                    ${usuarioInstance?.username}
                </div>
                <div class="fieldcontain">
                    <label>N&uacute;mero de identificaci&oacute;n:</label>
                    ${usuarioInstance?.perfil?.numeroIdentificacion}
                </div>
                <div class="fieldcontain">
                    <label>CURP:</label>
                    ${usuarioInstance?.perfil?.curp}
                </div>
                <div class="fieldcontain">
                    <label>Nombre completo:</label>
                    ${usuarioInstance?.perfil?.nombreCompleto}
                </div>
                <div class="fieldcontain">
                    <label>Cargo:</label>
                    ${usuarioInstance?.perfil?.cargo}
                </div>
                <div class="fieldcontain">
                    <label>Sexo:</label>
                    ${usuarioInstance?.perfil?.sexo}
                </div>
                <div class="fieldcontain">
                    <label>Fecha de nacimiento:</label>
                    <g:formatDate format="dd/MM/yyyy" date="${usuarioInstance?.perfil?.fechaNacimiento}"/>
                </div>
                <div class="fieldcontain">
                    <label>Entidad de nacimiento:</label>
                    ${usuarioInstance?.perfil?.estadoNacimiento}
                </div>
                <div class="fieldcontain">
                    <label>Correo electr&oacute;nico:</label>
                    ${usuarioInstance?.perfil?.correoElectronico}
                </div>
                <div class="fieldcontain">
                    <label>Tel&eacute;fono fijo:</label>
                    ${usuarioInstance?.perfil?.telefonoFijo}
                </div>
                <div class="fieldcontain">
                    <label>Tel&eacute;fono m&oacute;vil:</label>
                    ${usuarioInstance?.perfil?.telefonoMovil}
                </div>
                
            </fieldset>
            <fieldset class="buttons">
                <g:link class="edit" action="cambioContrasena">Cambiar contrase&ntilde;a</g:link>
            </fieldset>
        </div>
    </body>
</html>
