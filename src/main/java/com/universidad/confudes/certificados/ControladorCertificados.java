package com.universidad.confudes.certificados;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/certificados")
public class ControladorCertificados {

    private final ManejadorCertificadosFacade certificadosFacade;

    public ControladorCertificados(ManejadorCertificadosFacade certificadosFacade) {
        this.certificadosFacade = certificadosFacade;
    }

    @PostMapping("/{eventoId}/{participanteId}")
    public ResponseEntity<String> emitir(@PathVariable String eventoId,
                                         @PathVariable String participanteId,
                                         @RequestParam String nombre,
                                         @RequestParam String correoDestino) {
        boolean exito = certificadosFacade.emitirYEnviarCertificado(eventoId, participanteId, nombre, correoDestino);
        return exito ? ResponseEntity.ok("Certificado emitido y enviado")
                     : ResponseEntity.status(403).body("Asistencia insuficiente");
    }
}