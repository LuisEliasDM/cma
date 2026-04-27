package estructuraXml

import java.text.SimpleDateFormat

class Expedicion {
    static belongsTo=[tituloElectronico:TituloElectronico]
    //Fecha de expedicion del documento
    Date fechaExpedicion
    Integer idModalidadTitulacion
    String modalidadTitulacion
    Date fechaExamenProfesional
    Date fechaExencionExamenProfesional
    Integer cumplioServicioSocial
    Integer idFundamentoLegalServicioSocial
    String fundamentoLegalServicioSocial
    String idEntidadFederativa
    String entidadFederativa
    
    

    static constraints = {
        fechaExpedicion(blank:false, nullable:true)
        idModalidadTitulacion(blank:false, nullable:true)
        modalidadTitulacion(blank:false, nullable:true, size:1..200)
        fechaExamenProfesional(blank:true, nullable:true)
        fechaExencionExamenProfesional(blank:true, nullable:true)
        cumplioServicioSocial(blank:false, nullable:true)
        idFundamentoLegalServicioSocial(blank:false, nullable:true)
        fundamentoLegalServicioSocial(blank:false, nullable:true, size:1..200)
        idEntidadFederativa(blank:false, nullable:true, size:1..4)
        entidadFederativa(blank:false, nullable:true,  size:1..150)
        
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "xml_expedicion"
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}fechaExpedicion: ${fechaExpedicion? new SimpleDateFormat("dd/MM/yyyy").format(fechaExpedicion):""}\n"
        detalle += "${prefijo}idModalidadTitulacion: ${idModalidadTitulacion?:""}\n"
        detalle += "${prefijo}modalidadTitulacion: ${modalidadTitulacion?:""}\n"
        detalle += "${prefijo}fechaExamenProfesional: ${fechaExamenProfesional? new SimpleDateFormat("dd/MM/yyyy").format(fechaExamenProfesional):""}\n"
        detalle += "${prefijo}fechaExencionExamenProfesional: ${fechaExencionExamenProfesional? new SimpleDateFormat("dd/MM/yyyy").format(fechaExencionExamenProfesional):""}\n"
        detalle += "${prefijo}cumplioServicioSocial: ${cumplioServicioSocial?:""}\n"
        detalle += "${prefijo}idFundamentoLegalServicioSocial: ${idFundamentoLegalServicioSocial?:""}\n"
        detalle += "${prefijo}fundamentoLegalServicioSocial: ${fundamentoLegalServicioSocial?:""}\n"
        detalle += "${prefijo}idEntidadFederativa: ${idEntidadFederativa?:""}\n"
        detalle += "${prefijo}entidadFederativa: ${entidadFederativa?:""}\n"
        
        return detalle
    }
}
