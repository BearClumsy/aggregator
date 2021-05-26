package dplatonov.scaner.dao;

import dplatonov.scaner.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigDao extends JpaRepository<Config, Long> {
}
