package estructuraXml



import grails.test.mixin.*
import spock.lang.*

@TestFor(CarreraController)
@Mock(Carrera)
class CarreraControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.carreraInstanceList
            model.carreraInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.carreraInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def carrera = new Carrera()
            carrera.validate()
            controller.save(carrera)

        then:"The create view is rendered again with the correct model"
            model.carreraInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            carrera = new Carrera(params)

            controller.save(carrera)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/carrera/show/1'
            controller.flash.message != null
            Carrera.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def carrera = new Carrera(params)
            controller.show(carrera)

        then:"A model is populated containing the domain instance"
            model.carreraInstance == carrera
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def carrera = new Carrera(params)
            controller.edit(carrera)

        then:"A model is populated containing the domain instance"
            model.carreraInstance == carrera
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/carrera/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def carrera = new Carrera()
            carrera.validate()
            controller.update(carrera)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.carreraInstance == carrera

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            carrera = new Carrera(params).save(flush: true)
            controller.update(carrera)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/carrera/show/$carrera.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/carrera/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def carrera = new Carrera(params).save(flush: true)

        then:"It exists"
            Carrera.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(carrera)

        then:"The instance is deleted"
            Carrera.count() == 0
            response.redirectedUrl == '/carrera/index'
            flash.message != null
    }
}
