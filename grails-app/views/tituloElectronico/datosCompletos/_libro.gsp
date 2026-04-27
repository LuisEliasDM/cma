<div class="fieldcontain">
    <label>
        Libro:
    </label>
    <g:textField name="libro" required="" maxlength="16" value="${tituloElectronicoInstance?.libro}"/>
</div>
<div class="fieldcontain">
    <label>
        Foja:
    </label>
    <g:textField name="foja" required="" maxlength="16" value="${tituloElectronicoInstance?.foja}"/>
</div>
<div class="fieldcontain">
    <label>
        Fecha de registro:
    </label>
    <g:select name="fechaRegistroLibroDia" from="${(1..31)}" noSelection="${['':'']}" value="${tituloElectronicoInstance?.fechaRegistroLibro?tituloElectronicoInstance?.fechaRegistroLibro[Calendar.DAY_OF_MONTH]:""}" default="none" required="" />
    <select name="fechaRegistroLibroMes" required="">
        <option value=""></option>
        <option value="1" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 0? "selected": ""):""}>Enero</option>
        <option value="2" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 1? "selected": ""):""}>Febrero</option>
        <option value="3" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 2? "selected": ""):""}>Marzo</option>
        <option value="4" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 3? "selected": ""):""}>Abril</option>
        <option value="5" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 4? "selected": ""):""}>Mayo</option>
        <option value="6" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 5? "selected": ""):""}>Junio</option>
        <option value="7" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 6? "selected": ""):""}>Julio</option>
        <option value="8" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 7? "selected": ""):""}>Agosto</option>
        <option value="9" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 8? "selected": ""):""}>Septiembre</option>
        <option value="10" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 9? "selected": ""):""}>Octubre</option>
        <option value="11" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 10? "selected": ""):""}>Noviembre</option>
        <option value="12" ${tituloElectronicoInstance?.fechaRegistroLibro? (tituloElectronicoInstance?.fechaRegistroLibro[Calendar.MONTH] == 11? "selected": ""):""}>Diciembre</option>
    </select>
    <g:select name="fechaRegistroLibroAnio" from="${(new Date()[Calendar.YEAR]..1970)}" noSelection="${['':'']}" value="${tituloElectronicoInstance?.fechaRegistroLibro?tituloElectronicoInstance?.fechaRegistroLibro[Calendar.YEAR]:""}" default="none" required=""/>
    
</div>