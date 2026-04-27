package seguridad



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
@Transactional(readOnly = true)
class UsuarioRolController {
    def springSecurityService
    def actionLoggingService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond UsuarioRol.list(params), model:[usuarioRolInstanceCount: UsuarioRol.count()]
    }

    def show(UsuarioRol usuarioRolInstance) {
        respond usuarioRolInstance
    }

    def create(Usuario usuarioInstance) {
        if (usuarioInstance == null) {
            notFound()
            return
        }
        
        respond new UsuarioRol(params)
    }

    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Registro de rol de usuario")
    @PrintCustomLog
    @Transactional
    def save() {
        actionLoggingService.log("== Registro de rol de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a registrar rol...")
        
        actionLoggingService.log("Id: ${params?.int("usuario.id")}")
        
        def usuarioInstance = Usuario.get(params?.int("usuario.id"))
        
        if (!usuarioInstance) {
            flash.error="Es necesario seleccionar el usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "usuario", action: "index"
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del rol de usuario...")
        
        def rolInstance = Rol.get(params.int("rol.id"))
        
        if (!rolInstance) {
            flash.error="Es necesario seleccionar el rol de usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "usuario", action: "index"
            return
        }
        
        def usuarioRolInstance = new UsuarioRol()
        
        usuarioRolInstance.usuario = usuarioInstance
        usuarioRolInstance.rol = rolInstance
        
        usuarioInstance.requiereReingreso = true

        if (usuarioRolInstance.hasErrors()) {
            flash.error = usuarioRolInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "usuario", action: "index"
            return
        }
        
        actionLoggingService.log("Rol de usuario: ${usuarioRolInstance?.toPlainText("\t")}")

        actionLoggingService.log("Guardando usuario...")
        
        if(usuarioRolInstance.save(flush:true)) {
            flash.message="Rol de usuario registrado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect controller: "usuario", action: "show", id: usuarioInstance?.id
            return
        } else {
            flash.error= usuarioInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "usuario", action: "index"
            return
        }

    }

    def edit(UsuarioRol usuarioRolInstance) {
        respond usuarioRolInstance
    }

    @Transactional
    def update(UsuarioRol usuarioRolInstance) {
        if (usuarioRolInstance == null) {
            notFound()
            return
        }

        if (usuarioRolInstance.hasErrors()) {
            respond usuarioRolInstance.errors, view:'edit'
            return
        }

        usuarioRolInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'UsuarioRol.label', default: 'UsuarioRol'), usuarioRolInstance.id])
                redirect usuarioRolInstance
            }
            '*'{ respond usuarioRolInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(UsuarioRol usuarioRolInstance) {

        if (usuarioRolInstance == null) {
            notFound()
            return
        }

        usuarioRolInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'UsuarioRol.label', default: 'UsuarioRol'), usuarioRolInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuarioRol.label', default: 'UsuarioRol'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
