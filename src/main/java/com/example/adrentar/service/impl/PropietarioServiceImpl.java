package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Propietario;
import com.example.adrentar.repository.PropietarioRepository;
import com.example.adrentar.service.PropietarioService;
import com.example.adrentar.service.SuscripcionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropietarioServiceImpl implements PropietarioService {

    private final PropietarioRepository propietarioRepository;
    private final SuscripcionService suscripcionService;

    public PropietarioServiceImpl(PropietarioRepository propietarioRepository, SuscripcionService suscripcionService) {
        this.propietarioRepository = propietarioRepository;
        this.suscripcionService = suscripcionService;
    }

    @Override
    public Propietario crearPropietario(Propietario propietario) {
        Propietario guardado = propietarioRepository.save(propietario);
        suscripcionService.iniciarTrial(guardado); // arranca el trial de 30 días
        return guardado;
    }

    @Override
    public List<Propietario> mostrarPropietarios() {
        return  propietarioRepository.findAll();
    }

    @Override
    public Optional<Propietario> buscarPorNombre(String nombre) {
        Optional <Propietario> propietario = propietarioRepository.findByNombre(nombre);
        return propietario;
    }

    @Override
    public Optional<Propietario> buscarPorId(Long idPropietario) {
        Optional <Propietario> propietario = propietarioRepository.findById(idPropietario);
        return propietario;
    }

    @Override
    public Propietario actualizarPropietario(Long idPropietario, Propietario propietario) throws Exception {
        Propietario propietarioActual = propietarioRepository.findById(idPropietario).orElseThrow(()-> new Exception("Id No encontrado"));
        propietarioActual.setNombre(propietario.getNombre());
        propietarioActual.setApellido(propietario.getApellido());
        propietarioActual.setEmail(propietario.getEmail());
        propietarioActual.setDni(propietario.getDni());
        propietarioActual.setTelefono(propietario.getTelefono());
        propietarioActual.setFechaNacimiento(propietario.getFechaNacimiento());
        return propietarioRepository.save(propietarioActual);

    }
    //hola

    @Override
    public void eliminarPropietario(Long idPropietario) throws Exception {
        Propietario propietarioActual = propietarioRepository.findById(idPropietario).orElseThrow(()-> new Exception("Id No encontrado"));
        propietarioRepository.deleteById(idPropietario);
    }
}