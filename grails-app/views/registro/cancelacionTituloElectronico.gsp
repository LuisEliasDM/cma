<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Cancelación de título</title>
    </head>
    <body>
        <a href="#create-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="tituloElectronico" action="consultarTitulo" id="${tituloElectronicoInstance?.id}">Detalles del t&iacute;tulo</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_FIRMANTE">
                    <li><g:link class="list" action="index" id="${tituloElectronicoInstance?.id}">Registro DGP</g:link></li>
                </sec:ifAnyGranted>
            </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Cancelación el título: </h1>                
            
                <h4 style="margin-left: 25px; margin-top: 20px; margin-bottom: 20px;">
                    <label style=" width: 140px;">Folio de control:</label> ${tituloElectronicoInstance?.folioControl}<br/>
                    <label style=" width: 140px;">Profesionista:</label> ${tituloElectronicoInstance?.profesionista?.getNombreCompleto()} - ${tituloElectronicoInstance?.profesionista?.curp}<br/>
                    <label style=" width: 140px;">Estatus:</label> #${tituloElectronicoInstance?.getEstatus()?.id} - ${tituloElectronicoInstance?.getEstatus()?.descripcion}
                </h4>
            
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <g:hasErrors bean="${tituloElectronicoInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${tituloElectronicoInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </g:hasErrors>
            <g:form controller="Registro" action="cancelarTituloElectronico" id="${registroInstance?.id}">
                <fieldset class="form">
                    <label>A continuación, selecciona el motivo de la cancelación del título: </label><br/><br/>
                    <div class="fieldcontain">
                        <label for="abrTitulo">
                            Motivo: 
                        </label>
                        <g:select name="motivoCancelacionInstance.id" from="${catalogo.MotivoCancelacion.list()}" optionKey="id" optionValue="descripcion" noSelection="${['':'Selecciona...']}" required="" value="${motivoCancelacionInstance?.id}" />
                    </div>       
                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Cancelar titulo electrónico" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
