package seguridad

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import java.text.SimpleDateFormat
import revision.Observacion

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class Usuario implements Serializable {

	private static final long serialVersionUID = 1

	transient springSecurityService

        Integer id
	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
        Perfil perfil
        Date dateCreated
        Boolean requiereReingreso = false
        
        static hasMany = [
            instituciones: UsuarioInstitucion,
            roles: UsuarioRol,
            observaciones: Observacion
        ]

	Usuario(String username, String password) {
		this()
		this.username = username
		this.password = password
	}

	Set<Rol> getAuthorities() {
		UsuarioRol.findAllByUsuario(this)*.rol
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
                perfil nullable: true
                dateCreated nullable: true
                requiereReingreso nullable: true
	}

	static mapping = {
            version false
            password column: '`password`'
            table "seg_usuario"
            
            instituciones sort: "institucionEducativa"
	}
        
    String toString(){
        return username
    }
        
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}nombreUsuario: ${username?:""}\n"
        detalle += "${prefijo}habilitado: ${enabled? "Si":"No"}\n"
        detalle += "${prefijo}fechaRegistro: ${dateCreated? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dateCreated):""}\n"
        
        detalle += "${prefijo}roles:\n"
        roles?.each {
            detalle += "${prefijo}\trol: ${it?.toPlainText("\t\t\t")?:""}\n"
        }
        
        detalle += "${prefijo}institucionesEducativas:\n"
        instituciones?.each {
            detalle += "${prefijo}\tinstitucionEducativa: ${it?.toPlainText("\t\t\t")?:""}\n"
        }
        
        return detalle
    }
}
