package locationsapp.controller;

import locationsapp.entities.Location;
import locationsapp.service.LocationsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/server")
public class LocationsController {

    private LocationsService locationsService;

    public LocationsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView listLocations() {
        return new ModelAndView("locations", "page",
                locationsService.listLocations(PageRequest.of(0, 20_000, Sort.by("name"))));
    }

}
