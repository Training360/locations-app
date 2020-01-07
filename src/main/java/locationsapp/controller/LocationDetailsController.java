package locationsapp.controller;

import locationsapp.service.LocationsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/server")
public class LocationDetailsController {

    private LocationsService locationsService;

    public LocationDetailsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @GetMapping("/details")
    public ModelAndView showDetails(@RequestParam long id) {
        var command =
                locationsService.getLocationById(id).orElseThrow(() -> new IllegalArgumentException("Invalid identifier: " + id));
        return new ModelAndView("location-details")
                .addObject("location", command);
    }
}
