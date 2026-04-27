package seguridad

import catalogo.InstitucionEducativa
import java.text.SimpleDateFormat
import org.codehaus.groovy.grails.core.io.ResourceLocator
import org.mirzsoft.grails.actionlogging.ActionLoggingEvent
import org.mirzsoft.grails.actionlogging.Constants.EventStatus

class SeguridadService {
    def mailService
    ResourceLocator grailsResourceLocator
    def actionLoggingService
    
    def registrarAccesoCorrecto(Long idUsuario, String remoteAddress, String sessionId){
        Usuario.withTransaction {
            def usuario = Usuario.get(idUsuario)

            if (usuario) {

                ActionLoggingEvent event = new ActionLoggingEvent(
                    controllerName : "login",
                    actionName : "index",
                    customActionName : "Acceso de usuario (correcto)",
                    status : EventStatus.SUCCESS,
                    actionType : "Gestión de usuarios",
                    startTime : System.currentTimeMillis(),
                    endTime : System.currentTimeMillis(),
                    totalTime : 0,
                    date : new Date(),
                    forwardURI : "",
                    remoteHost : remoteAddress,
                    ajax : false,
                    params : "",
                    userId : usuario?.id
                )

                event.customLog = "== Acceso de usuario (correcto) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==\n"
                event.customLog += "Usuario: ${usuario?.toPlainText("\t")}\n"
                event.customLog += "Sesión: ${sessionId}\n"
                event.customLog += "Usuario autenticado correctamente\n"

                actionLoggingService.save(event)

            }
        }
    }
    
    def registrarAccesoIncorrecto(String username, String remoteAddress, String sessionId, String estatus){
        Usuario.withTransaction {
            def usuario = Usuario.findByUsername(username)

            ActionLoggingEvent event = new ActionLoggingEvent(
                controllerName : "login",
                actionName : "index",
                customActionName : "Acceso de usuario (${estatus})",
                status : EventStatus.SUCCESS,
                actionType : "Gestión de usuarios",
                startTime : System.currentTimeMillis(),
                endTime : System.currentTimeMillis(),
                totalTime : 0,
                date : new Date(),
                forwardURI : "",
                remoteHost : remoteAddress,
                ajax : false,
                params : "",
                userId : usuario?.id
            )

            event.customLog = "== Acceso de usuario (${estatus}) - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==\n"
            event.customLog += "Usuario: ${usuario? usuario?.toPlainText("\t") : username}\n"
            event.customLog += "Sesión: ${sessionId}\n"
            event.customLog += "El usuario no se pudo autenticar\n"

            actionLoggingService.save(event)
            
        }
    }
    
    def registrarSalida(Long idUsuario, String remoteAddress, String sessionId){
        Usuario.withTransaction {
            def usuario = Usuario.get(idUsuario)

            if (usuario) {

                ActionLoggingEvent event = new ActionLoggingEvent(
                    controllerName : "logout",
                    actionName : "index",
                    customActionName : "Salida de usuario",
                    status : EventStatus.SUCCESS,
                    actionType : "Gestión de usuarios",
                    startTime : System.currentTimeMillis(),
                    endTime : System.currentTimeMillis(),
                    totalTime : 0,
                    date : new Date(),
                    forwardURI : "",
                    remoteHost : remoteAddress,
                    ajax : false,
                    params : "",
                    userId : usuario?.id
                )

                event.customLog = "== Salida de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==\n"
                event.customLog += "Usuario: ${usuario?.toPlainText("\t")}\n"
                event.customLog += "Sesión: ${sessionId}\n"
                event.customLog += "El usuario salió correctamente\n"

                actionLoggingService.save(event)

            }
        }
    }

    def generaContrasena() {
        def numero = 12
        def contrasena = ""
        
        def posible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < numero; i++) {
            def r = new Random();
            contrasena = contrasena + posible.charAt(r.nextInt(posible.length() - 2));
        }
        return contrasena
    }
    
    def generaFolio() {
        def numero = 64
        def folio = ""
        
        def posible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < numero; i++) {
            def r = new Random();
            folio = folio + posible.charAt(r.nextInt(posible.length() - 2));
        }
        return folio
    }
    
    def generaIdentificadorConsecutivo() {
        def identificador = 0
        
        identificador = (Usuario.count()?:0) + 1
        
        return identificador
    }
    
    def generaIdentificadorEstructurado(Usuario usuarioInstance, InstitucionEducativa institucionEducativa, Integer idConsecutivo){
        def identificador = ""
        
        def fecha = new Date()
        
        identificador = (fecha.format("yyyyMMdd")?:"").trim().padLeft(8,"0")
        identificador += (institucionEducativa? institucionEducativa?.clave?:"170000" : "170000").trim().padLeft(8,"0")
        identificador += (idConsecutivo?:(generaIdentificadorConsecutivo()?:0)).toString().padLeft(8,"0")
        
        return identificador
    }
    
    def generaNombreUsuario(String nombre, String apellido){
        def nombreUsuario = ""
        
        def nombrePartes = nombre?.trim()?.split(" ")
                        
        for (def x = 0; x < nombrePartes.length && x < 2; x++) {
            nombreUsuario = "${nombreUsuario}-${nombrePartes[x]}"
        }
        
        def apellidoPartes = apellido?.trim()?.split(" ")
        
        nombreUsuario = "${nombreUsuario}.${apellidoPartes[0]}"
        
        nombreUsuario = nombreUsuario.substring(1,nombreUsuario.length())
        
        nombreUsuario = nombreUsuario.toLowerCase()
        
        def existente = Usuario.findByUsername(nombreUsuario)
        
        if (existente) {
            nombreUsuario = "${nombreUsuario}.${generaIdentificadorConsecutivo()}"
        }
        
        return nombreUsuario
    }
    
    def enviarConfirmacionCuenta(Usuario usuarioInstance, Perfil perfilInstance){
        mailService.sendMail {
            multipart true
            from "Título Eletrónico Morelos<tituloelectronico@morelos.gob.mx>"
            to "${perfilInstance?.correoElectronico}"
            subject "Confirmacion de su cuenta"
            inline "logoMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_morelos.png")?.getFile()
            inline "logoAnfitrion", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_anfitrion.png")?.getFile()
            inline "plecaMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/pleca_morelos.png")?.getFile()
            html view: "/email/confirmacionCuenta", model: [usuario: usuarioInstance?.username, folio: perfilInstance?.folioConfirmacion]
        }
    }
    
    def enviarConfirmacionEmail(String correoElectronico, String folio){
        mailService.sendMail {
            multipart true
            from "Título Eletrónico Morelos<tituloelectronico@morelos.gob.mx>"
            to "${correoElectronico}"
            subject "Confirmacion de coreo electronico"
            inline "logoMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_morelos.png")?.getFile()
            inline "logoAnfitrion", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_anfitrion.png")?.getFile()
            inline "plecaMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/pleca_morelos.png")?.getFile()
            html view: "/email/confirmacionEmail", model: [folio: folio]
        }
    }
    
    def enviarRecuperacion(String correoElectronico, String folio){
        mailService.sendMail {
            multipart true
            from "Título Eletrónico Morelos<tituloelectronico@morelos.gob.mx>"
            to "${correoElectronico}"
            subject "Recuperacion de contraseña"
            inline "logoMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_morelos.png")?.getFile()
            inline "logoAnfitrion", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_anfitrion.png")?.getFile()
            inline "plecaMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/pleca_morelos.png")?.getFile()
            html view: "/email/recuperacionContrasena", model: [folio: folio]
        }
    }
    
    def enviarAvisoAcceso(Usuario usuarioInstance, String correoElectronico){
        mailService.sendMail {
            multipart true
            from "Título Eletrónico Morelos<tituloelectronico@morelos.gob.mx>"
            to "${correoElectronico}"
            subject "Acceso a la plataforma"
            inline "logoMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_morelos.png")?.getFile()
            inline "logoAnfitrion", "image/png", grailsResourceLocator.findResourceForURI("/images/logo_anfitrion.png")?.getFile()
            inline "plecaMorelos", "image/png", grailsResourceLocator.findResourceForURI("/images/pleca_morelos.png")?.getFile()
            html view: "/email/avisoAcceso", model: [usuario: usuarioInstance?.username]
        }
    }
}
