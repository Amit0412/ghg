/*package test;

import static org.junit.Assert.*;
import org.junit.Test;

public class CarLeaseRepositoryTest {

    @Test
    public void testSomeMethod() {
        // Your test logic here
    }

    // Add other test methods...
}*/
package test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import dao.ICarLeaseRepositoryImpl;
import entity.Lease;
import entity.Vehicle;
import myexceptions.CarNotFoundException;

import java.sql.SQLException;
import java.util.Date;

public class CarLeaseRepositoryTest {

    private ICarLeaseRepositoryImpl carLeaseRepository;

    @Before
    public void setUp() {
        carLeaseRepository = new ICarLeaseRepositoryImpl();
    }

    @Test
    public void testAddCar() throws CarNotFoundException {
        int carID = 1;

        // Act
        Vehicle car = new Vehicle(carID);

        // Assert
        assertNotNull(car);
        assertEquals(carID, car.getVehicleID());
    }

    @Test
    public void testCreateLease() {
        // Arrange
        int customerID = 5;
        int carID = 5;
        Date startDate = new Date();
        Date endDate = new Date();
        String leaseType = "daily";

        // Act
        Lease newLease = carLeaseRepository.createLease(customerID, carID, startDate, endDate, leaseType);

        // Assert
        assertNotNull(newLease);
        assertEquals(newLease, carLeaseRepository.findLeaseById(newLease.getLeaseID()));
    }

    @Test(expected = SQLException.class)
    public void testExceptionOnInvalidCustomerId() throws SQLException {
        // Act & Assert
        carLeaseRepository.findCustomerById(999);
    }

    @Test(expected = SQLException.class)
    public void testExceptionOnInvalidCarId() throws SQLException {
        // Act & Assert
        carLeaseRepository.findCarById(999);
    }

    @Test(expected = SQLException.class)
    public void testExceptionOnInvalidLeaseId() throws SQLException {
        // Act & Assert
        carLeaseRepository.findLeaseById(999);
    }
}

