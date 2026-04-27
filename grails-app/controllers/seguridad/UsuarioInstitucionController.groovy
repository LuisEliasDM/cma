package seguridad



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import catalogo.InstitucionEducativa
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
class UsuarioInstitucionController {
    def springSecurityService
    def actionLoggingService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond UsuarioInstitucion.list(params), model:[usuarioInstitucionInstanceCount: UsuarioInstitucion.count()]
    }

    def show(UsuarioInstitucion usuarioInstitucionInstance) {
        respond usuarioInstitucionInstance
    }

    def create(Usuario usuarioInstance) {
        if (usuarioInstance == null) {
            notFound()
            return
        }
        
        respond new UsuarioInstitucion(params)
    }

    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Registro de institución de usuario")
    @PrintCustomLog
    @Transactional
    def save() {
        actionLoggingService.log("== Registro de institución de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a registrar institución...")
        
        actionLoggingService.log("Id: ${params?.int("usuario.id")}")
        
        def usuarioInstance = Usuario.get(params?.int("usuario.id"))
        
        if (!usuarioInstance) {
            flash.error="Es necesario seleccionar el usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "usuario", action: "index"
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información de la institución de usuario...")
        
        def institucionEducativaInstance = InstitucionEducativa.get(params.int("institucionEducativa.id"))
        
        if (!institucionEducativaInstance) {
            flash.error="Es necesario seleccionar la institución del usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "usuario", action: "index"
            return
        }
        
        def usuarioInstitucionInstance = new UsuarioInstitucion()
        
        usuarioInstitucionInstance.usuario = usuarioInstance
        usuarioInstitucionInstance.institucionEducativa = institucionEducativaInstance

        if (usuarioInstitucionInstance.hasErrors()) {
            flash.error = usuarioInstitucionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "usuario", action: "index"
            return
        }

        actionLoggingService.log("Institución del usuario: ${usuarioInstitucionInstance?.toPlainText("\t")}")

        actionLoggingService.log("Guardando institución...")
        
        if(usuarioInstitucionInstance.save(flush:true)) {
            flash.message="Institución de usuario registrada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect controller: "usuario", action: "show", id: usuarioInstance?.id
            return
        } else {
            flash.error= usuarioInstitucionInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "usuario", action: "index"
            return
        }
    }

    def edit(UsuarioInstitucion usuarioInstitucionInstance) {
        respond usuarioInstitucionInstance
    }

    @Transactional
    def update(UsuarioInstitucion usuarioInstitucionInstance) {
        if (usuarioInstitucionInstance == null) {
            notFound()
            return
        }

        if (usuarioInstitucionInstance.hasErrors()) {
            respond usuarioInstitucionInstance.errors, view:'edit'
            return
        }

        usuarioInstitucionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'UsuarioInstitucion.label', default: 'UsuarioInstitucion'), usuarioInstitucionInstance.id])
                redirect usuarioInstitucionInstance
            }
            '*'{ respond usuarioInstitucionInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(UsuarioInstitucion usuarioInstitucionInstance) {

        if (usuarioInstitucionInstance == null) {
            notFound()
            return
        }

        usuarioInstitucionInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'UsuarioInstitucion.label', default: 'UsuarioInstitucion'), usuarioInstitucionInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuarioInstitucion.label', default: 'UsuarioInstitucion'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
