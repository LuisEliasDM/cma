package estructuraXml

import java.text.SimpleDateFormat

class Antecedente {
    static belongsTo=[tituloElectronico:TituloElectronico]
    
    String institucionProcedencia
    Integer idTipoEstudioAntecedente
    String tipoEstudioAntecedente
    String idEntidadFederativa
    String entidadFederativa
    Date fechaInicio
    Date fechaTerminacion
    String noCedula

    static constraints = {
        institucionProcedencia(blank:false, nullable:true, size:1..512)
        idTipoEstudioAntecedente(blank:false, nullable:true)
        tipoEstudioAntecedente(blank:false, nullable:true, size:1..150)
        idEntidadFederativa(blank:false, nullable:true, size:1..4)
        entidadFederativa(blank:false, nullable:true,  size:1..150)
        fechaInicio(blank:true, nullable:true)
        fechaTerminacion(blank:false, nullable:true)
        noCedula(blank:true, nullable:true, size:1..20)
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "xml_antecedente"
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}institucionProcedencia: ${institucionProcedencia?:""}\n"
        detalle += "${prefijo}idTipoEstudioAntecedente: ${idTipoEstudioAntecedente?:""}\n"
        detalle += "${prefijo}tipoEstudioAntecedente: ${tipoEstudioAntecedente?:""}\n"
        detalle += "${prefijo}idEntidadFederativa: ${idEntidadFederativa?:""}\n"
        detalle += "${prefijo}entidadFederativa: ${entidadFederativa?:""}\n"
        detalle += "${prefijo}fechaInicio: ${fechaInicio? new SimpleDateFormat("dd/MM/yyyy").format(fechaInicio):""}\n"
        detalle += "${prefijo}fechaTerminacion: ${fechaTerminacion? new SimpleDateFormat("dd/MM/yyyy").format(fechaTerminacion):""}\n"
        detalle += "${prefijo}noCedula: ${noCedula?:""}\n"
        
        return detalle
    }
}
