package com.internship.cafe_management.Controller;

import com.internship.cafe_management.Entity.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/bill")
@CrossOrigin(origins = "http://localhost:4200")
public interface BillController {

    @PostMapping(path = "/generateReport")
    ResponseEntity<String> generateReport(@RequestBody Map<String,Object> requestmap);

    @GetMapping(path = "/getBills")
    ResponseEntity<List<Bill>> getBills();
}
