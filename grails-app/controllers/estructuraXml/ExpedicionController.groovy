package estructuraXml



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ExpedicionController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Expedicion.list(params), model:[expedicionInstanceCount: Expedicion.count()]
    }

    def show(Expedicion expedicionInstance) {
        respond expedicionInstance
    }

    def create() {
        respond new Expedicion(params)
    }

    @Transactional
    def save(Expedicion expedicionInstance) {
        if (expedicionInstance == null) {
            notFound()
            return
        }

        if (expedicionInstance.hasErrors()) {
            respond expedicionInstance.errors, view:'create'
            return
        }

        expedicionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'expedicion.label', default: 'Expedicion'), expedicionInstance.id])
                redirect expedicionInstance
            }
            '*' { respond expedicionInstance, [status: CREATED] }
        }
    }

    def edit(Expedicion expedicionInstance) {
        respond expedicionInstance
    }

    @Transactional
    def update(Expedicion expedicionInstance) {
        if (expedicionInstance == null) {
            notFound()
            return
        }

        if (expedicionInstance.hasErrors()) {
            respond expedicionInstance.errors, view:'edit'
            return
        }

        expedicionInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Expedicion.label', default: 'Expedicion'), expedicionInstance.id])
                redirect expedicionInstance
            }
            '*'{ respond expedicionInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Expedicion expedicionInstance) {

        if (expedicionInstance == null) {
            notFound()
            return
        }

        expedicionInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Expedicion.label', default: 'Expedicion'), expedicionInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'expedicion.label', default: 'Expedicion'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
