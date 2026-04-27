package catalogo

import java.text.SimpleDateFormat

class CarreraInstitucion {
    
    static belongsTo=[institucionEducativa:InstitucionEducativa]
    
    Integer id
    String clave
    String descripcion
    AutorizacionReconocimiento autorizacionReconocimiento
    String rvoe
    Date dateCreated
    
    static constraints = {
        clave(nullable: true, size:1..20)
        descripcion(nullable:false, size:1..256)
        autorizacionReconocimiento nullable: false
        rvoe nullable: false, maxSize: 16
        dateCreated nullable: true
    }
    
    static mapping = {
        version false
        
        table "cat_carrera_institucion"
        
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
    }
    
    String toString(){
        return "${clave} - ${descripcion} (RVOE: ${rvoe})"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}clave: ${clave?:""}\n"
        detalle += "${prefijo}descripcion: ${descripcion?:""}\n"
        detalle += "${prefijo}autorizacionReconocimiento: ${autorizacionReconocimiento?:""}\n"
        detalle += "${prefijo}rvoe: ${rvoe?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
