package com.bytser.campso.api.activities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ExampleRepository _exampleRepository;

	@Autowired
	public ActivityService(ExampleRepository exampleRepository) {
		this._exampleRepository = exampleRepository;
	}

	public void createExample(ExampleRequest exampleRequest) {
		// TODO: Throw your own ExampleFailedException (unchecked exception) when there is already a example with the given id.
		Object example = new Object(exampleRequest.data, exampleRequest.id);
		_exampleRepository.addExample(example);
	}

	public void updateExample(String id, ExampleRequest exampleRequest) {
		// TODO: Throw an own ExampleNotFoundException (custom unchecked exception) when there is no example with the given id
		Object example = _exampleRepository.getExampleById(id);
	}

}