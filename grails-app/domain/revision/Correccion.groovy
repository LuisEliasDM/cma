package revision

import estructuraXml.TituloElectronico
import java.text.SimpleDateFormat

class Correccion {
    Integer id
    TituloElectronico tituloElectronico
    Integer numero
    Integer dias
    Date dateCreated
    Boolean estatus
    
    static constraints = {
        tituloElectronico nullable: false
        numero nullable: false
        dias nullable: false
        dateCreated nullable: true
        estatus nullable: false
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "rev_correccion"
        
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        estatus defaultValue: 1
        
    }
    
    String toString(){
        return numero
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}numero: ${numero?:""}\n"
        detalle += "${prefijo}dias: ${dias?:""}\n"
        detalle += "${prefijo}estatus: ${estatus?:""}\n"

        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
