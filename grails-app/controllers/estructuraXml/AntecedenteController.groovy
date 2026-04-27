package estructuraXml



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AntecedenteController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Antecedente.list(params), model:[antecedenteInstanceCount: Antecedente.count()]
    }

    def show(Antecedente antecedenteInstance) {
        respond antecedenteInstance
    }

    def create() {
        respond new Antecedente(params)
    }

    @Transactional
    def save(Antecedente antecedenteInstance) {
        if (antecedenteInstance == null) {
            notFound()
            return
        }

        if (antecedenteInstance.hasErrors()) {
            respond antecedenteInstance.errors, view:'create'
            return
        }

        antecedenteInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'antecedente.label', default: 'Antecedente'), antecedenteInstance.id])
                redirect antecedenteInstance
            }
            '*' { respond antecedenteInstance, [status: CREATED] }
        }
    }

    def edit(Antecedente antecedenteInstance) {
        respond antecedenteInstance
    }

    @Transactional
    def update(Antecedente antecedenteInstance) {
        if (antecedenteInstance == null) {
            notFound()
            return
        }

        if (antecedenteInstance.hasErrors()) {
            respond antecedenteInstance.errors, view:'edit'
            return
        }

        antecedenteInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Antecedente.label', default: 'Antecedente'), antecedenteInstance.id])
                redirect antecedenteInstance
            }
            '*'{ respond antecedenteInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Antecedente antecedenteInstance) {

        if (antecedenteInstance == null) {
            notFound()
            return
        }

        antecedenteInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Antecedente.label', default: 'Antecedente'), antecedenteInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'antecedente.label', default: 'Antecedente'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
