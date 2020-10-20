package locationsapp.controller;

import locationsapp.dto.UpdateLocationCommand;
import locationsapp.service.LocationsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/server/update")
public class UpdateLocationController {

    private LocationsService locationsService;

    private ModelMapper modelMapper;

    public UpdateLocationController(LocationsService locationsService, ModelMapper modelMapper) {
        this.locationsService = locationsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ModelAndView updateLocation(@RequestParam long id) {
        var command = modelMapper.map(
                locationsService.getLocationById(id).orElseThrow(() -> new IllegalArgumentException("Invalid identifier: " + id)), UpdateLocationCommand.class);
        return new ModelAndView("update-location")
                .addObject("updateLocationCommand", command);
    }

    @PostMapping
    public ModelAndView updateLocationPost(@Valid UpdateLocationCommand command, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("update-location");
        }
        locationsService.updateLocation(command);
        redirectAttributes.addFlashAttribute("message", "Location has been updated.");
        return new ModelAndView("redirect:/server/details?id=" + command.getId());
    }
}
