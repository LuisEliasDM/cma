<ol class="property-list">
    <li class="fieldcontain">
        <label>Titulos electr&oacute;nicos sin autenticaci&oacute;n:</label>
    </li>
    <g:if test="${titulosNoAutenticados}">
        <g:each var="tituloElectronicoInstance" in="${titulosNoAutenticados}">
            <li class="fieldcontain">
                <span class="property-value">
                    <b>${tituloElectronicoInstance?.profesionista?.curp}:</b> ${tituloElectronicoInstance?.profesionista?.getNombreCompleto()}
                </span>
            </li>
        </g:each>
    </g:if>
    <g:else>
        <li class="fieldcontain">
            <span class="property-value">
                No hay titulos sin autenticar
            </span>
        </li>
    </g:else>
</ol>

<div class="fieldcontain">
    <label>
        Motivo del rechazo del paquete:
    </label>
    <g:textArea name="motivoRechazo" value="" rows="5" style="width: 500px" required=""/>
</div>