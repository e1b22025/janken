package oit.is.z2386.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.core.model.Model;

//@RequestMapping("")
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

  // CPUの手はguのみ
  @GetMapping("/jankengame")
  public String jankengame(@RequestParam String hand, ModelMap model) {
    int phand;
    int chand = 0;
    String gu = "Gu";
    String pa = "Pa";
    String choki = "Choki";
    String result = "a";
    if (gu.equals(hand)) {
      result = "Draw";
    }
    if (pa.equals(hand)) {
      result = "Win!";
    }
    if (choki.equals(hand)) {
      result = "Lose";
    }
    model.addAttribute("hand", "あなたの手 " + hand);
    model.addAttribute("chand", "相手の手 " + "Gu");
    model.addAttribute("result", "結果 You " + result);
    return "janken.html";
  }
}
