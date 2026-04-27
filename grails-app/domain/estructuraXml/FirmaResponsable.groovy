package estructuraXml

import java.text.SimpleDateFormat

class FirmaResponsable {
    static belongsTo=[firmaResponsables:FirmaResponsables]
    //Datos personales
    String nombre
    String primerApellido
    String segundoApellido
    String curp
    //Identificador y descripcion del cargo que desempeña
    Integer idCargo
    String cargo
    //Atributo opcional que representa el titulo o grado academico
    String abrTitulo       
    
    /*
        La secuencia de formación de la cadena original será siempre en el orden que se expresa a continuación.

        Uso Atributo
        1. Información del nodo TituloElectronico
            a. version
            b. folioControl
        2. Información del nodo FirmaResponsable
            a. curp
            b. idCargo
            c. cargo
            d. abrTitulo
        3. Información del nodo Institucion
            a. cveInstitucion
            b. nombreInstitucion
        4. Información del nodo Carrera
            a. cveCarrera
            b. nombreCarrera
            Opcional c. fechaInicio
            d. fechaTerminacion
            e. idAutorizacionReconocimiento
            f. autorizacionReconocimiento
            Opcional g. numeroRvoe
        5. Información del nodo Profesionista
            a. curp
            b. nombre
            c. primerApellido
            Opcional d. segundoApellido
            e. correoElectronico
        6. Información del nodo Expedicion
            a. fechaExpedicion
            b. idModalidadTitulacion
            c. modalidadTitulacion
            Opcional d. fechaExamenProfesional
            Opcional e. fechaExencionExamenProfesional
            f. cumplioServicioSocial
            g. idFundamentoLegalServicioSocial
            h. fundamentoLegalServicioSocial
            i. idEntidadFederativa
            j. entidadFederativa
        7. Información del nodo Antecedente
            a. institucionProcedencia
            b. idTipoEstudioAntecedente
            c. tipoEstudioAntecedente
            d. idEntidadFederativa
            e. entidadFederativa
            Opcional f. fechaInicio
            g. fechaTerminacion
            Opcional h. noCedula
            
    
        Ejemplo de la cadena original para obtener el sello:
        ||1.0|2345678|EICA750212HDFRNL01|3|RECTOR|LIC.|010033|UNIVERSIDAD TECNOLÓGICA DE
        AGUASCALIENTES|103339|INGENIERÍA EN NANOTECNOLOGÍA|2004-08-16|2009-06-12|5|ACTA DE
        SESIÓN||AICA770112HDFRNL01|ANTONIO|ALPIZAR|CASTRO|antonio.alpizar@gmail.com|2011-08-
        10|1|POR TESIS|2010-08-16||1|2|ART. 55 LRART. 5 CONST|09|CIUDAD DE MÉXICO|C.E.T.I.S. NO.
        80|4|BACHILLERATO|09|CIUDAD DE MÉXICO|2000-06-12|2003-08-12|||            
    */
   
    String cadenaOriginal
    //Strings para almacenar firma y sello en base 64
    /*   
     * Ejemplo del sello:
     *  mH5VumFizvrDBsbHiF4M9z1j61aDrofQmhzDpJdFVGFGIMkiYwdAmMu+Uq4escFg5oR4PswditV3+OI46N
        /PgNC7huembkCSrdXjy28MGq5JZeamFE1UqTMewq8MuhJO1+uNfhIUt0w3UcBuM6FzbvnF87LF747NH9
        wp7cMJUszmzaKSh1Pmw9aclhLl1fPW8FURXN1xA9IRcYnsod0jaRv7Hs61xxf7Zuo+vH5emmAYRX839Yb
        +PuR2iSCYqSDAtonT4nRujeyephw618llNzJvp3XkIB+YbRYS2tqCWZch1hhpDKTXAuCsssuoNTHkbywDT
        JonbyZd+3gUzok64w==
     * */
    String sello
    String certificadoResponsable
    String noCertificadoResponsable
    Date dateCreated

    static constraints = {
        nombre(blank:false, nullable:true, size:1..200)
        primerApellido(blank:false, nullable:false, size:1..200)
        segundoApellido(blank:true, nullable:true, size:1..200)
        curp(blank:false, nullable:false, size:0..18)
        idCargo(blank:false, nullable:false)
        cargo(blank:false, nullable:false)
        abrTitulo(blank:true, nullable:false, size:1..30)
        cadenaOriginal(display:false, nullable:false, size:1..1000000)
        sello(blank:false, nullable:false, size:1..1000000)
        certificadoResponsable(blank:false, nullable:false, size:1..1000000)
        noCertificadoResponsable(blank:false, nullable:false, size:1..1000)
        dateCreated(display:false, nullable:true)
    }
    
    static mapping = {
        version false
        
        cache false
                
        table "xml_firma_responsable"
        
        cadenaOriginal sqlType: "LONGTEXT"
        sello sqlType: "LONGTEXT"
        certificadoResponsable sqlType: "LONGTEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
    }
    
    String getNombreCompleto() {
        return abrTitulo + " " + nombre+" "+primerApellido+" "+segundoApellido
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}nombre: ${nombre?:""}\n"
        detalle += "${prefijo}primerApellido: ${primerApellido?:""}\n"
        detalle += "${prefijo}segundoApellido: ${segundoApellido?:""}\n"
        detalle += "${prefijo}curp: ${curp?:""}\n"
        detalle += "${prefijo}idCargo: ${idCargo?:""}\n"
        detalle += "${prefijo}cargo: ${cargo?:""}\n"
        detalle += "${prefijo}abrTitulo: ${abrTitulo?:""}\n"
        detalle += "${prefijo}cadenaOriginal: ${cadenaOriginal?:""}\n"
        detalle += "${prefijo}sello: ${sello?:""}\n"
        detalle += "${prefijo}certificadoResponsable: ${certificadoResponsable?:""}\n"
        detalle += "${prefijo}noCertificadoResponsable: ${noCertificadoResponsable?:""}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated):""}\n"
        
        return detalle
    }
}
