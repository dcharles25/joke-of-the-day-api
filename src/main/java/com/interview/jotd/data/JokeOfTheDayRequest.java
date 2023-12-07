package com.interview.jotd.data;

import javax.validation.constraints.NotBlank;

import com.interview.jotd.data.validation.FormattedDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
public class JokeOfTheDayRequest {
    
    public JokeOfTheDayRequest(String date, String joke) {
        this.date = date;
        this.joke = joke;
    }

    public JokeOfTheDayRequest(String date, String joke, String description) {
        this.date = date;
        this.joke = joke;
        this.description = description;
    }
    
    @NotBlank(message = "The date field is required and must not be blank")
    @FormattedDate(pattern = "yyyy-MM-dd", message = "Expected date to be in the following format: yyyy-MM-dd")
    private String date;

    @NotBlank(message = "The joke field is required and must not be blank")
    private String joke;

    private String description; //Optional? Or just a string then make backend model an Optional?
}
