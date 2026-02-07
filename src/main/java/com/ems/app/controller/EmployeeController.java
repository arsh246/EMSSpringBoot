package com.ems.app.controller;

import com.ems.app.pojo.ConfirmationForm;
import com.ems.app.pojo.Employee;
import com.ems.app.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeRepo employeeRepo;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("employees", employeeRepo.findAll());
        model.addAttribute("confirmationForm", new ConfirmationForm());
        return "index";
    }

    @PostMapping("/create")
    public String newEmployee(@ModelAttribute Employee employee) {
        // creating dynamic Employee ID
        String empId = "EMP";
        Random random = new Random();
        long randomNumber = 1000 + random.nextInt(9000);
        empId = empId + randomNumber;
        employee.setId(empId);
        // save the employee
        employeeRepo.save(employee);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee, Model model) {

        Optional<Employee> existingEmployee = employeeRepo.findById(employee.getId());

        // checking employee exist or not
        if (existingEmployee.isPresent()) {
            employeeRepo.save(employee);
        } else {
            model.addAttribute("errorMessage", "Employee with ID not found.");
        }
        return "redirect:/";
    }

    @PostMapping("/remove")
    public String removeEmployee( @ModelAttribute Employee employee) {

        Optional<Employee> existingEmployee = employeeRepo.findById(employee.getId());
        if (existingEmployee.isPresent()) {
            employeeRepo.deleteById(employee.getId());
        }
        return "redirect:/";
    }

    @PostMapping("/remove/all")
    public String removeAll(@ModelAttribute ConfirmationForm confirmationForm, Model model) {
        String confirmation = confirmationForm.getConfirmation();
        if ("Yes".equalsIgnoreCase(confirmation)) {
            employeeRepo.deleteAll();
        }

        return "redirect:/";
    }

}
