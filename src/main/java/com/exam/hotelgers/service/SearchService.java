package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;




//다른 서비스와 컨트롤러의 검색 등에 도움을 주는 메소드를 모아놓은 곳입니다.
@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final BranchRepository branchRepository;
    private final BrandRepository brandRepository;
    private final OrderRepository orderRepository;
    private final RoomRepository roomRepository;




    //현재 테이블로 읽어낼 수 없는 상위 테이블의 목록 값을 읽어내기 위한 메소드.
    //자주 쓸 것이기때문에 별도로 만든 것입니다
    public List<DistDTO> distList() {
        List<Dist> dists = distRepository.findAll();
        return dists.stream()
                .map(dist -> modelMapper.map(dist, DistDTO.class))
                .collect(Collectors.toList());
    }

    public List<BranchDTO> branchList() {
        List<Branch> branchs = branchRepository.findAll();
        return branchs.stream()
                .map(branch -> modelMapper.map(branch, BranchDTO.class))
                .collect(Collectors.toList());
    }

    public List<BrandDTO> brandList() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brand -> modelMapper.map(brand, BrandDTO.class))
                .collect(Collectors.toList());
    }

    public List<StoreDTO> storeList() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream()
                .map(store -> modelMapper.map(store, StoreDTO.class))
                .collect(Collectors.toList());
    }

    public List<RoomDTO> roomList() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> orderList() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }



    public List<OrderDTO> convertToOrderDTOList(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public List<RoomDTO> convertToRoomDTOList(List<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            return Collections.emptyList();
        }
        return rooms.stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());
    }





    //각 엔티티들을 DTO로 매핑하는 모델매퍼를 간략하게 사용하기 위한 메소드를 모아놓은 것.
    public DistDTO convertToDistDTO(Dist dist) {
        return modelMapper.map(dist, DistDTO.class);
    }

    public BrandDTO convertToBrandDTO(Brand brand) {
        return modelMapper.map(brand, BrandDTO.class);
    }

    public BranchDTO convertToBranchDTO(Branch branch) {
        return modelMapper.map(branch, BranchDTO.class);
    }

    public StoreDTO convertToStoreDTO(Store store) {
        return modelMapper.map(store, StoreDTO.class);
    }

    public RoomDTO convertToRoomDTO(Room room) {
        return modelMapper.map(room, RoomDTO.class);
    }





}
