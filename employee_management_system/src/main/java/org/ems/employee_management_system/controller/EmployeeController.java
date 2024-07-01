package org.ems.employee_management_system.controllers;

import jakarta.validation.Valid;
import org.ems.employee_management_system.entities.Employee;
import org.ems.employee_management_system.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public String getEmployeeList(Model model){
        model.addAttribute("employees", employeeRepository.findAll());
        return "index";
    }

    @GetMapping("/new-employee")
    public String createEmployee(Employee employee){
        return "create-employee";
    }

    @PostMapping("/create")
    public String createEmployee(@Valid Employee employee, BindingResult result, Model model) {
        if(result.hasErrors()){
            return "create-employee";
        }
        employee.setNew(true);
        employeeRepository.save(employee);
        return "redirect:/employees/";
    }

    @GetMapping("/edit/{id}")
    public String editTicket(@PathVariable("id") long id, Model model){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));

        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable("id") long id, @Valid Employee employee, BindingResult result, Model model){
        if(result.hasErrors()){
            employee.setId(id);
            return "edit-employee";
        }
        employee.setNew(false);
        employeeRepository.save(employee);
        return "redirect:/employees/";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") long id, Model model){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));
        employeeRepository.delete(employee);
        model.addAttribute("employees", employeeRepository.findAll());
        return "redirect:/employees/";
    }
}