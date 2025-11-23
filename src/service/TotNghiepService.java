package src.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import src.dto.ThongTinSinhVienDTO;
import src.entity.CongViec;
import src.entity.TotNghiep;
import src.repository.CongViecRepository;
import src.repository.TotNghiepRepository;

@Service
@RequiredArgsConstructor
public class TotNghiepService {

    final TotNghiepRepository totNghiepRepository;
    final CongViecRepository congViecRepository;

    public List<ThongTinSinhVienDTO> getThongKe() {

        List<TotNghiep> listTN = totNghiepRepository.findAll();
        List<CongViec> listCV = congViecRepository.findAll();

        Map<String, CongViec> cvMap = listCV.stream()
                .collect(Collectors.toMap(
                        cv -> cv.getSinhVien().getSoCMND(),
                        cv -> cv,
                        (a, b) -> a));

        List<ThongTinSinhVienDTO> result = new ArrayList<>();

        for (TotNghiep tn : listTN) {
            String cmnd = tn.getSinhVien().getSoCMND();

            CongViec cv = cvMap.get(cmnd);

            ThongTinSinhVienDTO dto = new ThongTinSinhVienDTO();
            dto.setSoCMND(cmnd);
            dto.setHoTen(tn.getSinhVien().getHoTen());

            dto.setMaTruong(tn.getTruong().getMaTruong());
            dto.setMaNganh(tn.getNganh().getMaNganh());
            dto.setHeTN(tn.getHeTN());
            dto.setLoaiTN(tn.getLoaiTN());

            if (cv != null) {
                dto.setTenCongViec(cv.getTenCongViec());
                dto.setTenCongTy(cv.getTenCongTy());
                dto.setThoiGianLamViec(cv.getThoiGianLamViec());
            }

            result.add(dto);
        }

        return result;
    }

    public List<ThongTinSinhVienDTO> search(String search) {

        List<TotNghiep> listTN = totNghiepRepository
                .findBySinhVienSoCMNDContainingIgnoreCaseOrSinhVienHoTenContainingIgnoreCaseOrTruongMaTruongContainingIgnoreCaseOrNganhMaNganhContainingIgnoreCase(
                        search, search, search, search);

        List<CongViec> listCV = congViecRepository
                .findByTenCongViecContainingIgnoreCaseOrTenCongTyContainingIgnoreCase(
                        search, search);

        Set<String> allCMND = new HashSet<>();
        listTN.forEach(tn -> allCMND.add(tn.getSinhVien().getSoCMND()));
        listCV.forEach(cv -> allCMND.add(cv.getSinhVien().getSoCMND()));

        Map<String, TotNghiep> tnMap = listTN.stream()
                .collect(Collectors.toMap(
                        tn -> tn.getSinhVien().getSoCMND(),
                        tn -> tn,
                        (a, b) -> a));

        Map<String, CongViec> cvMap = listCV.stream()
                .collect(Collectors.toMap(
                        cv -> cv.getSinhVien().getSoCMND(),
                        cv -> cv,
                        (a, b) -> a));

        List<ThongTinSinhVienDTO> result = new ArrayList<>();

        for (String cmnd : allCMND) {

            ThongTinSinhVienDTO dto = new ThongTinSinhVienDTO();
            dto.setSoCMND(cmnd);

            TotNghiep tn = tnMap.get(cmnd);
            CongViec cv = cvMap.get(cmnd);

            if (tn != null) {
                dto.setHoTen(tn.getSinhVien().getHoTen());
                dto.setMaTruong(tn.getTruong().getMaTruong());
                dto.setMaNganh(tn.getNganh().getMaNganh());
                dto.setHeTN(tn.getHeTN());
                dto.setLoaiTN(tn.getLoaiTN());
            } else if (cv != null) {
                dto.setHoTen(cv.getSinhVien().getHoTen());
            }

            if (cv != null) {
                dto.setTenCongViec(cv.getTenCongViec());
                dto.setTenCongTy(cv.getTenCongTy());
                dto.setThoiGianLamViec(cv.getThoiGianLamViec());
            }

            result.add(dto);
        }

        return result;
    }
}
