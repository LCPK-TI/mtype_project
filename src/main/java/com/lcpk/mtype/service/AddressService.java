package com.lcpk.mtype.service;

import com.lcpk.mtype.entity.AddressEntity;
import java.util.List;

public interface AddressService {
    List<AddressEntity> list(Long userNo);
    AddressEntity create(Long userNo, AddressEntity src);
    AddressEntity update(Long userNo, Long addrNo, AddressEntity src);
    void delete(Long userNo, Long addrNo);
}