
<%@ page import="catalogo.InstitucionEducativa" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'institucionEducativa.label', default: 'InstitucionEducativa')}" />
        <title>Lista de instituciones educativas</title>
    </head>
    <body>
        <a href="#list-institucionEducativa" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                    <li><g:link class="create" action="create">Capturar instituci&oacute;n</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="list-institucionEducativa" class="content scaffold-list" role="main">
                <h1>Lista de instituciones educativas</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <div style=" text-align: right; margin-right: 20px; margin-bottom: 20px;">
                <g:form controller="institucionEducativa" action="index" style="display: inline-block;">
                    <input type="text" name="referencia" required="" placeholder="Buscar..." value="${params.referencia}" style=" width: 380px;"/>
                    <g:submitButton name="buscar" class="list" value="Buscar" />
                </g:form>
                    
                <g:form controller="institucionEducativa" action="index" style="display: inline-block;">
                    <g:submitButton name="xfiltros" class="list" value="Quitar filtros" />
                </g:form>
                
            </div>
            
            <table>
                <thead>
                    <tr>
                        <th>Clave</th>
                        <th>Descripci&oacute;n</th>
                        <th>Carreras</th>
                        <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                            <th></th>
                            <th></th>
                        </sec:ifAnyGranted>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${institucionEducativaInstanceList}">
                        <g:each in="${institucionEducativaInstanceList}" status="i" var="institucionEducativaInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <td><g:link action="show" id="${institucionEducativaInstance.id}">${fieldValue(bean: institucionEducativaInstance, field: "clave")}</g:link></td>
                                <td><g:link action="show" id="${institucionEducativaInstance.id}">${fieldValue(bean: institucionEducativaInstance, field: "descripcion")}</g:link></td>
                                <td>
                                    <ul>
                                        <g:each in="${institucionEducativaInstance?.carrerasInstitucion}" var="carreraInstitucion">
                                            <li>${carreraInstitucion}</li>
                                        </g:each>
                                    </ul>
                                </td>
                                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                                    <td><g:link action="edit" id="${institucionEducativaInstance?.id}">Editar</g:link></td>
                                    <td>
                                        <g:link action="delete" id="${institucionEducativaInstance?.id}" class="confirm">Eliminar</g:link>
                                    </td>
                                </sec:ifAnyGranted>
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                        <tr>
                            <td colspan="5"><center>No se encontraron instituciones educativas</center></td>
                        </tr>
                    </g:else>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${institucionEducativaInstanceCount ?: 0}" params="${[referencia: params.referencia]}"/>
            </div>
        </div>
    </body>
</html>
