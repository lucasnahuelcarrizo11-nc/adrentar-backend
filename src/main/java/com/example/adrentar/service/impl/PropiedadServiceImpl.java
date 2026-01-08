package com.example.adrentar.service.impl;

import com.example.adrentar.entity.Propiedad;
import com.example.adrentar.repository.PropiedadRepository;
import com.example.adrentar.service.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    @Autowired
private PropiedadRepository propiedadRepository;

    public PropiedadServiceImpl(PropiedadRepository propiedadRepository) {
        this.propiedadRepository = propiedadRepository;
    }

    @Override
    public Propiedad crearPropiedad(Propiedad propiedad) {
        if (propiedadRepository.existsByDireccion(propiedad.getDireccion())){
            throw new IllegalArgumentException("La direccion ya existe");
        }
        Propiedad propiedad1= propiedadRepository.save(propiedad);
        return propiedad1;
    }

    @Override
    public Propiedad buscarPorDireccion(String direccion) {
        return null;
    }

    @Override
    public List<Propiedad> listarPropiedades() {
        return propiedadRepository.findAll();
    }

    @Override
    public Optional<Propiedad> obtenerPropiedadPorId(Long id) {
        return propiedadRepository.findById(id);
    }

    @Override
    public Propiedad actualizarPropiedad(Long idPropiedad, Propiedad propiedad) {
        Optional<Propiedad> encontrada = propiedadRepository.findById(idPropiedad);
        if (!encontrada.isPresent()) {
            throw new IllegalArgumentException("Propiedad no encontrada");
        }

        Propiedad propiedad1 = encontrada.get();
        propiedad1.setDireccion(propiedad.getDireccion());
        propiedad1.setEstado(propiedad.getEstado());
        propiedad1.setAmbientes(propiedad.getAmbientes());
        propiedad1.setLatitud(propiedad.getLatitud());
        propiedad1.setLongitud(propiedad.getLongitud());

        // ✅ No tocar el propietario al actualizar, se mantiene la relación actual
        return propiedadRepository.save(propiedad1);
    }


    @Override
    public void eliminarPropiedad(Long idPropiedad) {
    Optional <Propiedad> propiedad = propiedadRepository.findById(idPropiedad);
    if (!propiedad.isPresent()) {
        throw new IllegalArgumentException("Propiedad no encontrada");
    }
    propiedadRepository.deleteById(idPropiedad);
    }

    @Override
    public List<Propiedad> listarPropiedadesPorPropietario(Long propietarioId) {
        return propiedadRepository.findByPropietarioIdUsuario(propietarioId);
    }
}
