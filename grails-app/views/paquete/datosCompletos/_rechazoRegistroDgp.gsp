<ol class="property-list">
    <li class="fieldcontain">
        <label>Titulos electr&oacute;nicos sin registro en DGP:</label>
    </li>
    <g:if test="${titulosNoRegistrados}">
        <g:each var="tituloElectronicoInstance" in="${titulosNoRegistrados}">
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
                No hay titulos sin registro en DGP
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