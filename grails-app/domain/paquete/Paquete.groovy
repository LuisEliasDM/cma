package paquete

import catalogo.InstitucionEducativa
import estructuraXml.TituloElectronico
import java.text.SimpleDateFormat
import seguridad.Usuario

class Paquete {
    Integer id
    InstitucionEducativa institucionEducativa
    String folio
    EstatusPaquete estatus
    Date fechaEnvioRevision
    Date fechaRevision
    Date fechaCotejo
    Date fechaRechazoCotejo
    Date fechaAnulacionRechazoCotejo
    Date fechaAutenticacion
    Date fechaRechazoAutenticacion
    Date fechaRegistroDGP
    Date fechaRechazoRegistroDgp
    Date fechaParaEntrega
    Date fechaEntrega
    Date dateCreated
    Usuario receptor
    Usuario revisor
    Usuario cotejador
    Usuario autenticador
    Usuario entregante
    Usuario emisor
    String motivoRechazoCotejo
    String motivoAnulacionRechazoCotejo
    String motivoRechazoAutenticacion
    String motivoRechazoRegistroDgp
    Boolean activo
    
    static hasMany = [
        titulosElectronicos: TituloElectronico,
        comprobantesPago: ComprobantePago
    ]
    
    static constraints = {
        institucionEducativa nullable: false
        folio nullable: false, maxSize: 128
        estatus nullable: true
        dateCreated nullable: true
        estatus nullable: false
        fechaEnvioRevision nullable: true
        fechaRevision nullable: true
        fechaCotejo nullable: true
        fechaRechazoCotejo nullable: true
        fechaAnulacionRechazoCotejo nullable: true
        fechaAutenticacion nullable: true
        fechaRechazoAutenticacion nullable: true
        fechaRegistroDGP nullable: true
        fechaRechazoRegistroDgp nullable: true
        fechaParaEntrega nullable: true
        fechaEntrega nullable: true
        dateCreated nullable: true
        receptor nullable: true
        revisor nullable: true
        cotejador nullable: true
        autenticador nullable: true
        entregante nullable: true
        emisor nullable: true
        motivoRechazoCotejo nullable: true, maxSize: 50000
        motivoAnulacionRechazoCotejo  nullable: true, maxSize: 50000
        motivoRechazoAutenticacion nullable: true, maxSize: 50000
        motivoRechazoRegistroDgp nullable: true, maxSize: 50000
        activo nullable: false
    }
    
    static mapping = {
        version false
        
        cache false
        
        table "paq_paquete"
        
        titulosElectronicos sort: "dateCreated"
        estatus defaultValue: 1
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
        receptor sqlType: "BIGINT"
        revisor sqlType: "BIGINT"
        cotejador sqlType: "BIGINT"
        autenticador sqlType: "BIGINT"
        entregante sqlType: "BIGINT"
        emisor sqlType: "BIGINT"
        motivoRechazoCotejo sqlType: "TEXT"
        motivoAnulacionRechazoCotejo sqlType: "TEXT"
        motivoRechazoAutenticacion sqlType: "TEXT"
        motivoRechazoRegistroDgp sqlType: "TEXT"
        activo defaultValue: "1"
    }
    
    String toString(){
        return "${folio} IE: ${institucionEducativa}"
    }
    
    def toPlainText(){
        return toPlainText("")
    }
    
    def toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}folio: ${folio?:""}\n"
        detalle += "${prefijo}institucionEducativa: ${institucionEducativa?.toPlainText("${prefijo}\t")?:""}\n"
        detalle += "${prefijo}estatus: ${estatus?:""}\n"
        detalle += "${prefijo}fechaEnvioRevision: ${fechaEnvioRevision? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEnvioRevision) :""}\n"
        detalle += "${prefijo}fechaRevision: ${fechaRevision? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaRevision) :""}\n"
        detalle += "${prefijo}fechaCotejo: ${fechaCotejo? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaCotejo) :""}\n"
        detalle += "${prefijo}fechaRechazoCotejo: ${fechaRechazoCotejo? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaRechazoCotejo) :""}\n"
        detalle += "${prefijo}fechaAutenticacion: ${fechaAutenticacion? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaAutenticacion) :""}\n"
        detalle += "${prefijo}fechaRechazoAutenticacion: ${fechaRechazoAutenticacion? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaRechazoAutenticacion) :""}\n"
        detalle += "${prefijo}fechaRegistroDGP: ${fechaRegistroDGP? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaRegistroDGP) :""}\n"
        detalle += "${prefijo}fechaRechazoRegistroDgp: ${fechaRechazoRegistroDgp? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaRechazoRegistroDgp) :""}\n"
        detalle += "${prefijo}fechaParaEntrega: ${fechaParaEntrega? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaParaEntrega) :""}\n"
        detalle += "${prefijo}fechaEntrega: ${fechaEntrega? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEntrega) :""}\n"
        
        detalle += "${prefijo}receptor: ${receptor?:""}\n"
        detalle += "${prefijo}revisor: ${revisor?:""}\n"
        detalle += "${prefijo}cotejador: ${cotejador?:""}\n"
        detalle += "${prefijo}autenticador: ${autenticador?:""}\n"
        detalle += "${prefijo}entregante: ${entregante?:""}\n"
        detalle += "${prefijo}emisor: ${emisor?:""}\n"
        detalle += "${prefijo}motivoRechazoCotejo: ${motivoRechazoCotejo?:""}\n"
        detalle += "${prefijo}motivoRechazoAutenticacion: ${motivoRechazoAutenticacion?:""}\n"
        detalle += "${prefijo}motivoRechazoRegistroDgp: ${motivoRechazoRegistroDgp?:""}\n"
        
        titulosElectronicos?.each {
            detalle += "${prefijo}tituloElectronico: ${it.folioControl} - ${it.profesionista ? it.profesionista?.curp : ""} - ${it.profesionista ? it.profesionista?.nombreCompleto : ""}\n"
        }
        
        comprobantesPago?.each {
            detalle += "${prefijo}comprobantePago: ${it?.toPlainText("\t\t")?:""}\n"
        }
        
        detalle += "${prefijo}fechaCreacion: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated) :""}\n"
        
        return detalle
    }
}
