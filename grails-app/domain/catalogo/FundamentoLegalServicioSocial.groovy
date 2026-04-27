package catalogo

class FundamentoLegalServicioSocial {
    String descripcion
    Boolean permiteNoEfectuarSs

    static constraints = {
        descripcion(nullable: false, size: 1..256)
        permiteNoEfectuarSs(nullable:false)
    }
    
    static mapping = {
        version false
        
        table "cat_fundamento_legal_servicio_social"
    }
}
