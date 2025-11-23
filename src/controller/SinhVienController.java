package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import src.entity.SinhVien;
import src.entity.TotNghiep;
import src.entity.TotNghiepId;
import src.service.NganhService;
import src.service.SinhVienService;
import src.service.TruongService;

@Controller
@RequiredArgsConstructor
public class SinhVienController {

    final SinhVienService sinhVienService;
    final TruongService truongService;
    final NganhService nganhService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("danhSachSinhVien", sinhVienService.getAllSinhVien());
        return "sinhvien";
    }

    @GetMapping("/sinhvien/add")
    public String addForm(Model model) {
        TotNghiep tn = new TotNghiep();
        tn.setId(new TotNghiepId());
        tn.setSinhVien(new SinhVien());
        model.addAttribute("totNghiep", tn);
        model.addAttribute("danhSachTruong", truongService.getAllTruong());
        model.addAttribute("danhSachNganh", nganhService.getAllNganh());
        return "add-sinhvien-form";
    }

    @PostMapping("/sinhvien/add")
    public String submitForm(@ModelAttribute("totNghiep") TotNghiep totNghiep) {
        totNghiep.getId().setSoCMND(totNghiep.getSinhVien().getSoCMND());
        totNghiep.setTruong(truongService.getTruongById(totNghiep.getId().getMaTruong()));
        totNghiep.setNganh(nganhService.getNganhById(totNghiep.getId().getMaNganh()));
        sinhVienService.addSinhVienVaTotNghiep(totNghiep.getSinhVien(), totNghiep);
        return "redirect:/";
    }

    @GetMapping("/sinhvien/search")
    public String search(Model model, @RequestParam("search") String search) {
        model.addAttribute("danhSachSinhVien", sinhVienService.search(search));
        return "sinhvien";
    }
}
