package com.gmail.michzuerch.anouman.backend.session.deltaspike.jpa.facade;

import com.gmail.michzuerch.anouman.backend.entity.Buchung;
import com.gmail.michzuerch.anouman.backend.session.deltaspike.jpa.repository.BuchungDeltaspikeRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class BuchungDeltaspikeFacade {
    @Inject
    BuchungDeltaspikeRepository repo;

    public Buchung findBy(Long id) {
        return repo.findBy(id);
    }

    public void delete(Buchung val) {
        repo.attachAndRemove(val);
    }

    public Buchung save(Buchung val) {
        return repo.save(val);
    }

    public List<Buchung> findAll() {
        return repo.findAll();
    }


}