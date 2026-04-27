package estructuraXml


import org.mirzsoft.grails.actionlogging.annotation.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
@Transactional(readOnly = true)
class AutenticacionController {
    def firmaElectronicaService
    def actionLoggingService
    def springSecurityService
    def tituloElectronicoService
    def paqueteService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(TituloElectronico tituloElectronicoInstance) {
        respond tituloElectronicoInstance
    }

    def consultarAutenticacion(Autenticacion autenticacionInstance) {
        [
            autenticacionInstance: autenticacionInstance,
            tituloElectronicoInstance: autenticacionInstance?.tituloElectronico
        ]
    }

    def capturarAutenticacion(TituloElectronico tituloElectronicoInstance){
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        if (!tituloElectronicoInstance) {
            flash.error="Seleccione un titulo"
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "autenticarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        /*if (tituloElectronicoInstance?.registros) {
            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles) {
                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles?.last()?.estatus[0] == 1) {
                    flash.error = "El titulo electronico ya se encuentra REGISTRADO ANTE DGP y no puede modificarse"
                    redirect action: "index", id: tituloElectronicoInstance?.id
                    return
                }
            }
        }
                
        if (!tituloElectronicoInstance?.firmaResponsables?.firmaResponsable) {
            flash.error="No hay firmas registradas"
            respond tituloElectronicoInstance, view:'index', model: [tituloElectronicoInstance:tituloElectronicoInstance]
            return
        }*/
        
        if (!tituloElectronicoInstance?.carrera?.idAutorizacionReconocimiento in [1,2,3,4]) {
            flash.error="El tipo de reconocimiento no requiere autenticación"
            respond tituloElectronicoInstance, view:'index', model: [tituloElectronicoInstance:tituloElectronicoInstance]
            return
        }
        
        def firmasElectronicas = firmaElectronicaService.consultarFirmas(null, usuario)
        
        def estatusEFirma = firmaElectronicaService.consultarEstatusFirmas(null)
        
        respond tituloElectronicoInstance, model: [autenticacionInstance: new Autenticacion(), firmasElectronicas: firmasElectronicas, estatusEFirma: estatusEFirma]
    }

    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Registro de autenticación (autoridad estatal)")
    @PrintCustomLog
    @Transactional
    def guardarInformacion(TituloElectronico tituloElectronicoInstance) {
        actionLoggingService.log("== Registro de autenticación (autoridad estatal) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico a firmar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!tituloElectronicoInstance) {
            flash.error="Seleccione un titulo"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "autenticarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        /*if (tituloElectronicoInstance?.registros) {
            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles) {
                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles?.last()?.estatus[0] == 1) {
                    flash.error = "El título electrónico ya se encuentra REGISTRADO ANTE DGP y no puede modificarse"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    redirect action: "index", id: tituloElectronicoInstance?.id
                    return
                }
            }
        }*/
                        
        def autenticacionInstance = tituloElectronicoInstance?.autenticacion
        if(!autenticacionInstance) {
            autenticacionInstance = new Autenticacion()
        }
        
        def firmasElectronicas = FirmaElectronica.createCriteria().list {
            and {
                institucionEducativa {
                    eq("clave", tituloElectronicoInstance?.institucion?.cveInstitucion)
                }
                eq("activo", true)
            }
        }
        
        actionLoggingService.log("Consultando e.firma...")
        
        actionLoggingService.log("Id: ${params?.idFirmaElectronica}")
        
        def firmaElectronica = FirmaElectronica.get(params.int("idFirmaElectronica"))
        
        if(!firmaElectronica) {
            flash.error = "Es necesario seleccionar una e.firma"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("E.Firma: ${firmaElectronica?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando estatus de la e.firma...")
        
        if(!firmaElectronica.activo) {
            flash.error = "La e.firma se encuentra inactiva"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Consultando fechas de vencimiento de la e.firma...")
        if(new Date() > firmaElectronica?.validoHastaCer) {
            flash.error = "La e.firma ha caducado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Estableciendo información de la autenticación...")
        
        autenticacionInstance?.folioDigital = firmaElectronicaService.generarUuid()
        autenticacionInstance?.fechaAutenticacion = new Date()
        
        def selloTitulo = ""
        tituloElectronicoInstance?.firmaResponsables?.firmaResponsable?.each { firma ->
            selloTitulo += firma?.sello + "|"
        }
        autenticacionInstance?.selloTitulo = selloTitulo
                
        actionLoggingService.log("Autenticando título electrónico...")
        
        //Firma electrónica
        def sello
        def certificado
        def noCertificado
                        
        def certificadoBytes
        if (firmaElectronica?.archivoCer){
            certificadoBytes = firmaElectronica?.archivoCer
        } else {
            def certificadoCer = request.getFile("certificadoCer")
            if (!certificadoCer.empty) {
                if (certificadoCer?.originalFilename?.toLowerCase()?.endsWith(".cer")) {
                    certificadoBytes = certificadoCer?.getBytes()
                } else {
                    flash.error="El archivo de certificado es incorrecto"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
                    
                    return
                }
            } else {
                flash.error="Es necesario seleccionar el archivo de certificado .cer"
                    
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
                
                return
            }
        }
        
        if (certificadoBytes) {
            certificado = firmaElectronicaService.bytesToBase64(certificadoBytes)
            noCertificado = firmaElectronica?.numeroSerieCer
        } else {
            flash.error="Es necesario definir el archivo de certificado .cer"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            
            return
        }
                
        autenticacionInstance?.noCertificadoAutoridad = noCertificado
        autenticacionInstance?.cadenaOriginalSelloAutenticacion = firmaElectronicaService.generarCadenaOriginalAutenticacion(autenticacionInstance)
        
        def clavePrivadaBytes
        if (firmaElectronica?.archivoKey){
            clavePrivadaBytes = firmaElectronica?.archivoKey
        } else {
            def clavePrivada = request.getFile("clavePrivada")
            if (!clavePrivada.empty) {
                if (clavePrivada?.originalFilename?.toLowerCase()?.endsWith(".key")) {
                    clavePrivadaBytes = clavePrivada?.getBytes()
                } else {
                    flash.error="El archivo de clave privada es incorrecto"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id

                    return
                }
            } else {
                flash.error="Es necesario seleccionar el archivo de clave privada"
                
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
                
                return
            }
        }
        
        if (clavePrivadaBytes) {
            sello = firmaElectronicaService.firmar(clavePrivadaBytes, params.contrasenaClavePrivada, autenticacionInstance?.cadenaOriginalSelloAutenticacion)
        } else {
            flash.error="Es necesario definir el archivo de clave privada"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            
            return
        }
        
        autenticacionInstance?.selloAutenticacion = sello 
        
        if (!sello) {
            flash.error="La contraseña no corresponde al archivo de clave privada (.key). Favor de verificar la información"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            
            return
        }
        
        if (!certificado || !noCertificado) {
            flash.error="No se pudo realizar la firma electrónica. Favor de verificar la información"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            
            return
        }
                        
        if (autenticacionInstance.hasErrors()) {
            flash.error = autenticacionInstance?.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            
            return
        }
        
        tituloElectronicoInstance?.autenticacion = autenticacionInstance
        
        actionLoggingService.log("Autenticación: ${autenticacionInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando autenticación...")
        
        if(tituloElectronicoInstance.save(flush:true)) {
            flash.message="Autenticación registrada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            paqueteService?.verificarAutenticacionCompleta(tituloElectronicoInstance.paquete)
            
            redirect(action:"index", id: tituloElectronicoInstance?.id)
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarAutenticacion", id: tituloElectronicoInstance?.id
            
            return
        }
    }

    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Eliminación de autenticación (autoridad estatal)")
    @PrintCustomLog
    @Transactional
    def eliminarAutenticacion(Autenticacion autenticacionInstance) {
        actionLoggingService.log("== Eliminación de autenticación (autoridad estatal) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando autenticación a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!autenticacionInstance) {
            flash.error="Autenticación no encontrada"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Autenticación: ${autenticacionInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico...")
                
        def tituloElectronicoInstance = autenticacionInstance?.tituloElectronico
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "autenticarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        /*if (tituloElectronicoInstance?.registros) {
            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles) {
                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles?.last()?.estatus[0] == 1) {
                    flash.error = "El titulo electronico ya se encuentra REGISTRADO ANTE DGP y no puede modificarse"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    redirect action: "index", id: tituloElectronicoInstance?.id
                    return
                }
            }
        }*/
        
        actionLoggingService.log("Eliminando autenticación...")

        tituloElectronicoInstance?.autenticacion = null
        autenticacionInstance.delete(flush:true)
        paqueteService?.verificarAutenticacionCompleta(tituloElectronicoInstance.paquete)
        
        if(!autenticacionInstance.hasErrors()) {
            flash.message="Autenticación eliminada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect(action:"index", id: tituloElectronicoInstance?.id)
        } else {
            flash.error="No se pudo eliminar la autenticación"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(action:"index", id: tituloElectronicoInstance?.id)
        }
    }
}
