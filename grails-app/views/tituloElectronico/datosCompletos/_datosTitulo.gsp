<h3>Información general</h3>
<g:if test="${!verificacion}">
    <div class="fieldcontain">
        <label>Versión:</label>
        ${tituloElectronicoInstance?.version}
    </div>
</g:if>

<div class="fieldcontain">
    <label>Folio de control:</label>
    ${tituloElectronicoInstance?.folioControl}
</div>

<g:if test="${!verificacion}">
    <div class="fieldcontain">
        <label>Estatus:</label>
        #${tituloElectronicoInstance?.getEstatus()?.id} - 
        ${tituloElectronicoInstance?.getEstatus()?.descripcion}
    </div>
</g:if>

<g:if test="${!verificacion}">
    <div class="fieldcontain">
        <label>Fecha de registro:</label>
        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${tituloElectronicoInstance?.dateCreated}"/>
    </div>
</g:if>
<br/>

<h3>Información de la institución</h3>
<div class="fieldcontain">
    <label>Identificador y Nombre de la institución educativa:</label>
    ${tituloElectronicoInstance?.institucion?.cveInstitucion} - ${tituloElectronicoInstance?.institucion?.nombreInstitucion}
</div>
<br/>

<h3>Información de la carrera cursada</h3>
<div class="fieldcontain">
    <label>Identificador y nombre de la carrera:</label>
    ${tituloElectronicoInstance?.carrera?.cveCarrera} - ${tituloElectronicoInstance?.carrera?.nombreCarrera}
</div>
<div class="fieldcontain">
    <label>Fecha de inicio de la carrera cursada:</label>
    <g:formatDate format="dd/MM/yyyy" date="${tituloElectronicoInstance?.carrera?.fechaInicio}"/>
</div>
<div class="fieldcontain">
    <label>Fecha de terminaci&oacute;n de la carrera cursada:</label>
    <g:formatDate format="dd/MM/yyyy" date="${tituloElectronicoInstance?.carrera?.fechaTerminacion}"/>
</div>
<div class="fieldcontain">
    <label>Tipo de autorización o reconocimiento de estudios otorgado por la autoridad competente:</label>
    ${tituloElectronicoInstance?.carrera?.idAutorizacionReconocimiento} - ${tituloElectronicoInstance?.carrera?.autorizacionReconocimiento}
</div>
<div class="fieldcontain">
    <label>Número de reconocimiento de validez oficial de estudios (RVOE):</label>
    ${tituloElectronicoInstance?.carrera?.numeroRvoe}
</div>
<br/>

<h3>Información del profesionista</h3>
<g:if test="${!verificacion}">
    <div class="fieldcontain">
        <label>CURP:</label>
        ${tituloElectronicoInstance?.profesionista?.curp}
    </div>
</g:if>

<div class="fieldcontain">
    <label>Nombre completo:</label>
    ${tituloElectronicoInstance?.profesionista?.nombreCompleto}
</div>

<g:if test="${!verificacion}">
    <div class="fieldcontain">
        <label>Correo electrónico:</label>
        ${tituloElectronicoInstance?.profesionista?.correoElectronico}
    </div>
</g:if>
<br/>

<h3>Información sobre la expedición del título</h3>
<div class="fieldcontain">
    <label>Fecha de expedición:</label>
    <g:formatDate format="dd/MM/yyyy" date="${tituloElectronicoInstance?.expedicion?.fechaExpedicion}"/>
</div>
<div class="fieldcontain">
    <label>Modalidad de titulación:</label>
    ${tituloElectronicoInstance?.expedicion?.idModalidadTitulacion} - ${tituloElectronicoInstance?.expedicion?.modalidadTitulacion}
</div>
<g:if test="${tituloElectronicoInstance?.expedicion?.idModalidadTitulacion == 1}">
    <div class="fieldcontain">
        <label>Fecha del examen profesional:</label>
        <g:formatDate format="dd/MM/yyyy" date="${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional}"/>
    </div>
</g:if>
<g:else>
    <div class="fieldcontain">
        <label>Fecha de exención del examen profesional:</label>
        <g:formatDate format="dd/MM/yyyy" date="${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional}"/>
    </div>
</g:else>

<g:if test="${!verificacion}">
    <div class="fieldcontain">
        <label>Cumplió con servicio social:</label>
        <g:if test="${tituloElectronicoInstance?.expedicion?.cumplioServicioSocial == 0}">
            No
        </g:if>
        <g:elseif test="${tituloElectronicoInstance?.expedicion?.cumplioServicioSocial == 1}">
            Si
        </g:elseif>
    </div>

    <div class="fieldcontain">
        <label>Fundamento legal de servicio social:</label>
        ${tituloElectronicoInstance?.expedicion?.idFundamentoLegalServicioSocial} - ${tituloElectronicoInstance?.expedicion?.fundamentoLegalServicioSocial}
    </div>
</g:if>

<div class="fieldcontain">
    <label>Entidad Federativa:</label>
    ${tituloElectronicoInstance?.expedicion?.idEntidadFederativa} - ${tituloElectronicoInstance?.expedicion?.entidadFederativa}
</div>

<br/>

<g:if test="${!verificacion}">
    <h3>Información del antecedente de estudios del profesionista</h3>
    <div class="fieldcontain">
        <label>Institución de procedencia:</label>
        ${tituloElectronicoInstance?.antecedente?.institucionProcedencia}
    </div>
    <div class="fieldcontain">
        <label>Tipo de estudios:</label>
        ${tituloElectronicoInstance?.antecedente?.idTipoEstudioAntecedente} - ${tituloElectronicoInstance?.antecedente?.tipoEstudioAntecedente}
    </div>
    <div class="fieldcontain">
        <label>Entidad Federativa:</label>
        ${tituloElectronicoInstance?.antecedente?.idEntidadFederativa} - ${tituloElectronicoInstance?.antecedente?.entidadFederativa}
    </div>
    <div class="fieldcontain">
        <label>Fecha de inicio de los estudios (opcional):</label>
        <g:formatDate format="dd/MM/yyyy" date="${tituloElectronicoInstance?.antecedente?.fechaInicio}"/>
    </div>
    <div class="fieldcontain">
        <label>Fecha de terminaci&oacute;n de los estudios:</label>
        <g:formatDate format="dd/MM/yyyy" date="${tituloElectronicoInstance?.antecedente?.fechaTerminacion}"/>
    </div>
    <div class="fieldcontain">
        <label>Número de cédula profesional (opcional):</label>
        ${tituloElectronicoInstance?.antecedente?.noCedula}
    </div>
    <br/>
</g:if>

<g:if test="${!verificacion}">
    <h3>Información de las firmas de los responsables de la Autoridad Educativa</h3>
    <g:if test="${tituloElectronicoInstance?.firmaResponsables?.firmaResponsable}">
        <g:each var="firmaResponsable" in="${tituloElectronicoInstance?.firmaResponsables?.firmaResponsable}">
            <div class="fieldcontain">
                <label>CURP:</label>
                ${firmaResponsable?.curp}
            </div>
            <div class="fieldcontain">
                <label>Nombre completo:</label>
                ${firmaResponsable?.nombreCompleto}
            </div>
            <div class="fieldcontain">
                <label>Cargo:</label>
                ${firmaResponsable?.idCargo} - ${firmaResponsable?.cargo}
            </div>
            <div class="fieldcontain"><label></label>
                    __
            </div>
        </g:each>
    </g:if>
    <g:else>
        <div class="fieldcontain"><label></label>
                No hay firmas registradas
        </div>
    </g:else>
    <br/>
</g:if>

<g:if test="${!verificacion}">
    <h3>Información adicional</h3>
    
    <ol class="property-list">
        <li class="fieldcontain">
            <label>Aclaraciones adicionales:</label>
        </li>
        
        <li class="fieldcontain">
            <span class="property-value">
                ${tituloElectronicoInstance?.aclaracion}
            </span>
        </li>
    </ol>
</g:if>

<br/>