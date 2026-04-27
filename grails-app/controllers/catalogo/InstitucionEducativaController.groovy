package catalogo



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*
import org.hibernate.criterion.CriteriaSpecification

@SpringUserIdentification
@Transactional(readOnly = true)
class InstitucionEducativaController {
    def springSecurityService
    def actionLoggingService

    static allowedMethods = [save: "POST", update: "PUT"]

    def index(Integer max) {
        def referencia = params?.referencia?.trim()
        
        params.max = Math.min(max ?: 10, 100)
        params.sort = "descripcion"
        params.order = "asc"
        
        def institucionesEducativas = []

        if (referencia) {
            institucionesEducativas = InstitucionEducativa.createCriteria().list(params) {
                createAlias("carrerasInstitucion", "ci", CriteriaSpecification.LEFT_JOIN)
                or {
                    ilike("clave", "%${referencia}%")
                    ilike("descripcion", "%${referencia}%")
                    ilike("ci.clave", "%${referencia}%")
                    ilike("ci.descripcion", "%${referencia}%")
                    ilike("ci.rvoe", "%${referencia}%")
                }
            }
            
            if (institucionesEducativas) {
                institucionesEducativas.unique()
            }
            
        } else {
            institucionesEducativas = InstitucionEducativa.list(params)
        }
        
                
        respond institucionesEducativas, model:[institucionEducativaInstanceCount: institucionesEducativas.totalCount]
    }

    def show(InstitucionEducativa institucionEducativaInstance) {
        respond institucionEducativaInstance
    }

    def create() {
        respond new InstitucionEducativa(params)
    }

    @ActionLogging
    @ActionType("Gestión de instituciones educativas")
    @CustomActionName("Registro de institución educativa")
    @PrintCustomLog
    @Transactional
    def save() {
        actionLoggingService.log("== Registro de institución educativa - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
                
        actionLoggingService.log("Estableciendo información de la institución educativa...")
        
        def institucionEducativaInstance = new InstitucionEducativa()
        
        institucionEducativaInstance.clave = params?.clave
        institucionEducativaInstance.descripcion = params?.descripcion
        
        actionLoggingService.log("Institución educativa: ${institucionEducativaInstance?.toPlainText("\t")}")
        
        if (institucionEducativaInstance.hasErrors()) {
            flash.error= institucionEducativaInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond institucionEducativaInstance.errors, view:'create'
            return
        }
        
        actionLoggingService.log("Guardando institución educativa...")
        
        if (institucionEducativaInstance.save(flush:true)){
            flash.message="Institución educativa registrada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect action: "show", id: institucionEducativaInstance?.id
        } else {
            flash.error= institucionEducativaInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "create"
        }
    }

    def edit(InstitucionEducativa institucionEducativaInstance) {
        respond institucionEducativaInstance
    }

    @ActionLogging
    @ActionType("Gestión de instituciones educativas")
    @CustomActionName("Modificación de institución educativa")
    @PrintCustomLog
    @Transactional
    def update() {
        actionLoggingService.log("== Modificación de institución educativa - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando institución educativa a modificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def institucionEducativaInstance = InstitucionEducativa.get(params.int("id"))
        
        actionLoggingService.log("Institución educativa (original): ${institucionEducativaInstance?.toPlainText("\t")}")
        
        if (!institucionEducativaInstance) {
            flash.error="Es necesario indicar la institución educativa a modificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Estableciendo información de la institución educativa...")
        
        institucionEducativaInstance.clave = params?.clave
        institucionEducativaInstance.descripcion = params?.descripcion
        
        if (institucionEducativaInstance.hasErrors()) {
            flash.error= institucionEducativaInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond institucionEducativaInstance.errors, view:'edit'
            return
        }
        
        actionLoggingService.log("Institución educativa (actualizada): ${institucionEducativaInstance?.toPlainText("\t")}")

        actionLoggingService.log("Guardando institución educativa...")
        
        if (institucionEducativaInstance.save(flush:true)){
            flash.message="Institución educativa modificada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect action: "show", id: institucionEducativaInstance?.id
        } else {
            flash.error= institucionEducativaInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "create"
        }
    }

    @ActionLogging
    @ActionType("Gestión de instituciones educativas")
    @CustomActionName("Eliminación de institución educativa")
    @PrintCustomLog
    @Transactional
    def delete(InstitucionEducativa institucionEducativaInstance) {
        actionLoggingService.log("== Eliminación de institución educativa - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando institución educativa a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (institucionEducativaInstance == null) {
            flash.error="Es necesario indicar la institución educativa a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Institución educativa: ${institucionEducativaInstance?.toPlainText("\t")}")

        actionLoggingService.log("Eliminando institución educativa...")
        
        institucionEducativaInstance.delete(flush:true)
        
        flash.message="Institución educativa eliminada correctamente"
            
        actionLoggingService.log("${flash.message}")

        redirect action: "index"
    }
    
    def carreras(InstitucionEducativa institucionEducativaInstance){
        render institucionEducativaInstance?.carrerasInstitucion as JSON
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = "Institución educativa no encontrada"
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
