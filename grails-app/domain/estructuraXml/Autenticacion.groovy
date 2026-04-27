package estructuraXml

import java.text.SimpleDateFormat

class Autenticacion {
    static belongsTo=[tituloElectronico:TituloElectronico]
    //Atributo requerido con el valor prefijado a 1.0 que indica la versión del estándar bajo el que se encuentra expresado el título electrónico.
    String version="1.0"
    //Atributo requerido que define el folio único del documento (UUUID) electrónico conforme al estándar RFC 4122.
    String folioDigital
    //Atributo requerido para expresar la fecha y hora de la generación del sello del Documento Electrónico de la autoridad competente. aaaa-mm-ddThh:mm:ss.
    Date fechaAutenticacion
    //Atributo requerido que contiene los sellos digitales de los firmantes responsables concatenados del documento electrónico, que será autenticado. 
    //El sello deberá ser expresado cómo una cadena de texto en formato Base 64.    
    String selloTitulo
    //Atributo requerido para expresar el número de serie del certificado de la autoridad competente, usada para generar el sello digital del documento electrónico.
    String noCertificadoAutoridad
    
    
    //Atributo requerido para contener el sello digital al que hace referencia al documento electrónico. 
    //El sello deberá ser expresado cómo una cadena de texto en formato Base 64.
    /*
     *  La secuencia de formación será siempre en el orden que se expresa a continuación, tomando en cuenta
        las reglas generales expresadas en el punto anterior.
        1. Información del nodo Autenticacion
        a. version
        b. folioDigital
        c. fechaAutenticacion
        d. selloTitulo (1,N) <--Este es concatenar los sellos de los responsables
        e. noCertificadoAutoridad
        
        Ejemplo de la cadena original para obtener el selloAutenticacion
        
        ||1.0|ad662d33-6934-459c-a128-bdf0393e0f44|2017-12-
        17T09:30:47Z|mH5VumFizvrDBsbHiF4M9z1j61aDrofQmhzDpJdFVGFGIMkiYwdAmMu+Uq4escFg5oR4Ps
        wditV3+OI46N/PgNC7huembkCSrdXjy28MGq5JZeamFE1UqTMewq8MuhJO1+uNfhIUt0w3UcBuM6Fzbvn
        F87LF747NH9wp7cMJUszmzaKSh1Pmw9aclhLl1fPW8FURXN1xA9IRcYnsod0jaRv7Hs61xxf7Zuo+vH5em
        mAYRX839Yb+PuR2iSCYqSDAtonT4nRujeyephw618llNzJvp3XkIB+YbRYS2tqCWZch1hhpDKTXAuCsss
        uoNTHkbywDTJonbyZd+3gUzok64w==Kkie47cnf5yedfgHiF4M9z1j61aDrofQmhzDpJdFVGFGIMkiYwdAm
        Mu+Uq4escFg5oR4PswditV3+OI46N/PgNC7huembkCSrdXjy28MGq5JZeamFE1UqTMewq8MuhJO1+uNf
        hIUt0w3UcBuM6FzbvnF87LF747NH9wp7cMJUszmzaKSh1Pmw9aclhLl1fPW8FURXN1xA9IRcYnsod0jaRv
        7Hs61xxf7Zuo+vH5emmAYRX839Yb+PuR2iSCYqSDAtonT4nRujeyephw618llNzJvp3XkIB+YbRYS2tqCW
        Zch1hhpDKTXAuCsssuoNTHkbywDTJonbyZddjeo4j567n==|00001000000306602988||
     * */
    String cadenaOriginalSelloAutenticacion
    String selloAutenticacion
    
    static constraints = {
        version(display:false, size:1..4)
        folioDigital(blank:false, size:1..50)
        cadenaOriginalSelloAutenticacion(display:false, nullable:true, size:1..1000000)
        selloTitulo(display:false, nullable:true, size:1..1000000)
        noCertificadoAutoridad(blank:false, size:1..1000)
        selloAutenticacion(display:false, nullable:true, size:1..1000000)
        
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "xml_autenticacion"
        
        cadenaOriginalSelloAutenticacion sqlType: "LONGTEXT"
        selloTitulo sqlType: "LONGTEXT"
        selloAutenticacion sqlType: "LONGTEXT"
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}version: ${version?:""}\n"
        detalle += "${prefijo}folioDigital: ${folioDigital?:""}\n"
        detalle += "${prefijo}fechaAutenticacion: ${fechaAutenticacion? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaAutenticacion):""}\n"
        detalle += "${prefijo}selloTitulo: ${selloTitulo?:""}\n"
        detalle += "${prefijo}noCertificadoAutoridad: ${noCertificadoAutoridad?:""}\n"
        detalle += "${prefijo}cadenaOriginalSelloAutenticacion: ${cadenaOriginalSelloAutenticacion?:""}\n"
        detalle += "${prefijo}selloAutenticacion: ${selloAutenticacion?:""}\n"
        
        return detalle
    }
}
