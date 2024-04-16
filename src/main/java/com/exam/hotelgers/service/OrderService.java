package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
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
    private final StoreRepository storeRepository;
    private final RoomRepository roomRepository;




    public Long register(OrderDTO orderDTO) {


        Optional<Dist> dist = distRepository.findByDistCd(orderDTO.getDistDTO().getDistCd());
        Optional<Branch> branch = branchRepository.findByBranchCd(orderDTO.getBranchDTO().getBranchCd());
        Optional<Store> store = storeRepository.findByStoreCd(orderDTO.getStoreDTO().getStoreCd());
        Optional<Room> room = roomRepository.findByRoomCd(orderDTO.getRoomDTO().getRoomCd());


        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }
        if (!branch.isPresent()) {
            throw new IllegalStateException("존재하지 않는 지사 코드입니다.");
        }
        if (!store.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장 코드입니다.");
        }
        if (!room.isPresent()) {
            throw new IllegalStateException("존재하지 않는 룸 코드입니다.");
        }




        Optional<Order> temp = orderRepository
                .findByOrderCd(orderDTO.getOrderCd());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }


        Order order = modelMapper.map(orderDTO, Order.class);


        order.setDist(dist.get());
        order.setBranch(branch.get());
        order.setStore(store.get());
        order.setRoom(room.get());


//        store.ifPresent(s -> orderDTO.getStoreDTO().setStoreName(s.getStoreName()));
        modelMapper.map(store.get(), orderDTO.getStoreDTO());
        modelMapper.map(branch.get(), orderDTO.getBranchDTO());
        modelMapper.map(dist.get(), orderDTO.getDistDTO());
        modelMapper.map(room.get(), orderDTO.getRoomDTO());


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





    public OrderDTO read(Long orderIdx) {
        Optional<Order> storeEntityOptional = orderRepository.findById(orderIdx);
        if (storeEntityOptional.isPresent()) {
            Order order = storeEntityOptional.get();
            OrderDTO dto = modelMapper.map(order, OrderDTO.class);
            dto.setDistDTO(convertToStoreDistDTO(order.getDist()));
            dto.setBranchDTO(convertToStoreBranchDTO(order.getBranch()));
            dto.setStoreDTO(convertToStoreDTO(order.getStore()));

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



    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        dto.setDistDTO(convertToStoreDistDTO(order.getDist()));
        dto.setBranchDTO(convertToStoreBranchDTO(order.getBranch()));
        dto.setStoreDTO(convertToStoreDTO(order.getStore()));
        return dto;
    }




    private DistDTO convertToStoreDistDTO(Dist dist) {
        return modelMapper.map(dist, DistDTO.class);
    }

    private BranchDTO convertToStoreBranchDTO(Branch branch) {
        return modelMapper.map(branch, BranchDTO.class);
    }

    private StoreDTO convertToStoreDTO(Store store) {
        return modelMapper.map(store, StoreDTO.class);
    }




    public void delete(Long orderIdx){
        orderRepository.deleteById(orderIdx);
    }
}
