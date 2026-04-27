<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'paquete.label', default: 'Paquete')}" />
        <title>Detalles del paquete</title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="home" controller="tituloElectronico" action="index">T&iacute;tulos electr&oacute;nicos</g:link></li>
            </ul>
            </div>
            <div id="create-tituloElectronico" class="content scaffold-create" role="main">
                <h1>Detalles del paquete</h1>    
            
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:if test="${flash.error}">
                    <div class="errors" role="status">${flash.error}</div>
                </g:if>
            
                <ol class="property-list institucionEducativa">
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label">Instituci&oacute;n educativa:</span>
                        <span class="property-value" aria-labelledby="id-label">${paqueteInstance?.institucionEducativa?.clave} - ${paqueteInstance?.institucionEducativa?.descripcion}
                    </li>
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label">Folio:</span>
                        <span class="property-value" aria-labelledby="id-label">${paqueteInstance?.folio}</span>
                    </li>
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label">T&iacute;tulos electr&oacute;nicos:</span>
                         <span class="property-value" aria-labelledby="id-label">
                            <g:if test = "${paqueteInstance?.titulosElectronicos}">
                                <g:link controller="tituloElectronico" action="titulosPaquete" params="${[cveInstitucion: paqueteInstance?.institucionEducativa?.clave, idPaquete: paqueteInstance?.id]}">Ver t&iacute;tulos electr&oacute;nicos</g:link>
                            </g:if>
                            (${paqueteInstance?.titulosElectronicos?.size()})
                         </span>
                    </li>
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label">Estatus:</span>
                         <span class="property-value" aria-labelledby="id-label">${paqueteInstance?.estatus?.descripcion}</span>
                    </li>
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label">Comprobante(s) de pago:</span>
                        <g:each in="${paqueteInstance.comprobantesPago}" var="comprobantePagoInstance">
                            <span class="property-value">
                                ${comprobantePagoInstance?.poliza} / $ ${comprobantePagoInstance?.monto}
                                <sec:ifAnyGranted roles="ROLE_RECEPTOR">
                                    -
                                    <g:link class="edit" controller="comprobantePago" action="modificarComprobante" id="${comprobantePagoInstance.id}" params="${["idPaquete": paqueteInstance.id]}">Editar</g:link>
                                    |
                                    <g:link class="delete confirm" controller="comprobantePago" action="eliminarComprobante" id="${comprobantePagoInstance.id}">Eliminar</g:link>
                                </sec:ifAnyGranted>
                            </span>
                        </g:each>
                        <sec:ifAnyGranted roles="ROLE_RECEPTOR">
                            <br/>
                            <span class="property-value">
                                <g:link class="create" controller="comprobantePago" action="capturarComprobante" params="${["idPaquete": paqueteInstance.id]}">Agregar comrpobante de pago</g:link>
                            </span>
                        </sec:ifAnyGranted>
                    </li>
                    <li class="fieldcontain">
                        <span id="descripcion-label" class="property-label">Fecha de registro:</span>
                        <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.dateCreated}"/></span>
                    </li>
                    <g:if test="${paqueteInstance?.fechaEnvioRevision}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Env&iacute;o a revisi&oacute;n:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaEnvioRevision}"/></span>
                        </li>
                    </g:if>
                    
                    <g:if test="${paqueteInstance?.fechaCotejo}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Cotejo:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaCotejo}"/></span>
                        </li>
                    </g:if>
                    
                    <g:if test="${paqueteInstance?.fechaRechazoCotejo}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Rechazo cotejo:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaRechazoCotejo}"/></span>
                        </li>
                    </g:if>
                                        
                    <g:if test="${paqueteInstance?.fechaAutenticacion}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Autenticaci&oacute;n:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaAutenticacion}"/></span>
                        </li>
                    </g:if>
                    
                    <g:if test="${paqueteInstance?.fechaRechazoAutenticacion}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Rechazo autenticaci&oacute;n:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaRechazoAutenticacion}"/></span>
                        </li>
                    </g:if>
                    
                    <g:if test="${paqueteInstance?.fechaRegistroDGP}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Registro en DGP:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaRegistroDGP}"/></span>
                        </li>
                    </g:if>
                    
                    <g:if test="${paqueteInstance?.fechaRechazoRegistroDgp}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Rechazo registro DGP:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaRechazoRegistroDgp}"/></span>
                        </li>
                    </g:if>
                    
                    <g:if test="${paqueteInstance?.fechaParaEntrega}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Listo para entregar:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaParaEntrega}"/></span>
                        </li>
                    </g:if>
                    
                    <g:if test="${paqueteInstance?.fechaEntrega}">
                        <li class="fieldcontain">
                            <span id="descripcion-label" class="property-label">Entregado:</span>
                            <span class="property-value" aria-labelledby="id-label"><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${paqueteInstance?.fechaEntrega}"/></span>
                        </li>
                    </g:if>
                    
                </ol>
                <fieldset class="buttons">
                    <sec:ifAnyGranted roles="ROLE_RECEPTOR">
                        <g:link class="edit" controller="paquete" action="modificarPaquete" id="${paqueteInstance?.id}">Modificar</g:link>
                        <g:link class="delete confirm" controller="paquete" action="eliminarPaquete" id="${paqueteInstance?.id}">Eliminar</g:link>
                    </sec:ifAnyGranted>
                    
                </fieldset>
    </body>
</html>
