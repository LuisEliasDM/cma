package estructuraXml

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.grails.databinding.SimpleMapDataBindingSource
import java.sql.Timestamp
import groovy.xml.MarkupBuilder

class XmlService {
    def grailsApplication
    
    def generarArchivoXml(def tituloElectronicoInstance){
        
        def xml = new File("/tmp/${tituloElectronicoInstance?.getNombreArchivo("xml")}")
        
        def contenido = construir(tituloElectronicoInstance)
        
        xml.withWriter("UTF-8") { writer ->
            writer.write(contenido)
        }

        return xml
    }
    
    def construir(dominioPrincipal){
        def camposExluir = ["class", "id", "version", "nombreArchivo", "estatus", "valido", "tituloElectronicoService", "cadenaOriginal", "cadenaOriginalSelloAutenticacion", "dateCreated", "nombreCompleto", "apellidos", "registros", "paquete", "observaciones", "correcciones", "representaciones", "libro", "foja", "fechaRegistroLibro", "aclaracion", "activo"]
        
        def nodos = serializar(dominioPrincipal, camposExluir)
        establecerNamespace(nodos)
        
        def writer = new StringWriter() 
        def xml = new MarkupBuilder(writer)
        xml.setDoubleQuotes(true)
        xml.setEscapeAttributes(true)
        
        xml.mkp.xmlDeclaration(version: "1.0", encoding: "utf-8")
        procesarNodo(nodos, xml)        
        return writer.toString()
        
    }
    
    def establecerNamespace(nodos){
        nodos.atributos.put("xmlns","https://www.siged.sep.gob.mx/titulos/")
        nodos.atributos.put("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance")
        nodos.atributos.put("xsi:schemaLocation","https://www.siged.sep.gob.mx/titulos/schema.xsd")
    }
    
    def procesarNodo(nodo, xml){
        return xml."$nodo.nombre"(nodo.atributos){
            nodo.descendientes?.each { hijo ->
                procesarNodo(hijo, xml)
            }
        }
    }
    
    def serializar(dominio, camposExluir) {
        def nodo = [:]
        def atributos = []
        def descendientes = []
        
        def domainClass = new DefaultGrailsDomainClass(dominio.class)
        
        nodo.nombre = domainClass.getName()
        nodo.atributos = [:]
        nodo.descendientes = []
        camposExluir.add(domainClass.getName())
                        
        domainClass.properties.each { campo ->
            def excluir = false
            
            if (camposExluir*.toLowerCase().contains(campo.name.toLowerCase())) {
                excluir = true
            }
            
            if (nodo.nombre in ["TituloElectronico", "Autenticacion"] && campo.name == "version") {
                excluir = false
            }
            
            if (nodo.nombre == "Profesionista" && campo.name in ["sexo", "fechaNacimiento", "estadoNacimiento"]) {
                excluir = true
            }
            
            if (!excluir) {
                
                def numeroCampo = 1
                def orden = 0
                dominio.class.getDeclaredFields().each { 

                    if (it.name == campo.name) {
                        orden = numeroCampo
                        return false
                    }
                    
                    numeroCampo++
                }
                
                if (grailsApplication.isDomainClass(dominio[campo.name].getClass())) {
                    descendientes.add([orden: orden, campos: serializar(dominio[campo.name], camposExluir)])
                } else if (domainClass.isOneToMany(campo.name)) {
                    dominio[campo.name]?.each { descendiente ->
                        descendientes.add([orden: orden, campos: serializar(descendiente, camposExluir)])
                    }
                } else {
                    def valor = dominio[campo.name]
                    if (valor != null) {
                        if (dominio[campo.name] instanceof Date || dominio[campo.name] instanceof Timestamp) {
                            if (campo.name == "fechaAutenticacion") {
                                valor = dominio[campo.name]?.format("yyyy-MM-dd'T'HH:mm:ss")
                            } else {
                                valor = dominio[campo.name]?.format("yyyy-MM-dd")
                            }
                        }
                        atributos.add([orden: orden, nombre: campo.name, valor: valor])
                    }
                }

            }
        }
        
        descendientes.sort {it.orden}
        atributos.sort {it.orden}
        
        descendientes.each {
            nodo.descendientes.add(it.campos)
        }
        
        atributos.each {
            nodo.atributos.put(it.nombre, it.valor)
        }
        
        return nodo
    }
}
