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
    private final SearchService searchService;






    //등록
    public Long register(OrderDTO orderDTO) {


        //등록할 때 상위테이블의 값을 참조하는 필수정보들을 실제 값과 비교하고, 얻은 정보를 변수에 넣습니다.
        //각 레포지토리에서 만들어둔 메소드를 실행합니다.
        Optional<Dist> dist = distRepository.findByDistCd(orderDTO.getDistDTO().getDistCd());
        Optional<Branch> branch = branchRepository.findByBranchCd(orderDTO.getBranchDTO().getBranchCd());
        Optional<Store> store = storeRepository.findByStoreCd(orderDTO.getStoreDTO().getStoreCd());
        Optional<Room> room = roomRepository.findByRoomCd(orderDTO.getRoomDTO().getRoomCd());


        //Cd(코드) 값으로 조회를 했더니 해당하는 값이 없을 경우
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




        //사용자가 등록할 때 넣은 orderCd 값을 받아서 맞는 데이터가 있는지 확인합니다
        Optional<Order> temp = orderRepository.findByOrderCd(orderDTO.getOrderCd());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }


        //order 엔티티 order를 만들어서 orderDTO의 값을 모델매퍼를 이용하여 넣습니다.
        //DTO는 테이블과 같은 필드명으로 우리가 선언해놓은 임시로 쓰는 값인데, 그 값을 실제 테이블에 넣기 위함
        Order order = modelMapper.map(orderDTO, Order.class);


        //위에서 만든 order 엔티티에 맨 위에서 만든 각 테이블 정보 값들을 set 해서 집어넣습니다.
        order.setDist(dist.get());
        order.setBranch(branch.get());
        order.setStore(store.get());
        order.setRoom(room.get());


        //이건 테이블이 아니라 DTO에도 값을 넣기 위해 있는 부분.
        //각 테이블의 dto에도 값을 넣어서 뷰에서 사용하기 위해 넣었습니다.
        modelMapper.map(store.get(), orderDTO.getStoreDTO());
        modelMapper.map(branch.get(), orderDTO.getBranchDTO());
        modelMapper.map(dist.get(), orderDTO.getDistDTO());
        modelMapper.map(room.get(), orderDTO.getRoomDTO());



        //엔티티에 원하는 값들을 다 넣었으니 이제 save 해서 변경사항을 실제 테이블에 적용시킵니다.
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



    //조회
    //convertToDTO의 값을 order에 담아서 조회합니다. convertToDTO는 제일 하단에 있습니다.
    public OrderDTO read(Long orderIdx) {
        Optional<Order> orderOptional = orderRepository.findById(orderIdx);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return convertToDTO(order);
        } else {
            return null;
        }
    }





    //list(검색하기 전. 최초 페이지 or 페이지번호만 바뀐 페이지)
    public Page<OrderDTO> list(Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "orderIdx"));

        Page<Order> orders = orderRepository.findAll(page);
        return orders.map(this::convertToDTO);
    }



    //다중검색 후 목록 페이지.
    //order 레포지토리에서 만든 multisearch 메소드를 실행.
    //multisearch는 레포지토리에서 만들어뒀던 다중검색 쿼리문입니다.
    //반환할 때 맨 하단에 만들어둔 convertToDTO(필요한 다른 테이블들의 정보)도 함께 가져갑니다.
    public Page<OrderDTO> searchList(String distName, String branchName, String orderName, StorePType storePType, StoreStatus storeStatus, Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "orderIdx"));

        Page<Order> orders = orderRepository.multiSearch(distName, branchName,orderName, storePType, storeStatus, page);
        return orders.map(this::convertToDTO);
    }



    //다른 페이지에 필요한 다중 검색 메소드
    //위와 같은 방식입니다.
    public Page<OrderDTO> searchOrderList(String distName, String branchName, String orderName, String orderCd, String roomCd, Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "orderIdx"));

        Page<Order> orders = orderRepository.orderListSearch(distName, branchName,orderName, orderCd, roomCd, page);
        return orders.map(this::convertToDTO);
    }



    //SearchService에서 만들어 놓았던 매핑 된 DTO들을 orderDTO에 집어넣습니다.
    //검색,출력 등에 필요한 다른 테이블의 정보를 가져오기 위해 필요한 메소드입니다.
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        dto.setDistDTO(searchService.convertToDistDTO(order.getDist()));
        dto.setBranchDTO(searchService.convertToBranchDTO(order.getBranch()));
        dto.setStoreDTO(searchService.convertToStoreDTO(order.getStore()));
        dto.setRoomDTO(searchService.convertToRoomDTO(order.getRoom()));
        return dto;
    }



}
