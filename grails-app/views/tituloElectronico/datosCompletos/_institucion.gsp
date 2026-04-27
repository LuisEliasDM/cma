
<div class="fieldcontain">
    <label for="cveInstitucion">
        Institución educativa:
    </label>
    <g:select name="cveInstitucion" from="${institucionesEducativas}" optionKey="id" optionValue="${{it.clave +' - '+it.descripcion}}" noSelection="${['':'Selecciona...']}" required="" value="${idInstitucion}" />
</div>

