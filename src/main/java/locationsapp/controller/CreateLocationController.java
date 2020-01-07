package locationsapp.controller;

import locationsapp.service.LocationsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/server/create")
public class CreateLocationController {

    private LocationsService locationsService;

    public CreateLocationController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @GetMapping
    public ModelAndView createLocation() {
        return new ModelAndView("create-location", "createLocationCommand", new CreateLocationCommand());
    }

    @PostMapping
    public ModelAndView postSaveLocation(
            @Valid CreateLocationCommand createLocationCommand, BindingResult bindingResult,
            Pageable pageRequest, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("create-location");
        }

        var location = locationsService.createLocation(createLocationCommand);
        redirectAttributes.addFlashAttribute("message", "Location has been created.");
        return new ModelAndView("redirect:/server/details?id=" + location.getId());
    }
}
