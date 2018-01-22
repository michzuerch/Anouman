package ch.internettechnik.anouman.backend.session.deltaspike.jpa.facade;

import ch.internettechnik.anouman.backend.entity.Buchhaltung;
import ch.internettechnik.anouman.backend.entity.Kontoklasse;
import ch.internettechnik.anouman.backend.session.deltaspike.jpa.repository.KontoklasseDeltaspikeRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class KontoklasseDeltaspikeFacade {
    @Inject
    KontoklasseDeltaspikeRepository repo;

    public Kontoklasse findBy(Long id) {
        return repo.findBy(id);
    }

    public void delete(Kontoklasse val) {
        repo.attachAndRemove(val);
    }

    public Kontoklasse save(Kontoklasse val) {
        return repo.save(val);
    }

    public List<Kontoklasse> findAll() {
        return repo.findAll();
    }

    public List<Kontoklasse> findByBezeichnungLikeIgnoreCase(String bezeichnung) {
        return repo.findByBezeichnungLikeIgnoreCase(bezeichnung);
    }

    public List<Kontoklasse> findByBuchhaltung(Buchhaltung buchhaltung) {
        return repo.findByBuchhaltung(buchhaltung);
    }
}