package swdgp

import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
class ArchivoResultadoController {
    def firmaElectronicaService
    def DGPService
    def springSecurityService
    def actionLoggingService

    def index() { }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Descarga del archivo de resultado del proceso de registro (DGP)")
    @PrintCustomLog
    def descargar(ArchivoResultado archivoResultadoInstance){
        actionLoggingService.log("== Descarga del archivo de resultado del proceso de registro (DGP) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando archivo de resultado a descargar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!archivoResultadoInstance) {
            flash.error="Seleccione un archivo de resultado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Archivo de resultado: ${archivoResultadoInstance?.toPlainText("\t")}")
        
        if (!archivoResultadoInstance.base64) {
            flash.error="El archivo no se pudo recuperar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "registro", action:"index", id: archivoResultadoInstance.registro.tituloElectronico.id)
            return
        }
        
        actionLoggingService.log("Consultando título electrónico...")
        
        if (!archivoResultadoInstance?.registro?.tituloElectronico) {
            flash.error="No se pudo recuperar el titulo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${archivoResultadoInstance?.registro?.tituloElectronico?.toPlainText("\t")}")
                
        actionLoggingService.log("Descargando archivo de resultado...")
                
        render file: firmaElectronicaService.base64toBytes(archivoResultadoInstance.base64), fileName: archivoResultadoInstance.nombre, contentType: "application/zip"
    }
}
