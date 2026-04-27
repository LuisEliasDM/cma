package renapo

import grails.converters.JSON
import javax.imageio.ImageIO

class RenapoController {
    def renapoService
    
    def consultarDatosCurpWs(){
        render renapoService.consultarDatosCurpWs(params?.curp) as JSON
    }

    def consultarDatosCurp() { 
        render renapoService.consultarDatosCurp(params?.curp) as JSON
    }
    
    def validacionWeb(){
//        byte[] captcha = renapoService.validacionWeb(params?.curp)
//        println captcha
//        if (!captcha) {
//            return
//        }
        
//        def baos = new ByteArrayOutputStream()
//        def originalImage = ImageIO.read(captcha)
//        println originalImage
//        ImageIO.write(originalImage, "png", baos)
//        def imageInByte = baos.toByteArray()
//        println imageInByte
        
//        response.setHeader("Content-length", captcha.length.toString())
//        response.contentType = "image/jpg" 
//        response.outputStream << captcha 
//        response.outputStream.flush()
        
        render renapoService.validacionWeb(params?.curp) as JSON
    }
    
    def consultarCurpWeb(){
        render renapoService.consultarCurpWeb(params?.codigo) as JSON
    }
}
