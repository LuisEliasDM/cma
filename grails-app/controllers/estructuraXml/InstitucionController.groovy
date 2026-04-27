package estructuraXml



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class InstitucionController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Institucion.list(params), model:[institucionInstanceCount: Institucion.count()]
    }

    def show(Institucion institucionInstance) {
        respond institucionInstance
    }

    def create() {
        respond new Institucion(params)
    }

    @Transactional
    def save(Institucion institucionInstance) {
        if (institucionInstance == null) {
            notFound()
            return
        }

        if (institucionInstance.hasErrors()) {
            respond institucionInstance.errors, view:'create'
            return
        }

        institucionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'institucion.label', default: 'Institucion'), institucionInstance.id])
                redirect institucionInstance
            }
            '*' { respond institucionInstance, [status: CREATED] }
        }
    }

    def edit(Institucion institucionInstance) {
        respond institucionInstance
    }

    @Transactional
    def update(Institucion institucionInstance) {
        if (institucionInstance == null) {
            notFound()
            return
        }

        if (institucionInstance.hasErrors()) {
            respond institucionInstance.errors, view:'edit'
            return
        }

        institucionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Institucion.label', default: 'Institucion'), institucionInstance.id])
                redirect institucionInstance
            }
            '*'{ respond institucionInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Institucion institucionInstance) {

        if (institucionInstance == null) {
            notFound()
            return
        }

        institucionInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Institucion.label', default: 'Institucion'), institucionInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'institucion.label', default: 'Institucion'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
