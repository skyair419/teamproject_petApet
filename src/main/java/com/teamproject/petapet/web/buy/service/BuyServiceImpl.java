package com.teamproject.petapet.web.buy.service;


import com.teamproject.petapet.domain.buy.Buy;
import com.teamproject.petapet.domain.buy.BuyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BuyServiceImpl implements Buyservice{

    private final BuyRepository buyRepository;

    // 구매 목록
    @Override
    public List<Buy> findAll(String member) {
        return buyRepository.findBuyByMember(member);
    }

    // 장바구니 -> 구매
    @Override
    public Buy addBuy(Buy buy) {
        return buyRepository.save(buy);
    }

}