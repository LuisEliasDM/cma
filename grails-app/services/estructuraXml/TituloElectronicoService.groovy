package estructuraXml

import java.util.UUID
import catalogo.*
import java.text.SimpleDateFormat
import revision.Observacion
import revision.Correccion
import org.codehaus.groovy.grails.core.io.ResourceLocator
//Jasper
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.codehaus.groovy.grails.web.util.WebUtils
import java.util.Hashtable
import com.google.zxing.EncodeHintType

class TituloElectronicoService {
    def mailService
    ResourceLocator grailsResourceLocator
    def firmaElectronicaService
    def grailsApplication
    def dataSource

    def generarIdentificadorAleatorio(tamano) {
        def identificador = ""
        
        def posible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < tamano; i++) {
            def r = new Random();
            identificador = identificador + posible.charAt(r.nextInt(posible.length() - 2));
        }
        
        return identificador
    }
    
    def generaIdentificadorConsecutivo(Integer id, Date fechaExpedicion) {
        def identificador = ""
        
        if (!fechaExpedicion) {
            return ""
        }
        
        def anio = fechaExpedicion?.format("yyyy")?.toInteger()
                
        def fechaInicio = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/${anio}")
        def fechaFin = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/${anio}")
        
        def titulosAnio = TituloElectronico.createCriteria().list {
            projections {
                count()
            }
            expedicion {
                and {
                    between("fechaExpedicion", fechaInicio, fechaFin)
                }
            }
            if (id) {
                and {
                    ne("id", id)
                }
            }
        }
                
        identificador = "${(titulosAnio[0]?:0) + 1}"
                
        return identificador
    }
    
    def generaIdentificadorEstructurado(TituloElectronico tituloElectronicoInstance){
        def identificador = ""
                
        identificador = "TE"
        identificador += "17"
        identificador += (tituloElectronicoInstance?.expedicion?.fechaExpedicion?.format("yy")?:"")
        identificador += (generaIdentificadorConsecutivo(tituloElectronicoInstance?.id, tituloElectronicoInstance?.expedicion?.fechaExpedicion)?:"").padLeft(5,"0")
        
        return identificador
    }
    
    def generarCodigoRepresentacion() {
        def codigo = ""
        
        def posible = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < 20; i++) {
            def r = new Random();
            codigo = codigo + posible.charAt(r.nextInt(posible.length() - 2));
        }
        
        return codigo
    }
    
    def permisoTitulo(TituloElectronico tituloElectronicoInstance, String accion){
        def permiso = [
            permitido: false,
            descripcion: "Accion no permitida"
        ]
        
        def estatus = estatusTitulo(tituloElectronicoInstance)
        
        switch (accion) {
            case "asignarPaquete":
                
                def fechaInicioRezago = Date.parse("yyyy-MM-dd", "2018-10-01")
                def fechaFinRezago = Date.parse("yyyy-MM-dd", "2019-06-30")
                if (tituloElectronicoInstance?.expedicion?.fechaExpedicion >= fechaInicioRezago && tituloElectronicoInstance?.expedicion?.fechaExpedicion <= fechaFinRezago) {
                    permiso.descripcion = "El título electrónico tiene fecha de expedición anterior al 1ro de Julio de 2019, por lo que no es posible asignar el paquete"
                    return permiso
                }
                
                if (estatus.id in [4,8]) {
                    permiso.permitido = true
                } else {
                    permiso.descripcion = "${estatus.descripcionLarga}, por lo que no es posible asignar el paquete"
                }
            
                break
                
            case "modificarTitulo":
                
                if (estatus.id in [1,2,3,4,7,23,25,27,101,102,103,104,108]) {
                    permiso.permitido = true
                } else {
                    permiso.descripcion = "${estatus.descripcionLarga}, por lo que no es posible modificar el título electrónico"
                }
            
                break
             case "eliminarTitulo":
                
                if (estatus.id in [1,2,3,4,101,102,103,104]) {
                    permiso.permitido = true
                } else {
                    permiso.descripcion = "${estatus.descripcionLarga}, por lo que no es posible eliminar el título electrónico"
                }
            
                break
            case "cambioObservacionTitulo":
                if (estatus.id in [23,24,25,26,27,28]) {
                    permiso.permitido = true
                } else {
                    permiso.descripcion = "${estatus.descripcionLarga}, por lo que no es posible modificar las observaciones el título electrónico"
                }
            
                break
            case "autenticarTitulo":
                
                if (estatus.id in [12,13,14,104,105,106,107,108]) {
                    permiso.permitido = true
                } else {
                    permiso.descripcion = "${estatus.descripcionLarga}, por lo que no es posible autenticar el título electrónico"
                }
            
                break
            case "registrarTitulo":
                if (estatus.id in [14,17,104,108]) {
                    permiso.permitido = true
                } else {
                    permiso.descripcion = "${estatus.descripcionLarga}, por lo que no es posible cargar el título electrónico"
                }
            
                break
            case "cancelarRegistroTitulo":
                if (estatus.id in [18,19,21,22,109,110]) {
                    permiso.permitido = true
                } else {
                    permiso.descripcion = "${estatus.descripcionLarga}, por lo que no es posible cancelar el registro del título electrónico"
                }
            
                break
        }
        
        return permiso
    }
    
    def estatusTitulo(TituloElectronico tituloElectronico) {
        def estatus = [
            id: 0,
            descripcion: ""
        ]
        
        def tituloElectronicoInstance = TituloElectronico.get(tituloElectronico?.id)
        
        if (!tituloElectronicoInstance) {
            return estatus
        }
                
        estatus = estatusTituloRezago(tituloElectronicoInstance)
                
        if (!(estatus.id in [18, 109])) {
            def validacion = validarDatos(tituloElectronicoInstance)
            if (!validacion?.estatus) {
                estatus.id = estatus.id
                estatus.inconcistencia = validacion.descripcion
                estatus.descripcion = "${estatus.descripcion} {INCONSISTENCIA EN LA INFORMACIÓN DEL TÍTULO}"
                estatus.sugerencia = "Es necesario que la institución educativa realice la corrección de la información capturada"
            }
        }
                
        return estatus
        
    }
        
    def calcularEstatusTitulo(TituloElectronico tituloElectronicoInstance){
        def estatus = [
            id: 0,
            descripcion: ""
        ]
        
        if (!tituloElectronicoInstance) {
            return estatus
        }
                
        def fechaInicioRezago = Date.parse("yyyy-MM-dd", "2018-10-01")
        def fechaFinRezago = Date.parse("yyyy-MM-dd", "2019-06-30")
        if (tituloElectronicoInstance?.expedicion?.fechaExpedicion >= fechaInicioRezago && tituloElectronicoInstance?.expedicion?.fechaExpedicion <= fechaFinRezago) {
            return estatusTituloRezago(tituloElectronicoInstance)
        }
        
        def observaciones = Observacion.findAllByTituloElectronicoAndEstatus(tituloElectronicoInstance, true)
        //def correccionVigente = Correccion.executeQuery("SELECT c FROM Correccion c WHERE c.tituloElectronico = :te AND c.estatus = 1 AND DATE(current_time()) <= DATE_ADD(DATE(c.dateCreated), INTERVAL c.dias DAY)", [te: tituloElectronicoInstance])
        
        def correccionVigente
        use(groovy.time.TimeCategory) {
            tituloElectronicoInstance?.correcciones?.each { c ->
                if (c.estatus) {
                    if (new Date() <= (c.dateCreated + (c.dias).days)) {
                        correccionVigente = c
                        return
                    }
                }
            }
        }
        
        if (tituloElectronicoInstance?.firmaResponsables) {
            if (tituloElectronicoInstance?.firmaResponsables?.firmaResponsable) {
                def institucionEducativa = InstitucionEducativa.findByClave(tituloElectronicoInstance?.institucion?.cveInstitucion)                
                def firmasInstitucion = firmaElectronicaService.consultarFirmasNecesarias(institucionEducativa?.clave)
                
                if (tituloElectronicoInstance?.firmaResponsables?.firmaResponsable?.size() >= firmasInstitucion) {
                    if (tituloElectronicoInstance?.paquete) {

                        if (observaciones) {
                            estatus.descripcion = "OBSERVADO. ${observaciones.size()} observacion(es) realizada(s)."
                            
                            if (tituloElectronicoInstance?.correcciones) {
                                def ultimaCorreccion = tituloElectronicoInstance?.correcciones?.last()
                                
                                if (correccionVigente) {
                                    estatus.id = 7
                                    estatus.descripcion = "${estatus.descripcion} Rectificación de información pendiente"
                                    estatus.sugerencia = "Es necesario que la institución educativa realice la corrección de la información capturada"
                                    estatus.descripcionLarga = "El título electrónico se encuentra disponible para realizar correcciones"
                                } else {
                                    estatus.id = 6
                                    estatus.descripcion = "${estatus.descripcion} Entrega de expediente(s) pendiente [D2]"
                                    estatus.sugerencia = "Es necesario que la autoridad estatal realice la entrega de los expedientes físicos"
                                    estatus.descripcionLarga = "El título electrónico tiene observaciones"
                                }
                                
                            } else {
                                estatus.id = 5
                                estatus.descripcion = "${estatus.descripcion} Entrega de expediente(s) pendiente [D1]"
                                estatus.sugerencia = "Es necesario que la autoridad estatal realice la entrega de los expedientes físicos"
                                estatus.descripcionLarga = "El título electrónico tiene observaciones"
                            }
                            
                            
                            return estatus
                        }
                        
                        
                        if (tituloElectronicoInstance?.paquete?.estatus?.id == 1) {
                            estatus.id = 8
                            estatus.descripcion = "${tituloElectronicoInstance?.paquete?.estatus?.descripcion}. Revisión pendiente"
                            estatus.sugerencia = "Es necesario que la autoridad estatal realice la revisión de los expedientes físicos y electrónicos"
                            estatus.descripcionLarga = "El paquete se encuentra en ventanilla"
                        } else if (tituloElectronicoInstance?.paquete?.estatus?.id == 2) {
                            estatus.id = 9
                            estatus.descripcion = "${tituloElectronicoInstance?.paquete?.estatus?.descripcion}. Cotejo pendiente"
                            estatus.sugerencia = "Es necesario que la autoridad estatal finalice la revisión de los expedientes físicos y electrónicos"
                            estatus.descripcionLarga = "El paquete se encuentra en proceso de revisión"
                        } else if (tituloElectronicoInstance?.paquete?.estatus?.id == 3) {
                            
                            def registro = false
                            if (tituloElectronicoInstance?.registros) {
                                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado) {
                                    if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles) {
                                        if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus == 1) {
                                            registro = true
                                        }
                                    }
                                }
                            }
                            
                            if (registro) {
                                estatus.id = 18
                                estatus.descripcion = "REGISTRADO EN D.G.P. "
                                estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus + " - "
                                estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.descripcion + ". Entrega de expediente(s) pendiente"
                                estatus.sugerencia = ""
                                estatus.descripcionLarga = "El título electrónico se registró exitosamente ante la D.G.P."
                            } else if (tituloElectronicoInstance?.autenticacion) {
                                estatus.id = 12
                                estatus.descripcion = "AUTENTICADO. Registro ante D.G.P. pendiente"
                                estatus.sugerencia = "Es necesario que la autoridad estatal finalice la autenticación"
                                estatus.descripcionLarga = "El paquete se encuentra en proceso de autenticación"
                            } else {
                                estatus.id = 10
                                estatus.descripcion = "${tituloElectronicoInstance?.paquete?.estatus?.descripcion}. Autenticación pendiente"
                                estatus.sugerencia = "Es necesario que la autoridad estatal finalice el cotejo de los expedientes físicos y electrónicos"
                                estatus.descripcionLarga = "El paquete se encuentra en proceso de cotejo"
                            }
                            
                        } else if (tituloElectronicoInstance?.paquete?.estatus?.id == 4) {
                            estatus.id = 11
                            estatus.descripcion = "${tituloElectronicoInstance?.paquete?.estatus?.descripcion}. ${tituloElectronicoInstance?.paquete?.motivoRechazoCotejo}. Entrega de expediente(s) pendiente"
                            estatus.sugerencia = "Es necesario que la autoridad estatal realice la entrega de los expedientes físicos"
                            estatus.descripcionLarga = "El paquete se fué rechazado"
                        } else if (tituloElectronicoInstance?.paquete?.estatus?.id == 5) {
                            
                            def registro = false
                            if (tituloElectronicoInstance?.registros) {
                                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado) {
                                    if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles) {
                                        if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus == 1) {
                                            registro = true
                                        }
                                    }
                                }
                            }
                            
                            if (registro) {
                                estatus.id = 18
                                estatus.descripcion = "REGISTRADO EN D.G.P. "
                                estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus + " - "
                                estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.descripcion + ". Entrega de expediente(s) pendiente"
                                estatus.sugerencia = ""
                                estatus.descripcionLarga = "El título electrónico se registró exitosamente ante la D.G.P."
                            } else if (tituloElectronicoInstance?.autenticacion) {
                                estatus.id = 12
                                estatus.descripcion = "AUTENTICADO. Registro ante D.G.P. pendiente"
                                estatus.sugerencia = "Es necesario que la autoridad estatal finalice la autenticación"
                                estatus.descripcionLarga = "El paquete se encuentra en proceso de autenticación"
                            } else {
                                estatus.id = 13
                                estatus.descripcion = "${tituloElectronicoInstance?.paquete?.estatus?.descripcion}. Autenticación pendiente"
                                estatus.sugerencia = "Es necesario que la autoridad estatal finalice la autenticación"
                                estatus.descripcionLarga = "El paquete se encuentra en proceso de autenticación"
                            }
                            
                            
                        } else if (tituloElectronicoInstance?.paquete?.estatus?.id == 6 || tituloElectronicoInstance?.paquete?.estatus?.id == 7) {
                            
                            if (tituloElectronicoInstance?.registros) {
                                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado) {
                                    if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles) {
                                        
                                        
                                        if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus == 1) {
                                                
                                            if (!tituloElectronicoInstance?.registros?.last()?.cancelaciones) {
                                                estatus.id = 18
                                                estatus.descripcion = "REGISTRADO EN D.G.P. "
                                                estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus + " - "
                                                estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.descripcion + ". Entrega de expediente(s) pendiente"
                                                estatus.sugerencia = ""
                                                estatus.descripcionLarga = "El título electrónico se registró exitosamente ante la D.G.P."
                                            } else {
                                                if (tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.cancelado == true) {
                                                    estatus.id = 20
                                                    estatus.descripcion = "CANCELADO EN D.G.P. "
                                                    estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.codigoEstatusCancelacion + " - "
                                                    estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.mensajeEstatusCancelacion + ""
                                                    estatus.sugerencia = ""
                                                    estatus.descripcionLarga = "El título electrónico se canceló exitosamente ante la D.G.P."
                                                } else {
                                                    estatus.id = 19
                                                    estatus.descripcion = "EN CANCELACIÓN EN D.G.P. "
                                                    estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.codigoEstatusCancelacion + " - "
                                                    estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.mensajeEstatusCancelacion + ". Cancelación pendiente"
                                                    estatus.sugerencia = ""
                                                    estatus.descripcionLarga = "El título electrónico se canceló exitosamente ante la D.G.P."
                                                }
                                            }
                                            
                                        } else {
                                            estatus.id = 17
                                            estatus.descripcion = "ENVIADO A D.G.P. "
                                            estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus + " - "
                                            estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.descripcion + ". "
                                            if (tituloElectronicoInstance?.paquete?.estatus?.id == 6) {
                                                estatus.descripcion += "Resolución pendiente"
                                            } else if (tituloElectronicoInstance?.paquete?.estatus?.id == 7) {
                                                estatus.descripcion += "Sin resolución"
                                            }
                                            estatus.sugerencia = "Es necesario que la autoridad estatal verifique la información capturada"
                                            estatus.descripcionLarga = "El título electrónico se envió a registro ante la D.G.P. y se encontraron observaciones"
                                        }
                                        
                                        
                                    } else {
                                        estatus.id = 16
                                        estatus.descripcion = "ENVIADO A D.G.P. Resultado del registro pendiente [D2]"
                                        estatus.sugerencia = "Es necesario que la autoridad estatal realice la consulta del proceso de registro ante la D.G.P."
                                        estatus.descripcionLarga = "El título electrónico se envió a registro ante la D.G.P y se encuentra en espera del resultado"
                                    }
                                } else {
                                    estatus.id = 15
                                    estatus.descripcion = "ENVIADO A D.G.P. Resultado del registro pendiente [D1]"
                                    estatus.sugerencia = "Es necesario que la autoridad estatal realice la consulta del proceso de registro ante la D.G.P."
                                    estatus.descripcionLarga = "El título electrónico se envió a registro ante la D.G.P y se encuentra en espera del resultado"
                                }
                            } else {
                                estatus.id = 14
                                estatus.descripcion = "EN REGISTRO ANTE D.G.P. Registro ante D.G.P. pendiente"
                                estatus.sugerencia = "Es necesario que la autoridad estatal realice el registro ante la D.G.P."
                                estatus.descripcionLarga = "El título electrónico se encuentra en proceso de registro ante la D.G.P."
                            }
                            
                        } else if (tituloElectronicoInstance?.paquete?.estatus?.id == 8) {
                            estatus.id = 21
                            estatus.descripcion = "${tituloElectronicoInstance?.paquete?.estatus?.descripcion}. Entrega de expediente(s) disponible"
                            estatus.descripcionLarga = "El paquete se encuentra en proceso de entrega de expedientes físicos"
                        } else if (tituloElectronicoInstance?.paquete?.estatus?.id == 9) {
                            estatus.id = 22
                            estatus.descripcion = "${tituloElectronicoInstance?.paquete?.estatus?.descripcion}. Trámite finalizado"
                            estatus.descripcionLarga = "El paquete fué entregado"
                        }
                    } else {
                        estatus.id = 4
                        estatus.descripcion = "FIRMADO. Pago de derechos y entrega en ventanilla pendiente"
                        estatus.sugerencia = "Es necesario que la institución educativa realice el pago de derechos y entregue los expedientes físicos en ventanilla"
                        estatus.descripcionLarga = "El título electrónico se encuentra firmado por todos los firmantes habilitados"
                    }
                } else {
                    if (correccionVigente) {
                        estatus.id = 27
                        estatus.descripcion = "OBSERVADO (firmas incompletas). ${firmasInstitucion - tituloElectronicoInstance?.firmaResponsables?.firmaResponsable?.size()} firma(s) electrónica(s) pendiente(s) [D1]"
                        estatus.sugerencia = "La(s) firma(s) electrónica(s) de la(s) autoridad(es) de la institución educativa, se encuentran incompletas."
                        estatus.descripcionLarga = "El título electrónico se encuentra firmado por algunos de los firmantes habilitados"
                    } else if (observaciones) {
                        estatus.id = 28
                        estatus.descripcion = "OBSERVADO (firmas incompletas). ${firmasInstitucion - tituloElectronicoInstance?.firmaResponsables?.firmaResponsable?.size()} firma(s) electrónica(s) pendiente(s) [D2]"
                        estatus.sugerencia = "La(s) firma(s) electrónica(s) de la(s) autoridad(es) de la institución educativa, se encuentran incompletas."
                        estatus.descripcionLarga = "El título electrónico se encuentra firmado por algunos de los firmantes habilitados"
                    } else {
                        estatus.id = 3
                        estatus.descripcion = "FIRMADO (incompleto). ${firmasInstitucion - tituloElectronicoInstance?.firmaResponsables?.firmaResponsable?.size()} firma(s) electrónica(s) pendiente(s)"
                        estatus.sugerencia = "La(s) firma(s) electrónica(s) de la(s) autoridad(es) de la institución educativa, se encuentran incompletas."
                        estatus.descripcionLarga = "El título electrónico se encuentra firmado por algunos de los firmantes habilitados"
                    }
                }
            } else {
                if (correccionVigente) {
                    estatus.id = 25
                    estatus.descripcion = "OBSERVADO. Firma electrónica pendiente [D3]"
                    estatus.sugerencia = "Es necesario que la(s) autoridad(es) de la institución educativa realice(n) la firma electrónica"
                    estatus.descripcionLarga = "El título electrónico se encuentra disponible para realizar correcciones"
                } else if (observaciones) {
                    estatus.id = 26
                    estatus.descripcion = "OBSERVADO. Firma electrónica pendiente [D4]"
                    estatus.sugerencia = "Es necesario que la(s) autoridad(es) de la institución educativa realice(n) la firma electrónica"
                    estatus.descripcionLarga = "El título electrónico se encuentra capturado sin firma(s) electrónica(s)"
                } else {
                    estatus.id = 2
                    estatus.descripcion = "CAPTURADO. Firma electrónica pendiente [D2]"
                    estatus.sugerencia = "Es necesario que la(s) autoridad(es) de la institución educativa realice(n) la firma electrónica"
                    estatus.descripcionLarga = "El título electrónico se encuentra capturado sin firma(s) electrónica(s)"
                }
            }
        } else {
            if (correccionVigente) {
                estatus.id = 23
                estatus.descripcion = "OBSERVADO. Firma electrónica pendiente [D1]"
                estatus.sugerencia = "Es necesario que la(s) autoridad(es) de la institución educativa realice(n) la firma electrónica"
                estatus.descripcionLarga = "El título electrónico se encuentra disponible para realizar correcciones"
            } else if (observaciones) {
                estatus.id = 24
                estatus.descripcion = "OBSERVADO. Firma electrónica pendiente [D2]"
                estatus.sugerencia = "Es necesario que la(s) autoridad(es) de la institución educativa realice(n) la firma electrónica"
                estatus.descripcionLarga = "El título electrónico se encuentra capturado sin firma(s) electrónica(s)"
            } else {
                estatus.id = 1
                estatus.descripcion = "CAPTURADO. Firma electrónica pendiente [D1]"
                estatus.sugerencia = "Es necesario que la(s) autoridad(es) de la institución educativa realice(n) la firma electrónica"
                estatus.descripcionLarga = "El título electrónico se encuentra capturado sin firma(s) electrónica(s)"
            }
        }
        
        return estatus
    }
    
    def estatusTituloRezago(TituloElectronico tituloElectronicoInstance){
        def estatus = [
            id: 0,
            descripcion: ""
        ]
        
        if (tituloElectronicoInstance?.firmaResponsables) {
            
            if (tituloElectronicoInstance?.firmaResponsables?.firmaResponsable) {
                
                def institucionEducativa = InstitucionEducativa.findByClave(tituloElectronicoInstance?.institucion?.cveInstitucion)
                def firmasInstitucion = firmaElectronicaService.consultarFirmasNecesarias(institucionEducativa?.clave)
                
                if (tituloElectronicoInstance?.firmaResponsables?.firmaResponsable?.size() >= firmasInstitucion) {
                    
                    if (tituloElectronicoInstance?.registros) {
                            
                        if (tituloElectronicoInstance?.registros?.last()?.archivosResultado) {
                            if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles) {

                                if (tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus == 1) {

                                    if (!tituloElectronicoInstance?.registros?.last()?.cancelaciones) {
                                        estatus.id = 109
                                        estatus.descripcion = "REGISTRADO EN D.G.P. "
                                        estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus + " - "
                                        estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.descripcion
                                        estatus.sugenrencia = ""
                                        estatus.descripcionLarga = "El título electrónico se registró exitosamente ante la D.G.P."
                                    } else {
                                        if (tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.cancelado == true) {
                                            estatus.id = 111
                                            estatus.descripcion = "CANCELADO EN D.G.P. "
                                            estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.codigoEstatusCancelacion + " - "
                                            estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.mensajeEstatusCancelacion + ""
                                            estatus.sugerencia = ""
                                            estatus.descripcionLarga = "El título electrónico se canceló exitosamente ante la D.G.P."
                                        } else {
                                            estatus.id = 110
                                            estatus.descripcion = "EN CANCELACIÓN EN D.G.P. "
                                            estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.codigoEstatusCancelacion + " - "
                                            estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.cancelaciones?.last()?.mensajeEstatusCancelacion + ". Cancelación pendiente"
                                            estatus.sugerencia = ""
                                            estatus.descripcionLarga = "El título electrónico se canceló exitosamente ante la D.G.P."
                                        }
                                    }

                                } else {
                                    estatus.id = 108
                                    estatus.descripcion = "ENVIADO A D.G.P. "
                                    estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.estatus + " - "
                                    estatus.descripcion += tituloElectronicoInstance?.registros?.last()?.archivosResultado?.last()?.detalles?.last()?.descripcion
                                    estatus.sugenrencia = "Es necesario que la institución educativa realice la corrección de la información capturada"
                                    estatus.descripcionLarga = "El título electrónico se envió a registro ante la D.G.P. y se encontraron observaciones"
                                }
                            } else {
                                estatus.id = 107
                                estatus.descripcion = "ENVIADO A D.G.P. Esperando resultados [D2]"
                                estatus.sugerencia = "Es necesario que la autoridad estatal realice la consulta del proceso de registro ante la D.G.P."
                                estatus.descripcionLarga = "El título electrónico se envió a registro ante la D.G.P y se encuentra en espera del resultado"
                            }
                        } else {
                            estatus.id = 106
                            estatus.descripcion = "ENVIADO A D.G.P. Esperando resultados [D1]"
                            estatus.sugerencia = "Es necesario que la autoridad estatal realice la consulta del proceso de registro ante la D.G.P."
                            estatus.descripcionLarga = "El título electrónico se envió a registro ante la D.G.P y se encuentra en espera del resultado"
                        }

                    } else {
                        estatus.id = 104
                        estatus.descripcion = "FIRMADO. Registro en D.G.P pendiente"
                        estatus.sugerencia = "Es necesario que la autoridad estatal realice el registro ante la D.G.P."
                        estatus.descripcionLarga = "El título electrónico se encuentra autenticado por la autoridad estatal"
                    }
                    
                } else {
                    estatus.id = 103
                    estatus.descripcion = "FIRMADO (incompleto). ${firmasInstitucion - tituloElectronicoInstance?.firmaResponsables?.firmaResponsable?.size()} firma(s) electrónica(s) pendiente(s)"
                    estatus.sugerencia = "La(s) firma(s) electrónica(s) de la(s) autoridad(es) de la institución educativa, se encuentran incompletas."
                    estatus.descripcionLarga = "El título electrónico se encuentra firmado por algunos de los firmantes habilitados"
                }
                
            } else {
                estatus.id = 102
                estatus.descripcion = "CAPTURADO. Firma electrónica pendiente [D2]"
                estatus.sugerencia = "Es necesario que la(s) autoridad(es) de la institución educativa realice(n) la firma electrónica"
                estatus.descripcionLarga = "El título electrónico se encuentra capturado sin firma(s) electrónica(s)"
            }
        } else {
            estatus.id = 101
            estatus.descripcion = "CAPTURADO. Firma electrónica pendiente [D1]"
            estatus.sugerencia = "Es necesario que la(s) autoridad(es) de la institución educativa realice(n) la firma electrónica"
            estatus.descripcionLarga = "El título electrónico se encuentra capturado sin firma(s) electrónica(s)"
        }
        
        return estatus
    }
    
    def validarDatos(TituloElectronico tituloElectronicoInstance){
        def resultado = [
            estatus: false,
            descripcion: ""
        ]
        
        if (!tituloElectronicoInstance) {
            return null
        }
        
        if (!tituloElectronicoInstance?.folioControl) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con folio de control"
            return resultado
        }
        
        //Datos de la institucion educativa
        
        if (!tituloElectronicoInstance?.institucion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con los datos de la institución"
            return resultado
        }
        
        if (!tituloElectronicoInstance.institucion?.cveInstitucion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con clave de institución"
            return resultado
        }
        
        def ie = InstitucionEducativa.findByClave(tituloElectronicoInstance.institucion.cveInstitucion)
        
        if (!ie) {
            resultado.estatus = false
            resultado.descripcion = "La clave de la institución educativa \"${tituloElectronicoInstance.institucion.cveInstitucion}\" no se encuentra en el catalogo"
            return resultado
        }
        
        if (!tituloElectronicoInstance.institucion?.nombreInstitucion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con nombre de institución"
            return resultado
        }
                
        if (ie?.descripcion != tituloElectronicoInstance.institucion.nombreInstitucion) {
            resultado.estatus = false
            resultado.descripcion = "El nombre de la institución educativa \"${tituloElectronicoInstance.institucion.nombreInstitucion}\" no coincide con el catalogo \"${ie?.descripcion}\""
            return resultado
        }
        
        //Datos de la carrera
        
        if (!tituloElectronicoInstance?.carrera) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con los datos de la carrera"
            return resultado
        }
        
        if (!tituloElectronicoInstance.carrera?.cveCarrera) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con clave de carrera"
            return resultado
        }
        
        def cr = CarreraInstitucion.findAllByInstitucionEducativaAndClave(ie, tituloElectronicoInstance.carrera.cveCarrera)
        
        if (!cr) {
            resultado.estatus = false
            resultado.descripcion = "La clave de carrera \"${tituloElectronicoInstance.carrera?.cveCarrera}\" no se encuentra en el catalogo o no se encuentra asignada a la institución educativa \"${ie.clave}\""
            return resultado
        }
        
        /*
         * En algunas carrenas no aplica N/A
         * 
         * if (!tituloElectronicoInstance.carrera?.numeroRvoe) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con número de RVOE de la carrera"
            return resultado
        }*/
        
        def cre
        cr.each { c ->
            if (tituloElectronicoInstance.carrera.numeroRvoe == c.rvoe) {
                cre = c
                return 
            }
        }
                
        if (!cre) {
            resultado.estatus = false
            resultado.descripcion = "El número de RVOE de la carrera \"${tituloElectronicoInstance.carrera.numeroRvoe}\" no coincide con el catalogo \"${cr?.first().rvoe}\""
            return resultado
        }
        
        if (!tituloElectronicoInstance.carrera?.nombreCarrera) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con nombre de carrera"
            return resultado
        }
        
        if (cre?.descripcion != tituloElectronicoInstance.carrera.nombreCarrera) {
            resultado.estatus = false
            resultado.descripcion = "El nombre de la carrera \"${tituloElectronicoInstance.carrera.nombreCarrera}\" no coincide con el catalogo \"${cre?.descripcion}\""
            return resultado
        }
        
        if (!tituloElectronicoInstance.carrera?.fechaInicio) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con fecha de inicio de estudios de la carrera"
            return resultado
        }
        
        if (!tituloElectronicoInstance.carrera?.fechaTerminacion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con fecha de terminación de estudios de la carrera"
            return resultado
        }
        
        if (!tituloElectronicoInstance.carrera?.idAutorizacionReconocimiento) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con tipo de autorización o reconocimiento (id) de la carrera"
            return resultado
        }
        
        if (cre?.autorizacionReconocimiento?.id != tituloElectronicoInstance.carrera.idAutorizacionReconocimiento) {
            resultado.estatus = false
            resultado.descripcion = "El tipo de autorización o reconocimiento (id) de la carrera \"${tituloElectronicoInstance.carrera.idAutorizacionReconocimiento}\" no coincide con el catalogo \"${cre?.autorizacionReconocimiento?.id}\""
            return resultado
        }
        
        /*
         * En algunas carrenas no aplica N/A
         * 
        if (tituloElectronicoInstance.carrera.idAutorizacionReconocimiento != 2) {
            resultado.estatus = false
            resultado.descripcion = "El tipo de autorización o reconocimiento de la carrera debe ser: \"2 - RVOE Estatal\""
            return resultado
        }*/
        
        if (!tituloElectronicoInstance.carrera?.autorizacionReconocimiento) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con tipo de autorización o reconocimiento (descripción) de la carrera"
            return resultado
        }
        
        if (cre?.autorizacionReconocimiento?.descripcion != tituloElectronicoInstance.carrera.autorizacionReconocimiento) {
            resultado.estatus = false
            resultado.descripcion = "El tipo de autorización o reconocimiento (descripción) de la carrera \"${tituloElectronicoInstance.carrera.autorizacionReconocimiento}\" no coincide con el catalogo \"${cre?.autorizacionReconocimiento?.descripcion}\""
            return resultado
        }
                
        //Datos del profesionista
        
        if (!tituloElectronicoInstance?.profesionista) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con los datos del profesionista"
            return resultado
        }
        
        if (!tituloElectronicoInstance.profesionista?.curp) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con CURP del profesionista"
            return resultado
        }
        
        if (!tituloElectronicoInstance.profesionista?.nombre) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con nombre(s) del profesionista"
            return resultado
        }
        
        if (!tituloElectronicoInstance.profesionista?.primerApellido) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con primer apellido del profesionista"
            return resultado
        }
        
        if (!tituloElectronicoInstance.profesionista?.sexo) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con sexo/genero del profesionista"
            return resultado
        }
        
        if (!tituloElectronicoInstance.profesionista?.fechaNacimiento) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con fecha de nacimiento del profesionista"
            return resultado
        }
        
        if (!tituloElectronicoInstance.profesionista?.estadoNacimiento) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con estado de nacimiento del profesionista"
            return resultado
        }
        
        if (!tituloElectronicoInstance.profesionista?.correoElectronico) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con correo electrónico del profesionista"
            return resultado
        }
        
        //Datos de la expedicion del titulo
        
        if (!tituloElectronicoInstance?.expedicion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con los datos de expedición"
            return resultado
        }
        
        if (!tituloElectronicoInstance.expedicion?.fechaExpedicion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con fecha de expedición"
            return resultado
        }
        
        if (!tituloElectronicoInstance.expedicion?.idModalidadTitulacion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con modalidad de titulación (id)"
            return resultado
        }
        
        def mt = ModalidadTitulacion.get(tituloElectronicoInstance.expedicion.idModalidadTitulacion)
        
        if (!mt) {
            resultado.estatus = false
            resultado.descripcion = "La modalidad de titulación \"${tituloElectronicoInstance.expedicion.idModalidadTitulacion}\" no se encuentra en el catalogo"
            return resultado
        }
        
        if (!tituloElectronicoInstance.expedicion?.modalidadTitulacion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con modalidad de titulación (descripción)"
            return resultado
        }
        
        if (mt?.descripcion != tituloElectronicoInstance.expedicion.modalidadTitulacion) {
            resultado.estatus = false
            resultado.descripcion = "La modalidad de titulación (descripción) \"${tituloElectronicoInstance.expedicion.modalidadTitulacion}\" no coincide con el catalogo \"${mt?.descripcion}\""
            return resultado
        }
        
        if (!tituloElectronicoInstance.expedicion?.fechaExamenProfesional && !tituloElectronicoInstance.expedicion?.fechaExencionExamenProfesional) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con fecha de examen o exención del examen profesional"
            return resultado
        }
        
        if (tituloElectronicoInstance.expedicion.idModalidadTitulacion == 1) {
            
            if (!tituloElectronicoInstance.expedicion?.fechaExamenProfesional) {
                resultado.estatus = false
                resultado.descripcion = "El título electrónico no cuenta con fecha de examen profesional"
                return resultado
            }
            
        } else {
            
            if (!tituloElectronicoInstance.expedicion?.fechaExencionExamenProfesional) {
                resultado.estatus = false
                resultado.descripcion = "El título electrónico no cuenta con fecha de exención del examen profesional"
                return resultado
            }
            
        }
        
        if (!tituloElectronicoInstance.expedicion?.cumplioServicioSocial == null) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con cumplimiento de servicio social"
            return resultado
        }
        
        if (!tituloElectronicoInstance.expedicion?.idFundamentoLegalServicioSocial) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con fundamento legal del servicio social (id)"
            return resultado
        }
        
        def flss = FundamentoLegalServicioSocial.get(tituloElectronicoInstance.expedicion.idFundamentoLegalServicioSocial)
        
        if (!flss) {
            resultado.estatus = false
            resultado.descripcion = "El fundamento legal del servicio social \"${tituloElectronicoInstance.expedicion.idFundamentoLegalServicioSocial}\" no se encuentra en el catalogo"
            return resultado
        }
        
        if (!tituloElectronicoInstance.expedicion?.fundamentoLegalServicioSocial) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con fundamento legal del servicio social (descripción)"
            return resultado
        }
        
        if (flss?.descripcion != tituloElectronicoInstance.expedicion.fundamentoLegalServicioSocial) {
            resultado.estatus = false
            resultado.descripcion = "El fundamento legal del servicio social (descripción) \"${tituloElectronicoInstance.expedicion.fundamentoLegalServicioSocial}\" no coincide con el catalogo \"${flss?.descripcion}\""
            return resultado
        }
        
        if (!tituloElectronicoInstance.expedicion?.idEntidadFederativa) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con entidad federativa de expedición del título (id)"
            return resultado
        }
        
        def efe = EntidadFederativa.get(tituloElectronicoInstance.expedicion.idEntidadFederativa)
        
        if (!efe) {
            resultado.estatus = false
            resultado.descripcion = "La entidad federativa de expedición del título \"${tituloElectronicoInstance.expedicion.idEntidadFederativa}\" no se encuentra en el catalogo"
            return resultado
        }
        
        if (!tituloElectronicoInstance.expedicion?.entidadFederativa) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con entidad federativa de expedición del título (descripción)"
            return resultado
        }
        
        if (efe?.descripcion != tituloElectronicoInstance.expedicion.entidadFederativa) {
            resultado.estatus = false
            resultado.descripcion = "La entidad federativa de expedición del título (descripción) \"${tituloElectronicoInstance.expedicion.entidadFederativa}\" no coincide con el catalogo \"${efe?.descripcion}\""
            return resultado
        }
        
        if (tituloElectronicoInstance.expedicion.idEntidadFederativa != "17") {
            resultado.estatus = false
            resultado.descripcion = "La entidad federativa de expedición del título debe ser: \"17 - MORELOS\""
            return resultado
        }
                
        //Datos de antecedentes
        
        if (!tituloElectronicoInstance?.antecedente) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con los datos de antecedente de estudios"
            return resultado
        }
        
        if (!tituloElectronicoInstance.antecedente?.institucionProcedencia) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con nombre de la institución de procedencia"
            return resultado
        }
                
        if (!tituloElectronicoInstance.antecedente.institucionProcedencia.matches("[a-záéíóúñA-ZÁÉÍÓÚÑ°\"'&<>0-9 ,.\\-:(\\(|\\))]+")) {
            resultado.estatus = false
            resultado.descripcion = "El nombre de la institución de procedencia \"${tituloElectronicoInstance.antecedente.institucionProcedencia}\" solo puede contener caracteres de la A a la Z, números del 0 al 9 y signos de puntuación"
            return resultado
        }
        
        if (!tituloElectronicoInstance.antecedente?.idTipoEstudioAntecedente) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con tipo de estudios de antecedente (id)"
            return resultado
        }
        
        def tea = TipoEstudioAntecedente.get(tituloElectronicoInstance.antecedente.idTipoEstudioAntecedente)
        
        if (!tea) {
            resultado.estatus = false
            resultado.descripcion = "El tipo de estudios de antecedente \"${tituloElectronicoInstance.antecedente.idTipoEstudioAntecedente}\" no se encuentra en el catalogo"
            return resultado
        }
        
        if (!tituloElectronicoInstance.antecedente?.tipoEstudioAntecedente) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con tipo de estudios de antecedente (descripción)"
            return resultado
        }
        
        if (tea?.descripcion != tituloElectronicoInstance.antecedente.tipoEstudioAntecedente) {
            resultado.estatus = false
            resultado.descripcion = "El tipo de estudios de antecedente (descripción) \"${tituloElectronicoInstance.antecedente.tipoEstudioAntecedente}\" no coincide con el catalogo \"${tea?.descripcion}\""
            return resultado
        }
        
        if (!tituloElectronicoInstance.antecedente?.idEntidadFederativa) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con entidad federativa (id) de antecedente de estudios"
            return resultado
        }
        
        def efa = EntidadFederativa.get(tituloElectronicoInstance.antecedente.idEntidadFederativa)
        
        if (!efa) {
            resultado.estatus = false
            resultado.descripcion = "La entidad federativa del antecedente de estudios \"${tituloElectronicoInstance.antecedente.idEntidadFederativa}\" no se encuentra en el catalogo"
            return resultado
        }
        
        if (!tituloElectronicoInstance.antecedente?.entidadFederativa) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con entidad federativa (descripción) de antecedente de estudios"
            return resultado
        }
        
        if (efa?.descripcion != tituloElectronicoInstance.antecedente.entidadFederativa) {
            resultado.estatus = false
            resultado.descripcion = "La entidad federativa (descripción) del antecedente de estudios \"${tituloElectronicoInstance.antecedente.entidadFederativa}\" no coincide con el catalogo \"${efa?.descripcion}\""
            return resultado
        }
        
        if (!tituloElectronicoInstance.antecedente?.fechaTerminacion) {
            resultado.estatus = false
            resultado.descripcion = "El título electrónico no cuenta con fecha de terminación de los estudios de antecedente"
            return resultado
        }
        
        if (tituloElectronicoInstance.antecedente?.noCedula) {
            if (!tituloElectronicoInstance.antecedente.noCedula.matches("[0-9]+")) {
                resultado.estatus = false
                resultado.descripcion = "El número cédula de los estudios de antecedente debe ser un dato numérico"
                return resultado
            }
        }
        
        //Fechas carrera
        
        def toleranciaFechaTerminacionAntecente
        use(groovy.time.TimeCategory) {
            toleranciaFechaTerminacionAntecente = tituloElectronicoInstance.carrera.fechaInicio + 6.month
        }
        
        if (tituloElectronicoInstance.carrera.fechaInicio <= tituloElectronicoInstance.antecedente?.fechaTerminacion && tituloElectronicoInstance.antecedente?.fechaTerminacion > toleranciaFechaTerminacionAntecente) {
            resultado.estatus = false
            resultado.descripcion = "La fecha de inicio de estudios de la carrera \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaInicio)}\" debe ser posterior a la fecha de terminación de los estudios de antecedente \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.antecedente.fechaTerminacion)}\""
            return resultado
        }
                
        if (tituloElectronicoInstance.carrera.fechaTerminacion <= tituloElectronicoInstance.carrera.fechaInicio) {
            resultado.estatus = false
            resultado.descripcion = "La fecha de terminación de estudios de la carrera \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaTerminacion)}\" debe ser posterior a la fecha de inicio \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaInicio)}\""
            return resultado
        }
        
        /*
         * Es necesario validar de acuerdo al nivel o grado de la carrera. Licenciatura 3 años, Maestria 2 y Doctorado ?
         * use(groovy.time.TimeCategory) {
            
            def duracionCarrera = tituloElectronicoInstance.carrera.fechaTerminacion - tituloElectronicoInstance.carrera.fechaInicio
                        
            if ((duracionCarrera.days / 365) < 3) {
                resultado.estatus = false
                resultado.descripcion = "La duración de estudios de la carrera \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaInicio)}-\"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaTerminacion)}\" debe ser de al menos 3 años"
                return resultado
            }
            
        } 
        */ 
        
        // Fechas expedicion
        
        if (tituloElectronicoInstance.expedicion.fechaExpedicion < new Date().parse("dd/MM/yyyy", "01/10/2018")) {
            resultado.estatus = false
            resultado.descripcion = "La fecha de expedición del título electrónico \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.expedicion.fechaExpedicion)}\" debe ser a partir del \"1ro de Octubre de 2018\""
            return resultado
        }
        
        if (tituloElectronicoInstance.expedicion.fechaExpedicion <= tituloElectronicoInstance.carrera.fechaTerminacion) {
            resultado.estatus = false
            resultado.descripcion = "La fecha de expedición del título electrónico \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.expedicion.fechaExpedicion)}\" debe ser posterior a la fecha de terminación de estudios de la carrera \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaTerminacion)}\""
            return resultado
        }
        
        if (tituloElectronicoInstance.expedicion.idModalidadTitulacion == 1) {
            
            if (tituloElectronicoInstance.expedicion?.fechaExamenProfesional <= tituloElectronicoInstance.carrera.fechaTerminacion) {
                resultado.estatus = false
                resultado.descripcion = "La fecha de examen profesional \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.expedicion.fechaExamenProfesional)}\" debe ser posterior a la fecha de terminación de estudios de la carrera \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaTerminacion)}\""
                return resultado
            }
            
            if (tituloElectronicoInstance.expedicion?.fechaExamenProfesional > tituloElectronicoInstance.expedicion.fechaExpedicion) {
                resultado.estatus = false
                resultado.descripcion = "La fecha de examen profesional \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.expedicion.fechaExamenProfesional)}\" debe ser anterior a la fecha de expedición del título electrónico \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.expedicion.fechaExpedicion)}\""
                return resultado
            }
            
        } else {
            
            if (tituloElectronicoInstance.expedicion?.fechaExencionExamenProfesional <= tituloElectronicoInstance.carrera.fechaTerminacion) {
                resultado.estatus = false
                resultado.descripcion = "La fecha de exención de examen profesional \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.expedicion.fechaExencionExamenProfesional)}\" debe ser posterior a la fecha de terminación de estudios de la carrera \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaTerminacion)}\""
                return resultado
            }
            
            if (tituloElectronicoInstance.expedicion?.fechaExencionExamenProfesional > tituloElectronicoInstance.expedicion.fechaExpedicion) {
                resultado.estatus = false
                resultado.descripcion = "La fecha de exención de examen profesional \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.expedicion.fechaExencionExamenProfesional)}\" debe ser anterior a la fecha de expedición del título electrónico \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.expedicion.fechaExpedicion)}\""
                return resultado
            }
            
        }
        
        //Fechas de antecedentes
                
        use(groovy.time.TimeCategory) {
            toleranciaFechaTerminacionAntecente = tituloElectronicoInstance.carrera.fechaInicio + 6.month
        }
                
        if (tituloElectronicoInstance.antecedente.fechaTerminacion >= tituloElectronicoInstance.carrera.fechaInicio && tituloElectronicoInstance.antecedente.fechaTerminacion > toleranciaFechaTerminacionAntecente) {
            resultado.estatus = false
            resultado.descripcion = "La fecha de terminación del antecedente de estudios \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.antecedente.fechaTerminacion)}\" debe ser anterior a la fecha de inicio de estudios de la carrera \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.carrera.fechaInicio)}\""
            return resultado
        }
        
        if (tituloElectronicoInstance.antecedente.fechaInicio) {
            
            if (tituloElectronicoInstance.antecedente.fechaInicio > tituloElectronicoInstance.antecedente.fechaTerminacion) {
                resultado.estatus = false
                resultado.descripcion = "La fecha de inicio del antecedente de estudios \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.antecedente.fechaInicio)}\" debe ser anterior o igual a la fecha de terminación \"${new SimpleDateFormat("dd/MM/yyyy").format(tituloElectronicoInstance.antecedente.fechaTerminacion)}\""
                return resultado
            }
            
        }
        
        
        //Datos la(s) firma(s) electronica(s)
        
        //Datos de la autenticacion
        
        resultado.estatus = true
        
        return resultado
    }
    
    def generarRepresentacionGrafica(RepresentacionGrafica representacionInstance){
        if (!representacionInstance) {
            return null
        }
        
        def pdfStream = new ByteArrayOutputStream()
        
        try {
            String reportName, namaFile, dotJasper
            namaFile = representacionInstance.formato.nombre
            reportName = grailsApplication.mainContext.getResource("${representacionInstance.formato.folder}/${representacionInstance.formato.nombre}.jrxml").file.getAbsoluteFile()
            JasperCompileManager.compileReportToFile(reportName)
            dotJasper = grailsApplication.mainContext.getResource("${representacionInstance.formato.folder}/${representacionInstance.formato.nombre}.jasper").file.getAbsoluteFile()

            def dir = WebUtils.retrieveGrailsWebRequest().currentRequest.getSession().getServletContext().getRealPath(representacionInstance.formato.folder) + "/"

            Map<String> reportParam = new HashMap<Object>()
            
            Hashtable hints = new Hashtable()
            hints.put(EncodeHintType.MARGIN, 0)
            hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-3")
                        
            reportParam.put("representacion_grafica_id", representacionInstance.id)
            reportParam.put("hints", hints)
            JasperPrint print = JasperFillManager.fillReport(dotJasper, reportParam, dataSource.getConnection())

            JRExporter exporter = new JRPdfExporter()
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print)
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, pdfStream)
            exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8")
            exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, 0)
            exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, 0)
         
            exporter.exportReport()

	} catch (Exception e) {
            e.printStackTrace()
	}
        
        return pdfStream
        
    }
    
    def enviarNotificacionRegistroExitoso(TituloElectronico tituloElectronicoInstance, Date fechaRegistro){
        try {
            mailService.sendMail {
                multipart true
                from "Título Eletrónico Morelos<tituloelectronico@morelos.gob.mx>"
                to "${tituloElectronicoInstance?.profesionista?.correoElectronico}"
                subject "Título eletrónico registrado exitosamente"
                inline "logoMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_morelos.png")?.getFile()
                inline "logoAnfitrion", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_anfitrion.png")?.getFile()
                inline "plecaMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/pleca_morelos.png")?.getFile()
                html view: "/email/notificacionRegistroExitoso", model: [tituloElectronicoInstance: tituloElectronicoInstance, fechaRegistro: fechaRegistro]
            }
            return true
        } catch (Exception ex){
            return false
        }
    }
    
}
