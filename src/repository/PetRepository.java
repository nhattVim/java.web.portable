package src.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import src.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, String> {

    List<Pet> findByMaPetContainingIgnoreCaseOrTenContainingIgnoreCaseOrTypeContainingIgnoreCaseOrSubTypeContainingIgnoreCase(
            String search, String search2, String search3, String search4);
}
