package locationsapp.controller;

import locationsapp.dto.DeleteCommand;
import locationsapp.service.LocationsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/server")
public class DeleteLocationController {

    private LocationsService locationsService;

    public DeleteLocationController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute DeleteCommand command, RedirectAttributes redirectAttributes) {
        locationsService.deleteLocation(command.getId());
        redirectAttributes.addFlashAttribute("message", "Location has been deleted.");
        return "redirect:/server";
    }
}
