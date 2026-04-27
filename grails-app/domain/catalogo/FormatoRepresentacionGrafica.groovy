package catalogo

import estructuraXml.RepresentacionGrafica
import java.text.SimpleDateFormat

class FormatoRepresentacionGrafica {
    Integer id
    String nombre
    String folder
    Date dateCreated
    Boolean activo
    
    static hasMany = [
        representaciones: RepresentacionGrafica
    ]

    static constraints = {
        nombre nullable: false, maxSize: 256
        folder nullable: false, maxSize: 5000
        dateCreated nullable: true
        activo nullable: false
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "cat_formato_representacion_grafica"
        
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        activo defaultValue: 1
    }
    
    String toString(){
        return nombre
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}nombre: ${nombre?:""}\n"
        detalle += "${prefijo}folder: ${folder?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        detalle += "${prefijo}activo: ${activo? "Si":"No"}\n"
        
        return detalle
    }
}
