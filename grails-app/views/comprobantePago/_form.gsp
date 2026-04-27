<%@ page import="paquete.ComprobantePago" %>

<div class="fieldcontain ${hasErrors(bean: comprobantePagoInstance, field: 'paquete', 'error')} required">
	<label for="paquete">
		Paquete
	</label>
        ${paquete.Paquete.get(params.idPaquete)}
        <input type="hidden" name="paquete.id" value="${params?.idPaquete}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: comprobantePagoInstance, field: 'poliza', 'error')} required">
    <label for="poliza">
        # de poliza
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="poliza" maxlength="32" required="" value="${comprobantePagoInstance?.poliza}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: comprobantePagoInstance, field: 'monto', 'error')} required">
    <label for="monto">
        Monto
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="monto" maxlength="16" required="" value="${comprobantePagoInstance?.monto}"/>
</div>

