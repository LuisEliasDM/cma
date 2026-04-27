package seguridad



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.annotation.*

import catalogo.InstitucionEducativa

@SpringUserIdentification
@Transactional(readOnly = true)
class UsuarioController {
    def renapoService
    def seguridadService
    def springSecurityService
    def actionLoggingService
    
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        def referencia = params?.referencia?.trim()
        
        params.max = Math.min(max ?: 10, 100)
        params.sort = "username"
        params.order = "asc"
        
        if (params.cveInstitucion) {
            session["cveInstitucion"] = params.cveInstitucion != "0"? params.cveInstitucion: null   
        }
        
        def institucionesEducativas = InstitucionEducativa.list()
        
        def usuarios = []
        
        if (referencia) {
        
            usuarios = Usuario.createCriteria().list(params) {
                or {
                    ilike("username", "%${referencia}%")
                    perfil {
                        or {
                            ilike("curp", "%${referencia}%")
                            ilike("nombre", "%${referencia}%")
                            ilike("primerApellido", "%${referencia}%")
                            ilike("segundoApellido", "%${referencia}%")
                            ilike("nombreCompleto", "%${referencia}%")
                            ilike("correoElectronico", "%${referencia}%")
                            ilike("cargo", "%${referencia}%")
                            ilike("numeroIdentificacion", "%${referencia}%")
                        }
                    }
                }
                if (session["cveInstitucion"]) {
                    instituciones {
                        institucionEducativa {
                            and {
                                eq("clave", session["cveInstitucion"])
                            }
                        }
                    }
                }
            }
            
        } else {
            usuarios = Usuario.createCriteria().list(params) {
                if (session["cveInstitucion"]) {
                    instituciones {
                        institucionEducativa {
                            and {
                                eq("clave", session["cveInstitucion"])
                            }
                        }
                    }
                }
            }
        }
                
        respond usuarios, model:[usuarioInstanceCount: usuarios.totalCount, institucionesEducativas: institucionesEducativas], view: "index"
        
    }

    def show(Usuario usuarioInstance) {
        respond usuarioInstance
    }

    def create() {
        respond new Usuario(params)
    }

    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Registro de usuario")
    @PrintCustomLog
    @Transactional
    def save() {     
        actionLoggingService.log("== Registro de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando CURP existente...")
        
        def existeCurp = Perfil.findByCurp(params?.curp?.trim())
        
        if (existeCurp) {
            flash.error="El usuario con CURP '${params?.curp?.trim()}' ya se encuentra registrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "create"
            return
        }
        
        actionLoggingService.log("Consultando correo electrónico existente...")
        
        def existeEmail = Perfil.findByCorreoElectronico(params?.correoElectronico?.trim())
        
        if (existeEmail) {
            flash.error="El correo electronico '${params?.correoElectronico?.trim()}' ya se encuentra registrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "create"
            return
        }
        
        actionLoggingService.log("Validando CURP...")
        
        def datosCurp = renapoService.consultarDatosCurp(params?.curp)
        
        if (!datosCurp) {
            flash.error="No se pudo realizar la verificacion de la CURP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "create"
            return
        }
        
        if (!datosCurp?.curp) {
            flash.error="CURP invalida: ${datosCurp?.mensaje}"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "create"
            return
        }
        
        actionLoggingService.log("Datos del RENAPO: ${datosCurp?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del usuario...")
        
        def rol = Rol.get(params.int("idRol"))
        def institucionEducativa = InstitucionEducativa.get(params.int("idInstitucionEducativa"))
        
        if (!rol) {
            flash.error="Es necesario especificar el rol del usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "create"
            return
        } 
        
        def idConsecutivo  = seguridadService.generaIdentificadorConsecutivo()
        def usuarioInstance = new Usuario()
        def usuarioRol = new UsuarioRol(usuario: usuarioInstance, rol: rol)
        
        if (rol.id in [6,7] && !institucionEducativa) {
            flash.error="Es necesario seleccionar la institucion educativa"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "create"
            return
        }
                
        def perfilInstance = new Perfil()
        
        perfilInstance.curp = datosCurp?.curp
        perfilInstance.nombre = datosCurp?.nombre
        perfilInstance.primerApellido = datosCurp.apellidoPaterno
        perfilInstance.segundoApellido = datosCurp.apellidoMaterno
        perfilInstance.sexo = datosCurp?.sexo
        perfilInstance.fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(datosCurp?.fechaNacimiento)
        perfilInstance.estadoNacimiento = datosCurp?.claveEntidadNacimiento
        
        perfilInstance.correoElectronico = params?.correoElectronico?.trim()
        perfilInstance.telefonoFijo = params?.telefonoFijo?.trim()
        perfilInstance.telefonoMovil = params?.telefonoMovil?.trim()
        perfilInstance.cargo = params?.cargo?.trim()
        
        perfilInstance.numeroIdentificacion = seguridadService.generaIdentificadorEstructurado(usuarioInstance, institucionEducativa, idConsecutivo)
        perfilInstance.folioConfirmacion = seguridadService.generaFolio()
                        
        usuarioInstance.username = seguridadService.generaNombreUsuario(perfilInstance.nombre, perfilInstance.primerApellido)
        usuarioInstance.password = seguridadService.generaContrasena()
        usuarioInstance.enabled = true
        
        if (usuarioInstance.hasErrors()) {
            respond usuarioInstance.errors, view:'create'
            
            actionLoggingService.log("Error: ${usuarioInstance.errors}")
            
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Perfil: ${perfilInstance?.toPlainText("\t")}")
                
        actionLoggingService.log("Guardando usuario...")
        
        if(usuarioInstance.save(flush:true)) {
            
            UsuarioRol.create(usuarioInstance, rol, true)
            
            if (rol.id in [6,7]) {
                UsuarioInstitucion.create(usuarioInstance, institucionEducativa, true)
            }
            
            if (perfilInstance.save(flush:true)) {
                usuarioInstance.perfil = perfilInstance
            } else {
                actionLoggingService.log("Error: ${perfilInstance.errors}")
            }
            
            flash.message="Usuario registrado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            actionLoggingService.log("Enviando correo electrónico de confirmación...")
            
            seguridadService.enviarConfirmacionCuenta(usuarioInstance, perfilInstance)            
            
            redirect action: "show", id: usuarioInstance?.id
            return
        } else {
            flash.error= usuarioInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond usuarioInstance.errors, view:'create'
            return
        }
        
        usuarioInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
                redirect usuarioInstance
            }
            '*' { respond usuarioInstance, [status: CREATED] }
        }
    }

    def edit(Usuario usuarioInstance) {
        respond usuarioInstance
    }

    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Modificación de usuario")
    @PrintCustomLog
    @Transactional
    def update() {
        actionLoggingService.log("== Modificación de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a modificar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        def usuarioInstance = Usuario.get(params.int("id"))
        
        if (!usuarioInstance) {
            flash.error="Es necesario seleccionar el usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "index"
            return
        }
        
        actionLoggingService.log("Usuario (original): ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Perfil (original): ${usuarioInstance?.perfil?.toPlainText("\t")}")
        
        if (usuarioInstance?.perfil?.curp != params?.curp?.trim()) {
            
            actionLoggingService.log("Consultando CURP existente...")
            
            def existeCurp = Perfil.findByCurp(params?.curp?.trim())

            if (existeCurp) {
                flash.error="El usuario con CURP '${params?.curp?.trim()}' ya se encuentra registrado"
                
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "edit", id: usuarioInstance?.id
                return
            }
        }
        
        def solicitarConfirmacionEmail = false
        if (usuarioInstance?.perfil?.correoElectronico != params?.correoElectronico?.trim()) { 
            
            actionLoggingService.log("Consultando correo electrónico existente...")
            
            def existeEmail = Perfil.findByCorreoElectronico(params?.correoElectronico?.trim())

            if (existeEmail) {
                flash.error="El correo electronico '${params?.correoElectronico?.trim()}' ya se encuentra registrado"
                
                actionLoggingService.log("Error: ${flash.error}")
                
                redirect action: "edit", id: usuarioInstance?.id
                return
            } else {
                solicitarConfirmacionEmail = true
            }
        }
        
        actionLoggingService.log("Validando CURP...")
        
        def datosCurp = renapoService.consultarDatosCurp(params?.curp)
        
        if (!datosCurp) {
            flash.error="No se pudo realizar la verificacion de la CURP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "edit", id: usuarioInstance?.id
            return
        }
                
        if (!datosCurp?.curp) {
            flash.error="CURP invalida: ${datosCurp?.mensaje}"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "edit", id: usuarioInstance?.id
            return
        }
        
        actionLoggingService.log("Datos del RENAPO: ${datosCurp?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del usuario...")
        
        def perfilInstance
        def numeroIdentificacion
        if (!usuarioInstance?.perfil) {
            def idConsecutivo  = seguridadService.generaIdentificadorConsecutivo()
            perfilInstance = new Perfil()
            numeroIdentificacion = seguridadService.generaIdentificadorEstructurado(usuarioInstance, null, idConsecutivo)
        } else {
            perfilInstance = usuarioInstance?.perfil
            numeroIdentificacion = usuarioInstance?.perfil?.numeroIdentificacion
        }
        
        
        perfilInstance?.curp = datosCurp?.curp
        perfilInstance?.nombre = datosCurp?.nombre
        perfilInstance?.primerApellido = datosCurp.apellidoPaterno
        perfilInstance?.segundoApellido = datosCurp.apellidoMaterno
        perfilInstance?.sexo = datosCurp?.sexo
        perfilInstance?.fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(datosCurp?.fechaNacimiento)
        perfilInstance?.estadoNacimiento = datosCurp?.claveEntidadNacimiento
        
        perfilInstance?.correoElectronico = params?.correoElectronico?.trim()
        perfilInstance?.telefonoFijo = params?.telefonoFijo?.trim()
        perfilInstance?.telefonoMovil = params?.telefonoMovil?.trim()
        perfilInstance?.cargo = params?.cargo?.trim()
        
        perfilInstance?.numeroIdentificacion = numeroIdentificacion
        
        if (solicitarConfirmacionEmail) {
            perfilInstance?.confirmado = false
            perfilInstance?.fechaConfirmacion = null
            perfilInstance?.folioConfirmacion = seguridadService.generaFolio()
        }
                        
        if (perfilInstance.hasErrors()) {
            redirect action: "edit", id: usuarioInstance?.id
            
            actionLoggingService.log("Error: ${usuarioInstance.errors}")
            
            return
        }
        
        actionLoggingService.log("Usuario (actualizado): ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Perfil (actualizado): ${usuarioInstance?.perfil?.toPlainText("\t")}")
                
        actionLoggingService.log("Guardando usuario...")
        
        usuarioInstance?.perfil = perfilInstance
        
        if(perfilInstance.save(flush:true)) {
            flash.message="Usuario actualizado correctamente"
            
            actionLoggingService.log("${flash.message}")
                        
            if (solicitarConfirmacionEmail) {
                
                actionLoggingService.log("Enviando correo electrónico de confirmación...")
                
                seguridadService.enviarConfirmacionEmail( usuarioInstance?.perfil.correoElectronico,  usuarioInstance?.perfil.folioConfirmacion)
            }
            
            redirect action: "show", id: usuarioInstance?.id
            return
        } else {
            flash.error= usuarioInstance.perfil.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "edit", id: usuarioInstance?.id
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Usuario.label', default: 'Usuario'), usuarioInstance.id])
                redirect usuarioInstance
            }
            '*'{ respond usuarioInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Usuario usuarioInstance) {        
        
        if (usuarioInstance == null) {
            flash.error="Es necesario indicar el usuario a eliminar"
            redirect action:"index"
            return
        }

        usuarioInstance.delete flush:true
        
        flash.message="Usuario eliminado correctamente"
            
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Usuario.label', default: 'Usuario'), usuarioInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }
    
    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Habilitación de usuario")
    @PrintCustomLog
    @Transactional
    def habilitar(Usuario usuarioInstance) {
        actionLoggingService.log("== Habilitación de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a habilitar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (usuarioInstance == null) {
            flash.error="Es necesario indicar el usuario a habilitar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")

        actionLoggingService.log("Habilitando usuario...")

        usuarioInstance.enabled = true
        usuarioInstance.roles = null
        
        if (usuarioInstance.save(flush: true)) {
            flash.message = "Usuario habilitado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
        } else {
            flash.error= usuarioInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
        }
                
        redirect action:"index", method:"GET"
    }
    
    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Inhabilitación de usuario")
    @PrintCustomLog
    @Transactional
    def inhabilitar(Usuario usuarioInstance) {
        actionLoggingService.log("== Inhabilitación de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a inhabilitar...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (usuarioInstance == null) {
            flash.error="Es necesario indicar el usuario a inhabilitar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")

        actionLoggingService.log("Inhabilitando usuario...")

        usuarioInstance.enabled = false
        usuarioInstance.roles = null
        
        if (usuarioInstance.save(flush: true)) {
            flash.message = "Usuario inhabilitado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
        } else {
            flash.error= usuarioInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
        }
        
        redirect action:"index", method:"GET"
    }
    
    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Eliminación de rol de usuario")
    @PrintCustomLog
    @Transactional
    def eliminarRol(Usuario usuarioInstance, Rol rolInstance) {
        actionLoggingService.log("== Eliminación de rol de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a eliminar rol...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (usuarioInstance == null) {
            flash.error="Es necesario indicar el usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando rol a eliminar...")
        
        actionLoggingService.log("Id: ${params.int("rolInstance.id")}")
        
        if (rolInstance == null) {
            flash.error="Es necesario indicar el rol a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Rol: ${rolInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando rol de usuario...")
        
        def usuarioRol = UsuarioRol.get(usuarioInstance?.id, rolInstance?.id)
        
        if (!usuarioRol) {
            flash.error="Rol de usuario no encontrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
        }
        
        actionLoggingService.log("Rol de usuario: ${usuarioRol?.toPlainText("\t")}")
       
        actionLoggingService.log("Eliminando rol de usuario...")

        usuarioRol.remove(usuarioInstance, rolInstance, true)
        
        usuarioInstance.requiereReingreso = true
        
        if (usuarioInstance.save(flush: true)) {
            flash.message="Rol de usuario eliminado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
        } else {
            flash.error= usuarioInstance.errors
            
            actionLoggingService.log("${flash.error}")
        }
        
        redirect action:"show", id:usuarioInstance.id
        
    }
    
    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Eliminación de institución de usuario")
    @PrintCustomLog
    @Transactional
    def eliminarInstitucion(Usuario usuarioInstance, InstitucionEducativa institucionEducativaInstance) {
        actionLoggingService.log("== Eliminación de institución de usuario - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a eliminar institución...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (usuarioInstance == null) {
            flash.error="Es necesario indicar el usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando institución a eliminar...")
        
        actionLoggingService.log("Id: ${params.int("institucionEducativaInstance.id")}")
        
        if (institucionEducativaInstance == null) {
            flash.error="Es necesario indicar la institución a eliminar"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Institucion: ${institucionEducativaInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando institución de usuario...")
        
        def usuarioInstitucion = UsuarioInstitucion.findByUsuarioAndInstitucionEducativa(usuarioInstance, institucionEducativaInstance)
        
        if (!usuarioInstitucion) {
            flash.error="Institución de usuario no encontrada"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
        }
        
        actionLoggingService.log("Institución de usuario: ${usuarioInstitucion?.toPlainText("\t")}")
       
        actionLoggingService.log("Eliminando institución de usuario...")
        
        usuarioInstitucion.remove(usuarioInstance, institucionEducativaInstance, true)

        flash.message="Institución de usuario eliminada correctamente"
            
        actionLoggingService.log("${flash.message}")
        
        redirect action:"show", id:usuarioInstance.id, method:"GET"
    }
    
    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Reenvío de confirmación de cuenta / correo electrónico")
    @PrintCustomLog
    @Transactional
    def reenviarConfirmacion(Usuario usuarioInstance){
        actionLoggingService.log("== Reenvío de confirmación de cuenta / correo electrónico - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a reenviar confirmación...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (usuarioInstance == null) {
            flash.error="Es necesario indicar el usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Usuario (original): ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Perfil (original): ${usuarioInstance?.perfil?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información de la confirmación...")
        
        def perfilInstance = usuarioInstance?.perfil
        perfilInstance?.folioConfirmacion = seguridadService.generaFolio()
        perfilInstance?.confirmado = false
        
        actionLoggingService.log("Usuario (actualizado): ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Perfil (actualizado): ${perfilInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando confirmación...")
        
        if (perfilInstance.save(flush: true)) {
            
            actionLoggingService.log("Enviando correo electrónico de confirmación...")
            
            if (!perfilInstance?.acuseEntregado) {
                seguridadService.enviarConfirmacionCuenta(usuarioInstance, perfilInstance)
            } else {
                seguridadService.enviarConfirmacionEmail(perfilInstance?.correoElectronico, perfilInstance?.folioConfirmacion)
            }
            
            flash.message="Confirmacion reenviada correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            redirect action: "show", id: usuarioInstance?.id
            return
        } else {
            flash.error="No se pudo reenviar la confirmacion. Por favor intente nevamente"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "show", id: usuarioInstance?.id
            return
        }
            
        
    }
    
    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Registro de acuse de confirmación de cuenta / correo electrónico")
    @PrintCustomLog
    @Transactional
    def registrarEntregaAcuse(Usuario usuarioInstance){
        actionLoggingService.log("== Registro de acuse de confirmación de cuenta / correo electrónico - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario actual...")
        
        def usuario = springSecurityService.isLoggedIn() ? springSecurityService.loadCurrentUser() : null
        
        actionLoggingService.log("Usuario: ${usuario?.toPlainText("\t")}")
        
        actionLoggingService.log("Consultando usuario a registrar acuse...")
        
        actionLoggingService.log("Id: ${params?.id}")
        
        if (usuarioInstance == null) {
            flash.error="Es necesario indicar el usuario"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action:"index"
            return
        }
        
        actionLoggingService.log("Usuario (original): ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Perfil (original): ${usuarioInstance?.perfil?.toPlainText("\t")}")
        
        if (!usuarioInstance?.perfil?.confirmado) {
            flash.error="El usuario no ha realizado la confirmacion de su cuenta"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "show", id: usuarioInstance?.id
            return
        }
        
        if (usuarioInstance?.perfil?.acuseEntregado) {
            flash.error="El registro de acuse ya se encuentra realizado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "show", id: usuarioInstance?.id
            return
        }
        
        actionLoggingService.log("Estableciendo información del registro de acuse...")
        
        usuarioInstance?.perfil?.acuseEntregado = true
        usuarioInstance?.perfil?.fechaEntregaAcuse = new Date()
        usuarioInstance?.enabled = true
        usuarioInstance?.roles = null
                
        actionLoggingService.log("Usuario (actualizado): ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Perfil (actualizado): ${usuarioInstance?.perfil?.toPlainText("\t")}")
        
        actionLoggingService.log("Guardando registro de acuse...")
        
        if (usuarioInstance.save(flush:true)) {
            flash.message="Acuse registrado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            actionLoggingService.log("Enviando correo electrónico de aviso de acceso...")
            
            seguridadService.enviarAvisoAcceso(usuarioInstance, usuarioInstance?.perfil?.correoElectronico)
            
            redirect action: "show", id: usuarioInstance?.id
            return
        } else {
            flash.error = usuarioInstance.errors
            
            actionLoggingService.log("Error: ${flash.error}")
            
            redirect action: "show", id: usuarioInstance?.id
            return
        }
        
    }
    
    def registroAdministrador() {
        def administrador = Usuario.createCriteria().list {
            roles {
                rol {
                    eq("authority", "ROLE_ADMINISTRADOR")
                }
            }
            eq("enabled", true)
        }
                
        if (administrador) {
            redirect controller: "auth", action: "login"
            return
        }
        
        respond new Usuario()
    }
    
    @ActionLogging
    @ActionType("Gestión de usuarios")
    @CustomActionName("Registro de usuario administrador")
    @PrintCustomLog
    @Transactional
    def registrarAdministrador() {
        actionLoggingService.log("== Registro de usuario administrador - ${new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())} ==")
        
        actionLoggingService.log("Consultando usuario administrador existente...")
        
        def administrador = Usuario.createCriteria().list {
            roles {
                rol {
                    eq("authority", "ROLE_ADMINISTRADOR")
                }
            }
            eq("enabled", true)
        }
                
        if (administrador) {
            
            actionLoggingService.log("Usuario: ${administrador?.toPlainText("\t")}")
            
            flash.error="El usuario administrador ya se encuentra registrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "registroAdministrador"
            return
        }
        
        actionLoggingService.log("Consultando CURP existente...")
                
        def existeCurp = Perfil.findByCurp(params?.curp?.trim())
        
        if (existeCurp) {
            flash.error="El usuario con CURP '${params?.curp?.trim()}' ya se encuentra registrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "registroAdministrador"
            return
        }
        
        actionLoggingService.log("Consultando correo electrónico existente...")
        
        def existeEmail = Perfil.findByCorreoElectronico(params?.correoElectronico?.trim())
        
        if (existeEmail) {
            flash.error="El correo electronico '${params?.correoElectronico?.trim()}' ya se encuentra registrado"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "registroAdministrador"
            return
        }
        
        actionLoggingService.log("Validando CURP...")
        
        def datosCurp = renapoService.consultarDatosCurp(params?.curp)
        
        if (!datosCurp) {
            flash.error="No se pudo realizar la verificacion de la CURP"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "registroAdministrador"
            return
        }
        
        if (!datosCurp?.curp) {
            flash.error="CURP invalida: ${datosCurp?.mensaje}"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond new Usuario(params), view: "registroAdministrador"
            return
        }
        
        actionLoggingService.log("Datos del RENAPO: ${datosCurp?.toPlainText("\t")}")
        
        actionLoggingService.log("Estableciendo información del usuario administrador...")
        
        def rol = Rol.get(1)
        
        def idConsecutivo  = seguridadService.generaIdentificadorConsecutivo()
        def usuarioInstance = new Usuario()

        def perfilInstance = new Perfil()
        
        perfilInstance.curp = datosCurp?.curp
        perfilInstance.nombre = datosCurp?.nombre
        perfilInstance.primerApellido = datosCurp.apellidoPaterno
        perfilInstance.segundoApellido = datosCurp.apellidoMaterno
        perfilInstance.sexo = datosCurp?.sexo
        perfilInstance.fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(datosCurp?.fechaNacimiento)
        perfilInstance.estadoNacimiento = datosCurp?.claveEntidadNacimiento
        
        perfilInstance.correoElectronico = params?.correoElectronico?.trim()
        perfilInstance.telefonoFijo = params?.telefonoFijo?.trim()
        perfilInstance.telefonoMovil = params?.telefonoMovil?.trim()
        perfilInstance.cargo = params?.cargo?.trim()
        
        perfilInstance.numeroIdentificacion = seguridadService.generaIdentificadorEstructurado(usuarioInstance, null, idConsecutivo)
        perfilInstance.folioConfirmacion = seguridadService.generaFolio()
        perfilInstance.acuseEntregado = true
                        
        usuarioInstance.username = seguridadService.generaNombreUsuario(perfilInstance.nombre, perfilInstance.primerApellido)
        usuarioInstance.password = seguridadService.generaContrasena()
        usuarioInstance.enabled = true
        
        if (usuarioInstance.hasErrors()) {
            respond usuarioInstance.errors, view:'registroAdministrador'
            
            actionLoggingService.log("Error: ${usuarioInstance.errors}")
            
            return
        }
        
        actionLoggingService.log("Usuario: ${usuarioInstance?.toPlainText("\t")}")
        
        actionLoggingService.log("Perfil: ${perfilInstance?.toPlainText("\t")}")
                
        actionLoggingService.log("Guardando usuario...")
                
        if(usuarioInstance.save(flush:true)) {
            
            UsuarioRol.create(usuarioInstance, rol, true)
            
            if (perfilInstance.save(flush:true)) {
                usuarioInstance.perfil = perfilInstance
            } else {
                println perfilInstance.errors
            }
            
            flash.message="Usuario administrador registrado correctamente"
            
            actionLoggingService.log("${flash.message}")
            
            actionLoggingService.log("Enviando correo electrónico de confirmación...")
            
            seguridadService.enviarConfirmacionCuenta(usuarioInstance, perfilInstance)
            
            
        } else {
            flash.error="No se pudo registrar al usuario administrador. Por favor intente nuevamente"
            
            actionLoggingService.log("Error: ${flash.error}")
            
            respond usuarioInstance.errors, view:'create'
            return
        }
        
        usuarioInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'usuario.label', default: 'Usuario'), usuarioInstance.id])
                redirect usuarioInstance
            }
            '*' { respond usuarioInstance, [status: CREATED] }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'usuario.label', default: 'Usuario'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
