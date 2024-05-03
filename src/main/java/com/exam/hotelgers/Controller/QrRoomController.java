package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Log4j2
public class QrRoomController {
    private final RoomService roomService;
    @GetMapping("/member/memberpage/{roomIdx}")
    public String readForm(@PathVariable Long roomIdx, Model model) {
        RoomDTO roomDTO = roomService.read(roomIdx);
        model.addAttribute("roomDTO", roomDTO);

        return "member/memberpage/qrroot}";
    }
}
