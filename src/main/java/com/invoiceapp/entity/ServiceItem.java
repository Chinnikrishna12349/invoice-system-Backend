package com.invoiceapp.entity;

public class ServiceItem {
    private String overtime; // Normal Days, Weekends, Holidays
    private String description;
    private String shift; // 9:00 AM – 10:00 PM, 10:00 PM – 5:00 AM
    @jakarta.validation.constraints.NotNull(message = "Hours are required")
    @jakarta.validation.constraints.Min(value = 0, message = "Hours cannot be negative")
    private Double hours;
    @jakarta.validation.constraints.NotNull(message = "Rate is required")
    @jakarta.validation.constraints.Min(value = 0, message = "Rate cannot be negative")
    private Double rate;
    private Double percentage;
    
    public ServiceItem() {}
    
    public ServiceItem(String id, String overtime, String description, String shift, Double hours, Double rate, Double percentage) {
        this.id = id;
        this.overtime = overtime;
        this.description = description;
        this.shift = shift;
        this.hours = hours;
        this.rate = rate;
        this.percentage = percentage;
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOvertime() { return overtime; }
    public void setOvertime(String overtime) { this.overtime = overtime; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    
    public Double getHours() { return hours; }
    public void setHours(Double hours) { this.hours = hours; }
    
    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }

    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    
    public Double getTotal() {
        double base = (hours != null ? hours : 0.0) * (rate != null ? rate : 0.0);
        if (percentage != null && percentage != 0) {
            return base + (base * (percentage / 100.0));
        }
        return base;
    }
}
