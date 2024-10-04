package oit.is.z2386.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import ch.qos.logback.core.model.Model;
@RequestMapping("")
@Controller
public class JankenController {

  @PostMapping("/janken")
  public String janken(@RequestParam String name, ModelMap model) {
    String h = "Hi ";
    h += name;
    h += "!";
    name = h;
    model.addAttribute("name", name);
    return "janken.html";
  }

  @GetMapping("/janken")
  public String janken1() {
    return "janken.html";
  }

}
