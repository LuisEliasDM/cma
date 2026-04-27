package estructuraXml



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProfesionistaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Profesionista.list(params), model:[profesionistaInstanceCount: Profesionista.count()]
    }

    def show(Profesionista profesionistaInstance) {
        respond profesionistaInstance
    }

    def create() {
        respond new Profesionista(params)
    }

    @Transactional
    def save(Profesionista profesionistaInstance) {
        if (profesionistaInstance == null) {
            notFound()
            return
        }

        if (profesionistaInstance.hasErrors()) {
            respond profesionistaInstance.errors, view:'create'
            return
        }

        profesionistaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'profesionista.label', default: 'Profesionista'), profesionistaInstance.id])
                redirect profesionistaInstance
            }
            '*' { respond profesionistaInstance, [status: CREATED] }
        }
    }

    def edit(Profesionista profesionistaInstance) {
        respond profesionistaInstance
    }

    @Transactional
    def update(Profesionista profesionistaInstance) {
        if (profesionistaInstance == null) {
            notFound()
            return
        }

        if (profesionistaInstance.hasErrors()) {
            respond profesionistaInstance.errors, view:'edit'
            return
        }

        profesionistaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Profesionista.label', default: 'Profesionista'), profesionistaInstance.id])
                redirect profesionistaInstance
            }
            '*'{ respond profesionistaInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Profesionista profesionistaInstance) {

        if (profesionistaInstance == null) {
            notFound()
            return
        }

        profesionistaInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Profesionista.label', default: 'Profesionista'), profesionistaInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'profesionista.label', default: 'Profesionista'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
