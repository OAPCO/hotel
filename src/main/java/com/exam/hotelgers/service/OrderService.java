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

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final StoreRepository storeRepository;
    private final RoomRepository roomRepository;
    private final SearchService searchService;






    //등록
    public Long register(OrderDTO orderDTO) {


        Optional<Dist> dist = distRepository.findByDistCd(orderDTO.getDistDTO().getDistCd());
        Optional<Store> store = storeRepository.findByStoreCd(orderDTO.getStoreDTO().getStoreCd());
        Optional<Room> room = roomRepository.findByRoomCd(orderDTO.getRoomDTO().getRoomCd());


        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }
        if (!store.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장 코드입니다.");
        }
        if (!room.isPresent()) {
            throw new IllegalStateException("존재하지 않는 룸 코드입니다.");
        }




        Optional<Order> temp = orderRepository.findByOrderCd(orderDTO.getOrderCd());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }


        Order order = modelMapper.map(orderDTO, Order.class);


        order.setDist(dist.get());
        order.setStore(store.get());
        order.setRoom(room.get());


        modelMapper.map(store.get(), orderDTO.getStoreDTO());
        modelMapper.map(dist.get(), orderDTO.getDistDTO());
        modelMapper.map(room.get(), orderDTO.getRoomDTO());



        return orderRepository.save(order).getOrderIdx();
    }

    

    //수정
    public void modify(OrderDTO orderDTO){


        Optional<Order> temp = orderRepository
                .findByOrderIdx(orderDTO.getOrderIdx());

        if(temp.isPresent()) {

            Order order = modelMapper.map(orderDTO, Order.class);
            orderRepository.save(order);
        }

    }



    //삭제
    public void delete(Long orderIdx){
        orderRepository.deleteById(orderIdx);
    }



    public OrderDTO read(Long orderIdx) {
        Optional<Order> orderOptional = orderRepository.findById(orderIdx);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return convertToDTO(order);
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










    public Page<OrderDTO> searchList(SearchDTO searchDTO, Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "orderIdx"));

        Page<Order> orders = orderRepository.multiSearch(searchDTO, page);
        return orders.map(this::convertToDTO);
    }



    public Page<OrderDTO> searchOrderList(String distName,String orderName, String orderCd, String roomCd, Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "orderIdx"));

        Page<Order> orders = orderRepository.orderListSearch(distName, orderName, orderCd, roomCd, page);
        return orders.map(this::convertToDTO);
    }



    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        dto.setDistDTO(searchService.convertToDistDTO(order.getDist()));
        dto.setStoreDTO(searchService.convertToStoreDTO(order.getStore()));
        dto.setRoomDTO(searchService.convertToRoomDTO(order.getRoom()));
        return dto;
    }





//    public List<StoreDTO> managerOfStoreList(Principal principal) {
//
//        String userId = principal.getName();
//        List<Store> stores = storeRepository.findByDistChief_DistChiefId(userId);
//
//        return dists.stream()
//                .map(dist -> modelMapper.map(dist, DistDTO.class))
//                .collect(Collectors.toList());
//    }



}
