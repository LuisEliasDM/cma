package estructuraXml

import catalogo.FormatoRepresentacionGrafica
import java.text.SimpleDateFormat

class RepresentacionGrafica {
    Integer id
    static belongsTo=[tituloElectronico:TituloElectronico]
    FormatoRepresentacionGrafica formato
    String codigo
    Integer descargas
    Integer descargasInstitucion
    Date ultimaDescarga
    Date ultimaDescargaInstitucion
    boolean descargado
    boolean activo
    Date dateCreated

    static constraints = {
        formato nullable: false
        codigo nullable: false, maxSize: 256
        descargas nullable: true
        descargasInstitucion nullable: true
        descargado nullable: false
        ultimaDescarga nullable: true
        ultimaDescargaInstitucion nullable: true
        activo nullable: false
        dateCreated display:false, nullable:true
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "repg_representacion_grafica"
        
        tituloElectronico sqlType: "BIGINT"
        descargas defaultValue: "0"
        descargasInstitucion defaultValue: "0"
        descargado defaultValue: "0"
        activo defaultValue: "1"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
    }
        
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}formato: ${formato?:""}\n"
        detalle += "${prefijo}descargas: ${descargas?:""}\n"
        detalle += "${prefijo}descargasInstitucion: ${descargasInstitucion?:""}\n"
        detalle += "${prefijo}descargado: ${descargado?:""}\n"
        detalle += "${prefijo}ultimaDescarga: ${ultimaDescarga? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ultimaDescarga) :""}\n"
        detalle += "${prefijo}ultimaDescargaInstitucion: ${ultimaDescargaInstitucion? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ultimaDescargaInstitucion) :""}\n"
        detalle += "${prefijo}activo: ${activo? "Si":"No"}\n"
        
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
    
}
