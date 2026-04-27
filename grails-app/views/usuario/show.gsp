
<%@ page import="seguridad.Usuario" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'usuario.label', default: 'Usuario')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#show-usuario" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
                <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR">
                    <li><g:link class="list" controller="usuario" action="index">Listado de usuarios</g:link></li>
                </sec:ifAnyGranted>
                </ul>
            </div>
            <div id="show-usuario" class="content scaffold-show" role="main">
                <h1>Detalles del usuario</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors" role="status">${flash.error}</div>
            </g:if>
            <ol class="property-list usuario">
                <li class="fieldcontain">
                    <span class="property-label">
                        Folio de indentificaci&oacute;n
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.numeroIdentificacion}
                    </span>
                </li>
            
                <li class="fieldcontain">
                    <span class="property-label">
                        CURP
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.curp}
                    </span>
                </li>
                
                <li class="fieldcontain">
                    <span class="property-label">
                        Nombre completo
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.nombre} ${usuarioInstance?.perfil?.primerApellido} ${usuarioInstance?.perfil?.segundoApellido}
                    </span>
                </li>
                
                <li class="fieldcontain">
                    <span class="property-label">
                        Sexo
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.sexo}
                    </span>
                </li>
                
                <li class="fieldcontain">
                    <span class="property-label">
                        Fecha de nacimiento
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        <g:formatDate format="dd/MM/yyyy" date="${usuarioInstance?.perfil?.fechaNacimiento}"/>
                    </span>
                </li>
                
                <li class="fieldcontain">
                    <span class="property-label">
                        Estado de nacimiento
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.estadoNacimiento}
                    </span>
                </li>
                
                <li class="fieldcontain">
                    <span class="property-label">
                        Correo electr&oacute;nico
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.correoElectronico}
                    </span>
                </li>
                
                <li class="fieldcontain">
                    <span class="property-label">
                        Tel&eacute;fono fijo
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.telefonoFijo}
                    </span>
                </li>
                
                <li class="fieldcontain">
                    <span class="property-label">
                        Tel&eacute;fono m&oacute;vil
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.telefonoMovil}
                    </span>
                </li>
                
                <li class="fieldcontain">
                    <span class="property-label">
                        Cargo
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.perfil?.cargo}
                    </span>
                </li>

                <li class="fieldcontain">
                    <span class="property-label">
                        Nombre de usuario
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        ${usuarioInstance?.username}
                    </span>
                </li>

                <li class="fieldcontain">
                    <span id="password-label" class="property-label">
                        Roles
                    </span>

                    <span class="property-value" aria-labelledby="password-label">
                        <ul>
                            <g:each in="${usuarioInstance?.roles}" var="usuarioRol">
                                <li>
                                    ${usuarioRol.rol}
                                    <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                                        - 
                                        <g:link class="delete confirm" controller="usuario" action="eliminarRol" id="${usuarioInstance?.id}" params="${["rolInstance.id": usuarioRol.rol.id]}">Eliminar</g:link>
                                    </sec:ifAnyGranted>
                                </li>
                            </g:each>
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                                <li><g:link class="create" controller="usuarioRol" action="create" id="${usuarioInstance?.id}">Agregar rol</g:link></li>
                            </sec:ifAnyGranted>
                        </ul>
                        
                    </span>

                </li>
                
                
                <li class="fieldcontain">
                    <span id="password-label" class="property-label">
                        Instituciones
                    </span>

                    <span class="property-value" aria-labelledby="password-label">
                        <ul>
                            <g:each in="${usuarioInstance?.instituciones}" var="usuarioInstitucion">
                                <li>
                                    ${usuarioInstitucion.institucionEducativa}
                                    <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                                        -
                                        <g:link class="delete confirm" controller="usuario" action="eliminarInstitucion" id="${usuarioInstance?.id}" params="${["institucionEducativaInstance.id": usuarioInstitucion.institucionEducativa.id]}">Eliminar</g:link>
                                    </sec:ifAnyGranted>
                                </li>
                            </g:each>
                            <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                                <li><g:link class="create" controller="usuarioInstitucion" action="create" id="${usuarioInstance?.id}">Agregar instituci&oacute;n</g:link></li>
                            </sec:ifAnyGranted>
                        </ul>
                    </span>

                </li>

                

                <li class="fieldcontain">
                    <span id="enabled-label" class="property-label">Estatus</span>

                    <span class="property-value" aria-labelledby="enabled-label">
                        <g:if test="${usuarioInstance?.enabled}">
                            Habilitado
                        </g:if>
                        <g:else>
                            Inhabilitado
                        </g:else>
                        <br/>
                         <g:if test="${usuarioInstance?.perfil?.confirmado}">
                            - Confirmado - <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${usuarioInstance?.perfil?.fechaConfirmacion}"/>
                        </g:if>
                        <g:else>
                            - Confirmaci&oacute;n pendiente
                        </g:else>
                        <br/>
                        <g:if test="${usuarioInstance?.perfil?.acuseEntregado}">
                            - Acuse entregado - <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${usuarioInstance?.perfil?.fechaEntregaAcuse}"/>
                        </g:if>
                        <g:else>
                            - Acuse pendiente
                        </g:else>
                    </span>

                </li>

            </ol>
            <g:form url="[resource:usuarioInstance, action:'deshabilitar']">
                <fieldset class="buttons">
                    <sec:ifAnyGranted roles="ROLE_ADMINISTRADOR">
                        <g:link class="edit" action="edit" resource="${usuarioInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        
                        <g:if test="${usuarioInstance?.perfil?.confirmado && usuarioInstance?.perfil?.acuseEntregado}">
                            <g:if test="${usuarioInstance?.enabled}">
                                <g:actionSubmit action="inhabilitar" value="Inhabilitar"/>
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="habilitar" value="Habilitar"/>
                            </g:else>
                        </g:if>
                        
                        <g:if test="${!usuarioInstance?.perfil?.confirmado}">
                            <g:actionSubmit action="reenviarConfirmacion" value="Reenviar confirmacion"/>
                        </g:if>
                        <g:elseif test="${usuarioInstance?.perfil?.confirmado && !usuarioInstance?.perfil?.acuseEntregado}">
                            <g:actionSubmit action="registrarEntregaAcuse" value="Registrar acuse"/>
                        </g:elseif>
                    </sec:ifAnyGranted>
                    
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
