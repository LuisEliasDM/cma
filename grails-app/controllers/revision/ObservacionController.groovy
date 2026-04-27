package revision

import org.mirzsoft.grails.actionlogging.annotation.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*
import estructuraXml.TituloElectronico
import grails.plugin.springsecurity.SpringSecurityUtils

@SpringUserIdentification
@Transactional(readOnly = true)
class ObservacionController {
    def actionLoggingService
    def springSecurityService
    def tituloElectronicoService
    
    def index(TituloElectronico tituloElectronicoInstance) {
        respond tituloElectronicoInstance
    }
    
    def consultarObservacion(Observacion observacionInstance) {
        [
            observacionInstance: observacionInstance,
            tituloElectronicoInstance: observacionInstance?.tituloElectronico
        ]
    }
    
    def capturarObservacion(TituloElectronico tituloElectronicoInstance){
        
        if (!tituloElectronicoInstance) {
            flash.error="Seleccione un titulo"
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_REVISOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 2) {
                flash.error = "El paquete no se encuentra en EN REVISION"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
                flash.error = "El paquete no se encuentra en EN COTEJO"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE(c.dateCreated) + c.dias", [te: tituloElectronicoInstance])
        
        if (correccionVigente) {
            flash.error = "El título electrónico ya se encuentra en proceso de corrección"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
                
        respond tituloElectronicoInstance, model: [observacionInstance: new Observacion()]
    }
    
    @ActionLogging
    @ActionType("Revisión de títulos electrónicos")
    @CustomActionName("Registro de observación")
    @PrintCustomLog
    @Transactional
    def guardarInformacion(TituloElectronico tituloElectronicoInstance) {
        actionLoggingService.log("== Registro de observación - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico a observar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!tituloElectronicoInstance) {
            flash.error="Seleccione un titulo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_REVISOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 2) {
                flash.error = "El paquete no se encuentra en EN REVISION"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
                flash.error = "El paquete no se encuentra en EN COTEJO"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE(c.dateCreated) + c.dias", [te: tituloElectronicoInstance])
        
        if (correccionVigente) {
            flash.error = "El título electrónico ya se encuentra en proceso de corrección"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Estableciendo información de la observación...")
        
        def observacionInstance = new Observacion()
        
        observacionInstance?.usuario = usuario
        observacionInstance?.tituloElectronico = tituloElectronicoInstance
        observacionInstance?.descripcion = params?.descripcion?.trim()
        observacionInstance?.estatus = 1
        
        /*tituloElectronicoInstance?.libro=null
        tituloElectronicoInstance?.foja=null
        tituloElectronicoInstance?.fechaRegistroLibro=null*/
                        
        if (observacionInstance.hasErrors()) {
            flash.error = observacionInstance?.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarObservacion", id: tituloElectronicoInstance?.id
            
            return
        }
        
        actionLoggingService.log("Observación: ${observacionInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando observación...")
        
        if(observacionInstance.save(flush:true)) {
            flash.message="Observación registrada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect(controller:"tituloElectronico", action:"consultarTitulo", id: tituloElectronicoInstance?.id)
        } else {
            flash.error= observacionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller:"tituloElectronico", action: "capturarObservacion", id: tituloElectronicoInstance?.id
            
            return
        }
    }
    
    def modificarObservacion(Observacion observacionInstance) {
        
        if (!observacionInstance) {
            flash.error="Seleccione una observación"
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        def tituloElectronicoInstance = observacionInstance?.tituloElectronico
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_REVISOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 2) {
                flash.error = "El paquete no se encuentra en EN REVISION"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
            
            if (observacionInstance?.usuario?.id != usuario?.id) {
                flash.error = "La observación fue realizada por ${observacionInstance?.usuario?.username}"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
            
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
                flash.error = "El paquete no se encuentra en EN COTEJO"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE(c.dateCreated) + c.dias", [te: tituloElectronicoInstance])
        
        if (correccionVigente) {
            flash.error = "El título electrónico ya se encuentra en proceso de corrección"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        } else {
            def ultimaCorreccionFinalizada = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) > (DATE(c.dateCreated) + c.dias) ORDER BY c.id ASC", [te: tituloElectronicoInstance], [max: 1])
            if (ultimaCorreccionFinalizada) {
                def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "cambioObservacionTitulo")
                if (permiso?.permitido && (observacionInstance?.dateCreated < ultimaCorreccionFinalizada[0]?.dateCreated)) {
                    flash.error = "El título electrónico no cuenta con la(s) firma(s) electrónica(s) de la(s) autoridad(es) de la institución educativa"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                    return
                }
            }
        }
       
        respond observacionInstance, model: [tituloElectronicoInstance: observacionInstance?.tituloElectronico]
    }
    
    @ActionLogging
    @ActionType("Revisión de títulos electrónicos")
    @CustomActionName("Modificación de observación")
    @PrintCustomLog
    @Transactional
    def actualizarRegistro() {
        actionLoggingService.log("== Modificación de observación - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando observación a modificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def observacionInstance = Observacion.get(params?.id)
        
        if (!observacionInstance) {
            flash.error="Seleccione una observación a modificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Observación (original): ${observacionInstance?.toPlainText("\t")}")        
                
        actionLoggingService.log("Consultando título electrónico...")
        
        actionLoggingService.log("Título electrónico: ${observacionInstance?.tituloElectronico?.toPlainText("\t")}")
        
        def tituloElectronicoInstance = observacionInstance?.tituloElectronico
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_REVISOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 2) {
                flash.error = "El paquete no se encuentra en EN REVISION"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
            
            if (observacionInstance?.usuario?.id != usuario?.id) {
                flash.error = "La observación fue realizada por ${observacionInstance?.usuario?.username}"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
            
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
                flash.error = "El paquete no se encuentra en EN COTEJO"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE(c.dateCreated) + c.dias", [te: tituloElectronicoInstance])
        
        if (correccionVigente) {
            flash.error = "El título electrónico ya se encuentra en proceso de corrección"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        } else {
            def ultimaCorreccionFinalizada = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) > (DATE(c.dateCreated) + c.dias) ORDER BY c.id ASC", [te: tituloElectronicoInstance], [max: 1])
            if (ultimaCorreccionFinalizada) {
                def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "cambioObservacionTitulo")
                if (permiso?.permitido && (observacionInstance?.dateCreated < ultimaCorreccionFinalizada[0]?.dateCreated)) {
                    flash.error = "El título electrónico no cuenta con la(s) firma(s) electrónica(s) de la(s) autoridad(es) de la institución educativa"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                    return
                }
            }
        }
        
        actionLoggingService.log("Estableciendo información de la observación...")
                
        observacionInstance?.descripcion = params?.descripcion?.trim()
                        
        if (observacionInstance.hasErrors()) {
            flash.error = observacionInstance?.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarObservacion", id: tituloElectronicoInstance?.id
            
            return
        }
        
        actionLoggingService.log("Observación (actualizado): ${observacionInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando observación...")
        
        if(observacionInstance.save(flush:true)) {
            flash.message="Observación modificada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect(controller:"tituloElectronico", action:"consultarTitulo", id: tituloElectronicoInstance?.id)
        } else {
            flash.error= observacionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarObservacion", id: tituloElectronicoInstance?.id
            
            return
        }
    }
    
    @ActionLogging
    @ActionType("Revisión de títulos electrónicos")
    @CustomActionName("Eliminación de observación")
    @PrintCustomLog
    @Transactional
    def eliminarObservacion(Observacion observacionInstance) {
        actionLoggingService.log("== Eliminación de Observación - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando observación a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!observacionInstance) {
            flash.error="Observación no encontrada"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Observación: ${observacionInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico...")
                
        def tituloElectronicoInstance = observacionInstance?.tituloElectronico
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_REVISOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 2) {
                flash.error = "El paquete no se encuentra en EN REVISION"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
            
            if (observacionInstance?.usuario?.id != usuario?.id) {
                flash.error = "La observación fue realizada por ${observacionInstance?.usuario?.username}"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
                flash.error = "El paquete no se encuentra en EN COTEJO"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        }        
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE(c.dateCreated) + c.dias", [te: tituloElectronicoInstance])
        
        if (correccionVigente) {
            flash.error = "El título electrónico ya se encuentra en proceso de corrección"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        } else {
            def ultimaCorreccionFinalizada = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) > (DATE(c.dateCreated) + c.dias) ORDER BY c.id ASC", [te: tituloElectronicoInstance], [max: 1])
            if (ultimaCorreccionFinalizada) {
                def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "cambioObservacionTitulo")
                if (permiso?.permitido && (observacionInstance?.dateCreated < ultimaCorreccionFinalizada[0]?.dateCreated)) {
                    flash.error = "El título electrónico no cuenta con la(s) firma(s) electrónica(s) de la(s) autoridad(es) de la institución educativa"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                    return
                }
            }
        }
        
        actionLoggingService.log("Eliminando observación...")

        observacionInstance.delete(flush:true)
        
        if(!observacionInstance.hasErrors()) {
            flash.message="Observación eliminada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect(controller:"tituloElectronico", action:"consultarTitulo", id: tituloElectronicoInstance?.id)
        } else {
            flash.error="No se pudo eliminar la observación"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller:"tituloElectronico", action:"consultarTitulo", id: tituloElectronicoInstance?.id)
        }
    }
    
    @ActionLogging
    @ActionType("Revisión de títulos electrónicos")
    @CustomActionName("Anulación de observación")
    @PrintCustomLog
    @Transactional
    def anularObservacion(Observacion observacionInstance) {
        actionLoggingService.log("== Anulación de Observación - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando observación a anular...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!observacionInstance) {
            flash.error="Observación no encontrada"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Observación: ${observacionInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico...")
                
        def tituloElectronicoInstance = observacionInstance?.tituloElectronico
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_REVISOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 2) {
                flash.error = "El paquete no se encuentra en EN REVISION"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
            
            if (observacionInstance?.usuario?.id != usuario?.id) {
                flash.error = "La observación fue realizada por ${observacionInstance?.usuario?.username}"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR")) {
            if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
                flash.error = "El paquete no se encuentra en EN COTEJO"
                redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                return
            }
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE(c.dateCreated) + c.dias", [te: tituloElectronicoInstance])
        
        if (correccionVigente) {
            flash.error = "El título electrónico ya se encuentra en proceso de corrección"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        } else {
            def ultimaCorreccionFinalizada = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) > (DATE(c.dateCreated) + c.dias) ORDER BY c.id ASC", [te: tituloElectronicoInstance], [max: 1])
            if (ultimaCorreccionFinalizada) {
                def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "cambioObservacionTitulo")
                if (permiso?.permitido && (observacionInstance?.dateCreated < ultimaCorreccionFinalizada[0]?.dateCreated)) {
                    flash.error = "El título electrónico no cuenta con la(s) firma(s) electrónica(s) de la(s) autoridad(es) de la institución educativa"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
                    return
                }
            }
        }
        
        actionLoggingService.log("Anulando observación...")

        observacionInstance.estatus = 0
        
        if(observacionInstance.save(flush:true)) {
            flash.message="Observación anulada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect(controller:"tituloElectronico", action:"consultarTitulo", id: tituloElectronicoInstance?.id)
        } else {
            flash.error= observacionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller:"tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            
            return
        }
    }
    
    def solicitudCorreccion(TituloElectronico tituloElectronicoInstance){
        
        if (!tituloElectronicoInstance) {
            flash.error="Seleccione un titulo"
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
            flash.error = "El paquete no se encuentra en EN COTEJO"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE(c.dateCreated) + c.dias", [te: tituloElectronicoInstance])

        if (correccionVigente) {
            flash.error = "El título electrónico ya se encuentra en proceso de corrección"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def solicitudesRealizadas = Correccion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        
        actionLoggingService.log("Solicitudes realizadas: ${solicitudesRealizadas?.size()}")
        
        if (solicitudesRealizadas?.size() > 1) {
            flash.error = "El título electrónico tiene ${solicitudesRealizadas?.size()} solicitud(es) de correccion realizadas. Por lo que no es posible solicitar mas correcciones."
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def observaciones = Observacion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        
        if (!observaciones) {
            flash.error = "El título electrónico no tiene OBSERVACIONES a corregir"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        respond tituloElectronicoInstance, model: [observaciones: observaciones]
    }
    
    @ActionLogging
    @ActionType("Revisión de títulos electrónicos")
    @CustomActionName("Solicitud de corrección de observaciones")
    @SpringUserIdentification
    @PrintCustomLog
    @Transactional
    def solicitarCorreccion() {
        actionLoggingService.log("== Solicitud de corrección de observaciones - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
                        
        actionLoggingService.log("Consultando título electrónico a solicitar corrección...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def tituloElectronicoInstance = TituloElectronico?.findById(params?.id)    
        
        if (!tituloElectronicoInstance) {
            flash.error="Es necesario indicar el título electrónico a solicitar corrección"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
            flash.error = "El paquete no se encuentra en EN COTEJO"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def observaciones = Observacion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        
        if (!observaciones) {
            flash.error = "El título electrónico no tiene OBSERVACIONES a corregir"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def solicitudesRealizadas = Correccion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        
        actionLoggingService.log("Solicitudes realizadas: ${solicitudesRealizadas?.size()}")
        
        if (solicitudesRealizadas?.size() > 2) {
            flash.error = "El título electrónico tiene ${solicitudesRealizadas?.size()} solicitud(es) de correccion realizadas. Por lo que no es posible solicitar mas correcciones."
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Estableciendo información de la solicitud de corrección...")
        
        def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE(c.dateCreated) + c.dias", [te: tituloElectronicoInstance])

        if (correccionVigente) {
            flash.error = "El título electrónico ya se encuentra en proceso de corrección"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def ultimaCorreccion
        
        if (tituloElectronicoInstance?.correcciones) {
            ultimaCorreccion = tituloElectronicoInstance.correcciones.last()
        }
        
        def correccion = new Correccion()
        
        correccion.tituloElectronico = tituloElectronicoInstance
        correccion.numero = ultimaCorreccion? ultimaCorreccion?.numero +1 : 1
        correccion.dias = params.int("dias")
        correccion.estatus = 1
        
        if (correccion.hasErrors()) {
            flash.error= correccion.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond correccion.errors, view:'solicitudCorreccion'
            return
        }
        
        actionLoggingService.log("Solicitud de corrección: ${correccion?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando solicitud de corrección...")
        
        if(correccion.save(flush:true)) {
            flash.message="Solicitud de corrección registrada correctamente"
            
            actionLoggingService.log("${flash.message}")

            redirect controller: "tituloElectronico", action:'consultarTitulo', id: tituloElectronicoInstance?.id
            return
        } else {
            flash.error= correccion.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond correccion.errors, view:'solicitudCorreccion'
            return
        }
        
        
    }
    
    @ActionLogging
    @ActionType("Revisión de títulos electrónicos")
    @CustomActionName("Anulación de corrección")
    @PrintCustomLog
    @Transactional
    def anularCorreccion(Correccion correccionInstance) {
        actionLoggingService.log("== Anulación de Corrección - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando corrección a anular...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!correccionInstance) {
            flash.error="Corrección no encontrada"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Corrección: ${correccionInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico...")
                
        def tituloElectronicoInstance = correccionInstance?.tituloElectronico
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        if (tituloElectronicoInstance?.paquete?.estatus?.id != 3) {
            flash.error = "El paquete no se encuentra en EN COTEJO"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        if (new Date() > (correccionInstance?.dateCreated?.plus(correccionInstance?.dias))) {
            flash.error = "La solicitud de correccion no se encuenta vigente, por lo que no puede anularla."
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Anulando corrección...")

        correccionInstance.estatus = 0
        
        if(correccionInstance.save(flush:true)) {
            flash.message="Corrección anulada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect(controller:"tituloElectronico", action:"consultarTitulo", id: tituloElectronicoInstance?.id)
        } else {
            flash.error= correccionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller:"tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            
            return
        }
    }
}
