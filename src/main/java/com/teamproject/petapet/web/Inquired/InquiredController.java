package com.teamproject.petapet.web.Inquired;


import com.teamproject.petapet.domain.inquired.Inquired;
import com.teamproject.petapet.domain.member.Member;
import com.teamproject.petapet.web.Inquired.dto.InquiredSubmitDTO;
import com.teamproject.petapet.web.Inquired.service.InquiredService;
import com.teamproject.petapet.web.member.service.MemberService;
import com.teamproject.petapet.web.product.fileupload.FileService;
import com.teamproject.petapet.web.product.fileupload.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry")
public class InquiredController {

    private final InquiredService inquiredService;

    private final MemberService memberService;

    private final FileService fileService;

    public final String INQUIRED_CATEGORY1 = "문의";

    @GetMapping()
    public String getMyInquiry(@ModelAttribute("Inquired") InquiredSubmitDTO inquiredSubmitDTO,
                          Principal principal, HttpServletRequest request,
                          HttpSession httpSession, Model model){
        String loginMember = checkMember(principal, request, httpSession);
        List<Inquired> myInquiry = inquiredService.getMyInquired(loginMember);
        model.addAttribute("myInquiry", myInquiry);
        return "mypage/inquiry";
    }

//    @GetMapping
//    public String dd(){
//        return "mypage/inquiry";
//    }

    @PostMapping("/submit")
    public String inquirySubmit(@Validated
                                  @ModelAttribute("Inquired") InquiredSubmitDTO inquiredSubmitDTO,
                                   Principal principal, HttpServletRequest request, HttpSession httpSession) throws IOException {
        log.info("실행");
//        List<MultipartFile> inquiredImg = inquiredSubmitDTO.getInquiredImg();
//        List<UploadFile> uploadFiles = fileService.storeFiles(inquiredImg);
        String login = checkMember(principal, request, httpSession);
        Member loginMember = memberService.findOne(login);


        Inquired inquired = new Inquired(
                inquiredSubmitDTO.getTitle(),
                inquiredSubmitDTO.getInquiredContent(),
                INQUIRED_CATEGORY1,
                loginMember,
                inquiredSubmitDTO.getEmail(),
                false
        );

        inquiredService.inquiredSubmit(inquired);

        return "redirect:/";
    }


    private String checkMember(Principal principal, HttpServletRequest request, HttpSession httpSession) {
        httpSession.setAttribute("loginMember", memberService.findOne(principal.getName()));
        HttpSession session = request.getSession(false);
        Member loginMemberSession = (Member) session.getAttribute("loginMember");
        String loginMember = loginMemberSession.getMemberId();
        return loginMember;
    }
}