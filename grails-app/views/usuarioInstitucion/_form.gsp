<%@ page import="seguridad.UsuarioInstitucion" %>

<div class="fieldcontain ${hasErrors(bean: usuarioRolInstance, field: 'usuario', 'error')} required">
        <label for="usuario">Usuario</label>
        ${seguridad.Usuario.get(params.id).username}
    </div>
    <input type="hidden" name="usuario.id" value="${params.id}"/>

<div class="fieldcontain ${hasErrors(bean: usuarioInstitucionInstance, field: 'institucionEducativa', 'error')} required">
	<label for="institucionEducativa">
		<g:message code="usuarioInstitucion.institucionEducativa.label" default="Institucion Educativa" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="institucionEducativa" name="institucionEducativa.id" from="${catalogo.InstitucionEducativa.list()}" optionKey="id" required="" value="${usuarioInstitucionInstance?.institucionEducativa?.id}" class="many-to-one"/>

</div>

