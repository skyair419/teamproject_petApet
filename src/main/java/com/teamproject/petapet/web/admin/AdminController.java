package com.teamproject.petapet.web.admin;

import com.teamproject.petapet.domain.inquired.Inquired;
import com.teamproject.petapet.web.Inquired.dto.InquiredFAQDTO;
import com.teamproject.petapet.web.Inquired.service.InquiredService;
import com.teamproject.petapet.web.community.dto.CommunityRequestDTO;
import com.teamproject.petapet.web.community.service.CommunityService;
import com.teamproject.petapet.web.company.service.CompanyService;
import com.teamproject.petapet.web.member.service.MemberService;
import com.teamproject.petapet.web.product.service.ProductService;
import com.teamproject.petapet.web.report.dto.ReportTargetDTO;
import com.teamproject.petapet.web.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 박채원 22.10.09 작성
 */

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final InquiredService inquiredService;
    private final ProductService productService;
    private final CommunityService communityService;
    private final MemberService memberService;
    private final ReportService reportService;
    private final CompanyService companyService;

    @GetMapping("/adminPage")
    public String adminPage(Model model){

        model.addAttribute("notice", communityService.getNotice());
        model.addAttribute("otherInquiry", inquiredService.getOtherInquiries());
        model.addAttribute("communityReport", reportService.getReportCommunityList());
        model.addAttribute("memberReport", reportService.getReportMemberList());
        model.addAttribute("productReport", reportService.getReportProductList());
        model.addAttribute("product", productService.getProductList());
        model.addAttribute("community", communityService.getCommunityList());
        model.addAttribute("member", memberService.getMemberList());
        model.addAttribute("company", companyService.getCompanyList());

        return "/admin/adminMain";
    }

    // 뷰 확인
    @GetMapping("/{idx}/edit")
    public String inquiryView(@PathVariable("idx") Long inquiredId, Model model){
        Inquired inquiredList = inquiredService.findOne(inquiredId);
        model.addAttribute("inquiredList", inquiredList);
        log.info("뷰 완료!!");
        return "admin/inquiryView";
    }
//     뷰 업데이트
//    @PostMapping("/{idx}/edit")
//    public String updateCheckInquiry(@PathVariable("idx") Long inquiredId, @ModelAttribute InquiryDTO inquiryDTO){
//
//        inquiredService.setInquiredCheck(inquiredId);
//        log.info("수정 완료!!");
//        return "redirect:/admin/adminPage";
//    }

    @GetMapping("/registerNotice")
    public String registerNoticeForm(){
        return "/admin/registerNotice";
    }

    @PostMapping("/registerNotice") 
    public String registerNotice(CommunityRequestDTO.registerNotice registerNotice){
        communityService.registerNotice(registerNotice);
        return "redirect:/admin/adminPage";
    }

    @GetMapping("/updateNoticeForm/{noticeId}")
    public String getOneFAQ(@PathVariable("noticeId") Long noticeId, Model model){
        model.addAttribute("noticeInfo",communityService.getOneNotice(noticeId));
        return "/admin/updateNotice";
    }

    @PostMapping("/updateNotice")
    public String updateFAQ(CommunityRequestDTO.registerNotice registerNotice){
        communityService.updateNotice(registerNotice);
        return "redirect:/admin/adminPage";
    }

    @GetMapping("/deleteNotice/{noticeId}")
    public void deleteFAQ(@PathVariable("noticeId") Long noticeId){
        communityService.deleteNotice(noticeId);
    }

    @GetMapping("/registerProduct")
    public String registerProductForm(){
        return "/admin/registerProduct";
    }

    @PostMapping("/registerProduct")
    public String registerProduct(){
        return "redirect:/admin/adminPage";
    }

    @ResponseBody
    @RequestMapping(value = "/updateProductStatus/{productId}", method = RequestMethod.GET)
    public void updateProductStatus(@RequestParam Map<String, Object> map, @PathVariable("productId") Long productId){
        productService.updateProductStatus((String)map.get("status"), Long.valueOf((String) map.get("stock")), productId);
    }

    @GetMapping("/deleteCommunity/{communityId}")
    public void deleteCommunity(@PathVariable("communityId") Long communityId){
        communityService.deleteCommunity(communityId);
    }

    @ResponseBody
    @GetMapping("/disabledMember/{memberId}")
    public void disabledMember(@PathVariable("memberId") String memberId){
        memberService.updateMemberStopDate(memberId);
    }

    @GetMapping("/deleteMember/{memberId}")
    public void deleteMember(@PathVariable("memberId") String memberId){
        memberService.deleteMember(memberId);
    }

    @ResponseBody
    @GetMapping("/acceptCommunityReport/{reportId}")
    public void acceptCommunityReport(@PathVariable("reportId") Long reportId, @RequestParam("communityId") Long communityId){
        communityService.addCommunityReport(communityId);
        reportService.setResponseStatusCommunity(reportId);
    }

    @ResponseBody
    @GetMapping("/acceptMemberReport/{reportId}")
    public void acceptMemberReport(@PathVariable("reportId") Long reportId, @RequestParam("memberId") String memberId){
        memberService.addMemberReport(memberId);
        reportService.setResponseStatusCommunity(reportId);
    }

    @ResponseBody
    @GetMapping("/acceptProductReport/{reportId}")
    public void acceptProductReport(@PathVariable("reportId") Long reportId, @RequestParam("productId") Long productId){
        productService.addProductReport(productId);
        reportService.setResponseStatusCommunity(reportId);
    }

    @ResponseBody
    @GetMapping("/getGenderList")
    public int[] getGenderList(){
        return memberService.getGenderList();
    }

    @ResponseBody
    @GetMapping("/getAgeList")
    public List<Integer> getAgeList(){
        return memberService.getAgeList();
    }

    @ResponseBody
    @GetMapping("/setOutOfStock")
    public void setOutOfStock(@RequestParam(value="productIdList[]") List<String> productIdList){
        productService.updateProductStatusOutOfStock(productIdList);
    }

    @ResponseBody
    @GetMapping("/getReportReason/{id}/{type}")
    public HashMap<String, ReportTargetDTO> getReportReason(@PathVariable("id") Long id, @PathVariable("type") String type){
        HashMap<String, ReportTargetDTO> reportTarget = new HashMap<String, ReportTargetDTO>();
        reportTarget.put("report",reportService.getOneReport(id, type));
        return reportTarget;
    }

    @ResponseBody
    @PostMapping("/refuseReport/{reportId}")
    public void refuseReport(@PathVariable("reportId") Long reportId){
        reportService.refuseReport(reportId);
    }
}