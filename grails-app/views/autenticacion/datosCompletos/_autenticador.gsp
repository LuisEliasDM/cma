<div class="fieldcontain">
    <label for="firmaElectronica">
        e.firma: 
    </label>
    <g:select name="idFirmaElectronica" from="${firmasElectronicas}" optionKey="id" optionValue="${{it.abreviaturaTitulo.descripcion +' '+it.nombre+' ' + it.primerApellido + ' ' + it.segundoApellido  + ' - '+it.cargoFirmante.descripcion}}" noSelection="${['':'Selecciona...']}" required="" />
</div>

<div class="fieldcontain clavePrivada">
    <label for="clavePrivada">
        Clave privada (.key):	
    </label>
    <input type="file" name="clavePrivada" required=""/>
</div>

<div class="fieldcontain  ">
    <label for="contrasenaClavePrivada">
        Contraseña de la clave privada:		
    </label>
    <g:passwordField name="contrasenaClavePrivada" required=""  maxlength="250"/>
</div>