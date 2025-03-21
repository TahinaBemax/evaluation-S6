package site.easy.to.build.crm.importcsv;


import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.role.RoleService;
import site.easy.to.build.crm.service.user.UserProfileService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class GlobalDataGeneration {
    private final Faker faker;
    private final CustomerService customerService;
    private final CustomerLoginInfoService customerLoginInfoService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    UserProfileService userProfileService;
    @Autowired
    RoleService roleService;

    @Autowired
    public GlobalDataGeneration(CustomerService customerService, CustomerLoginInfoService customerLoginInfoService) {
        this.faker = new Faker();
        this.customerService = customerService;
        this.customerLoginInfoService = customerLoginInfoService;
    }

    public List<User> generateFakeUser(int limit){
        Set<User> UserSet = new HashSet<>();

        for (int i = 0; i < limit; i++) {
            User temp = this.prepareFakeUserInformation();
            UserSet.add(temp);
        }

        return UserSet.parallelStream().toList();
    }
    public List<Customer> generateFakeCustomer(int limit){
        Set<Customer> customerSet = new HashSet<>();

        for (int i = 0; i < limit; i++) {
            Customer temp = this.prepareFakeCustomerInformation();
            customerSet.add(temp);
        }

        return customerSet.parallelStream().toList();
    }

    public List<Ticket> generateTicket(int limit){
        Random random = new Random();
        Set<Ticket> customerSet = new HashSet<>();
        List<User> users = userService.findAll();
        List<User> managers = users
                .stream()
                .filter(u -> u.getRoles()
                        .stream().anyMatch(role -> role.getName().equals("ROLE_MANAGER"))).toList();

        List<User> employees = users
                .stream()
                .filter(u -> u.getRoles()
                        .stream().anyMatch(role -> role.getName().equals("ROLE_EMPLOYEE"))).toList();


        if (managers.size() == 0 || employees.size() == 0)
            throw new RuntimeException("Error when generating ticket because there is no manager or employee found");

        User manager = managers.get(random.nextInt(managers.size()));
        User employee = employees.get(random.nextInt(employees.size()));


        for (int i = 0; i < limit; i++) {
            Ticket temp = this.prepareTiket(manager, employee);
            customerSet.add(temp);
        }

        return customerSet.parallelStream().toList();
    }

    private Customer prepareFakeCustomerInformation(){
        //prepare customer information
        Customer fakeCustomer = new Customer();

        fakeCustomer.setDescription(faker.name().title());
        fakeCustomer.setName(faker.name().name());
        fakeCustomer.setEmail(faker.internet().emailAddress());
        fakeCustomer.setPhone(faker.phoneNumber().phoneNumber());
        fakeCustomer.setAddress(faker.address().secondaryAddress());
        fakeCustomer.setCity(faker.address().city());
        fakeCustomer.setState(faker.address().state());
        fakeCustomer.setCountry(faker.address().country());
        fakeCustomer.setFacebook(null);

        //prepare customer credentials (login, password, token)
        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();

        customerLoginInfo.setEmail(fakeCustomer.getEmail());
        customerLoginInfo.setPasswordSet(false);
        customerLoginInfo.setToken(EmailTokenUtils.generateToken());
        CustomerLoginInfo createdCustomerLoginInfo = customerLoginInfoService.save(customerLoginInfo);

        //link customer login information to this customer
        fakeCustomer.setCustomerLoginInfo(createdCustomerLoginInfo);

        return fakeCustomer;
    }
    private User prepareFakeUserInformation(){
        Random random = new Random();
        //prepare customer information
        User fakeUser = new User();

        // Set the created_at and updated_at timestamps
        LocalDateTime now = LocalDateTime.now();
        long countUsers = userService.countAllUsers();
        Role role;

        if(countUsers == 0) {
            role = roleService.findByName("ROLE_MANAGER");
            fakeUser.setStatus("active");
        } else {
            role = roleService.findByName("ROLE_EMPLOYEE");

            String[] status = new String[]{"inactive", "suspended"};
            int i = random.nextInt(0, 2);
            fakeUser.setStatus(status[i]);
        }

        fakeUser.setUsername(faker.name().username());
        fakeUser.setEmail(faker.internet().emailAddress());
        fakeUser.setPassword("fakeUser");
        fakeUser.setRoles(List.of(role));
        fakeUser.setCreatedAt(now);

        LocalDate hired_date = LocalDate.of(
                random.nextInt(1970, 2026),
                random.nextInt(1, 13),
                random.nextInt(1, 28));

        fakeUser.setHireDate(hired_date);
        fakeUser.setPasswordSet(true);
        String hashPassword = passwordEncoder.encode(fakeUser.getPassword());
        fakeUser.setPassword(hashPassword);
        User createdUser = userService.save(fakeUser);

        UserProfile profile = new UserProfile();
        profile.setUser(createdUser);
        profile.setFirstName(fakeUser.getUsername());
        userProfileService.save(profile);

        return fakeUser;
    }

    private Ticket prepareTiket(User manager, User employee){
        Random random = new Random();
        String[] status = new String[] {"open","assigned","on-hold", "in-progress","resolved","closed", "reopened","pending-customer-response","escalated","archived"};
        String[] priority = new String[] {"low","medium","high","closed","urgent","critical"};

        List<Customer> customers = (employee == null) ? customerService.findByUserId(manager.getId()) : customerService.findByUserId(employee.getId());
        if (customers == null || customers.size() == 0)
            throw new RuntimeException("Error when generating ticket because there is no customer found");

        Ticket t = new Ticket();
        t.setSubject(faker.lorem().sentence(random.nextInt(10, 51)));
        t.setDescription(faker.lorem().sentence());
        t.setStatus(status[random.nextInt(status.length)]);
        t.setPriority(priority[random.nextInt(priority.length)]);
        t.setManager(manager);
        t.setEmployee(employee);
        t.setCustomer(customers.get(random.nextInt(customers.size())));
        t.setCreatedAt(LocalDateTime.now());

        return t;
    }
}
