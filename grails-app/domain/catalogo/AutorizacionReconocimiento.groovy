package catalogo

class AutorizacionReconocimiento {
    Integer id
    String descripcion
    Boolean requiereAutenticacion
    
    static constraints = {
        descripcion(nullable: false, size: 1..256)
        requiereAutenticacion(nullable: false)
    }
    
    static mapping = {
        version false
        
        table "cat_autorizacion_reconocimiento"
    }
    
    String toString() {
        "${id} - ${descripcion}"
    }
}
