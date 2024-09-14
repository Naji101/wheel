// PrizeWheelController.java
package com.snipers.wheel.controller;


import com.snipers.wheel.model.Prize;
import com.snipers.wheel.model.User;
import com.snipers.wheel.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("user")  // Access user from session
public class PrizeWheelController {

    @Autowired
    private PrizeService prizeService;

    @GetMapping("/prize-wheel")
    public String showPrizeWheel(Model model, @ModelAttribute("user") User user) { // Get user data from session
        List<Prize> prizes = prizeService.getAllPrizes();
        model.addAttribute("prizes", prizes);
        model.addAttribute("user", user);  // Pass user data to the view
        return "prize-wheel";
    }

    @PostMapping("/saveWinner")
    @ResponseBody
    public String saveWinner(@RequestBody Map<String, String> requestData) {
        String name = requestData.get("name");
        String phone = requestData.get("phone");
        String location = requestData.get("location");
        String prizeName = requestData.get("prize");

        Prize winningPrize = new Prize();
        winningPrize.setName(prizeName);

        prizeService.saveWinner(name, phone, location, winningPrize);
        return "{\"status\":\"success\"}";
    }
}
