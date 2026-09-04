package com.universidad.confudes.certificados;

public class MarcaDeAguaDecorador extends CertificadoDecorador {
    private final String textoMarca;

    public MarcaDeAguaDecorador(ServicioCertificados servicio, String textoMarca) {
        super(servicio);
        this.textoMarca = textoMarca;
    }

    @Override
    public byte[] emitir(SolicitudCertificado solicitud) {
        byte[] pdfOriginal = super.emitir(solicitud);
        return UtilidadesPDF.aplicarMarcaDeAgua(pdfOriginal, textoMarca);
    }
}