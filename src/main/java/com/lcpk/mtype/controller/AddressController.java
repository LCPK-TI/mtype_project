package com.lcpk.mtype.controller;

import com.lcpk.mtype.entity.AddressEntity;
import com.lcpk.mtype.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;
    private static final long FIXED_USER_NO = 2L;

    @GetMapping("/addresses")
    public String addressPage() {
        return "address-management";
    }

    // API 
    @GetMapping("/api/addresses") 
    @ResponseBody
    public List<AddressEntity> list() {
        return service.list(FIXED_USER_NO);
    }

    @PostMapping("/api/addresses") 
    @ResponseBody
    public ResponseEntity<AddressEntity> create(@Valid @RequestBody AddressReq req) {
        AddressEntity saved = service.create(FIXED_USER_NO, req.toEntity());
        return ResponseEntity.created(URI.create("/api/addresses/" + saved.getAddrNo()))
                             .body(saved);
    }

    @PutMapping("/api/addresses/{addrNo}") 
    @ResponseBody
    public AddressEntity update(@PathVariable("addrNo") Long addrNo,  
                                @Valid @RequestBody AddressReq req) {
        return service.update(FIXED_USER_NO, addrNo, req.toEntity());
    }

    @DeleteMapping("/api/addresses/{addrNo}") 
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable("addrNo") Long addrNo) { 
        service.delete(FIXED_USER_NO, addrNo);
        return ResponseEntity.noContent().build();
    }

  
    public record AddressReq(
            @NotBlank String name,
            @NotBlank String receiver,
            String phone,
            @NotBlank String addr1,
            String addr2,
            boolean isDefault
    ){
        public AddressEntity toEntity(){
            return AddressEntity.builder()
                    .addrName(name)
                    .recipient(receiver)
                    .reciPhone(phone)
                    .addrMain(addr1)
                    .addrDetail(addr2)
                    .addrDefault(isDefault)
                    .build();
        }
    }
}
