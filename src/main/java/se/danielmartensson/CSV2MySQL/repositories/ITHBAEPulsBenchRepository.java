package se.danielmartensson.CSV2MySQL.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.CSV2MySQL.entities.ITHBAEPulsBench;

@Repository
public interface ITHBAEPulsBenchRepository extends JpaRepository<ITHBAEPulsBench, Long> {

}