package seguridad

import catalogo.InstitucionEducativa
import java.text.SimpleDateFormat

class Perfil {
    String curp
    String nombre
    String primerApellido
    String segundoApellido
    String sexo
    Date fechaNacimiento
    String estadoNacimiento
    String correoElectronico
    String telefonoFijo
    String telefonoMovil
    String cargo
    String numeroIdentificacion
    String folioConfirmacion
    String folioRecuperacion
    Boolean confirmado = false
    Boolean acuseEntregado = false
    Boolean contrasenaRecuperada = true
    Date dateCreated
    Date fechaConfirmacion
    Date fechaEntregaAcuse
    Date fechaRecuperacion
    String nombreCompleto
        
    static constraints = {
        curp nullable: false, maxSize: 20
        nombre nullable: false, maxSize: 256
        primerApellido nullable: false, maxSize: 256
        segundoApellido nullable: true, maxSize: 256
        sexo nullable: false, maxSize: 2
        fechaNacimiento nullable: false
        estadoNacimiento nullable: false, maxSize: 4
        correoElectronico nullable: false, maxSize: 256
        telefonoFijo nullable: true, maxSize: 256
        telefonoMovil nullable: true, maxSize: 256
        cargo nullable: true, maxSize: 1024
        numeroIdentificacion nullable: false, maxSize: 32
        folioConfirmacion nullable: true, maxSize: 256
        folioRecuperacion nullable: true, maxSize: 256
        confirmado nullable: false
        acuseEntregado nullable: false
        contrasenaRecuperada nullable: false
        dateCreated nullable: true
        fechaConfirmacion nullable: true
        fechaEntregaAcuse nullable: true
        fechaRecuperacion nullable: true
        nombreCompleto nullable: true
    }
    
    static mapping = {
        version false
        
        table "seg_perfil"
        
        fechaNacimiento sqlType: "DATE"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        nombreCompleto formula: "CONCAT(nombre,' ',primer_apellido,' ', segundo_apellido)"
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
        detalle += "${prefijo}nombreCompleto: ${nombreCompleto?:""}\n"
        detalle += "${prefijo}sexo: ${sexo?:""}\n"
        detalle += "${prefijo}fechaNacimiento: ${fechaNacimiento? new SimpleDateFormat("dd/MM/yyyy").format(fechaNacimiento):""}\n"
        detalle += "${prefijo}estadoNacimiento: ${estadoNacimiento?:""}\n"
        detalle += "${prefijo}correoElectronico: ${correoElectronico?:""}\n"
        detalle += "${prefijo}telefonoFijo: ${telefonoFijo?:""}\n"
        detalle += "${prefijo}telefonoMovil: ${telefonoMovil?:""}\n"
        detalle += "${prefijo}cargo: ${cargo?:""}\n"
        detalle += "${prefijo}numeroIdentificacion: ${numeroIdentificacion?:""}\n"
        detalle += "${prefijo}folioConfirmacion: ${folioConfirmacion?:""}\n"
        detalle += "${prefijo}folioRecuperacion: ${folioRecuperacion?:""}\n"
        detalle += "${prefijo}confirmado: ${confirmado? "Si":"No"}\n"
        detalle += "${prefijo}acuseEntregado: ${acuseEntregado? "Si":"No"}\n"
        detalle += "${prefijo}contrasenaRecuperada: ${contrasenaRecuperada? "Si":"No"}\n"
        detalle += "${prefijo}fechaConfirmacion: ${fechaConfirmacion? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaConfirmacion):""}\n"
        detalle += "${prefijo}fechaEntregaAcuse: ${fechaEntregaAcuse? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEntregaAcuse):""}\n"
        detalle += "${prefijo}fechaRecuperacion: ${fechaRecuperacion? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaRecuperacion):""}\n"
        detalle += "${prefijo}fechaRegistro: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated):""}\n"

        return detalle
    }
}
