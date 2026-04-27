package swdgp

import java.text.SimpleDateFormat

class ArchivoResultado {
    Registro registro
    Boolean estatusDescarga
    Integer codigoEstatusDescarga
    String mensajeEstatusDescarga
    String nombre
    Date dateCreated
    
    Integer numeroLote
    String mensajeDescarga
    String base64
    
    static hasMany = [
        detalles: DetalleArchivoResultado
    ]

    static constraints = {
        registro nullable: false
        estatusDescarga nullable: true
        codigoEstatusDescarga nullable:true
        mensajeEstatusDescarga nullable: true, size: 0..5000000
        nombre nullable: false, size: 3..512
        dateCreated nullable: true
        
        numeroLote nullable: true
        mensajeDescarga nullable: true, size: 0..5000000
        base64 nullable: true, size: 0..5000000
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "sw_archivo_resultado"
        
        mensajeEstatusDescarga sqlType: "LONGTEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        mensajeDescarga sqlType: "LONGTEXT"
        base64 sqlType: "LONGTEXT"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}estatusDescarga: ${estatusDescarga?:""}\n"
        detalle += "${prefijo}codigoEstatusDescarga: ${codigoEstatusDescarga?:""}\n"
        detalle += "${prefijo}mensajeEstatusDescarga: ${mensajeEstatusDescarga?:""}\n"
        detalle += "${prefijo}nombre: ${nombre?:""}\n"
        detalle += "${prefijo}numeroLote: ${numeroLote?:""}\n"
        detalle += "${prefijo}mensajeDescarga: ${mensajeDescarga?:""}\n"
        detalle += "${prefijo}base64: ${base64?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        detalles?.each {
            detalle += "${prefijo}detalle: ${it?.toPlainText("\t\t\t")?:""}\n"
        }
        
        return detalle
    }
}
