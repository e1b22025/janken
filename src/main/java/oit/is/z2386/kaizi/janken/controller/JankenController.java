package oit.is.z2386.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z2386.kaizi.janken.model.Entry;
import oit.is.z2386.kaizi.janken.model.UserMapper;
import oit.is.z2386.kaizi.janken.model.User;
import oit.is.z2386.kaizi.janken.model.MatchMapper;
import oit.is.z2386.kaizi.janken.model.Match;

//@RequestMapping("")
@Controller
public class JankenController {

  @Autowired
  private Entry entry;
  @Autowired
  UserMapper userMapper;

  @Autowired
  MatchMapper matchMapper;

  String loginUser;
  int pid = 88;

  @PostMapping("/janken")
  public String janken(@RequestParam String n, ModelMap model) {
    String h = "Hi ";
    h += n;
    h += "!";
    n = h;
    model.addAttribute("n", n);
    return "janken.html";
  }

  @GetMapping("/janken")
  public String janken1(Principal prin, ModelMap model) {
    this.loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);
    ArrayList<Match> matches = matchMapper.selectAllByMatches();
    model.addAttribute("matches", matches);
    ArrayList<User> users = userMapper.selectAllByUsers();
    model.addAttribute("users", users);

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
    model.addAttribute("entry", this.entry);
    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam Integer id, ModelMap model) {
    model.addAttribute("id", id);
    pid = id;
    return "match.html";
  }

  @GetMapping("/fight")
  public String fight(@RequestParam String hand, @RequestParam Integer id, ModelMap model) {
    String gu = "Gu";
    String pa = "Pa";
    String choki = "Choki";
    String result = "a";

    ArrayList<User> player = userMapper.selectNamebyUsers(this.loginUser);
    int cpuid = id;
    Match match = new Match();

    if (gu.equals(hand)) {
      result = "Draw";
    }
    if (pa.equals(hand)) {
      result = "Win!";
    }
    if (choki.equals(hand)) {
      result = "Lose";
    }

    match.setUser1(player.get(0).getId());
    match.setUser2(cpuid);
    match.setUser1Hand(hand);
    match.setUser2Hand("Gu");
    matchMapper.insertMatchesInfo(match);
    model.addAttribute("hand", "あなたの手 " + hand);
    model.addAttribute("chand", "相手の手 " + "Gu");
    model.addAttribute("result", "結果 You " + result);
    model.addAttribute("entry", this.entry);

    model.addAttribute("id", pid);
    return "match.html";
  }
}
