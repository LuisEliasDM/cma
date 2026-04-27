
<%@ page import="estructuraXml.Autenticacion" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'autenticacion.label', default: 'Autenticacion')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-autenticacion" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-autenticacion" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list autenticacion">
			
				<g:if test="${autenticacionInstance?.folioDigital}">
				<li class="fieldcontain">
					<span id="folioDigital-label" class="property-label"><g:message code="autenticacion.folioDigital.label" default="Folio Digital" /></span>
					
						<span class="property-value" aria-labelledby="folioDigital-label"><g:fieldValue bean="${autenticacionInstance}" field="folioDigital"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${autenticacionInstance?.noCertificadoAutoridad}">
				<li class="fieldcontain">
					<span id="noCertificadoAutoridad-label" class="property-label"><g:message code="autenticacion.noCertificadoAutoridad.label" default="No Certificado Autoridad" /></span>
					
						<span class="property-value" aria-labelledby="noCertificadoAutoridad-label"><g:fieldValue bean="${autenticacionInstance}" field="noCertificadoAutoridad"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${autenticacionInstance?.fechaAutenticacion}">
				<li class="fieldcontain">
					<span id="fechaAutenticacion-label" class="property-label"><g:message code="autenticacion.fechaAutenticacion.label" default="Fecha Autenticacion" /></span>
					
						<span class="property-value" aria-labelledby="fechaAutenticacion-label"><g:formatDate date="${autenticacionInstance?.fechaAutenticacion}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:autenticacionInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${autenticacionInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
