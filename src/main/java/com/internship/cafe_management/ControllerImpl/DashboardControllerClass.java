package com.internship.cafe_management.ControllerImpl;

import com.internship.cafe_management.Controller.DashboardController;
import com.internship.cafe_management.Service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboardControllerClass implements DashboardController {
    @Autowired
    DashboardService dashboardService;
    @Override
    public ResponseEntity<Map<String, Object>> getDetails() {
        return dashboardService.getDetails();
    }
}
