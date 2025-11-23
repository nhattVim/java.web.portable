package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import src.service.CongViecService;
import src.service.TotNghiepService;

@Controller
@RequiredArgsConstructor
public class TotNghiepController {

    final TotNghiepService totNghiepService;
    final CongViecService congViecService;

    @GetMapping("/totnghiep")
    public String index(Model model) {
        model.addAttribute("ds", totNghiepService.getThongKe());
        return "totnghiep";
    }

    @GetMapping("/totnghiep/search")
    public String search(Model model, @RequestParam("search") String search) {
        model.addAttribute("ds", totNghiepService.search(search));
        return "totnghiep";
    }
}
