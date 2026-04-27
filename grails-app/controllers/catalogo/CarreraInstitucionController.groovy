package catalogo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
class CarreraInstitucionController {
    def springSecurityService
    def actionLoggingService

    static allowedMethods = [save: "POST", update: "PUT"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond CarreraInstitucion.list(params), model:[carreraInstitucionInstanceCount: CarreraInstitucion.count()]
    }

    def show(CarreraInstitucion carreraInstitucionInstance) {
        respond carreraInstitucionInstance
    }

    def create() {
        def autorizacionReconocimientoList = AutorizacionReconocimiento.list()
        respond new CarreraInstitucion(params), model: [autorizacionReconocimientoList: autorizacionReconocimientoList]
    }

    @ActionLogging
    @ActionType("Gestión de instituciones educativas")
    @CustomActionName("Registro de carrera")
    @PrintCustomLog
    @Transactional
    def save() {
        actionLoggingService.log("== Registro de carrera - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando institución educativa...")
        
        def institucionEducativa = InstitucionEducativa.get(params.int("institucionEducativa.id"))
        
        if (!institucionEducativa) {
            flash.error="Es necesario indicar la institución educativa para la carrera"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"create"
            return
        }
        
        actionLoggingService.log("Institución educativa: ${institucionEducativa?.toPlainText("\t")}")
                
        actionLoggingService.log("Estableciendo información de la carrera...")
        
        def carreraInstitucionInstance = new CarreraInstitucion()
        
        carreraInstitucionInstance.institucionEducativa = institucionEducativa
        carreraInstitucionInstance.clave = params.clave
        carreraInstitucionInstance.autorizacionReconocimiento = AutorizacionReconocimiento.get(params.int("autorizacionReconocimiento.id"))
        carreraInstitucionInstance.rvoe = params.rvoe
        carreraInstitucionInstance.descripcion = params.descripcion
        
        actionLoggingService.log("Carrera: ${carreraInstitucionInstance?.toPlainText("\t")}")

        if (carreraInstitucionInstance.hasErrors()) {
            flash.error= carreraInstitucionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond carreraInstitucionInstance.errors, view:'create'
            return
        }
        
        actionLoggingService.log("Guardando carrera...")
        
        if (carreraInstitucionInstance.save(flush:true)){
            flash.message="Carrera registrada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect controller: "institucionEducativa", action: "show", id: institucionEducativa?.id
        } else {
            flash.error= carreraInstitucionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "create"
        }
    }

    def edit(CarreraInstitucion carreraInstitucionInstance) {
        def autorizacionReconocimientoList = AutorizacionReconocimiento.list()
        respond carreraInstitucionInstance, model: [autorizacionReconocimientoList: autorizacionReconocimientoList]
    }

    @ActionLogging
    @ActionType("Gestión de instituciones educativas")
    @CustomActionName("Modificación de carrera")
    @PrintCustomLog
    @Transactional
    def update() {
        actionLoggingService.log("== Modificación de carrera - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando carrera a modificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def carreraInstitucionInstance = CarreraInstitucion.get(params.int("id"))
        
        actionLoggingService.log("Carrera (original): ${carreraInstitucionInstance?.toPlainText("\t")}")
        
        if (!carreraInstitucionInstance) {
            flash.error="Es necesario indicar la carrera a modificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "institucionEducativa", action:"index"
            return
        }
        
        actionLoggingService.log("Consultando institución educativa...")
        
        def institucionEducativa = InstitucionEducativa.get(params.int("institucionEducativa.id"))
        
        if (!institucionEducativa) {
            flash.error="Es necesario indicar la institución educativa para la carrera"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"edit", id: carreraInstitucionInstance.id
            return
        }
        
        actionLoggingService.log("Institución educativa: ${institucionEducativa?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información de la carrera...")
        
        carreraInstitucionInstance.institucionEducativa = institucionEducativa
        carreraInstitucionInstance.clave = params.clave
        carreraInstitucionInstance.autorizacionReconocimiento = AutorizacionReconocimiento.get(params.int("autorizacionReconocimiento.id"))
        carreraInstitucionInstance.rvoe = params.rvoe
        carreraInstitucionInstance.descripcion = params.descripcion

        if (carreraInstitucionInstance.hasErrors()) {
            flash.error= carreraInstitucionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond carreraInstitucionInstance.errors, view:'edit'
            return
        }
        
        actionLoggingService.log("Carrera (actualizada): ${carreraInstitucionInstance?.toPlainText("\t")}")

        actionLoggingService.log("Guardando carrera...")
        
        if (carreraInstitucionInstance.save(flush:true)){
            flash.message="Carrera modificada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect controller: "institucionEducativa", action: "show", id: institucionEducativa?.id
        } else {
            flash.error= carreraInstitucionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "create"
        }
    }

    @ActionLogging
    @ActionType("Gestión de instituciones educativas")
    @CustomActionName("Eliminación de carrera")
    @PrintCustomLog
    @Transactional
    def delete(CarreraInstitucion carreraInstitucionInstance) {
        actionLoggingService.log("== Eliminación de carrera - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando carrera a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (carreraInstitucionInstance == null) {
            flash.error="Es necesario indicar la carrera a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "institucionEducativa", action:"index"
            return
        }
        
        actionLoggingService.log("Carrera: ${carreraInstitucionInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando institución educativa...")
        
        def institucionEducativa = InstitucionEducativa.get(carreraInstitucionInstance?.institucionEducativa?.id)
        
        if (!institucionEducativa) {
            flash.error="No se pudo recuperar la institución educativa de la carrera"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"edit", id: carreraInstitucionInstance.id
            return
        }
        
        actionLoggingService.log("Institución educativa: ${institucionEducativa?.toPlainText("\t")}")

        actionLoggingService.log("Eliminando carrera...")
        
        institucionEducativa.removeFromCarrerasInstitucion(carreraInstitucionInstance)

        carreraInstitucionInstance.delete flush:true

        flash.message="Carrera eliminada correctamente"
            
        actionLoggingService.log("${flash.message}")

        redirect controller: "institucionEducativa", action: "show", id: institucionEducativa.id
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = "Carrera no encontrada"
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
