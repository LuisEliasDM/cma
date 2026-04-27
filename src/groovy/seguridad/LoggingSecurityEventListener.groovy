package seguridad
 
import javax.servlet.http.*
import org.apache.commons.logging.LogFactory
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.authentication.event.AuthenticationFailureExpiredEvent
import org.springframework.security.authentication.event.AuthenticationFailureCredentialsExpiredEvent
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import grails.util.Holders
 
class LoggingSecurityEventListener implements ApplicationListener<AbstractAuthenticationEvent>, LogoutHandler {
  
    void onApplicationEvent(AbstractAuthenticationEvent event) {
        def seguridadService = Holders.grailsApplication.mainContext.getBean('seguridadService')
        
        event.authentication.with {
            if (event instanceof AuthenticationSuccessEvent) {
                seguridadService.registrarAccesoCorrecto(principal.id, details.remoteAddress, details.sessionId,)
            } else if (event instanceof AuthenticationFailureBadCredentialsEvent) {
                seguridadService.registrarAccesoIncorrecto(principal, details.remoteAddress, details.sessionId, "credenciales incorrectas")
            } else if (event instanceof AuthenticationFailureLockedEvent) {
                seguridadService.registrarAccesoIncorrecto(principal, details.remoteAddress, details.sessionId, "cuenta bloqueada")
            } else if (event instanceof AuthenticationFailureExpiredEvent) {
                seguridadService.registrarAccesoIncorrecto(principal, details.remoteAddress, details.sessionId, "cuenta caducada")
            } else if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
                seguridadService.registrarAccesoIncorrecto(principal, details.remoteAddress, details.sessionId, "contraseña caducada")
            } else if (event instanceof AuthenticationFailureDisabledEvent) {
                seguridadService.registrarAccesoIncorrecto(principal, details.remoteAddress, details.sessionId, "cuenta deshabilitada")
            }
            
        }
    }
 
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        def seguridadService = Holders.grailsApplication.mainContext.getBean('seguridadService')
        
        authentication.with {
            seguridadService.registrarSalida(principal.id, details.remoteAddress, details.sessionId)
        }
    }
}