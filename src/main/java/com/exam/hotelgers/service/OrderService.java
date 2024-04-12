package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.BranchDTO;
import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.OrderDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final BranchRepository branchRepository;
    private final BrandRepository brandRepository;
    private final StoreRepository storeRepository;



    public Long register(OrderDTO orderDTO) {


//        Optional<Dist> dist = distRepository.findByOrderDistIdx(orderDTO.getOrderDistDTO().getOrderDistIdx());
//        Optional<Branch> branch = branchRepository.findByOrderBranchIdx(orderDTO.getOrderBranchDTO().getOrderBranchIdx());
//        Optional<Brand> brand = brandRepository.findByBrandIdx(orderDTO.getBrandDTO().getBrandIdx());

        Optional<Dist> dist = distRepository.findByDistCd(orderDTO.getDistDTO().getDistCd());
        Optional<Branch> branch = branchRepository.findByBranchCd(orderDTO.getBranchDTO().getBranchCd());
        Optional<Brand> brand = brandRepository.findByBrandCd(orderDTO.getBrandDTO().getBrandCd());
        Optional<Store> store = storeRepository.findByStoreCd(orderDTO.getStoreDTO().getStoreCd());


        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }
        if (!branch.isPresent()) {
            throw new IllegalStateException("존재하지 않는 지사 코드입니다.");
        }
        if (!brand.isPresent()) {
            throw new IllegalStateException("존재하지 않는 브랜드 코드입니다.");
        }
        if (!store.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장 코드입니다.");
        }



        Optional<Order> temp = orderRepository
                .findByOrderCd(orderDTO.getOrderCd());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }



        Order order = modelMapper.map(orderDTO, Order.class);



        order.setDist(dist.get());
        order.setBranch(branch.get());
        order.setBrand(brand.get());



        orderRepository.save(order);

        return orderRepository.save(order).getOrderIdx();
    }



    public void modify(OrderDTO orderDTO){


        Optional<Order> temp = orderRepository
                .findByOrderIdx(orderDTO.getOrderIdx());

        if(temp.isPresent()) {

            Order order = modelMapper.map(orderDTO, Order.class);
            orderRepository.save(order);
        }

    }


//    public OrderDTO read(Long orderIdx){
//
//        Optional<Order> order= orderRepository.findById(orderIdx);
//
//
//        return modelMapper.map(order,OrderDTO.class);
//    }





    public OrderDTO read(Long orderIdx) {
        Optional<Order> orderEntityOptional = orderRepository.findById(orderIdx);
        if (orderEntityOptional.isPresent()) {
            Order order = orderEntityOptional.get();
            OrderDTO dto = modelMapper.map(order, OrderDTO.class);
            dto.setDistDTO(convertToOrderDistDTO(order.getDist()));
            dto.setBranchDTO(convertToOrderBranchDTO(order.getBranch()));
            dto.setBrandDTO(convertToBrandDTO(order.getBrand()));
            return dto;

        } else {
            return null;
        }
    }






    public Page<OrderDTO> list(Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "orderIdx"));

        Page<Order> orders = orderRepository.findAll(page);
        return orders.map(this::convertToDTO);
    }



    public Page<OrderDTO> searchList(String distName, String branchName, String orderName, StorePType storePType, StoreStatus storeStatus, Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "orderIdx"));

        Page<Order> orders = orderRepository.multiSearch(distName, branchName,orderName, storePType, storeStatus, page);
        return orders.map(this::convertToDTO);
    }



//        public Page<OrderDTO> list2(Pageable pageable, String distName) {
//
//            int currentPage = pageable.getPageNumber() - 1;
//            int pageCnt = 5;
//            Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "orderIdx"));
//
//        if(distName != null){
//            Page<Order> orders = orderRepository.distNameSearch(distName,page);
//            return orders.map(this::convertToDTO);
//        }
//
//        else {
//            Page<Order> orders = orderRepository.findAll(page);
//            return orders.map(this::convertToDTO);
//        }
//
//
//
//    }







    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        dto.setDistDTO(convertToOrderDistDTO(order.getDist()));
        dto.setBranchDTO(convertToOrderBranchDTO(order.getBranch()));
        dto.setBrandDTO(convertToBrandDTO(order.getBrand()));
        return dto;
    }

    private DistDTO convertToOrderDistDTO(Dist dist) {
        return modelMapper.map(dist, DistDTO.class);
    }

    private BranchDTO convertToOrderBranchDTO(Branch branch) {
        return modelMapper.map(branch, BranchDTO.class);
    }

    private BrandDTO convertToBrandDTO(Brand brand) {
        return modelMapper.map(brand, BrandDTO.class);
    }




    public void delete(Long orderIdx){
        orderRepository.deleteById(orderIdx);
    }
}
