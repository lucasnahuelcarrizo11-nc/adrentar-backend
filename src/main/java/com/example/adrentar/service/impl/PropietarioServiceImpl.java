package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Propietario;
import com.example.adrentar.repository.PropietarioRepository;
import com.example.adrentar.service.PropietarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropietarioServiceImpl implements PropietarioService {

    private PropietarioRepository propietarioRepository;

    public PropietarioServiceImpl(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    @Override
    public Propietario crearPropietario(Propietario propietario) {
        return propietarioRepository.save(propietario);
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
        propietarioActual.setContrasenia(propietario.getContrasenia());
        return propietarioRepository.save(propietarioActual);

    }

    @Override
    public void eliminarPropietario(Long idPropietario) throws Exception {
        Propietario propietarioActual = propietarioRepository.findById(idPropietario).orElseThrow(()-> new Exception("Id No encontrado"));
        propietarioRepository.deleteById(idPropietario);



    }
}
