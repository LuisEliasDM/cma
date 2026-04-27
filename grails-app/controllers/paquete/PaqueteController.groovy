package paquete

import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*
import seguridad.UsuarioInstitucion
import grails.plugin.springsecurity.SpringSecurityUtils
import catalogo.InstitucionEducativa
import estructuraXml.TituloElectronico
import org.hibernate.criterion.CriteriaSpecification

@SpringUserIdentification
class PaqueteController {
    def actionLoggingService
    def springSecurityService
    
    def index(Integer max) {
        def referencia = params?.referencia?.trim()
        
        params.max = Math.min(max ?: 10, 100)
        params.sort = "dateCreated"
        params.order = "desc"
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
    
        def usuarioInstitucion = UsuarioInstitucion.findAllByUsuario(usuario)
        if (usuarioInstitucion) {
            session["cveInstitucion"] = usuarioInstitucion?.first()?.institucionEducativa?.clave
        }
                
        if (params.cveInstitucion) {
            session["cveInstitucion"] = params.cveInstitucion != "0"? params.cveInstitucion: null   
        }
        
        def institucionesEducativas
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR")) {
            institucionesEducativas = InstitucionEducativa.list()
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
        
//        if (!institucionesEducativas && !session["cveInstitucion"] && SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
//            redirect action: "institucionNoAsignada"
//            return
//        }

        def listaEstatusPaquetes = EstatusPaquete.createCriteria().list {
            createAlias("paquetes", "pq", CriteriaSpecification.LEFT_JOIN)
            projections{
                groupProperty "id"
                property("nombreBandeja")
                count("pq.id")
            }
            or {
                eq("pq.activo", true)
                isNull("pq.id")
            }
            
        }
        
        def conteoTodos = Paquete.createCriteria().list {
            projections{
                count("id")
            }
            eq("activo", true)
        }
                
        def estatusPaquete
        if (params.idEstatusPaquete) {
            session["idEstatusPaquete"] = params.idEstatusPaquete
        }
        
        estatusPaquete = EstatusPaquete.get(session["idEstatusPaquete"]?.toInteger())
                        
        def paquetes = []
        
        if (referencia) {
        
            paquetes = Paquete.createCriteria().list(params) {
                or {
                    ilike("folio", "%${referencia}%")
                    estatus {
                        or {
                            ilike("descripcion", "%${referencia}%")
                        }
                    }
                    institucionEducativa {
                        or {
                            ilike("clave", "%${referencia}%")
                            ilike("descripcion", "%${referencia}%")
                        }
                    }
                }
                if (session["cveInstitucion"]) {
                    institucionEducativa {
                        and {
                            eq("clave", session["cveInstitucion"]?:"")
                        }
                    }
                } else {
                    institucionEducativa {
                        and {
                            'in'("clave", institucionesEducativas*.clave?:"")
                        }
                    }
                }
                
                if (estatusPaquete) {
                    and {
                        eq("estatus", estatusPaquete)
                    }
                }
                and {
                    eq("activo", true)
                }
                
                /*
                if (SpringSecurityUtils.ifAnyGranted("ROLE_RECEPTOR")) {
                    firmaResponsables {
                        and {
                            isNotEmpty("firmaResponsable")
                        }
                    }
                }
                
                if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR,ROLE_REVISOR")) {
                    and {
                        eq("pagado", true)
                    }
                }
                
                if (SpringSecurityUtils.ifAnyGranted("ROLE_AUTENTICADOR")) {
                    and {
                        eq("revisado", true)
                    }
                }*/
                
            }
            
        } else {
            paquetes = Paquete.createCriteria().list(params) {
                if (session["cveInstitucion"]) {
                    institucionEducativa {
                        and {
                            eq("clave", session["cveInstitucion"]?:"")
                        }
                    }
                } else {
                    institucionEducativa {
                        and {
                            'in'("clave", institucionesEducativas*.clave?:"")
                        }
                    }
                }
                
                if (estatusPaquete) {
                    and {
                        eq("estatus", estatusPaquete)
                    }
                }
                and {
                    eq("activo", true)
                }
                
                /*
                if (SpringSecurityUtils.ifAnyGranted("ROLE_RECEPTOR")) {
                    firmaResponsables {
                        and {
                            isNotEmpty("firmaResponsable")
                        }
                    }
                }
                
                if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR,ROLE_REVISOR")) {
                    and {
                        eq("pagado", true)
                    }
                }
                
                if (SpringSecurityUtils.ifAnyGranted("ROLE_AUTENTICADOR")) {
                    and {
                        eq("revisado", true)
                    }
                }
                */
            }
        }
        
        def totalTitulos = TituloElectronico.countByActivo(true)
                
        respond paquetes, model:[paqueteInstanceCount: paquetes.totalCount, institucionesEducativas: institucionesEducativas, usuarioInstitucion: usuarioInstitucion, totalTitulos: totalTitulos, listaEstatusPaquetes: listaEstatusPaquetes, conteoTodos: conteoTodos], view: "index"
        
    }
    
    def consultarPaquete(Paquete paqueteInstance) {
        respond paqueteInstance
    }
    
    def capturarPaquete() {
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        
        def institucionesEducativas
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR")) {
            institucionesEducativas = InstitucionEducativa.list()
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
        
        respond new Paquete(params), model: [institucionesEducativas: institucionesEducativas]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Registro de paquete")
    @PrintCustomLog
    @Transactional
    def guardarInformacion() {
        actionLoggingService.log("== Registro de paquete - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
                
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR")) {
            institucionesEducativas = InstitucionEducativa.list()
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
        
        actionLoggingService.log("Estableciendo información del paquete...")
        
        def paqueteInstance = new Paquete()
        
        def estatus = EstatusPaquete.get(1)
        
        def institucionEducativaInstance = InstitucionEducativa.get(params.int("institucionEducativa.id"))
        
        if (!institucionEducativaInstance) {
            flash.error="Es necesario seleccionar la institución educativa del paquete"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action: "capturarPaquete"
            return
        }

        paqueteInstance?.institucionEducativa = institucionEducativaInstance    
        paqueteInstance?.folio = params?.folio?.trim()
        paqueteInstance?.estatus = estatus
        paqueteInstance?.activo = true
                
        if (paqueteInstance.hasErrors()) {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'capturarPaquete'
            return
        } 
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando paquete...")
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete registrado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'capturarPaquete'
            return
        }
    }
    
    def modificarPaquete(Paquete paqueteInstance) {
        
        if (paqueteInstance?.estatus?.id != 1) {
            flash.error = "El paquete no se puede modificar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
                
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        
        def institucionesEducativas
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR")) {
            institucionesEducativas = InstitucionEducativa.list()
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
        
        respond paqueteInstance, model: [institucionesEducativas: institucionesEducativas]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Modificación de paquete")
    @PrintCustomLog
    @Transactional
    def actualizarRegistro() {
        actionLoggingService.log("== Modificación de paquete - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
                        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR")) {
            institucionesEducativas = InstitucionEducativa.list()
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
                
        actionLoggingService.log("Consultando paquete a modificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def paqueteInstance = Paquete?.findById(params?.id)    
        
        if (!paqueteInstance) {
            flash.error="Es necesario indicar el paquete a modificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Paquete (original): ${paqueteInstance?.toPlainText("\t")}")
        
        if (paqueteInstance?.estatus?.id != 1) {
            flash.error = "El paquete no se puede modificar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        actionLoggingService.log("Estableciendo información del paquete...")
        
        def estatus = EstatusPaquete.get(1)
        
        def institucionEducativaInstance = InstitucionEducativa.get(params.int("institucionEducativa.id"))
        
        if (!institucionEducativaInstance) {
            flash.error="Es necesario seleccionar la institución educativa del paquete"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action: "capturarPaquete"
            return
        }

        paqueteInstance?.institucionEducativa = institucionEducativaInstance    
        paqueteInstance?.folio = params?.folio?.trim()
        paqueteInstance?.estatus = estatus

        if (paqueteInstance.hasErrors()) {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'modificarPaquete'
            return
        }  
       
        actionLoggingService.log("Paquete (actualizado): ${paqueteInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando paquete...")
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete actualizado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'modificarPaquete'
            return
        }
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Eliminación de paquete")
    @PrintCustomLog
    @Transactional
    def eliminarPaquete(Paquete paqueteInstance) {
        actionLoggingService.log("== Eliminación de paquete - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (paqueteInstance?.estatus?.id != 1) {
            flash.error = "El paquete no se puede eliminar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        paqueteInstance.activo = false
        
        def titulosPaquete = paqueteInstance?.titulosElectronicos
        
        titulosPaquete?.each {
            paqueteInstance?.removeFromTitulosElectronicos(it)
        }
        
        actionLoggingService.log("Eliminando paquete...")
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete eliminado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect action: "index"
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: 'consultarPaquete', id: paqueteInstance?.id
            return
        }

    }
    
    def asignacionPaquete(TituloElectronico tituloElectronicoInstance){
        if (tituloElectronicoInstance == null) {
            flash.error="Es necesario indicar el título electrónico a asignar paquete"
            redirect controller: "tituloElectronico", action:"index"
            return
        }
        
        def permiso = tituloElectronicoInstance?.getPermiso("asignarPaquete")
        if (!permiso.permitido) {
            flash.error=permiso?.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action:"consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def estatus = EstatusPaquete.get(1)
        def institucion = InstitucionEducativa.findByClave(tituloElectronicoInstance?.institucion?.cveInstitucion)
        def paquetesInstitucion = Paquete.findAllByEstatusAndInstitucionEducativaAndActivo(estatus, institucion, true)
        
        [paquetesInstitucion: paquetesInstitucion, tituloElectronicoInstance: tituloElectronicoInstance]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Asignación de paquete")
    @PrintCustomLog
    @Transactional
    def asignarPaquete(TituloElectronico tituloElectronicoInstance) {
        actionLoggingService.log("== Asignación de paquete - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico a asignar paquete...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (tituloElectronicoInstance == null) {
            flash.error="Es necesario indicar el título electrónico a asignar paquete"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action:"index"
            return
        }
        
        actionLoggingService.log("Título Electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        def permiso = tituloElectronicoInstance?.getPermiso("asignarPaquete")
        if (!permiso.permitido) {
            flash.error=permiso?.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action:"consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Consultando paquete a asignar...")
        
        actionLoggingService.log("Id: ${params?.idPaquete}")
        
        def paqueteInstance = Paquete.get(params?.idPaquete)
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (paqueteInstance) {
            if (paqueteInstance?.estatus?.id != 1) {
                flash.error = "El paquete ya se envio a revision y no puede modificarse"
                redirect action: "consultarPaquete", id: paqueteInstance?.id
                return
            }
        }

        actionLoggingService.log("Asignando paquete...")
        
        tituloElectronicoInstance?.paquete = paqueteInstance?: null
        
        if(tituloElectronicoInstance.save(flush:true)) {
            flash.message="Paquete asignado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id 
            return
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'asignacionPaquete'
            return
        }

    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Envío de paquete a revisión")
    @PrintCustomLog
    @Transactional
    def enviarRevision(Paquete paqueteInstance) {
        actionLoggingService.log("== Envío de paquete a revisión - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a enviar a revisión...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a enviar a revisión"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (paqueteInstance?.estatus?.id != 1) {
            flash.error = "El paquete no se puede enviar a revisión"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        if (!paqueteInstance?.comprobantesPago) {
            flash.error = "El paquete no tiene comprobantes de pago registrados"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Enviando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(2)
        paqueteInstance.fechaEnvioRevision = new Date()
        paqueteInstance.receptor = usuario
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete enviado a revisión correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Envío de paquete a cotejo")
    @PrintCustomLog
    @Transactional
    def finalizarRevision(Paquete paqueteInstance) {
        actionLoggingService.log("== Envío de paquete a cotejo - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a enviar a revisión...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a enviar a cotejo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (paqueteInstance?.estatus?.id != 2) {
            flash.error = "El paquete no se puede enviar a cotejo"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Enviando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(3)
        paqueteInstance.fechaRevision = new Date()
        paqueteInstance.revisor = usuario
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete enviado a cotejo correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    def rechazoCotejo(Paquete paqueteInstance) {
        
        if (!paqueteInstance) {
            flash.error = "Seleccione el paquete a rechazar"
            redirect action: "index"
            return
        }
        
        if (!paqueteInstance?.estatus?.id in [2,3]) {
            flash.error = "El paquete no se puede rechazar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
                
        respond paqueteInstance
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Rechazo de paquete en revision/cotejo a ventanilla")
    @PrintCustomLog
    @Transactional
    def rechazarCotejo(Paquete paqueteInstance) {
        actionLoggingService.log("== Rechazo de paquete en revision/cotejo a ventanilla - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a rechazar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a rechazar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (!paqueteInstance?.estatus?.id in [2,3]) {
            flash.error = "El paquete no se puede rechazar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Rechazando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(4)
        paqueteInstance.fechaRechazoCotejo = new Date()
        paqueteInstance.cotejador = usuario
        paqueteInstance.motivoRechazoCotejo = params?.motivoRechazo?.trim()
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete rechazado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    def anulacionRechazoCotejo(Paquete paqueteInstance) {
        
        if (!paqueteInstance) {
            flash.error = "Seleccione el paquete a anular rechazo"
            redirect action: "index"
            return
        }
        
        if (!paqueteInstance?.estatus?.id in [4]) {
            flash.error = "El rechazo del paquete no se puede anular"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
                
        respond paqueteInstance
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Anulación de rechazo de paquete en cotejo")
    @PrintCustomLog
    @Transactional
    def anularRechazoCotejo(Paquete paqueteInstance) {
        actionLoggingService.log("== Anulación de rechazo de paquete en cotejo - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a anular rechazo...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a anular rechazao"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (!paqueteInstance?.estatus?.id in [4]) {
            flash.error = "El rechazo del paquete no se puede anular"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Anulando rechazo del paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(3)
        paqueteInstance.fechaAnulacionRechazoCotejo = new Date()
        paqueteInstance.autenticador = usuario
        paqueteInstance.motivoAnulacionRechazoCotejo = params?.motivoAnulacionRechazo?.trim()
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Rechazo del paquete anulado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Envío de paquete a autenticación")
    @PrintCustomLog
    @Transactional
    def finalizarCotejo(Paquete paqueteInstance) {
        actionLoggingService.log("== Envío de paquete a autenticación - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a enviar a autenticación...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a enviar a autenticación"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (paqueteInstance?.estatus?.id != 3) {
            flash.error = "El paquete no se puede enviar a autenticación"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        def titulosObservados = TituloElectronico.createCriteria().listDistinct {
            and {
                eq("paquete", paqueteInstance)
                observaciones {
                    and {
                        eq("estatus", true)
                    }
                }
            }
        }
        
        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
        
        actionLoggingService.log("Títulos electronicos observados: ${titulosObservados?.size()}")
        actionLoggingService.log("Títulos electronicos a autenticar: ${titulosCotejadosSinObservaciones?.size()}")
        
        if (paqueteInstance?.titulosElectronicos?.size() == titulosObservados?.size()) {
            flash.error = "El paquete no tiene títulos electrónicos para autenticar"
            redirect action: "rechazoCotejo", id: paqueteInstance?.id
            return
        }
        
        def registradosLibro = 0
        titulosCotejadosSinObservaciones?.each {
            if (it?.libro && it.foja && it.fechaRegistroLibro) {
                registradosLibro ++
            }
        }
        
        if (registradosLibro != titulosCotejadosSinObservaciones?.size()) {
            flash.error = "El paquete tiene ${(titulosCotejadosSinObservaciones?.size() - registradosLibro)} título(s) electrónico(s) sin registro en libro. Por lo que no se puede finalizar el cotejo"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        
        def correccionesPendientes = 0
        titulosObservados?.each { to ->
            if (to?.correcciones) {
                to.correcciones.each {
                    if (new Date() <= (it?.dateCreated?.plus(it?.dias))) {
                        correccionesPendientes ++
                    }
                }
            }
        }
        
        if (correccionesPendientes > 0) {
            flash.error = "El paquete tiene ${correccionesPendientes} título(s) electrónico(s) con observacion en espera de su correción. Por lo que no se puede finalizar el cotejo"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Enviando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(5)
        paqueteInstance.fechaCotejo= new Date()
        paqueteInstance.cotejador = usuario
        
        paqueteInstance.motivoRechazoCotejo = null
        paqueteInstance.motivoRechazoAutenticacion = null
        paqueteInstance.motivoRechazoRegistroDgp = null        
        paqueteInstance.fechaRechazoCotejo = null
        paqueteInstance.fechaRechazoAutenticacion = null
        paqueteInstance.fechaRechazoRegistroDgp = null
        paqueteInstance.autenticador = null
        
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete enviado a autenticación correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Envío de paquete a registro ante DGP")
    @PrintCustomLog
    @Transactional
    def finalizarAutenticacion(Paquete paqueteInstance) {
        actionLoggingService.log("== Envío de paquete a registro ante DGP - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a enviar a registro ante DGP...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a enviar a registro ante DGP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (paqueteInstance?.estatus?.id != 5) {
            flash.error = "El paquete no se puede enviar a registro ante DGP"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
        
        def titulosAutenticados = 0
        titulosCotejadosSinObservaciones?.each {
            if (it.autenticacion) {
                titulosAutenticados ++
            }
        }
        
        actionLoggingService.log("Títulos electronicos cotejados sin observacion: ${titulosCotejadosSinObservaciones?.size()}")
        actionLoggingService.log("Títulos electronicos autenticados: ${titulosAutenticados}")
        
        if (titulosCotejadosSinObservaciones?.size() != titulosAutenticados) {
            flash.error = "El paquete tiene ${titulosCotejadosSinObservaciones?.size() - titulosAutenticados} título(s) electrónico(s) sin autenticar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Enviando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(6)
        paqueteInstance.fechaAutenticacion = new Date()
        paqueteInstance.autenticador = usuario
        
        paqueteInstance.motivoRechazoCotejo = null
        paqueteInstance.motivoRechazoAutenticacion = null
        paqueteInstance.motivoRechazoRegistroDgp = null        
        paqueteInstance.fechaRechazoCotejo = null
        paqueteInstance.fechaRechazoAutenticacion = null
        paqueteInstance.fechaRechazoRegistroDgp = null
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete enviado a registro ante DGP correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    def rechazoAutenticacion(Paquete paqueteInstance) {
        
        if (!paqueteInstance) {
            flash.error = "Seleccione el paquete a rechazar"
            redirect action: "index"
            return
        }
        
        if (!paqueteInstance?.estatus?.id in [5]) {
            flash.error = "El paquete no se puede rechazar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
        
        def titulosNoAutenticados = []
        def titulosAutenticados = 0
        titulosCotejadosSinObservaciones?.each { t ->
            if (t.autenticacion) {
                titulosAutenticados ++
            } else {
                titulosNoAutenticados.add(t)
            }
        }
        
        actionLoggingService.log("Títulos electronicos cotejados sin observacion: ${titulosCotejadosSinObservaciones?.size()}")
        actionLoggingService.log("Títulos electronicos autenticados: ${titulosAutenticados}")
        actionLoggingService.log("Títulos electronicos no autenticados: ${titulosNoAutenticados?.size()}")
        
        if (titulosCotejadosSinObservaciones?.size() == titulosAutenticados) {
            flash.error = "El paquete tiene todos título(s) electrónico(s) autenticados. Por lo que no se puede rechazar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
                
        respond paqueteInstance, model: [titulosNoAutenticados: titulosNoAutenticados]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Rechazo de paquete en autenticación a cotejo")
    @PrintCustomLog
    @Transactional
    def rechazarAutenticacion(Paquete paqueteInstance) {
        actionLoggingService.log("== Rechazo de paquete en autenticación a cotejo - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a rechazar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a rechazar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (!paqueteInstance?.estatus?.id in [5]) {
            flash.error = "El paquete no se puede rechazar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
        
        def titulosNoAutenticados = []
        def titulosAutenticados = 0
        titulosCotejadosSinObservaciones?.each { t ->
            if (t.autenticacion) {
                titulosAutenticados ++
            } else {
                titulosNoAutenticados.add(t)
            }
        }
        
        actionLoggingService.log("Títulos electronicos cotejados sin observacion: ${titulosCotejadosSinObservaciones?.size()}")
        actionLoggingService.log("Títulos electronicos autenticados: ${titulosAutenticados}")
        actionLoggingService.log("Títulos electronicos no autenticados: ${titulosNoAutenticados?.size()}")
        
        if (titulosCotejadosSinObservaciones?.size() == titulosAutenticados) {
            flash.error = "El paquete tiene todos título(s) electrónico(s) autenticados. Por lo que no se puede rechazar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        actionLoggingService.log("Rechazando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(3)
        paqueteInstance.fechaRechazoAutenticacion = new Date()
        paqueteInstance.autenticador = usuario
        paqueteInstance.motivoRechazoAutenticacion = params?.motivoRechazo?.trim()
        
        paqueteInstance.motivoRechazoCotejo = null
        paqueteInstance.motivoRechazoRegistroDgp = null        
        paqueteInstance.fechaRechazoCotejo = null
        paqueteInstance.fechaRechazoRegistroDgp = null
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete rechazado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    /**/
    
    def rechazoRegistroDgp(Paquete paqueteInstance) {
        
        if (!paqueteInstance) {
            flash.error = "Seleccione el paquete a rechazar"
            redirect action: "index"
            return
        }
       
        if (!paqueteInstance?.estatus?.id in [6]) {
            flash.error = "El paquete no se puede rechazar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
        
        def titulosRegistrados = TituloElectronico.createCriteria().listDistinct {
            and {
                eq("paquete", paqueteInstance)
                registros {
                    archivosResultado {
                        detalles {
                            and {
                                eq("estatus", 1)
                            }
                        }
                    }
                }
            }
        }
        
        def titulosNoRegistrados = []
        titulosCotejadosSinObservaciones?.each { t ->
            if (!(t in titulosRegistrados)) {
                titulosNoRegistrados.add(t)
            }
        }
        
        actionLoggingService.log("Títulos electronicos cotejados sin observacion: ${titulosCotejadosSinObservaciones?.size()}")
        actionLoggingService.log("Títulos electronicos registrados ante DGP: ${titulosRegistrados?.size()}")
        actionLoggingService.log("Títulos electronicos no registrados ante DGP: ${titulosNoRegistrados?.size()}")
        
        if (titulosCotejadosSinObservaciones?.size() == titulosRegistrados?.size()) {
            flash.error = "El paquete tiene todos los título(s) electrónico(s) registrados exitosamente ante DGP"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        respond paqueteInstance, model: [titulosNoRegistrados: titulosNoRegistrados]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Rechazo de paquete en registro ante DGP a cotejo")
    @PrintCustomLog
    @Transactional
    def rechazarRegistroDgp(Paquete paqueteInstance) {
        actionLoggingService.log("== Rechazo de paquete en registro ante DGP a cotejo - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a rechazar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a rechazar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (!paqueteInstance?.estatus?.id in [6]) {
            flash.error = "El paquete no se puede rechazar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
        
        def titulosRegistrados = TituloElectronico.createCriteria().listDistinct {
            and {
                eq("paquete", paqueteInstance)
                registros {
                    archivosResultado {
                        detalles {
                            and {
                                eq("estatus", 1)
                            }
                        }
                    }
                }
            }
        }
        
        def titulosNoRegistrados = []
        titulosCotejadosSinObservaciones?.each { t ->
            if (!(t in titulosRegistrados)) {
                titulosNoRegistrados.add(t)
                
                def autenticacion = t?.autenticacion
                
                if (autenticacion) {
                    t.autenticacion = null
                    autenticacion.delete(flush: true)
                    t.save(flush: true)
                }
                                
            }
        }
        
        actionLoggingService.log("Títulos electronicos cotejados sin observacion: ${titulosCotejadosSinObservaciones?.size()}")
        actionLoggingService.log("Títulos electronicos registrados ante DGP: ${titulosRegistrados?.size()}")
        actionLoggingService.log("Títulos electronicos no registrados ante DGP: ${titulosNoRegistrados?.size()}")
        
        if (titulosCotejadosSinObservaciones?.size() == titulosRegistrados?.size()) {
            flash.error = "El paquete tiene todos los título(s) electrónico(s) registrados exitosamente ante DGP"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        actionLoggingService.log("Rechazando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(3)
        paqueteInstance.fechaRechazoRegistroDgp = new Date()
        paqueteInstance.autenticador = usuario
        paqueteInstance.motivoRechazoRegistroDgp = params?.motivoRechazo?.trim()
        
        paqueteInstance.motivoRechazoCotejo = null
        paqueteInstance.motivoRechazoAutenticacion = null
        paqueteInstance.fechaRechazoCotejo = null
        paqueteInstance.fechaRechazoAutenticacion = null
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete rechazado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    /**/
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Envío de paquete a registrado ante DGP")
    @PrintCustomLog
    @Transactional
    def finalizarRegistroDgp(Paquete paqueteInstance) {
        actionLoggingService.log("== Envío de paquete a registrado ante DGP - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a enviar a registrado ante DGP...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a enviar a registrado ante DGP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (paqueteInstance?.estatus?.id != 6) {
            flash.error = "El paquete no se puede enviar a registrado ante DGP"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }
        
        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
                
        def titulosRegistrados = TituloElectronico.createCriteria().listDistinct {
            and {
                eq("paquete", paqueteInstance)
                registros {
                    archivosResultado {
                        detalles {
                            and {
                                eq("estatus", 1)
                            }
                        }
                    }
                }
            }
        }
        
        def titulosNoRegistrados = TituloElectronico.createCriteria().listDistinct {
            and {
                eq("paquete", paqueteInstance)
                registros {
                    archivosResultado {
                        detalles {
                            and {
                                ne("estatus", 1)
                            }
                        }
                    }
                }
            }
        }
        
        actionLoggingService.log("Títulos electronicos cotejados sin observacion: ${titulosCotejadosSinObservaciones?.size()}")
        actionLoggingService.log("Títulos electronicos registrados ante DGP: ${titulosRegistrados?.size()}")
        actionLoggingService.log("Títulos electronicos no registrados ante DGP: ${titulosNoRegistrados?.size()}")
        
        if (titulosCotejadosSinObservaciones?.size() != (titulosRegistrados?.size() + titulosNoRegistrados?.size())) {
            flash.error = "El paquete tiene ${titulosCotejadosSinObservaciones?.size() - titulosRegistrados?.size()} título(s) electrónico(s) sin regitrar ante DGP"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Enviando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(7)
        paqueteInstance.fechaRegistroDGP = new Date()
        paqueteInstance.autenticador = usuario
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete enviado a registro ante DGP correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Envío de paquete a entrega")
    @PrintCustomLog
    @Transactional
    def enviarEntrega(Paquete paqueteInstance) {
        actionLoggingService.log("== Envío de paquete a entrega - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a enviar a entrega...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a enviar a entrega"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (!paqueteInstance?.estatus?.id in [4,7]) {
            flash.error = "El paquete no se puede enviar a entrega"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Enviando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(8)
        paqueteInstance.fechaParaEntrega = new Date()
        paqueteInstance.entregante = usuario
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete enviado a entrega correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Entrega de paquete")
    @PrintCustomLog
    @Transactional
    def entregar(Paquete paqueteInstance) {
        actionLoggingService.log("== Entrega de paquete - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete a entregar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (paqueteInstance == null) {
            flash.error="Es necesario indicar el paquete a entregar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Paquete: ${paqueteInstance?.toPlainText("\t")}")
        
        if (!paqueteInstance?.estatus?.id in [8]) {
            flash.error = "El paquete no se puede entregar"
            redirect action: "consultarPaquete", id: paqueteInstance?.id
            return
        }

        actionLoggingService.log("Entregando paquete...")
        
        paqueteInstance.estatus = EstatusPaquete.get(9)
        paqueteInstance.fechaEntrega = new Date()
        paqueteInstance.emisor = usuario
        
        if(paqueteInstance.save(flush:true)) {
            flash.message="Paquete entregado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond paqueteInstance, view:'consultarPaquete'
            return
        } else {
            flash.error= paqueteInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond paqueteInstance.errors, view:'consultarPaquete'
            return
        }
        
    }
}
