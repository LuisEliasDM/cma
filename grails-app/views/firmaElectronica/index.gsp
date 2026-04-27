
<%@ page import="estructuraXml.TituloElectronico" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'firmaElectronica.label', default: 'FirmaElectronica')}" />
        <title>Lista de e.firmas</title>
    </head>
    <body>
        <a href="#list-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                
                <sec:ifAnyGranted roles="ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                    <li><g:link class="list" action="capturarFirmaElectronica">Capturar e.firma</g:link></li>
                </sec:ifAnyGranted>
                
            </ul>
            </div>
            <div id="list-tituloElectronico" class="content scaffold-list" role="main">
                <h1>Listado de e.firmas</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <div style=" text-align: right; margin-right: 20px; margin-bottom: 20px;">
                <g:form controller="firmaElectronica" action="index" style="display: inline-block;">
                    <input type="hidden" name="referencia" value="${params?.referencia}"/>
                    <g:select name="cveInstitucion" from="${institucionesEducativas}" optionKey="clave" optionValue="${{it.clave +' - '+it.descripcion}}" noSelection="${['0':'Todas las e.firmas']}" value="${session?.cveInstitucion}" onchange="this.form.submit()"/>
                </g:form>
                
                <g:form controller="firmaElectronica" action="index" style="display: inline-block;">
                    <input type="text" name="referencia" required="" placeholder="Buscar..." value="${params.referencia}" style=" width: 380px;"/>
                    <g:submitButton name="buscar" class="list" value="Buscar" />
                </g:form>
                
                <g:form controller="firmaElectronica" action="index" style="display: inline-block;">
                    <input type="hidden" name="cveInstitucion" value="0"/>
                    <g:submitButton name="xfiltros" class="list" value="Quitar filtros" />
                </g:form>
                    
            </div>
            
            <table>
                <thead>
                    <tr>
                        <th>Firmante</th>
                        <th>Instituci&oacute;n</th>
                        <th>Validez</th>
                        <th>Cargo</th>
                        <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_RECEPTOR">
                            <th></th>
                        </sec:ifAnyGranted>
                         <sec:ifAnyGranted roles="ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                            <th></th>
                        </sec:ifAnyGranted>
                        <sec:ifAnyGranted roles="ROLE_AUTENTICADOR,ROLE_FIRMANTE">
                            <th></th>
                        </sec:ifAnyGranted>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${firmaElectronicaInstanceList}">
                        <g:each in="${firmaElectronicaInstanceList}" status="i" var="firmaElectronicaInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <td>
                                    <g:link controller="firmaElectronica" action="consultarFirmaElectronica" id="${firmaElectronicaInstance?.id}">
                                        ${firmaElectronicaInstance?.abreviaturaTitulo?.descripcion} ${firmaElectronicaInstance?.nombre} ${firmaElectronicaInstance?.primerApellido} ${firmaElectronicaInstance?.segundoApellido}
                                    </g:link>
                                    <br/>
                                    ${firmaElectronicaInstance?.curp}<br/>
                                </td>
                                <td>
                                    ${firmaElectronicaInstance?.institucionEducativa?.clave} - ${firmaElectronicaInstance?.institucionEducativa?.descripcion}<br/>
                                </td>
                                <td>
                                    <g:formatDate format="dd/MM/yyyy" date="${firmaElectronicaInstance?.validoDesdeCer}"/>
                                    -
                                    <g:formatDate format="dd/MM/yyyy" date="${firmaElectronicaInstance?.validoHastaCer}"/>
                                    <br/>
                                    <g:if test="${new Date() < firmaElectronicaInstance?.validoHastaCer}">
                                        <b>Vigente</b>
                                    </g:if>
                                    <g:else>
                                        <b>Expir&oacute;</b>
                                    </g:else>
                                </td>
                                <td>
                                    ${firmaElectronicaInstance?.cargoFirmante?.descripcion}
                                </td>
                                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE,ROLE_RECEPTOR">
                                    <td>
                                        <g:link controller="firmaElectronica" action="descargarCer" id="${firmaElectronicaInstance?.id}" target="_blank">.cer</g:link>
                                        <g:if test="${firmaElectronicaInstance?.archivoKey}">
                                            | <g:link controller="firmaElectronica" action="descargarKey" id="${firmaElectronicaInstance?.id}" target="_blank">.key</g:link>
                                        </g:if>
                                    </td>
                                </sec:ifAnyGranted>
                                <sec:ifAnyGranted roles="ROLE_AUTENTICADOR">
                                    <g:if test="${!firmaElectronicaInstance?.institucionEducativa && firmaElectronicaInstance?.curp == usuario?.perfil?.curp}">
                                        <td><g:link controller="firmaElectronica" action="modificarFirmaElectronica" id="${firmaElectronicaInstance?.id}">Editar</g:link></td>
                                        <td><g:link controller="firmaElectronica" action="eliminarFirmaElectronica" id="${firmaElectronicaInstance?.id}" class="confirm">Eliminar</g:link></td>
                                    </g:if>
                                    <g:else>
                                        <td></td>
                                        <td></td>
                                    </g:else>
                                </sec:ifAnyGranted>
                                <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                                    <td>
                                        <g:if test="${firmaElectronicaInstance?.curp == usuario?.perfil?.curp}">
                                            <g:link controller="firmaElectronica" action="modificarFirmaElectronica" id="${firmaElectronicaInstance?.id}">Editar</g:link>
                                        </g:if>
                                    </td>
                                    
                                    <td>
                                        <g:if test="${firmaElectronicaInstance?.curp == usuario?.perfil?.curp}">
                                            <g:link controller="firmaElectronica" action="eliminarFirmaElectronica" id="${firmaElectronicaInstance?.id}" class="confirm">Eliminar</g:link>
                                        </g:if>
                                    </td>
                                </sec:ifAnyGranted>
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                        <tr>
                            <td colspan="10"><center>No se encontraron e.firmas</center></td>
                        </tr>
                    </g:else>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${firmaElectronicaInstanceCount ?: 0}" params="${[referencia: params.referencia]}" />
            </div>
        </div>
    </body>
</html>
