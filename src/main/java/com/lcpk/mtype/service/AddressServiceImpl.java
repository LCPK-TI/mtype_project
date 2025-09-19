package com.lcpk.mtype.service;

import com.lcpk.mtype.entity.AddressEntity;
import com.lcpk.mtype.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<AddressEntity> list(Long userNo) {
        return repo.findByUserNoOrderByAddrDefaultDescAddrNoDesc(userNo);
    }

    @Override
    @Transactional
    public AddressEntity create(Long userNo, AddressEntity src) {
        src.setUserNo(userNo);

        // 새 배송지를 기본배송지로 지정하면 기존 기본을 해제(INSERT 한 번만 해도 되게)
        if (src.isAddrDefault()) {
            unsetDefault(userNo);
        }
        // 처음 배송지 등록이면 자동 기본배송지 지정
        if (!src.isAddrDefault() && repo.findByUserNoAndAddrDefaultTrue(userNo).isEmpty()) {
            src.setAddrDefault(true);
        }
        return repo.save(src);
    }

    @Override
    @Transactional
    public AddressEntity update(Long userNo, Long addrNo, AddressEntity src) {
        AddressEntity dst = repo.findById(addrNo).orElseThrow();
        if (!dst.getUserNo().equals(userNo)) throw new IllegalArgumentException("user_no 불일치");

        dst.setAddrName(src.getAddrName());
        dst.setRecipient(src.getRecipient());
        dst.setReciPhone(src.getReciPhone());
        dst.setAddrMain(src.getAddrMain());
        dst.setAddrDetail(src.getAddrDetail());

        if (src.isAddrDefault()) {
            unsetDefault(userNo);
            dst.setAddrDefault(true);
        }
        return dst;
    }

    @Override
    @Transactional
    public void delete(Long userNo, Long addrNo) {
        AddressEntity row = repo.findById(addrNo).orElseThrow();
        if (!row.getUserNo().equals(userNo)) throw new IllegalArgumentException("user_no 불일치");

        boolean wasDefault = row.isAddrDefault();
        repo.delete(row);

        // 기본 배송지 삭제시 남은 배송지 중 하나를 기본배송지
        if (wasDefault) {
            List<AddressEntity> rest = repo.findByUserNoOrderByAddrDefaultDescAddrNoDesc(userNo);
            if (!rest.isEmpty()) rest.get(0).setAddrDefault(true);
        }
    }

    private void unsetDefault(Long userNo) {
        repo.findByUserNoAndAddrDefaultTrue(userNo)
            .forEach(a -> a.setAddrDefault(false));
    }
}
