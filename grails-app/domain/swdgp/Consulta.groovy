package swdgp

import java.text.SimpleDateFormat

class Consulta {
    Registro registro
    Boolean estatusConsulta
    Integer codigoEstatusConsulta
    String mensajeEstatusConsulta
    
    Integer numeroLote
    Integer estatusLote
    String mensajeConsulta
    Date dateCreated
    
    static constraints = {
        registro nullable: false
        estatusConsulta nullable: true
        codigoEstatusConsulta nullable: true
        mensajeEstatusConsulta nullable: true, size: 0..5000000
        numeroLote nullable: true
        estatusLote nullable: true
        mensajeConsulta nullable: true, size: 0..5000000
        dateCreated nullable: true
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "sw_consulta"
        
        mensajeEstatusConsulta sqlType: "LONGTEXT"
        mensajeConsulta sqlType: "LONGTEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}estatusConsulta: ${estatusConsulta?:""}\n"
        detalle += "${prefijo}codigoEstatusConsulta: ${codigoEstatusConsulta?:""}\n"
        detalle += "${prefijo}mensajeEstatusConsulta: ${mensajeEstatusConsulta?:""}\n"
        detalle += "${prefijo}numeroLote: ${numeroLote?:""}\n"
        detalle += "${prefijo}estatusLote: ${estatusLote?:""}\n"
        detalle += "${prefijo}mensajeConsulta: ${mensajeConsulta?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
