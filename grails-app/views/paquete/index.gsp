
<%@ page import="paquete.Paquete" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'paquete.label', default: 'Paquete')}" />
        <title>Lista de paquetes de titulos electrónicos</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                
                <li><g:link class="home" controller="tituloElectronico">Titulos electr&oacute;nicos</g:link></li>
                
                <sec:ifAnyGranted roles="ROLE_RECEPTOR">
                    <li><g:link class="create" action="capturarPaquete">Capturar paquete</g:link></li>
                </sec:ifAnyGranted>
                
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_FIRMANTE">
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
            <div id="list-paquete" class="content scaffold-list" role="main">
                <h1>Listado de t&iacute;tulos electr&oacute;nicos por paquete</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <div style=" text-align: right; margin-right: 20px; margin-bottom: 20px;">
                <g:form controller="paquete" action="index" style="display: inline-block;">
                    <input type="hidden" name="referencia" value="${params?.referencia}"/>
                    <g:select name="cveInstitucion" from="${institucionesEducativas}" optionKey="clave" optionValue="${{it.clave +' - '+it.descripcion}}" noSelection="${['0':'Todas las instituciones']}" value="${session?.cveInstitucion}" onchange="this.form.submit()"/>
                </g:form>
                
                <g:form controller="paquete" action="index" style="display: inline-block;">
                    <input type="text" name="referencia" required="" placeholder="Buscar..." value="${params.referencia}" style=" width: 380px;"/>
                    <g:submitButton name="buscar" class="list" value="Buscar" />
                </g:form>
                    
                <g:form controller="paquete" action="index" style="display: inline-block;">
                    <g:submitButton name="xfiltros" class="list" value="Cancelar busqueda" />
                </g:form>
            </div>
            
            <div style=" text-align: left; margin-left: 20px; margin-bottom: 20px;">
                <g:form controller="paquete" action="index" style="display: inline-block;">
                    <input type="hidden" name="referencia" value="${params?.referencia}"/>
                    <label>
                        | <input type="radio" name="idEstatusPaquete" value="0" onclick="this.form.submit()" <g:if test="${session.idEstatusPaquete == "0" || !session.idEstatusPaquete}">checked=""</g:if>/> Todos (${conteoTodos[0]}) | 
                    </label>

                    <g:each in="${listaEstatusPaquetes}" var="estatusPaquete">
                        <g:if test="${estatusPaquete[0] in [1]}">
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_RECEPTOR">
                                <label>
                                    <input type="radio" name="idEstatusPaquete" value="${estatusPaquete[0]}" onclick="this.form.submit()" <g:if test="${session.idEstatusPaquete == "${estatusPaquete[0]}"}">checked=""</g:if>/> ${estatusPaquete[1]} (${estatusPaquete[2]}) | 
                                </label>
                            </sec:ifAnyGranted>
                        </g:if>
                        
                        <g:if test="${estatusPaquete[0] in [2,3]}">
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_COTEJADOR,ROLE_REVISOR">
                                
                                <label>
                                    <input type="radio" name="idEstatusPaquete" value="${estatusPaquete[0]}" onclick="this.form.submit()" <g:if test="${session.idEstatusPaquete == "${estatusPaquete[0]}"}">checked=""</g:if>/> ${estatusPaquete[1]} (${estatusPaquete[2]}) | 
                                </label>
                                
                            </sec:ifAnyGranted>
                        </g:if>
                        
                        <g:if test="${estatusPaquete[0] in [4]}">
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR">
                                
                                <label>
                                    <input type="radio" name="idEstatusPaquete" value="${estatusPaquete[0]}" onclick="this.form.submit()" <g:if test="${session.idEstatusPaquete == "${estatusPaquete[0]}"}">checked=""</g:if>/> ${estatusPaquete[1]} (${estatusPaquete[2]}) | 
                                </label>
                                
                            </sec:ifAnyGranted>
                        </g:if>
                        
                        <g:if test="${estatusPaquete[0] in [5,6]}">
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR">
                                
                                <label>
                                    <input type="radio" name="idEstatusPaquete" value="${estatusPaquete[0]}" onclick="this.form.submit()" <g:if test="${session.idEstatusPaquete == "${estatusPaquete[0]}"}">checked=""</g:if>/> ${estatusPaquete[1]} (${estatusPaquete[2]}) | 
                                </label>
                                
                            </sec:ifAnyGranted>
                        </g:if>
                        
                        <g:if test="${estatusPaquete[0] in [7]}">
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR">
                                
                                <label>
                                    <input type="radio" name="idEstatusPaquete" value="${estatusPaquete[0]}" onclick="this.form.submit()" <g:if test="${session.idEstatusPaquete == "${estatusPaquete[0]}"}">checked=""</g:if>/> ${estatusPaquete[1]} (${estatusPaquete[2]}) | 
                                </label>
                                
                            </sec:ifAnyGranted>
                        </g:if>
                        
                        <g:if test="${estatusPaquete[0] in [8,9]}">
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_RECEPTOR">
                                
                                <label>
                                    <input type="radio" name="idEstatusPaquete" value="${estatusPaquete[0]}" onclick="this.form.submit()" <g:if test="${session.idEstatusPaquete == "${estatusPaquete[0]}"}">checked=""</g:if>/> ${estatusPaquete[1]} (${estatusPaquete[2]}) | 
                                </label>
                                
                            </sec:ifAnyGranted>
                        </g:if>
                        
                    </g:each>

                </g:form>
            </div>
            
            <table>
                <thead>
                    <tr>
                        <th>Folio</th>
                        <th>Instituci&oacute;n</th>
                        <th>T&iacute;tulos electr&oacute;nicos</th>
                        <th>Estatus</th>
                        <th>Fecha y hora</th>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="odd">
                        <td>Todos los t&iacute;tulos</td>
                        <td></td>
                        <td>
                            <g:link controller="tituloElectronico" action="titulosPaquete" params="${[cveInstitucion: session.cveInstitucion?:0, idPaquete: "0", referencia: params.referencia]}">Ver t&iacute;tulos electr&oacute;nicos</g:link>
                            (${totalTitulos})
                        </td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <g:if test="${paqueteInstanceList}">
                        <g:each in="${paqueteInstanceList}" status="i" var="paqueteInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                <td>
                                    <g:link controller="paquete" action="consultarPaquete" id="${paqueteInstance?.id}">
                                        ${paqueteInstance?.folio}
                                    </g:link>
                                </td>
                                <td>
                                    ${paqueteInstance?.institucionEducativa?.clave} - ${paqueteInstance?.institucionEducativa?.descripcion}
                                </td>
                                <td>
                                    <g:if test = "${paqueteInstance?.titulosElectronicos}">
                                        <g:link controller="tituloElectronico" action="titulosPaquete" params="${[cveInstitucion: paqueteInstance?.institucionEducativa?.clave, idPaquete: paqueteInstance?.id]}">Ver t&iacute;tulos electr&oacute;nicos</g:link>
                                    </g:if>
                                    (${paqueteInstance?.titulosElectronicos?.size()})
                                </td>
                                <td>
                                    ${paqueteInstance?.estatus?.descripcion}.
                                    <g:if test="${paqueteInstance?.motivoRechazoAutenticacion}">
                                        Rechazado desde la autenticaci&oacute;n
                                    </g:if>
                                    <g:if test="${paqueteInstance?.motivoRechazoRegistroDgp}">
                                        Rechazado desde el registro ante la DGP
                                    </g:if>
                                    <g:if test="${paqueteInstance?.motivoAnulacionRechazoCotejo}">
                                        Rechazo anulado desde la autenticaci&oacute;n
                                    </g:if>
                                    <g:elseif test="${paqueteInstance?.motivoRechazoCotejo}">
                                        Rechazado desde el cotejo
                                    </g:elseif>
                                </td>
                                <td>
                                    <b>Registro</b>
                                    <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.dateCreated}"/><br/>
                                    <g:if test="${paqueteInstance?.fechaEnvioRevision}">
                                        <b>Env&iacute;o a revisi&oacute;n:</b>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaEnvioRevision}"/><br/>
                                    </g:if>

                                    <g:if test="${paqueteInstance?.fechaRevision}">
                                        <b>Revisi&oacute;n:</b>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaRevision}"/><br/>
                                    </g:if>
                                    
                                    <g:if test="${paqueteInstance?.fechaCotejo}">
                                        <b>Cotejo:</b>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaCotejo}"/><br/>
                                    </g:if>

                                    <g:if test="${paqueteInstance?.fechaRechazoCotejo}">
                                        <b>Rechazo cotejo:</b>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaRechazoCotejo}"/><br/>
                                    </g:if>
                                        
                                    <g:if test="${paqueteInstance?.fechaAutenticacion}">
                                        <b>Autenticaci&oacute;n:</b>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaAutenticacion}"/><br/>
                                    </g:if>

                                    <g:if test="${paqueteInstance?.fechaRegistroDGP}">
                                        <b>Registro en DGP:</b>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaRegistroDGP}"/><br/>
                                    </g:if>

                                    <g:if test="${paqueteInstance?.fechaParaEntrega}">
                                        <b>Listo para entregar:</b>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaParaEntrega}"/><br/>
                                    </g:if>

                                    <g:if test="${paqueteInstance?.fechaEntrega}">
                                        <b>Entregado:</b>
                                        <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaEntrega}"/>
                                    </g:if>
                                </td>
                                
                                <td>
                                    <sec:ifAnyGranted roles="ROLE_RECEPTOR">
                                        <g:if test="${paqueteInstance?.estatus?.id == 1}">
                                            <g:link controller="paquete" action="modificarPaquete" id="${paqueteInstance?.id}">Editar</g:link>
                                        </g:if>
                                    </sec:ifAnyGranted>
                                </td>
                                
                                <td>
                                    <sec:ifAnyGranted roles="ROLE_RECEPTOR">
                                        <g:if test="${paqueteInstance?.estatus?.id == 1}">
                                            <g:link controller="paquete" action="eliminarPaquete" id="${paqueteInstance?.id}" class="confirm">Eliminar</g:link>
                                        </g:if>
                                    </sec:ifAnyGranted>
                                </td>
                                
                                <td>
                                    <sec:ifAnyGranted roles="ROLE_RECEPTOR">
                                        <g:if test="${paqueteInstance?.estatus?.id == 1}">
                                            <g:link controller="paquete" action="enviarRevision" id="${paqueteInstance?.id}" class="confirm">Enviar a revisi&oacute;n</g:link>
                                        </g:if>
                                        
                                        <g:if test="${paqueteInstance?.estatus?.id == 8}">
                                            <g:link controller="paquete" action="entregar" id="${paqueteInstance?.id}" class="confirm">Entregar</g:link>
                                        </g:if>
                                    </sec:ifAnyGranted>
                                    
                                    <sec:ifAnyGranted roles="ROLE_REVISOR">
                                        <g:if test="${paqueteInstance?.estatus?.id == 2}">
                                            <g:link controller="paquete" action="finalizarRevision" id="${paqueteInstance?.id}" class="confirm">Finalizar revisi&oacute;n</g:link>
                                        </g:if>
                                    </sec:ifAnyGranted>
                                    
                                    <sec:ifAnyGranted roles="ROLE_COTEJADOR">
                                        <g:if test="${paqueteInstance?.estatus?.id == 3}">
                                            <g:link controller="paquete" action="finalizarCotejo" id="${paqueteInstance?.id}" class="confirm">Finalizar cotejo</g:link>
                                        </g:if>
                                    </sec:ifAnyGranted>
                                    
                                    <sec:ifAnyGranted roles="ROLE_AUTENTICADOR">
                                        <g:if test="${paqueteInstance?.estatus?.id == 5}">
                                            <g:link controller="paquete" action="finalizarAutenticacion" id="${paqueteInstance?.id}" class="confirm">Finalizar autenticaci&oacute;n</g:link>
                                        </g:if>
                                        <g:elseif test="${paqueteInstance?.estatus?.id == 6}">
                                            <g:link controller="paquete" action="finalizarRegistroDgp" id="${paqueteInstance?.id}" class="confirm">Finalizar registro (DGP)</g:link>
                                        </g:elseif>
                                    </sec:ifAnyGranted>
                                    
                                    <sec:ifAnyGranted roles="ROLE_COTEJADOR,ROLE_REVISOR">
                                        <g:if test="${paqueteInstance?.estatus?.id in [4,7]}">
                                            <g:link controller="paquete" action="enviarEntrega" id="${paqueteInstance?.id}" class="confirm">Enviar a entrega</g:link>
                                        </g:if>
                                    </sec:ifAnyGranted>
                                </td>
                                
                                <td>
                                    
                                    <sec:ifAnyGranted roles="ROLE_COTEJADOR">
                                        <g:if test="${paqueteInstance?.estatus?.id in [2,3]}">
                                            <g:link controller="paquete" action="rechazoCotejo" id="${paqueteInstance?.id}" class="confirm">Rechazar paquete</g:link>
                                        </g:if>
                                    </sec:ifAnyGranted>
                                    
                                    <sec:ifAnyGranted roles="ROLE_AUTENTICADOR">
                                        <g:if test="${paqueteInstance?.estatus?.id in [4]}">
                                            <g:link controller="paquete" action="anulacionRechazoCotejo" id="${paqueteInstance?.id}" class="confirm">Anular rechazo</g:link>
                                        </g:if>
                                        <g:if test="${paqueteInstance?.estatus?.id in [5]}">
                                            <g:link controller="paquete" action="rechazoAutenticacion" id="${paqueteInstance?.id}" class="confirm">Rechazar paquete</g:link>
                                        </g:if>
                                        <g:elseif test="${paqueteInstance?.estatus?.id in [6]}">
                                            <g:link controller="paquete" action="rechazoRegistroDgp" id="${paqueteInstance?.id}" class="confirm">Rechazar paquete</g:link>
                                        </g:elseif>
                                    </sec:ifAnyGranted>
                                    
                                </td>
                                
                            </tr>
                        </g:each>
                    </g:if>
                    <g:else>
                        <tr>
                            <td colspan="10"><center>No se encontraron paquetes</center></td>
                        </tr>
                    </g:else>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${paqueteInstanceCount ?: 0}" params="${[referencia: params.referencia, cveInstitucion: session.cveInstitucion]}" />
            </div>
        </div>
    </body>
</html>
