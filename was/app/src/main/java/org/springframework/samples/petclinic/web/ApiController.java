package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;

/** Minimal JSON API used by the separate WEB frontend. */
@RestController
@RequestMapping("/api")
public class ApiController {
    private final ClinicService clinicService;

    @Autowired
    public ApiController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        return result;
    }

    @GetMapping("/vets")
    public Collection<Vet> vets() {
        return clinicService.findVets();
    }

    @GetMapping("/owners")
    public List<Map<String, Object>> owners(@RequestParam(value = "lastName", defaultValue = "") String lastName) {
        Collection<Owner> owners = clinicService.findOwnerByLastName(lastName);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Owner owner : owners) {
            result.add(ownerSummary(owner));
        }
        return result;
    }

    @GetMapping("/owners/{id}")
    public ResponseEntity<Map<String, Object>> owner(@PathVariable("id") int id) {
        Owner owner = clinicService.findOwnerById(id);
        return owner == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(ownerSummary(owner));
    }

    private Map<String, Object> ownerSummary(Owner owner) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", owner.getId());
        result.put("firstName", owner.getFirstName());
        result.put("lastName", owner.getLastName());
        result.put("address", owner.getAddress());
        result.put("city", owner.getCity());
        result.put("telephone", owner.getTelephone());
        return result;
    }
}
