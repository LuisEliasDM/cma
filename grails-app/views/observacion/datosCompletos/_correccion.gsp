<ol class="property-list">
    <li class="fieldcontain">
        <label>Observaciones:</label>
    </li>
    <g:if test="${observaciones}">
        <g:each var="observacion" in="${observaciones}">
            <g:if test="${observacion?.estatus}">
                <li class="fieldcontain">
                    <span class="property-value">
                        <b>${observacion?.usuario?.username} (<g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${observacion?.dateCreated}"/>):</b> ${observacion?.descripcion}
                    </span>
                </li>
            </g:if>
        </g:each>
    </g:if>
    <g:else>
        <li class="fieldcontain">
            <span class="property-value">
                No hay observaciones registradas
            </span>
        </li>
    </g:else>
</ol>

<div class="fieldcontain">
    <label>
        D&iacute;as para corregir (naturales):
    </label>
    <g:select name="dias" from="${(1..10)}" noSelection="${['':'']}" default="none" required="" />
</div>