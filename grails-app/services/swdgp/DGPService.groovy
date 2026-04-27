package swdgp

import wslite.soap.SOAPClient
import wslite.soap.SOAPResponse
import java.util.zip.*
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class DGPService {
    def xmlService
    def firmaElectronicaService
    /*def url = "https://metqa.siged.sep.gob.mx/met-ws/services/TitulosElectronicos"
    def usuario = "usuariomet.qa16"
    def contrasena = "XzDIjiUV"*/
    
    def url = "https://met.sep.gob.mx/met-ws/services/TitulosElectronicos"
    def usuario = "usuarioMET.prod1700a"
    def contrasena = "he0NwAagJ3"

    def cargarTituloElectronico(def tituloElectronicoInstance) {
        def resultado = [:]
        
        if (!tituloElectronicoInstance) {
            return [estatus: false, mensaje: "Seleccione un titulo"]
        }
        
        def nombreArchivo = tituloElectronicoInstance?.getNombreArchivo("xml")
        def base64 = firmaElectronicaService.bytesToBase64(firmaElectronicaService.toByteArray(xmlService?.generarArchivoXml(tituloElectronicoInstance)))
        
        def archivoRegistro = new ArchivoRegistro(
            nombre: nombreArchivo,
            base64: base64
        )
        
        def registro = new Registro(
            tituloElectronico: tituloElectronicoInstance,
            archivoRegistro: archivoRegistro
        )
                
        def client = new SOAPClient(url)

        def response
        try {
            response = client.send(SOAPAction: "cargaTituloElectronicoRequest", sslTrustAllCerts: true) {
                body {
                    "sch:cargaTituloElectronicoRequest"("xmlns:sch": "http://ws.web.mec.sep.mx/schemas") {
                        "sch:nombreArchivo"(nombreArchivo)
                        "sch:archivoBase64"(base64)
                        "sch:autenticacion" {
                            "sch:usuario"(usuario)
                            "sch:password"(contrasena)
                        }
                    }
                }
            }
        } catch (Exception ex) {}
        
        if (response) {
            registro.codigoEstatusEnvio = response.httpResponse.statusCode
            registro.mensajeEstatusEnvio = response.httpResponse.statusMessage
            registro.estatusEnvio = response.httpResponse.statusCode == 200
                        
            if (response.httpResponse.statusCode == 200) {
                def resp = response.cargaTituloElectronicoResponse
                
                if (resp?.numeroLote != "") {
                    def numeroLote = resp?.numeroLote?.text()?.toInteger()
                    def mensaje = numeroLote? null: resp.mensaje.text()

                    registro.numeroLote = numeroLote
                    registro.mensajeRegistro = mensaje

                    if (archivoRegistro.save()) {
                        if (registro.save()){
                            resultado.estatus = true 
                            resultado.mensaje = "Título transmitido correctamente"
                        } else {
                            println registro.errors
                            resultado.estatus = false 
                            resultado.mensaje = "1: No se pudo recuperar la respuesta del servidor"
                        }
                    } else {
                        println archivoRegistro.errors
                        resultado.estatus = false 
                        resultado.mensaje = "2: No se pudo recuperar la respuesta del servidor"
                    }
                } else {
                    resultado.estatus = false 
                    resultado.mensaje = "${resp}"
                }            
                
            } else {
                resultado.estatus = false 
                resultado.mensaje = "No se pudo transmitir el título. Error al establecer la conexión"
            }

        } else {
            resultado.estatus = false 
            resultado.mensaje = "No se pudo transmitir el título. Error general"
        }
        
        resultado.registro = registro

        return resultado
    }
    
    def consultarProcesoTituloElectronico(def registroInstance){
        def resultado = [:]
        
        if (!registroInstance) {
            return [estatus: false, mensaje: "Seleccione un registro"]
        }
        
        def consulta = new Consulta(
            registro: registroInstance
        )
                
        def client = new SOAPClient(url)
        
        def response
        try {
            response = client.send(SOAPAction: "consultaProcesoTituloElectronicoRequest", sslTrustAllCerts: true) {
                body {
                    "sch:consultaProcesoTituloElectronicoRequest"("xmlns:sch": "http://ws.web.mec.sep.mx/schemas") {
                        "sch:numeroLote"(registroInstance.numeroLote)
                        "sch:autenticacion" {
                            "sch:usuario"(usuario)
                            "sch:password"(contrasena)
                        }
                    }
                }
            }
        } catch (Exception ex) {}
        
        if (response) {
            consulta.codigoEstatusConsulta = response.httpResponse.statusCode
            consulta.mensajeEstatusConsulta = response.httpResponse.statusMessage
            consulta.estatusConsulta = response.httpResponse.statusCode == 200
                        
            if (response.httpResponse.statusCode == 200) {
                def resp = response.consultaProcesoTituloElectronicoResponse
                def numeroLote = resp.numeroLote.text().toInteger()
                def estatusLote = resp.estatusLote.text().toInteger()
                def mensaje = resp.mensaje.text()
                
                consulta.numeroLote = numeroLote
                consulta.estatusLote = estatusLote
                consulta.mensajeConsulta = mensaje
                
                if (consulta.save()){
                    resultado.estatus = true 
                    resultado.mensaje = "Consulta transmitida correctamente"
                } else {
                    println consulta.errors
                    resultado.estatus = false 
                    resultado.mensaje = "1: No se pudo recuperar la respuesta del servidor"
                }
                
            } else {
                resultado.estatus = false 
                resultado.mensaje = "No se pudo transmitir la consulta. Error al establecer la conexión"
            }
            
        } else {
            resultado.estatus = false 
            resultado.mensaje = "No se pudo transmitir la consulta. Error general"
        }
        
        resultado.consulta = consulta
        
        return resultado
    }
    
    def descargarTituloElectronico(def registroInstance){
        def resultado = [:]
        
        if (!registroInstance) {
            return [estatus: false, mensaje: "Seleccione un registro"]
        }
        
        def nombreArchivo = registroInstance.tituloElectronico.getNombreArchivo("zip")
        
        def archivoResultado = new ArchivoResultado(
            registro: registroInstance,
            nombre: nombreArchivo
        )
                
        def client = new SOAPClient(url)
        
        def response
        try {
            response = client.send(SOAPAction: "descargaTituloElectronicoRequest", sslTrustAllCerts: true) {
                body {
                    "sch:descargaTituloElectronicoRequest"("xmlns:sch": "http://ws.web.mec.sep.mx/schemas") {
                        "sch:numeroLote"(registroInstance.numeroLote)
                        "sch:autenticacion" {
                            "sch:usuario"(usuario)
                            "sch:password"(contrasena)
                        }
                    }
                }
            }
        } catch (Exception ex) {}
        
        if (response) {
            archivoResultado.codigoEstatusDescarga = response.httpResponse.statusCode
            archivoResultado.mensajeEstatusDescarga = response.httpResponse.statusMessage
            archivoResultado.estatusDescarga = response.httpResponse.statusCode == 200
            
            if (response.httpResponse.statusCode == 200) {
                def resp = response.descargaTituloElectronicoResponse
                def numeroLote = resp.numeroLote.text().toInteger()
                def mensaje = resp.mensaje.text()
                def base64 = resp.titulosBase64.text()
                
                archivoResultado.numeroLote = numeroLote
                archivoResultado.mensajeDescarga = mensaje
                archivoResultado.base64 = base64
                
                if (archivoResultado.save()){
                    if (base64) {
                        
                        def procesoArchivo = procesarArchivoResultado(archivoResultado)

                        if (procesoArchivo.estatus) {
                            resultado.estatus = true 
                            resultado.mensaje = "Consulta del archivo de respuesta realizada correctamente"
                            
                            archivoResultado.refresh()
                            
                        } else {
                            resultado = procesoArchivo
                        }
                        
                        resultado.archivoResultado = archivoResultado
                        
                    } else {
                        resultado.estatus = false 
                        resultado.mensaje = "No se pudo recuperar el archivo de respuesta"
                    }
                } else {
                    println consulta.errors
                    resultado.estatus = false 
                    resultado.mensaje = "1: No se pudo recuperar la respuesta del servidor"
                }
                
            } else {
                resultado.estatus = false 
                resultado.mensaje = "No se pudo transmitir la descarga. Error al establecer la conexión"
            }
            
        } else {
            resultado.estatus = false 
            resultado.mensaje = "No se pudo transmitir la descarga. Error general"
        }
        
        resultado.archivo = archivoResultado

        return resultado
    }
    
    def cancelarTituloElectronico(def registroInstance, def motivoCancelacionInstance){
        def resultado = [:]
        
        if (!registroInstance) {
            return [estatus: false, mensaje: "Seleccione una solicitud de registro"]
        }
        
        if (!motivoCancelacionInstance) {
            return [estatus: false, mensaje: "Seleccione el motivo de cancelación"]
        }
        
        def cancelacion = new Cancelacion(
            registro: registroInstance,
            folioControl: registroInstance.tituloElectronico.folioControl,
            motivoCancelacion: motivoCancelacionInstance.id
        )
                
        def client = new SOAPClient(url)
        
        def response
        try {
            response = client.send(SOAPAction: "cancelaTituloElectronicoRequest", sslTrustAllCerts: true) {
                body {
                    "sch:cancelaTituloElectronicoRequest"("xmlns:sch": "http://ws.web.mec.sep.mx/schemas") {
                        "sch:folioControl"(cancelacion.folioControl)
                        "sch:motCancelacion"(cancelacion.motivoCancelacion)
                        "sch:autenticacion" {
                            "sch:usuario"(usuario)
                            "sch:password"(contrasena)
                        }
                    }
                }
            }
        } catch (Exception ex) {}
        
        if (response) {
            cancelacion.codigoEstatusCancelacion = response.httpResponse.statusCode
            cancelacion.mensajeEstatusCancelacion = response.httpResponse.statusMessage
            cancelacion.estatusCancelacion = response.httpResponse.statusCode == 200
                        
            if (response.httpResponse.statusCode == 200) {
                def resp = response.cancelaTituloElectronicoResponse
                def codigoEjecucion = resp.codigo.text().toInteger()
                def mensajeEjecucion = resp.mensaje.text()
                
                cancelacion.codigoEjecucion = codigoEjecucion
                cancelacion.mensajeEjecucion = mensajeEjecucion
                cancelacion.cancelado = codigoEjecucion == 0
                
                if (cancelacion.save()){
                    resultado.estatus = true 
                    resultado.mensaje = "Cancelación transmitida correctamente"
                } else {
                    println consulta.errors
                    resultado.estatus = false 
                    resultado.mensaje = "1: No se pudo recuperar la respuesta del servidor"
                }
                
            } else {
                resultado.estatus = false 
                resultado.mensaje = "No se pudo transmitir la cancelación. Error al establecer la conexión"
            }
            
        } else {
            resultado.estatus = false 
            resultado.mensaje = "No se pudo transmitir la cancelación. Error general"
        }
        
        resultado.cancelacion = cancelacion
        
        return resultado
    }
    
    def procesarArchivoResultado(def archivoResultadoInstance){
        def resultado = [:]
        
        if (!archivoResultadoInstance) {
            return [estatus: false, mensaje: "Seleccione un archivo"]
        }
        
        def now = new Date()
         
        def archivoZip = firmaElectronicaService.byteArrayToFile("/tmp/${now.getTime()}_${archivoResultadoInstance.nombre}", firmaElectronicaService.base64toBytes(archivoResultadoInstance.base64))
                
        if (!archivoZip) {
            return [estatus: false, mensaje: "No se pudo procesar el archivo zip"]
        }
        
        def unzip = unzip("/tmp/${now.getTime()}_${archivoResultadoInstance.nombre}Dir/", archivoZip)
        
        if (!unzip) {
            return [estatus: false, mensaje: "No se pudo extraer el contenido del archivo zip"]
        }
                    
        try {
            new File("/tmp/${now.getTime()}_${archivoResultadoInstance.nombre}Dir/").eachFile() { file -> 
                if (file.isFile()){
                    leerXls(file, archivoResultadoInstance)
                }
                return false
            }
        } catch (Exception ex) {
            return [estatus: false, mensaje: "Error general: No se pudo procesar el archivo excel"]
        }
        
        resultado.estatus = true
        resultado.mensaje = "Archivo de respuesta consultado y procesado correctamente"
        
        return resultado
    }
    
    def leerXls(def archivoExcel, def archivoResultadoInstance){
        def fileInputStream = new FileInputStream(archivoExcel)
        def workBook = new HSSFWorkbook(fileInputStream)
        def hssfSheet = workBook.getSheetAt(0)
        def rowIterator = hssfSheet.rowIterator()
        
        def rowNumber = 0
        while (rowIterator.hasNext()) {
            def hssfRow = rowIterator.next()
            
            if (hssfRow.getRowNum() > 0) {

                def detalleArchivoResultado = new DetalleArchivoResultado(
                    archivoResultado: archivoResultadoInstance
                )

                try {
                    def archivoCell = hssfRow.getCell(0)
                    archivoCell.setCellType(Cell.CELL_TYPE_STRING)
                    detalleArchivoResultado.nombreArchivo = archivoCell.getStringCellValue().trim()
                } catch (Exception ex) {}

                try {
                    def estatusCell = hssfRow.getCell(1)
                    estatusCell.setCellType(Cell.CELL_TYPE_NUMERIC)
                    detalleArchivoResultado.estatus = estatusCell.getNumericCellValue()
                } catch (Exception ex) {}

                try {
                    def descripcionCell = hssfRow.getCell(2)
                    descripcionCell.setCellType(Cell.CELL_TYPE_STRING)
                    detalleArchivoResultado.descripcion = descripcionCell.getStringCellValue().trim()
                } catch (Exception ex) {}

                try {
                    def folioControlCell = hssfRow.getCell(3)
                    folioControlCell.setCellType(Cell.CELL_TYPE_STRING)
                    detalleArchivoResultado.folioControl = folioControlCell.getStringCellValue().trim()
                } catch (Exception ex) {}

                if (!detalleArchivoResultado.save(flush: true)){
                    println detalleArchivoResultado.errors
                }
            }
            rowNumber++
        }
        
    }
    
    def zip(def directorioEntrada, nombreArchivoZip) {
        def result = true
        
        if (!directorioEntrada || !nombreArchivoZip) {
            return false
        }
        
        try {
            ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(nombreArchivoZip))  
            new File(directorioEntrada).eachFile() { file -> 
                if (file.isFile()){
                    zipFile.putNextEntry(new ZipEntry(file.name))
                    def buffer = new byte[file.size()]  
                    file.withInputStream { 
                        zipFile.write(buffer, 0, it.read(buffer))  
                    }  
                    zipFile.closeEntry()
                }
            }  
            zipFile.close()
        } catch(Exception ex) {
            result = false
        }
        return result
    }
    
    def unzip(def directorioSalida, def archivoZip) {
        def result = true
        
        if (!archivoZip) {
            return false
        }
                
        try {
            def zip = new ZipFile(archivoZip)
            zip.entries().each {
                if (!it.isDirectory()){
                    def fOut = new File(directorioSalida+ File.separator + it.name)
                    //create output dir if not exists
                    new File(fOut.parent).mkdirs()
                    def fos = new FileOutputStream(fOut)
                    //println "name:${it.name}, size:${it.size}"
                    def buf = new byte[it.size]
                    def len = zip.getInputStream(it).read(buf) //println zip.getInputStream(it).text
                    fos.write(buf, 0, len)
                    fos.close()
                }
            }
            zip.close()
        } catch(Exception ex) {
            ex.printStackTrace()
            result = false
        }
        return result
    }
}
