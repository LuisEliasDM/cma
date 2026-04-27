package catalogo

class TipoEstudioAntecedente {
    String descripcion
    TipoEducativoAntecedente tipo 

    static constraints = {
        descripcion(nullable: false, size:1..256)
        tipo(nullable:false)
    }
    
    static mapping = {
        version false
        
        table "cat_tipo_estudio_antecedente"
    }
}
