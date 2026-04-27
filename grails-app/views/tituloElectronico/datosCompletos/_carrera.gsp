
<div class="fieldcontain">
    <label for="cveCarrera">
        Carrera:		
    </label>
    <g:if test="${tituloElectronicoInstance.id}">
        <g:select name="cveCarrera" from="${carreras}" optionKey="id" optionValue="${{it.clave +' - '+it.descripcion + ' - RVOE: ' + it.rvoe}}" noSelection="${['':'Selecciona...']}" required="" value="${idCarrera}" default="none" />
    </g:if>
    <g:else>
        <select name="cveCarrera" required="" default="none" id="cveCarrera">
            <option value="">Selecciona una institución educativa</option>
        </select>
    </g:else>
</div>

<div class="fieldcontain  ">
    <label for="fechaInicio">
        Fecha de inicio de la carrera cursada:		
    </label>
    <g:select name="fechaInicioCarreraDia" from="${(1..31)}" noSelection="${['':'']}" required="" value="${tituloElectronicoInstance?.carrera?.fechaInicio?tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.DAY_OF_MONTH]:""}" default="none" />
    <select name="fechaInicioCarreraMes" required="">
        <option value=""></option>
        <option value="1" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 0? "selected": ""):""}>Enero</option>
        <option value="2" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 1? "selected": ""):""}>Febrero</option>
        <option value="3" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 2? "selected": ""):""}>Marzo</option>
        <option value="4" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 3? "selected": ""):""}>Abril</option>
        <option value="5" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 4? "selected": ""):""}>Mayo</option>
        <option value="6" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 5? "selected": ""):""}>Junio</option>
        <option value="7" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 6? "selected": ""):""}>Julio</option>
        <option value="8" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 7? "selected": ""):""}>Agosto</option>
        <option value="9" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 8? "selected": ""):""}>Septiembre</option>
        <option value="10" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 9? "selected": ""):""}>Octubre</option>
        <option value="11" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 10? "selected": ""):""}>Noviembre</option>
        <option value="12" ${tituloElectronicoInstance?.carrera?.fechaInicio? (tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.MONTH] == 11? "selected": ""):""}>Diciembre</option>
    </select>
    <g:select name="fechaInicioCarreraAnio" from="${(new Date()[Calendar.YEAR]..1970)}" noSelection="${['':'']}" required="" value="${tituloElectronicoInstance?.carrera?.fechaInicio?tituloElectronicoInstance?.carrera?.fechaInicio[Calendar.YEAR]:""}" default="none" />
</div>

<div class="fieldcontain ">
    <label for="fechaTerminacion">
        Fecha de terminación de la carrera cursada:		
    </label>
    <g:select name="fechaTerminacionCarreraDia" from="${(1..31)}" noSelection="${['':'']}" required="" value="${tituloElectronicoInstance?.carrera?.fechaTerminacion?tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.DAY_OF_MONTH]:""}" default="none" />
    <select name="fechaTerminacionCarreraMes" required="">
        <option value=""></option>
        <option value="1" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 0? "selected": ""):""}>Enero</option>
        <option value="2" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 1? "selected": ""):""}>Febrero</option>
        <option value="3" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 2? "selected": ""):""}>Marzo</option>
        <option value="4" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 3? "selected": ""):""}>Abril</option>
        <option value="5" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 4? "selected": ""):""}>Mayo</option>
        <option value="6" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 5? "selected": ""):""}>Junio</option>
        <option value="7" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 6? "selected": ""):""}>Julio</option>
        <option value="8" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 7? "selected": ""):""}>Agosto</option>
        <option value="9" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 8? "selected": ""):""}>Septiembre</option>
        <option value="10" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 9? "selected": ""):""}>Octubre</option>
        <option value="11" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 10? "selected": ""):""}>Noviembre</option>
        <option value="12" ${tituloElectronicoInstance?.carrera?.fechaTerminacion? (tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.MONTH] == 11? "selected": ""):""}>Diciembre</option>
    </select>
    <g:select name="fechaTerminacionCarreraAnio" from="${(new Date()[Calendar.YEAR]..1970)}" noSelection="${['':'']}" required="" value="${tituloElectronicoInstance?.carrera?.fechaTerminacion?tituloElectronicoInstance?.carrera?.fechaTerminacion[Calendar.YEAR]:""}" default="none" />
</div>
