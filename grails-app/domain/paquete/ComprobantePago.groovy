package paquete

import java.text.SimpleDateFormat

class ComprobantePago {
    Integer id
    String poliza
    Double monto
    Date dateCreated
    
    static belongsTo = [
        paquete: Paquete
    ]
    
    static constraints = {
        poliza nullable: false, maxSize: 32
        monto nullable: true
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "paq_comprobante_pago"
        
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}poliza: ${poliza?:""}\n"
        detalle += "${prefijo}monto: ${monto?:""}\n"

        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
