package src.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import src.entity.Pet;
import src.repository.PetRepository;

@Service
@RequiredArgsConstructor
public class PetService {

    final PetRepository petRepository;

    @Transactional(readOnly = true)
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Pet> search(String search) {
        return petRepository
                .findByMaPetContainingIgnoreCaseOrTenContainingIgnoreCaseOrTypeContainingIgnoreCaseOrSubTypeContainingIgnoreCase(
                        search, search, search, search);
    }

    @Transactional(readOnly = true)
    public Pet getPetById(String id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
    }

    @Transactional
    public void addPet(Pet pet) {
        petRepository.save(pet);
    }
}
