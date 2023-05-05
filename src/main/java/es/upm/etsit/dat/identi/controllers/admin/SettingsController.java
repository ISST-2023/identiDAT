package es.upm.etsit.dat.identi.controllers.admin;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.upm.etsit.dat.identi.forms.SettingForm;
import es.upm.etsit.dat.identi.persistence.model.Setting;
import es.upm.etsit.dat.identi.persistence.repository.SettingRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SettingsController {

    @Autowired
    SettingRepository stngRepo;

    @GetMapping("/admin/settings")
    public String settingsView(Model model, HttpServletRequest request) {
        model.addAttribute("settings", stngRepo.findAll());
        model.addAttribute("settingForm", new SettingForm());
        model.addAttribute("_csrf", request.getAttribute("_csrf"));

        return "admin/settings/index";
    }

    @PostMapping("/admin/settings/{id}")
    @ResponseBody
    public String settingsUpdate(@ModelAttribute("id") Integer id, @ModelAttribute("settingForm") SettingForm settingForm, Model model, HttpServletResponse response) {
        //response.setContentType("text/plain");
        if (id < 3) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }
        Setting setting;
        Optional<Setting> settingCandidate = stngRepo.findById(id);

        if (settingCandidate.isPresent())
            setting = settingCandidate.get();
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        switch (id) {
            case 3:
                if (!academicYearValidator(settingForm.getSettingValue())) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return "";
                }
                setting.setSettingValue(settingForm.getSettingValue());
                try {
                    stngRepo.saveAndFlush(setting);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return setting.toString();
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return "";
                }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return "";
    }

    private Boolean academicYearValidator(String value) {
        Pattern academicYearPattern = Pattern.compile("[2-9][0-9][0-9][0-9]-[0-9]{2}");
        Matcher academicYearMatcher = academicYearPattern.matcher(value);

        if (academicYearMatcher.matches()) {
            String[] split = value.split("-");
            return Integer.parseInt(split[0].substring(2)) == Integer.parseInt(split[1]) - 1;
        }
        return false;
    }

}
