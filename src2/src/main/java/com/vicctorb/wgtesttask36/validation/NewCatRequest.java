package com.vicctorb.wgtesttask36.validation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class NewCatRequest {

    @NotEmpty(message = "Field 'name'cannot be empty")
    @Pattern(regexp = "^\\b[a-zA-Z]*\\b$", message = "Error in 'name' parameter value. Only one word without special symbols and digits is allowed for 'name' param\n ")
    private String name;

    @NotEmpty(message = "Field 'color'cannot be empty")
    @Pattern(regexp = "^(red|black|white|red & white|red & black & white|black & white)$", message = "Error in 'color' parameter value. Only red|black|white|red & white|red & black & white|black & white values can be setted\n'")
    private String color;


    @NotNull(message = "Field 'tail_length' cannot be null")
    @Positive(message = "Value for 'tail_length' parameter must be positive")
    private int tail_length;


    @NotNull(message = "Field 'whiskers_length' cannot be null")
    @Positive(message = "Value for 'whiskers_length' parameter must be positive")
    private int whiskers_length;

    public NewCatRequest() {
    }

    public NewCatRequest(@NotEmpty(message = "Field 'name'cannot be empty") @Pattern(regexp = "^\\b[a-zA-Z]*\\b$", message = "Error in 'name' parameter value. Only one word without special symbols and digits is allowed for 'name' param\n ") String name, @NotEmpty(message = "Field 'color'cannot be empty") @Pattern(regexp = "^(red|black|white|red & white|red & black & white|black & white)$", message = "Error in 'color' parameter value. Only red|black|white|red & white|red & black & white|black & white values can be setted\n'") String color, @NotNull(message = "Field 'tail_length' cannot be null") @Positive(message = "Value for 'tail_length' parameter must be positive") int tail_length, @NotNull(message = "Field 'whiskers_length' cannot be null") @Positive(message = "Value for 'whiskers_length' parameter must be positive") int whiskers_length) {
        this.name = name;
        this.color = color;
        this.tail_length = tail_length;
        this.whiskers_length = whiskers_length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getTail_length() {
        return tail_length;
    }

    public void setTail_length(int tail_length) {
        this.tail_length = tail_length;
    }

    public int getWhiskers_length() {
        return whiskers_length;
    }

    @Override
    public String toString() {
        return "NewCatRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", tail_length=" + tail_length +
                ", whiskers_length=" + whiskers_length +
                '}';
    }

    public void setWhiskers_length(int whiskers_length) {
        this.whiskers_length = whiskers_length;

    }
}