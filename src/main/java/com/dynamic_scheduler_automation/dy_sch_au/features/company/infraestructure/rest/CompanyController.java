package com.dynamic_scheduler_automation.dy_sch_au.features.company.infraestructure.rest;

import com.dynamic_scheduler_automation.dy_sch_au.features.company.application.CompanyUseCase;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.exceptions.NotSuchCompanyException;
import com.dynamic_scheduler_automation.dy_sch_au.features.company.domain.model.Company;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyUseCase useCase;

    public CompanyController(CompanyUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public Page<Company> listAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String nit,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean active
    ) {
        return useCase.listCompanies(pageable, nit, name, description, active);
    }

    @GetMapping("/{id}")
    public Company getById(@PathVariable String id) {
        return useCase.getCompanyById(id).orElseThrow(() -> new NotSuchCompanyException(id));
    }

    @PostMapping
    public ResponseEntity<Company> create(@Valid @RequestBody Company company) {
        return new ResponseEntity<Company>(useCase.createCompany(company), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> update(
            @PathVariable String id,
            @Valid @RequestBody Company company
    ) {
        return ResponseEntity.ok(useCase.updateCompany(id, company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        useCase.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

}
