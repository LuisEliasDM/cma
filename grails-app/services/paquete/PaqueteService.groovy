package paquete

import estructuraXml.TituloElectronico

import grails.transaction.Transactional
import org.hibernate.criterion.CriteriaSpecification

@Transactional
class PaqueteService {
    def springSecurityService

    def verificarAutenticacionCompleta(Paquete paqueteInstance) {
        
        if (!paqueteInstance) {
            return
        }
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        if (!usuario) {
            return
        }
        
        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
        
        def titulosAutenticados = 0
        titulosCotejadosSinObservaciones?.each {
            if (it?.autenticacion) {
                titulosAutenticados ++
            }
        }
                
        if (titulosAutenticados == titulosCotejadosSinObservaciones?.size()) {
            paqueteInstance.estatus = EstatusPaquete.get(6)
            paqueteInstance.fechaAutenticacion = new Date()
            paqueteInstance.autenticador = usuario
        } else {
            paqueteInstance.estatus = EstatusPaquete.get(5)
            paqueteInstance.fechaAutenticacion = null
            paqueteInstance.autenticador = null
        }
        
        paqueteInstance.save(flush: true)
        
    }
    
    def verificarRegistroAnteDGPCompleto(Paquete paqueteInstance) {
        
        if (!paqueteInstance) {
            return
        }
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        if (!usuario) {
            return
        }
        
        def titulosCotejadosSinObservaciones = TituloElectronico.createCriteria().listDistinct {
            createAlias("observaciones", 'o', CriteriaSpecification.LEFT_JOIN)
            and {
                eq("paquete", paqueteInstance)
                or {
                    eq("o.estatus", false)
                    isNull("o.id")
                }
            }
        }
        
        def titulosRegistrados = TituloElectronico.createCriteria().listDistinct {
            and {
                eq("paquete", paqueteInstance)
                registros {
                    archivosResultado {
                        detalles {
                            and {
                                eq("estatus", 1)
                            }
                        }
                    }
                }
            }
        }
                
        if (titulosRegistrados?.size() == titulosCotejadosSinObservaciones?.size()) {
            paqueteInstance.estatus = EstatusPaquete.get(7)
            paqueteInstance.fechaRegistroDGP = new Date()
            paqueteInstance.autenticador = usuario
        } else {
            paqueteInstance.estatus = EstatusPaquete.get(6)
            paqueteInstance.fechaRegistroDGP = null
            paqueteInstance.autenticador = null
        }
        
        paqueteInstance.save(flush: true)
        
    }
}
