(function($) {
    $.fn.validaCurp = function(options) {

        $.each(this, function(index, element) {
            var settings = $.extend(true, {
                // These are the defaults.
                inputName: {
                    curp: "curp",
                    nombre: "nombre",
                    aPaterno: "apellidoPaterno",
                    aMaterno: "apellidoMaterno",
                    sexo: "sexo",
                    fNacimiento: "fechaNacimiento",
                    eNacimiento: "estadoNacimiento"
                },
                hidden: true,
                columns: true,
                readOnly: false
            }, options);

            //Global vars
            var url = [
                "renapo/consultarDatosCurpWs/",
            ];
            var calling = 0;
            var countCalls = 0;
            var colL = $("<section class='sectionLeft'></section>");
            var colR = $("<section class='sectionRight'></section>");

            //Element vars
            var curpInput = $("<input type='text' name='" + settings.inputName.curp + "' maxlength='18' minlength='18' class='textInput standar' style='display: inline-block;' required " + (settings.readOnly ? "readOnly" : "") + " autocomplete='off'/>");

            var renapoIcon = $("<a href='http://consultas.curp.gob.mx/' rel='Popup'><span class='ui-icon ui-icon-search purpleIcon' style='display: inline-block; margin-left: 10px; margin-top:5px; position:absolute;'>Consultar curp</span></a>");

            var nombreInput = $("<input type='text' name='" + settings.inputName.nombre + "' class='textInput standar' required autocomplete='off' placeholder='ingresa una CURP valida'/>");
            var aPaternoInput = $("<input type='text' name='" + settings.inputName.aPaterno + "' class='textInput standar' required autocomplete='off' placeholder='ingresa una CURP valida'/>");
            var aMaternoInput = $("<input type='text' name='" + settings.inputName.aMaterno + "' class='textInput standar' autocomplete='off' placeholder=''/>");
            var sexoInput = $("<input type='text' name='" + settings.inputName.sexo + "' class='textInput standar' required autocomplete='off' placeholder='ingresa una CURP valida'/>");
            var fNacimientoInput = $("<input type='text' name='" + settings.inputName.fNacimiento + "' class='textInput standar' required autocomplete='off' placeholder='ingresa una CURP valida'/>");
            var eNacimientoInput = $("<input type='text' name='" + settings.inputName.eNacimiento + "' class='textInput standar' required autocomplete='off' placeholder='ingresa una CURP valida'/>");

            $.each([nombreInput, aPaternoInput, aMaternoInput, sexoInput, fNacimientoInput, eNacimientoInput], function(){
                $(this).on("keydown paste", function(e){
                    e.preventDefault();
                });

                $(this).on("click", function(e){
                    return false;
                });
            });

            var largeSection = $("<div class='sectionLarge'></div>");
            var labelField = $("<label>CURP:</label>");
            var fieldContain = $("<div class='fieldcontain'></div>");

            var messageBox = $("<div class='pluginCurpMessageBox'></div>");

            var retryContent = $("<span style='display: inline-block; margin-left: 10px; margin-top:5px; position:absolute;'></span>");
            var retry = $("<a href='#' style='display:none;'>Re-intentar</a>");


            //Elements Sort
            fieldContain.append(labelField);
            fieldContain.append(curpInput);
            //fieldContain.append(renapoIcon);
            fieldContain.append(retryContent);
            $(retryContent).append(retry);
            fieldContain.append(messageBox);

            if (settings.columns) {
                colL.append(fieldContain);
            } else {
                largeSection.append(fieldContain);
            }



            if (settings.hidden) {
                //Hide elements
                $(nombreInput).attr("type", "hidden");
                $(aPaternoInput).attr("type", "hidden");
                $(aMaternoInput).attr("type", "hidden");
                $(sexoInput).attr("type", "hidden");
                $(fNacimientoInput).attr("type", "hidden");
                $(eNacimientoInput).attr("type", "hidden");

                if (settings.columns) {
                    colL.append(nombreInput);
                    colL.append(aPaternoInput);
                    colL.append(aMaternoInput);
                    colL.append(sexoInput);
                    colL.append(fNacimientoInput);
                    colL.append(eNacimientoInput);
                } else {
                    largeSection.append(nombreInput);
                    largeSection.append(aPaternoInput);
                    largeSection.append(aMaternoInput);
                    largeSection.append(sexoInput);
                    largeSection.append(fNacimientoInput);
                    largeSection.append(eNacimientoInput);
                }

            } else {
                //Show elements
                var nfieldContain = fieldContain.clone();
                var nlabelField = labelField.clone();
                nfieldContain.html(nlabelField.html("Nombre(s): "));
                nfieldContain.append(nombreInput);

                if (settings.columns) {
                    colL.append(nfieldContain);
                } else {
                    largeSection.append(nfieldContain);
                }

                var apfieldContain = fieldContain.clone();
                var aplabelField = labelField.clone();
                apfieldContain.html(aplabelField.html("Apellido Paterno: "));
                apfieldContain.append(aPaternoInput);

                if (settings.columns) {
                    colL.append(apfieldContain);
                } else {
                    largeSection.append(apfieldContain);
                }

                var amfieldContain = fieldContain.clone();
                var amlabelField = labelField.clone();
                amfieldContain.html(amlabelField.html("Apellido Materno: "));
                amfieldContain.append(aMaternoInput);

                if (settings.columns) {
                    colL.append(amfieldContain);
                } else {
                    largeSection.append(amfieldContain);
                }

                var sfieldContain = fieldContain.clone();
                var slabelField = labelField.clone();
                sfieldContain.html(slabelField.html("Sexo: "));
                sfieldContain.append(sexoInput);

                if (settings.columns) {
                    colR.append(sfieldContain);
                } else {
                    largeSection.append(sfieldContain);
                }

                var fnfieldContain = fieldContain.clone();
                var fnlabelField = labelField.clone();
                fnfieldContain.html(fnlabelField.html("Fecha de nacimiento: "));
                fnfieldContain.append(fNacimientoInput);

                if (settings.columns) {
                    colR.append(fnfieldContain);
                } else {
                    largeSection.append(fnfieldContain);
                }

                var enfieldContain = fieldContain.clone();
                var enlabelField = labelField.clone();
                enfieldContain.html(enlabelField.html("Entidad de nacimiento: "));
                enfieldContain.append(eNacimientoInput);

                if (settings.columns) {
                    colR.append(enfieldContain);
                } else {
                    largeSection.append(enfieldContain);
                }

            }

            if (settings.columns) {

                if (settings.hidden) {
                    $(element).append(colL);
                } else {
                    $(element).append(colL);
                    $(element).append(colR);
                }

            } else {
                $(element).append(largeSection);
            }

            //Set init data
            if (settings.initData !== undefined) {

                $(curpInput).val((settings.initData.curp !== undefined ? settings.initData.curp : "").toUpperCase());
                $(nombreInput).val((settings.initData.nombre !== undefined ? settings.initData.nombre : "").toUpperCase());
                $(aPaternoInput).val((settings.initData.aPaterno !== undefined ? settings.initData.aPaterno : "").toUpperCase());
                $(aMaternoInput).val((settings.initData.aMaterno !== undefined ? settings.initData.aMaterno : "").toUpperCase());
                $(sexoInput).val((settings.initData.sexo !== undefined ? settings.initData.sexo : "").toUpperCase());

                if (settings.initData.fNacimiento !== undefined) {
                    var dia = settings.initData.fNacimiento.dia !== undefined ? settings.initData.fNacimiento.dia : "";
                    var mes = settings.initData.fNacimiento.mes !== undefined ? settings.initData.fNacimiento.mes : "";
                    var anio = settings.initData.fNacimiento.anio !== undefined ? settings.initData.fNacimiento.anio : "";
                    var fecha = dia + "/" + mes + "/" + anio;

                    $(fNacimientoInput).val((fecha.length > 0 ? fecha : "").toUpperCase());
                }

                $(eNacimientoInput).val((settings.initData.eNacimiento !== undefined ? settings.initData.eNacimiento : "").toUpperCase());

            }


            //Get data from initData
            if (settings.initData !== undefined) {
                if (settings.initData.validate) {
                    callServer();
                }
            }


            // == Events ==

            //Popup window
            $(renapoIcon).on("click", function() {
                var opts = "height=650,width=450,resizable=false,scrollbars=false,location=0";
                nueva = window.open(this.href, 'Popup', opts);
                return false;
            });

            //Key validate
            $(curpInput).on("keypress", function() {
                //alert("press");
                if (((event.keyCode < 48) || (event.keyCode > 57)) && ((event.keyCode < 65) || (event.keyCode > 90)) && ((event.keyCode < 97) || (event.keyCode > 122))) {
                    event.returnValue = false;
                }
            });

            //Uppercase
            $(curpInput).on("keyup", function() {
                curpInput.val(curpInput.val().toUpperCase());
            });

            //Value change
            var lastValue = '';
            setInterval(function() {
//                if ($(curpInput).val() !== lastValue) {
                var reference = "";

                if (settings.initData !== undefined) {
                    reference = (settings.initData.curp !== undefined ? settings.initData.curp : "").toUpperCase();
                }

                if ($(curpInput).val() !== lastValue) {
                    $(messageBox).html("");
                    $(retry).hide();

                    if (curpInput.val().length === 18) {
                        if (reference !== "") {
                            if (settings.initData.validate || (lastValue !== "" && reference !== lastValue)) {
                                callServer();
                            }
                            
                        } else {
                            callServer();
                        }
                    } else {
                        clearData();
                        $(messageBox).html("Por favor ingrese los 18 digitos de la CURP");
                    }   

                    lastValue = $(curpInput).val();

                }
            }, 100);

            $(retry).click(function(e) {
                e.preventDefault();
                if (curpInput.val().length === 18) {
                    callServer();
                } else {
                    $(messageBox).html("Por favor ingrese los 18 digitos de la CURP");
                } 
            });

            // == Functions ==

            //Ajax call
            function callServer() {
                if (calling === 0) {
                    clearData();
                    $(retry).hide();
                    $(messageBox).html("Validando CURP, por favor espere...");
                    console.log("Llamando al servidor... Intento(" + countCalls + ")");
                    calling = 1;
                    $.ajax({
                        url: url[countCalls],
                        data: {
                            curp: curpInput.val()
                        },
                        type: "POST",
                        timeout: 15000
                    }).done(function(result) {
                        console.log("La operación se ejecutó correctamente");
                        console.log(result);

                        if (result.curp !== null && result.curp !== undefined) {
                            showData(result);
                            $(messageBox).html("CURP correcto.");
                        } else {
                            $(messageBox).html(result.mensaje + ".<br/>Por favor verifique e intente nuevamente.");
                            calling = 0;
                            callServerWebCaptcha();
                        }

                    }).error(function(data, textStatus, jqXHR) {
                        console.log("Ocurrió un error: " + textStatus);
                        calling = 0;
//                        callServerWebCaptcha();
//                        $(retry).fadeIn();
//                        clearData();
//                        
//                        if (textStatus === "timeout") {
//                            $(messageBox).html("No se pudo realizar la validacion de la CURP. Por favor verifique su conexion a internet e intente nuevamente.");
//                        } else {
//                            $(messageBox).html("No se pudo realizar la validacion de la CURP. Por favor verifique e intente nuevamente.");
//                        }
//                        
//////                        if (textStatus === "timeout") {
////                            console.log("No se pudo establecer la conexión.");
////                            console.log("Llamadas realizadas: " + countCalls + ", disponibles: " + url.length-1);
////                            if (countCalls < url.length-1) {
////                                console.log("Realizando un intento en otra instancia...");
////                                countCalls++;
////                                calling = 0;
////                                callServer();
////                            } else {
////                                console.log("Cambiando a modo libre");
////                                changeFree();
////                            }
//////                        }

                    }).always(function() {
                        calling = 0;
                    });
                } else {
                    console.log("Servidor ocupado.");
                }
            }
            
            
            //Ajax call
            function callServerWebCaptcha() {
                if (calling === 0) {
                    clearData();
                    $(retry).hide();
                    $(messageBox).html("Validando CURP, por favor espere... *");
                    console.log("Llamando al servidor... Intento alternativo");
                    calling = 1;
                    $.ajax({
                        url: "renapo/validacionWeb?curp=" + curpInput.val(),
                        type: "POST",
                        contentType: "image/jpg",
//                        dataType: "image/jpg",
//                        parse: false,
                        timeout: 30000
                    }).success(function(result) {
                        console.log("La operación se ejecutó correctamente");
                        
                        if (result.hecho) {
                            var i = new Image();
                            $(i).attr("src", "data:image/jpg;base64," + result.captcha);
                            $(i).attr("style", "width: 90%;");
                            
                            var dialog = $("<div title='Codigo de verificacion' style='text-align: center;'></div>");
                            $(dialog).append("Por favor ingrese el código de verificación mostrado en la siguiente imagen:<br/><br/>");
                            $(dialog).append(i);
                            $(dialog).append("<br/>");
                            $(dialog).append("<br/>");
                            var input = $("<input type='text' name='codigo' style='width: 90%; text-align: center; font-weight: bold; height: 35px; font-size: 20px; text-transform: uppercase;'/>");
                            $(dialog).append(input);
                            
                            $(input).on("keyup", function() {
                                input.val(input.val().toUpperCase());
                            });
                            
                            var atmClosed = false;
                            $(dialog).dialog({
                                modal: true,
                                width: 350,
                                height: 340,
                                buttons: {
                                    Continuar: function() {
                                        atmClosed = true;
                                        callServerWeb($(input).val());
                                        $(this).dialog( "close" );
                                    }
                                }
                            });
                            
                            $(dialog).bind("dialogclose", function(event, ui) {
                                if (!atmClosed) {
                                    $(messageBox).html("No se pudo realizar la validacion de la CURP.<br/>Por favor intente nuevamente.");
                                    $(retry).fadeIn();
                                }
                            });
                            
                        }else {
                            $(messageBox).html(result.mensaje + ".<br/>Por favor verifique e intente nuevamente.");
                            $(retry).fadeIn();
                        }

                    }).error(function(data, textStatus, jqXHR) {
                        $(retry).fadeIn();
                        console.log("Ocurrió un error: " + textStatus);
                        clearData();
                        
                        if (textStatus === "timeout") {
                            $(messageBox).html("No se pudo realizar la validacion de la CURP.<br/>Por favor verifique su conexion a internet e intente nuevamente.");
                        } else {
                            $(messageBox).html("No se pudo realizar la validacion de la CURP.<br/>Por favor verifique e intente nuevamente.");
                        }

                    }).always(function() {
                        calling = 0;
                    });
                } else {
                    console.log("Servidor ocupado.");
                }
            }
            
            function callServerWeb(codigo) {
                if (calling === 0) {
                    clearData();
                    $(retry).hide();
                    $(messageBox).html("Validando CURP, por favor espere...");
                    console.log("Llamando al servidor... Intento(" + countCalls + ")");
                    calling = 1;
                    $.ajax({
                        url: "renapo/consultarCurpWeb",
                        data: {
                            codigo: codigo
                        },
                        type: "POST",
                        timeout: 15000
                    }).done(function(result) {
                        console.log("La operación se ejecutó correctamente.");
                        console.log(result);

                        if (result.hecho == true) {
                            showData(result);
                            $(messageBox).html("CURP correcto.");
                        } else {
                            $(messageBox).html(result.mensaje + ".<br/>Por favor verifique e intente nuevamente.");
                            $(retry).fadeIn();
                        }

                    }).error(function(data, textStatus, jqXHR) {
                        $(retry).fadeIn();
                        console.log("Ocurrió un error: " + textStatus);
                        clearData();
                        
                        if (textStatus === "timeout") {
                            $(messageBox).html("No se pudo realizar la validacion de la CURP.<br/>Por favor verifique su conexion a internet e intente nuevamente.");
                        } else {
                            $(messageBox).html("No se pudo realizar la validacion de la CURP.<br/>Por favor verifique e intente nuevamente.");
                        }
                        
////                        if (textStatus === "timeout") {
//                            console.log("No se pudo establecer la conexión.");
//                            console.log("Llamadas realizadas: " + countCalls + ", disponibles: " + url.length-1);
//                            if (countCalls < url.length-1) {
//                                console.log("Realizando un intento en otra instancia...");
//                                countCalls++;
//                                calling = 0;
//                                callServer();
//                            } else {
//                                console.log("Cambiando a modo libre");
//                                changeFree();
//                            }
////                        }

                    }).always(function() {
                        calling = 0;
                    });
                } else {
                    console.log("Servidor ocupado.");
                }
            }

            //Data clear
            function clearData() {
                $(nombreInput).val("");
                $(aPaternoInput).val("");
                $(aMaternoInput).val("");
                $(sexoInput).val("");
                $(fNacimientoInput).val("");
                $(eNacimientoInput).val("");
            }

            //Data show
            function showData(result) {
                $(curpInput).val(result.curp);
                $(nombreInput).val(result.nombre);
                $(aPaternoInput).val(result.apellidoPaterno);
                $(aMaternoInput).val(result.apellidoMaterno);
                $(sexoInput).val(result.sexo);
                $(fNacimientoInput).val(result.fechaNacimiento);
                $(eNacimientoInput).val(result.claveEntidadNacimiento);
            }

            //Change to free mode (timeout)
            function changeFree() {
                //Set init data
                if (settings.initData === undefined) {
                    settings.initData = {
                        curp: $(curpInput).val()
                    };
                }

                //Set old function
                settings.oldFunction = "curp";

                $(element).html("");
                $(element).formDatos(settings);
            }

            function fieldValidate() {
                return validaCURP($(curpInput).val());
            }

            function validaCURP(curp) {

                var reg = "";

                if (curp.length === 18) {
                    var digito = calculaDigito(curp);

                    reg = /[A-Z]{4}\d{6}[HM][A-Z]{2}[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9][0-9]/;

                    if (curp.search(reg)) {
                        alert("La curp: " + curp + " no es valida, verifiqué ");
                        $(curpInput).focus();
                        return false;
                    }

                    if (!(parseInt(digito) === parseInt(curp.substring(17, 18)))) {
                        alert("La curp: " + curp + " no es valida, revisé el Digito Verificador (" + digito + ")");
                        $(curpInput).focus();
                        return false;
                    }
                    return true;
                } else {
                    alert("La curp debe tener 18 caractéres, verifiqué ");
                    $(curpInput).focus();
                    return false;
                }
            }

            function calculaDigito(curp) {
                var segRaiz = curp.substring(0, 17);
                var chrCaracter = "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
                var intFactor = new Array(17);
                var lngSuma = 0.0;
                var lngDigito = 0.0;

                for (var i = 0; i < 17; i++) {
                    for (var j = 0; j < 37; j++) {
                        if (segRaiz.substring(i, i + 1) === chrCaracter.substring(j, j + 1)) {
                            intFactor[i] = j;
                        }
                    }
                }

                for (var k = 0; k < 17; k++) {
                    lngSuma = lngSuma + ((intFactor[k]) * (18 - k));
                }

                lngDigito = (10 - (lngSuma % 10));

                if (lngDigito === 10) {
                    lngDigito = 0;
                }

                return lngDigito;
            }

        });

    };

    $.fn.validaDatos = function(options) {

        $.each(this, function(index, element) {
            var settings = $.extend(true, {
                // These are the defaults.
                inputName: {
                    curp: "curp",
                    nombre: "nombre",
                    aPaterno: "apellidoPaterno",
                    aMaterno: "apellidoMaterno",
                    sexo: "sexo",
                    fNacimiento: {
                        dia: "diaNacimiento",
                        mes: "mesNacimiento",
                        anio: "anioNacimiento"
                    },
                    eNacimiento: "estadoNacimiento"
                },
                hidden: true,
                columns: true,
                readOnly: false
            }, options);

            //Global vars
            var url = [
                "http://200.33.79.33:8080/WSCurp/restful/",
                "http://200.33.79.19:8080/WSCurp/restful/"
            ];
            var calling = 0;
            var countCalls = 0;
            var colL = $("<section class='sectionLeft'></section>");
            var colR = $("<section class='sectionRight'></section>");

            //Element vars
            var curpInput = $("<input type='text' name='" + settings.inputName.curp + "' maxlength='18' minlength='18' class='textInput standar' style='display: inline-block;' required readonly/>");
            var nombreInput = $("<input type='text' name='" + settings.inputName.nombre + "' maxlength='50' class='textInput standar' " + (settings.readOnly ? "readOnly" : "") + "/>");
            var aPaternoInput = $("<input type='text' name='" + settings.inputName.aPaterno + "'maxlength='50' class='textInput standar' " + (settings.readOnly ? "readOnly" : "") + "/>");
            var aMaternoInput = $("<input type='text' name='" + settings.inputName.aMaterno + "'maxlength='50' class='textInput standar' " + (settings.readOnly ? "readOnly" : "") + "/>");
            var sexoInput1 = $("<input type='radio' name='" + settings.inputName.sexo + "' value='H' " + (settings.readOnly ? "onclick='return false'" : "") + "/>");
            var sexoInput2 = $("<input type='radio' name='" + settings.inputName.sexo + "' value='M' " + (settings.readOnly ? "onclick='return false'" : "") + "/>");
            var fNacimientoDiaInput = $("<select name='" + settings.inputName.fNacimiento.dia + "' class='textInput micro' " + (settings.readOnly ? "onfocus='this.defaultIndex=this.selectedIndex;' onchange='this.selectedIndex=this.defaultIndex;'" : "") + "></select>");
            var fNacimientoMesInput = $("<select name='" + settings.inputName.fNacimiento.mes + "' class='textInput litle' " + (settings.readOnly ? "onfocus='this.defaultIndex=this.selectedIndex;' onchange='this.selectedIndex=this.defaultIndex;'" : "") + "></select>");
            var fNacimientoAnioInput = $("<input type='text' name='" + settings.inputName.fNacimiento.anio + "'maxlength='4' class='textInput micro' " + (settings.readOnly ? "readOnly" : "") + "/>");
            var eNacimientoInput = $("<select name='" + settings.inputName.eNacimiento + "' class='textInput standar' " + (settings.readOnly ? "onfocus='this.defaultIndex=this.selectedIndex;' onchange='this.selectedIndex=this.defaultIndex;'" : "") + "></select>");
            var required = $("<span class='required-indicator'>*</span>");
            var validaBoton = $("<input type='button' value='Validar' class='orangeButton'/>");
            var limpiaBoton = $("<input type='button' value='Limpiar' class='purpleButton'/>");

            var largeSection = $("<div class='sectionLarge'></div>");
            var labelField = $("<label>CURP: </label>");
            var fieldContain = $("<div class='fieldcontain'></div>");

            var messageBox = $("<div class='pluginCurpMessageBox'></div>");


            //Fill options
            fNacimientoDiaInput = fillDays(fNacimientoDiaInput);
            fNacimientoMesInput = fillMonths(fNacimientoMesInput);

            eNacimientoInput = fillStates(eNacimientoInput);

            //Elements Sort
            var nfieldContain = fieldContain.clone();
            var nlabelField = labelField.clone();
            nfieldContain.html(nlabelField.html("Nombre(s): "));
            nlabelField.append(required.clone());
            nfieldContain.append(nombreInput);

            if (settings.columns) {
                colL.append(nfieldContain);
            } else {
                largeSection.append(nfieldContain);
            }

            var apfieldContain = fieldContain.clone();
            var aplabelField = labelField.clone();
            apfieldContain.html(aplabelField.html("Apellido Paterno: "));
            aplabelField.append(required.clone());
            apfieldContain.append(aPaternoInput);

            if (settings.columns) {
                colL.append(apfieldContain);
            } else {
                largeSection.append(apfieldContain);
            }

            var amfieldContain = fieldContain.clone();
            var amlabelField = labelField.clone();
            amfieldContain.html(amlabelField.html("Apellido Materno: "));
            amfieldContain.append(aMaternoInput);

            if (settings.columns) {
                colL.append(amfieldContain);
            } else {
                largeSection.append(amfieldContain);
            }

            var sfieldContain = fieldContain.clone();
            var slabelField = labelField.clone();
            sfieldContain.html(slabelField.html("Sexo: "));
            slabelField.append(required.clone());
            sfieldContain.append(sexoInput1);
            sfieldContain.append("Hombre");
            sfieldContain.append(sexoInput2);
            sfieldContain.append("Mujer");

            if (settings.columns) {
                colL.append(sfieldContain);
            } else {
                largeSection.append(sfieldContain);
            }

            var fnfieldContain = fieldContain.clone();
            var fnlabelField = labelField.clone();
            fnfieldContain.html(fnlabelField.html("Fecha de nacimiento: "));
            fnlabelField.append(required.clone());
            fnfieldContain.append(fNacimientoDiaInput);
            fnfieldContain.append(" / ");
            fnfieldContain.append(fNacimientoMesInput);
            fnfieldContain.append(" / ");
            fnfieldContain.append(fNacimientoAnioInput);

            if (settings.columns) {
                colR.append(fnfieldContain);
            } else {
                largeSection.append(fnfieldContain);
            }

            var enfieldContain = fieldContain.clone();
            var enlabelField = labelField.clone();
            enfieldContain.html(enlabelField.html("Entidad de nacimiento: "));
            enlabelField.append(required.clone());
            enfieldContain.append(eNacimientoInput);

            if (settings.columns) {
                colR.append(enfieldContain);
            } else {
                largeSection.append(enfieldContain);
            }

            if (settings.hidden) {
                //Hide elements
                $(curpInput).attr("type", "hidden");

                if (settings.columns) {
                    colR.append(curpInput);
                } else {
                    largeSection.append(curpInput);
                }

            } else {
                //Show elements
                fieldContain.append(labelField);
                fieldContain.append(curpInput);

                if (settings.columns) {
                    colR.append(fieldContain);
                } else {
                    largeSection.append(fieldContain);
                }
            }

            if (settings.columns) {
                colR.append(messageBox);

                if (!settings.readOnly) {
                    colR.append(validaBoton);
                    colR.append(limpiaBoton);
                }


                $(element).append(colL);
                $(element).append(colR);

            } else {
                largeSection.append(messageBox);

                if (!settings.readOnly) {
                    largeSection.append(validaBoton);
                    largeSection.append(limpiaBoton);
                }

                $(element).append(largeSection);
            }

            //Set init data
            if (settings.initData !== undefined) {

                $(curpInput).val(settings.initData.curp !== undefined ? settings.initData.curp : "");
                $(nombreInput).val(settings.initData.nombre !== undefined ? settings.initData.nombre : "");
                $(aPaternoInput).val(settings.initData.aPaterno !== undefined ? settings.initData.aPaterno : "");
                $(aMaternoInput).val(settings.initData.aMaterno !== undefined ? settings.initData.aMaterno : "");

                if (settings.initData.sexo !== undefined) {
                    if (settings.initData.sexo === "H" || settings.initData.sexo === "h") {
                        $(sexoInput1).attr("checked", "");
                    } else if (settings.initData.sexo === "M" || settings.initData.sexo === "m") {
                        $(sexoInput2).attr("checked", "");
                    }
                }

                if (settings.initData.fNacimiento !== undefined) {
                    $(fNacimientoDiaInput).val(settings.initData.fNacimiento.dia !== undefined ? settings.initData.fNacimiento.dia : "");
                    $(fNacimientoMesInput).val(settings.initData.fNacimiento.mes !== undefined ? settings.initData.fNacimiento.mes : "");
                    $(fNacimientoAnioInput).val(settings.initData.fNacimiento.anio !== undefined ? settings.initData.fNacimiento.anio : "");
                }

                $(eNacimientoInput).val(settings.initData.eNacimiento !== undefined ? settings.initData.eNacimiento : "");
            }

            //Get data from initData
            if (settings.initData !== undefined) {
                if (settings.initData.validate) {
                    callServer();
                }
            }

            // == Events ==

            //Key validate
            $.each([nombreInput, aPaternoInput, aMaternoInput], function(index, e) {
                $(e).on("keypress", function() {
                    if (((event.keyCode < 65) || (event.keyCode > 90)) && ((event.keyCode < 97) || (event.keyCode > 122)) && event.keyCode !== 32 && event.keyCode !== 241 && event.keyCode !== 209 && event.keyCode !== 46) {
                        event.returnValue = false;
                    }
                });
            });

            $(fNacimientoAnioInput).on("keypress", function() {
                if ((event.keyCode < 48) || (event.keyCode > 57)) {
                    event.returnValue = false;
                }
            });

            //Uppercase
            $.each([nombreInput, aPaternoInput, aMaternoInput], function(index, e) {
                $(e).on("keyup", function() {
                    $(this).val($(this).val().toUpperCase());
                });
            });

            $(validaBoton).on("click", function() {
                callServer();
            });

            $(limpiaBoton).on("click", function() {
                clearData();
            });


            // == Functions ==

            //Ajax call
            function callServer() {
                if (calling === 0) {
                    $(curpInput).val("");
                    $(messageBox).html("Validando datos...");
                    console.log("Llamando al servidor... Intento(" + countCalls + ")");
                    calling = 1;
                    $.ajax({
                        url: url[countCalls] + "datos.json",
                        data: {
                            nombre: $(nombreInput).val(),
                            apellidoPaterno: $(aPaternoInput).val(),
                            apllidoMaterno: $(aMaternoInput).val(),
                            sexo: ($(sexoInput1).is(":checked")) ? $(sexoInput1).val() : $(sexoInput2).val(),
                            fechaNacimiento: $(fNacimientoDiaInput).val() + "/" + $(fNacimientoMesInput).val() + "/" + $(fNacimientoAnioInput).val(),
                            estadoNacimiento: $(eNacimientoInput).val()
                        },
                        type: "POST",
                        timeout: 15000
                    }).done(function(result) {
                        console.log("La operación se ejecutó correctamente");
                        console.log(result);

                        if (result.curp !== null && result.curp !== undefined) {
                            showData(result);
                            $(messageBox).html("Datos correctos.");
                        } else {
                            $(messageBox).html(result.mensaje);
                        }

                    }).error(function(data, textStatus, jqXHR) {
                        console.log("Ocurrió un error: " + textStatus);

                        $(messageBox).html("Ocurrió un error.");

//                        if (textStatus === "timeout") {
                            console.log("No se pudo establecer la conexión.");
                            console.log("Llamadas realizadas: " + countCalls + ", disponibles: " + url.length-1);
                            if (countCalls < url.length-1) {
                                console.log("Realizando un intento en otra instancia...");
                                countCalls++;
                                calling = 0;
                                callServer();
                            } else {
                                console.log("Cambiando a modo libre");
                                changeFree();
                            }
//                        }

                    }).always(function() {
                        calling = 0;
                    });
                } else {
                    console.log("Servidor ocupado.");
                }
            }

            //Change to free mode
            function changeFree() {
                //Set init data
                if (settings.initData === undefined) {
                    settings.initData = {
                        curp: $(curpInput).val(),
                        nombre: $(nombreInput).val(),
                        aPaterno: $(aPaternoInput).val(),
                        aMaterno: $(aMaternoInput).val(),
                        sexo: $(sexoInput1).is(":checked") ? $(sexoInput1).val() : $(sexoInput2).val(),
                        fNacimiento: {
                            dia: $(fNacimientoDiaInput).val(),
                            mes: $(fNacimientoMesInput).val(),
                            anio: $(fNacimientoAnioInput).val()
                        },
                        eNacimiento: $(eNacimientoInput).val()
                    };
                }

                //Set old function
                settings.oldFunction = "datos";

                $(element).html("");
                $(element).formDatos(settings);
            }

            //Data clear
            function clearData() {
                $(nombreInput).val("");
                $(aPaternoInput).val("");
                $(aMaternoInput).val("");
                $(sexoInput1).removeAttr("checked");
                $(sexoInput2).removeAttr("checked");
                $(fNacimientoDiaInput).val("0");
                $(fNacimientoMesInput).val("");
                $(fNacimientoAnioInput).val("");
                $(eNacimientoInput).val("");
                $(curpInput).val("");
                $(messageBox).html("");
            }

            //Data show
            function showData(result) {
                $(curpInput).val(result.curp);
            }

            //Days fill
            function fillDays(element) {
                var defOption = $("<option value='0'>S...</option>");
                $(element).append(defOption);
                for (var i = 1; i <= 31; i++) {
                    var day = (i < 10) ? "0" + i : i;
                    var option = $("<option>" + day + "</option>");
                    $(element).append(option);
                }
                return element;
            }

            //Months fill
            function fillMonths(element) {
                var months = [
                    {key: "", value: "Seleciona..."},
                    {key: "01", value: "Enero"},
                    {key: "02", value: "Febrero"},
                    {key: "03", value: "Marzo"},
                    {key: "04", value: "Abril"},
                    {key: "05", value: "Mayo"},
                    {key: "06", value: "Junio"},
                    {key: "07", value: "Julio"},
                    {key: "08", value: "Agosto"},
                    {key: "09", value: "Septiembre"},
                    {key: "10", value: "Octubre"},
                    {key: "11", value: "Noviembre"},
                    {key: "12", value: "Diciembre"}
                ];

                $.each(months, function(index, e) {
                    var option = $("<option value='" + e.key + "'>" + e.value + "</option>");
                    $(element).append(option);
                });

                return element;

            }

            //States fill
            function fillStates(element) {
                var states = [
                    {key: "", value: "Selecciona..."},
                    {key: "AS", value: "AGUASCALIENTES"},
                    {key: "BC", value: "BAJA CALIFORNIA"},
                    {key: "BS", value: "BAJA CALIFORNIA SUR"},
                    {key: "CC", value: "CAMPECHE"},
                    {key: "CL", value: "COAHUILA DE ZARAGOZA"},
                    {key: "CM", value: "COLIMA"},
                    {key: "CS", value: "CHIAPAS"},
                    {key: "CH", value: "CHIHUAHUA"},
                    {key: "DF", value: "DISTRITO FEDERAL"},
                    {key: "DG", value: "DURANGO"},
                    {key: "GT", value: "GUANAJUATO"},
                    {key: "GR", value: "GUERRERO"},
                    {key: "HG", value: "HIDALGO"},
                    {key: "JC", value: "JALISCO"},
                    {key: "MC", value: "MEXICO"},
                    {key: "MN", value: "MICHOACAN DE OCAMPO"},
                    {key: "MS", value: "MORELOS"},
                    {key: "NT", value: "NAYARIT"},
                    {key: "NL", value: "NUEVO LEON"},
                    {key: "OC", value: "OAXACA"},
                    {key: "PL", value: "PUEBLA"},
                    {key: "OT", value: "QUERETARO DE ARTEAGA"},
                    {key: "OR", value: "QUINTANA ROO"},
                    {key: "SP", value: "SAN LUIS POTOSI"},
                    {key: "SL", value: "SINALOA"},
                    {key: "SR", value: "SONORA"},
                    {key: "TC", value: "TABASCO"},
                    {key: "TS", value: "TAMAULIPAS"},
                    {key: "TL", value: "TLAXCALA"},
                    {key: "VZ", value: "VERACRUZ"},
                    {key: "YN", value: "YUCATAN"},
                    {key: "ZS", value: "ZACATECAS"},
                    {key: "NE", value: "NACIDO EN EL EXTRANJERO"}
                ];

                $.each(states, function(index, e) {
                    var option = $("<option value='" + e.key + "'>" + e.value + "</option>");
                    $(element).append(option);
                });

                return element;

            }

            function fieldValidate() {
                var intDia;
                var intMes;
                var intAnio;
                var strAnio;
                var dtmHoy = new Date();

                $(aPaternoInput).val(trim($(aPaternoInput).val()).toUpperCase());
                $(aMaternoInput).val(trim($(aMaternoInput).val()).toUpperCase());
                $(nombreInput).val(trim($(nombreInput).val()).toUpperCase());

                if ($(nombreInput).val() === "" || $(nombreInput).val().length < 1) {
                    alert("Es necesario proporcionar completo el Nombre");
                    $(nombreInput).focus();
                    return false;
                }
                if ($(nombreInput).val().length === 1) {
                    var strNombre = $(nombreInput).val();
                    var regN = "[A-ZÑÜ.']";
                    if (strNombre.search(regN) !== 0) {
                        alert("El nombre es invalido, \n Caracteres validos: \n A-Z (incluso Ñ)");
                        $(nombreInput).focus();
                        return false;
                    }
                } else {
                    var strNombre = $(nombreInput).val();
                    var intLongN = strNombre.length - 2;
                    var regN = "[A-ZÑÜ]" + "[A-ZÑÜ'/.\\- ]{" + intLongN + "}" + "[A-Z.ÑÜ]";
                    if (strNombre.search(regN) !== 0) {
                        alert("El nombre es invalido, \n. Caracteres permitidos: A-Z (incluso Ñ), diagonal  (/), guión  (-), punto (.) o apóstrofo ('), Diéresis ( ¨ ) y espacio (excepto al inicio y al final).");
                        $(nombreInput).focus();
                        return false;
                    }
                }

                if ($(aPaternoInput).val() === "" || $(aPaternoInput).val().length < 1) {
                    alert("Es necesario proporcionar completo el Apellido paterno");
                    $(aPaternoInput).focus();
                    return false;
                }

                if ($(aPaternoInput).val().length === 1) {
                    var strPrimer = $(aPaternoInput).val();
                    var regP = "[A-ZÑÜ.']";
                    if (strPrimer.search(regP) !== 0) {
                        alert("El Apellido paterno es invalido, \n caracteres validos: A-Z (incluso Ñ)");
                        $(aPaternoInput).focus();
                        return false;
                    }
                } else {
                    var strPrimer = $(aPaternoInput).val();
                    if (strPrimer.length === 2) {
                        var regP = "[A-ZÑÜ]{1}[A-ZÑÜ.']{1}";
                        if (strPrimer.search(regP) !== 0) {
                            alert("El Apellido paterno es invalido, caracteres invalidos");
                            $(aPaternoInput).focus();
                            return false;
                        }
                    } else {
                        var intLongP = strPrimer.length - 2;
                        var regP = "[A-ZÑÜ]" + "[A-ZÑÜ'/.\\- ]{" + intLongP + "}" + "[A-ZÑÜ.]";
                        if (strPrimer.search(regP) !== 0) {
                            alert("El Apellido paterno es invalido, \n Caracteres permitidos: A-Z (incluso Ñ), diagonal  (/), guión  (-), punto (.) o apóstrofo ('), Diéresis ( ¨ ) y espacio (excepto al inicio y al final).");
                            $(aPaternoInput).focus();
                            return false;
                        }
                    }
                }

                if ($(aMaternoInput).val() !== "") {
                    if ($(aMaternoInput).val().length === 1) {
                        var strSegundo = $(aMaternoInput).val();
                        var regS = "[A-ZÑÜ.']";
                        if (strSegundo.search(regS) !== 0) {
                            alert("El Apellido materno es invalido, \n caracteres validos: A-Z (incluso Ñ)");
                            $(aMaternoInput).focus();
                            return false;
                        }
                    } else {
                        var segundoAplido = $(aMaternoInput).val();
                        if (segundoAplido.length === 2) {
                            var regP = "[A-ZÑÜ]{1}[A-ZÑÜ.']{1}";
                            if (segundoAplido.search(regP) !== 0) {
                                alert("El Apellido materno es invalido, caracteres invalidos");
                                $(aMaternoInput).focus();
                                return false;
                            }
                        } else {
                            if ($(aMaternoInput).val().length < 2) {
                                alert("Verificar, es necesario proporcionar completo el Apellido materno");
                                $(aMaternoInput).focus();
                                return false;
                            }
                            var strSegundo = $(aMaternoInput).val();
                            var intLongS = strSegundo.length - 2;
                            var regS = "[A-ZÑÜ]" + "[A-ZÑÜ'/.\\- ]{" + intLongS + "}" + "[A-ZÑÜ.]";
                            if (strSegundo.search(regS) !== 0) {
                                alert("El apellido materno es invalido, \n Caracteres permitidos: A-Z (incluso Ñ), diagonal  (/), guión  (-), punto (.) o apóstrofo ('), Diéresis ( ¨ ) y espacio (excepto al inicio y al final).");
                                $(aMaternoInput).focus();
                                return false;
                            }
                        }
                    }
                }

                if ($(sexoInput1).is(":checked")) {

                } else if ($(sexoInput2).is(":checked")) {

                } else {
                    alert("Selecciona el sexo.");
                    return false;
                }

                if ($(fNacimientoDiaInput).val() === "0") {
                    alert("Seleeciona el dia de nacimiento.");
                    $(fNacimientoDiaInput).focus();
                    return false;
                }

                if ($(fNacimientoMesInput).val() === "") {
                    alert("Seleeciona el mes de nacimiento.");
                    $(fNacimientoMesInput).focus();
                    return false;
                }

                strAnio = $(fNacimientoAnioInput).val();
                if (strAnio === "" || strAnio.length < 4 || strAnio.search(/\d{4}/) !== 0) {
                    alert("Es necesario proporcionar el año de la Fecha de nacimiento, con 4 digitos");
                    $(fNacimientoAnioInput).focus();
                    return false;
                }

                intAnio = parseInt(strAnio);
                if (intAnio < (dtmHoy.getFullYear() - 120) || intAnio > dtmHoy.getFullYear()) {
                    alert("Es necesario que el año de la Fecha de nacimiento, se encuentre entre " + (dtmHoy.getFullYear() - 120) + " y " + dtmHoy.getFullYear());
                    $(fNacimientoAnioInput).focus();
                    return false;
                }

                intMes = parseInt($(fNacimientoMesInput).val()) - 1;
                intDia = parseInt($(fNacimientoDiaInput).val());

                if ($(fNacimientoDiaInput).val() === '08') {
                    intDia = parseInt('8');
                }

                if ($(fNacimientoDiaInput).val() === '09') {
                    intDia = parseInt('9');
                }

                if ($(fNacimientoMesInput).val() === '08') {
                    intMes = parseInt('7');
                }

                if ($(fNacimientoMesInput).val() === '09') {
                    intMes = parseInt('8');
                }


                if (!(ValidaFecha(intDia, intMes, intAnio, "La Fecha de nacimiento no es valida"))) {
                    return false;
                }

                if (ComparaFechas(intDia, intMes, intAnio, dtmHoy.getDate(), dtmHoy.getMonth(), dtmHoy.getFullYear()) === 1) {
                    alert("La Fecha de nacimiento no puede ser mayor a la fecha actual");
                    return false;
                }

                if ($(eNacimientoInput).val() === "") {
                    alert("Seleeciona una entidad de nacimiento");
                    $(eNacimientoInput).focus();
                    return false;
                }
                return true;
            }

            function trim(inputString) {
                if (typeof inputString !== "string") {
                    return inputString;
                }
                var retValue = inputString;

                var ch = retValue.substring(0, 1);
                while (ch === " ") {
                    retValue = retValue.substring(1, retValue.length);
                    ch = retValue.substring(0, 1);
                }

                ch = retValue.substring(retValue.length - 1, retValue.length);
                while (ch === " ") {
                    retValue = retValue.substring(0, retValue.length - 1);
                    ch = retValue.substring(retValue.length - 1, retValue.length);
                }

                while (retValue.indexOf("  ") !== -1) {
                    retValue = retValue.substring(0, retValue.indexOf("  ")) +
                            retValue.substring(retValue.indexOf("  ") + 1, retValue.length);
                }
                return retValue;
            }


            function ValidaFecha(dia, mes, anio, mensaje) {
                var fecha = new Date(anio, mes, dia);

                if (fecha.getYear() < 100 || fecha.getYear() >= 2000)
                    var tmp_anio = (fecha.getYear() < 100) ? 1900 + fecha.getYear() : fecha.getYear();

                else if (fecha.getYear() >= 100 && fecha.getYear() < 200)
                    var tmp_anio = 1900 + fecha.getYear();
                else
                    var tmp_anio = fecha.getYear();

                var fecha1 = dia + "/" + mes + "/" + anio;
                var fecha2 = fecha.getDate() + "/" + fecha.getMonth() + "/" + tmp_anio;

                if (fecha1 !== fecha2) {
                    alert(mensaje);
                    return false;
                }
                return true;
            }

            function ComparaFechas(dia1, mes1, anio1, dia2, mes2, anio2) {
                var fecha1 = new Date(anio1, mes1, dia1);
                var fecha2 = new Date(anio2, mes2, dia2);

                if (fecha1.getTime() === fecha2.getTime()) {
                    return (0);
                }
                else if (fecha1.getTime() < fecha2.getTime()) {
                    return (-1);
                }
                else {
                    return (1);
                }
            }

        });

    };


    $.fn.formDatos = function(options) {

        $.each(this, function(index, element) {
            var settings = $.extend(true, {
                // These are the defaults.
                inputName: {
                    curp: "curp",
                    nombre: "nombre",
                    aPaterno: "apellidoPaterno",
                    aMaterno: "apellidoMaterno",
                    sexo: "sexo",
                    fNacimiento: {
                        dia: "diaNacimiento",
                        mes: "mesNacimiento",
                        anio: "anioNacimiento"
                    },
                    eNacimiento: "estadoNacimiento"
                },
                columns: true
            }, options);

            //Global vars
            var colL = $("<section class='sectionLeft'></section>");
            var colR = $("<section class='sectionRight'></section>");

            //Element vars            
            var curpInput = $("<input type='text' name='" + settings.inputName.curp + "' maxlength='18' minlength='18' class='textInput standar' style='display: inline-block;' required " + (settings.readOnly ? "readOnly" : "") + "/>");
            var nombreInput = $("<input type='text' name='" + settings.inputName.nombre + "' maxlength='50' class='textInput standar' required " + (settings.readOnly ? "readOnly" : "") + "/>");
            var aPaternoInput = $("<input type='text' name='" + settings.inputName.aPaterno + "'maxlength='50' class='textInput standar' required " + (settings.readOnly ? "readOnly" : "") + "/>");
            var aMaternoInput = $("<input type='text' name='" + settings.inputName.aMaterno + "'maxlength='50' class='textInput standar' " + (settings.readOnly ? "readOnly" : "") + "/>");
            var sexoInput1 = $("<input type='radio' name='" + settings.inputName.sexo + "' value='H' required " + (settings.readOnly ? "onclick='return false'" : "") + "/>");
            var sexoInput2 = $("<input type='radio' name='" + settings.inputName.sexo + "' value='M' required " + (settings.readOnly ? "onclick='return false'" : "") + "/>");
            var fNacimientoDiaInput = $("<select name='" + settings.inputName.fNacimiento.dia + "' class='textInput micro' required " + (settings.readOnly ? "onfocus='this.defaultIndex=this.selectedIndex;' onchange='this.selectedIndex=this.defaultIndex;'" : "") + "></select>");
            var fNacimientoMesInput = $("<select name='" + settings.inputName.fNacimiento.mes + "' class='textInput litle' required " + (settings.readOnly ? "onfocus='this.defaultIndex=this.selectedIndex;' onchange='this.selectedIndex=this.defaultIndex;'" : "") + "></select>");
            var fNacimientoAnioInput = $("<input type='text' name='" + settings.inputName.fNacimiento.anio + "'maxlength='4' class='textInput micro' required " + (settings.readOnly ? "readOnly" : "") + "/>");
            var eNacimientoInput = $("<select name='" + settings.inputName.eNacimiento + "' class='textInput standar' required " + (settings.readOnly ? "onfocus='this.defaultIndex=this.selectedIndex;' onchange='this.selectedIndex=this.defaultIndex;'" : "") + "></select>");

            var required = $("<span class='required-indicator'>*</span>");

            var largeSection = $("<div class='sectionLarge'></div>");
            var labelField = $("<label>CURP: </label>");
            var fieldContain = $("<div class='fieldcontain'></div>");

            var messageBox = $("<div class='pluginCurpMessageBox'></div>");

            //Fill options
            fNacimientoDiaInput = fillDays(fNacimientoDiaInput);
            fNacimientoMesInput = fillMonths(fNacimientoMesInput);

            eNacimientoInput = fillStates(eNacimientoInput);

            //Elements Sort
            var nfieldContain = fieldContain.clone();
            var nlabelField = labelField.clone();
            nfieldContain.html(nlabelField.html("Nombre(s): "));
            nlabelField.append(required.clone());
            nfieldContain.append(nombreInput);

            if (settings.columns) {
                colL.append(nfieldContain);
            } else {
                largeSection.append(nfieldContain);
            }

            var apfieldContain = fieldContain.clone();
            var aplabelField = labelField.clone();
            apfieldContain.html(aplabelField.html("Apellido Paterno: "));
            aplabelField.append(required.clone());
            apfieldContain.append(aPaternoInput);

            if (settings.columns) {
                colL.append(apfieldContain);
            } else {
                largeSection.append(apfieldContain);
            }

            var amfieldContain = fieldContain.clone();
            var amlabelField = labelField.clone();
            amfieldContain.html(amlabelField.html("Apellido Materno: "));
            amfieldContain.append(aMaternoInput);

            if (settings.columns) {
                colL.append(amfieldContain);
            } else {
                largeSection.append(amfieldContain);
            }

            var sfieldContain = fieldContain.clone();
            var slabelField = labelField.clone();
            sfieldContain.html(slabelField.html("Sexo: "));
            slabelField.append(required.clone());
            sfieldContain.append(sexoInput1);
            sfieldContain.append("Hombre");
            sfieldContain.append(sexoInput2);
            sfieldContain.append("Mujer");

            if (settings.columns) {
                colL.append(sfieldContain);
            } else {
                largeSection.append(sfieldContain);
            }

            var fnfieldContain = fieldContain.clone();
            var fnlabelField = labelField.clone();
            fnfieldContain.html(fnlabelField.html("Fecha de nacimiento: "));
            fnlabelField.append(required.clone());
            fnfieldContain.append(fNacimientoDiaInput);
            fnfieldContain.append(" / ");
            fnfieldContain.append(fNacimientoMesInput);
            fnfieldContain.append(" / ");
            fnfieldContain.append(fNacimientoAnioInput);

            if (settings.columns) {
                colR.append(fnfieldContain);
            } else {
                largeSection.append(fnfieldContain);
            }

            var enfieldContain = fieldContain.clone();
            var enlabelField = labelField.clone();
            enfieldContain.html(enlabelField.html("Entidad de nacimiento: "));
            enlabelField.append(required.clone());
            enfieldContain.append(eNacimientoInput);

            if (settings.columns) {
                colR.append(enfieldContain);
            } else {
                largeSection.append(enfieldContain);
            }

            //Show elements
            labelField.append(required.clone());
            fieldContain.append(labelField);
            fieldContain.append(curpInput);

            if (settings.columns) {
                colR.append(fieldContain);
            } else {
                largeSection.append(fieldContain);
            }

            if (settings.columns) {
                $(colR).append(messageBox);

                $(element).append(colL);
                $(element).append(colR);

            } else {
                $(largeSection).append(messageBox);

                $(element).append(largeSection);
            }

            //Set init data
            if (settings.initData !== undefined) {

                $(curpInput).val(settings.initData.curp !== undefined ? settings.initData.curp : "");
                $(nombreInput).val(settings.initData.nombre !== undefined ? settings.initData.nombre : "");
                $(aPaternoInput).val(settings.initData.aPaterno !== undefined ? settings.initData.aPaterno : "");
                $(aMaternoInput).val(settings.initData.aMaterno !== undefined ? settings.initData.aMaterno : "");

                if (settings.initData.sexo !== undefined) {
                    if (settings.initData.sexo === "H" || settings.initData.sexo === "h") {
                        $(sexoInput1).attr("checked", "");
                    } else if (settings.initData.sexo === "M" || settings.initData.sexo === "m") {
                        $(sexoInput2).attr("checked", "");
                    }
                }

                if (settings.initData.fNacimiento !== undefined) {
                    $(fNacimientoDiaInput).val(settings.initData.fNacimiento.dia !== undefined ? settings.initData.fNacimiento.dia : "");
                    $(fNacimientoMesInput).val(settings.initData.fNacimiento.mes !== undefined ? settings.initData.fNacimiento.mes : "");
                    $(fNacimientoAnioInput).val(settings.initData.fNacimiento.anio !== undefined ? settings.initData.fNacimiento.anio : "");
                }

                $(eNacimientoInput).val(settings.initData.eNacimiento !== undefined ? settings.initData.eNacimiento : "");
            }

            //Verify old function
            if (settings.oldFunction !== undefined) {

                fieldValidate(true);

            }

            // == Events ==

            //Key validate
            $.each([nombreInput, aPaternoInput, aMaternoInput], function(index, e) {
                $(e).on("keypress", function() {
                    if (((event.keyCode < 65) || (event.keyCode > 90)) && ((event.keyCode < 97) || (event.keyCode > 122)) && event.keyCode !== 32 && event.keyCode !== 241 && event.keyCode !== 209 && event.keyCode !== 46) {
                        event.returnValue = false;
                    }
                });
            });

            $(fNacimientoAnioInput).on("keypress", function() {
                if ((event.keyCode < 48) || (event.keyCode > 57)) {
                    event.returnValue = false;
                }
            });

            //Uppercase
            $.each([nombreInput, aPaternoInput, aMaternoInput], function(index, e) {
                $(e).on("keyup", function() {
                    $(this).val($(this).val().toUpperCase());
                });
            });

            // == Functions ==

            //Days fill
            function fillDays(element) {
                var defOption = $("<option value='0'>S...</option>");
                $(element).append(defOption);
                for (var i = 1; i <= 31; i++) {
                    var day = (i < 10) ? "0" + i : i;
                    var option = $("<option>" + day + "</option>");
                    $(element).append(option);
                }
                return element;
            }

            //Months fill
            function fillMonths(element) {
                var months = [
                    {key: "", value: "Seleciona..."},
                    {key: "01", value: "Enero"},
                    {key: "02", value: "Febrero"},
                    {key: "03", value: "Marzo"},
                    {key: "04", value: "Abril"},
                    {key: "05", value: "Mayo"},
                    {key: "06", value: "Junio"},
                    {key: "07", value: "Julio"},
                    {key: "08", value: "Agosto"},
                    {key: "09", value: "Septiembre"},
                    {key: "10", value: "Octubre"},
                    {key: "11", value: "Noviembre"},
                    {key: "12", value: "Diciembre"}
                ];

                $.each(months, function(index, e) {
                    var option = $("<option value='" + e.key + "'>" + e.value + "</option>");
                    $(element).append(option);
                });

                return element;

            }

            //States fill
            function fillStates(element) {
                var states = [
                    {key: "", value: "Selecciona..."},
                    {key: "AS", value: "AGUASCALIENTES"},
                    {key: "BC", value: "BAJA CALIFORNIA"},
                    {key: "BS", value: "BAJA CALIFORNIA SUR"},
                    {key: "CC", value: "CAMPECHE"},
                    {key: "CL", value: "COAHUILA DE ZARAGOZA"},
                    {key: "CM", value: "COLIMA"},
                    {key: "CS", value: "CHIAPAS"},
                    {key: "CH", value: "CHIHUAHUA"},
                    {key: "DF", value: "DISTRITO FEDERAL"},
                    {key: "DG", value: "DURANGO"},
                    {key: "GT", value: "GUANAJUATO"},
                    {key: "GR", value: "GUERRERO"},
                    {key: "HG", value: "HIDALGO"},
                    {key: "JC", value: "JALISCO"},
                    {key: "MC", value: "MEXICO"},
                    {key: "MN", value: "MICHOACAN DE OCAMPO"},
                    {key: "MS", value: "MORELOS"},
                    {key: "NT", value: "NAYARIT"},
                    {key: "NL", value: "NUEVO LEON"},
                    {key: "OC", value: "OAXACA"},
                    {key: "PL", value: "PUEBLA"},
                    {key: "OT", value: "QUERETARO DE ARTEAGA"},
                    {key: "OR", value: "QUINTANA ROO"},
                    {key: "SP", value: "SAN LUIS POTOSI"},
                    {key: "SL", value: "SINALOA"},
                    {key: "SR", value: "SONORA"},
                    {key: "TC", value: "TABASCO"},
                    {key: "TS", value: "TAMAULIPAS"},
                    {key: "TL", value: "TLAXCALA"},
                    {key: "VZ", value: "VERACRUZ"},
                    {key: "YN", value: "YUCATAN"},
                    {key: "ZS", value: "ZACATECAS"},
                    {key: "NE", value: "NACIDO EN EL EXTRANJERO"}
                ];

                $.each(states, function(index, e) {
                    var option = $("<option value='" + e.key + "'>" + e.value + "</option>");
                    $(element).append(option);
                });

                return element;

            }


            function fieldValidate(curp) {
                var intDia;
                var intMes;
                var intAnio;
                var strAnio;
                var dtmHoy = new Date();

                if (curp) {
                    if ($(curpInput).val().length > 0) {
                        validaCURP($(curpInput).val());
                    } else {
                        alert("Es necesario proporcionar la CURP completa.");
                    }
                }

                $(aPaternoInput).val(trim($(aPaternoInput).val()).toUpperCase());
                $(aMaternoInput).val(trim($(aMaternoInput).val()).toUpperCase());
                $(nombreInput).val(trim($(nombreInput).val()).toUpperCase());

                if ($(nombreInput).val() === "" || $(nombreInput).val().length < 1) {
                    alert("Es necesario proporcionar completo el Nombre");
                    $(nombreInput).focus();
                    return false;
                }
                if ($(nombreInput).val().length === 1) {
                    var strNombre = $(nombreInput).val();
                    var regN = "[A-ZÑÜ.']";
                    if (strNombre.search(regN) !== 0) {
                        alert("El nombre es invalido, \n Caracteres validos: \n A-Z (incluso Ñ)");
                        $(nombreInput).focus();
                        return false;
                    }
                } else {
                    var strNombre = $(nombreInput).val();
                    var intLongN = strNombre.length - 2;
                    var regN = "[A-ZÑÜ]" + "[A-ZÑÜ'/.\\- ]{" + intLongN + "}" + "[A-Z.ÑÜ]";
                    if (strNombre.search(regN) !== 0) {
                        alert("El nombre es invalido, \n. Caracteres permitidos: A-Z (incluso Ñ), diagonal  (/), guión  (-), punto (.) o apóstrofo ('), Diéresis ( ¨ ) y espacio (excepto al inicio y al final).");
                        $(nombreInput).focus();
                        return false;
                    }
                }

                if ($(aPaternoInput).val() === "" || $(aPaternoInput).val().length < 1) {
                    alert("Es necesario proporcionar completo el Apellido paterno");
                    $(aPaternoInput).focus();
                    return false;
                }

                if ($(aPaternoInput).val().length === 1) {
                    var strPrimer = $(aPaternoInput).val();
                    var regP = "[A-ZÑÜ.']";
                    if (strPrimer.search(regP) !== 0) {
                        alert("El Apellido paterno es invalido, \n caracteres validos: A-Z (incluso Ñ)");
                        $(aPaternoInput).focus();
                        return false;
                    }
                } else {
                    var strPrimer = $(aPaternoInput).val();
                    if (strPrimer.length === 2) {
                        var regP = "[A-ZÑÜ]{1}[A-ZÑÜ.']{1}";
                        if (strPrimer.search(regP) !== 0) {
                            alert("El Apellido paterno es invalido, caracteres invalidos");
                            $(aPaternoInput).focus();
                            return false;
                        }
                    } else {
                        var intLongP = strPrimer.length - 2;
                        var regP = "[A-ZÑÜ]" + "[A-ZÑÜ'/.\\- ]{" + intLongP + "}" + "[A-ZÑÜ.]";
                        if (strPrimer.search(regP) !== 0) {
                            alert("El Apellido paterno es invalido, \n Caracteres permitidos: A-Z (incluso Ñ), diagonal  (/), guión  (-), punto (.) o apóstrofo ('), Diéresis ( ¨ ) y espacio (excepto al inicio y al final).");
                            $(aPaternoInput).focus();
                            return false;
                        }
                    }
                }

                if ($(aMaternoInput).val() !== "") {
                    if ($(aMaternoInput).val().length === 1) {
                        var strSegundo = $(aMaternoInput).val();
                        var regS = "[A-ZÑÜ.']";
                        if (strSegundo.search(regS) !== 0) {
                            alert("El Apellido materno es invalido, \n caracteres validos: A-Z (incluso Ñ)");
                            $(aMaternoInput).focus();
                            return false;
                        }
                    } else {
                        var segundoAplido = $(aMaternoInput).val();
                        if (segundoAplido.length === 2) {
                            var regP = "[A-ZÑÜ]{1}[A-ZÑÜ.']{1}";
                            if (segundoAplido.search(regP) !== 0) {
                                alert("El Apellido materno es invalido, caracteres invalidos");
                                $(aMaternoInput).focus();
                                return false;
                            }
                        } else {
                            if ($(aMaternoInput).val().length < 2) {
                                alert("Verificar, es necesario proporcionar completo el Apellido materno");
                                $(aMaternoInput).focus();
                                return false;
                            }
                            var strSegundo = $(aMaternoInput).val();
                            var intLongS = strSegundo.length - 2;
                            var regS = "[A-ZÑÜ]" + "[A-ZÑÜ'/.\\- ]{" + intLongS + "}" + "[A-ZÑÜ.]";
                            if (strSegundo.search(regS) !== 0) {
                                alert("El apellido materno es invalido, \n Caracteres permitidos: A-Z (incluso Ñ), diagonal  (/), guión  (-), punto (.) o apóstrofo ('), Diéresis ( ¨ ) y espacio (excepto al inicio y al final).");
                                $(aMaternoInput).focus();
                                return false;
                            }
                        }
                    }
                }

                if ($(sexoInput1).is(":checked")) {

                } else if ($(sexoInput2).is(":checked")) {

                } else {
                    alert("Selecciona el sexo.");
                    return false;
                }

                if ($(fNacimientoDiaInput).val() === "0") {
                    alert("Seleeciona el dia de nacimiento.");
                    $(fNacimientoDiaInput).focus();
                    return false;
                }

                if ($(fNacimientoMesInput).val() === "") {
                    alert("Seleeciona el mes de nacimiento.");
                    $(fNacimientoMesInput).focus();
                    return false;
                }

                strAnio = $(fNacimientoAnioInput).val();
                if (strAnio === "" || strAnio.length < 4 || strAnio.search(/\d{4}/) !== 0) {
                    alert("Es necesario proporcionar el año de la Fecha de nacimiento, con 4 digitos");
                    $(fNacimientoAnioInput).focus();
                    return false;
                }

                intAnio = parseInt(strAnio);
                if (intAnio < (dtmHoy.getFullYear() - 120) || intAnio > dtmHoy.getFullYear()) {
                    alert("Es necesario que el año de la Fecha de nacimiento, se encuentre entre " + (dtmHoy.getFullYear() - 120) + " y " + dtmHoy.getFullYear());
                    $(fNacimientoAnioInput).focus();
                    return false;
                }

                intMes = parseInt($(fNacimientoMesInput).val()) - 1;
                intDia = parseInt($(fNacimientoDiaInput).val());

                if ($(fNacimientoDiaInput).val() === '08') {
                    intDia = parseInt('8');
                }

                if ($(fNacimientoDiaInput).val() === '09') {
                    intDia = parseInt('9');
                }

                if ($(fNacimientoMesInput).val() === '08') {
                    intMes = parseInt('7');
                }

                if ($(fNacimientoMesInput).val() === '09') {
                    intMes = parseInt('8');
                }


                if (!(ValidaFecha(intDia, intMes, intAnio, "La Fecha de nacimiento no es valida"))) {
                    return false;
                }

                if (ComparaFechas(intDia, intMes, intAnio, dtmHoy.getDate(), dtmHoy.getMonth(), dtmHoy.getFullYear()) === 1) {
                    alert("La Fecha de nacimiento no puede ser mayor a la fecha actual");
                    return false;
                }

                if ($(eNacimientoInput).val() === "") {
                    alert("Seleeciona una entidad de nacimiento");
                    $(eNacimientoInput).focus();
                    return false;
                }
                return true;
            }

            function trim(inputString) {
                if (typeof inputString !== "string") {
                    return inputString;
                }
                var retValue = inputString;

                var ch = retValue.substring(0, 1);
                while (ch === " ") {
                    retValue = retValue.substring(1, retValue.length);
                    ch = retValue.substring(0, 1);
                }

                ch = retValue.substring(retValue.length - 1, retValue.length);
                while (ch === " ") {
                    retValue = retValue.substring(0, retValue.length - 1);
                    ch = retValue.substring(retValue.length - 1, retValue.length);
                }

                while (retValue.indexOf("  ") !== -1) {
                    retValue = retValue.substring(0, retValue.indexOf("  ")) +
                            retValue.substring(retValue.indexOf("  ") + 1, retValue.length);
                }
                return retValue;
            }


            function ValidaFecha(dia, mes, anio, mensaje) {
                var fecha = new Date(anio, mes, dia);

                if (fecha.getYear() < 100 || fecha.getYear() >= 2000)
                    var tmp_anio = (fecha.getYear() < 100) ? 1900 + fecha.getYear() : fecha.getYear();

                else if (fecha.getYear() >= 100 && fecha.getYear() < 200)
                    var tmp_anio = 1900 + fecha.getYear();
                else
                    var tmp_anio = fecha.getYear();

                var fecha1 = dia + "/" + mes + "/" + anio;
                var fecha2 = fecha.getDate() + "/" + fecha.getMonth() + "/" + tmp_anio;

                if (fecha1 !== fecha2) {
                    alert(mensaje);
                    return false;
                }
                return true;
            }

            function ComparaFechas(dia1, mes1, anio1, dia2, mes2, anio2) {
                var fecha1 = new Date(anio1, mes1, dia1);
                var fecha2 = new Date(anio2, mes2, dia2);

                if (fecha1.getTime() === fecha2.getTime()) {
                    return (0);
                }
                else if (fecha1.getTime() < fecha2.getTime()) {
                    return (-1);
                }
                else {
                    return (1);
                }
            }

            function validaCURP(curp) {

                var reg = "";

                if (curp.length === 18) {
                    var digito = calculaDigito(curp);

                    reg = /[A-Z]{4}\d{6}[HM][A-Z]{2}[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9][0-9]/;

                    if (curp.search(reg)) {
                        alert("La curp: " + curp + " no es valida, verifiqué ");
                        return false;
                    }

                    if (!(parseInt(digito) === parseInt(curp.substring(17, 18)))) {
                        alert("La curp: " + curp + " no es valida, revisé el Digito Verificador (" + digito + ")");
                        return false;
                    }
                    return true;
                } else {
                    alert("La curp debe tener 18 caractéres, verifiqué ");
                    $(curpInput).focus();
                    return false;
                }
            }

            function calculaDigito(curp) {
                var segRaiz = curp.substring(0, 17);
                var chrCaracter = "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
                var intFactor = new Array(17);
                var lngSuma = 0.0;
                var lngDigito = 0.0;

                for (var i = 0; i < 17; i++) {
                    for (var j = 0; j < 37; j++) {
                        if (segRaiz.substring(i, i + 1) === chrCaracter.substring(j, j + 1)) {
                            intFactor[i] = j;
                        }
                    }
                }

                for (var k = 0; k < 17; k++) {
                    lngSuma = lngSuma + ((intFactor[k]) * (18 - k));
                }

                lngDigito = (10 - (lngSuma % 10));

                if (lngDigito === 10) {
                    lngDigito = 0;
                }

                return lngDigito;
            }

        });

    };


}(jQuery));