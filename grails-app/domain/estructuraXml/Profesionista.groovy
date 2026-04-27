package estructuraXml

import java.text.SimpleDateFormat

class Profesionista {
    static belongsTo=[tituloElectronico:TituloElectronico]
    //Datos personales
    String curp
    String nombre
    String primerApellido
    String segundoApellido
    String sexo
    Date fechaNacimiento
    String estadoNacimiento
    String correoElectronico
    String nombreCompleto
    String apellidos

    static constraints = {
        nombre(blank:false, nullable:true, size:1..200)
        primerApellido(blank:false, nullable:true, size:1..200)
        segundoApellido(blank:true, nullable:true, size:1..200)
        sexo nullable: true, maxSize: 2
        fechaNacimiento nullable: true
        estadoNacimiento nullable: true, maxSize: 4
        nombreCompleto nullable: true
        curp(blank:false, nullable:true, size:18..18)
        correoElectronico(blank:false, nullable:true)
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "xml_profesionista"
        
        nombreCompleto formula: "CONCAT(nombre,' ',primer_apellido,' ', IFNULL(segundo_apellido, ''))"
        apellidos formula: "CONCAT(primer_apellido,' ', segundo_apellido)"
        fechaNacimiento sqlType: "DATE"
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}curp: ${curp?:""}\n"
        detalle += "${prefijo}nombre: ${nombre?:""}\n"
        detalle += "${prefijo}primerApellido: ${primerApellido?:""}\n"
        detalle += "${prefijo}segundoApellido: ${segundoApellido?:""}\n"
        detalle += "${prefijo}sexo: ${sexo?:""}\n"
        detalle += "${prefijo}fechaNacimiento: ${fechaNacimiento? new SimpleDateFormat("dd/MM/yyyy").format(fechaNacimiento):""}\n"
        detalle += "${prefijo}estadoNacimiento: ${estadoNacimiento?:""}\n"
        detalle += "${prefijo}correoElectronico: ${correoElectronico?:""}\n"
        detalle += "${prefijo}nombreCompleto: ${nombreCompleto?:""}\n"
        
        return detalle
    }
}
