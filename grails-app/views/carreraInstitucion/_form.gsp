<%@ page import="catalogo.CarreraInstitucion" %>

<div class="fieldcontain ${hasErrors(bean: carreraInstitucionInstance, field: 'institucionEducativa', 'error')} required">
	<label for="institucionEducativa">
		Instituci&oacute;n educaiva
	</label>
        ${catalogo.InstitucionEducativa.get(params.idInstitucion)}
        <input type="hidden" name="institucionEducativa.id" value="${params?.idInstitucion}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: carreraInstitucionInstance, field: 'clave', 'error')} required">
	<label for="id">
		Clave
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="clave" maxlength="20" required="" value="${carreraInstitucionInstance?.clave}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: carreraInstitucionInstance, field: 'clave', 'error')} required">
	<label for="id">
		Tipo de autorizaci&oacute;n o reconocimiento de estudios
		<span class="required-indicator">*</span>
	</label>
        <g:select name="autorizacionReconocimiento.id" from="${autorizacionReconocimientoList}" optionKey="id" optionValue="${{it.id +' - '+it.descripcion}}" noSelection="${['':'Selecciona...']}" required="" value="${carreraInstitucionInstance?.autorizacionReconocimiento?.id}" />

</div>

<div class="fieldcontain ${hasErrors(bean: carreraInstitucionInstance, field: 'clave', 'error')} required">
	<label for="id">
		N&uacute;mero de reconocimiento de validez oficial de etudios (RVOE)
	</label>
	<g:textField name="rvoe" maxlength="16" value="${carreraInstitucionInstance?.rvoe}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: carreraInstitucionInstance, field: 'descripcion', 'error')} required">
	<label for="descripcion">
		<g:message code="carreraInstitucion.descripcion.label" default="Descripcion" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="descripcion" cols="40" rows="5" maxlength="256" required="" value="${carreraInstitucionInstance?.descripcion}"/>

</div>

