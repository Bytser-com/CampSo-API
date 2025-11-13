package com.bytser.campso.api.facilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("facilities")
public class FacilityController {

    private final Object example;

	@Autowired
	public FacilityController(Object example) {
		this.example = example;
	}

	@PostMapping
	public void createExample(@RequestBody ExampleRequest exampleRequest) {
		example.addRequest(exampleRequest);
	}

	@PutMapping("{id}")
	public void updateExample(@PathVariable String id, @RequestBody ExampleUpdatingRequest exampleRequest) {
		example.updateRequest(id, exampleRequest);
	}

    @PutMapping("{id}/set-as-disabled")
	public void updateExampleMarkAsDisabled(@PathVariable String id) {
		example.markAsDisabled(id);
	}

}
