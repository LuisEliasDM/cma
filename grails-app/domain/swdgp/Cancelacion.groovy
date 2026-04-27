package swdgp

import java.text.SimpleDateFormat

class Cancelacion {
    Registro registro
    String folioControl
    String motivoCancelacion
    Boolean estatusCancelacion
    Integer codigoEstatusCancelacion
    String mensajeEstatusCancelacion
    Date dateCreated
    
    Integer codigoEjecucion
    String mensajeEjecucion
    
    Boolean cancelado

    static constraints = {
        registro nullable: false
        estatusCancelacion nullable: true
        codigoEstatusCancelacion nullable: true
        mensajeEstatusCancelacion nullable: true, size: 0..5000000
        folioControl nullable:false, size:6..40
        motivoCancelacion nullable: false, size: 1..5
        dateCreated nullable: true
        
        codigoEjecucion nullable: true
        mensajeEjecucion nullable: true, size: 0..5000000
        
        cancelado nullable: true
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "sw_cancelacion"
        
        mensajeEstatusCancelacion sqlType: "LONGTEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        mensajeEjecucion sqlType: "LONGTEXT"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}folioControl: ${folioControl?:""}\n"
        detalle += "${prefijo}motivoCancelacion: ${motivoCancelacion?:""}\n"
        detalle += "${prefijo}estatusCancelacion: ${estatusCancelacion?:""}\n"
        detalle += "${prefijo}codigoEstatusCancelacion: ${codigoEstatusCancelacion?:""}\n"
        detalle += "${prefijo}mensajeEstatusCancelacion: ${mensajeEstatusCancelacion?:""}\n"
        detalle += "${prefijo}codigoEjecucion: ${codigoEjecucion?:""}\n"
        detalle += "${prefijo}mensajeEjecucion: ${mensajeEjecucion?:""}\n"
        detalle += "${prefijo}cancelado: ${cancelado?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
