package kubernetes.client.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kubernetes.client.model.Storage;

@Repository
public class PVClaimsAPI {
	private static final Logger logger = LoggerFactory.getLogger(NamespacesAPI.class);

	String master = "https://k8s-master:6443/";

	Config config = new ConfigBuilder().withMasterUrl(master).build();

	public void create(Storage storage, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Create a pvc
			Quantity size = new Quantity(storage.getSize());
			System.out.println();
			PersistentVolumeClaim pvc = new PersistentVolumeClaimBuilder().withNewMetadata().withName(storage.getName())
					.endMetadata().withNewSpec().withAccessModes().addToAccessModes("ReadWriteMany")
					.withStorageClassName("").withNewResources().addToRequests("storage", size).endResources().endSpec()
					.build();
			client.persistentVolumeClaims().inNamespace(namespace).create(pvc);
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

	public void delete(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Delete a pvc
			client.persistentVolumeClaims().inNamespace(namespace).withName(name).delete();

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

	public Storage getPVClaimByName(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get a pvc
			PersistentVolumeClaim pvc = client.persistentVolumeClaims().inNamespace(namespace).withName(name).get();
			if (pvc != null) {
				Storage storage = new Storage();
				storage.setName(name);
				storage.setSize(pvc.getSpec().getResources().getRequests().get("storage").getAmount());
				storage.setStatus(pvc.getStatus().getPhase());
				storage.setCreatedAt(pvc.getMetadata().getCreationTimestamp());
				return storage;
			}
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

	public List<Storage> getPVClaims(String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Get a pvc
			List<PersistentVolumeClaim> pvcList = client.persistentVolumeClaims().inNamespace(namespace).list()
					.getItems();
			if (!pvcList.isEmpty()) {
				List<Storage> list = new ArrayList<Storage>();
				for (PersistentVolumeClaim pvc : pvcList) {
					Storage storage = new Storage();
					storage.setName(pvc.getMetadata().getName());
					storage.setSize(pvc.getSpec().getResources().getRequests().get("storage").getAmount());
					storage.setStatus(pvc.getStatus().getPhase());
					storage.setCreatedAt(pvc.getMetadata().getCreationTimestamp());
					list.add(storage);
				}
				return list;
			}

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

	public boolean exists(String name, String namespace) {
		try (final KubernetesClient client = new DefaultKubernetesClient(config)) {
			// Exists pvc
			PersistentVolumeClaim pvc = client.persistentVolumeClaims().inNamespace(namespace).withName(name).get();
			if (pvc != null) {
				return true;
			}
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