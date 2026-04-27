package seguridad

class SeguridadFilters {
    def springSecurityService
    
    def filters = {
        seguridad(uri: "/**"){
            before = {
                if (!request?.forwardURI?.contains("assets/") && !(request?.forwardURI?.contains("confirmacionEmailRequerida") || request?.forwardURI?.contains("entregaAcuseRequerida") || request?.forwardURI?.contains("confirmarEmail") || request?.forwardURI?.contains("logout")  || request?.forwardURI?.contains("denied"))) {
                    def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null

                    if (usuario) {
                        if (usuario?.perfil) {
                            if (!usuario?.perfil?.confirmado) {
                                redirect(controller: "perfil", action: "confirmacionEmailRequerida")
                                return false
                            } else if (!usuario?.perfil?.acuseEntregado) {
                                redirect(controller: "perfil", action: "entregaAcuseRequerida")
                                return false
                            }
                        }
                        
                        if (!usuario.enabled) {
                            redirect(controller: "auth", action: "denied")
                            return false
                        }
                        
                        if (usuario.requiereReingreso) {
                            
                            def usuarioInstance = Usuario.get(usuario.id)
                            usuarioInstance.requiereReingreso = false
                            
                            usuarioInstance.save(flush: true)
                            
                            redirect(controller: "logout")
                            
                            return false
                        }
                        
                    }
                }
            }
        }
    }
}

