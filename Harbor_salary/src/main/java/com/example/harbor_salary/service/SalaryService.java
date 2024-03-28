package com.example.harbor_salary.service;

import com.example.harbor_salary.domain.Salary;
import com.example.harbor_salary.domain.SalaryCode;
import com.example.harbor_salary.dto.request.MySalaryRequest;
import com.example.harbor_salary.repository.SalaryCodeRepository;
import com.example.harbor_salary.repository.SalaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final SalaryCodeRepository salaryCodeRepository;

    public SalaryService(SalaryRepository salaryRepository, SalaryCodeRepository salaryCodeRepository) {
        this.salaryRepository = salaryRepository;
        this.salaryCodeRepository = salaryCodeRepository;
    }

    private final double NATIONAL_PENSION_RATE = 0.045; // 국민연금료 비율
    private final double HEALTH_INSURANCE_RATE = 0.03545; // 건강보험료 비율
    private final double LONG_TERM_CARE_INSURANCE_RATE = 0.004591; // 장기요양보험료 비율
    private final double EMPLOYMENT_INSURANCE_RATE = 0.009; // 고용보험료 비율


    //월급 계산
    public int calculateSalary(int baseSalaryInput) {

        int baseSalary = (int) Math.floor(baseSalaryInput / 12);
        int nationalPension = (int) Math.floor(baseSalary * NATIONAL_PENSION_RATE);
        int healthInsurance = (int) Math.floor(baseSalary * HEALTH_INSURANCE_RATE);
        int longTermCareInsurance = (int) Math.floor(baseSalary * LONG_TERM_CARE_INSURANCE_RATE);
        int employmentInsurance = (int) Math.floor(baseSalary * EMPLOYMENT_INSURANCE_RATE);
        // 공제액 합계 계산
        int totalDeductions = nationalPension + healthInsurance + longTermCareInsurance +
                employmentInsurance;
        // 실수령액 계산
        return baseSalary - totalDeductions;
    }

    //퇴직금 계산서비스
    public class RetirementService {
        public double calculateRetirement(double averageSalary, int totalWorkingDays) {
            return averageSalary * (totalWorkingDays / 365.0);
        }
    }

    //테스트 사원번호
    public int getCurrentEmployeeId() {
        return 123; // 예시 사원번호
    }
    //테스트 직위번호
    public String getCurrentPositionCode() {
        return "S11"; // 예시 사원번호
    }
    //급여 생성
    public Salary createSalary(){
        // SalaryCode 에서 baseSalary 코드를 받아오는 과정
        int employeeId = getCurrentEmployeeId();
        String positionCode = getCurrentPositionCode();
        SalaryCode salaryCodes = salaryCodeRepository.findByCodeNum(positionCode);
        int baseSalary = salaryCodes.getBaseSalary();
        int salaryGross = calculateSalary(baseSalary);
        Salary salary = Salary.builder()
                .employeeId(employeeId)
                .salaryGross(salaryGross)
                .salaryCode(salaryCodes)
                .build();
        return salaryRepository.save(salary);
    }
    //급여목록조회
    public List<MySalaryRequest> findMySalary() {
        int employeeId = getCurrentEmployeeId();

        // 현재 로그인한 사원의 급여 정보 조회
        List<Salary> salaries = salaryRepository.findByEmployeeId(employeeId);
        return salaries.stream().map(salary ->
                MySalaryRequest.builder()
                        .employeeId(salary.getEmployeeId())
                        .salaryMonthOfYear(salary.getSalaryMonthOfYear())
                        .salaryBase(salary.getSalaryGross())
                        .build()
        ).collect(Collectors.toList());
    }
}

