
<%@ page import="seguridad.Usuario" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
        <title>Listado de usuarios</title>
    </head>
    <body>
        <a href="#list-usuario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                    <li><g:link class="create" action="create">Capturar usuario</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="list-usuario" class="content scaffold-list" role="main">
                <h1>Listado de usuarios</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            
            <div style=" text-align: right; margin-right: 20px; margin-bottom: 20px;">
                <g:form controller="usuario" action="index" style="display: inline-block;">
                    <input type="hidden" name="referencia" value="${params?.referencia}"/>
                    <g:select name="cveInstitucion" from="${institucionesEducativas}" optionKey="clave" optionValue="${{it.clave +' - '+it.descripcion}}" noSelection="${['0':'Todos los usuarios']}" value="${session?.cveInstitucion}" onchange="this.form.submit()"/>
                </g:form>
                
                <g:form controller="usuario" action="index" style="display: inline-block;">
                    <input type="text" name="referencia" required="" placeholder="Buscar..." value="${params.referencia}" style=" width: 380px;"/>
                    <g:submitButton name="buscar" class="list" value="Buscar" />
                </g:form>
                
                <g:form controller="usuario" action="index" style="display: inline-block;">
                    <input type="hidden" name="cveInstitucion" value="0"/>
                    <g:submitButton name="xfiltros" class="list" value="Quitar filtros" />
                </g:form>
                    
            </div>
            
            <table>
                <thead>
                    <tr>
                        <th>Usuario</th>
                        <th>Instituciones</th>
                        <th>Estatus</th>
                        <th>Fecha</th>
                        <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                            <th></th>
                            <th></th>
                            <th></th>
                        </sec:ifAnyGranted>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${usuarioInstanceList}" status="i" var="usuarioInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                            <td>
                                <g:link action="show" id="${usuarioInstance.id}">${usuarioInstance?.perfil?.curp}</g:link>
                                <br/>
                                <g:link action="show" id="${usuarioInstance.id}">${usuarioInstance?.perfil?.nombreCompleto} (${usuarioInstance?.username})</g:link>
                                <br/>
                                ${usuarioInstance?.perfil?.numeroIdentificacion}
                                <br/>
                                <g:each in="${usuarioInstance?.roles}" var="usuarioRol">
                                    - ${usuarioRol.rol}<br/>
                                </g:each>
                            </td>
                            <td>
                                <ul>
                                    <g:each in="${usuarioInstance?.instituciones}" var="usuarioInstitucion">
                                        <li>${usuarioInstitucion.institucionEducativa}</li>
                                    </g:each>
                                </ul>
                            </td>
                            <td>
                                <g:if test="${usuarioInstance?.enabled}">
                                    <b>Habilitado</b>
                                </g:if>
                                <g:else>
                                    <b>Inhabilitado</b>
                                </g:else>
                                <br/>
                                <g:if test="${usuarioInstance?.perfil?.confirmado}">
                                    - Confirmado
                                </g:if>
                                <g:else>
                                    - Confirmaci&oacute;n pendiente
                                </g:else>
                                <br/>
                                <g:if test="${usuarioInstance?.perfil?.acuseEntregado}">
                                    - Acuse entregado
                                </g:if>
                                <g:else>
                                    - Acuse pendiente
                                </g:else>
                            </td>
                            <td>
                                <b>Registro: </b> <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${usuarioInstance?.perfil?.dateCreated}"/>
                                <br/>
                                <g:if test="${usuarioInstance?.perfil?.confirmado}">
                                    <b>Confirmaci&oacute;n: </b> <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${usuarioInstance?.perfil?.fechaConfirmacion}"/>
                                    <br/>
                                </g:if>
                                <g:if test="${usuarioInstance?.perfil?.acuseEntregado}">
                                    <b>Acuse: </b> <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${usuarioInstance?.perfil?.fechaEntregaAcuse}"/>
                                </g:if>
                            </td>
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                                <td><g:link action="edit" id="${usuarioInstance?.id}">Editar</g:link></td>
                                <td>
                                    <g:if test="${usuarioInstance?.enabled}">
                                        <g:link action="inhabilitar" id="${usuarioInstance?.id}">Inhabilitar</g:link>
                                    </g:if>
                                    <g:else>
                                        <g:link action="habilitar" id="${usuarioInstance?.id}">Habilitar</g:link>
                                    </g:else>
                                </td>
                                <td>
                                    <g:if test="${usuarioInstance?.enabled}">
                                        <g:if test="${!usuarioInstance?.perfil?.confirmado}">
                                            <g:link action="reenviarConfirmacion" id="${usuarioInstance?.id}">Reenviar confirmaci&oacute;n</g:link>
                                        </g:if>
                                        <g:elseif test="${usuarioInstance?.perfil?.confirmado && !usuarioInstance?.perfil?.acuseEntregado}">
                                            <g:link action="registrarEntregaAcuse" id="${usuarioInstance?.id}">Registrar acuse</g:link>
                                        </g:elseif>
                                    </g:if>
                                </td>
                            </sec:ifAnyGranted>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${usuarioInstanceCount ?: 0}" params="${[referencia: params.referencia]}"/>
            </div>
        </div>
    </body>
</html>
