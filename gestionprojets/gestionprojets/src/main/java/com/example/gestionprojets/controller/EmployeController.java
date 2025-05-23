package com.example.gestionprojets.controller;

import com.example.gestionprojets.model.Employe;
import com.example.gestionprojets.repository.EmployeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employes")
public class EmployeController {
    private final EmployeRepository employeRepository;

    public EmployeController(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    @PostMapping
    public Employe createEmploye(@RequestBody Employe employe) {
        return employeRepository.save(employe);
    }

    @GetMapping("/{id}")
    public Employe getEmployeById(@PathVariable Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
    }

    @PutMapping("/{id}")
    public Employe updateEmploye(@PathVariable Long id, @RequestBody Employe employeDetails) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        employe.setNom(employeDetails.getNom());
        employe.setEmail(employeDetails.getEmail());
        employe.setRole(employeDetails.getRole());
        employe.setEquipe(employeDetails.getEquipe());
        return employeRepository.save(employe);
    }

    @DeleteMapping("/{id}")
    public void deleteEmploye(@PathVariable Long id) {
        employeRepository.deleteById(id);
    }
}