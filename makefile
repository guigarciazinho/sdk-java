release:
	mvn -DaltDeploymentRepository=repo::default::file:releases clean deploy

snapshot:
	mvn -DaltDeploymentRepository=snapshot-repo::default::file:snapshots clean deploy