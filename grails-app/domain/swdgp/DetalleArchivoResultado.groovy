package swdgp

class DetalleArchivoResultado {
    ArchivoResultado archivoResultado
    String nombreArchivo
    Integer estatus
    String descripcion
    String folioControl

    static constraints = {
        archivoResultado nullable: false
        nombreArchivo nullable: true, size: 0..512
        estatus nullable: true
        descripcion nullable: true, size: 0..5000000
        folioControl nullable:true, size: 0..512
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "sw_detalle_archivo_resultado"
        
        descripcion sqlType: "LONGTEXT"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}nombreArchivo: ${nombreArchivo?:""}\n"
        detalle += "${prefijo}estatus: ${estatus?:""}\n"
        detalle += "${prefijo}descripcion: ${descripcion?:""}\n"
        detalle += "${prefijo}folioControl: ${folioControl?:""}\n"
        
        return detalle
    }
}
