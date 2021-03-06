package kubernetes.client.service;

import kubernetes.client.model.Project;
import kubernetes.client.model.Template;

public interface TemplateService {
	void deploy(Template template, Project project);

	boolean exists(String name, String projectName);
}
