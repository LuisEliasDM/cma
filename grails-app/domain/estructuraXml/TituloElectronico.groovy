package estructuraXml

import java.text.SimpleDateFormat
import swdgp.Registro
import paquete.Paquete
import revision.Observacion
import revision.Correccion

class TituloElectronico {  
    Integer id
    //Version del estandar bajo el que se encuentra expresado el Titulo
    String version="1.0"
    
    //Folio de control interno
    String folioControl
    
    //Nodos del titulo
    FirmaResponsables firmaResponsables
    Institucion institucion
    Carrera carrera
    Profesionista profesionista
    Expedicion expedicion
    Antecedente antecedente
    Autenticacion autenticacion
        
    String libro
    String foja
    Date fechaRegistroLibro
    
    String aclaracion
    
    Date dateCreated
    
    Boolean activo
    
    def tituloElectronicoService
    
    static hasMany = [
        registros: Registro, 
        observaciones: Observacion,
        correcciones: Correccion,
        representaciones: RepresentacionGrafica
    ]
    
    static belongsTo = [
        paquete: Paquete
    ]
    
    static transients = ["tituloElectronicoService"]

    static constraints = {
        folioControl(display:false, unique:true, nullable:true, size:6..40)
        version(display:false, size:3..3, nullable:true)         
        firmaResponsables(nullable:true,display:false)
        institucion(nullable:true,display:false)
        carrera(nullable:true,display:false)
        profesionista(nullable:true,display:false)
        expedicion(nullable:true,display:false)
        antecedente(nullable:true,display:false)
        autenticacion(nullable:true,display:false)
        dateCreated(display:false, nullable:true)
        paquete nullable: true
        libro nullable: true, maxSize: 256
        foja nullable: true, maxSize: 256
        fechaRegistroLibro nullable: true
        aclaracion nullable: true, maxSize: 5000000
        activo nullable: false
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "xml_titulo_electronico"
        
        aclaracion sqlType: "TEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        activo defaultValue: 1
        
        registros sort: "id", order: "ASC"
        observaciones sort: "id", order: "ASC"
        correcciones sort: "id", order: "ASC"
    }
    
    def getNombreArchivo(def ext){
        return "${folioControl}_${institucion?.cveInstitucion}_${profesionista?.curp}.${ext}"
    }
    
    def getEstatus () {
        return id? tituloElectronicoService?.estatusTitulo(this) : null
    }
    
    def getValido() {
        return id? tituloElectronicoService?.validarDatos(this) : null
    }
    
    def getPermiso(String accion) {
        return tituloElectronicoService?.permisoTitulo(this, accion)
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}version: ${version?:""}\n"
        detalle += "${prefijo}paquete: ${paquete?:""}\n"
        detalle += "${prefijo}folioControl: ${folioControl?:""}\n"
        detalle += "${prefijo}estatus: ${getEstatus()? "${getEstatus()?.id} - ${getEstatus()?.descripcion}":""}\n"
        detalle += "${prefijo}institucion: ${institucion?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}carrera: ${carrera?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}profesionista: ${profesionista?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}expedicion: ${expedicion?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}antecedente: ${antecedente?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}firmaResponsables: ${firmaResponsables?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}autenticacion: ${autenticacion?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}libro: ${libro?:""}\n"
        detalle += "${prefijo}foja: ${foja?:""}\n"
        detalle += "${prefijo}fechaRegistroLibro: ${fechaRegistroLibro? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaRegistroLibro) :""}\n"
        detalle += "${prefijo}aclaracion: ${aclaracion?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        observaciones?.each {
            detalle += "${prefijo}observacion: ${it?.toPlainText("\t\t")?:""}\n"
        }
        
        correcciones?.each {
            detalle += "${prefijo}correccion: ${it?.toPlainText("\t\t")?:""}\n"
        }
        
        representaciones?.each {
            detalle += "${prefijo}representacion: ${it?.toPlainText("\t\t")?:""}\n"
        }
        
        return detalle
    }
}
