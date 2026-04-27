<%@ page import="catalogo.InstitucionEducativa" %>

<div class="fieldcontain ${hasErrors(bean: institucionEducativaInstance, field: 'clave', 'error')} required">
    <label for="id">
        Clave
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="clave" maxlength="20" required="" value="${institucionEducativaInstance?.clave}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: institucionEducativaInstance, field: 'descripcion', 'error')} required">
    <label for="descripcion">
        <g:message code="institucionEducativa.descripcion.label" default="Descripcion" />
        <span class="required-indicator">*</span>
    </label>
    <g:textArea name="descripcion" cols="40" rows="5" maxlength="256" required="" value="${institucionEducativaInstance?.descripcion}"/>

</div>


