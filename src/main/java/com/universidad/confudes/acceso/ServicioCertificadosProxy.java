package com.universidad.confudes.acceso;

import com.universidad.confudes.certificados.ServicioCertificados;
import com.universidad.confudes.certificados.SolicitudCertificado;

public class ServicioCertificadosProxy implements ServicioCertificados {

    private final ServicioCertificados servicioReal;

    public ServicioCertificadosProxy(ServicioCertificados servicioReal) {
        this.servicioReal = servicioReal;
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        String rol = ContextoUsuario.rolActual();
        
        if (!"ORGANIZADOR".equalsIgnoreCase(rol) && !"ADMIN".equalsIgnoreCase(rol)) {
            throw new SecurityException("Acceso denegado: El usuario con rol '" + rol + "' no tiene permisos para realizar esta operación.");
        }
        
        return servicioReal.emitir(solicitud);
    }
}