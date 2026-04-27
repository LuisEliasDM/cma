<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Detalles de firma de responsable</title>
    </head>
    <body>
        <a href="#create-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="tituloElectronico" action="consultarTitulo" id="${tituloElectronicoInstance?.id}">Detalles del t&iacute;tulo</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_GESTOR">
                    <li><g:link class="list" action="index" id="${tituloElectronicoInstance?.id}">Listado de firmas de responsables</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Firma de responsable de la institución para el título: </h1>                
            
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
            
            <fieldset class="form">
                <h3>Información del firmante</h3>
                <div class="fieldcontain">
                    <label>CURP:</label>
                    ${firmaResponsableInstance?.curp}
                </div>
                <div class="fieldcontain">
                    <label>Nombre completo:</label>
                    ${firmaResponsableInstance?.nombreCompleto}
                </div>
                <div class="fieldcontain">
                    <label>Cargo:</label>
                    ${firmaResponsableInstance?.idCargo} - ${firmaResponsableInstance?.cargo}
                </div>
                <br/>
                
                <h3>Firma electrónica</h3>
                <div class="fieldcontain">
                    <label>Cadena original:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 100px; color: #333333;" readonly="">${firmaResponsableInstance?.cadenaOriginal}</textarea>
                </div>
                <div class="fieldcontain">
                    <label>Sello:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 100px; color: #333333;" readonly="">${firmaResponsableInstance?.sello}</textarea>
                </div>
                <div class="fieldcontain">
                    <label>Certificado del sello:</label>
                    <textarea style="border: none; background: none; word-wrap:break-word; display:inline-block; width: 70%; resize: none; height: 100px; color: #333333;" readonly="">${firmaResponsableInstance?.certificadoResponsable}</textarea>
                </div>
                <div class="fieldcontain">
                    <label>Número de serie del certificado:</label>
                    ${firmaResponsableInstance?.noCertificadoResponsable}
                </div>
                <div class="fieldcontain">
                    <label>Fecha de la firma:</label>
                    <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${firmaResponsableInstance?.dateCreated}"/>
                </div>
                
            </fieldset>
            <fieldset class="buttons">
                <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                    <g:link class="save confirm" controller="FirmaResponsable" action="eliminarFirma" id="${firmaResponsableInstance?.id}">Eliminar firma</g:link>
                </sec:ifAnyGranted>
            </fieldset>
        </div>
    </body>
</html>
