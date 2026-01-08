package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Inquilino;
import com.example.adrentar.repository.InquilinoRepository;
import com.example.adrentar.service.InquilinoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InquilinoServiceImpl implements InquilinoService {

    private InquilinoRepository inquilinoRepository;

    public InquilinoServiceImpl(InquilinoRepository inquilinoRepository) {
        this.inquilinoRepository = inquilinoRepository;
    }

    @Override
    public Inquilino crearInquilino(Inquilino inquilino) {return inquilinoRepository.save(inquilino);}

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
