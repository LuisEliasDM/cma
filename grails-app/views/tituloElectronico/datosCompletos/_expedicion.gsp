<div class="fieldcontain">
    <label for="fechaExpedicion">
        Fecha de expedición:		
    </label>
    <g:select name="fechaExpedicionDia" from="${(1..31)}" noSelection="${['':'']}" required="" value="${tituloElectronicoInstance?.expedicion?.fechaExpedicion?tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.DAY_OF_MONTH]:""}" default="none" />
    <select name="fechaExpedicionMes" required="">
        <option value=""></option>
        <option value="1" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 0? "selected": ""):""}>Enero</option>
        <option value="2" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 1? "selected": ""):""}>Febrero</option>
        <option value="3" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 2? "selected": ""):""}>Marzo</option>
        <option value="4" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 3? "selected": ""):""}>Abril</option>
        <option value="5" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 4? "selected": ""):""}>Mayo</option>
        <option value="6" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 5? "selected": ""):""}>Junio</option>
        <option value="7" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 6? "selected": ""):""}>Julio</option>
        <option value="8" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 7? "selected": ""):""}>Agosto</option>
        <option value="9" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 8? "selected": ""):""}>Septiembre</option>
        <option value="10" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 9? "selected": ""):""}>Octubre</option>
        <option value="11" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 10? "selected": ""):""}>Noviembre</option>
        <option value="12" ${tituloElectronicoInstance?.expedicion?.fechaExpedicion? (tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.MONTH] == 11? "selected": ""):""}>Diciembre</option>
    </select>
    <g:select name="fechaExpedicionAnio" from="${(new Date()[Calendar.YEAR]..1970)}" noSelection="${['':'']}" required="" value="${tituloElectronicoInstance?.expedicion?.fechaExpedicion?tituloElectronicoInstance?.expedicion?.fechaExpedicion[Calendar.YEAR]:""}" default="none" />

</div>

<div class="fieldcontain  ">
    <label for="idModalidadTitulacion">
        Modalidad de titulación:
    </label>
    <g:select name="idModalidadTitulacion" from="${catalogo.ModalidadTitulacion.list()}" optionKey="id" optionValue="${{it.id +' - '+it.descripcion}}" noSelection="${['':'Selecciona...']}" required="" value="${tituloElectronicoInstance?.expedicion?.idModalidadTitulacion}" />
</div>

<div class="fieldcontain fep" <g:if test="${tituloElectronicoInstance?.expedicion?.idModalidadTitulacion != 1 && !tituloElectronicoInstance?.expedicion?.fechaExamenProfesional}">style="display: none;"</g:if>>
    <label for="fechaExamenProfesional">
        Fecha del examen profesional (opcional, dependiendo la modalidad de titulación):
    </label>
    <g:select name="fechaExamenProfesionalDia" from="${(1..31)}" noSelection="${['':'']}" value="${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional?tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.DAY_OF_MONTH]:""}" default="none"/>
    <select name="fechaExamenProfesionalMes">
        <option value=""></option>
        <option value="1" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 0? "selected": ""):""}>Enero</option>
        <option value="2" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 1? "selected": ""):""}>Febrero</option>
        <option value="3" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 2? "selected": ""):""}>Marzo</option>
        <option value="4" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 3? "selected": ""):""}>Abril</option>
        <option value="5" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 4? "selected": ""):""}>Mayo</option>
        <option value="6" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 5? "selected": ""):""}>Junio</option>
        <option value="7" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 6? "selected": ""):""}>Julio</option>
        <option value="8" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 7? "selected": ""):""}>Agosto</option>
        <option value="9" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 8? "selected": ""):""}>Septiembre</option>
        <option value="10" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 9? "selected": ""):""}>Octubre</option>
        <option value="11" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 10? "selected": ""):""}>Noviembre</option>
        <option value="12" ${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.MONTH] == 11? "selected": ""):""}>Diciembre</option>
    </select>
    <g:select name="fechaExamenProfesionalAnio" from="${(new Date()[Calendar.YEAR]..1970)}" noSelection="${['':'']}" value="${tituloElectronicoInstance?.expedicion?.fechaExamenProfesional?tituloElectronicoInstance?.expedicion?.fechaExamenProfesional[Calendar.YEAR]:""}" default="none"/>
    
</div>

<div class="fieldcontain feep" <g:if test="${tituloElectronicoInstance?.expedicion?.idModalidadTitulacion == 1 && !tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional}">style="display: none;"</g:if>>
    <label for="fechaExencionExamenProfesional">
        Fecha de exención del examen profesional (opcional, dependiendo la modalidad de titulación):
    </label>
    <g:select name="fechaExencionExamenProfesionalDia" from="${(1..31)}" noSelection="${['':'']}" value="${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional?tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.DAY_OF_MONTH]:""}" default="none"/>
    <select name="fechaExencionExamenProfesionalMes">
        <option value=""></option>
        <option value="1" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 0? "selected": ""):""}>Enero</option>
        <option value="2" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 1? "selected": ""):""}>Febrero</option>
        <option value="3" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 2? "selected": ""):""}>Marzo</option>
        <option value="4" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 3? "selected": ""):""}>Abril</option>
        <option value="5" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 4? "selected": ""):""}>Mayo</option>
        <option value="6" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 5? "selected": ""):""}>Junio</option>
        <option value="7" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 6? "selected": ""):""}>Julio</option>
        <option value="8" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 7? "selected": ""):""}>Agosto</option>
        <option value="9" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 8? "selected": ""):""}>Septiembre</option>
        <option value="10" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 9? "selected": ""):""}>Octubre</option>
        <option value="11" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 10? "selected": ""):""}>Noviembre</option>
        <option value="12" ${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional? (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.MONTH] == 11? "selected": ""):""}>Diciembre</option>
    </select>
    <g:select name="fechaExencionExamenProfesionalAnio" from="${(new Date()[Calendar.YEAR]..1970)}" noSelection="${['':'']}" value="${tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional?tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional[Calendar.YEAR]:""}" default="none"/>
    
</div>

<div class="fieldcontain">
    <label for="cumplioServicioSocial">
        Cumplió con servicio social:
    </label>
    Si <g:radio name="cumplioServicioSocial" value="1" checked="${tituloElectronicoInstance?.expedicion?.cumplioServicioSocial == 1}" required=""/> 
    No <g:radio name="cumplioServicioSocial" value="0" checked="${tituloElectronicoInstance?.expedicion?.cumplioServicioSocial == 0}" required=""/>
</div>

<div class="fieldcontain" >
    <label for="idFundamentoLegalServicioSocial">
        Fundamento legal del servicio social:
    </label>
    <g:select name="idFundamentoLegalServicioSocial" from="${catalogo.FundamentoLegalServicioSocial.list()}" optionKey="id" optionValue="${{it.id +' - '+it.descripcion}}" noSelection="${['':'Selecciona...']}" style="max-width:50%" required="" value="${tituloElectronicoInstance?.expedicion?.idFundamentoLegalServicioSocial}" />
</div>

<div class="fieldcontain ">
    <label for="idEntidadFederativaExpedicion">
        Entidad Federativa:
    </label>
    <g:select name="idEntidadFederativaExpedicion" from="${entidadesFederativasExpedicion}" optionKey="id" optionValue="${{it.id +' - '+it.descripcion}}" noSelection="${['':'Selecciona...']}" required="" value="${tituloElectronicoInstance?.expedicion?.idEntidadFederativa}" />
</div>

