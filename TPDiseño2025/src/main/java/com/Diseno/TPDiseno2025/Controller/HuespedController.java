package com.Diseno.TPDiseno2025.Controller;

// import isi.deso.tp_spring.util.NotFoundException;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Service.HuespedService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class HuespedController {
    
    Logger logger = org.slf4j.LoggerFactory.getLogger(HuespedController.class);

    private final HuespedService huespedservice;

    private HuespedController(final HuespedService huespedservice){
        this.huespedservice = huespedservice;
    }

    @GetMapping("/api/huespedes")
    public String getAllHuespedes(Model model) {
        List<HuespedDTO> huespedes = huespedservice.obtenerTodosDTO();
        model.addAttribute("huespedes", huespedes);
        return "huespedes";
    }
    
    @GetMapping("/api/huespedes/dni")
    public ResponseEntity<HuespedDTO> getHuespedByTipoDniAndDni(@RequestParam @NotNull final String tipodni, @RequestParam @NotNull final Integer dni) {
        return ResponseEntity.ok(huespedservice.buscarHuespedDTOByTipoDniAndDni(tipodni, dni));
    }

    @GetMapping("/api/huespedes/nombre")
    public String getHuespedesByNombre(@RequestParam @NotBlank @NotNull final String nombre, Model model) {
        List<HuespedDTO> huespedes = huespedservice.getByNombreDTO(nombre);
        model.addAttribute("huespedes", huespedes);
        return "huespedes";
    }

    @PostMapping("/api/huepedes")
    //Agregar api response
    @ResponseBody
    public ResponseEntity<Integer> createHuesped(@RequestBody @Valid HuespedDTO huespedDTO) {
        Integer createdId = huespedservice.crearHuespedDTO(huespedDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdId);
    }


    @PutMapping("/api/huespedes/{dni, tipodni}")
    // Agregar api response
    @ResponseBody
    public ResponseEntity<Integer> updateHuesped(@PathVariable(name = "dni") final Integer dni, 
                        @PathVariable(name = "tipodni") final String tipodni, @RequestParam @Valid final HuespedDTO huespedDTO) {
        huespedservice.modificarHuespedDTO(tipodni, dni, huespedDTO);
        return ResponseEntity.ok(dni);
    }

    @DeleteMapping("/api/huespedes/{dni, tipodni}")
    //agregar api response
    @ResponseBody
    public ResponseEntity<Void> deleteHuesped(@PathVariable(name = "dni") final Integer dni, @PathVariable(name = "tipodni") final String tipodni){
        huespedservice.eliminarHuespedByTipoDniAndDni(tipodni, dni);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/api/huespedes/editar")
    public String showUpdateForm(@RequestParam final Integer dni, @RequestParam final String tipodni, Model model) {
        HuespedDTO huesped = huespedservice.buscarHuespedDTOByTipoDniAndDni(tipodni, dni);
        model.addAttribute("huesped", huesped);
        return "editar-huesped";
    }

    @PostMapping("/api/huespedes/guardar")
    public String postMethodName(@ModelAttribute HuespedDTO huesped) {
        huespedservice.crearHuespedDTO(huesped);
        return "redirect:/api/huespedes";
    }

    @GetMapping("/api/huespedes/reservas/detalle")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    

    


}
