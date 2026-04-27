<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'paquete.label', default: 'Paquete')}" />
        <title>Asignaci&oacute;n de paquete</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
            </ul>
        </div>
        <div id="create-paquete" class="content scaffold-create" role="main">
            <h1>Asignaci&oacute;n de paquete</h1>                
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:form controller="paquete" action="asignarPaquete">
                <fieldset class="form">
                    <div class="fieldcontain ${hasErrors(bean: paqueteInstance, field: 'institucionEducativa', 'error')} required">
                        <label for="tituloElectronico">
                            T&iacute;tulo electr&oacute;nico
                        </label>
                        ${tituloElectronicoInstance?.profesionista?.curp}
                        <input type="hidden" name="tituloElectronicoInstance.id" value="${tituloElectronicoInstance?.id}"/>
                    </div>
                    
                    <div class="fieldcontain ${hasErrors(bean: paqueteInstance, field: 'institucionEducativa', 'error')} required">
                        <label for="tituloElectronico"></label>
                        ${tituloElectronicoInstance?.profesionista?.getNombreCompleto()}
                    </div>
                    
                    <div class="fieldcontain ${hasErrors(bean: paqueteInstance, field: 'institucionEducativa', 'error')} required">
                        <label for="tituloElectronico"></label>
                        ${tituloElectronicoInstance?.folioControl}
                    </div>
                    
                    <div class="fieldcontain ${hasErrors(bean: paqueteInstance, field: 'institucionEducativa', 'error')} required">
                        <label for="tituloElectronico"></label>
                        ${tituloElectronicoInstance?.institucion?.cveInstitucion} - ${tituloElectronicoInstance?.institucion?.nombreInstitucion}
                    </div>

                    <div class="fieldcontain ${hasErrors(bean: paqueteInstance, field: 'folio', 'error')} required">
                        <label for="id">
                            Paquete
                        </label>
                        <g:select name="idPaquete" from="${paquetesInstitucion}" optionKey="id" noSelection="${['':'Ninguno...']}" value="${tituloElectronicoInstance?.paquete?.id}"/>
                    </div>  
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Asignar paquete" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
