package com.internship.cafe_management.Service;


import com.internship.cafe_management.Entity.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateReport(Map<String, Object> requestmap);

    ResponseEntity<List<Bill>> getBills();
}
