package catalogo

class ModalidadTitulacion {
    String descripcion
    TipoModalidadTitulacion tipo
    
    static constraints = {
        descripcion(nullable:false, size:1..256)
        tipo(nullable: false)
    }
    
    static mapping = {
        version false
        
        table "cat_modalidad_titulacion"
    }
}
