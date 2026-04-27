<%@ page import="seguridad.Usuario" %>
<div class="pluginCurp"></div>
<br/>
<div class="fieldcontain">
    <label>
        Correo electr&oacute;nico
    </label>
    <g:field type="email" name="correoElectronico" required="" value="${usuarioInstance?.perfil?.correoElectronico}" style="width: 400px;"/>
</div>

<div class="fieldcontain">
    <label>
        Tel&eacute;fono fijo
    </label>
    <g:textField name="telefonoFijo" value="${usuarioInstance?.perfil?.telefonoFijo}"/>
</div>

<div class="fieldcontain">
    <label>
        Tel&eacute;fono m&oacute;vil
    </label>
    <g:textField name="telefonoMovil" value="${usuarioInstance?.perfil?.telefonoMovil}"/>
</div>

<div class="fieldcontain required">
    <label>
        Cargo
    </label>
    <g:textField name="cargo" required="" value="${usuarioInstance?.perfil?.cargo}" style="width: 650px;"/>
</div>

<br/>

<g:if test="${!usuarioInstance?.id && !admin}">
    <div class="fieldcontain">
        <label>
            Funci&oacute;n en la plataforma
        </label>
        <g:select name="idRol" from="${seguridad.Rol.list()}" optionKey="id" required="" noSelection="${['':'Selecciona...']}"/>
    </div>

    <div class="fieldcontain">
        <label>
            Instituci&oacute;n educativa
        </label>
        <g:select name="idInstitucionEducativa" from="${catalogo.InstitucionEducativa.list()}" optionKey="id" noSelection="${['':'Selecciona...']}"/>
    </div>
</g:if>