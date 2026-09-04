package com.universidad.confudes.asistencia;

import com.universidad.confudes.externo.qrcheck.QRCheckClient;
import com.universidad.confudes.externo.qrcheck.QRCheckRequest;
import com.universidad.confudes.externo.qrcheck.QRCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class AdaptadorQRCheck implements ServicioAsistencia {

    private final QRCheckClient qrCheckClient;

    public AdaptadorQRCheck() {
        this.qrCheckClient = new QRCheckClient();
    }

    @Override
    public ResultadoCheckIn registrarAsistencia(String eventoId, String participanteId, String credencialQR) {
        long idEventoParsed;
        try {
            idEventoParsed = Long.parseLong(eventoId.replaceAll("\\D+", ""));
            if (idEventoParsed == 0) idEventoParsed = 1L;
        } catch (NumberFormatException e) {
            idEventoParsed = 1L;
        }

        String payload = credencialQR.startsWith("QR-") ? credencialQR : "QR-" + credencialQR;

        QRCheckRequest request = new QRCheckRequest(payload, idEventoParsed);
        QRCheckResponse response = qrCheckClient.validar(request);

        boolean esExitoso = response.getCodigoRespuesta() == 200;
        return new ResultadoCheckIn(esExitoso, response.getDetalle());
    }
}