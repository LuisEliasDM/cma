package estructuraXml

import catalogo.AbreviaturaTitulo
import catalogo.CargoFirmante
import catalogo.InstitucionEducativa
import java.text.SimpleDateFormat
import seguridad.Usuario

class FirmaElectronica {
    Integer id
    InstitucionEducativa institucionEducativa
    String curp
    String nombre
    String primerApellido
    String segundoApellido
    String sexo
    Date fechaNacimiento
    String estadoNacimiento
    String nombreCompleto
    
    byte [] archivoCer
    byte [] archivoKey
    String contenidoCer
    String nombreCer
    String curpCer
    String rfcCer
    String correoElectronicoCer
    Date validoDesdeCer
    Date validoHastaCer
    String numeroSerieCer
    CargoFirmante cargoFirmante
    AbreviaturaTitulo abreviaturaTitulo
    Boolean activo
    Date dateCreated

    static constraints = {
        institucionEducativa nullable: true
        curp nullable: false, maxSize: 20
        nombre nullable: false, maxSize: 256
        primerApellido nullable: false, maxSize: 256
        segundoApellido nullable: true, maxSize: 256
        sexo nullable: false, maxSize: 2
        fechaNacimiento nullable: false
        estadoNacimiento nullable: false, maxSize: 4
        nombreCompleto nullable: true
        
        archivoCer nullable: false
        archivoKey nullable: true
        contenidoCer nullable: false, maxSize: 5000
        nombreCer nullable: false, maxSize: 256
        curpCer nullable: false, maxSize: 64
        rfcCer nullable: false, maxSize: 64
        correoElectronicoCer nullable: false, maxSize: 256
        validoDesdeCer nullable: false
        validoHastaCer nullable: false
        numeroSerieCer nullable: false
        cargoFirmante nullable: false
        abreviaturaTitulo nullable: false
        activo nullable: false
        dateCreated display:false, nullable:true
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "fiel_firma_electronica"
        
        fechaNacimiento sqlType: "DATE"
        nombreCompleto formula: "CONCAT(nombre,' ',primer_apellido,' ', segundo_apellido)"
        archivoCer sqlType: "BLOB"
        archivoKey sqlType: "BLOB"
        contenidoCer sqlType: "LONGTEXT"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}institucionEducativa: ${version?:""}\n"
        detalle += "${prefijo}curp: ${curp?:""}\n"
        detalle += "${prefijo}nombre: ${nombre?:""}\n"
        detalle += "${prefijo}primerApellido: ${primerApellido?:""}\n"
        detalle += "${prefijo}segundoApellido: ${segundoApellido?:""}\n"
        detalle += "${prefijo}nombreCompleto: ${nombreCompleto?:""}\n"
        detalle += "${prefijo}sexo: ${sexo?:""}\n"
        detalle += "${prefijo}fechaNacimiento: ${fechaNacimiento? new SimpleDateFormat("dd/MM/yyyy").format(fechaNacimiento) :""}\n"
        detalle += "${prefijo}estadoNacimiento: ${estadoNacimiento?:""}\n"
        detalle += "${prefijo}archivoCer: ${archivoCer?:""}\n"
        detalle += "${prefijo}archivoKey: ${archivoKey?:""}\n"
        detalle += "${prefijo}contenidoCer: ${contenidoCer?:""}\n"
        detalle += "${prefijo}nombreCer: ${nombreCer?:""}\n"
        detalle += "${prefijo}curpCer: ${curpCer?:""}\n"
        detalle += "${prefijo}rfcCer: ${rfcCer?:""}\n"
        detalle += "${prefijo}correoElectronicoCer: ${estadoNacimiento?:""}\n"
        detalle += "${prefijo}validoDesdeCer: ${validoDesdeCer? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(validoDesdeCer) :""}\n"
        detalle += "${prefijo}validoHastaCer: ${validoHastaCer? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(validoHastaCer) :""}\n"
        detalle += "${prefijo}numeroSerieCer: ${numeroSerieCer?:""}\n"
        detalle += "${prefijo}cargoFirmante: ${cargoFirmante?:""}\n"
        detalle += "${prefijo}abreviaturaTitulo: ${abreviaturaTitulo?:""}\n"
        detalle += "${prefijo}activo: ${activo? "Si":"No"}\n"
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
