
<%@ page import="catalogo.CarreraInstitucion" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'carreraInstitucion.label', default: 'CarreraInstitucion')}" />
		<title>Lista de carreras</title>
	</head>
	<body>
		<a href="#list-carreraInstitucion" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                                <li><g:link class="list" controller="tituloElectronico" action="index">Listado de titulos electronicos</g:link></li>
				<li><g:link class="create" action="create">Capturar carrera</g:link></li>
			</ul>
		</div>
		<div id="list-carreraInstitucion" class="content scaffold-list" role="main">
			<h1>Lista de carreras</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
                                                <g:sortableColumn property="id" title="${message(code: 'carreraInstitucion.id.label', default: 'Id')}" />
					
						<g:sortableColumn property="descripcion" title="${message(code: 'carreraInstitucion.descripcion.label', default: 'Descripcion')}" />
					
						<th><g:message code="carreraInstitucion.institucionEducativa.label" default="Institucion Educativa" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${carreraInstitucionInstanceList}" status="i" var="carreraInstitucionInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
                                                <td><g:link action="show" id="${carreraInstitucionInstance.id}">${fieldValue(bean: carreraInstitucionInstance, field: "id")}</g:link></td>
                                            
						<td>${fieldValue(bean: carreraInstitucionInstance, field: "descripcion")}</td>
					
                                                <td><g:link controller="institucionEducativa" action="show" id="${carreraInstitucionInstance.institucionEducativa.id}">${fieldValue(bean: carreraInstitucionInstance, field: "institucionEducativa")}</g:link></td>
                                                					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${carreraInstitucionInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
