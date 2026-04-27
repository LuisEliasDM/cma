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

import static org.codehaus.groovy.grails.commons.ControllerArtefactHandler.TYPE as CONTROLLER
import groovy.util.logging.Slf4j
import org.codehaus.groovy.grails.commons.GrailsClass
import grails.web.Action

import org.mirzsoft.grails.actionlogging.ActionLoggingEvent
import org.mirzsoft.grails.actionlogging.ActionLoggingService
import org.mirzsoft.grails.actionlogging.annotation.*
import org.mirzsoft.grails.actionlogging.Constants.EventStatus

@Slf4j
class ActionLoggingFilters {

    ActionLoggingService actionLoggingService
    def springSecurityService

    def filters = {
        actionLogging(controller:'*', action:'*') {
            before = {
                boolean actionLoggingEnabled = true
                long start = System.currentTimeMillis()

                if (!controllerName) {
                    return
                }

                GrailsClass controllerClazz = grailsApplication.getArtefactByLogicalPropertyName(CONTROLLER, controllerName)
                if (!controllerClazz.clazz.getAnnotation(ActionLogging)?.value()) {
                    actionLoggingEnabled = false
                }
                
                List controllerMethods = controllerClazz.clazz.methods.findAll {it.getAnnotation(Action)}
                
                String actionNameValue = actionName ?: controllerClazz.getPropertyValue("defaultAction")?: controllerClazz.clazz.metaClass.respondsTo(controllerClazz, "index")? "index": controllerMethods.size == 1? controllerMethods.first().name: null
                boolean printCustomLogEnabled = controllerClazz.clazz.getAnnotation(PrintCustomLog)?.value()
                String actionType = controllerClazz.clazz.getAnnotation(ActionType)?.value()
                String customActionName
                
                for (method in controllerMethods) {
                    if (method.name != actionNameValue) {
                        continue
                    }

                    CustomActionName customActionNameAnnotation = method.getAnnotation(CustomActionName)
                    if (customActionNameAnnotation) {
                        customActionName = customActionNameAnnotation.value()
                    }

                    ActionType actionTypeAnnotation = method.getAnnotation(ActionType)
                    if (actionTypeAnnotation) {
                        actionType = actionTypeAnnotation.value()
                    }

                    if (method.getAnnotation(ActionLogging)) {
                        actionLoggingEnabled = method.getAnnotation(ActionLogging)?.value()? true: false
                    }

                    if (method.getAnnotation(PrintCustomLog)) {
                        printCustomLogEnabled = method.getAnnotation(PrintCustomLog)?.value()? true: false
                    }

                    break
                }
                
                if (!actionLoggingEnabled) {
                    return
                }

                Long userId
                if (springSecurityService && controllerClazz.clazz.getAnnotation(SpringUserIdentification)?.value()) {
                    userId = springSecurityService?.currentUser?.id
                }
                
                
                def parameters = new TreeMap(params)
                def value
                ["contrasenaClavePrivada", "contrasenaActual", "contrasenaNueva", "contrasenaNuevac"].each {
                    value = parameters[it]
                    if (value) {
                        parameters?.replace(it, "".padLeft(value?.length(),"*"))
                    }
                }
                                
                ActionLoggingEvent event = new ActionLoggingEvent(
                    controllerName: controllerName,
                    actionName: actionNameValue,
                    customActionName: customActionName,
                    actionType: actionType,
                    startTime: start,
                    date: new Date(),
                    forwardURI: request.forwardURI,
                    remoteHost: request.remoteHost,
                    ajax : request.xhr,
                    params: parameters?.toString(),
                    userId: userId)

                request.actionLoggingEvent = event
                request.printCustomLogEnabled = printCustomLogEnabled
                
                actionLoggingService.save event

            }

            afterView = { Exception e ->
                ActionLoggingEvent event = request.actionLoggingEvent
                if (!event) return

                long end = System.currentTimeMillis()
                event.endTime = end
                event.totalTime = end - event.startTime

                if (e) {
                    actionLoggingService.setCustomException request.exception ?: e
                }
                else if (event.status != EventStatus.ERROR) {
                    event.status = EventStatus.SUCCESS
                }
                
                actionLoggingService.save event
            }
        }
    }
}
