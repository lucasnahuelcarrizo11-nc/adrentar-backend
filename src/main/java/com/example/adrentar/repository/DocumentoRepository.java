package com.example.adrentar.repository;

import com.example.adrentar.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoRepository  extends JpaRepository<Documento, Long> {

    List<Documento> findByAlquiler_IdAlquiler(Long idAlquiler);


    Optional<Documento> findById(Long aLong);
}
