
<%@ page import="estructuraXml.TituloElectronico" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Lista de titulos electrónicos</title>
    </head>
    <body>
        <a href="#list-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                
                <sec:ifAnyGranted roles="ROLE_GESTOR">
                    <li><g:link class="create" action="capturarTitulo">Capturar t&iacute;tulo electr&oacute;nico</g:link></li>
                </sec:ifAnyGranted>
                
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_RECEPTOR">
                    <li><g:link class="list" controller="firmaElectronica">e.firma</g:link></li>
                </sec:ifAnyGranted>
                
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR">
                    <li><g:link class="list" controller="institucionEducativa">Instituciones educativas</g:link></li>
                </sec:ifAnyGranted>
                
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR">
                    <li><g:link class="list" controller="usuario">Usuarios</g:link></li>
                </sec:ifAnyGranted>
                
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_SOPORTE_TECNICO">
                    <li><g:link class="list" controller="bitacora">Bit&aacute;cora</g:link></li>
                </sec:ifAnyGranted>
                
            </ul>
            </div>
            <div id="list-tituloElectronico" class="content scaffold-list" role="main">
                <h1>Listado de titulos electronicos almacenados</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <div style=" text-align: right; margin-right: 20px; margin-bottom: 20px;">
                <g:form controller="tituloElectronico" action="titulosPaquete" style="display: inline-block;">
                    <input type="hidden" name="referencia" value="${params?.referencia}"/>
                    <g:select name="cveInstitucion" from="${institucionesEducativas}" optionKey="clave" optionValue="${{it.clave +' - '+it.descripcion}}" noSelection="${['0':'Todas las instituciones']}" value="${session?.cveInstitucion}" onchange="this.form.submit()"/>
                </g:form>
                <br/>
                <g:form controller="tituloElectronico" action="titulosPaquete" style="display: inline-block;">
                    <input type="text" name="referencia" required="" placeholder="Buscar..." value="${params.referencia}" style=" width: 380px;"/>
                    <g:submitButton name="buscar" class="list" value="Buscar" />
                </g:form>
                    
                <g:form controller="tituloElectronico" action="titulosPaquete" style="display: inline-block;">
                    <g:submitButton name="xfiltros" class="list" value="Cancelar busqueda" />
                </g:form>
            </div>
            
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Profesionista</th>
                        <th>Instituci&oacute;n</th>
                        <th>Estatus</th>
                        <th>Fecha y hora</th>
                        <sec:ifAnyGranted roles="ROLE_GESTOR">
                            <th></th>
                            <th></th>
                        </sec:ifAnyGranted>
                        <th></th>
                        <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_GESTOR">
                            <th></th>
                        </sec:ifAnyGranted>
                        <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                            <th></th>
                        </sec:ifAnyGranted>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${tituloElectronicoInstanceList}">
                        <g:each in="${tituloElectronicoInstanceList}" status="i" var="tituloElectronicoInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <td>${(params.int("offset")?:0) + (i + 1)}</td>
                                <td>
                                    ${tituloElectronicoInstance?.profesionista?.getNombreCompleto()}
                                    <br/>
                                    ${tituloElectronicoInstance?.profesionista?.curp}
                                    <br/>
                                    <g:link controller="tituloElectronico" action="consultarTitulo" id="${tituloElectronicoInstance?.id}">Ver t&iacute;tulo electr&oacute;nico</g:link>
                                </td>
                                <td>
                                    ${tituloElectronicoInstance?.institucion?.cveInstitucion} - ${tituloElectronicoInstance?.institucion?.nombreInstitucion}<br/>
                                    ${tituloElectronicoInstance?.carrera?.cveCarrera} - ${tituloElectronicoInstance?.carrera?.nombreCarrera}
                                </td>
                                <td>#${tituloElectronicoInstance?.getEstatus()?.id} - ${tituloElectronicoInstance?.getEstatus()?.descripcion}</td>
                                <td><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${tituloElectronicoInstance?.dateCreated}"/></td>
                                <sec:ifAnyGranted roles="ROLE_GESTOR">
                                    <td><g:link controller="TituloElectronico" action="modificarTitulo" id="${tituloElectronicoInstance?.id}">Editar</g:link></td>
                                    <td><g:link controller="TituloElectronico" action="eliminarTitulo" id="${tituloElectronicoInstance?.id}" class="confirm">Eliminar</g:link></td>
                                </sec:ifAnyGranted>
                                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                                    <td>
                                        <g:if test="${tituloElectronicoInstance?.getEstatus().id in [18,21,22,109]}">
                                            <g:link controller="TituloElectronico" action="xml" id="${tituloElectronicoInstance?.id}" target="_blank">XML</g:link>
                                            <g:link controller="TituloElectronico" action="descargarRepresentacionGrafica" id="${tituloElectronicoInstance?.id}" target="_blank">Representaci&oacute;n<br/>gr&aacute;fica</g:link>
                                        </g:if>
                                    </td>
                                </sec:ifAnyGranted>
                                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_GESTOR">
                                    <td><g:link controller="FirmaResponsable" action="index" id="${tituloElectronicoInstance?.id}">Firmas</g:link></td>
                                </sec:ifAnyGranted>
                                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                                    <td><g:link controller="Registro" action="index" id="${tituloElectronicoInstance?.id}">Registro DGP</g:link></td>
                                </sec:ifAnyGranted>
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                        <tr>
                            <td colspan="10"><center>No se encontraron t&iacute;tulos electr&oacute;nicos</center></td>
                        </tr>
                    </g:else>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${tituloElectronicoInstanceCount ?: 0}" params="${[referencia: params.referencia, cveInstitucion: session.cveInstitucion]}" />
            </div>
        </div>
    </body>
</html>
