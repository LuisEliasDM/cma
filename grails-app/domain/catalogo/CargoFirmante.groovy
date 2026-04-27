package catalogo

class CargoFirmante {
    Integer id
    String descripcion

    static constraints = {
        descripcion(nullable:false, size:1..256)
    }
    
    static mapping = {
        version false
        
        table "cat_cargo_firmante"
    }
    
    String toString(){
        "${id} - ${descripcion}"
    }
}
