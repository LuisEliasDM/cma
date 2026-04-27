package paquete



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*
import seguridad.UsuarioInstitucion
import grails.plugin.springsecurity.SpringSecurityUtils
import catalogo.InstitucionEducativa

@SpringUserIdentification
class ComprobantePagoController {
    def springSecurityService
    def actionLoggingService
    
    def capturarComprobante() {
        
        def paquete = Paquete.get(params.int("idPaquete"))
        
        if (!paquete) {
            flash.error="Es necesario indicar el paquete a capturar comprobante"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        if (paquete?.estatus?.id != 1) {
            flash.error="El paquete se encuentra en revisión/autenticación y no puede modificarse"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"consultarPaquete", id: paquete?.id
            return
        }
        
        respond new ComprobantePago(params)
    }

    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Registro de comprobante de pago")
    @PrintCustomLog
    @Transactional
    def guardarInformacion() {
        actionLoggingService.log("== Registro de comprobante de pago - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete...")
        
        def paquete = Paquete.get(params.int("paquete.id"))
        
        if (!paquete) {
            flash.error="Es necesario indicar el paquete para el comprobante de pago"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"capturarComprobante"
            return
        }
        
        if (paquete?.estatus?.id != 1) {
            flash.error="El paquete se encuentra en revisión/autenticación y no puede modificarse"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"consultarPaquete", id: paquete?.id
            return
        }
        
        actionLoggingService.log("Paquete: ${paquete?.toPlainText("\t")}")
                
        actionLoggingService.log("Estableciendo información del comprobante de pago...")
        
        def comprobantePagoInstance = new ComprobantePago()
        
        comprobantePagoInstance.paquete = paquete
        comprobantePagoInstance.poliza = params.poliza
        comprobantePagoInstance.monto = params.double("monto")
        
        actionLoggingService.log("Comprobante de pago: ${comprobantePagoInstance?.toPlainText("\t")}")

        if (comprobantePagoInstance.hasErrors()) {
            flash.error= comprobantePagoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond comprobantePagoInstance.errors, view:'capturarComprobante'
            return
        }
        
        actionLoggingService.log("Guardando comprobante de pago...")
        
        if (comprobantePagoInstance.save(flush:true)){
            flash.message="Comprobante de pago registrado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect controller: "paquete", action: "consultarPaquete", id: paquete?.id
        } else {
            flash.error= comprobantePagoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "create"
        }
    }

    def modificarComprobante(ComprobantePago comprobantePagoInstance) {
        
        if (!comprobantePagoInstance) {
            flash.error="Es necesario indicar el comprobante de pago modificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        if (comprobantePagoInstance?.paquete?.estatus?.id != 1) {
            flash.error = "El paquete se encuentra en revisión/autenticación y no puede modificarse"
            redirect controller: "paquete", action: "consultarPaquete", id: comprobantePagoInstance?.paquete?.id
            return
        }
        
        respond comprobantePagoInstance
    }

    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Modificación de comprobante de pago")
    @PrintCustomLog
    @Transactional
    def actualizarRegistro() {
        actionLoggingService.log("== Modificación de comprobante de pago - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando comprobante de pago a modificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def comprobantePagoInstance = ComprobantePago.get(params.int("id"))
        
        actionLoggingService.log("Comprobante de pago (original): ${comprobantePagoInstance?.toPlainText("\t")}")
        
        if (!comprobantePagoInstance) {
            flash.error="Es necesario indicar el comprobante de pago a modificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Consultando paquete...")
        
        def paquete = Paquete.get(params.int("paquete.id"))
        
        if (!paquete) {
            flash.error="Es necesario indicar el paquete para el comprobante de pago"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"modificarComprobante", id: comprobantePagoInstance.id
            return
        }
        
        if (paquete?.estatus?.id != 1) {
            flash.error="El paquete se encuentra en revisión/autenticación y no puede modificarse"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"consultarPaquete", id: paquete?.id
            return
        }
        
        actionLoggingService.log("Paquete: ${paquete?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del comprobante de pago...")
        
        comprobantePagoInstance.paquete = paquete
        comprobantePagoInstance.poliza = params.poliza
        comprobantePagoInstance.monto = params.double("monto")

        if (comprobantePagoInstance.hasErrors()) {
            flash.error= comprobantePagoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond comprobantePagoInstance.errors, view:'modificarComprobante'
            return
        }
        
        actionLoggingService.log("Comprobante de pago (actualizado): ${comprobantePagoInstance?.toPlainText("\t")}")

        actionLoggingService.log("Guardando comprobante de pago...")
        
        if (comprobantePagoInstance.save(flush:true)){
            flash.message="Comprobante de pago modificado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect controller: "paquete", action: "consultarPaquete", id: paquete?.id
        } else {
            flash.error= comprobantePagoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "create"
        }
    }

    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Eliminación de comprobante de pago")
    @PrintCustomLog
    @Transactional
    def eliminarComprobante(ComprobantePago comprobantePagoInstance) {
        actionLoggingService.log("== Eliminación de comprobante de pago - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando comprobante de pago a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (comprobantePagoInstance == null) {
            flash.error="Es necesario indicar el comprobante de pago a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        actionLoggingService.log("Comprobante de pago: ${comprobantePagoInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando paquete...")
        
        def paquete = Paquete.get(comprobantePagoInstance?.paquete?.id)
        
        if (!paquete) {
            flash.error="No se pudo recuperar el paquete del comprobante de pago"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"index"
            return
        }
        
        if (paquete?.estatus?.id != 1) {
            flash.error="El paquete se encuentra en revisión/autenticación y no puede modificarse"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "paquete", action:"consultarPaquete", id: paquete?.id
            return
        }
        
        actionLoggingService.log("Paquete: ${paquete?.toPlainText("\t")}")

        actionLoggingService.log("Eliminando comprobante de pago...")
        
        paquete.removeFromComprobantesPago(comprobantePagoInstance)

        comprobantePagoInstance.delete flush:true

        flash.message="Comprobante de pago eliminado correctamente"
            
        actionLoggingService.log("${flash.message}")

        redirect controller: "paquete", action: "consultarPaquete", id: paquete.id
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'comprobantePago.label', default: 'ComprobantePago'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
