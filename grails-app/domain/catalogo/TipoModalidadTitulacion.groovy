package catalogo

class TipoModalidadTitulacion {
    String descripcion
    
    static constraints = {
        descripcion(nullable: false, size: 1..256)
    }
    
    static mapping = {
        version false
        
        table "cat_tipo_modalidad_titulacion"
    }
}
