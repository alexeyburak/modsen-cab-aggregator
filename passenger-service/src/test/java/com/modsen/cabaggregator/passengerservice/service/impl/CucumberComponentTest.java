package com.modsen.cabaggregator.passengerservice.service.impl;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        snippets = CucumberOptions.SnippetType.UNDERSCORE
)
public class CucumberComponentTest {
}
