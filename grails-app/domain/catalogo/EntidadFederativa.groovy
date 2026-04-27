package catalogo

class EntidadFederativa {
    String id
    String descripcion
    String abreviatura
    
    static constraints = {
        id(nullable: false, size:1..2)
        descripcion(nullable:false, size:1..256)
        abreviatura(nullable:false, size:1..8)
    }
    
    static mapping = {
        version false
        
        table "cat_entidad_federativa"
        
        id generator:"assigned"
    }
}
