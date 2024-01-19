package com.sapient.PSBank.functions;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateAndTime {
    public String getDateAndTime(){
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Define the date and time format patterns
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Format the date and time according to the patterns
        String formattedDate = currentDate.format(dateFormat);
        String formattedTime = currentTime.format(timeFormat);

        // Print the formatted date and time
        return (formattedDate+" "+formattedTime);
    }
}
