
package com.javaRestApi.BankingApplicationRestApi.Controller;

import com.javaRestApi.BankingApplicationRestApi.security.AesGcmUtil;
import com.javaRestApi.BankingApplicationRestApi.Model.TransferAmount.TransferRequestDTO;
import com.javaRestApi.BankingApplicationRestApi.service.TransferService.TransferService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/encrypted")
public class EncryptedTransferController {

    private final TransferService transferService;
    private final AesGcmUtil aesGcmUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EncryptedTransferController(
            TransferService transferService,
            AesGcmUtil aesGcmUtil) {
        this.transferService = transferService;
        this.aesGcmUtil = aesGcmUtil;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferEncrypted(
            @RequestBody EncryptedPayload payload) {

        try {
            String json = aesGcmUtil.decryptFromBase64(payload.getData());

            TransferRequestDTO dto =
                    objectMapper.readValue(json, TransferRequestDTO.class);

            return ResponseEntity.ok(transferService.transfer(dto));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Invalid encrypted payload: " + e.getMessage());
        }
    }

    public static class EncryptedPayload {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
