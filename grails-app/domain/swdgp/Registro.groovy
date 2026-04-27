package swdgp

import estructuraXml.TituloElectronico
import java.text.SimpleDateFormat

class Registro {
    TituloElectronico tituloElectronico
    ArchivoRegistro archivoRegistro
    Boolean estatusEnvio
    Integer codigoEstatusEnvio
    String mensajeEstatusEnvio
    Integer numeroLote
    String mensajeRegistro
    Date dateCreated
        
    static hasMany = [
        consultas: Consulta, 
        archivosResultado: ArchivoResultado, 
        cancelaciones: Cancelacion
    ]

    static constraints = {
        tituloElectronico nullable: false
        archivoRegistro nullable: false
        estatusEnvio nullable: true
        codigoEstatusEnvio nullable: true
        mensajeEstatusEnvio nullable: true, size: 0..5000000
        numeroLote nullable: true
        mensajeRegistro nullable: true, size: 0..5000000
        dateCreated nullable: true
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "sw_registro"
        
        mensajeEstatusEnvio sqlType: "LONGTEXT"
        mensajeRegistro sqlType: "LONGTEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        consultas sort: "id", order: "ASC"
        archivosResultado sort: "id", order: "ASC"
        cancelaciones sort: "id", order: "ASC"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}archivoRegistro: ${archivoRegistro?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}estatusEnvio: ${estatusEnvio?:""}\n"
        detalle += "${prefijo}codigoEstatusEnvio: ${codigoEstatusEnvio?:""}\n"
        detalle += "${prefijo}mensajeEstatusEnvio: ${mensajeEstatusEnvio?:""}\n"
        detalle += "${prefijo}numeroLote: ${numeroLote?:""}\n"
        detalle += "${prefijo}mensajeRegistro: ${mensajeRegistro?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
