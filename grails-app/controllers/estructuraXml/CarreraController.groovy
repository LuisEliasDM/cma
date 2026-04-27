package estructuraXml

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CarreraController 
{
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
          

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Carrera.list(params), model:[carreraInstanceCount: Carrera.count()]
    }

    def show(Carrera carreraInstance) {
        respond carreraInstance
    }

    def create() {
        respond new Carrera(params)
    }

    @Transactional
    def save(Carrera carreraInstance) {
        if (carreraInstance == null) {
            notFound()
            return
        }

        if (carreraInstance.hasErrors()) {
            respond carreraInstance.errors, view:'create'
            return
        }

        carreraInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'carrera.label', default: 'Carrera'), carreraInstance.id])
                redirect carreraInstance
            }
            '*' { respond carreraInstance, [status: CREATED] }
        }
    }

    def edit(Carrera carreraInstance) {
        respond carreraInstance
    }

    @Transactional
    def update(Carrera carreraInstance) {
        if (carreraInstance == null) {
            notFound()
            return
        }

        if (carreraInstance.hasErrors()) {
            respond carreraInstance.errors, view:'edit'
            return
        }

        carreraInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Carrera.label', default: 'Carrera'), carreraInstance.id])
                redirect carreraInstance
            }
            '*'{ respond carreraInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Carrera carreraInstance) {

        if (carreraInstance == null) {
            notFound()
            return
        }

        carreraInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Carrera.label', default: 'Carrera'), carreraInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'carrera.label', default: 'Carrera'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
