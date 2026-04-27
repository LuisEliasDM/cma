package estructuraXml

class Institucion {
    static belongsTo=[tituloElectronico:TituloElectronico]
    //Clave y nombre de la institucion de acuerdo al catalogo excel
    String cveInstitucion
    String nombreInstitucion

    static constraints = {
        cveInstitucion(blank:false, nullable:true, size:6..20)
        nombreInstitucion(blank:false, nullable:true, size:1..512)
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "xml_institucion"
        
        cveInstitucion index: 'institucion_cve_idx'
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}cveInstitucion: ${cveInstitucion?:""}\n"
        detalle += "${prefijo}nombreInstitucion: ${nombreInstitucion?:""}\n"
        
        return detalle
    }
}
