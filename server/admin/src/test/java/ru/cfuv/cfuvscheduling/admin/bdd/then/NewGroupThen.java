package ru.cfuv.cfuvscheduling.admin.bdd.then;

import io.cucumber.java.en.Then;

public class NewGroupThen {

    @Then("a group named {} has been added to the database")
    public void groupWillBeAdded(String name) {

    }

    @Then("The {} group will not be added")
    public void groupWontAdded() {

    }

}
