package estructuraXml

class FirmaResponsables {
    static belongsTo=[tituloElectronico:TituloElectronico]
    //Este nodo contiene 1 o varias firmas de responsables
    static hasMany = [firmaResponsable:FirmaResponsable]
    
    static constraints = {
        firmaResponsable(nullable:true)
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "xml_firma_responsables"
        
        firmaResponsable sort: "idCargo", order: "ASC"
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        firmaResponsable?.each {
            detalle += "${prefijo}firmaResponsable: ${it?.toPlainText("\t\t\t")?:""}\n"
        }
        
        return detalle
    }
}
