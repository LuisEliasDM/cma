package seguridad

import catalogo.InstitucionEducativa

class UsuarioInstitucion {
    Usuario usuario
    InstitucionEducativa institucionEducativa
    Date dateCreated
    
    static UsuarioInstitucion create(Usuario usuario, InstitucionEducativa institucionEducativa, boolean flush = false) {
        def instance = new UsuarioInstitucion(usuario: usuario, institucionEducativa: institucionEducativa)
        instance.save(flush: flush, insert: true)
        instance
    }
    
    static boolean remove(Usuario u, InstitucionEducativa i, boolean flush = false) {
            if (u == null || i == null) return false

            int rowCount = UsuarioInstitucion.where { usuario == u && institucionEducativa == i }.deleteAll()

            if (flush) { UsuarioInstitucion.withSession { it.flush() } }

            rowCount
    }

    static constraints = {
        usuario nullable: false
        institucionEducativa nullable: false
        dateCreated nullable: true
    }
    
    static mapping = {
        version false
        
        table "seg_usuario_institucion"
        dateCreated column: "fecha_registro", sqlType: "TIMESTAMP", defaultValue: "CURRENT_TIMESTAMP"
    }
    
    String toPlainText(){
        return toPlainText("")
    }
    
    String toPlainText(String prefijo){
        def detalle = "\n"
        
        detalle += "${prefijo}clave: ${institucionEducativa?.clave}\n"
        detalle += "${prefijo}descripcion: ${institucionEducativa?.descripcion}\n"
        
        return detalle
    }
}
