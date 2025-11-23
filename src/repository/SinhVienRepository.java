// package com.example.repository;
//
// import com.example.model.Product;
// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;
// import java.util.List;
// import org.springframework.stereotype.Repository;
//
// @Repository
// public class ProductRepository {
//
//     @PersistenceContext // Jakarta Persistence annotation
//     private EntityManager entityManager;
//
//     public Product findById(Long id) {
//         return entityManager.find(Product.class, id);
//     }
//
//     public List<Product> findAll() {
//         return entityManager.createQuery("select p from Product p", Product.class).getResultList();
//     }
//
//     public void save(Product product) {
//         if (product.getId() == null) {
//             entityManager.persist(product);
//         } else {
//             entityManager.merge(product);
//         }
//     }
// }
package src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import src.entity.SinhVien;

public interface SinhVienRepository extends JpaRepository<SinhVien, String> {

    List<SinhVien> findBySoCMNDContainingIgnoreCase(String soCMND);

    List<SinhVien> findByHoTenContainingIgnoreCase(String hoTen);

    List<SinhVien> findByEmailContainingIgnoreCase(String email);

    List<SinhVien> findBySoDTContainingIgnoreCase(String soDt);

    List<SinhVien> findByDiaChiContainingIgnoreCase(String diaChi);

    List<SinhVien> findBySoCMNDContainingIgnoreCaseOrHoTenContainingIgnoreCaseOrSoDTContainingIgnoreCaseOrDiaChiContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String search, String search2, String search3, String search4, String search5);
}
