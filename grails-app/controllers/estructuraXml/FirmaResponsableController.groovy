package estructuraXml


import org.mirzsoft.grails.actionlogging.annotation.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import catalogo.*
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
@Transactional(readOnly = true)
class FirmaResponsableController {
    def springSecurityService
    def firmaElectronicaService
    def actionLoggingService
    def tituloElectronicoService
    
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(TituloElectronico tituloElectronicoInstance){
        respond tituloElectronicoInstance
    }
    
    def consultarFirma(FirmaResponsable firmaResponsableInstance) {
        [
            firmaResponsableInstance: firmaResponsableInstance,
            tituloElectronicoInstance: firmaResponsableInstance?.firmaResponsables?.tituloElectronico
        ]
    }
    
    def capturarFirma(TituloElectronico tituloElectronicoInstance){
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        if (!tituloElectronicoInstance) {
            flash.error="Seleccione un titulo"
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "modificarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
//        if (tituloElectronicoInstance?.registros) {
//            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles) {
//                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles?.last()?.estatus[0] == 1) {
//                    flash.error = "El titulo electronico ya se encuentra REGISTRADO ANTE DGP y no puede modificarse"
//                    redirect action: "index", id: tituloElectronicoInstance?.id
//                    return
//                }
//            }
//        }
//        
//        if (tituloElectronicoInstance?.autenticacion) {
//            flash.error = "El titulo electronico ya se encuentra AUTENTICADO y no puede modificarse"
//            redirect action: "index", id: tituloElectronicoInstance?.id
//            return
//        }
        
        def firmasElectronicas = firmaElectronicaService.consultarFirmas(tituloElectronicoInstance?.institucion?.cveInstitucion, usuario)
        
        def estatusEFirma = firmaElectronicaService.consultarEstatusFirmas(tituloElectronicoInstance?.institucion?.cveInstitucion)
                
        respond tituloElectronicoInstance, model: [firmasElectronicas: firmasElectronicas, estatusEFirma: estatusEFirma]
    }

    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Registro de firma electrónica (institución educativa)")
    @PrintCustomLog
    @Transactional
    def guardarInformacion(TituloElectronico tituloElectronicoInstance) {
        actionLoggingService.log("== Registro de firma electrónica (institución educativa) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico a firmar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!tituloElectronicoInstance) {
            flash.error="Es necesario indicar el título electrónico a firmar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "modificarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
//        actionLoggingService.log("Consultando registro ante DGP...")
//        
//        if (tituloElectronicoInstance?.registros) {
//            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles) {
//                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles?.last()?.estatus[0] == 1) {
//                    flash.error = "El título electrónico ya se encuentra REGISTRADO ANTE DGP y no puede modificarse"
//                    
//                    actionLoggingService.log("Error: ${flash.error}")
//                    
//                    redirect action: "index", id: tituloElectronicoInstance?.id
//                    return
//                }
//            }
//        }
//        
//        actionLoggingService.log("Consultando autenticación...")
//        
//        if (tituloElectronicoInstance?.autenticacion) {
//            flash.error = "El título electrónico ya se encuentra AUTENTICADO y no puede modificarse"
//            
//            actionLoggingService.log("Error: ${flash.error}")
//            
//            redirect action: "index", id: tituloElectronicoInstance?.id
//            return
//        }
        
        def firmasElectronicas = FirmaElectronica.createCriteria().list {
            and {
                institucionEducativa {
                    eq("clave", tituloElectronicoInstance?.institucion?.cveInstitucion)
                }
                eq("activo", true)
            }
        }        
                        
        if(!tituloElectronicoInstance?.firmaResponsables) {
            tituloElectronicoInstance.firmaResponsables = new FirmaResponsables()
        }
        
        actionLoggingService.log("Consultando e.firma...")
        
        actionLoggingService.log("Id: ${params?.idFirmaElectronica}")
        
        def firmaElectronica = FirmaElectronica.get(params.int("idFirmaElectronica"))
        
        if(!firmaElectronica) {
            flash.error = "Es necesario seleccionar una e.firma"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("E.Firma: ${firmaElectronica?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando estatus de la e.firma...")
        if(!firmaElectronica.activo) {
            flash.error = "La e.firma se encuentra inactiva"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Consultando fechas de vencimiento de la e.firma...")
        if(new Date() > firmaElectronica?.validoHastaCer) {
            flash.error = "La e.firma ha caducado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
        
        
        actionLoggingService.log("Consultando e.firma utilizada anteriormente...")
        def firmaExistente = FirmaResponsable.createCriteria().get {
            projections {
                count("noCertificadoResponsable")
            }
            eq("firmaResponsables",tituloElectronicoInstance?.firmaResponsables)
            eq("noCertificadoResponsable", firmaElectronica?.numeroSerieCer)
        }
        
        if (firmaExistente > 0) {
            flash.error = "La e.firma seleccionada ya se encuentra registrada y no se puede repetir."
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Estableciendo información de la firma electrónica...")
        
        def firmaResponsables = tituloElectronicoInstance?.firmaResponsables
        
        def firmaResponsableInstance = new FirmaResponsable()
        firmaResponsableInstance.nombre = firmaElectronica?.nombre
        firmaResponsableInstance.primerApellido = firmaElectronica?.primerApellido
        firmaResponsableInstance.segundoApellido = firmaElectronica?.segundoApellido
        firmaResponsableInstance.curp = firmaElectronica?.curp
        firmaResponsableInstance.idCargo = firmaElectronica?.cargoFirmante?.id
        firmaResponsableInstance.cargo = firmaElectronica?.cargoFirmante?.descripcion
        firmaResponsableInstance.abrTitulo = firmaElectronica?.abreviaturaTitulo?.descripcion
        
        firmaResponsableInstance?.cadenaOriginal = firmaElectronicaService.generarCadenaOriginalTitulo(tituloElectronicoInstance, firmaResponsableInstance)
        
        actionLoggingService.log("Firmando título electrónico...")
        
        //Firma electrónica
        def sello
        def certificado
        def noCertificado
        
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
                    
                    redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
                    return
                }
            } else {
                flash.error="Es necesario seleccionar el archivo de clave privada"
                
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
                return
            }
        }
        
        if (clavePrivadaBytes) {
            sello = firmaElectronicaService.firmar(clavePrivadaBytes, params.contrasenaClavePrivada, firmaResponsableInstance?.cadenaOriginal)
        } else {
            flash.error="Es necesario definir el archivo de clave privada"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
        
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
                    
                    redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
                    return
                }
            } else {
                flash.error="Es necesario seleccionar el archivo de certificado .cer"
                
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
                return
            }
        }
        
        if (certificadoBytes) {
            certificado = firmaElectronicaService.bytesToBase64(certificadoBytes)
            noCertificado = firmaElectronica?.numeroSerieCer
        } else {
            flash.error="Es necesario definir el archivo de certificado .cer"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
        
        if (!sello) {
            flash.error="La contraseña no corresponde al archivo de clave privada (.key). Favor de verificar la información"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
        
        if (!certificado || !noCertificado) {
            flash.error="No se pudo realizar la firma electrónica. Favor de verificar la información"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
        
        firmaResponsableInstance?.sello = sello
        firmaResponsableInstance?.certificadoResponsable = certificado
        firmaResponsableInstance?.noCertificadoResponsable = noCertificado
        
        firmaResponsables?.addToFirmaResponsable(firmaResponsableInstance)

        if (firmaResponsableInstance.hasErrors()) {
            
            flash.error= firmaResponsableInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
        
        def autenticacionInstance = tituloElectronicoInstance?.autenticacion
        if (autenticacionInstance) {
            tituloElectronicoInstance?.autenticacion = null
            autenticacionInstance.delete(flush:true)
        }
        
        actionLoggingService.log("Firma de responsable: ${firmaResponsableInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando firma electrónica de responsable...")
        
        if(tituloElectronicoInstance.save(flush:true)){
            flash.message="Firma electrónica registrada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect(action:"index", id: tituloElectronicoInstance?.id)
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "capturarFirma", id: tituloElectronicoInstance?.id
            return
        }
    }

    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Eliminación de firma electrónica (institución educativa)")
    @PrintCustomLog
    @Transactional
    def eliminarFirma(FirmaResponsable firmaResponsableInstance) {
        actionLoggingService.log("== Eliminación de firma electrónica (institución educativa) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando firma electrónica a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")

        if (!firmaResponsableInstance) {
            flash.error="Es necesario indicar la firma electrónica a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(controller: "TituloElectronico", action:"index")
            return
        }
        
        actionLoggingService.log("Firma de responsable: ${firmaResponsableInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico...")
        
        def tituloElectronicoInstance = firmaResponsableInstance?.firmaResponsables?.tituloElectronico
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "modificarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
//        actionLoggingService.log("Consultando registro ante DGP...")
//        
//        if (tituloElectronicoInstance?.registros) {
//            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles) {
//                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles?.last()?.estatus[0] == 1) {
//                    flash.error = "El titulo electrónico ya se encuentra REGISTRADO ANTE DGP y no puede modificarse"
//                    
//                    actionLoggingService.log("Error: ${flash.error}")
//                    
//                    redirect action: "index", id: tituloElectronicoInstance?.id
//                    return
//                }
//            }
//        }
//        
//        actionLoggingService.log("Consultando autenticación...")
//        
//        if (tituloElectronicoInstance?.autenticacion) {
//            flash.error = "El titulo electrónico ya se encuentra AUTENTICADO y no puede modificarse"
//            
//            actionLoggingService.log("Error: ${flash.error}")
//            
//            redirect action: "index", id: tituloElectronicoInstance?.id
//            return
//        }
        
        actionLoggingService.log("Eliminando firma electrónica...")
        
        def autenticacionInstance = tituloElectronicoInstance?.autenticacion
        
        tituloElectronicoInstance?.firmaResponsables?.removeFromFirmaResponsable(firmaResponsableInstance)
        firmaResponsableInstance.delete(flush:true)
        
        if (autenticacionInstance) {
            tituloElectronicoInstance?.autenticacion = null
            autenticacionInstance.delete(flush:true)
        }
        
        if(!firmaResponsableInstance.hasErrors()) {
            flash.message="Firma electrónica eliminada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect(action:"index", id: tituloElectronicoInstance?.id)
        } else {
            flash.error="No se pudo eliminar la firma electrónica"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect(action:"index", id: tituloElectronicoInstance?.id)
        }
    }
}
