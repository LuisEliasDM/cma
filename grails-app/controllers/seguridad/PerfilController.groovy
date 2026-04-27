package seguridad

import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.codehaus.groovy.grails.core.io.ResourceLocator
import org.mirzsoft.grails.actionlogging.annotation.*

@SpringUserIdentification
class PerfilController {
    def springSecurityService
    def actionLoggingService
    def passwordEncoder
    def seguridadService
    ResourceLocator grailsResourceLocator

    def index() {
        def usuario = springSecurityService.loadCurrentUser()
        
        respond usuario
    }
    
    def cambioContrasena(){
        
    }
    
    @ActionLogging
    @ActionType("Perfil de usuario")
    @CustomActionName("Modificación de contraseña")
    @PrintCustomLog
    def cambiarContrasena(){
        actionLoggingService.log("== Modificación de contraseña - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del usuario...")
                        
        if (!params?.contrasenaActual || !params.contrasenaNueva || !params.contrasenaNuevac) {
            flash.error="Es necesario proporcionar los datos completos"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "cambioContrasena"
            return
        }
                
        if (!passwordEncoder.isPasswordValid(usuario.password, params?.contrasenaActual?.trim(), null)) {
            flash.error="La contraseña actual es incorrecta. Por favor intente nuevamente."
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "cambioContrasena"
            return
        }
        
        if (params?.contrasenaNueva?.trim()?.length() < 8 || params?.contrasenaNueva?.trim()?.length() > 32) {
            flash.error="La contraseña debe contener de 8 a 32 caracteres. Por favor intente nuevamente."
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "cambioContrasena"
            return
        }
        
        if (params?.contrasenaNueva?.trim() != params?.contrasenaNuevac?.trim()) {
            flash.error="La contraseña nueva no coincide. Por favor intente nuevamente."
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "cambioContrasena"
            return
        }
        
        if (passwordEncoder.isPasswordValid(usuario.password, params?.contrasenaNueva?.trim(), null)) {
            flash.error="La contraseña nueva no puede ser igual a la actual. Por favor intente nuevamente."
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "cambioContrasena"
            return
        }
        
        usuario.password = params?.contrasenaNueva?.trim()
        usuario.roles = null
        
        actionLoggingService.log("Guardando contraseña...")
        
        if (usuario.save(flush: true)) {
            flash.message="Su contraseña se cambio con exito."
            
            actionLoggingService.log("${flash.message}")
            
            redirect action: "index"
            return
        } else {
            usuario.errors.each {
                println "===="+it+"-------"
            }
            
            flash.error= usuario.perfil.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "cambioContrasena"
            return
        }
    }
    
    def recuperacionContrasena(){
        
    }
    
    @ActionLogging
    @ActionType("Perfil de usuario")
    @CustomActionName("Recuperación de contraseña")
    @PrintCustomLog
    @Transactional
    def recuperarContrasena(){
        actionLoggingService.log("== Recuperación de contraseña - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a recuperar contraseña...")
        
        actionLoggingService.log("correoElectronico: ${params?.correoElectronico?.trim()}")        
        
        if (!params.correoElectronico) {
            flash.error="Es necesario proporcionar el correo electronico"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "recuperacionContrasena"
            return
        }
        
        def usuarioInstance = Usuario.createCriteria().get {
            perfil {
                eq("correoElectronico", params?.correoElectronico?.trim())
            }
        }
        
        if (!usuarioInstance) {
            flash.error="El correo electroinico proporcionado no se encuentra registrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "recuperacionContrasena"
            return
        }
        
        if (!usuarioInstance?.enabled) {
            flash.error="La cuenta asociada al correo electronico ingresado, se encuentra inactiva"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "recuperacionContrasena"
            return
        }
        
        if (!usuarioInstance?.perfil?.confirmado || !usuarioInstance?.perfil?.acuseEntregado) {
            flash.error="La cuenta asociada al correo electronico ingresado, no ha finalizado el proceso de registro"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "recuperacionContrasena"
            return
        }
        
        actionLoggingService.log("Usuario (original): ${usuarioInstance?.toPlainText("\t")}")
        actionLoggingService.log("Perfil (original): ${usuarioInstance?.perfil?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del usuario...")
        
        def perfil = usuarioInstance?.perfil
        perfil?.contrasenaRecuperada = false
        perfil?.folioRecuperacion = seguridadService.generaFolio()
        
        actionLoggingService.log("Usuario (actualizado): ${usuarioInstance?.toPlainText("\t")}")
        actionLoggingService.log("Perfil (actualizado): ${perfil?.toPlainText("\t")}")
                
        actionLoggingService.log("Guardando recuperación de contraseña...")
        
        if (perfil.save(flush:true)) {
            
            actionLoggingService.log("Enviando correo electrónico de recuperación...")
            
            seguridadService.enviarRecuperacion(perfil?.correoElectronico, perfil?.folioRecuperacion)
            
            flash.message="Se envio un enlace a su correo electronico desde el cual podra establecer su nueva contraseña"
            
            actionLoggingService.log("${flash.message}")
            
            redirect controller: "login", action: "auth"
            return
        } else {
            flash.error = usuarioInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "recuperacionContrasena"
            return
        }
        
    }
    
    def confirmacionCuenta(){
        def perfil = Perfil.findByFolioConfirmacion(params?.folio?.trim())
        
        [perfil: perfil]
        
    }
    
    @ActionLogging
    @ActionType("Perfil de usuario")
    @CustomActionName("Confirmación de cuenta")
    @PrintCustomLog
    @Transactional
    def confirmarCuenta(){
        actionLoggingService.log("== Confirmación de cuenta - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a confirmar cuenta...")
        
        actionLoggingService.log("folio: ${params?.folio?.trim()}")  
        
        def perfil = Perfil.findByFolioConfirmacion(params?.folio?.trim())
                
        if (!perfil) {
            flash.error="La confirmacion de cuenta mediante este enlace, no se encuentra disponible"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "confirmacionCuenta"
            return
        }
        
        if (!params?.contrasenaNueva?.trim() || !params?.contrasenaNuevac?.trim()) {
            flash.error="Es necesario proporcionar una contraseña nueva. Por favor intente nuevamente"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "confirmacionCuenta", params: [folio: params?.folio?.trim()]
            return
        }
        
        if (params?.contrasenaNueva?.trim() != params?.contrasenaNuevac?.trim()) {
            flash.error="Las contraseñas ingresadas no coinciden. Por favor intente nuevamente"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "confirmacionCuenta", params: [folio: params?.folio?.trim()]
            return
        }
        
        def usuarioInstance = Usuario.findByPerfil(perfil)
        
        if (!usuarioInstance) {
            flash.error="La confirmacion de cuenta mediante este enlace, no se encuentra disponible"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "confirmacionCuenta", params: [folio: params?.folio?.trim()]
            return
        }
        
        actionLoggingService.log("Usuario (original): ${usuarioInstance?.toPlainText("\t")}")
        actionLoggingService.log("Perfil (original): ${perfil?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del usuario...")
                
        usuarioInstance.password = params?.contrasenaNueva?.trim()
        perfil.confirmado = true
        perfil.fechaConfirmacion = new Date()
        usuarioInstance.roles = null
        
        actionLoggingService.log("Usuario (actualizado): ${usuarioInstance?.toPlainText("\t")}")
        actionLoggingService.log("Perfil (actualizado): ${perfil?.toPlainText("\t")}")
                
        actionLoggingService.log("Guardando confirmación de cuenta...")
        
        if (usuarioInstance.save(flush: true)) {
            if(perfil.save(flush: true)) {
                flash.message="Su cuenta se confirmó con éxito."
                
                actionLoggingService.log("${flash.message}")
                
                redirect action: "confirmacionCuenta", params: [folio: params?.folio?.trim()]
                return 
            } else {
                flash.error = perfil.errors
                
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "confirmacionCuenta", params: [folio: params?.folio?.trim()]
                return
            }
            
        } else {
            flash.error = usuarioInstance.errors
                
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "confirmacionCuenta", params: [folio: params?.folio?.trim()]
            return
        }
               
    }
    
    def restablecimientoContrasena(){
        def perfil = Perfil.findByFolioRecuperacion(params?.folio?.trim())
        [perfil: perfil]
        
    }
    
    @ActionLogging
    @ActionType("Perfil de usuario")
    @CustomActionName("Restablecimiento de contraseña")
    @PrintCustomLog
    @Transactional
    def restablecerContrasena(){
        actionLoggingService.log("== Restablecimiento de contraseña - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a restablecer contraseña...")
        
        actionLoggingService.log("folio: ${params?.folio?.trim()}")  
        
        def perfil = Perfil.findByFolioRecuperacion(params?.folio?.trim())
        
        if (!perfil) {
            flash.error="La recuperacion de contraseña mediante este enlace, no se encuentra disponible"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "restablecimientoContrasena"
            return
        }
        
        if (!params?.contrasenaNueva?.trim() || !params?.contrasenaNuevac?.trim()) {
            flash.error="Es necesario proporcionar una contraseña nueva. Por favor intente nuevamente"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "restablecimientoContrasena", params: [folio: params?.folio?.trim()]
            return
        }
        
        if (params?.contrasenaNueva?.trim() != params?.contrasenaNuevac?.trim()) {
            flash.error="Las contraseñas ingresadas no coinciden. Por favor intente nuevamente"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "restablecimientoContrasena", params: [folio: params?.folio?.trim()]
            return
        }
        
        def usuarioInstance = Usuario.findByPerfil(perfil)
        
        if (!usuarioInstance) {
            flash.error="La confirmacion de cuenta mediante este enlace, no se encuentra disponible"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "restablecimientoContrasena", params: [folio: params?.folio?.trim()]
            return
        }
        
        if (!usuarioInstance?.enabled) {
            flash.error="La cuenta asociada al correo electronico ingresado, se encuentra inactiva"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "restablecimientoContrasena", params: [folio: params?.folio?.trim()]
            return
        }
        
        if (!usuarioInstance?.perfil?.confirmado || !usuarioInstance?.perfil?.acuseEntregado) {
            flash.error="La cuenta asociada al correo electronico ingresado, no ha finalizado el proceso formal de registro"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "restablecimientoContrasena", params: [folio: params?.folio?.trim()]
            return
        }
        
        actionLoggingService.log("Usuario (original): ${usuarioInstance?.toPlainText("\t")}")
        actionLoggingService.log("Perfil (original): ${perfil?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del usuario...")
                
        usuarioInstance.password = params?.contrasenaNueva?.trim()
        perfil.contrasenaRecuperada = true
        perfil.fechaRecuperacion = new Date()
        usuarioInstance.roles = null
        
        actionLoggingService.log("Usuario (actualizado): ${usuarioInstance?.toPlainText("\t")}")
        actionLoggingService.log("Perfil (actualizado): ${perfil?.toPlainText("\t")}")
                
        actionLoggingService.log("Guardando contraseña...")
        
        if (usuarioInstance.save(flush: true)) {
            if(perfil.save(flush: true)) {
                flash.message="Su contraseña se restablecio con éxito."
                
                actionLoggingService.log("${flash.message}")
                
                redirect action: "restablecimientoContrasena", params: [folio: params?.folio?.trim()]
                return 
            } else {
                flash.error = perfil.errors
                
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "restablecimientoContrasena", params: [folio: params?.folio?.trim()]
                return
            }
            
        } else {
            flash.error = usuarioInstance.errors
                
            actionLoggingService.log("Error: ${flash.error}")
                
            redirect action: "restablecimientoContrasena", params: [folio: params?.folio?.trim()]
            return
        }
               
    }
    
    def confirmacionEmailRequerida(){
        
    }
    
    @ActionLogging
    @ActionType("Perfil de usuario")
    @CustomActionName("Confirmación de correo electrónico")
    @PrintCustomLog
    @Transactional
    def confirmarEmail(){
        actionLoggingService.log("== Confirmación de correo electrónico - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a confirmar correo electrónico...")
        
        actionLoggingService.log("folio: ${params?.folio?.trim()}")  
        
        def perfil = Perfil.findByFolioConfirmacion(params?.folio?.trim())
        
        if (!perfil) {
            flash.error="La confirmacion de correo electronico mediante este enlace, no se encuentra disponible"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            return
        }
        
        def usuarioInstance = Usuario.findByPerfil(perfil)
        
        if (!usuarioInstance) {
            flash.error="La confirmacion de correo electronico mediante este enlace, no se encuentra disponible"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            return
        }
        
        actionLoggingService.log("Usuario (original): ${usuarioInstance?.toPlainText("\t")}")
        actionLoggingService.log("Perfil (original): ${perfil?.toPlainText("\t")}")
                        
        if (!perfil.confirmado) {
            actionLoggingService.log("Estableciendo información del usuario...")
            
            perfil.confirmado = true
            perfil.fechaConfirmacion = new Date()
            
            actionLoggingService.log("Usuario (actualizado): ${usuarioInstance?.toPlainText("\t")}")
            actionLoggingService.log("Perfil (actualizado): ${perfil?.toPlainText("\t")}")

            actionLoggingService.log("Guardando confirmación de correo electrónico...")

            if (perfil.save(flush: true)) {
                flash.message="La confirmacion de correo electronico se realizo con exito."
                
                actionLoggingService.log("${flash.message}")
                
                return 
            } else {
                flash.error="No se pudo realizar la confirmacion de correo electronico. Por favor intente nuevamente."
                
                actionLoggingService.log("Error: ${flash.error}")
                
                return
            }
        } else {
            flash.message="La confirmacion de correo electronico se realizo con exito."
            
            actionLoggingService.log("${flash.message}")
            
            return 
        }
               
    }
    
    @ActionLogging
    @ActionType("Perfil de usuario")
    @CustomActionName("Descarga de acuse de confirmación de cuenta")
    @PrintCustomLog
    def descargarAcuseConfirmacion(){
        actionLoggingService.log("== Descarga de acuse de confirmación de cuenta - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a confirmar correo electrónico...")
        
        actionLoggingService.log("folio: ${params?.folio?.trim()}")  
        
        def perfil = Perfil.findByFolioConfirmacion(params?.folio?.trim())
        
        if (!perfil) {
            flash.error="La confirmacion de correo electronico mediante este enlace, no se encuentra disponible"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "confirmarCuenta"
            return
        }
        
        def usuarioInstance = Usuario.findByPerfil(perfil)
        
        if (!usuarioInstance) {
            flash.error="La confirmación de correo electronico mediante este enlace, no se encuentra disponible"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "confirmarCuenta", params: [folio: params?.folio?.trim()]
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")
        actionLoggingService.log("Perfil: ${perfil?.toPlainText("\t")}")
        
        if (perfil.confirmado) {
            
            actionLoggingService.log("Descargando acuse...")
            
            def archivo = grailsResourceLocator.findResourceForURI('/files/CARTA_RESPONSIVA_IEBEM.pdf')
                        
            render file: archivo.inputStream, fileName: "CARTA_RESPONSIVA_IEBEM.pdf", contentType: 'application/pdf'
            return
        } else {
            flash.error="No se ha realizado la confirmación de cuenta"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "confirmarCuenta", params: [folio: params?.folio?.trim()]
            return 
        }
        
    }
    
    def entregaAcuseRequerida(){
        
    }
}
