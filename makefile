release:
	mvn -DaltDeploymentRepository=repo::default::file:releases clean deploy
	mvn -DoutputDirectory=releases/lib dependency:copy-dependencies

snapshot:
	mvn -DaltDeploymentRepository=snapshot-repo::default::file:snapshots clean deploy
	mvn -DoutputDirectory=snapshots/lib dependency:copy-dependencies
