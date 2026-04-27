<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <title>Registro exitoso de T&iacute;tulo electr&oacute;nico</title>
    </head>
    <body>
        <table width="100%" cellpadding="5" cellspacing="0" style="background-color: #FFF;">
            <tr>
                <td style="text-align: left">
                    <img src="cid:logoMorelos" width="120"/>
                </td>
                <td style="text-align: right">
                    <img src="cid:logoAnfitrion" width="120"/>
                </td>
            </tr>
        </table>

        <p style="text-align: justify;">

        <h3 style="text-align: center">
            T&Iacute;TULO ELECTR&Oacute;NICO REGISTRADO EXITOSAMENTE
        </h3>
        <br/><br/>
        Con base al "Decreto por el que se reforman y derogan diversas disposiciones del
        Reglamento de la Ley Reglamentaria del Art&iacute;culo 5 o. Constitucional, relativo al ejercicio de las
        profesiones en el Distrito Federal" y la Ley sobre el ejercicio de las profesiones en el Estado
        de Morelos Art&iacute;culo 6, me
        es grato informarle que su <b>T&iacute;tulo Electr&oacute;nico ha sido registrado exitosamente</b> ante la
        Direcci&oacute;n General de Profesiones de la Secretar&iacute;a de Educaci&oacute;n P&uacute;blica con los siguientes
        datos:
        <br/><br/>

        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>CURP:</b> ${tituloElectronicoInstance?.profesionista?.curp}<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Nombre:</b> ${tituloElectronicoInstance?.profesionista?.nombreCompleto}<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Carrera:</b> ${tituloElectronicoInstance?.carrera?.cveCarrera} - ${tituloElectronicoInstance?.carrera?.nombreCarrera} (RVOE: ${tituloElectronicoInstance?.carrera?.numeroRvoe})<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Instituci&oacute;n Educativa:</b> ${tituloElectronicoInstance?.institucion?.cveInstitucion} - ${tituloElectronicoInstance?.institucion?.nombreInstitucion}<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Folio:</b> ${tituloElectronicoInstance?.folioControl}<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Fecha y hora:</b> <g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${fechaRegistro}"/><br/>

        <br/>
        
        Por lo anterior se le invita a tramitar su C&eacute;dula Profesional Electr&oacute;nica en la siguiente direcci&oacute;n:
        <br/><br/>
        
        https://www.gob.mx/cedulaprofesional
        
        <br/><br/>
        
        No omito mencionar que para solicitar su C&eacute;dula Profesional Electr&oacute;nica es necesario contar con la Firma
        Electr&oacute;nica Avanzada, misma que deber&aacute; tramitar ante el SAT previa cita.
        
        <br/><br/>
        
        <b>
            Atentamente<br/>
            Departamento de Control Escolar del IEBEM<br/>
            777 102 01 12
        </b>

    </p>
    
    <br/>

    <img src="cid:plecaMorelos" width="100%"/>
    
</body>
</html>