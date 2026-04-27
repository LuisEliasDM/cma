package catalogo

class TipoEducativoAntecedente {
    String descripcion

    static constraints = {
        descripcion(nullable:false, size:1..256)
    }
    
    static mapping = {
        version false
        
        table "cat_tipo_educativo_antecedente"
    }
}
