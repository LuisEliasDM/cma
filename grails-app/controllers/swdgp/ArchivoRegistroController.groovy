package swdgp

import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
class ArchivoRegistroController {
    def firmaElectronicaService
    def DGPService
    def springSecurityService
    def actionLoggingService

    def index() { }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Descarga del archivo enviado a proceso de registro (DGP)")
    @PrintCustomLog
    def descargar(ArchivoRegistro archivoRegistroInstance){
        actionLoggingService.log("== Descarga del archivo enviado a proceso de registro (DGP) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando archivo de registro a descargar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!archivoRegistroInstance) {
            flash.error="Seleccione un archivo de registro"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Archivo de registro: ${archivoRegistroInstance?.toPlainText("\t")}")
        
        if (!archivoRegistroInstance.base64) {
            flash.error="El archivo no se pudo recuperar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "registro", action:"index", id: archivoRegistroInstance.registro.tituloElectronico.id)
            return
        }
        
        actionLoggingService.log("Consultando título electrónico...")
        
        if (!archivoRegistroInstance?.registro?.tituloElectronico) {
            flash.error="No se pudo recuperar el titulo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${archivoRegistroInstance?.registro?.tituloElectronico?.toPlainText("\t")}")
                
        actionLoggingService.log("Descargando archivo de registro...")
        
        render file: firmaElectronicaService.base64toBytes(archivoRegistroInstance.base64), fileName: archivoRegistroInstance.nombre
    }
}
