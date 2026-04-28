<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <base href="${createLink(uri:'/')}"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
    <asset:stylesheet src="application.css"/>
    <asset:stylesheet src="jquery-ui.css"/>
    <asset:javascript src="application.js"/>
    <asset:javascript src="jquery-ui.js"/>
    <asset:javascript src="dialog.js"/>
    <g:layoutHead/>
</head>
<body>
    <!--<div class="errors" style="margin-top: 0px; margin-left: 0px; margin-right: 0px; padding-left: 10px; padding-right: 10px;">
        <b>Esta es una instancia para capacitaci&oacute;n y pruebas</b>
    </div>-->
    <div style="padding: 20px;">
        <a href="${createLink(uri:'/')}">
            <img style="height: 50px;" src="https://cma.morelos.gob.mx/index/logo.jpg" alt="Secretaría de Educación"/>
        </a>
    </div>
    <div id="grailsLogo" role="banner" style="padding-left: 20px; padding-right: 20px; text-align: right; color: #FFF">
        <sec:ifLoggedIn>
            <sec:loggedInUserInfo field="username"/>
            <g:link controller="perfil" style="color: #FFF">Perfil de usuario</g:link>
            <g:link controller="logout" style="color: #FFF">Cerrar sesi&oacute;n</g:link>
            </sec:ifLoggedIn>
        </div>
    <g:layoutBody/>
    <div id="pleca"></div>
    <div class="footer" role="contentinfo">
        <b>CENTRO MORELENSE DE LAS ARTES (CMA)</b>
    </div>
    <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
</body>
</html>
