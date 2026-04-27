package catalogo

class MotivoCancelacion {
    String id
    String descripcion

    static constraints = {
        id(nullable: false, size:1..2)
        descripcion(nullable:false, size:1..256)
    }
    
    static mapping = {
        version false
        
        table "cat_motivo_cancelacion"
        
        id generator:"assigned"
    }
}
