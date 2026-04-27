package estructuraXml

import java.text.SimpleDateFormat

class Carrera {
    static belongsTo=[tituloElectronico:TituloElectronico]
    //Clave y nombre de la carrera de acuerdo al catalogo de excel
    String cveCarrera
    String nombreCarrera
    //Fechas de inicio y terminación de la carrera
    Date fechaInicio
    Date fechaTerminacion
    //Estos atributos deben ser llenados a partir del catalogo de excel
    Integer idAutorizacionReconocimiento
    String autorizacionReconocimiento
    //Atributo condicional indica el numero de Reconocimiento de validez oficial de esudios otorgado por la autoridad competente
    String numeroRvoe

    static constraints = {
        cveCarrera(blank:false, nullable:true, size:6..20)
        nombreCarrera(blank:false, nullable:true, size:1..512)
        fechaInicio(blank:false, nullable:true)
        fechaTerminacion(blank:false, nullable:true)
        idAutorizacionReconocimiento(blank:false, nullable:true)
        autorizacionReconocimiento(blank:false, nullable:true)
        numeroRvoe(blank:true, nullable:true, size:3..100)
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "xml_carrera"
        
        cveInstitucion index: 'carrera_cve_idx'
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}cveCarrera: ${cveCarrera?:""}\n"
        detalle += "${prefijo}nombreCarrera: ${nombreCarrera?:""}\n"
        detalle += "${prefijo}fechaInicio: ${fechaInicio? new SimpleDateFormat("dd/MM/yyyy").format(fechaInicio):""}\n"
        detalle += "${prefijo}fechaTerminacion: ${fechaTerminacion? new SimpleDateFormat("dd/MM/yyyy").format(fechaTerminacion):""}\n"
        detalle += "${prefijo}idAutorizacionReconocimiento: ${idAutorizacionReconocimiento?:""}\n"
        detalle += "${prefijo}autorizacionReconocimiento: ${autorizacionReconocimiento?:""}\n"
        detalle += "${prefijo}numeroRvoe: ${numeroRvoe?:""}\n"
        
        return detalle
    }
}
