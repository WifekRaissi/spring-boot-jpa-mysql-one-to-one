package com.axeane.OneToOne.controllers;

import com.axeane.OneToOne.model.Tache;
import com.axeane.OneToOne.services.TacheService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class TacheController {

    private final TacheService tacheService;

    public TacheController(TacheService tacheService) {
        this.tacheService = tacheService;
    }


    @ApiOperation(value = "add a new task")
    @PostMapping("/salaries/{salarieId}/taches")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created task")}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createTache(@PathVariable(value = "salarieId") Long salarieId,
                                      @Valid @RequestBody Tache tache) {
        tacheService.addTache(salarieId, tache);
        return new ResponseEntity<>(tache, HttpStatus.CREATED);
    }


    @ApiOperation(value = "View a list of tasks by salaried", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @GetMapping("/salaries/{salarieId}/taches")
    public ResponseEntity getAlltachesBySalarietId(@PathVariable(value = "salarieId") Long salarieId, Pageable pageable) {
        Page<Tache> taches = tacheService.getAllTachesBySalarietId(salarieId, pageable);
        return new ResponseEntity<>(taches, HttpStatus.OK);
    }


    @ApiOperation(value = "update a task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated task"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping("/salaries/{salarieId}/taches/{tacheId}")
    public ResponseEntity updateTache(@PathVariable(value = "salarieId") Long salarieId,
                                      @Valid @RequestBody Tache tacheRequest) {
        tacheService.updateTache(salarieId, tacheRequest);
        return new ResponseEntity<>(tacheRequest, HttpStatus.OK);
    }

    @ApiOperation(value = "delete a task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted task"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/salaries/{salarieId}/taches/{tacheId}")
    public ResponseEntity deleteTache(@PathVariable(value = "salarieId") Long salarieId,
                                      @PathVariable(value = "salarieId") Long tacheId) {
        tacheService.deleteTache(salarieId, tacheId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/taches/{id}")
    public ResponseEntity findTache(@PathVariable(value = "id") Long id) {
        Tache tache = tacheService.findTacheById(id);

        return new ResponseEntity<>(tache, HttpStatus.OK);
    }
}
