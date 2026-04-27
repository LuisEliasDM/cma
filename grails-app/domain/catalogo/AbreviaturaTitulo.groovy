package catalogo

class AbreviaturaTitulo {
    String descripcion

    static constraints = {
        descripcion(nullable:false, size:1..256)
    }
    
    static mapping = {
        version false
        
        table "cat_abreviatura_titulo"
    }
    
    String toString(){
        descripcion
    }
}
