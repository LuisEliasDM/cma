
<%@ page import="estructuraXml.TituloElectronico" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'tituloElectronico.label', default: 'TituloElectronico')}" />
        <title>Registro DGP</title>
    </head>
    <body>
        <a href="#list-tituloElectronico" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                <li><g:link class="list" controller="tituloElectronico" action="consultarTitulo" id="${tituloElectronicoInstance?.id}">Detalles del t&iacute;tulo</g:link></li>
                <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                    <li><g:link class="create" controller="Registro" action="cargarTituloElectronico" id="${tituloElectronicoInstance?.id}">Cargar título electrónico</g:link></li>
                </sec:ifAnyGranted>
            </ul>
            </div>
            <div id="list-tituloElectronico" class="content scaffold-list" role="main">
                <h1>Registro ante DGP del titulo electrónico:</h1>
            
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
            
            <table>
                <thead>
                    <tr>
                        <th>Estatus del envío</th>
                        <th>Estatus del proceso</th>
                        <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                            <th>Consulta del proceso</th>
                            <th>Archivo de respuesta</th>
                            
                            <g:if test="${tituloElectronicoInstance?.firmaResponsables}">
                                <g:if test="${tituloElectronicoInstance?.firmaResponsables?.firmaResponsable}">
                                    <g:if test="${tituloElectronicoInstance?.registros}">

                                        <g:if test="${tituloElectronicoInstance?.registros?.last()?.archivosResultado}">

                                            <g:if test="${tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles}">

                                                <g:if test="${tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus == 1}">
                                                    <th>Cancelación</th>
                                                </g:if>

                                            </g:if>

                                        </g:if>

                                    </g:if>
                                </g:if>
                            </g:if>
                            
                        </sec:ifAnyGranted>
                    </tr>
                </thead>
                <tbody>
                    <g:if test="${registros}">
                        <g:each var="registro" in="${registros}" status="i">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <td>
                                    <g:if test="${registro.estatusEnvio}">
                                        ${registro.codigoEstatusEnvio} - Registro transmitido correctamente
                                        <br/>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${registro?.dateCreated}"/>
                                    </g:if>
                                    <g:else>
                                        ${registro.codigoEstatusEnvio} - Error: "${registro.mensajeEstatusEnvio}"
                                    </g:else>
                                </td>
                                <td>
                                    <b>Registro:</b><br/>
                                    <g:if test="${registro.numeroLote}">
                                        Procesando con número de lote: ${registro.numeroLote}
                                    </g:if>
                                    <g:else>
                                        Error en el registro: "${registro.mensajeRegistro}"
                                    </g:else>
                                    <br/>
                                    <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_FIRMANTE">
                                        <g:link class="save" controller="ArchivoRegistro" action="descargar" id="${registro.archivoRegistro.id}">Descargar archivo transmitido</g:link>
                                        <br/>
                                    </sec:ifAnyGranted>
                                    <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${registro?.dateCreated}"/>
                                        
                                    <g:if test="${registro?.consultas?.size() > 0}">
                                        <br/><br/><b>Última consulta:</b><br/>
                                        <g:if test="${registro?.consultas?.last().estatusConsulta}">
                                            ${registro?.consultas?.last().estatusLote} - ${registro?.consultas?.last().mensajeConsulta}
                                        </g:if>
                                        <g:else>
                                            ${registro?.consultas?.last().codigoEstatusConsulta} - Error: "${registro?.consultas?.last().mensajeEstatusConsulta}"
                                        </g:else>
                                        <br/>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${registro?.consultas?.last().dateCreated}"/>
                                    </g:if>
                                        
                                    <g:if test="${registro?.archivosResultado?.size() > 0}">
                                        <br/><br/><b>Archivo de respuesta:</b><br/>
                                        <g:if test="${registro?.archivosResultado?.last().estatusDescarga}">
                                            ${registro?.archivosResultado?.last().mensajeDescarga}<br/>
                                            <br/>
                                            <g:if test="${registro?.archivosResultado?.last().detalles.size() > 0}">
                                                <g:each var="detalle" in="${registro?.archivosResultado?.last().detalles}">
                                                    ${detalle.estatus} - ${detalle.descripcion}
                                                    <br/>
                                                </g:each>
                                            </g:if>
                                            <br/>
                                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_FIRMANTE">
                                                <g:link class="save" controller="ArchivoResultado" action="descargar" id="${registro?.archivosResultado?.last().id}">Descargar archivo de respuesta</g:link>
                                            </sec:ifAnyGranted>
                                        </g:if>
                                        <g:else>
                                            Error: "${registro?.archivosResultado?.last().mensajeDescarga}"
                                        </g:else>
                                        <br/>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${registro?.archivosResultado?.last().dateCreated}"/>
                                    </g:if>
                                        
                                    <g:if test="${registro?.cancelaciones?.size() > 0}">
                                        <br/><br/><b>Cancelación:</b><br/>
                                        <g:if test="${registro?.cancelaciones?.last().estatusCancelacion}">
                                            ${registro?.cancelaciones?.last().codigoEjecucion} - ${registro?.cancelaciones?.last().mensajeEjecucion}
                                        </g:if>
                                        <g:else>
                                            ${registro?.cancelaciones?.last().codigoEstatusCancelacion} - Error: "${registro?.cancelaciones?.last().mensajeEstatusCancelacion}"
                                        </g:else>
                                        <br/>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${registro?.cancelaciones?.last().dateCreated}"/>
                                    </g:if>
                                </td>
                                <sec:ifAnyGranted roles="ROLE_FIRMANTE">
                                    <td>
                                        <g:link class="save" controller="Registro" action="consultarProcesoTituloElectronico" id="${registro.id}">Consultar proceso</g:link>
                                    </td>
                                    <td>
                                        <g:link class="save" controller="Registro" action="descargarTituloElectronico" id="${registro.id}">Consultar archivo de respuesta</g:link>
                                    </td>
                                    
                                    <g:if test="${tituloElectronicoInstance?.firmaResponsables}">
                                        <g:if test="${tituloElectronicoInstance?.firmaResponsables?.firmaResponsable}">
                                            <g:if test="${tituloElectronicoInstance?.registros}">

                                                <g:if test="${tituloElectronicoInstance?.registros?.last()?.archivosResultado}">

                                                    <g:if test="${tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles}">

                                                        <g:if test="${tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus == 1}">
                                                            <td>
                                                                <g:link class="save" controller="Registro" action="cancelacionTituloElectronico" id="${registro.id}">Cancelar título</g:link>
                                                            </td>
                                                        </g:if>

                                                    </g:if>

                                                </g:if>

                                            </g:if>
                                        </g:if>
                                    </g:if>
                                    
                                </sec:ifAnyGranted>
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                        <td colspan="5"><center>No hay registros</center></td>
                    </g:else>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${tituloElectronicoInstance?.registros?.size() ?: 0}" id="${tituloElectronicoInstance?.id}" />
            </div>
        </div>
    </body>
</html>
