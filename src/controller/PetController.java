package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import src.entity.Pet;
import src.service.CustomerService;
import src.service.LichKhamService;
import src.service.PetService;

@Controller
@RequiredArgsConstructor
public class PetController {

    final PetService petService;
    final LichKhamService lichKhamService;
    final CustomerService customerService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("petList", petService.getAllPets());
        model.addAttribute("pet", new Pet());
        model.addAttribute("customerList", customerService.getAllCustomer());
        return "pet";
    }

    @PostMapping("/pet/add")
    public String addPet(Pet pet) {
        petService.addPet(pet);
        return "redirect:/";
    }

    @GetMapping("/pet/search")
    public String search(Model model, @RequestParam("search") String search) {
        model.addAttribute("petList", petService.search(search));
        model.addAttribute("pet", new Pet());
        model.addAttribute("customerList", customerService.getAllCustomer());
        return "pet";
    }
}
