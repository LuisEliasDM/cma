package estructuraXml

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.XML
import catalogo.*
import seguridad.UsuarioInstitucion
import grails.plugin.springsecurity.SpringSecurityUtils
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*
import paquete.Paquete
import org.hibernate.criterion.CriteriaSpecification
import revision.Observacion
import revision.Correccion

@SpringUserIdentification
@Transactional(readOnly = true)
class TituloElectronicoController {
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    def xmlService
    def tituloElectronicoService
    def DGPPruebaService
    def springSecurityService
    def renapoService
    def actionLoggingService

    def capturarTitulo() {
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        def usuarioInstitucion = UsuarioInstitucion.findAllByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
        
        def entidadesFederativasExpedicion = EntidadFederativa.findAllById("17")
        def entidadesFederativasAntecedente = EntidadFederativa.list()
        
        respond new TituloElectronico(params), model: [institucionesEducativas: institucionesEducativas, entidadesFederativasExpedicion: entidadesFederativasExpedicion, entidadesFederativasAntecedente: entidadesFederativasAntecedente]
    }
    
    def consultarTitulo(TituloElectronico tituloElectronicoInstance) {
        if (!tituloElectronicoInstance) {
            flash.error = "Es necesario selecciona un título electrónico"
            redirect action: "index"
            return
        }
        
        def observaciones = Observacion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        
        def correcciones = Correccion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        
        respond tituloElectronicoInstance, model: [observaciones: observaciones, correcciones: correcciones]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Registro de título electrónico")
    @PrintCustomLog
    @Transactional
    def guardarInformacion() {
        actionLoggingService.log("== Registro de título electrónico - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
                
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
                
        actionLoggingService.log("Validando CURP...")
        
        def datosCurp = renapoService.consultarDatosCurp(params?.curp)
                
        if (!datosCurp) {
            flash.error="No se pudo realizar la verificación de la CURP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new TituloElectronico(params), view: "capturarTitulo", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        if (!datosCurp?.curp) {
            flash.error="CURP invalida: ${datosCurp?.mensaje}"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new TituloElectronico(params), view: "capturarTitulo", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        actionLoggingService.log("Datos del RENAPO: ${datosCurp?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del título electrónico...")
        
        def tituloElectronicoInstance = new TituloElectronico()
        
        def antecedente=new Antecedente()
        def tipoEstudioAntecedente = TipoEstudioAntecedente.get(params.int("idTipoEstudioAntecedente"))
        def entidadFederativaAntecedente = EntidadFederativa.get(params?.idEntidadFederativaAntecedente)
        
        antecedente.institucionProcedencia=params?.institucionProcedencia?.trim()
        antecedente.idTipoEstudioAntecedente=tipoEstudioAntecedente?.id
        antecedente.tipoEstudioAntecedente=tipoEstudioAntecedente?.descripcion
        antecedente.idEntidadFederativa=entidadFederativaAntecedente?.id
        antecedente.entidadFederativa=entidadFederativaAntecedente?.descripcion
        
        if (params?.fechaInicioAntecedenteDia && params?.fechaInicioAntecedenteMes && params?.fechaInicioAntecedenteAnio) {
            antecedente.fechaInicio=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaInicioAntecedenteDia?:""}/${params?.fechaInicioAntecedenteMes?:""}/${params?.fechaInicioAntecedenteAnio?:""}")
        }
        
        if(params?.fechaTerminacionAntecedenteDia && params?.fechaTerminacionAntecedenteMes && params?.fechaTerminacionAntecedenteAnio) {
            antecedente.fechaTerminacion=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaTerminacionAntecedenteDia?:""}/${params?.fechaTerminacionAntecedenteMes?:""}/${params?.fechaTerminacionAntecedenteAnio?:""}")
        }
        
        antecedente.noCedula=params?.noCedula?.trim()
        
        def carrera_=new Carrera()
        def carreraInstitucion = CarreraInstitucion.get(params.int("cveCarrera"))
        
        carrera_?.cveCarrera=carreraInstitucion?.clave
        carrera_?.nombreCarrera=carreraInstitucion?.descripcion
        
        if (params?.fechaInicioCarreraDia && params?.fechaInicioCarreraMes && params?.fechaInicioCarreraAnio) {
            carrera_?.fechaInicio=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaInicioCarreraDia?:""}/${params?.fechaInicioCarreraMes?:""}/${params?.fechaInicioCarreraAnio?:""}")
        }
        
        if(params?.fechaTerminacionCarreraDia && params?.fechaTerminacionCarreraMes && params?.fechaTerminacionCarreraAnio) {
            carrera_?.fechaTerminacion=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaTerminacionCarreraDia?:""}/${params?.fechaTerminacionCarreraMes?:""}/${params?.fechaTerminacionCarreraAnio?:""}")
        }
        
        carrera_?.idAutorizacionReconocimiento=carreraInstitucion?.autorizacionReconocimiento?.id
        carrera_?.autorizacionReconocimiento=carreraInstitucion?.autorizacionReconocimiento?.descripcion
        carrera_?.numeroRvoe=carreraInstitucion?.rvoe
        
        def expedicion=new Expedicion()
        def modalidadTitulacion = ModalidadTitulacion.get(params.int("idModalidadTitulacion"))
        def fundamentoLegalServicioSocial = FundamentoLegalServicioSocial.get(params.int("idFundamentoLegalServicioSocial"))
        def entidadFederativaExpedicion = EntidadFederativa.get(params?.idEntidadFederativaExpedicion)
        
        if (params?.fechaExpedicionDia && params?.fechaExpedicionMes && params?.fechaExpedicionAnio) {
            expedicion.fechaExpedicion=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaExpedicionDia?:""}/${params?.fechaExpedicionMes?:""}/${params?.fechaExpedicionAnio?:""}")
        
            if (expedicion.fechaExpedicion < new Date().parse("dd/MM/yyyy", "01/10/2018")) {
                flash.error="La fecha de expedición del título electrónico no puede ser anterior al 1ro de Octubre de 2018"
            
                actionLoggingService.log("Error: ${flash.error}")

                respond new TituloElectronico(params), view: "capturarTitulo", model: [institucionesEducativas: institucionesEducativas]
                return
            }
        }
        
        expedicion.idModalidadTitulacion=modalidadTitulacion?.id
        expedicion.modalidadTitulacion=modalidadTitulacion?.descripcion
        
        if (params?.fechaExamenProfesionalDia && params?.fechaExamenProfesionalMes && params?.fechaExamenProfesionalAnio) {
            expedicion.fechaExamenProfesional=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaExamenProfesionalDia?:""}/${params?.fechaExamenProfesionalMes?:""}/${params?.fechaExamenProfesionalAnio?:""}")
        }
        
        if (params?.fechaExencionExamenProfesionalDia && params?.fechaExencionExamenProfesionalMes && params?.fechaExencionExamenProfesionalAnio) {
            expedicion.fechaExencionExamenProfesional=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaExencionExamenProfesionalDia?:""}/${params?.fechaExencionExamenProfesionalMes?:""}/${params?.fechaExencionExamenProfesionalAnio?:""}")
        }
        
        if (modalidadTitulacion?.id == 1) {
            expedicion.fechaExencionExamenProfesional = null
        } else {
            expedicion.fechaExamenProfesional = null
        }
        
        expedicion.cumplioServicioSocial=params?.cumplioServicioSocial?.toInteger()
        expedicion.idFundamentoLegalServicioSocial=fundamentoLegalServicioSocial?.id
        expedicion.fundamentoLegalServicioSocial=fundamentoLegalServicioSocial?.descripcion
        expedicion.idEntidadFederativa=entidadFederativaExpedicion?.id
        expedicion.entidadFederativa=entidadFederativaExpedicion?.descripcion
        
        def institucion_=new Institucion()
        def institucionEducativa = InstitucionEducativa.get(params.int("cveInstitucion"))
        
        institucion_.cveInstitucion=institucionEducativa?.clave
        institucion_.nombreInstitucion=institucionEducativa?.descripcion
        
        def profesionista_=new Profesionista()
        profesionista_.curp=datosCurp?.curp
        profesionista_.nombre=datosCurp?.nombre
        profesionista_.primerApellido=datosCurp?.apellidoPaterno
        profesionista_.segundoApellido=datosCurp?.apellidoMaterno
        profesionista_.sexo=datosCurp?.sexo
        profesionista_.fechaNacimiento=new SimpleDateFormat("dd/MM/yyyy").parse(datosCurp?.fechaNacimiento)
        profesionista_.estadoNacimiento=datosCurp?.claveEntidadNacimiento
        profesionista_.correoElectronico="controlescolar.titulos@iebem.edu.mx"
        
        tituloElectronicoInstance?.aclaracion=params?.aclaracion?.trim()

        tituloElectronicoInstance?.institucion=institucion_
        tituloElectronicoInstance?.carrera=carrera_
        tituloElectronicoInstance?.profesionista=profesionista_
        tituloElectronicoInstance?.expedicion=expedicion
        tituloElectronicoInstance?.antecedente=antecedente
        
        def firmaResponsables=new FirmaResponsables()
        tituloElectronicoInstance.firmaResponsables = firmaResponsables
        
        tituloElectronicoInstance?.folioControl=tituloElectronicoService.generaIdentificadorEstructurado(tituloElectronicoInstance)
                
        tituloElectronicoInstance?.activo = true
        
        if (tituloElectronicoInstance.hasErrors()) {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'capturarTitulo'
            return
        } 
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")

        def listaExistentes = TituloElectronico.createCriteria().list {
            and {
                institucion {
                    and {
                        eq("cveInstitucion", tituloElectronicoInstance?.institucion?.cveInstitucion)
                    }
                }
                carrera {
                    and {
                        eq("cveCarrera", tituloElectronicoInstance?.carrera?.cveCarrera)
                    }
                }
                profesionista {
                    and {
                        eq("curp", tituloElectronicoInstance?.profesionista?.curp)
                    }
                }
                isNull("paquete")
                eq("activo", true)
            }
        }

        def existente = false
        if (listaExistentes) {
            existente = true
            listaExistentes?.each {
                if (it.estatus?.id in [20,111]) {
                    existente = false
                }
            }
        }

        if (existente) {
            flash.error="El título electrónico del profesionista ya se encuentra registrado en la institución y carrera indicadas"

            actionLoggingService.log("Error: ${flash.error}")

            respond new TituloElectronico(params), view: "capturarTitulo", model: [institucionesEducativas: institucionesEducativas]
            return
        }
        
        actionLoggingService.log("Guardando título electrónico...")
        
        if(tituloElectronicoInstance.save(flush:true)) {
            flash.message="Título electrónico registrado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond tituloElectronicoInstance, view:'consultarTitulo'
            return
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'capturarTitulo'
            return
        }
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Modificación de título electrónico")
    @SpringUserIdentification
    @PrintCustomLog
    @Transactional
    def actualizarRegistro() {
        actionLoggingService.log("== Modificación de título electrónico - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
                        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
                
        actionLoggingService.log("Consultando título electrónico a modificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def tituloElectronicoInstance = TituloElectronico?.findById(params?.id)    
        
        if (!tituloElectronicoInstance) {
            flash.error="Es necesario indicar el título electrónico a modificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "modificarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Título electrónico (original): ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Validando CURP...")
        
        def datosCurp = renapoService.consultarDatosCurp(params?.curp)
        
        actionLoggingService.log("Datos del RENAPO: ${datosCurp?.toPlainText("\t")}")
        
        if (!datosCurp) {
            flash.error="No se pudo realizar la verificacion de la CURP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "modificarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        if (!datosCurp?.curp) {
            flash.error="CURP invalida: ${datosCurp?.mensaje}"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "modificarTitulo", id: tituloElectronicoInstance?.id
            return
        }        
        
        actionLoggingService.log("Estableciendo información del título electrónico...")
        
        def tipoEstudioAntecedente = TipoEstudioAntecedente.get(params.int("idTipoEstudioAntecedente"))
        def entidadFederativaAntecedente = EntidadFederativa.get(params?.idEntidadFederativaAntecedente)
       
        tituloElectronicoInstance?.antecedente?.institucionProcedencia=params?.institucionProcedencia?.trim()
        tituloElectronicoInstance?.antecedente?.idTipoEstudioAntecedente=tipoEstudioAntecedente?.id
        tituloElectronicoInstance?.antecedente?.tipoEstudioAntecedente=tipoEstudioAntecedente?.descripcion
        tituloElectronicoInstance?.antecedente?.idEntidadFederativa=entidadFederativaAntecedente?.id
        tituloElectronicoInstance?.antecedente?.entidadFederativa=entidadFederativaAntecedente?.descripcion
        
        if (params?.fechaInicioAntecedenteDia && params?.fechaInicioAntecedenteMes && params?.fechaInicioAntecedenteAnio) {
            tituloElectronicoInstance?.antecedente?.fechaInicio=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaInicioAntecedenteDia?:""}/${params?.fechaInicioAntecedenteMes?:""}/${params?.fechaInicioAntecedenteAnio?:""}")
        }
        
        if(params?.fechaTerminacionAntecedenteDia && params?.fechaTerminacionAntecedenteMes && params?.fechaTerminacionAntecedenteAnio) {
            tituloElectronicoInstance?.antecedente?.fechaTerminacion=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaTerminacionAntecedenteDia?:""}/${params?.fechaTerminacionAntecedenteMes?:""}/${params?.fechaTerminacionAntecedenteAnio?:""}")
        }
                
        tituloElectronicoInstance?.antecedente?.noCedula=params?.noCedula?.trim()
        
        def carreraInstitucion = CarreraInstitucion.get(params.int("cveCarrera"))
        
        tituloElectronicoInstance?.carrera?.cveCarrera=carreraInstitucion?.clave
        tituloElectronicoInstance?.carrera?.nombreCarrera=carreraInstitucion?.descripcion
        
        if (params?.fechaInicioCarreraDia && params?.fechaInicioCarreraMes && params?.fechaInicioCarreraAnio) {
            tituloElectronicoInstance?.carrera?.fechaInicio=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaInicioCarreraDia?:""}/${params?.fechaInicioCarreraMes?:""}/${params?.fechaInicioCarreraAnio?:""}")
        }
        
        if(params?.fechaTerminacionCarreraDia && params?.fechaTerminacionCarreraMes && params?.fechaTerminacionCarreraAnio) {
            tituloElectronicoInstance?.carrera?.fechaTerminacion=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaTerminacionCarreraDia?:""}/${params?.fechaTerminacionCarreraMes?:""}/${params?.fechaTerminacionCarreraAnio?:""}")
        }
        
        tituloElectronicoInstance?.carrera?.idAutorizacionReconocimiento=carreraInstitucion?.autorizacionReconocimiento?.id
        tituloElectronicoInstance?.carrera?.autorizacionReconocimiento=carreraInstitucion?.autorizacionReconocimiento?.descripcion
        tituloElectronicoInstance?.carrera?.numeroRvoe=carreraInstitucion?.rvoe
        
        def modalidadTitulacion = ModalidadTitulacion.get(params.int("idModalidadTitulacion"))
        def fundamentoLegalServicioSocial = FundamentoLegalServicioSocial.get(params.int("idFundamentoLegalServicioSocial"))
        def entidadFederativaExpedicion = EntidadFederativa.get(params?.idEntidadFederativaExpedicion)
        
        if (params?.fechaExpedicionDia && params?.fechaExpedicionMes && params?.fechaExpedicionAnio) {
            def fechaExpedicion = new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaExpedicionDia?:""}/${params?.fechaExpedicionMes?:""}/${params?.fechaExpedicionAnio?:""}")
            
            def anioOriginal = fechaExpedicion?.format("yyyy")?.toInteger()
            def anioNuevo = tituloElectronicoInstance?.expedicion?.fechaExpedicion?.format("yyyy")?.toInteger()
            
            tituloElectronicoInstance?.expedicion?.fechaExpedicion=fechaExpedicion
            
            if (anioOriginal != anioNuevo) {
                tituloElectronicoInstance.folioControl = tituloElectronicoService.generaIdentificadorEstructurado(tituloElectronicoInstance)
            }
        }
        
        tituloElectronicoInstance?.expedicion?.idModalidadTitulacion=modalidadTitulacion?.id
        tituloElectronicoInstance?.expedicion?.modalidadTitulacion=modalidadTitulacion?.descripcion
        
        if (params?.fechaExamenProfesionalDia && params?.fechaExamenProfesionalMes && params?.fechaExamenProfesionalAnio) {
            tituloElectronicoInstance?.expedicion?.fechaExamenProfesional=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaExamenProfesionalDia?:""}/${params?.fechaExamenProfesionalMes?:""}/${params?.fechaExamenProfesionalAnio?:""}")
        }
        
        if (params?.fechaExencionExamenProfesionalDia && params?.fechaExencionExamenProfesionalMes && params?.fechaExencionExamenProfesionalAnio) {
            tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaExencionExamenProfesionalDia?:""}/${params?.fechaExencionExamenProfesionalMes?:""}/${params?.fechaExencionExamenProfesionalAnio?:""}")
        }
        
        if (modalidadTitulacion?.id == 1) {
            tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional = null
        } else {
            tituloElectronicoInstance?.expedicion?.fechaExamenProfesional = null
        }
        
        tituloElectronicoInstance?.expedicion?.cumplioServicioSocial=params?.cumplioServicioSocial?.toInteger()
        tituloElectronicoInstance?.expedicion?.idFundamentoLegalServicioSocial=fundamentoLegalServicioSocial?.id
        tituloElectronicoInstance?.expedicion?.fundamentoLegalServicioSocial=fundamentoLegalServicioSocial?.descripcion
        tituloElectronicoInstance?.expedicion?.idEntidadFederativa=entidadFederativaExpedicion?.id
        tituloElectronicoInstance?.expedicion?.entidadFederativa=entidadFederativaExpedicion?.descripcion
        
        def institucionEducativa = InstitucionEducativa.get(params.int("cveInstitucion"))
        
        tituloElectronicoInstance?.institucion?.cveInstitucion=institucionEducativa?.clave
        tituloElectronicoInstance?.institucion?.nombreInstitucion=institucionEducativa?.descripcion
        
        tituloElectronicoInstance?.profesionista?.curp=datosCurp?.curp
        tituloElectronicoInstance?.profesionista?.nombre=datosCurp?.nombre
        tituloElectronicoInstance?.profesionista?.primerApellido=datosCurp?.apellidoPaterno
        tituloElectronicoInstance?.profesionista?.segundoApellido=datosCurp?.apellidoMaterno
        tituloElectronicoInstance?.profesionista?.sexo=datosCurp?.sexo
        tituloElectronicoInstance?.profesionista?.fechaNacimiento=new SimpleDateFormat("dd/MM/yyyy").parse(datosCurp?.fechaNacimiento)
        tituloElectronicoInstance?.profesionista?.estadoNacimiento=datosCurp?.claveEntidadNacimiento
        tituloElectronicoInstance?.profesionista?.correoElectronico="controlescolar.titulos@iebem.edu.mx"

        tituloElectronicoInstance?.aclaracion=params?.aclaracion?.trim()
        
        if (tituloElectronicoInstance.hasErrors()) {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "modificarTitulo", id: tituloElectronicoInstance?.id
            return
        }  
        
        def listaExistentes = TituloElectronico.createCriteria().list {
            and {
                institucion {
                    and {
                        eq("cveInstitucion", tituloElectronicoInstance?.institucion.cveInstitucion)
                    }
                }
                carrera {
                    and {
                        eq("cveCarrera", tituloElectronicoInstance?.carrera?.cveCarrera)
                    }
                }
                profesionista {
                    and {
                        eq("curp", tituloElectronicoInstance?.profesionista?.curp)
                    }
                }
                ne("id", tituloElectronicoInstance?.id)
                isNull("paquete")
                eq("activo", true)
            }
        }

        def existente = false
        if (listaExistentes) {
            existente = true
            listaExistentes?.each {
                if (it.estatus?.id in [20,111]) {
                    existente = false
                }
            }
        }

        if (existente) {
            flash.error="El título electrónico del profesionista ya se encuentra registrado en la institución y carrera indicadas"

            actionLoggingService.log("Error: ${flash.error}")

            redirect action: "modificarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Consultando firmas de responsables...")
        
        actionLoggingService.log("Firmas de responsables: ${tituloElectronicoInstance?.firmaResponsables?.toPlainText("\t")?:""}")
        
        def firmas = []
        if (tituloElectronicoInstance?.firmaResponsables) {
            actionLoggingService.log("Eliminando firmas de responsables...")
            
            firmas = tituloElectronicoInstance?.firmaResponsables?.firmaResponsable
            
            tituloElectronicoInstance?.firmaResponsables?.firmaResponsable = []
            
            firmas?.each { firma ->
                 firma.delete(flush:true)
            }
        }
        
        actionLoggingService.log("Consultando autenticación...")
        
        actionLoggingService.log("Autenticación: ${tituloElectronicoInstance?.autenticacion?.toPlainText("\t")?:""}")
        
        def autenticacionInstance = tituloElectronicoInstance?.autenticacion
        if (autenticacionInstance) {
            actionLoggingService.log("Eliminando autenticación...")
            tituloElectronicoInstance?.autenticacion = null
            autenticacionInstance.delete(flush:true)
        }
        
        actionLoggingService.log("Título electrónico (actualizado): ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando título electrónico...")
        
        if(tituloElectronicoInstance.save(flush:true)) {
            flash.message="Título electrónico actualizado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond tituloElectronicoInstance, view:'consultarTitulo'
            return
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'actualizarRegistro'
            return
        }
        
        
    }
    
    def index(){
        forward action: "titulosPaquete"
    }
    
    def titulosPaquete(Integer max){
        def referencia = params?.referencia?.trim()
        
        params.max = Math.min(max ?: 10, 100)
        params.sort = "dateCreated"
        params.order = "desc"
        
        def paquetesInstitucion
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
    
        def usuarioInstitucion = UsuarioInstitucion.findAllByUsuario(usuario)
        if (usuarioInstitucion) {
            session["cveInstitucion"] = usuarioInstitucion?.first()?.institucionEducativa?.clave
        }
                
        if (params.cveInstitucion) {
            session["cveInstitucion"] = params.cveInstitucion != "0"? params.cveInstitucion: null   
        }
        
        if (!session["cveInstitucion"]) {
            session.removeAttribute("idPaquete")
        }
        
        if (params.idPaquete) {
            session["idPaquete"] = params.idPaquete != "0"? params.idPaquete: null   
        }
        
        def paqueteInstance
        if (session["cveInstitucion"]) {
            def institucion = InstitucionEducativa.findByClave(session["cveInstitucion"])
            paquetesInstitucion = Paquete.findAllByInstitucionEducativa(institucion)
            
            if (session["idPaquete"]) {
                paqueteInstance = Paquete.findByInstitucionEducativaAndId(institucion, session["idPaquete"]?.toInteger())
                if (!paqueteInstance) {
                    session.removeAttribute("idPaquete")
                }
                
            }
        }
        
        def institucionesEducativas
        
        if (SpringSecurityUtils.ifAnyGranted("ROLE_ADMINISTRADOR,ROLE_AUTENTICADOR,ROLE_COTEJADOR,ROLE_REVISOR,ROLE_RECEPTOR")) {
            institucionesEducativas = InstitucionEducativa.list()
        } else if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
        
//        if (!institucionesEducativas && !session["cveInstitucion"] && SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
//            redirect action: "institucionNoAsignada"
//            return
//        }
                        
        def titulosElectronicos = []
        
        if (referencia) {
        
            titulosElectronicos = TituloElectronico.createCriteria().list(params) {
                createAlias("paquete", 'pq', CriteriaSpecification.LEFT_JOIN)
                or {
                    profesionista {
                        or {
                            ilike("curp", "%${referencia}%")
                            ilike("nombre", "%${referencia}%")
                            ilike("primerApellido", "%${referencia}%")
                            ilike("segundoApellido", "%${referencia}%")
                            ilike("correoElectronico", "%${referencia}%")
                            ilike("nombreCompleto", "%${referencia}%")
                            ilike("apellidos", "%${referencia}%")
                        }
                    }
                    ilike("pq.folio", "%${referencia}%")
                    ilike("folioControl", "%${referencia}%")
                }
                if (session["cveInstitucion"]) {
                    institucion {
                        and {
                            eq("cveInstitucion", session["cveInstitucion"]?:"")
                        }
                    }
                } else {
                    institucion {
                        and {
                            'in'("cveInstitucion", institucionesEducativas*.clave?:"")
                        }
                    }
                }
                
                if (session["idPaquete"]) {
                    and {
                        eq("pq.id", session["idPaquete"]?.toInteger())
                    }
                } else {
                    //isNull("pq.id")
                }
                
                and {
                    eq("activo", true)
                }
                
                /*
                if (SpringSecurityUtils.ifAnyGranted("ROLE_RECEPTOR")) {
                    firmaResponsables {
                        and {
                            isNotEmpty("firmaResponsable")
                        }
                    }
                }
                
                if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR,ROLE_REVISOR")) {
                    and {
                        eq("pagado", true)
                    }
                }
                
                if (SpringSecurityUtils.ifAnyGranted("ROLE_AUTENTICADOR")) {
                    and {
                        eq("revisado", true)
                    }
                }*/
                
            }
            
        } else {
            titulosElectronicos = TituloElectronico.createCriteria().list(params) {
                createAlias("paquete", 'pq', CriteriaSpecification.LEFT_JOIN)
                if (session["cveInstitucion"]) {
                    institucion {
                        and {
                            eq("cveInstitucion", session["cveInstitucion"]?:"")
                        }
                    }
                } else {
                    institucion {
                        and {
                            'in'("cveInstitucion", institucionesEducativas*.clave?:"")
                        }
                    }
                }
                
                if (session["idPaquete"]) {
                    and {
                        eq("pq.id", session["idPaquete"]?.toInteger())
                    }
                } else {
                    isNull("pq.id")
                }
                
                and {
                    eq("activo", true)
                }
                
                /*
                if (SpringSecurityUtils.ifAnyGranted("ROLE_RECEPTOR")) {
                    firmaResponsables {
                        and {
                            isNotEmpty("firmaResponsable")
                        }
                    }
                }
                
                if (SpringSecurityUtils.ifAnyGranted("ROLE_COTEJADOR,ROLE_REVISOR")) {
                    and {
                        eq("pagado", true)
                    }
                }
                
                if (SpringSecurityUtils.ifAnyGranted("ROLE_AUTENTICADOR")) {
                    and {
                        eq("revisado", true)
                    }
                }
                */
            }
        }
                
        respond titulosElectronicos, model:[tituloElectronicoInstanceCount: titulosElectronicos.totalCount, institucionesEducativas: institucionesEducativas, usuarioInstitucion: usuarioInstitucion, paquetesInstitucion: paquetesInstitucion, paqueteInstance: paqueteInstance], view: "index"
        
    }

    def modificarTitulo(TituloElectronico tituloElectronicoInstance) {
        
        if (!tituloElectronicoInstance) {
            flash.error = "En necesario indicar el titulo electronico a modificar"
            redirect action: "index"
            return
        }
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "modificarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
//        if (tituloElectronicoInstance?.registros) {
//            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles) {
//                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.detalles?.last()?.estatus[0] == 1) {
//                    flash.error = "El titulo electronico ya se encuentra REGISTRADO ANTE DGP y no puede modificarse"
//                    redirect action: "consultarTitulo", id: tituloElectronicoInstance?.id
//                    return
//                }
//            }
//        }
//              
//        if (tituloElectronicoInstance?.autenticacion) {
//            flash.error = "El titulo electronico ya se encuentra AUTENTICADO y no puede modificarse"
//            redirect action: "consultarTitulo", id: tituloElectronicoInstance?.id
//            return
//        }
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        def usuarioInstitucion = UsuarioInstitucion.findAllByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
        
        def institucion = InstitucionEducativa.findByClave(tituloElectronicoInstance?.institucion?.cveInstitucion)
        def carrera = CarreraInstitucion.findByInstitucionEducativaAndClave(institucion, tituloElectronicoInstance?.carrera?.cveCarrera)
                
        def idInstitucion = institucion?.id
        def idCarrera = carrera?.id
        
        def entidadesFederativasExpedicion = EntidadFederativa.findAllById("17")
        def entidadesFederativasAntecedente = EntidadFederativa.list()

        respond tituloElectronicoInstance, model: [institucionesEducativas: institucionesEducativas,carreras: institucion?.carrerasInstitucion, idInstitucion: idInstitucion, idCarrera: idCarrera, entidadesFederativasExpedicion: entidadesFederativasExpedicion, entidadesFederativasAntecedente: entidadesFederativasAntecedente]
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Eliminación de título electrónico")
    @SpringUserIdentification
    @PrintCustomLog
    @Transactional
    def eliminarTitulo() {
        actionLoggingService.log("== Eliminación de título electrónico - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
                        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
                
        actionLoggingService.log("Consultando título electrónico a eliminar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def tituloElectronicoInstance = TituloElectronico?.findById(params?.id)    
        
        if (!tituloElectronicoInstance) {
            flash.error="Es necesario indicar el título electrónico a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        def permiso = tituloElectronicoService.permisoTitulo(tituloElectronicoInstance, "eliminarTitulo")
        if (!permiso?.permitido) {
            flash.error = permiso.descripcion
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        tituloElectronicoInstance?.activo = false
        
        actionLoggingService.log("Eliminando título electrónico...")
        
        if(tituloElectronicoInstance.save(flush:true)) {
            flash.message="Título electrónico eliminado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect action: "index"
            return
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'consultarTitulo'
            return
        }
        
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Finalización de revisión de título electrónico")
    @PrintCustomLog
    @Transactional
    def finalizarRevision() {
        actionLoggingService.log("== Finalización de revisión de título electrónico - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
                
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico a finalizar la revisión...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def tituloElectronicoInstance = TituloElectronico?.findById(params?.id)    
        
        if (!tituloElectronicoInstance) {
            flash.error="Es necesario indicar el título electrónico a finalizar la revisión"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Título electrónico (original): ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo revisión del título electrónico...")
        
        tituloElectronicoInstance.pagado = true
        
        if (tituloElectronicoInstance.hasErrors()) {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'consultarTitulo'
            return
        } 
        
        actionLoggingService.log("Título electrónico (actualizado): ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando revisión de título electrónico...")
        
        if(tituloElectronicoInstance.save(flush:true)) {
            flash.message="Revisión del Título electrónico finalizada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond tituloElectronicoInstance, view:'consultarTitulo'
            return
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'consultarTitulo'
            return
        }
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Descarga de XML")
    @PrintCustomLog
    @Transactional
    def xml(TituloElectronico tituloElectronicoInstance) {
        actionLoggingService.log("== Descarga de representación gráfica - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
                
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        def institucionesEducativas
        if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
        
        actionLoggingService.log("Consultando título electrónico a generar la representación gráfica...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!tituloElectronicoInstance) {
            flash.error = "Titulo electronico no encontrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Verificando registro exitoso ante la DGP...")
            
        def registrado = false
        def cancelado = false
        if (tituloElectronicoInstance?.registros) {
            if (tituloElectronicoInstance.registros.last()?.archivosResultado?.detalles) {
                if (tituloElectronicoInstance.registros.last().archivosResultado.detalles.last()?.estatus[0] == 1) {
                    
                    tituloElectronicoInstance.registros.cancelaciones?.each {
                        if (it.cancelado) {
                            cancelado = true
                            return
                        }
                    }
                    
                    if (!cancelado) {
                        registrado = true
                    }
                }
            }
        }
        
        if (cancelado) {
            flash.error = "El titulo electronico se encuentra cancelado ante la DGP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        if (!registrado) {
            flash.error = "Es necesario que el titulo electronico se encuentre registrado con exito ante la DGP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }

        actionLoggingService.log("Generando archivo XML...")
        
        def xml = xmlService.generarArchivoXml(tituloElectronicoInstance)

        if (!xml) {
            flash.error = "No se pudo generar el archivo XML"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        response.setHeader("Content-disposition", "attachment; filename=Titulo_Electronico_XML_${tituloElectronicoInstance.folioControl}.xml")
        response.contentType = "text/xml"
        response.outputStream << xml.getBytes()
             
                
    }
    //Lo mismo para el xml
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Descarga de representación gráfica")
    @PrintCustomLog
    @Transactional
    def descargarRepresentacionGrafica(TituloElectronico tituloElectronicoInstance) {
        actionLoggingService.log("== Descarga de representación gráfica - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
                
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        def institucionesEducativas
        if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
            institucionesEducativas = usuarioInstitucion*.institucionEducativa
        }
        
        actionLoggingService.log("Consultando título electrónico a generar la representación gráfica...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (!tituloElectronicoInstance) {
            flash.error = "Titulo electronico no encontrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Verificando registro exitoso ante la DGP...")
            
        def registrado = false
        def cancelado = false
        if (tituloElectronicoInstance?.registros) {
            if (tituloElectronicoInstance.registros.last()?.archivosResultado?.detalles) {
                if (tituloElectronicoInstance.registros.last().archivosResultado.detalles.last()?.estatus[0] == 1) {
                    
                    tituloElectronicoInstance.registros.cancelaciones?.each {
                        if (it.cancelado) {
                            cancelado = true
                            return
                        }
                    }
                    
                    if (!cancelado) {
                        registrado = true
                    }
                }
            }
        }
        
        if (cancelado) {
            flash.error = "El titulo electronico se encuentra cancelado ante la DGP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        if (!registrado) {
            flash.error = "Es necesario que el titulo electronico se encuentre registrado con exito ante la DGP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }

        actionLoggingService.log("Consultando formato de representación gráfica...")
        
        def formato = FormatoRepresentacionGrafica.findByActivo(true,[sort: "id", order: "desc", max: 1])
        
        if (!formato) {
            flash.error = "No se encontro el formato de la representacion grafica"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Formato: ${formato?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando representación gráfica registrada...")
        
        def representacion = RepresentacionGrafica.findByTituloElectronicoAndActivo(tituloElectronicoInstance, true, [sort: "id", order: "desc", max: 1])

        if (!representacion) {

            actionLoggingService.log("Registrando nueva representación gráfica...")
            
            def codigo = tituloElectronicoService.generarCodigoRepresentacion()
            representacion = new RepresentacionGrafica(
                tituloElectronico: tituloElectronicoInstance,
                formato: formato,
                codigo: codigo,
                activo: true
            )

            if (!representacion.save(flush: true)) {
                flash.error = "No se pudo guardar el registro de la representación gráfica"
                
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "index"
                return
            }

        }
                
        if (!representacion?.id) {
            flash.error = "No se pudo registrar la representación gráfica"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Representación Gráfica: ${representacion?.toPlainText("\t")}")

        /**
         * 
         * La descarga no se limitara
         * 
         * if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            actionLoggingService.log("Verificando descarga ya realizada...")
            
            if (representacion.descargado) {
                flash.error = "La representación gráfica ya fue descargada con anterioridad"
            
                actionLoggingService.log("Error: ${flash.error}")

                redirect action: "index"
                return
            }
        }*/
        
        
        actionLoggingService.log("Generando representación gráfica...")
        
        def pdfStream = tituloElectronicoService.generarRepresentacionGrafica(representacion)
        
        if (!pdfStream) {
            flash.error = "No se pudo generar la representacion grafica"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }

        /*
         * 
         * La descarga no se limitara
         * 
         * if (SpringSecurityUtils.ifAnyGranted("ROLE_FIRMANTE,ROLE_GESTOR")) {
            if (tituloElectronicoInstance?.institucion?.cveInstitucion in institucionesEducativas*.clave) {
                representacion.descargasInstitucion = (representacion?.descargasInstitucion?:0) + 1
                representacion.descargado = true
                representacion.ultimaDescargaInstitucion = new Date()
            }
        }*/
        
        representacion.descargas = (representacion?.descargas?:0) + 1
        representacion.ultimaDescarga = new Date()

        representacion.save(flush: true)

        def pdfBytes = pdfStream.toByteArray()
        response.setHeader("Content-disposition", "attachment; filename=${formato?.nombre}_${tituloElectronicoInstance.folioControl}.pdf")
        response.contentType = "application/pdf"
        response.outputStream << pdfBytes

    }
    
    def institucionNoAsignada(){
        
    }
    
    def registroLibro(TituloElectronico tituloElectronicoInstance) {
        
        if (!tituloElectronicoInstance) {
            flash.error = "Seleccione un titulo electronico"
            redirect action: "index"
            return
        }
        
        if (!tituloElectronicoInstance?.paquete?.estatus?.id in [2,3]) {
            flash.error = "El titulo electronico no se puede registrar en libro"
            redirect action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        def observaciones = Observacion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        
        /*if (observaciones) {
            flash.error = "El titulo electronico tiene OBSERVACIONES y no puede registrarse"
            redirect action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }*/
       
        respond tituloElectronicoInstance
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Registro de título electrónico en libro")
    @SpringUserIdentification
    @PrintCustomLog
    @Transactional
    def registrarLibro() {
        actionLoggingService.log("== Registro de título electrónico en libro - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
                        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuario(usuario)
        def institucionesEducativas = usuarioInstitucion*.institucionEducativa
                
        actionLoggingService.log("Consultando título electrónico a registrar en libro...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def tituloElectronicoInstance = TituloElectronico?.findById(params?.id)    
        
        if (!tituloElectronicoInstance) {
            flash.error="Es necesario indicar el título electrónico a registrar en libro"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Título electrónico (original): ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        if (!tituloElectronicoInstance?.paquete?.estatus?.id in [2,3]) {
            flash.error = "El titulo electronico no se puede registrar en libro"
            redirect action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        if (tituloElectronicoInstance.autenticacion) {
            flash.error = "El título electrónico ya se encuentra autentiacado"
            redirect controller: "tituloElectronico", action: "consultarTitulo", id: tituloElectronicoInstance?.id
            return
        }
        
        /*def observaciones = Observacion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        
        if (observaciones) {
            flash.error = "El titulo electronico tiene OBSERVACIONES y no puede registrarse"
            redirect action: "index", id: observacionInstance?.tituloElectronico?.id
            return
        }*/
        
        actionLoggingService.log("Estableciendo información del libro...")
       
        tituloElectronicoInstance?.libro=params?.libro?.trim()
        tituloElectronicoInstance?.foja=params?.foja?.trim()
        
        if (params?.fechaRegistroLibroDia && params?.fechaRegistroLibroMes && params?.fechaRegistroLibroAnio) {
            tituloElectronicoInstance?.fechaRegistroLibro=new SimpleDateFormat("dd/MM/yyyy").parse("${params?.fechaRegistroLibroDia?:""}/${params?.fechaRegistroLibroMes?:""}/${params?.fechaRegistroLibroAnio?:""}")
        }
        
        if (tituloElectronicoInstance.hasErrors()) {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'capturarTitulo'
            return
        }  
        
        actionLoggingService.log("Título electrónico (actualizado): ${tituloElectronicoInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando libro...")
        
        if(tituloElectronicoInstance.save(flush:true)) {
            flash.message="Título electrónico registrado en libro correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            respond tituloElectronicoInstance, view:'consultarTitulo'
            return
        } else {
            flash.error= tituloElectronicoInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond tituloElectronicoInstance.errors, view:'registroLibro'
            return
        }
        
        
    }
    
    @ActionLogging
    @ActionType("Gestión de títulos electrónicos")
    @CustomActionName("Verificación de registro de título electrónico")
    @SpringUserIdentification
    @PrintCustomLog
    def verificacion(){
        actionLoggingService.log("== Verificación de registro de título electrónico - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")       
                
        actionLoggingService.log("Consultando representación gráfica a verificar...")
        
        actionLoggingService.log("Código: ${params?.codigo}")
        
        def representacion = RepresentacionGrafica.findByCodigoAndActivo(params?.codigo, true)
        
        if (!representacion) {
            flash.error="Es necesario indicar el código de la representación gráfica a verificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            return
        }
        
        actionLoggingService.log("Representación Gráfica: ${representacion?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando título electrónico a verificar...")
                
        def tituloElectronicoInstance = representacion.tituloElectronico
        
        if (!tituloElectronicoInstance) {
            flash.error="Es necesario indicar el título electrónico a verificar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            return
        }
        
        actionLoggingService.log("Título electrónico: ${tituloElectronicoInstance.toPlainText("\t")}")
        
        actionLoggingService.log("Verificando registro exitoso ante la DGP...")
            
        def registrado = false
        def cancelado = false
        if (tituloElectronicoInstance?.registros) {
            if (tituloElectronicoInstance.registros.last()?.archivosResultado?.detalles) {
                if (tituloElectronicoInstance.registros.last().archivosResultado.detalles.last()?.estatus[0] == 1) {
                    
                    tituloElectronicoInstance.registros.cancelaciones?.each {
                        if (it.cancelado) {
                            cancelado = true
                            return
                        }
                    }
                    
                    if (!cancelado) {
                        registrado = true
                    }
                }
            }
        }
        
        if (cancelado) {
            flash.error = "El título electrónico se encuentra cancelado ante la DGP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            return
        }
        
        if (!registrado) {
            flash.error = "El título electrónico no se encuentra registrado con éxito ante la DGP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            return
        }

        /*
         * 
         * No se registra libro y foja
         * 
         * actionLoggingService.log("Verificando registro de libro y foja...")
        
        if (!tituloElectronicoInstance?.libro || !tituloElectronicoInstance?.foja) {
            flash.error = "El titulo electronico no cuenta con registro en libro y foja"

            actionLoggingService.log("Error: ${flash.error}")
            
            return
        }*/
        
        [tituloElectronicoInstance: tituloElectronicoInstance]
        
    }
    
    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tituloElectronico.label', default: 'TituloElectronico'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
