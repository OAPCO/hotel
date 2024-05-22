package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final BrandRepository brandRepository;
    private final MenuOrderRepository menuOrderRepository;
    private final RoomRepository roomRepository;
    private final MenuCateRepository menuCateRepository;
    private final DetailmenuRepository detailmenuRepository;
    private final MenuoptionRepository menuoptionRepository;
    private final AdminRepository adminRepository;
    private final DistChiefRepository distChiefRepository;
    private final ManagerRepository managerRepository;
    
    
    
    
    //날짜 변환 메소드(String을 LocalDateTime으로)
    public LocalDateTime changeDate(String dateString){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter).atStartOfDay();
    }


    public List<DistChiefDTO> distChiefList() {
        List<DistChief> distChiefs = distChiefRepository.findAll();
        return distChiefs.stream()
                .map(distChief -> modelMapper.map(distChief, DistChiefDTO.class))
                .collect(Collectors.toList());
    }


    public List<ManagerDTO> managerList() {
        List<Manager> managers = managerRepository.findAll();
        return managers.stream()
                .map(manager -> modelMapper.map(manager, ManagerDTO.class))
                .collect(Collectors.toList());
    }

    public List<DistDTO> distList() {
        List<Dist> dists = distRepository.findAll();
        return dists.stream()
                .map(dist -> modelMapper.map(dist, DistDTO.class))
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

    public List<MenuOrderDTO> orderList() {
        List<MenuOrder> orders = menuOrderRepository.findAll();
        return orders.stream()
                .map(order -> modelMapper.map(order, MenuOrderDTO.class))
                .collect(Collectors.toList());
    }


    public List<MenuCateDTO> menuCateList() {
        List<MenuCate> menuCates = menuCateRepository.findAll();
        return menuCates.stream()
                .map(menuCate -> modelMapper.map(menuCate, MenuCateDTO.class))
                .collect(Collectors.toList());
    }

    public List<DetailmenuDTO> detailmenuList() {
        List<Detailmenu> detailmenus = detailmenuRepository.findAll();
        return detailmenus.stream()
                .map(detailmenu -> modelMapper.map(detailmenu, DetailmenuDTO.class))
                .collect(Collectors.toList());
    }

    public List<MenuOptionDTO> menuOptionList(Long detailmenuIdx) {
        List<MenuOption> menuOptions = menuoptionRepository.findAll();
        return menuOptions.stream()
                .map(menuOption -> modelMapper.map(menuOption, MenuOptionDTO.class))
                .collect(Collectors.toList());
    }



    public List<MenuOrderDTO> convertToOrderDTOList(List<MenuOrder> orders) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        return orders.stream()
                .map(order -> modelMapper.map(order, MenuOrderDTO.class))
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


    public List<StoreDTO> convertToStoreDTOList(List<Store> stores) {
        if (stores == null || stores.isEmpty()) {
            return Collections.emptyList();
        }
        return stores.stream()
                .map(store -> modelMapper.map(store, StoreDTO.class))
                .collect(Collectors.toList());
    }


    public List<MenuCateDTO> convertToMenuCateDTOList(List<MenuCate> menuCates) {
        if (menuCates == null || menuCates.isEmpty()) {
            return Collections.emptyList();
        }
        return menuCates.stream()
                .map(menuCate -> modelMapper.map(menuCate, MenuCateDTO.class))
                .collect(Collectors.toList());
    }

    public List<DetailmenuDTO> convertToDetailMenuDTOList(List<Detailmenu> detailmenus) {
        if (detailmenus == null || detailmenus.isEmpty()) {
            return Collections.emptyList();
        }
        return detailmenus.stream()
                .map(detailmenu -> modelMapper.map(detailmenu, DetailmenuDTO.class))
                .collect(Collectors.toList());
    }

    public List<MenuOptionDTO> convertTomenuOptionDTOList(List<MenuOption> menuoptions) {
        if (menuoptions == null || menuoptions.isEmpty()) {
            return Collections.emptyList();
        }
        return menuoptions.stream()
                .map(menuoption -> modelMapper.map(menuoptions, MenuOptionDTO.class))
                .collect(Collectors.toList());
    }



    public DistDTO convertToDistDTO(Dist dist) {
        DistDTO distDTO = modelMapper.map(dist, DistDTO.class);

        // Dist에서 DistChief를 가져와서 DistChiefDTO로 변환
        if(dist.getDistChief() != null){
            DistChiefDTO distChiefDTO = modelMapper.map(dist.getDistChief(), DistChiefDTO.class);
            distDTO.setDistChiefDTO(distChiefDTO);
        }

        return distDTO;
    }

    public BrandDTO convertToBrandDTO(Brand brand) {
        return modelMapper.map(brand, BrandDTO.class);
    }



    public StoreDTO convertToStoreDTO(Store store) {
        return modelMapper.map(store, StoreDTO.class);
    }

    public RoomDTO convertToRoomDTO(Room room) {
        return modelMapper.map(room, RoomDTO.class);
    }


    public DistChiefDTO convertToDistChiefDTO(DistChief distChief) {
        return modelMapper.map(distChief, DistChiefDTO.class);
    }

    public ManagerDTO convertToManagerDTO(Manager manager) {
        return modelMapper.map(manager, ManagerDTO.class);
    }




    public String findByIdSendName(Principal principal){


        String userid = principal.getName();

        Optional<Admin> admin = adminRepository.findByAdminId(userid);
        Optional<DistChief> distChief = distChiefRepository.findByDistChiefId(userid);
        Optional<Manager> manager = managerRepository.findByManagerId(userid);


        if (admin.isPresent()) {
            String userName = admin.get().getAdminName();
            return userName;
        }
        else if (distChief.isPresent()) {
            String userName = distChief.get().getDistChiefName();
            return userName;
        }
        else if (manager.isPresent()) {
            String userName = manager.get().getManagerName();
            return userName;
        }
        else {
            return "로그인안했어요요요요용";
        }

    }


    public class RoomDetailsDto {
        private RoomDTO room;
        private StoreDTO store;
        private List<DetailmenuDTO> detailMenus;

        // getters, setters 등의 메소드들...
    }






}