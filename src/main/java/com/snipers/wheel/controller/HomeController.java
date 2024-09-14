// HomeController.java
package com.snipers.wheel.controller;


import com.snipers.wheel.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("user")  // Add this to enable session attributes
public class HomeController {

    @GetMapping("/")
    public String showHomePage(Model model) {
        // Create a new User object to bind form data
        model.addAttribute("user", new User());
        return "home"; // Return the name of the Thymeleaf template (home.html)
    }

    @PostMapping("/submit")
    public String submitUserData(@ModelAttribute User user, Model model) {
        // Store user data in the session
        model.addAttribute("user", user);  // User data is stored in the session automatically
        System.out.println("User Data: " + user.getName() + ", " + user.getPhone() + ", " + user.getLocation());

        // Redirect to the prize wheel page
        return "redirect:/prize-wheel"; // Redirect to the /prize-wheel URL
    }
}
