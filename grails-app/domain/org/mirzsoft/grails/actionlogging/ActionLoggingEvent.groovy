/*****************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Erick M. Morales M.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *****************************************************************************/

package org.mirzsoft.grails.actionlogging

import org.mirzsoft.grails.actionlogging.Constants.EventStatus
import seguridad.Usuario

class ActionLoggingEvent implements Serializable{
    private static final long serialVersionUID = 1

    String controllerName
    String actionName
    String customActionName
    String customLog = ""
    String exceptionMessage
    String exceptionStackTrace
    EventStatus status = EventStatus.RUNNING
    String actionType
    Long startTime
    Long endTime
    Long totalTime
    Date date
    String forwardURI
    String remoteHost
    Boolean ajax
    String params
    Long userId
    
    def getHtmlCustomLog(){
        customLog?.replace("\n", "<br/>")
    }

    def getHtmlExceptionStackTrace(){
        exceptionStackTrace?.replace("\n", "<br/>")
    }
    
    def getUsuario(){
        return Usuario.get(userId)?:null
    }
    
    static constraints = {
        actionName          nullable: true, blank: true, maxSize: 250
        actionType          nullable: true, blank: true, maxSize: 5000
        controllerName      nullable: true, blank: true, maxSize: 250
        customActionName    nullable: true, blank: true, maxSize: 5000
        customLog           nullable: true, blank: true, maxSize: 5000000
        endTime             nullable: true
        exceptionMessage    nullable: true, blank: true, maxSize: 5000000
        exceptionStackTrace nullable: true, blank: true, maxSize: 5000000
        forwardURI          nullable: true, blank: true, maxSize: 5000
        params              nullable: true, blank: true, maxSize: 5000
        remoteHost          nullable: true, blank: true, maxSize: 250
        startTime           nullable: true, blank: true
        totalTime           nullable: true
        userId              nullable: true
    }
    
    static mapping = {
        version false
        
        table "seg_bitacora"
    }
}