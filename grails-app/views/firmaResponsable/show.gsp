
<%@ page import="estructuraXml.FirmaResponsable" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'firmaResponsable.label', default: 'FirmaResponsable')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-firmaResponsable" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-firmaResponsable" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list firmaResponsable">
			
				<g:if test="${firmaResponsableInstance?.nombre}">
				<li class="fieldcontain">
					<span id="nombre-label" class="property-label"><g:message code="firmaResponsable.nombre.label" default="Nombre" /></span>
					
						<span class="property-value" aria-labelledby="nombre-label"><g:fieldValue bean="${firmaResponsableInstance}" field="nombre"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.primerApellido}">
				<li class="fieldcontain">
					<span id="primerApellido-label" class="property-label"><g:message code="firmaResponsable.primerApellido.label" default="Primer Apellido" /></span>
					
						<span class="property-value" aria-labelledby="primerApellido-label"><g:fieldValue bean="${firmaResponsableInstance}" field="primerApellido"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.segundoApellido}">
				<li class="fieldcontain">
					<span id="segundoApellido-label" class="property-label"><g:message code="firmaResponsable.segundoApellido.label" default="Segundo Apellido" /></span>
					
						<span class="property-value" aria-labelledby="segundoApellido-label"><g:fieldValue bean="${firmaResponsableInstance}" field="segundoApellido"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.curp}">
				<li class="fieldcontain">
					<span id="curp-label" class="property-label"><g:message code="firmaResponsable.curp.label" default="Curp" /></span>
					
						<span class="property-value" aria-labelledby="curp-label"><g:fieldValue bean="${firmaResponsableInstance}" field="curp"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.idCargo}">
				<li class="fieldcontain">
					<span id="idCargo-label" class="property-label"><g:message code="firmaResponsable.idCargo.label" default="Id Cargo" /></span>
					
						<span class="property-value" aria-labelledby="idCargo-label"><g:fieldValue bean="${firmaResponsableInstance}" field="idCargo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.cargo}">
				<li class="fieldcontain">
					<span id="cargo-label" class="property-label"><g:message code="firmaResponsable.cargo.label" default="Cargo" /></span>
					
						<span class="property-value" aria-labelledby="cargo-label"><g:fieldValue bean="${firmaResponsableInstance}" field="cargo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.abrTitulo}">
				<li class="fieldcontain">
					<span id="abrTitulo-label" class="property-label"><g:message code="firmaResponsable.abrTitulo.label" default="Abr Titulo" /></span>
					
						<span class="property-value" aria-labelledby="abrTitulo-label"><g:fieldValue bean="${firmaResponsableInstance}" field="abrTitulo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.sello}">
				<li class="fieldcontain">
					<span id="sello-label" class="property-label"><g:message code="firmaResponsable.sello.label" default="Sello" /></span>
					
						<span class="property-value" aria-labelledby="sello-label"><g:fieldValue bean="${firmaResponsableInstance}" field="sello"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.certificadoResponsable}">
				<li class="fieldcontain">
					<span id="certificadoResponsable-label" class="property-label"><g:message code="firmaResponsable.certificadoResponsable.label" default="Certificado Responsable" /></span>
					
						<span class="property-value" aria-labelledby="certificadoResponsable-label"><g:fieldValue bean="${firmaResponsableInstance}" field="certificadoResponsable"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${firmaResponsableInstance?.noCertificadoResponsable}">
				<li class="fieldcontain">
					<span id="noCertificadoResponsable-label" class="property-label"><g:message code="firmaResponsable.noCertificadoResponsable.label" default="No Certificado Responsable" /></span>
					
						<span class="property-value" aria-labelledby="noCertificadoResponsable-label"><g:fieldValue bean="${firmaResponsableInstance}" field="noCertificadoResponsable"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:firmaResponsableInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${firmaResponsableInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
