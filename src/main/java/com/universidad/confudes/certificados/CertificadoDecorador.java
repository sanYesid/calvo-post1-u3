package com.universidad.confudes.certificados;

public abstract class CertificadoDecorador implements ServicioCertificados {
    protected final ServicioCertificados servicioEnvoltorio;

    public CertificadoDecorador(ServicioCertificados servicioEnvoltorio) {
        this.servicioEnvoltorio = servicioEnvoltorio;
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        return servicioEnvoltorio.emitir(solicitud);
    }
}