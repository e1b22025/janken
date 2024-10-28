package oit.is.z2386.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z2386.kaizi.janken.model.Entry;
//import oit.is.z2386.kaizi.janken.model.UserMapper;
import oit.is.z2386.kaizi.janken.model.User;
//import oit.is.z2386.kaizi.janken.model.MatchMapper;
import oit.is.z2386.kaizi.janken.model.Match;
import oit.is.z2386.kaizi.janken.model.MatchInfo;
import oit.is.z2386.kaizi.janken.model.MatchInfoMapper;
import oit.is.z2386.kaizi.janken.service.AsyncKekka;
//import oit.is.z2386.kaizi.janken.mode.janken;

//@RequestMapping("")
@Controller
public class JankenController {

  @Autowired
  private Entry entry;
  // @Autowired
  // UserMapper userMapper;

  // @Autowired
  // MatchMapper matchMapper;

  @Autowired
  MatchInfoMapper matchinfoMapper;

  @Autowired
  AsyncKekka asynckekk;

  String loginUser = "a";
  int pid = 88;

  // @PostMapping("/janken")
  // public String janken(@RequestParam String n, ModelMap model) {
  // String h = "Hi ";
  // h += n;
  // h += "!";
  // n = h;
  // model.addAttribute("n", n);
  // return "janken.html";
  // }

  @GetMapping("/janken")
  public String janken1(Principal prin, ModelMap model) {
    this.loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("entry", this.entry);
    ArrayList<Match> matches = asynckekk.syncShowMatchList();
    model.addAttribute("matches", matches);
    ArrayList<User> users = asynckekk.syncShowUserList();
    model.addAttribute("users", users);
    ArrayList<MatchInfo> matchinfos = matchinfoMapper.selecttrueActivebyMatchInfo();
    model.addAttribute("matchinfos", matchinfos);
    return "janken.html";
  }

  // CPUの手はguのみ
  @GetMapping("/jankengame")
  public String jankengame(@RequestParam String hand, ModelMap model) {
    // int phand;
    // int chand = 0;
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
    String ename = "a";
    if (id == 1) {
      ename = "CPU";
    }
    if (id == 2) {
      ename = "ほんだ";
    }
    if (id == 3) {
      ename = "いがき";
    }
    model.addAttribute("id", pid);
    model.addAttribute("ename", ename);
    return "match.html";
  }

  @GetMapping("/fight")
  public String fight(@RequestParam String hand, @RequestParam Integer id, ModelMap model) {
    String gu = "Gu";
    String pa = "Pa";
    String choki = "Choki";
    // String result = "a";

    // ArrayList<User> player = userMapper.selectNamebyUsers(this.loginUser);
    // int cpuid = id;
    // Match match = new Match();
    // MatchInfo matchinfo = new MatchInfo();

    int playerid = asynckekk.syncShowUserId(loginUser);
    MatchInfo matchInfo = new MatchInfo(playerid, id, hand, true);

    if (matchinfoMapper.checkActive(playerid, id)) {
      int targetrecode = matchinfoMapper.selectIdActive(playerid, id);
      String player2hand = matchinfoMapper.selectUser1Hand(targetrecode);
      Match match = new Match(playerid, id, hand, player2hand, true);
      asynckekk.syncInsertMatch(match);// 結果を格納する処理
      matchinfoMapper.updateActive(targetrecode);// FALSEに更新
    } else {
      matchinfoMapper.insertMatchInfo(matchInfo);
    }

    // match.setUser1(player.get(0).getId());
    // match.setUser2(cpuid);
    // match.setUser1Hand(hand);
    // match.setUser2Hand("Gu");
    // matchMapper.insertMatchesInfo(match);
    // matchinfo.setUser1(player.get(0).getId());
    // matchinfo.setUser2(cpuid);
    // matchinfo.setUser1Hand(hand);
    // matchinfo.setIsActive(true);
    // matchinfoMapper.insertMatchInfo(matchinfo);
    // model.addAttribute("hand", "あなたの手 " + hand);
    // model.addAttribute("chand", "相手の手 " + "Gu");
    // model.addAttribute("result", "結果 You " + result);
    // model.addAttribute("entry", this.entry);

    String ename = "a";
    if (id == 1) {
      ename = "CPU";
    }
    if (id == 2) {
      ename = "ほんだ";
    }
    if (id == 3) {
      ename = "いがき";
    }

    ArrayList<Match> matches = asynckekk.syncShowMatchList();
    model.addAttribute("matches", matches);
    model.addAttribute("id", id);
    model.addAttribute("ename", ename);
    model.addAttribute("id", pid);
    return "wait.html";
  }

  @GetMapping("/UPdata")
  public SseEmitter sample59() {// htmlが読み込まれた時に呼び出される。
    final SseEmitter sseEmitter = new SseEmitter();
    this.asynckekk.asyncJankenKekka(sseEmitter);
    return sseEmitter;
  }
}
