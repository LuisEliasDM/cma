<div class="fieldcontain ${hasErrors(bean: paqueteInstance, field: 'institucionEducativa', 'error')} required">
    <label for="institucionEducativa">
        Instituci&oacute;n educaiva
        <span class="required-indicator">*</span>
    </label>
    <g:select name="institucionEducativa.id" from="${catalogo.InstitucionEducativa.list()}" optionKey="id" noSelection="${['':'Selecciona...']}" value="${paqueteInstance?.institucionEducativa?.id}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: paqueteInstance, field: 'folio', 'error')} required">
    <label for="id">
        Folio
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="folio" maxlength="32" required="" value="${paqueteInstance?.folio}"/>
</div>