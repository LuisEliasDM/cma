package swdgp

import catalogo.MotivoCancelacion
import estructuraXml.RepresentacionGrafica
import catalogo.FormatoRepresentacionGrafica
import estructuraXml.TituloElectronico
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
class RegistroController {
    def DGPService
    def firmaElectronicaService
    def springSecurityService
    def actionLoggingService
    def tituloElectronicoService
    def paqueteService

    def index(TituloElectronico tituloElectronicoInstance, Integer max) {
        params.max = Math.min(max ?: 1, 100)
        params.sort = "id"
        params.order = "desc"
        def registros = Registro.findAllByTituloElectronico(tituloElectronicoInstance, params)
        respond registros, model:[registros: registros, tituloElectronicoInstance: tituloElectronicoInstance]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Envío de título electrónico a proceso de registro (DGP)")
    @PrintCustomLog
    def cargarTituloElectronico(TituloElectronico tituloElectronicoInstance){
        actionLoggingService.log("== Envío de título electrónico a proceso de registro (DGP) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico a registrar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!tituloElectronicoInstance) {
            flash.error="Seleccione un titulo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "registrarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
//        if (tituloElectronicoInstance?.registros) {
//            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles) {
//                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles?.last()?.estatus[0] == 1) {
//                    flash.error = "El título electrónico ya se encuentra REGISTRADO ANTE DGP"
//                    
//                    actionLoggingService.log("Error: ${flash.error}")
//                    
//                    redirect action: "index", id: tituloElectronicoInstance?.id
//                    return
//                }
//            }
//        }
        
//        actionLoggingService.log("Consultando firmas electrónicas (institución educativa)...")
//                
//        if (!tituloElectronicoInstance?.firmaResponsables?.firmaResponsable) {
//            flash.error="No hay firmas registradas"
//            
//            actionLoggingService.log("Error: ${flash.error}")
//            
//            redirect action: "index", id: tituloElectronicoInstance?.id
//            return
//        }
                
        /*
         * No existe el mecanismo de autenticacion federal
         * 
         * if (tituloElectronicoInstance?.carrera?.idAutorizacionReconocimiento in [1,2,3,4] && !tituloElectronicoInstance?.autenticacion) {
            flash.error="El tipo de reconocimiento requiere autenticación"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index", id: tituloElectronicoInstance?.id
            return
        }*/
        
        actionLoggingService.log("Registrando título electrónico ante DGP...")
        
        def resultado = DGPService.cargarTituloElectronico(tituloElectronicoInstance)        
        
        actionLoggingService.log("Registro: ${resultado?.registro?.toPlainText("\t")}")
        
        if (resultado.estatus) {            
            flash.message=resultado.mensaje
            
            actionLoggingService.log("${flash.message}")
            
        } else {
            flash.error=resultado.mensaje
            
            actionLoggingService.log("Error: ${flash.error}")
            
        }
        
        redirect(action:"index", id: tituloElectronicoInstance?.id)
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Consulta del proceso de registro de título electrónico (DGP)")
    @PrintCustomLog
    def consultarProcesoTituloElectronico(Registro registroInstance){
        actionLoggingService.log("== Consulta del proceso de registro de título electrónico (DGP) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando solicitud de registro a verificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!registroInstance) {
            flash.error="Seleccione una solicitud de registro"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Solicitud de registro: ${registroInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico...")
        
        if (!registroInstance.tituloElectronico) {
            flash.error="No se pudo recuperar el titulo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${registroInstance.tituloElectronico?.toPlainText("\t")}")
                
        actionLoggingService.log("Consultando proceso de registro ante DGP...")
        
        def resultado = DGPService.consultarProcesoTituloElectronico(registroInstance)
        
        actionLoggingService.log("Consulta: ${resultado?.consulta?.toPlainText("\t")}")
        
        if (resultado.estatus) {
            flash.message=resultado.mensaje
            
            actionLoggingService.log("${flash.message}")
            
        } else {
            flash.error=resultado.mensaje
            
            actionLoggingService.log("${flash.error}")
            
        }
        
        redirect(action:"index", id: registroInstance.tituloElectronico.id)
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Consulta del archivo de resultado del proceso de registro (DGP)")
    @PrintCustomLog
    def descargarTituloElectronico(Registro registroInstance){
        actionLoggingService.log("== Consulta del archivo de resultado del proceso de registro (DGP) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando solicitud de registro a verificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!registroInstance) {
            flash.error="Seleccione una solicitud de registro"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Solicitud de registro: ${registroInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico...")
        
        if (!registroInstance.tituloElectronico) {
            flash.error="No se pudo recuperar el titulo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${registroInstance.tituloElectronico?.toPlainText("\t")}")
                
        
        if (registroInstance.consultas.size() == 0) {
            flash.error="Es necesario realizar la consulta del proceso de registro"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "Registro", action:"index", id: registroInstance.tituloElectronico.id)
            return
        }
        
        if (registroInstance.consultas.last().estatusLote != 1) {
            flash.error="Es necesario realizar la consulta del proceso de registro y que este haya sido correcto"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "Registro", action:"index", id: registroInstance.tituloElectronico.id)
            return
        }
        
        actionLoggingService.log("Consultando archivo de resultado de registro ante DGP...")
        
        def resultado = DGPService.descargarTituloElectronico(registroInstance)
        
        actionLoggingService.log("ArchivoResultado: ${resultado?.archivo?.toPlainText("\t")}")
        
        if (resultado.estatus) {
            flash.message=resultado.mensaje
            
            actionLoggingService.log("${flash.message}")
            
            paqueteService?.verificarRegistroAnteDGPCompleto(registroInstance.tituloElectronico.paquete)
            
            resultado?.archivo?.detalles?.each {
                if (it?.estatus == 1) {
                    actionLoggingService.log("Generando representación gráfica...")
                    def formato = FormatoRepresentacionGrafica.findByActivo(true,[sort: "id", order: "desc", max: 1])
                    
                    if (formato) {
                        def codigo = tituloElectronicoService.generarCodigoRepresentacion()
                        def representacion = new RepresentacionGrafica(
                            tituloElectronico: registroInstance.tituloElectronico,
                            formato: formato,
                            codigo: codigo,
                            descargado: false,
                            activo: true
                        )
                        
                        if (!representacion.save(flush: true)) {
                            actionLoggingService.log("Error: No se pudo generar la representación gráfica")
                            actionLoggingService.log("${representacion.errors}")
                        }
                        
                    } else {
                        actionLoggingService.log("Error: Formato de representación gráfica no encontrado")
                    }
                    
                    actionLoggingService.log("Enviando correo electrónico de notificación...")
                    tituloElectronicoService.enviarNotificacionRegistroExitoso(registroInstance.tituloElectronico, registroInstance.dateCreated)
                }
            }
            
            
        } else {
            flash.error=resultado.mensaje
            actionLoggingService.log("${flash.error}")
        }
        
        redirect(action:"index", id: registroInstance.tituloElectronico.id)
    }
    
    def cancelacionTituloElectronico(Registro registroInstance){
        if (!registroInstance) {
            flash.error="Seleccione una solicitud de registro"
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        [tituloElectronicoInstance: registroInstance.tituloElectronico, registroInstance: registroInstance]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Cancelación de registro de título electrónico (DGP)")
    @PrintCustomLog
    def cancelarTituloElectronico(Registro registroInstance, MotivoCancelacion motivoCancelacionInstance){
        actionLoggingService.log("== Cancelación de registro de título electrónico (DGP) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando solicitud de registro a cancelar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!registroInstance) {
            flash.error="Seleccione una solicitud de registro"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
                
        if (!motivoCancelacionInstance) {
            flash.error="Seleccione el motivo de cancelación"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Consultando título electrónico...")
        
        if (!registroInstance.tituloElectronico) {
            flash.error="No se pudo recuperar el titulo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${registroInstance.tituloElectronico?.toPlainText("\t")}")
        
        def permiso = tituloElectronicoService.permisoTitulo(registroInstance.tituloElectronico, "cancelarRegistroTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: registroInstance.tituloElectronico?.id
            return
        }
        
        actionLoggingService.log("Cancelando registro de título electrónico ante DGP...")
        
        def resultado = DGPService.cancelarTituloElectronico(registroInstance, motivoCancelacionInstance)
        
        actionLoggingService.log("Canelación: ${resultado?.cancelacion?.toPlainText("\t")}")
        
        if (resultado.estatus) {
            flash.message=resultado.mensaje
            
            actionLoggingService.log("${flash.message}")
            
        } else {
            flash.error=resultado.mensaje
            
            actionLoggingService.log("${flash.error}")
            
        }
        
        redirect(action:"index", id: registroInstance.tituloElectronico.id)
        
    } 
}
