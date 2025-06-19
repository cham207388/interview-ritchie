# Questions

1. Write a test to check that element returned from 
   GET api/v1/equipments includes a creation date, 
   and demonstrate that the test fails

2. Write tests to check that 
    POST to api/v1/equipments 
    throws an exception 
     if (1) hours or mileage are not included in the payload, 
     and (2) model year is not included. 
     Demonstrate that the tests fail

look into: jakarta.servlet.ServletException