package swdgp

import java.text.SimpleDateFormat

class ArchivoRegistro {
    static belongsTo = [registro: Registro]
    String nombre
    String base64
    Date dateCreated

    static constraints = {
        nombre nullable: false, size: 3..512
        base64 nullable: false, size: 3..5000000
        dateCreated nullable: true
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "sw_archivo_registro"
        
        base64 sqlType: "LONGTEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}nombre: ${nombre?:""}\n"
        detalle += "${prefijo}base64: ${base64?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
    
}
