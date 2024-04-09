package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.service.RoomService;
import com.exam.hotelgers.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/register/{storeId}")
    public String register(@PathVariable String storeId) {
        // 여기서 storeId를 사용하여 필요한 작업을 수행합니다
        return "room/register";
    }


    @PostMapping("/register/{storeId}")
    public String registerProc(@Valid @RequestBody RoomDTO roomDTO,
                               @PathVariable Long storeId,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("Room registerProc arrived " + roomDTO);

        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/error";
        }

        roomService.register(storeId, roomDTO);

        redirectAttributes.addFlashAttribute("result", "Room registered");

        return "redirect:/room/list";
    }
}
