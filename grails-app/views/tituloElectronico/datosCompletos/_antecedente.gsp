
<div class="fieldcontain">
    <label for="institucionProcedencia">
        Institución de procedencia:
    </label>
    <g:textField name="institucionProcedencia" required="" maxlength="512" value="${tituloElectronicoInstance?.antecedente?.institucionProcedencia}" style="width: 650px;"/>
</div>

<div class="fieldcontain">
    <label for="idTipoEstudioAntecedente">
        Tipo de estudios de antecedente :
    </label>
    <g:select name="idTipoEstudioAntecedente" from="${catalogo.TipoEstudioAntecedente.list()}" optionKey="id" optionValue="${{it.id +' - '+it.descripcion}}" noSelection="${['':'Selecciona...']}" required="" value="${tituloElectronicoInstance?.antecedente?.idTipoEstudioAntecedente}" />
</div>

<div class="fieldcontain  ">
    <label for="idEntidadFederativa">
        Entidad Federativa:
    </label>
    <g:select name="idEntidadFederativaAntecedente" from="${entidadesFederativasAntecedente}" optionKey="id" optionValue="${{it.id +' - '+it.descripcion}}" noSelection="${['':'Selecciona...']}" required="" value="${tituloElectronicoInstance?.antecedente?.idEntidadFederativa}" />
</div>

<div class="fieldcontain">
    <label for="fechaInicio">
        Fecha de inicio de los estudios de antecedente (opcional):
    </label>
    <g:select name="fechaInicioAntecedenteDia" from="${(1..31)}" noSelection="${['':'']}" value="${tituloElectronicoInstance?.antecedente?.fechaInicio?tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.DAY_OF_MONTH]:""}" default="none" />
    <select name="fechaInicioAntecedenteMes">
        <option value=""></option>
        <option value="1" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 0? "selected": ""):""}>Enero</option>
        <option value="2" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 1? "selected": ""):""}>Febrero</option>
        <option value="3" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 2? "selected": ""):""}>Marzo</option>
        <option value="4" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 3? "selected": ""):""}>Abril</option>
        <option value="5" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 4? "selected": ""):""}>Mayo</option>
        <option value="6" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 5? "selected": ""):""}>Junio</option>
        <option value="7" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 6? "selected": ""):""}>Julio</option>
        <option value="8" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 7? "selected": ""):""}>Agosto</option>
        <option value="9" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 8? "selected": ""):""}>Septiembre</option>
        <option value="10" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 9? "selected": ""):""}>Octubre</option>
        <option value="11" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 10? "selected": ""):""}>Noviembre</option>
        <option value="12" ${tituloElectronicoInstance?.antecedente?.fechaInicio? (tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.MONTH] == 11? "selected": ""):""}>Diciembre</option>
    </select>
    <g:select name="fechaInicioAntecedenteAnio" from="${(new Date()[Calendar.YEAR]..1970)}" noSelection="${['':'']}" value="${tituloElectronicoInstance?.antecedente?.fechaInicio?tituloElectronicoInstance?.antecedente?.fechaInicio[Calendar.YEAR]:""}" default="none" />
    
</div>

<div class="fieldcontain">
    <label for="fechaTerminacion">
         Fecha de terminación de los estudios de antecedente:
    </label>
    <g:select name="fechaTerminacionAntecedenteDia" from="${(1..31)}" noSelection="${['':'']}" required="" value="${tituloElectronicoInstance?.antecedente?.fechaTerminacion?tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.DAY_OF_MONTH]:""}" default="none" />
    <select name="fechaTerminacionAntecedenteMes" required="">
        <option value=""></option>
        <option value="1" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 0? "selected": ""):""}>Enero</option>
        <option value="2" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 1? "selected": ""):""}>Febrero</option>
        <option value="3" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 2? "selected": ""):""}>Marzo</option>
        <option value="4" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 3? "selected": ""):""}>Abril</option>
        <option value="5" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 4? "selected": ""):""}>Mayo</option>
        <option value="6" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 5? "selected": ""):""}>Junio</option>
        <option value="7" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 6? "selected": ""):""}>Julio</option>
        <option value="8" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 7? "selected": ""):""}>Agosto</option>
        <option value="9" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 8? "selected": ""):""}>Septiembre</option>
        <option value="10" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 9? "selected": ""):""}>Octubre</option>
        <option value="11" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 10? "selected": ""):""}>Noviembre</option>
        <option value="12" ${tituloElectronicoInstance?.antecedente?.fechaTerminacion? (tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.MONTH] == 11? "selected": ""):""}>Diciembre</option>
    </select>
    <g:select name="fechaTerminacionAntecedenteAnio" from="${(new Date()[Calendar.YEAR]..1970)}" noSelection="${['':'']}" required="" value="${tituloElectronicoInstance?.antecedente?.fechaTerminacion?tituloElectronicoInstance?.antecedente?.fechaTerminacion[Calendar.YEAR]:""}" default="none" />
    
</div>

<div class="fieldcontain">
    <label for="noCedula">
        Número de cedula de estudios de antecedente (opcional):
    </label>
    <g:textField name="noCedula" maxlength="20" value="${tituloElectronicoInstance?.antecedente?.noCedula}"/>

</div>