package seguridad

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Rol implements Serializable {

	private static final long serialVersionUID = 1

        Integer id
	String authority

	Rol(String authority) {
		this()
		this.authority = authority
	}

	static constraints = {
		authority blank: false, unique: true
	}

	static mapping = {
            version false
            cache true
            table "seg_rol"
	}
        
    String toString(){
        return authority
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}authority: ${authority?:""}\n"
        
        return detalle
    }
}
