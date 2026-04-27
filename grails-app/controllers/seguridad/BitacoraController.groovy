package seguridad

import java.text.SimpleDateFormat
import org.mirzsoft.grails.actionlogging.ActionLoggingEvent

class BitacoraController {
    def sessionFactory

    def index(Integer max) {
        
        def referencia = params?.referencia?.trim()
        
        params.max = Math.min(max ?: 10, 100)
        params.sort = "id"
        params.order = "desc"
        
        def actionLoggingEventInstanceList = []
        def count = 0
        
        def fecha
        try {
            fecha = new SimpleDateFormat("dd/MM/yyyy").parse(referencia)
        } catch (Exception ex) {}

        if (referencia) {
            
            def session = sessionFactory.currentSession
            
            def querySelect = ""
            def queryCount = "SELECT COUNT(*) FROM ("
            
            def query = "SELECT b.* "
            query += "FROM seg_bitacora b "
            query += "LEFT JOIN seg_usuario u ON b.user_id = u.id "    
            query += "LEFT JOIN seg_perfil p ON p.id = u.perfil_id "
            query += "LEFT JOIN seg_usuario_rol ur ON u.id = ur.usuario_id "
            query += "LEFT JOIN seg_rol r ON ur.rol_id = r.id "
            query += "WHERE "
                if (fecha) {
                    query += "DATE(b.date) = :fecha or "
                }
                query += "b.controller_name like :referencia or "
                query += "b.action_name like :referencia or "
                query += "b.custom_action_name like :referencia or "
                query += "b.status like :referencia or "
                query += "u.username like :referencia or "
                query += "p.nombre like :referencia or "
                query += "p.primer_apellido like :referencia or "
                query += "p.segundo_apellido like :referencia or "
                query += "r.authority like :referencia or "
                query += "CONCAT(p.nombre, ' ', p.primer_apellido, ' ', p.segundo_apellido) like :referencia "
            query += "GROUP BY b.id "
            
            queryCount += query
            
            query += "ORDER BY b.id DESC "
            query += "LIMIT ${params.offset?:"0"},${params.max}"
            
            querySelect += query
            
            queryCount += ") AS A "
                                    
            def sqlQuerySelect = session.createSQLQuery(querySelect)
                        
            actionLoggingEventInstanceList = sqlQuerySelect.with {
                addEntity(ActionLoggingEvent)
                setString("referencia", "%${referencia}%")
                if (fecha) {
                    setDate("fecha", fecha)
                }
                list()
            }
            
            def sqlQueryCount = session.createSQLQuery(queryCount)
            count = sqlQueryCount.with {
                setString("referencia", "%${referencia}%")
                if (fecha) {
                    setDate("fecha", fecha)
                }
                uniqueResult()
            }
                        
        } else {
            actionLoggingEventInstanceList = ActionLoggingEvent.list(params)
            count = ActionLoggingEvent.count()
        }
        
        [
            actionLoggingEventInstanceList: actionLoggingEventInstanceList,
            actionLoggingEventInstanceCount: count
        ]
        
    }

    def detalle(ActionLoggingEvent actionLoggingEventInstance){
        [actionLoggingEventInstance: actionLoggingEventInstance]
    }
}
