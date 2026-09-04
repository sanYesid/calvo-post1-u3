package com.universidad.confudes.certificados;

public class CodigoQRDecorador extends CertificadoDecorador {
    private final String urlVerificacion;

    public CodigoQRDecorador(ServicioCertificados servicio, String urlVerificacion) {
        super(servicio);
        this.urlVerificacion = urlVerificacion;
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        byte[] pdfOriginal = super.emitir(solicitud);
        return UtilidadesPDF.insertarCodigoQR(pdfOriginal, urlVerificacion);
    }
}