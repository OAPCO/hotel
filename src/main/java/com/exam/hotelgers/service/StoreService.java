package com.exam.hotelgers.service;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class StoreService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final DistRepository distRepository;
    private final BrandRepository brandRepository;
    private final SearchService searchService;
    private final DetailmenuRepository detailmenuRepository;
    private final ManagerRepository managerRepository;
    private final RoomOrderRepository roomOrderRepository;
    private final DistService distService;



    //Application.properties에 선언한 파일이 저장될 경로
    @Value("${imgUploadLocation}")
    private String imgUploadLocation;

    //파일저장을 위한 클래스
    private final S3Uploader s3Uploader;



    public Long register(StoreDTO storeDTO,SearchDTO searchDTO, MultipartFile imgFile) throws Exception{


        log.info("수수료화긴@"+storeDTO.getCancelCharge());

        Optional<Dist> dist = distRepository.distCheckGet(searchDTO);
        Optional<Manager> manager = managerRepository.managerCheckGet(searchDTO);
        Optional<Brand> brand = brandRepository.brandCheckGet(searchDTO);


        if (!dist.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판 코드입니다.");
        }
        if (!brand.isPresent()) {
            throw new IllegalStateException("존재하지 않는 브랜드 코드입니다.");
        }
        if (!manager.isPresent()) {
            throw new IllegalStateException("존재하지 않는 매장주입니다.");
        }




        Optional<Store> temp = storeRepository
                .findByStoreCd(storeDTO.getStoreCd());

        if(temp.isPresent()) {
            throw new IllegalStateException("이미 존재하는 코드입니다.");
        }


//        Optional<Store> storeCheck = storeRepository.findByManagerId(storeDTO.getManagerId());
//
//        if(!storeCheck.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 코드입니다.");
//        }


        String originalFileName = imgFile.getOriginalFilename(); //저장할 파일명
        String newFileName = ""; //새로 만든 파일명

        if(originalFileName != null) { //파일이 존재하면
            newFileName = s3Uploader.upload(imgFile,imgUploadLocation);
        }

        storeDTO.setStoreimgName(newFileName); //새로운 파일명을 재등록


        //소속총판의 매장 수 하나 증가
        log.info("화긴@@" + dist.get().getDistIdx());
        distService.distStoreCountAdd(dist.get().getDistIdx());


        Store store = modelMapper.map(storeDTO, Store.class);


        if (storeDTO.getStorePType().equals("DIRECTSTORE")){
            store.setStorePType(StorePType.DIRECTSTORE);
        }
        if (storeDTO.getStorePType().equals("FRANCHISEE")){
            store.setStorePType(StorePType.FRANCHISEE);
        }

        if (storeDTO.getStoreStatus().equals("ON")){
            store.setStoreStatus(StoreStatus.ON);
        }
        if (storeDTO.getStoreStatus().equals("OFF")){
            store.setStoreStatus(StoreStatus.OFF);
        }


        switch (storeDTO.getStoreGrade()){
            case ONE : store.setStoreGrade(StoreGrade.ONE);
                break;
            case TWO : store.setStoreGrade(StoreGrade.TWO);
                break;
            case THREE : store.setStoreGrade(StoreGrade.THREE);
                break;
            case FOUR : store.setStoreGrade(StoreGrade.FOUR);
                break;
            case FIVE : store.setStoreGrade(StoreGrade.FIVE);
                break;
        }


        store.setDist(dist.get());
        store.setRoomCount(12);



        return storeRepository.save(store).getStoreIdx();
    }






    @Transactional
    public void imageModify(MultipartFile imgFile,Long storeIdx) throws Exception{


        String storeimgName = "";
        String originalFileName = imgFile.getOriginalFilename();

        if(originalFileName != null) {

            storeimgName = s3Uploader.upload(imgFile,imgUploadLocation);
        }

        storeRepository.storeImgModify(storeimgName,storeIdx);

    }








    private StoreDTO convertToDTO(Object[] result) {
        Store store = (Store) result[0];
        Brand brand = (Brand) result[1];
        Manager manager = (Manager) result[2];
        StoreDTO dto = modelMapper.map(store, StoreDTO.class);
        dto.setDistDTO(searchService.convertToDistDTO(store.getDist()));
        dto.setBrandDTO(modelMapper.map(brand, BrandDTO.class));
        dto.setManagerDTO(modelMapper.map(manager, ManagerDTO.class));
        return dto;
    }




    //로그인 총판장 id로 매장 list 찾기
    public Page<StoreDTO> list(Pageable pageable,Principal principal) throws Exception{

        String userId = principal.getName();

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeIdx"));

        Page<Object[]> stores = storeRepository.storeToBrand(page,userId);


        return stores.map(this::convertToDTO);
    }




    public Page<StoreDTO> listAll(Pageable pageable) throws Exception{

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeIdx"));

        Page<Store> store = storeRepository.findAll(pageable);


        return store.map(storeItem -> modelMapper.map(storeItem, StoreDTO.class));
    }





    public Page<StoreDTO> searchList(SearchDTO searchDTO, Pageable pageable,Principal principal) {

        String userId = principal.getName();

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeIdx"));


        Page<Object[]> stores = storeRepository.multiSearch(searchDTO,page,userId);

        return stores.map(this::convertToDTO);
    }





    public Page<StoreDTO> adminSearchList(SearchDTO searchDTO, Pageable pageable) {

        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "storeIdx"));


        Page<Object[]> stores = storeRepository.adminStoreSearch(searchDTO,page);

        return stores.map(this::convertToDTO);
    }







    public Store modify(StoreDTO storeDTO, MultipartFile imgFile) throws IOException {
        Long storeIdx = storeDTO.getStoreIdx();
        if (storeIdx == null) {
            throw new IllegalArgumentException("storeIdx must not be null.");
        }

        Store store = storeRepository.findById(storeIdx).orElseThrow(() -> new IllegalArgumentException("Invalid storeIdx."));

        // Process the image file
        if (imgFile != null && !imgFile.isEmpty()) {
            String originalFileName = imgFile.getOriginalFilename(); // Get the original file name
            String newFileName = s3Uploader.upload(imgFile, imgUploadLocation); // Upload the file and get the new file's name
            storeDTO.setStoreimgName(newFileName); // Set the new file's name to the storeDTO
        }
        store.setStoreimgName(storeDTO.getStoreimgName());

        // 'dist', 'distChief', 'brand'를 제외한 나머지 필드들을 업데이트합니다.
        store.setStoreGrade(storeDTO.getStoreGrade());

//        store.setStoreChief(storeDTO.getStoreChief());
//        store.setStoreChieftel(storeDTO.getStoreChieftel());

        store.setStoreCd(storeDTO.getStoreCd());
        store.setStoreName(storeDTO.getStoreName());

        store.setStoreTel(storeDTO.getStoreTel());
        store.setStoreAddr(storeDTO.getStoreAddr());
        store.setStoreAddrDetail(storeDTO.getStoreAddrDetail());

        store.setStoreStatus(storeDTO.getStoreStatus());
        store.setStoreSummary(storeDTO.getStoreSummary());

        store.setStoreIdx(storeDTO.getStoreIdx());

        // 변경된 엔티티를 저장합니다.
        storeRepository.save(store);

        return store;
    }

    public StoreDTO orderread(Long storeIdx) throws Exception {
        Optional<Store> optStore = storeRepository.findWithMenuCateByStoreIdx(storeIdx);

        if (!optStore.isPresent()) {
            throw new Exception("Store with storeIdx " + storeIdx + " not found");
        }

        Store store = optStore.get();
        List<MenuCate> menuCateList = store.getMenuCateList();
        List<MenuCate> fullMenuCateList = storeRepository.findWithDetailMenuByMenuCateList(menuCateList);

        // Replace store's menuCateList with fullMenuCateList
        store.setMenuCateList(fullMenuCateList);

        // Map Store to StoreDTO
        StoreDTO storeDTO = modelMapper.map(store, StoreDTO.class);

        return storeDTO;
    }
    public StoreDTO read(Long storeIdx) throws Exception {
        List<Object[]> resultList = storeRepository.storeBrandDistDistChiefManager(storeIdx);

        if (!resultList.isEmpty()) {
            Object[] array = resultList.get(0);
            Store store = (Store) array[0];
            Brand brand = (Brand) array[1]; // Brand 항목 추가
            Dist dist = (Dist) array[2];
            DistChief distChief = (DistChief) array[3];
            Manager manager = (Manager) array[4]; // Brand가 추가되면서 index가 변경되었습니다.

            StoreDTO dto = modelMapper.map(store, StoreDTO.class);
            dto.setDistDTO(searchService.convertToDistDTO(dist));

            dto.setRoomDTOList(searchService.convertToRoomDTOList(store.getRoomList()));

            List<MenuCateDTO> menuCateDTOList = searchService.convertToMenuCateDTOList(store.getMenuCateList());
            dto.setMenuCateDTOList(menuCateDTOList);

            for (MenuCateDTO menuCateDTO : menuCateDTOList) {
                List<Detailmenu> detailMenus = detailmenuRepository.findByMenuCateMenuCateIdx(menuCateDTO.getMenuCateIdx());
                List<DetailmenuDTO> detailMenuDTOList = searchService.convertToDetailMenuDTOList(detailMenus);
                menuCateDTO.setDetailMenuDTOList(detailMenuDTOList);
            }

            ManagerDTO managerDTO = modelMapper.map(manager, ManagerDTO.class);
            dto.setManagerDTO(managerDTO);

            BrandDTO brandDTO = modelMapper.map(brand, BrandDTO.class); // BrandDTO를 생성합니다.
            dto.setBrandDTO(brandDTO); // DTO에 넣어줍니다.

            return dto;
        } else {
            return null;
        }
    }





    //총판,브랜드로 매장 찾기
    public List<StoreDTO> distbrandOfStore(SearchDTO searchDTO) {

        List<Store> stores = storeRepository.distbrandOfStore(searchDTO);
        return stores.stream()
                .map(store -> modelMapper.map(store, StoreDTO.class))
                .collect(Collectors.toList());
    }



    public List<StoreDTO> searchStoreDistChiefId(Principal principal) {

        List<Store> stores = storeRepository.searchStoreDistChiefId(principal.getName());
        return stores.stream()
                .map(store -> modelMapper.map(store, StoreDTO.class))
                .collect(Collectors.toList());
    }
    
    


    public void delete(Long storeIdx){
        storeRepository.deleteById(storeIdx);
    }



    public List<StoreDTO> getRandomRecommendedStores() {
        List<Store> randomStores = storeRepository.findRandomStores();
        return randomStores.stream()
                .map(store -> modelMapper.map(store, StoreDTO.class))
                .collect(Collectors.toList());
    }

    public StoreDTO searchStoreuserId(Principal principal){

        Optional<Store> store = storeRepository.searchStoreuserId(principal.getName());

        return modelMapper.map(store,StoreDTO.class);
    }


    public StoreDTO findCheckinStore(Long roomorderIdx){

        Optional<Store> store = storeRepository.findCheckinStore(roomorderIdx);

        return modelMapper.map(store,StoreDTO.class);
    }



    @Transactional
    public void storeSummaryUpdate(String storeSummary, Long storeIdx) {
        storeRepository.storeSummaryModify(storeSummary, storeIdx);
    }

    @Transactional
    public void cancelChargeUpdate(double cancelCharge, Long storeIdx) {
        log.info("cancelChargeUpdate 실행됨"+cancelCharge);
        storeRepository.cancelChargeModify(cancelCharge, storeIdx);
    }


    @Transactional
    public void storeMessageUpdate(String storeMessage, Long storeIdx) {
        storeRepository.storeMessageModify(storeMessage, storeIdx);
    }


    @Transactional
    public void storeCheckinTimeUpdate(String storeCheckinTime, Long storeIdx) {
        storeRepository.storeCheckinTimeModify(storeCheckinTime, storeIdx);
    }

    @Transactional
    public void storeCheckoutTimeUpdate(String storeCheckoutTime, Long storeIdx) {
        storeRepository.storeCheckoutTimeModify(storeCheckoutTime, storeIdx);
    }



    //룸카드 추가 메소드
    @Transactional
    public void roomCardAdd(Long storeIdx) {
        storeRepository.roomCardAdd(storeIdx);
    }




}