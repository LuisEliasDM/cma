<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Detalles de la e.firma</title>
    </head>
    <body>
        <a href="#create-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                    <li><g:link class="list" action="index" id="${firmaElectronicaInstance?.id}">Listado de e.firmas</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Detalles de la e.firma</h1>                                
            
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <fieldset class="form">
                <h3>Informaci&oacute;n del firmante</h3>
                <div class="fieldcontain">
                    <label>Instituci&oacute;n educativa:</label>
                    ${firmaElectronicaInstance?.institucionEducativa?.clave} - ${firmaElectronicaInstance?.institucionEducativa?.descripcion}
                </div>
                <div class="fieldcontain">
                    <label>Nombre completo:</label>
                    ${firmaElectronicaInstance?.abreviaturaTitulo?.descripcion} ${firmaElectronicaInstance?.nombre} ${firmaElectronicaInstance?.primerApellido} ${firmaElectronicaInstance?.segundoApellido}
                </div>
                <div class="fieldcontain">
                    <label>CURP:</label>
                    ${firmaElectronicaInstance?.curp}
                </div>
                <div class="fieldcontain">
                    <label>Sexo:</label>
                    ${firmaElectronicaInstance?.sexo}
                </div>
                <div class="fieldcontain">
                    <label>Fecha de nacimiento:</label>
                    <g:formatDate format="dd/MM/yyyy" date="${firmaElectronicaInstance?.fechaNacimiento}"/>
                </div>
                <div class="fieldcontain">
                    <label>Entidad de nacimiento:</label>
                    ${firmaElectronicaInstance?.estadoNacimiento}
                </div>
                <div class="fieldcontain">
                    <label>RFC:</label>
                    ${firmaElectronicaInstance?.rfcCer}
                </div>
                <div class="fieldcontain">
                    <label>Cargo:</label>
                    ${firmaElectronicaInstance?.cargoFirmante?.id} - ${firmaElectronicaInstance?.cargoFirmante?.descripcion}
                </div>
                <br/>
                
                <h3>Informaci&oacute;n del certificado</h3>
                <div class="fieldcontain">
                    <label>N&uacute;mero de serie:</label>
                    ${firmaElectronicaInstance?.numeroSerieCer}
                </div>
                <div class="fieldcontain">
                    <label>Validez:</label>
                    <g:formatDate format="dd/MM/yyyy" date="${firmaElectronicaInstance?.validoDesdeCer}"/>
                    -
                    <g:formatDate format="dd/MM/yyyy" date="${firmaElectronicaInstance?.validoHastaCer}"/>

                    <g:if test="${new Date() < firmaElectronicaInstance?.validoHastaCer}">
                        <b>(Vigente)</b>
                    </g:if>
                    <g:else>
                        <b>(Expir&oacute;)</b>
                    </g:else>
                </div>
                <div class="fieldcontain">
                    <label>Certificado:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 300px; color: #333333;" readonly="">${firmaElectronicaInstance?.contenidoCer}</textarea>
                </div>
                <div class="fieldcontain">
                    <label>Fecha de registro:</label>
                    <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${firmaElectronicaInstance?.dateCreated}"/>
                </div>
                
            </fieldset>
            <fieldset class="buttons">
                <sec:ifAnyGranted roles="ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                    <g:if test="${firmaElectronicaInstance?.curp == usuario?.perfil?.curp}">
                        <g:link class="edit" controller="firmaElectronica" action="modificarFirmaElectronica" id="${firmaElectronicaInstance?.id}">Editar</g:link>
                        <g:link class="delete confirm" controller="firmaElectronica" action="eliminarFirmaElectronica" id="${firmaElectronicaInstance?.id}">Eliminar</g:link>
                    </g:if>
                </sec:ifAnyGranted>
            </fieldset>
        </div>
    </body>
</html>
