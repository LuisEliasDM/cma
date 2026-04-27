package revision

import estructuraXml.TituloElectronico
import java.text.SimpleDateFormat
import seguridad.Usuario

class Observacion {
    Integer id
    Usuario usuario
    TituloElectronico tituloElectronico
    String descripcion
    Date dateCreated
    Boolean estatus
    
    static constraints = {
        usuario nullable: false
        tituloElectronico nullable: false
        descripcion nullable: false, maxSize: 500000
        dateCreated nullable: true
        estatus nullable: true
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "rev_observacion"
        
        usuario sqlType: "BIGINT"
        descripcion sqlType: "TEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        estatus defaultValue: "1"
        
    }
    
    String toString(){
        return descripcion
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}usuario: ${usuario? usuario?.toPlainText("\t\t"):""}\n"
        detalle += "${prefijo}descripcion: ${descripcion?:""}\n"

        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
