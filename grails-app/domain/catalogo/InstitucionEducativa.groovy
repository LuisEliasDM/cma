package catalogo

import java.text.SimpleDateFormat
import seguridad.UsuarioInstitucion

class InstitucionEducativa {
    
    static hasMany = [
        carrerasInstitucion: CarreraInstitucion,
        usuarios: UsuarioInstitucion
    ]
  
    Integer id
    String clave
    String descripcion
    Date dateCreated
    
    static constraints = {
        clave(nullable: true, size:1..20, unique: true)
        descripcion(nullable:false, size:1..256)
        dateCreated nullable: true
    }
    
    static mapping = {
        version false
        
        table "cat_institucion_educativa"
        
        carrerasInstitucion sort: "descripcion"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        
        sort descripcion: "ASC"
        
    }
    
    String toString(){
        return "${clave} - ${descripcion}"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}clave: ${clave?:""}\n"
        detalle += "${prefijo}descripcion: ${descripcion?:""}\n"
        
        carrerasInstitucion?.each {
            detalle += "${prefijo}carrera: ${it?.toPlainText("\t\t")?:""}\n"
        }
        
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
