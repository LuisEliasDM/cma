
<%@ page import="estructuraXml.FirmaElectronica" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'firmaElectronica.label', default: 'FirmaElectronica')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-firmaElectronica" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-firmaElectronica" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list firmaElectronica">
			
				<g:if test="${firmaElectronicaInstance?.usuario}">
				<li class="fieldcontain">
					<span id="usuario-label" class="property-label"><g:message code="firmaElectronica.usuario.label" default="Usuario" /></span>
					
						<span class="property-value" aria-labelledby="usuario-label"><g:link controller="usuario" action="show" id="${firmaElectronicaInstance?.usuario?.id}">${firmaElectronicaInstance?.usuario?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.archivoCer}">
				<li class="fieldcontain">
					<span id="archivoCer-label" class="property-label"><g:message code="firmaElectronica.archivoCer.label" default="Archivo Cer" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.archivoKey}">
				<li class="fieldcontain">
					<span id="archivoKey-label" class="property-label"><g:message code="firmaElectronica.archivoKey.label" default="Archivo Key" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.contenidoCer}">
				<li class="fieldcontain">
					<span id="contenidoCer-label" class="property-label"><g:message code="firmaElectronica.contenidoCer.label" default="Contenido Cer" /></span>
					
						<span class="property-value" aria-labelledby="contenidoCer-label"><g:fieldValue bean="${firmaElectronicaInstance}" field="contenidoCer"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.nombre}">
				<li class="fieldcontain">
					<span id="nombre-label" class="property-label"><g:message code="firmaElectronica.nombre.label" default="Nombre" /></span>
					
						<span class="property-value" aria-labelledby="nombre-label"><g:fieldValue bean="${firmaElectronicaInstance}" field="nombre"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.curp}">
				<li class="fieldcontain">
					<span id="curp-label" class="property-label"><g:message code="firmaElectronica.curp.label" default="Curp" /></span>
					
						<span class="property-value" aria-labelledby="curp-label"><g:fieldValue bean="${firmaElectronicaInstance}" field="curp"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.rfc}">
				<li class="fieldcontain">
					<span id="rfc-label" class="property-label"><g:message code="firmaElectronica.rfc.label" default="Rfc" /></span>
					
						<span class="property-value" aria-labelledby="rfc-label"><g:fieldValue bean="${firmaElectronicaInstance}" field="rfc"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.correoElectronico}">
				<li class="fieldcontain">
					<span id="correoElectronico-label" class="property-label"><g:message code="firmaElectronica.correoElectronico.label" default="Correo Electronico" /></span>
					
						<span class="property-value" aria-labelledby="correoElectronico-label"><g:fieldValue bean="${firmaElectronicaInstance}" field="correoElectronico"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.validoDesde}">
				<li class="fieldcontain">
					<span id="validoDesde-label" class="property-label"><g:message code="firmaElectronica.validoDesde.label" default="Valido Desde" /></span>
					
						<span class="property-value" aria-labelledby="validoDesde-label"><g:formatDate date="${firmaElectronicaInstance?.validoDesde}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.validoHasta}">
				<li class="fieldcontain">
					<span id="validoHasta-label" class="property-label"><g:message code="firmaElectronica.validoHasta.label" default="Valido Hasta" /></span>
					
						<span class="property-value" aria-labelledby="validoHasta-label"><g:formatDate date="${firmaElectronicaInstance?.validoHasta}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.numeroSerie}">
				<li class="fieldcontain">
					<span id="numeroSerie-label" class="property-label"><g:message code="firmaElectronica.numeroSerie.label" default="Numero Serie" /></span>
					
						<span class="property-value" aria-labelledby="numeroSerie-label"><g:fieldValue bean="${firmaElectronicaInstance}" field="numeroSerie"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.cargoFirmante}">
				<li class="fieldcontain">
					<span id="cargoFirmante-label" class="property-label"><g:message code="firmaElectronica.cargoFirmante.label" default="Cargo Firmante" /></span>
					
						<span class="property-value" aria-labelledby="cargoFirmante-label"><g:link controller="cargoFirmante" action="show" id="${firmaElectronicaInstance?.cargoFirmante?.id}">${firmaElectronicaInstance?.cargoFirmante?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.abreviaturaTitulo}">
				<li class="fieldcontain">
					<span id="abreviaturaTitulo-label" class="property-label"><g:message code="firmaElectronica.abreviaturaTitulo.label" default="Abreviatura Titulo" /></span>
					
						<span class="property-value" aria-labelledby="abreviaturaTitulo-label"><g:link controller="abreviaturaTitulo" action="show" id="${firmaElectronicaInstance?.abreviaturaTitulo?.id}">${firmaElectronicaInstance?.abreviaturaTitulo?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaElectronicaInstance?.activo}">
				<li class="fieldcontain">
					<span id="activo-label" class="property-label"><g:message code="firmaElectronica.activo.label" default="Activo" /></span>
					
						<span class="property-value" aria-labelledby="activo-label"><g:formatBoolean boolean="${firmaElectronicaInstance?.activo}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:firmaElectronicaInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${firmaElectronicaInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
