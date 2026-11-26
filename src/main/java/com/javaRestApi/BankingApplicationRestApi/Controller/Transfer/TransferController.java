
package com.javaRestApi.BankingApplicationRestApi.Controller.Transfer;

import com.javaRestApi.BankingApplicationRestApi.Model.TransferAmount.TransferDTO;
import com.javaRestApi.BankingApplicationRestApi.Model.TransferAmount.TransferRequestDTO;
import com.javaRestApi.BankingApplicationRestApi.Model.TransferAmount.TransferResponseDTO;
import com.javaRestApi.BankingApplicationRestApi.service.TransferService.TransferService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDTO> transfer(
            @RequestBody TransferRequestDTO requestDTO) {

        TransferDTO dto = service.transfer(requestDTO);

        TransferResponseDTO responseDTO = new TransferResponseDTO();

        responseDTO.setFromAccountNumber(dto.getFromAccountNumber());
        responseDTO.setToAccountNumber(dto.getToAccountNumber());
        responseDTO.setAmount(dto.getAmount());
        responseDTO.setStatusCode(201);
        responseDTO.setError(false);
        responseDTO.setMsg("Successfully transferred the amount");

        return new ResponseEntity<>(
                responseDTO,
                HttpStatus.CREATED
        );
    }
}
