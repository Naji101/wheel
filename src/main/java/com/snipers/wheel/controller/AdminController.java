// AdminController.java
package com.snipers.wheel.controller;

import com.snipers.wheel.service.PrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class AdminController {

    @Autowired
    private PrizeService prizeService;

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin"; // Return the name of the Thymeleaf template (admin.html)
    }

    @GetMapping("/admin/generate-csv")
    public ResponseEntity<InputStreamResource> generateCsv() throws IOException {
        ByteArrayInputStream csvStream = prizeService.generateCsvForWinners();
        InputStreamResource fileResource = new InputStreamResource(csvStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prize_winners.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(fileResource);
    }
}
