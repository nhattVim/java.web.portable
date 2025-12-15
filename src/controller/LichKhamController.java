package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import src.entity.LichKham;
import src.service.LichKhamService;
import src.service.PetService;

@Controller
@RequiredArgsConstructor
public class LichKhamController {

    final LichKhamService lichKhamService;
    final PetService petService;

    @GetMapping("/lich-kham")
    public String index(Model model) {
        model.addAttribute("lichKhamList", lichKhamService.getAllLichKham());
        model.addAttribute("petList", petService.getAllPets());
        model.addAttribute("lichKham", new LichKham());
        return "lich-kham";
    }

    @PostMapping("/lich-kham/add")
    public String addLichKham(LichKham lichKham, @RequestParam("maPetSelect") String petId) {
        lichKhamService.addLichKham(lichKham, petId);
        return "redirect:/lich-kham";
    }

    @GetMapping("/lich-kham/search")
    public String deleteLichKham(Model model, @RequestParam("search") String search) {
        model.addAttribute("lichKhamList", lichKhamService.search(search));
        model.addAttribute("petList", petService.getAllPets());
        model.addAttribute("lichKham", new LichKham());
        return "lich-kham";
    }
}
