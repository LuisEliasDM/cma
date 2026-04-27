package estructuraXml



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

import grails.plugin.springsecurity.SpringSecurityUtils
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

import seguridad.UsuarioInstitucion
import catalogo.CargoFirmante
import catalogo.AbreviaturaTitulo
import catalogo.InstitucionEducativa

@SpringUserIdentification
@Transactional(readOnly = true)
class FirmaElectronicaController {
    def springSecurityService
    def firmaElectronicaService
    def renapoService
    def actionLoggingService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        def referencia = params?.referencia?.trim()
        
        params.max = Math.min(max ?: 10, 100)
        params.sort = "institucionEducativa"
        params.order = "asc"
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
    
        def usuarioInstitucion = UsuarioInstitucion.findAllByUsuario(usuario)
        if (usuarioInstitucion) {
            session["cveInstitucion"] = usuarioInstitucion?.first()?.institucionEducativa?.clave
        }
                
        if (params.cveInstitucion) {
            session["cveInstitucion"] = params.cveInstitucion != "0"? params.cveInstitucion: null   
        }
        
        def institucionesEducativas
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_RECEPTOR")) {
            institucionesEducativas = InstitucionEducativa.list()
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE")) {
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
                        
        def firmasElectronicas = []
        
        if (referencia) {
        
            firmasElectronicas = FirmaElectronica.createCriteria().list(params) {
                or {
                    ilike("curp", "%${referencia}%")
                    ilike("nombre", "%${referencia}%")
                    ilike("primerApellido", "%${referencia}%")
                    ilike("segundoApellido", "%${referencia}%")
                    ilike("nombreCompleto", "%${referencia}%")
                    ilike("rfcCer", "%${referencia}%")
                    ilike("correoElectronicoCer", "%${referencia}%")
                    ilike("numeroSerieCer", "%${referencia}%")
                }
                if (session["cveInstitucion"]) {
                    institucionEducativa {
                        and {
                            eq("clave", session["cveInstitucion"])
                        }
                    }
                } else {
                    if (!SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_RECEPTOR")) {
                        institucionEducativa {
                            and {
                                'in'("clave", institucionesEducativas*.clave)
                            }
                        }
                    }
                }
                eq("activo", true)
            }
            
        } else {
            firmasElectronicas = FirmaElectronica.createCriteria().list(params) {
                and {
                    if (session["cveInstitucion"]) {
                        institucionEducativa {
                            and {
                                eq("clave", session["cveInstitucion"])
                            }
                        }
                    } else {
                        if (!SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_RECEPTOR")) {
                            institucionEducativa {
                                and {
                                    'in'("clave", institucionesEducativas*.clave)
                                }
                            }
                        }
                    }
                    eq("activo", true)
                }
            }
        }
                                
        respond firmasElectronicas, model:[firmaElectronicaInstanceCount: firmasElectronicas.totalCount, institucionesEducativas: institucionesEducativas, usuarioInstitucion: usuarioInstitucion, usuario: usuario], view: "index"
        
    }
    
    def capturarFirmaElectronica() {
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        def usuarioInstitucion = UsuarioInstitucion.findAllByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
        
        respond new FirmaElectronica(params), model: [institucionesEducativas: institucionesEducativas]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Registro de e.firma")
    @PrintCustomLog
    @Transactional
    def guardarInformacion() {
        actionLoggingService.log("== Registro de e.firma - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
                
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
        
        actionLoggingService.log("Validando CURP...")
        
        def datosCurp = renapoService.consultarDatosCurp(params?.curp)
        
        if (!datosCurp) {
            flash.error="No se pudo realizar la verificacion de la CURP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new FirmaElectronica(params), view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        if (!datosCurp?.curp) {
            flash.error="CURP invalida: ${datosCurp?.mensaje}"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new FirmaElectronica(params), view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        actionLoggingService.log("Datos del RENAPO: ${datosCurp?.toPlainText("\t")}")
                
        actionLoggingService.log("Estableciendo información de la e.firma...")
        
        def certificadoCer = request.getFile("certificadoCer")
        def clavePrivada = request.getFile("clavePrivada")
                
        if (certificadoCer.empty) {
            flash.error="Es necesario seleccionar el archivo de certificado .cer"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new FirmaElectronica(params), view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        if (!certificadoCer?.originalFilename?.toLowerCase()?.endsWith(".cer")) {
            flash.error="El archivo de certificado es incorrecto"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new FirmaElectronica(params), view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        if (clavePrivada) {
            if (!clavePrivada.empty) {
                if (!clavePrivada?.originalFilename?.toLowerCase()?.endsWith(".key")) {
                    flash.error="El archivo de clave privada es incorrecto"
                    
                    actionLoggingService.log("Error: ${flash.error}")
                    
                    respond new FirmaElectronica(params), view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
                    return
                }
            } else {
                clavePrivada = null
            }
        }
        
        def datosCertificado = firmaElectronicaService.extraerDatosCertificado(certificadoCer?.getInputStream())
        
        if (!datosCertificado) {
            flash.error="El archivo de certificado no se pudo procesar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new FirmaElectronica(params), view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        if (datosCertificado.curp != params.curp) {
            flash.error="La CURP del certificado (${datosCertificado.curp}) no corresponde a la CURP ingresada (${params.curp}). "
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new FirmaElectronica(params), view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
                
        def firmaElectronica = new FirmaElectronica()
        
        def cargoFirmante = CargoFirmante.get(params.int("idCargo"))
        def abreviaturaTitulo = AbreviaturaTitulo.get(params.int("idAbrTitulo"))
        def institucionEducativa = InstitucionEducativa.get(params.int("idInstitucionEducativa"))
        
        firmaElectronica.curp = datosCurp?.curp
        firmaElectronica.nombre = datosCurp?.nombre
        firmaElectronica.primerApellido = datosCurp.apellidoPaterno
        firmaElectronica.segundoApellido = datosCurp.apellidoMaterno
        firmaElectronica.sexo = datosCurp?.sexo
        firmaElectronica.fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(datosCurp?.fechaNacimiento)
        firmaElectronica.estadoNacimiento = datosCurp?.claveEntidadNacimiento
        
        
        firmaElectronica.archivoCer = certificadoCer?.getBytes()
        firmaElectronica.archivoKey = clavePrivada? clavePrivada?.getBytes(): null
        firmaElectronica.cargoFirmante = cargoFirmante
        firmaElectronica.abreviaturaTitulo = abreviaturaTitulo
        firmaElectronica.institucionEducativa = institucionEducativa
        firmaElectronica.activo = true
        
        
        firmaElectronica.contenidoCer = datosCertificado.contenido
        firmaElectronica.nombreCer = datosCertificado.nombre
        firmaElectronica.curpCer = datosCertificado.curp
        firmaElectronica.rfcCer = datosCertificado.rfc
        firmaElectronica.correoElectronicoCer = datosCertificado.correoElectronico
        firmaElectronica.validoDesdeCer = datosCertificado.validoDesde
        firmaElectronica.validoHastaCer = datosCertificado.validoHasta
        firmaElectronica.numeroSerieCer = datosCertificado.numeroSerie
        
        if (firmaElectronica.hasErrors()) {
            
            flash.error= firmaElectronica.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond firmaElectronica.errors, view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        actionLoggingService.log("e.firma: ${firmaElectronica?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando e.firma...")
        
        if(firmaElectronica.save(flush:true)){
            flash.message="e.firma registrada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect action:"index"
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond firmaElectronica.errors, view: "capturarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
    }
    
    def consultarFirmaElectronica(FirmaElectronica firmaElectronicaInstance){
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        respond firmaElectronicaInstance, model: [usuario: usuario]
    }
    
    def modificarFirmaElectronica(FirmaElectronica firmaElectronicaInstance){
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
        
        respond firmaElectronicaInstance, model: [institucionesEducativas: institucionesEducativas]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Modificación de e.firma")
    @PrintCustomLog
    @Transactional
    def actualizarRegistro(){
        actionLoggingService.log("== Modificación de e.firma - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
                
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
        
        actionLoggingService.log("Consultando e.firma a modificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def firmaElectronicaInstance = FirmaElectronica.get(params.int("id"))
        
        if (!firmaElectronicaInstance) {
            flash.error="Es necesario indicar la firma electronica a modificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("e.firma (original): ${firmaElectronicaInstance?.toPlainText("\t")}")
        
        if (firmaElectronicaInstance?.curp != usuario?.perfil?.curp) {
            flash.error="El usuario no puede modificar la firma electronica"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Validando CURP...")        
        
        def datosCurp = renapoService.consultarDatosCurp(params?.curp)
        
        if (!datosCurp) {
            flash.error="No se pudo realizar la verificacion de la CURP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond firmaElectronicaInstance, view: "modificarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        if (!datosCurp?.curp) {
            flash.error="CURP invalida: ${datosCurp?.mensaje}"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond firmaElectronicaInstance, view: "modificarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        actionLoggingService.log("Datos del RENAPO: ${datosCurp?.toPlainText("\t")}")
                
        actionLoggingService.log("Estableciendo información de la e.firma...")
        
        def certificadoCer = request.getFile("certificadoCer")
        def clavePrivada = request.getFile("clavePrivada")
                
        if (!certificadoCer.empty) {
            if (!certificadoCer?.originalFilename?.toLowerCase()?.endsWith(".cer")) {
                flash.error="El archivo de certificado es incorrecto"
                respond firmaElectronicaInstance, view: "modificarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
                return
            }
        } else {
            certificadoCer = null
        }
        
        if (clavePrivada) {
            if (!clavePrivada.empty) {
                if (!clavePrivada?.originalFilename?.toLowerCase()?.endsWith(".key")) {
                    flash.error="El archivo de clave privada es incorrecto"
                    respond firmaElectronicaInstance, view: "modificarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
                    return
                }
            } else {
                clavePrivada = null
            }
        }
        
        def cargoFirmante = CargoFirmante.get(params.int("idCargo"))
        def abreviaturaTitulo = AbreviaturaTitulo.get(params.int("idAbrTitulo"))
        def institucionEducativa = InstitucionEducativa.get(params.int("idInstitucionEducativa"))
        
        if (certificadoCer) {
            firmaElectronicaInstance.archivoCer = certificadoCer?.getBytes()
        }
        
        if (clavePrivada) {
            firmaElectronicaInstance.archivoKey = clavePrivada?.getBytes()
        }
        
        firmaElectronicaInstance.cargoFirmante = cargoFirmante
        firmaElectronicaInstance.abreviaturaTitulo = abreviaturaTitulo
        firmaElectronicaInstance.institucionEducativa = institucionEducativa
        
        firmaElectronicaInstance.curp = datosCurp?.curp
        firmaElectronicaInstance.nombre = datosCurp?.nombre
        firmaElectronicaInstance.primerApellido = datosCurp?.apellidoPaterno
        firmaElectronicaInstance.segundoApellido = datosCurp?.apellidoMaterno
        firmaElectronicaInstance.sexo = datosCurp?.sexo
        firmaElectronicaInstance.fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(datosCurp?.fechaNacimiento)
        firmaElectronicaInstance.estadoNacimiento = datosCurp?.claveEntidadNacimiento

        if (certificadoCer) {
            def datosCertificado = firmaElectronicaService.extraerDatosCertificado(certificadoCer?.getInputStream())

            if (!datosCertificado) {
                flash.error="El archivo de certificado no se pudo procesar"
                
                actionLoggingService.log("Error: ${flash.error}")
                
                respond firmaElectronicaInstance, view: "modificarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
                return
            }

            firmaElectronicaInstance.contenidoCer = datosCertificado.contenido
            firmaElectronicaInstance.nombreCer = datosCertificado.nombre
            firmaElectronicaInstance.curpCer = datosCertificado.curp
            firmaElectronicaInstance.rfcCer = datosCertificado.rfc
            firmaElectronicaInstance.correoElectronicoCer = datosCertificado.correoElectronico
            firmaElectronicaInstance.validoDesdeCer = datosCertificado.validoDesde
            firmaElectronicaInstance.validoHastaCer = datosCertificado.validoHasta
            firmaElectronicaInstance.numeroSerieCer = datosCertificado.numeroSerie
        }
        
        if (firmaElectronicaInstance.hasErrors()) {
            flash.error= firmaElectronicaInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond firmaElectronicaInstance.errors, view: "modificarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        actionLoggingService.log("e.firma (actualizada): ${firmaElectronicaInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando e.firma...")
        
        if(firmaElectronicaInstance.save(flush:true)){
            flash.message="e.firma actualizada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect action:"consultarFirmaElectronica", id: firmaElectronicaInstance?.id
        } else {
            flash.error= firmaElectronicaInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond firmaElectronicaInstance.errors, view: "modificarFirmaElectronica", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Eliminación de e.firma")
    @PrintCustomLog
    @Transactional
    def eliminarFirmaElectronica(FirmaElectronica firmaElectronicaInstance){
        actionLoggingService.log("== Eliminación de e.firma - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando e.firma a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!firmaElectronicaInstance) {
            flash.error="Es necesario indicar la e.firma a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("e.firma: ${firmaElectronicaInstance?.toPlainText("\t")}")
        
        if (firmaElectronicaInstance?.curp != usuario?.perfil?.curp) {
            flash.error="El usuario no puede eliminar la firma electronica"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Eliminando e.firma...")
        
        firmaElectronicaInstance.activo = false
        
        if(firmaElectronicaInstance.save(flush:true)){
            flash.message="e.firma eliminada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect action:"index"
        } else {
            flash.error= firmaElectronicaInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond firmaElectronicaInstance, view: "consultarFirmaElectronica"
            return
        }
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Descarga de certificado de la e.firma (.CER)")
    @PrintCustomLog
    def descargarCer(FirmaElectronica firmaElectronicaInstance){
        actionLoggingService.log("== Descarga de certificado de la e.firma (.CER) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando e.firma a descargar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!firmaElectronicaInstance) {
            flash.error="Es necesario indicar la e.firma a descargar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        if (!firmaElectronicaInstance?.archivoCer) {
            flash.error="No se encontro el archivo .cer"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("e.firma: ${firmaElectronicaInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Descargando certificado...")
        
        def nombreArchivo = "Certificado_${firmaElectronicaInstance?.curp}.cer"
        
        render file: firmaElectronicaInstance?.archivoCer, fileName: nombreArchivo, contentType: "application/pkix-cert"
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Descarga de la clave privada de la e.firma (.KEY)")
    @PrintCustomLog
    def descargarKey(FirmaElectronica firmaElectronicaInstance){
        actionLoggingService.log("== Descarga de la clave privada de la e.firma (.KEY) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando e.firma a descargar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!firmaElectronicaInstance) {
            flash.error="Es necesario indicar la e.firma a descargar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        if (!firmaElectronicaInstance?.archivoKey) {
            flash.error="No se encontro el archivo .key"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("e.firma: ${firmaElectronicaInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Descargando clave privada...")
        
        def nombreArchivo = "Clave_Privada_${firmaElectronicaInstance?.curp}.key"
        
        render file: firmaElectronicaInstance?.archivoKey, fileName: nombreArchivo, contentType: "application/pkcs8"
    }

    def show(FirmaElectronica firmaElectronicaInstance) {
        respond firmaElectronicaInstance
    }

    def create() {
        respond new FirmaElectronica(params)
    }

    @Transactional
    def save(FirmaElectronica firmaElectronicaInstance) {
        if (firmaElectronicaInstance == null) {
            notFound()
            return
        }

        if (firmaElectronicaInstance.hasErrors()) {
            respond firmaElectronicaInstance.errors, view:'create'
            return
        }

        firmaElectronicaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'firmaElectronica.label', default: 'FirmaElectronica'), firmaElectronicaInstance.id])
                redirect firmaElectronicaInstance
            }
            '*' { respond firmaElectronicaInstance, [status: CREATED] }
        }
    }

    def edit(FirmaElectronica firmaElectronicaInstance) {
        respond firmaElectronicaInstance
    }

    @Transactional
    def update(FirmaElectronica firmaElectronicaInstance) {
        if (firmaElectronicaInstance == null) {
            notFound()
            return
        }

        if (firmaElectronicaInstance.hasErrors()) {
            respond firmaElectronicaInstance.errors, view:'edit'
            return
        }

        firmaElectronicaInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'FirmaElectronica.label', default: 'FirmaElectronica'), firmaElectronicaInstance.id])
                redirect firmaElectronicaInstance
            }
            '*'{ respond firmaElectronicaInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(FirmaElectronica firmaElectronicaInstance) {

        if (firmaElectronicaInstance == null) {
            notFound()
            return
        }

        firmaElectronicaInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'FirmaElectronica.label', default: 'FirmaElectronica'), firmaElectronicaInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'firmaElectronica.label', default: 'FirmaElectronica'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
