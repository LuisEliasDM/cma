package estructuraXml

import java.security.Principal
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder
import seguridad.Usuario

import org.apache.commons.ssl.PKCS8Key;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import grails.plugin.springsecurity.SpringSecurityUtils

class FirmaElectronicaService {
    def springSecurityService

    def firmar(byte[] clavePrivada, String contrasena, String cadenaOriginal){
        String resultado;
        try {
            PKCS8Key pkcs8Key = new PKCS8Key(clavePrivada, contrasena?.toCharArray());
            PrivateKey privateKey = pkcs8Key.getPrivateKey();
            
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(cadenaOriginal.getBytes("UTF-8"));
            
            resultado = Base64.encodeBase64String(signature.sign());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return resultado;
    }
    
    def numeroSerieCertificado(InputStream certificadoCer){
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
	Certificate cert = (X509Certificate) cf.generateCertificates(certificadoCer).iterator().next();
        return new String(cert.getSerialNumber().toByteArray());
    }
    
    def toByteArray(File archivo){

        FileInputStream fis = new FileInputStream(archivo);

        byte[] fbytes = new byte[(int) archivo.length()];

        fis.read(fbytes);
        fis.close();

        return fbytes;
    }
    
    def bytesToBase64(byte[] fbytes){
        return Base64.encodeBase64String(fbytes)
    }
    
    def byteArrayToFile(String path, byte[] fbytes){
        def file = new File(path)
        FileUtils.writeByteArrayToFile(file, fbytes)
        return file
    }
    
    def base64toBytes(String base64){
        return Base64.decodeBase64(base64)
    }
    
    def generarCadenaOriginalTitulo(TituloElectronico tituloElectronicoInstance, FirmaResponsable firmaResponsableInstance){
        def cadenaOriginal = "||"
        
        cadenaOriginal += (tituloElectronicoInstance?.version?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.folioControl?:"").trim() + "|"
        cadenaOriginal += (firmaResponsableInstance?.curp?:"").trim() + "|"
        cadenaOriginal += (firmaResponsableInstance?.idCargo?:"") + "|"
        cadenaOriginal += (firmaResponsableInstance?.cargo?:"").trim() + "|"
        cadenaOriginal += (firmaResponsableInstance?.abrTitulo?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.institucion?.cveInstitucion?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.institucion?.nombreInstitucion?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.carrera?.cveCarrera?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.carrera?.nombreCarrera?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.carrera?.fechaInicio?.format("yyyy-MM-dd")?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.carrera?.fechaTerminacion?.format("yyyy-MM-dd")?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.carrera?.idAutorizacionReconocimiento?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.carrera?.autorizacionReconocimiento?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.carrera?.numeroRvoe?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.profesionista?.curp?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.profesionista?.nombre?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.profesionista?.primerApellido?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.profesionista?.segundoApellido?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.profesionista?.correoElectronico?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.fechaExpedicion?.format("yyyy-MM-dd")?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.idModalidadTitulacion?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.modalidadTitulacion?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.fechaExamenProfesional?.format("yyyy-MM-dd")?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.fechaExencionExamenProfesional?.format("yyyy-MM-dd")?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.cumplioServicioSocial?"1":"0") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.idFundamentoLegalServicioSocial?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.fundamentoLegalServicioSocial?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.idEntidadFederativa?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.expedicion?.entidadFederativa?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.antecedente?.institucionProcedencia?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.antecedente?.idTipoEstudioAntecedente?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.antecedente?.tipoEstudioAntecedente?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.antecedente?.idEntidadFederativa?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.antecedente?.entidadFederativa?:"").trim() + "|"
        cadenaOriginal += (tituloElectronicoInstance?.antecedente?.fechaInicio?.format("yyyy-MM-dd")?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.antecedente?.fechaTerminacion?.format("yyyy-MM-dd")?:"") + "|"
        cadenaOriginal += (tituloElectronicoInstance?.antecedente?.noCedula?:"").trim() + "||"
        
        cadenaOriginal = cadenaOriginal.replaceAll(/\&/,/&amp;/)
        cadenaOriginal = cadenaOriginal.replaceAll(/\"/,/&quot;/)
        cadenaOriginal = cadenaOriginal.replaceAll(/\</,/&lt;/)
        cadenaOriginal = cadenaOriginal.replaceAll(/\>/,/&gt;/)
        
        return cadenaOriginal
    }
    
    def generarCadenaOriginalAutenticacion(Autenticacion autenticacionInstance){
        def cadenaOriginal = "||"
        cadenaOriginal += (autenticacionInstance?.version?:"").trim() + "|"
        cadenaOriginal += (autenticacionInstance?.folioDigital?:"").trim() + "|"
        cadenaOriginal += (autenticacionInstance?.fechaAutenticacion?.format("yyyy-MM-dd'T'HH:mm:ss")?:"").trim() + "|"
        cadenaOriginal += (autenticacionInstance?.selloTitulo?:"").trim()
        cadenaOriginal += (autenticacionInstance?.noCertificadoAutoridad?:"").trim() + "||"
    }
    
    def generarUuid() {
        def uuid = UUID.randomUUID()
        return uuid.toString()?.toUpperCase()
    }
    
    def extraerDatosCertificado(InputStream certificadoCer) {
        def datos = [:]
        
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificates(certificadoCer).iterator().next();

            
            datos.contenido = cert
            datos.validoDesde = cert.getNotBefore()
            datos.validoHasta = cert.getNotAfter()
            datos.numeroSerie = new String(cert.getSerialNumber().toByteArray())
                        
            datos.nombre = extraerValorAtributo(cert.getSubjectDN(), "CN")
            datos.curp = extraerValorAtributo(cert.getSubjectDN(), "SERIALNUMBER")
            datos.rfc = extraerValorAtributo(cert.getSubjectDN(), "OID.2.5.4.45")
            datos.correoElectronico = extraerValorAtributo(cert.getSubjectDN(), "EMAILADDRESS")
        
        } catch (Exception ex) {
            datos = null
            ex.printStackTrace()
        }
        
        return datos
    }
    
    def extraerValorAtributo(Principal principal, String nombreAtributo){
        int start = principal.getName().indexOf(nombreAtributo);
        String tmpName, name = "";
        if (start != -1) {
            tmpName = principal.getName().substring(start + nombreAtributo.length() +1);
            int end = tmpName.indexOf(",");
            if (end > 0) {
                name = tmpName.substring(0, end);
            } else {
                name = tmpName;
            }
        }
        return name;
    }
   
    def consultarFirmas(String claveInstitucion, Usuario usuario){
        def firmasElectronicas = []
                
        def fechaLimite

        use (groovy.time.TimeCategory) {
            fechaLimite = new Date() + 1.month
        }

        firmasElectronicas = FirmaElectronica.createCriteria().list {
            and {
                if (claveInstitucion) {
                    institucionEducativa {
                        eq("clave", claveInstitucion)
                    }
                } else {
                    isNull("institucionEducativa")
                }
                eq("activo", true)
                //gt("validoHastaCer", fechaLimite)
                if (usuario) {
                    eq("curp", usuario?.perfil?.curp)
                }
            }
        }
                
        if (usuario) {
            firmasElectronicas?.removeAll {
                it.curp == usuario?.perfil?.curp && !usuario.enabled
            }
        }
        
        return firmasElectronicas
    }
    
    def consultarFirmasNecesarias(String claveInstitucion){
        def firmasElectronicas = []

        firmasElectronicas = FirmaElectronica.createCriteria().list {
            and {
                if (claveInstitucion) {
                    institucionEducativa {
                        eq("clave", claveInstitucion)
                    }
                } else {
                    isNull("institucionEducativa")
                }
                eq("activo", true)
            }
        }

        firmasElectronicas?.removeAll { fiel ->
            def usuarioFirma = Usuario.createCriteria().list {
                perfil {
                    eq("curp", fiel.curp)
                }
                eq("enabled", true)
            }
            
            !usuarioFirma
        }
        
        return firmasElectronicas? firmasElectronicas?.size() : 1
    }
    
    def consultarEstatusFirmas(String claveInstitucion){
        def fechaLimite
        def fechaAvisoInicio
        def fechaAvisoFin
        
        use (groovy.time.TimeCategory) {
            fechaLimite = new Date() + 1.month
            fechaAvisoInicio = new Date() + 1.month
            fechaAvisoFin = new Date() + 3.month
        }
        
        def firmasCaducadas = FirmaElectronica.createCriteria().list {
            projections {
                count()
            }
            and {
                if (claveInstitucion) {
                    institucionEducativa {
                        and {
                            eq("clave", claveInstitucion)
                        }
                    }
                } else {
                    isNull("institucionEducativa")
                }
                eq("activo", true)
                lt("validoHastaCer", fechaLimite)
            }
        }
        
        def firmasProximasCaducar = FirmaElectronica.createCriteria().list {
            projections {
                count()
            }
            and {
                if (claveInstitucion) {
                    institucionEducativa {
                        and {
                            eq("clave", claveInstitucion)
                        }
                    }
                } else {
                    isNull("institucionEducativa")
                }
                eq("activo", true)
                between("validoHastaCer", fechaAvisoInicio, fechaAvisoFin)
            }
        }
        
        return [
            firmasCaducadas: firmasCaducadas,
            firmasProximasCaducar: firmasProximasCaducar
        ]
        
    }
    
    private HttpSession getSession() {
        return RequestContextHolder.currentRequestAttributes().getSession()
    }
}
