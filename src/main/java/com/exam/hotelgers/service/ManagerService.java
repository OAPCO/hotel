package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.exam.hotelgers.entity.QDist.dist;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final AdminRepository adminRepository;
    private final DistChiefRepository distChiefRepository;
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final DistRepository distRepository;
    private final RoomRepository roomRepository;



    public Long register(ManagerDTO managerDTO){


        List<String> adminIdCheck = adminRepository.registerCheck(managerDTO.getManagerId());
        List<String> distChiefIdCheck = distChiefRepository.registerCheck(managerDTO.getManagerId());
        List<String> managerIdCheck = managerRepository.registerCheck(managerDTO.getManagerId());


        if(!adminIdCheck.isEmpty() || !distChiefIdCheck.isEmpty() || !managerIdCheck.isEmpty()) {
            throw new IllegalStateException("중복된 아이디가 있습니다.");
        }

        Optional<Dist> dist = distRepository.findByDistName(managerDTO.getDistDTO().getDistName());

        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 이름입니다.");
        }



        String password = passwordEncoder.encode(managerDTO.getPassword());
        Manager manager = modelMapper.map(managerDTO, Manager.class);




        if(managerDTO.getRoleType().equals(RoleType.MANAGER)){
            manager.setRoleType(RoleType.MANAGER);
        }


        manager.setPassword(password);
        manager.setDist(dist.get());


        return managerRepository.save(manager).getManagerIdx();
    }





    public void delete(Long managerIdx){
        managerRepository.deleteById(managerIdx);
    }


    public ManagerDTO read(Long managerIdx){

        Optional<Manager> temp= managerRepository.findById(managerIdx);

        return modelMapper.map(temp,ManagerDTO.class);
    }


    public Page<ManagerDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"managerIdx"));

        Page<Manager> managers = managerRepository.findAll(page);


        Page<ManagerDTO> ManagerDTOS = managers.map(data->modelMapper.map(data,ManagerDTO.class));

        return ManagerDTOS;
    }



    public List<ManagerDTO> distOfManager(SearchDTO searchDTO) {

        List<Manager> managers = managerRepository.distOfManager(searchDTO);
        return managers.stream()
                .map(manager -> modelMapper.map(manager, ManagerDTO.class))
                .collect(Collectors.toList());
    }


    public StoreDTO managerOfStore(Principal principal) {

        String userId = principal.getName();
        Optional<Store> store = storeRepository.managerToStoreSearch(userId);

        return modelMapper.map(store.get(),StoreDTO.class);
    }

    public DistDTO managerOfDist(Principal principal) {

        String userId = principal.getName();
        Optional<Dist> dist = distRepository.findByManagerList_ManagerId(userId);

        return modelMapper.map(dist.get(),DistDTO.class);
    }





    //현재 로그인중인 매니저의 아이디로 소속 매장을 구한 뒤 매장의 코드를 이용해 room과 조인하여 보유 객실목록을 가져온다.
    public List<RoomDTO> managerOfLoom(Principal principal) {

        String userId = principal.getName();
        Optional<Store> store = storeRepository.managerToStoreSearch(userId);

        StoreDTO storeDTO = modelMapper.map(store.get(),StoreDTO.class);


        List<Room> rooms = roomRepository.loginManagerRoomSearch(storeDTO.getStoreCd());


        List<RoomDTO> roomDTOS = rooms.stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());

        return roomDTOS;
    }





}
