package ru.cfuv.cfuvscheduling.admin.bdd.when;

import io.cucumber.java.en.When;

public class NewGroupWhen {

    @When("server got a request to add a group named {}")
    public void serverGotARequestToAddAGroupNamed(String name) {

    }

    @When("server got a request to add a group named {} and group already exists")
    public void serverGotARequestToAddGroupAndGroupAlreadyExists(String name) {

    }

}
