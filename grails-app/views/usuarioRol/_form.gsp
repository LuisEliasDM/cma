<%@ page import="seguridad.UsuarioRol" %>

<div class="fieldcontain ${hasErrors(bean: usuarioRolInstance, field: 'usuario', 'error')} required">
        <label for="usuario">Usuario</label>
        ${seguridad.Usuario.get(params.id).username}
    </div>
    <input type="hidden" name="usuario.id" value="${params.id}"/>

<div class="fieldcontain ${hasErrors(bean: usuarioRolInstance, field: 'rol', 'error')} required">
	<label for="rol">
		<g:message code="usuarioRol.rol.label" default="Rol" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="rol" name="rol.id" from="${seguridad.Rol.list()}" optionKey="id" required="" value="${usuarioRolInstance?.rol?.id}" class="many-to-one"/>

</div>
