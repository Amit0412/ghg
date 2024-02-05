package main;

import dao.ICarLeaseRepository;
import dao.ICarLeaseRepositoryImpl;
import entity.Vehicle;
import entity.Customer;
import entity.Lease;
import myexceptions.CarNotFoundException;
import myexceptions.CustomerNotFoundException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainModule 
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CarRen";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Ssp7001@";

    public static void main(String[] args) throws CustomerNotFoundException, CarNotFoundException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            ICarLeaseRepositoryImpl carLeaseRepository = new ICarLeaseRepositoryImpl(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Menu:");
                System.out.println("1. Customer Management");
                System.out.println("2. Car Management");
                System.out.println("3. Lease Management");
                System.out.println("4. Payment Handling");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume the newline character

                switch (choice) {
                    case 1:
                        customerManagement(carLeaseRepository, scanner);
                        break;
                    case 2:
                        carManagement(carLeaseRepository, scanner);
                        break;
                    case 3:
                        leaseManagement(carLeaseRepository, scanner);
                        break;
                    case 4:
                        paymentHandling(carLeaseRepository, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting the application.");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    private static void customerManagement(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Customer Management:");
            System.out.println("1. Add New Customer");
            System.out.println("2. Update Customer Information");
            System.out.println("3. Retrieve Customer Details");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    addNewCustomer(carLeaseRepository, scanner);
                    break;
                case 2:
                    updateCustomerInformation(carLeaseRepository, scanner);
                    break;
                case 3:
                    retrieveCustomerDetails(carLeaseRepository, scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void addNewCustomer(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) {
        //Added CustomerId
        System.out.print("Enter CustomerId: ");
        int CustomerId = scanner.nextInt();
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        Customer newCustomer = new Customer(CustomerId,firstName, lastName, email, phoneNumber);
        carLeaseRepository.addCustomer(newCustomer);
        System.out.println("New customer added successfully!");
    }

    private static void updateCustomerInformation(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer ID to Update: ");
        int customerID = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        try {
            Customer existingCustomer = carLeaseRepository.findCustomerById(customerID);
            if (existingCustomer != null) {
                System.out.print("Enter New First Name: ");
                String newFirstName = scanner.nextLine();
                System.out.print("Enter New Last Name: ");
                String newLastName = scanner.nextLine();
                System.out.print("Enter New Email: ");
                String newEmail = scanner.nextLine();
                System.out.print("Enter New Phone Number: ");
                String newPhoneNumber = scanner.nextLine();

                existingCustomer.setFirstName(newFirstName);
                existingCustomer.setLastName(newLastName);
                existingCustomer.setEmail(newEmail);
                existingCustomer.setPhoneNumber(newPhoneNumber);

                carLeaseRepository.updateCustomer(existingCustomer);
                System.out.println("Customer information updated successfully!");
            } else {
                System.out.println("Customer not found with ID: " + customerID);
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("Error updating customer information: " + e.getMessage());
        }
    }

    private static void retrieveCustomerDetails(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer ID: ");
        int customerID = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        try {
            Customer customer = carLeaseRepository.findCustomerById(customerID);
            if (customer != null) {
                System.out.println("Customer Details:");
                System.out.println("Customer ID: " + customer.getCustomerID());
                System.out.println("First Name: " + customer.getFirstName());
                System.out.println("Last Name: " + customer.getLastName());
                System.out.println("Email: " + customer.getEmail());
                System.out.println("Phone Number: " + customer.getPhoneNumber());
            } else {
                System.out.println("Customer not found with ID: " + customerID);
            }
        } catch (CustomerNotFoundException e) {
            System.out.println("Error retrieving customer details: " + e.getMessage());
        }
    }

    private static void carManagement(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Car Management:");
            System.out.println("1. Add New Car");
            System.out.println("2. Update Car Availability");
            System.out.println("3. Retrieve Car Information");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    addNewCar(carLeaseRepository, scanner);
                    break;
                case 2:
                    updateCarAvailability(carLeaseRepository, scanner);
                    break;
                case 3:
                    retrieveCarInformation(carLeaseRepository, scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void addNewCar(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) {
        System.out.print("Enter VehicleID: ");
        int vehicleID = scanner.nextInt();
        System.out.print("Enter Make: ");
        String make = scanner.nextLine();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        System.out.print("Enter Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character
        System.out.print("Enter Daily Rate: ");
        double dailyRate = scanner.nextDouble();
        scanner.nextLine();  // Consume the newline character
        System.out.print("Enter Passenger Capacity: ");
        int passengerCapacity = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character
        System.out.print("Enter Engine Capacity: ");
        int engineCapacity = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        Vehicle newCar = new Vehicle(vehicleID,make, model, year, dailyRate, "available", passengerCapacity, engineCapacity);
        carLeaseRepository.addCar(newCar);
        System.out.println("New car added successfully!");
    }

    private static void updateCarAvailability(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter Car ID to Update Availability: ");
        int carID = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        try {
            Vehicle existingCar = carLeaseRepository.findCarById(carID);
            if (existingCar != null) {
                System.out.print("Enter New Availability Status (available/notAvailable): ");
                String newAvailability = scanner.nextLine();

                existingCar.setStatus(newAvailability);
                carLeaseRepository.updateCarAvailability(existingCar);
                System.out.println("Car availability updated successfully!");
            } else {
                System.out.println("Car not found with ID: " + carID);
            }
        } catch (CarNotFoundException e) {
            System.out.println("Error updating car availability: " + e.getMessage());
        }
    }

    private static void retrieveCarInformation(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter Car ID: ");
        int carID = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        try {
            Vehicle car = carLeaseRepository.findCarById(carID);
            if (car != null) {
                System.out.println("Car Information:");
                System.out.println("Car ID: " + car.getVehicleID());
                System.out.println("Make: " + car.getMake());
                System.out.println("Model: " + car.getModel());
                System.out.println("Year: " + car.getYear());
                System.out.println("Daily Rate: " + car.getDailyRate());
                System.out.println("Availability: " + car.getStatus());
                System.out.println("Passenger Capacity: " + car.getPassengerCapacity());
                System.out.println("Engine Capacity: " + car.getEngineCapacity());
            } else {
                System.out.println("Car not found with ID: " + carID);
            }
        } catch (CarNotFoundException e) {
            System.out.println("Error retrieving car information: " + e.getMessage());
        }
    }

    private static void leaseManagement(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException, CustomerNotFoundException, CarNotFoundException {
        while (true) {
            System.out.println("Lease Management:");
            System.out.println("1. Create Lease");
            System.out.println("2. Calculate Total Cost");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    createLease(carLeaseRepository, scanner);
                    break;
                case 2:
                    calculateTotalCost(carLeaseRepository, scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void createLease(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException, CustomerNotFoundException, CarNotFoundException {
        System.out.print("Enter Customer ID: ");
        int customerID = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character
        System.out.print("Enter Car ID: ");
        int carID = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character
        System.out.print("Enter Start Date (yyyy-mm-dd): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter End Date (yyyy-mm-dd): ");
        String endDateStr = scanner.nextLine();
        System.out.print("Enter Lease Type (daily/monthly): ");
        String leaseType = scanner.nextLine();

        Date startDate = Date.valueOf(startDateStr);
        Date endDate = Date.valueOf(endDateStr);
        Lease newLease = carLeaseRepository.createLease(customerID, carID, startDate, endDate, leaseType);
        System.out.println("Lease created successfully! Lease ID: " + newLease.getLeaseID());
    }

    //private static void calculateTotalCost(Scanner scanner) {

        public static void calculateTotalCost(ICarLeaseRepository carLeaseRepository, Scanner scanner) {
            System.out.print("Enter Lease Type (daily/monthly): ");
            String leaseType = scanner.nextLine();
        
            System.out.print("Enter the Duration (in " + (leaseType.equalsIgnoreCase("daily") ? "days" : "months") + "): ");
            int duration = scanner.nextInt();
        
            try {
                double totalCost = carLeaseRepository.calculateLeaseCost(leaseType, duration);
                System.out.println("Total Cost of Lease: $" + totalCost);
            } catch (SQLException e) {
                System.out.println("Error calculating total cost: " + e.getMessage());
            }
        }
        
        /* 
        System.out.print("Enter Lease Type (daily/monthly): ");
        String leaseType = scanner.nextLine();
        System.out.print("Enter Duration (in days or months): ");
        int duration = scanner.nextInt();
      
        double totalCost = Lease.calculateTotalCost(leaseType, duration);
        //double totalCost = Lease.calculateTotalCost(leaseType, duration);
        System.out.println("Total Cost for the Lease: " + totalCost);
        */
    

    private static void paymentHandling(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Payment Handling:");
            System.out.println("1. Record Payment for Lease");
            System.out.println("2. Retrieve Payment History for Customer");
            System.out.println("3. Calculate Total Revenue from Payments");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    recordPayment(carLeaseRepository, scanner);
                    break;
                case 2:
                    retrievePaymentHistoryForCustomer(carLeaseRepository, scanner);
                    break;
                case 3:
                    calculateTotalRevenue(carLeaseRepository);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void recordPayment(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter Lease ID: ");
        int leaseID = scanner.nextInt();
        Lease lease = carLeaseRepository.getLeaseById(leaseID);

    if (lease != null) {
        System.out.print("Enter Payment Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume the newline character  

        carLeaseRepository.recordPayment(lease, amount);
        System.out.println("Payment recorded successfully!");
    } else {
        System.out.println("Lease not found with ID: " + leaseID);
    }
}
        /*scanner.nextLine();  // Consume the newline character
        System.out.print("Enter Payment Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();  // Consume the newline character

        try {
            carLeaseRepository.recordPayment(leaseID, amount);
            System.out.println("Payment recorded successfully!");
        } catch (SQLException e) {
            System.out.println("Error recording payment: " + e.getMessage());
        }
    }*/

    private static void retrievePaymentHistoryForCustomer(ICarLeaseRepositoryImpl carLeaseRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer ID: ");
        int customerID = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character

        List<Lease> paymentHistory = carLeaseRepository.retrievePaymentHistoryForCustomer(customerID);
        if (paymentHistory.isEmpty()) {
            System.out.println("No payment history found for Customer ID: " + customerID);
        } else {
            System.out.println("Payment History for Customer ID " + customerID + ":");
            for (Lease lease : paymentHistory) {
                System.out.println("Lease ID: " + lease.getLeaseID() + ", Amount: " + lease.getAmount());
            }
        }
    }

    private static void calculateTotalRevenue(ICarLeaseRepositoryImpl carLeaseRepository) throws SQLException {
        double totalRevenue = carLeaseRepository.calculateTotalRevenue();
        System.out.println("Total Revenue from Payments: " + totalRevenue);
    }

  
    
}

    


