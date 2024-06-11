package com.exam.hotelgers.Controller;


import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.QnaDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminIncomeController {



}

