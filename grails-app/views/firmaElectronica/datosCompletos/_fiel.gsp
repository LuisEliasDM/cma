<sec:ifAnyGranted roles="ROLE_FIRMANTE">
    <div class="fieldcontain">
        <label for="cveInstitucion">
            Institución educativa:
        </label>
        <g:select name="idInstitucionEducativa" from="${institucionesEducativas}" optionKey="id" optionValue="${{it.clave +' - '+it.descripcion}}" noSelection="${['':'Selecciona...']}" required="" value="${firmaElectronicaInstance?.institucionEducativa?.id}" />
    </div>
</sec:ifAnyGranted>

<div class="pluginCurp"></div>

<div class="fieldcontain">
    <label for="idCargo">
        Cargo: 
    </label>
    <g:select name="idCargo" from="${catalogo.CargoFirmante.list()}" optionKey="id" optionValue="descripcion" noSelection="${['':'Selecciona...']}" required="" value="${firmaElectronicaInstance?.cargoFirmante?.id}" />
</div>

<div class="fieldcontain">
    <label for="abrTitulo">
        Abreviatura del titulo: 
    </label>
    <g:select name="idAbrTitulo" from="${catalogo.AbreviaturaTitulo.list()}" optionKey="id" optionValue="descripcion" noSelection="${['':'Selecciona...']}" required="" value="${firmaElectronicaInstance?.abreviaturaTitulo?.id}" />
</div>

<g:if test="${firmaElectronicaInstance?.id}">
    <br/>
    <div class="message">Complete los siguientes campos &uacute;nicamente si desea reemplazar la e.firma previamente cargada</div>
</g:if>
<div class="fieldcontain">
    <label for="certificadoCer">
        Certificado (.cer):		
    </label>
    <input type="file" name="certificadoCer" <g:if test="${!firmaElectronicaInstance?.id}">required=""</g:if>/>
</div>

<sec:ifAnyGranted roles="ROLE_FIRMANTE">
    <div class="fieldcontain  ">
        <label for="clavePrivada">
            Clave privada (.key):	
        </label>
        <input type="file" name="clavePrivada"/>
    </div>
</sec:ifAnyGranted>