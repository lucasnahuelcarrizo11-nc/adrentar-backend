package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Inquilino;
import com.example.adrentar.repository.InquilinoRepository;
import com.example.adrentar.service.InquilinoService;
import com.example.adrentar.service.SuscripcionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InquilinoServiceImpl implements InquilinoService {

    private final InquilinoRepository inquilinoRepository;
    private final SuscripcionService suscripcionService;

    public InquilinoServiceImpl(InquilinoRepository inquilinoRepository, SuscripcionService suscripcionService) {
        this.inquilinoRepository = inquilinoRepository;
        this.suscripcionService = suscripcionService;
    }

    @Override
    public Inquilino crearInquilino(Inquilino inquilino) {
        Inquilino guardado = inquilinoRepository.save(inquilino);
        suscripcionService.iniciarTrial(guardado); // arranca el trial de 30 días
        return guardado;
    }

    @Override
    public List<Inquilino> listarInquilinos() {return inquilinoRepository.findAll();}

    @Override
    public Optional<Inquilino> buscarInquilinoPorNombre(String nombre) {
        Optional<Inquilino> inquilino = inquilinoRepository.findByNombre(nombre);

        return inquilino;
    }

    @Override
    public Optional<Inquilino> buscarInquilinoPorId(Long idInquilino) {
        Optional<Inquilino> inquilino = inquilinoRepository.findById(idInquilino);
        return inquilino;
    }

    @Override
    public Optional<Inquilino> buscarInquilinoPorEmail(String email) {
        Optional<Inquilino> inquilino = inquilinoRepository.findByEmail(email);
        return inquilino;
    }

    @Override
    public Inquilino actualizarInquilino(Long idInquilino, Inquilino inquilino)throws Exception  {
        Inquilino inquilinoActual = inquilinoRepository.findById(idInquilino).orElseThrow(() ->new Exception("Id No encontrado"));
        inquilinoActual.setNombre(inquilino.getNombre());
        inquilinoActual.setApellido(inquilino.getApellido());
        inquilinoActual.setEmail(inquilino.getEmail());
        inquilinoActual.setContrasenia(inquilino.getContrasenia());
        return inquilinoRepository.save(inquilinoActual);
    }

    @Override
    public void eliminarInquilino(Long idInquilino) throws Exception {
        inquilinoRepository.deleteById(idInquilino);
    }
}