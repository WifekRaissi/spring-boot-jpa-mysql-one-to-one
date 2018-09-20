# spring-boot-jpa-mysql-one-to-one

Dans le tutorial précédant on a étudié le mapping de la relation One to Many https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-many. 
On continue dans ce tutorial avec la relation One to One entre la table Salarie et une nouvelle table Tache, à un moment donné chaque salarie travaille sur une seule tâche et une tâche est affectée à un seul salarié.
L'architecture de l'application est la suivante:

  ![alt text](https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-one/blob/master/src/main/resources/images/architecture.PNG) 
  
  
  Dans les tutoriaux précédants on créé les tables manuellement. Dans ce tutorial on va laisser Hibernate les créer automatiquement.
  
  ## Salarie.java
  
  ```
  
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "salarie")
public class Salarie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private static final AtomicInteger count = new AtomicInteger(0);

    @NotEmpty
    @NotNull
    private String nom;

    @NotEmpty
    @NotNull
    private String prenom;

    @NotNull
    private BigDecimal salaire;

    @NotEmpty
    @NotNull
    @Size(max = 256, message = "address should have maximum 256 characters")
    private String adresse;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "salarie")
    private Tache tache;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public BigDecimal getSalaire() {
        return salaire;
    }

    public void setSalaire(BigDecimal salaire) {
        this.salaire = salaire;
    }

    public String getAdresse() {
        return adresse;
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    @Required
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Salarie() {
    }


    @Override
    public String toString() {
        return "Salarie{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", salaire=" + salaire +
                ", adresse='" + adresse + '\'' +
                ", tache=" + tache +
                '}';
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + salaire.hashCode();
        result = 31 * result + adresse.hashCode();
        return result;
    }
}

  ```
  
  ## Tache.java
  
  ```
  
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "tache")
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private static final AtomicInteger count = new AtomicInteger(0);
    @NotEmpty
    @NotNull
    private String nom;
    @NotEmpty
    @NotNull
    private String description;

    private Timestamp delai;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salarie_id", nullable = false)
    private Salarie salarie;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Tache() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDelai() {
        return delai;
    }

    public void setDelai(Timestamp delai) {
        this.delai = delai;
    }

    @JsonIgnore
    public Salarie getSalarie() {
        return salarie;
    }

    public void setSalarie(Salarie salarie) {
        this.salarie = salarie;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", delai=" + delai +
                '}';
    }
}

  ```
  Dans une relation bidirectionnelle on ajoute l'annotation @OneToOne dans les deux entités mais on a un seul propriétaire de la relation qui est annoté par @JoinColumn.
  
  ## Repository
  
  ##   SalariesRepository.java
  ```
import com.axeane.OneToOne.model.Salarie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalariesRepository extends JpaRepository<Salarie, Long> {
    List<Salarie> findSalarieByNom(String nom);

    Salarie findSalarieById(Long id);

}
  ```
  
  ##   TacheRepository
  
  ```
  
import com.axeane.OneToOne.model.Tache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {
    List<Tache> findTacheByNom(String nom);

    Tache findTacheById(Long id);

    Page<Tache> findBySalarieId(Long salarieId, Pageable pageable);

}
  ```
  
  ##    Service
  
 ##      SalariesServiceImpl.java
  ```
import com.axeane.OneToOne.model.Salarie;
import com.axeane.OneToOne.repositories.SalariesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SalariesServiceImpl implements SalariesService {

    private final SalariesRepository salariesRepository;

    public SalariesServiceImpl(SalariesRepository salariesRepository) {
        this.salariesRepository = salariesRepository;
    }

    private Logger logger = LoggerFactory.getLogger(SalariesServiceImpl.class);

    @Override
    public void addsalarie(Salarie salarie) {
        salariesRepository.save(salarie);
    }

    @Override
    public List<Salarie> getListSalaries() {
        return salariesRepository.findAll();
    }

    @Override
    public Salarie findSalariedById(Long searchedId) {
        return salariesRepository.findSalarieById(searchedId);
    }

    @Override
    public void deleteSalaried(Long id) {
        Salarie salarie = findSalariedById(id);
        salariesRepository.delete(salarie);
    }

    @Override
    public void updateSalarie(Salarie salarie) {
        Salarie salarie1 = findSalariedById(salarie.getId());
        if (salarie1 != null) {
            salarie1.setNom(salarie.getNom());
            salarie1.setPrenom(salarie.getPrenom());
            salarie1.setAdresse(salarie.getAdresse());
            salarie1.setSalaire(salarie.getSalaire());
        }
    }
}
  ```
  
 ##     
 
 
 ```
 
import com.axeane.OneToOne.model.Tache;
import com.axeane.OneToOne.repositories.SalariesRepository;
import com.axeane.OneToOne.repositories.TacheRepository;
import com.axeane.OneToOne.utils.ExceptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TacheServiceImpl implements TacheService {


    private final TacheRepository tacheRepository;
    private final SalariesRepository salariesRepository;

    public TacheServiceImpl(TacheRepository tacheRepository, SalariesRepository salariesRepository) {
        this.tacheRepository = tacheRepository;
        this.salariesRepository = salariesRepository;
    }

    @Override
    public void addTache(Long salarieId, Tache tache) {
        salariesRepository.findById(salarieId).map(salarie -> {
            tache.setSalarie(salarie);
            return tacheRepository.save(tache);
        });
    }

    @Override
    public Page<Tache> getAllTachesBySalarietId(Long salarieId,
                                                Pageable pageable) {
        return tacheRepository.findBySalarieId(salarieId, pageable);
    }

    @Override
    public void deleteTache(Long salarieId, Long tacheId) {
        if (!salariesRepository.existsById(salarieId)) {
            throw new ExceptionResponse.ResourceNotFoundException("tacheId" + tacheId + " not found");
        }
        tacheRepository.findById(tacheId).map(tache -> {
            tacheRepository.delete(tache);
            return tache;
        });
    }

    @Override
    public void updateTache(Long salarieId, Tache tacheRequest) {
        if (!salariesRepository.existsById(salarieId)) {
            throw new ExceptionResponse.ResourceNotFoundException("salarieId" + salarieId + " not found");
        }
        tacheRepository.findById(tacheRequest.getId()).map(tache -> {
            tache.setNom(tacheRequest.getNom());
            tache.setDescription(tacheRequest.getDescription());
            tache.setDelai((Timestamp) tacheRequest.getDelai());
            return tacheRepository.save(tache);
        });
    }

    @Override
    public Tache findTacheById(Long id) {
        return tacheRepository.findTacheById(id);
    }

}
```

##    Salaries


```

import com.axeane.OneToOne.model.Salarie;
import com.axeane.OneToOne.services.SalariesService;
import com.axeane.OneToOne.utils.ExceptionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/salaries")
@Api(value = "gestion des salariés", description = "Operations pour la gestion des salariés")
public class SalariesController {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer problemObjectMapperModules() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.modules(
                new ProblemModule(),
                new ConstraintViolationProblemModule()
        );
    }
    private Logger logger = LoggerFactory.getLogger(SalariesController.class);
    private final SalariesService salariesService;

    public SalariesController(SalariesService salariesService) {
        this.salariesService = salariesService;
    }

    @ApiOperation(value = "add a new salaried")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created salaried")}
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity addSalaries(@Valid @RequestBody Salarie salarie) {
        salariesService.addsalarie(salarie);
        return new ResponseEntity<>(salarie, HttpStatus.CREATED);
    }

    @ApiOperation(value = "View a list of available salaries", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping
    public ResponseEntity getSalaries() {
        List<Salarie> salaries = salariesService.getListSalaries();
        if (salaries != null) {
            logger.info("list of salaries:" + salaries);
            return new ResponseEntity<>(salaries, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "find a salaried by its id", response = Salarie.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity getSalariesById(@PathVariable("id") long id) {
        Salarie salarie = salariesService.findSalariedById(id);
        if (salarie != null) {
            logger.info("Salaried:" + salarie);
            return new ResponseEntity<>(salarie, HttpStatus.OK);
        }
        throw new ExceptionResponse();
    }

    @ApiOperation(value = "update a salaried")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated salaried"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping
    public ResponseEntity updateSalaries(@RequestBody Salarie salarie) {
        if (salariesService.findSalariedById(salarie.getId()) != null) {
            logger.info("Salaried:" + salarie);
            salariesService.updateSalarie(salarie);
            return new ResponseEntity<>(salarie, HttpStatus.OK);
        }
        throw new ExceptionResponse();
    }

    @ApiOperation(value = "delete a salaried")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted salaried"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity deleteSalaries(@PathVariable("id") Long id) {
        Salarie salarie = salariesService.findSalariedById(id);
        if (salarie != null) {
            salariesService.deleteSalaried(id);
            logger.info("Deleted:");
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
```

##    

```

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

```
Affecter une tâche à un salarié:

  ![alt text](https://github.com/WifekRaissi/spring-boot-jpa-mysql-one-to-one/blob/master/src/main/resources/images/postTache.PNG) 
  

##  Conclusion

On a réalisé dans ce tutorial le mapping de la relation One To One et on continue par la relation Many To many dans le prochain tutorial.
https://github.com/WifekRaissi/spring-boot-jpa-mysql-many-to-many.
