package hello;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;


@Controller
public class ShortController {
    @Autowired
    ShrtUrlRep shrtUrlRep;

    public static String md5Apache(String url) {
        return md5Hex(url);
    }

    @GetMapping("/")
    public String Form(Model model) {
        model.addAttribute("short", new Short());
        return "startpage";
    }

    @GetMapping("/way")
    public String Redirect(@RequestParam String ref, Model model) {
        Optional <ShrtUrl> var = shrtUrlRep.findById(ref);
        if (var.isPresent())
            return "redirect:" + var.get().getFullUrl();
        return "error";
    }

    private UrlValidator validator = new UrlValidator();

    @PostMapping("/")
    public String shorter(@ModelAttribute Short shorter, HttpServletRequest req, Model model) {
        if (validator.isValid(shorter.getUrl())) {
            ShrtUrl su = shrtUrlRep.getByFullUrl(shorter.getUrl());
            if (su == null) {
                String par = md5Hex(shorter.getUrl()) + new Date().getTime();
                shorter.setShorter(req.getRequestURL().toString() + "way?ref=" + par);
                su = new ShrtUrl(par, shorter.getUrl());
                shrtUrlRep.save(su);
            } else
                shorter.setShorter(req.getRequestURL().toString() + "way?ref=" + su.getShortUrl());
            return "startpage";
        }
        return "invalid";
    }
}
