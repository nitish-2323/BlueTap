
package com.javaRestApi.BankingApplicationRestApi.Controller.Customer;

import com.javaRestApi.BankingApplicationRestApi.Model.CustomerDTO.CustomerDto;
import com.javaRestApi.BankingApplicationRestApi.Model.CustomerDTO.ResponseDTO;
import com.javaRestApi.BankingApplicationRestApi.service.Mainservice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class MainController {

    private final Mainservice mainservice;

    public MainController(Mainservice mainservice) {
        this.mainservice = mainservice;
    }

    @PostMapping("/addUser/{customerId}")
    public ResponseEntity<ResponseDTO> addUser(
            @RequestBody CustomerDto dto,
            @PathVariable long customerId) {

        CustomerDto obj = mainservice.save(dto, customerId);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(false);
        responseDTO.setMsg("Created successfully");
        responseDTO.setStatusCode(HttpStatus.CREATED.value());

        List<CustomerDto> list = new ArrayList<>();
        list.add(toSafeCustomer(obj));

        responseDTO.setMydtos(list);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/userInfo/{customerId}")
    public ResponseEntity<ResponseDTO> userInfo(
            @PathVariable long customerId) {

        CustomerDto obj = mainservice.getInfo(customerId);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(false);
        responseDTO.setMsg("Customer information retrieved successfully");
        responseDTO.setStatusCode(HttpStatus.OK.value());

        List<CustomerDto> list = new ArrayList<>();
        list.add(toSafeCustomer(obj));

        responseDTO.setMydtos(list);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/userUpdate/{customerId}")
    public ResponseEntity<ResponseDTO> userUpdate(
            @RequestBody CustomerDto dto,
            @PathVariable long customerId) {

        CustomerDto obj = mainservice.updateObj(dto, customerId);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(false);
        responseDTO.setMsg("Customer updated successfully");
        responseDTO.setStatusCode(HttpStatus.OK.value());

        List<CustomerDto> list = new ArrayList<>();
        list.add(toSafeCustomer(obj));

        responseDTO.setMydtos(list);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/deleteUser/{customerId}")
    public ResponseEntity<ResponseDTO> deleteUser(
            @PathVariable long customerId) {

        CustomerDto obj = mainservice.delete(customerId);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setError(false);
        responseDTO.setMsg("Customer deleted successfully");
        responseDTO.setStatusCode(HttpStatus.OK.value());

        List<CustomerDto> list = new ArrayList<>();
        list.add(toSafeCustomer(obj));

        responseDTO.setMydtos(list);

        return ResponseEntity.ok(responseDTO);
    }

    private CustomerDto toSafeCustomer(CustomerDto customer) {

        CustomerDto safeCustomer = new CustomerDto();

        safeCustomer.setCustomerId(customer.getCustomerId());
        safeCustomer.setFullName(customer.getFullName());
        safeCustomer.setEmail(customer.getEmail());
        safeCustomer.setPhoneNumber(customer.getPhoneNumber());
        safeCustomer.setAddress(customer.getAddress());

        return safeCustomer;
    }
}
