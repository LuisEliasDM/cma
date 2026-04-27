package paquete

class EstatusPaquete {
    Integer id
    String descripcion
    String nombreBandeja
    
    static constraints = {
        descripcion nullable:false, maxSize: 256
        nombreBandeja nullable: false, maxSize: 256
    }
    
    static hasMany = [
        paquetes: Paquete
    ]
    
    static mapping = {
        version false
        
        cache false
        
        table "cat_estatus_paquete"
        
        nombreBandeja defaultValue: ""
    }
    
    String toString(){
        "${id} - ${descripcion}"
    }
}
