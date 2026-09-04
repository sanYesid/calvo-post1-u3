package com.universidad.confudes.certificados;

public class TraduccionInglesDecorador extends CertificadoDecorador {

    public TraduccionInglesDecorador(ServicioCertificados servicio) {
        super(servicio);
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        byte[] pdfOriginal = super.emitir(solicitud);
        return UtilidadesPDF.traducirAIngles(pdfOriginal);
    }
}