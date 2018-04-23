package kubernetes.client.api;

import java.util.List;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.ReplicaSet;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@Repository
public class ReplicaSetAPI extends ConnectK8SConfig {

	public List<ReplicaSet> getAll(Deployment deployment) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get all ReplicaSets
			List<ReplicaSet> replicaSets = client.extensions().replicaSets().inNamespace(deployment.getMetadata().getNamespace())
					.withLabel("app", deployment.getMetadata().getLabels().get("app")).list().getItems();
			logger.info("{}: {}", "Get ReplicaSets", replicaSets);
			return replicaSets;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			Throwable[] suppressed = e.getSuppressed();
			if (suppressed != null) {
				for (Throwable t : suppressed) {
					logger.error(t.getMessage(), t);
				}
			}
		}
		return null;
	}

	public ReplicaSet get(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get a ReplicaSet
			ReplicaSet relica = client.extensions().replicaSets().inNamespace(namespace).withName(name).get();
			logger.info("{}: {}", "Get ReplicaSet", relica);
			return relica;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			Throwable[] suppressed = e.getSuppressed();
			if (suppressed != null) {
				for (Throwable t : suppressed) {
					logger.error(t.getMessage(), t);
				}
			}
		}
		return null;
	}

	public long getRevision(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get a ReplicaSet
			ReplicaSet relica = client.extensions().replicaSets().inNamespace(namespace).withName(name).get();
			logger.info("{}: {}", "Delete ReplicaSet", relica);
			return Long.parseLong(relica.getMetadata().getAnnotations().get("deployment.kubernetes.io/revision"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			Throwable[] suppressed = e.getSuppressed();
			if (suppressed != null) {
				for (Throwable t : suppressed) {
					logger.error(t.getMessage(), t);
				}
			}
		}
		return 0;
	}

	public void delete(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Delete a ReplicaSet
			logger.info("{}: {}", "Delete ReplicaSet",
					client.extensions().replicaSets().inNamespace(namespace).withName(name).delete());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			Throwable[] suppressed = e.getSuppressed();
			if (suppressed != null) {
				for (Throwable t : suppressed) {
					logger.error(t.getMessage(), t);
				}
			}
		}
	}

	public boolean exists(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			if (client.extensions().replicaSets().inNamespace(namespace).withName(name).get() != null) {
				return true;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			Throwable[] suppressed = e.getSuppressed();
			if (suppressed != null) {
				for (Throwable t : suppressed) {
					logger.error(t.getMessage(), t);
				}
			}
		}
		return false;
	}
}
